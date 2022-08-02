package com.noslen.lsaloginservice.responseDTO;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO implements Serializable {

    private String token;
}
