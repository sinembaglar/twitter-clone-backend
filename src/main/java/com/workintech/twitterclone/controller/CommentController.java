package com.workintech.twitterclone.controller;

import com.workintech.twitterclone.dto.CommentRequest;
import com.workintech.twitterclone.dto.CommentResponse;
import com.workintech.twitterclone.security.CustomUserDetails;
import com.workintech.twitterclone.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentRequest request,
                                                           @AuthenticationPrincipal CustomUserDetails currentUser) {
        CommentResponse response = commentService.createComment(request, currentUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id,
                                                           @Valid @RequestBody CommentRequest request,
                                                           @AuthenticationPrincipal CustomUserDetails currentUser) {
        CommentResponse response = commentService.updateComment(id, request, currentUser.getUserId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        commentService.deleteComment(id, currentUser.getUserId());
        return ResponseEntity.noContent().build();
    }
}
