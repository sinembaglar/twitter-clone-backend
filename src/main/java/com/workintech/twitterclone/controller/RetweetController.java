package com.workintech.twitterclone.controller;

import com.workintech.twitterclone.dto.RetweetRequest;
import com.workintech.twitterclone.dto.RetweetResponse;
import com.workintech.twitterclone.security.CustomUserDetails;
import com.workintech.twitterclone.service.RetweetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retweet")
public class RetweetController {

    private final RetweetService retweetService;

    public RetweetController(RetweetService retweetService) {
        this.retweetService = retweetService;
    }

    @PostMapping
    public ResponseEntity<RetweetResponse> retweet(@Valid @RequestBody RetweetRequest request,
                                                     @AuthenticationPrincipal CustomUserDetails currentUser) {
        RetweetResponse response = retweetService.retweet(request, currentUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRetweet(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        retweetService.deleteRetweet(id, currentUser.getUserId());
        return ResponseEntity.noContent().build();
    }
}
