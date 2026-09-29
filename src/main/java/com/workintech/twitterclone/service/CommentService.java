package com.workintech.twitterclone.service;

import com.workintech.twitterclone.dto.CommentRequest;
import com.workintech.twitterclone.dto.CommentResponse;
import com.workintech.twitterclone.entity.Comment;
import com.workintech.twitterclone.entity.Tweet;
import com.workintech.twitterclone.entity.User;
import com.workintech.twitterclone.exception.ResourceNotFoundException;
import com.workintech.twitterclone.exception.UnauthorizedActionException;
import com.workintech.twitterclone.repository.CommentRepository;
import com.workintech.twitterclone.repository.TweetRepository;
import com.workintech.twitterclone.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, TweetRepository tweetRepository,
                           UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public CommentResponse createComment(CommentRequest request, Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanici bulunamadi: " + currentUserId));
        Tweet tweet = tweetRepository.findById(request.getTweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Tweet bulunamadi: " + request.getTweetId()));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment.setTweet(tweet);

        Comment savedComment = commentRepository.save(comment);
        return toResponse(savedComment);
    }

    public CommentResponse updateComment(Long commentId, CommentRequest request, Long currentUserId) {
        Comment comment = getCommentOrThrow(commentId);

        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedActionException("Bu yorumu guncelleme yetkiniz yok, sadece yorum sahibi guncelleyebilir");
        }

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return toResponse(updatedComment);
    }

    public void deleteComment(Long commentId, Long currentUserId) {
        Comment comment = getCommentOrThrow(commentId);

        boolean isCommentOwner = comment.getUser().getId().equals(currentUserId);
        boolean isTweetOwner = comment.getTweet().getUser().getId().equals(currentUserId);

        if (!isCommentOwner && !isTweetOwner) {
            throw new UnauthorizedActionException(
                    "Bu yorumu silme yetkiniz yok, sadece tweet sahibi veya yorum sahibi silebilir");
        }

        commentRepository.delete(comment);
    }

    private Comment getCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Yorum bulunamadi: " + commentId));
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getTweet().getId()
        );
    }
}
