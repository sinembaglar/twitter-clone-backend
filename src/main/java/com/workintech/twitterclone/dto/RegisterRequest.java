package com.workintech.twitterclone.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Kullanici adi bos olamaz")
    @Size(min = 3, max = 30, message = "Kullanici adi 3-30 karakter arasinda olmalidir")
    private String username;

    @NotBlank(message = "Email bos olamaz")
    @Email(message = "Gecerli bir email adresi giriniz")
    private String email;

    @NotBlank(message = "Sifre bos olamaz")
    @Size(min = 6, message = "Sifre en az 6 karakter olmalidir")
    private String password;
}
