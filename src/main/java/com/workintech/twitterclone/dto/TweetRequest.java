package com.workintech.twitterclone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TweetRequest {

    @NotBlank(message = "Tweet icerigi bos olamaz")
    @Size(max = 280, message = "Tweet en fazla 280 karakter olabilir")
    private String content;
}
