//package com.noslen.lsaauthservice;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//
//@Configuration
//@EnableAuthorizationServer
//public class OAuthConfig extends AuthorizationServerConfigurerAdapter {
//
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    public OAuthConfig(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//
//        clients.inMemory()
//                .withClient("html5")
//                .authorizedGrantTypes("password")
//                .scopes("ROLE_USER", "ROLE_TEACHER", "ROLE_ADMIN")
//                .secret("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");
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.authenticationManager(this.authenticationManager);
//    }
//}