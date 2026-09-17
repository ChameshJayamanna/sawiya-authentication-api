package com.sawiya.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigninResponseDTO {

    private boolean success;
    private String message;
}