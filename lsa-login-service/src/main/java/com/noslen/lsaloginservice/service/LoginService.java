package com.noslen.lsaloginservice.service;

import com.noslen.lsaloginservice.requestDTO.LoginRequestDTO;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

     String login(LoginRequestDTO requestDTO, HttpServletRequest request);

}
