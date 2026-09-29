package com.workintech.twitterclone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TweetResponse {

    private Long id;
    private String content;
    private Long userId;
    private String username;
}
