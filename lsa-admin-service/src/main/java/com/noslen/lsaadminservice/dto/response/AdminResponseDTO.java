package com.noslen.lsaadminservice.dto.response;

import lombok.*;

import java.io.Serializable;

//@Getter
//@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDTO  implements Serializable{

    private String emailAddress;

    private Long id;

    private String password;

    private Character status;

    private Integer loginAttempt;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Integer getLoginAttempt() {
        return loginAttempt;
    }

    public void setLoginAttempt(Integer loginAttempt) {
        this.loginAttempt = loginAttempt;
    }
}
