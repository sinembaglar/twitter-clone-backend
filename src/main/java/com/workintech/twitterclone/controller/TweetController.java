package com.workintech.twitterclone.controller;

import com.workintech.twitterclone.dto.TweetRequest;
import com.workintech.twitterclone.dto.TweetResponse;
import com.workintech.twitterclone.security.CustomUserDetails;
import com.workintech.twitterclone.service.TweetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tweet")
public class TweetController {

    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping
    public ResponseEntity<TweetResponse> createTweet(@Valid @RequestBody TweetRequest request,
                                                       @AuthenticationPrincipal CustomUserDetails currentUser) {
        TweetResponse response = tweetService.createTweet(request, currentUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/findByUserId")
    public ResponseEntity<List<TweetResponse>> findByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(tweetService.findByUserId(userId));
    }

    @GetMapping("/findById")
    public ResponseEntity<TweetResponse> findById(@RequestParam Long id) {
        return ResponseEntity.ok(tweetService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetResponse> updateTweet(@PathVariable Long id,
                                                       @Valid @RequestBody TweetRequest request,
                                                       @AuthenticationPrincipal CustomUserDetails currentUser) {
        TweetResponse response = tweetService.updateTweet(id, request, currentUser.getUserId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id,
                                             @AuthenticationPrincipal CustomUserDetails currentUser) {
        tweetService.deleteTweet(id, currentUser.getUserId());
        return ResponseEntity.noContent().build();
    }
}
