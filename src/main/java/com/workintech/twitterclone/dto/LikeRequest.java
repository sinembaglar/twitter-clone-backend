package com.workintech.twitterclone.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeRequest {

    @NotNull(message = "Tweet id bos olamaz")
    private Long tweetId;
}
