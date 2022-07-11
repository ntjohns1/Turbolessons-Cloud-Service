package com.noslen.lsaadminservice.util.auth;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.noslen.lsaadminservice.model.LSAUser;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

public class BCryptPasswordDeserializer extends JsonDeserializer<String> {

    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        PasswordEncoder encoder = LSAUser.getEncoder();
        String encodedPassword = encoder.encode(node.asText());
        return encodedPassword;
    }
}
