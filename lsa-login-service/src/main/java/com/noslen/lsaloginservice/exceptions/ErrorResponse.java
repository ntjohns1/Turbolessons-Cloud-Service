package com.noslen.lsaloginservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements Serializable {

    private String errorMsg;

    private String developerMsg;

    private HttpStatus responseStatus;

    private int responseCode;

//    public void setErrorMsg(String errorMsg) {
//        this.errorMsg = errorMsg;
//    }
//
//    public void setDeveloperMsg(String developerMsg) {
//        this.developerMsg = developerMsg;
//    }
//
//    public void setResponseStatus(HttpStatus responseStatus) {
//        this.responseStatus = responseStatus;
//    }
//
//    public void setResponseCode(int responseCode) {
//        this.responseCode = responseCode;
//    }
}



