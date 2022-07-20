package com.noslen.lsaadminservice.util.auth;


import com.noslen.lsaadminservice.model.LSAUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static PasswordEncoder getEncoder() {
        return encoder;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    private String encodedPassword;

    public PasswordUtil(String pwd) {
       this.encodedPassword =  PasswordUtil.encoder.encode(pwd);
    }
}

