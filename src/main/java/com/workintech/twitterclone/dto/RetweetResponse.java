package com.workintech.twitterclone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RetweetResponse {

    private Long id;
    private Long tweetId;
    private String tweetContent;
    private Long userId;
    private String username;
}
