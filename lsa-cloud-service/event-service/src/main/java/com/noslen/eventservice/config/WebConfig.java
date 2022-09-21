//package com.noslen.eventservice.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Configuration
//@EnableWebMvc
//public class WebConfig implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
//        HttpServletResponse response = (HttpServletResponse) res;
//        HttpServletRequest request = (HttpServletRequest) req;
//        System.out.println("WebConfig; "+request.getRequestURI());
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,observe");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Expose-Headers", "Authorization");
//        response.addHeader("Access-Control-Expose-Headers", "responseType");
//        response.addHeader("Access-Control-Expose-Headers", "observe");
//        System.out.println("Request Method: "+request.getMethod());
//        if (!(request.getMethod().equalsIgnoreCase("OPTIONS"))) {
//            try {
//                chain.doFilter(req, res);
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("Pre-flight");
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE,PUT");
//            response.setHeader("Access-Control-Max-Age", "3600");
//            response.setHeader("Access-Control-Allow-Headers", "Access-Control-Expose-Headers"+"Authorization, content-type,"+
//                    "access-control-request-headers,access-control-request-method,accept,origin,authorization,x-requested-with,responseType,observe");
//            response.setStatus(HttpServletResponse.SC_OK);
//        }
//
//    }
//
////    @Override
////    public void configure(WebSecurity web) throws Exception {
////        web
////                .ignoring()
////                .antMatchers(HttpMethod.OPTIONS,"/**");
////        //URL you want to ignore
////    }
////
////    @Override
////    protected void configure(HttpSecurity http) throws Exception {
////
////        // Disable CSRF (cross site request forgery)
////        http.csrf().disable();
////
////        // No session will be created or used by spring security
////        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////
////        // Entry points
////        http.authorizeRequests()
////                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
////                // Disallow everything else..
////                .anyRequest().authenticated();
////
////
////        // If a user try to access a resource without having enough permissions
////        //http.exceptionHandling().accessDeniedPage("/login");
////
////        // Apply JWT
////        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
////
////        // Optional, if you want to test the API from a browser
////         http.httpBasic();
//    }
////}