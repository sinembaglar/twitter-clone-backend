package com.workintech.twitterclone.controller;

import com.workintech.twitterclone.dto.LikeRequest;
import com.workintech.twitterclone.security.CustomUserDetails;
import com.workintech.twitterclone.service.LikeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like")
    public ResponseEntity<Void> likeTweet(@Valid @RequestBody LikeRequest request,
                                           @AuthenticationPrincipal CustomUserDetails currentUser) {
        likeService.likeTweet(request, currentUser.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> dislikeTweet(@Valid @RequestBody LikeRequest request,
                                              @AuthenticationPrincipal CustomUserDetails currentUser) {
        likeService.dislikeTweet(request, currentUser.getUserId());
        return ResponseEntity.noContent().build();
    }
}
