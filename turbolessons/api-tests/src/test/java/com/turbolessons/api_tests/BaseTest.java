package com.turbolessons.api_tests;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public abstract class BaseTest {

    protected RequestSpecification requestSpec;
    
    @Value("${spring.security.oauth2.client.provider.okta.token-uri}")
    private String tokenUri;
    
    @Value("${spring.security.oauth2.client.registration.okta.client-id}")
    private String clientId;
    
    @Value("${spring.security.oauth2.client.registration.okta.client-secret}")
    private String clientSecret;

    private KeyPair keyPair;

    @BeforeAll
    public void setup() throws Exception {
        // Generate RSA key pair for DPoP
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        keyPair = keyPairGenerator.generateKeyPair();

        // Obtain a valid JWT from Okta
        String accessToken = obtainAccessToken();

        // Set up a default request specification that adds the Authorization header
        requestSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .setContentType(ContentType.JSON)
                .build();

        // Set this as the default specification for all REST Assured requests
        RestAssured.requestSpecification = requestSpec;
    }

    private String createDPoPProofToken(String nonce) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        
        // Create JWK from public key
        Map<String, String> jwk = Map.of(
            "kty", "RSA",
            "e", Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getPublicExponent().toByteArray()),
            "n", Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getModulus().toByteArray()),
            "alg", "RS256",
            "use", "sig"
        );

        // Create DPoP proof JWT
        return Jwts.builder()
                .setHeaderParam("typ", "dpop+jwt")
                .setHeaderParam("alg", "RS256")
                .setHeaderParam("jwk", jwk)
                .claim("htm", "POST")
                .claim("htu", tokenUri)
                .claim("iat", Date.from(Instant.now()))
                .claim("jti", UUID.randomUUID().toString())
                .claim("nonce", nonce) // Add nonce if provided
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    private String obtainAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "test_client");
        
        // First attempt without nonce
        try {
            headers.add("DPoP", createDPoPProofToken(null));
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            restTemplate.exchange(tokenUri, HttpMethod.POST, request, OktaTokenResponse.class);
        } catch (HttpClientErrorException.BadRequest e) {
            // Get nonce from response header
            HttpHeaders responseHeaders = e.getResponseHeaders();
            if (responseHeaders != null) {
                String nonce = responseHeaders.getFirst("dpop-nonce");
                if (nonce != null) {
                    // Retry with nonce
                    headers.set("DPoP", createDPoPProofToken(nonce));
                    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
                    ResponseEntity<OktaTokenResponse> response = restTemplate.exchange(
                        tokenUri,
                        HttpMethod.POST,
                        request,
                        OktaTokenResponse.class
                    );
                    
                    OktaTokenResponse tokenResponse = response.getBody();
                    if (tokenResponse == null) {
                        throw new RuntimeException("Failed to obtain access token from Okta: Empty response body");
                    }
                    
                    return tokenResponse.getAccessToken();
                }
            }
            throw e;
        }
        
        throw new RuntimeException("Failed to obtain access token from Okta: No nonce received");
    }
    
    private static class OktaTokenResponse {
        private String access_token;
        
        public String getAccessToken() {
            return access_token;
        }
    }
}