package com.workintech.twitterclone.service;

import com.workintech.twitterclone.entity.Comment;
import com.workintech.twitterclone.entity.Tweet;
import com.workintech.twitterclone.entity.User;
import com.workintech.twitterclone.exception.UnauthorizedActionException;
import com.workintech.twitterclone.repository.CommentRepository;
import com.workintech.twitterclone.repository.TweetRepository;
import com.workintech.twitterclone.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    private User tweetOwner;
    private User commentOwner;
    private User unrelatedUser;
    private Comment comment;

    @BeforeEach
    void setUp() {
        tweetOwner = new User();
        tweetOwner.setId(1L);
        tweetOwner.setUsername("sinem");

        commentOwner = new User();
        commentOwner.setId(2L);
        commentOwner.setUsername("testuser1");

        unrelatedUser = new User();
        unrelatedUser.setId(3L);
        unrelatedUser.setUsername("testuser2");

        Tweet tweet = new Tweet();
        tweet.setId(10L);
        tweet.setContent("Sinem'in tweeti");
        tweet.setUser(tweetOwner);

        comment = new Comment();
        comment.setId(100L);
        comment.setContent("Guzel tweet");
        comment.setUser(commentOwner);
        comment.setTweet(tweet);
    }

    @Test
    void deleteComment_yorumSahibiSilebilir() {
        when(commentRepository.findById(100L)).thenReturn(Optional.of(comment));

        assertThatCode(() -> commentService.deleteComment(100L, 2L)).doesNotThrowAnyException();
        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteComment_tweetSahibiDeSilebilir() {
        when(commentRepository.findById(100L)).thenReturn(Optional.of(comment));

        assertThatCode(() -> commentService.deleteComment(100L, 1L)).doesNotThrowAnyException();
        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteComment_alakasizKullaniciSilemez() {
        when(commentRepository.findById(100L)).thenReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.deleteComment(100L, 3L))
                .isInstanceOf(UnauthorizedActionException.class);
    }
}
