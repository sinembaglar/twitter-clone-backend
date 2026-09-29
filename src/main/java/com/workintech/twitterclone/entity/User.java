package com.workintech.twitterclone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Kullanici adi bos olamaz")
    @Size(min = 3, max = 30, message = "Kullanici adi 3-30 karakter arasinda olmalidir")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Email bos olamaz")
    @Email(message = "Gecerli bir email adresi giriniz")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Sifre bos olamaz")
    @Size(min = 6, message = "Sifre en az 6 karakter olmalidir")
    @Column(nullable = false)
    private String password;
}
