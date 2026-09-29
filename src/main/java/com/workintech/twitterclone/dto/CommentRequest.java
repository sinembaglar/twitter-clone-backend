package com.workintech.twitterclone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    @NotBlank(message = "Yorum icerigi bos olamaz")
    @Size(max = 280, message = "Yorum en fazla 280 karakter olabilir")
    private String content;

    @NotNull(message = "Tweet id bos olamaz")
    private Long tweetId;
}
