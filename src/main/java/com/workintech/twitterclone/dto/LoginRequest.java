package com.workintech.twitterclone.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Kullanici adi bos olamaz")
    private String username;

    @NotBlank(message = "Sifre bos olamaz")
    private String password;
}
