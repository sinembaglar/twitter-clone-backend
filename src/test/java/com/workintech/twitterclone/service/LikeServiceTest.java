package com.workintech.twitterclone.service;

import com.workintech.twitterclone.dto.LikeRequest;
import com.workintech.twitterclone.entity.Like;
import com.workintech.twitterclone.entity.Tweet;
import com.workintech.twitterclone.entity.User;
import com.workintech.twitterclone.exception.DuplicateActionException;
import com.workintech.twitterclone.exception.ResourceNotFoundException;
import com.workintech.twitterclone.repository.LikeRepository;
import com.workintech.twitterclone.repository.TweetRepository;
import com.workintech.twitterclone.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LikeService likeService;

    @Test
    void likeTweet_dahaOncelikeAtilmamissaKaydeder() {
        LikeRequest request = new LikeRequest();
        request.setTweetId(10L);

        when(likeRepository.existsByUserIdAndTweetId(1L, 10L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(tweetRepository.findById(10L)).thenReturn(Optional.of(new Tweet()));

        likeService.likeTweet(request, 1L);

        verify(likeRepository).save(any(Like.class));
    }

    @Test
    void likeTweet_zatenLikeAtilmissaDuplicateExceptionFirlatir() {
        LikeRequest request = new LikeRequest();
        request.setTweetId(10L);

        when(likeRepository.existsByUserIdAndTweetId(1L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> likeService.likeTweet(request, 1L))
                .isInstanceOf(DuplicateActionException.class);
    }

    @Test
    void dislikeTweet_likeYoksaResourceNotFoundFirlatir() {
        LikeRequest request = new LikeRequest();
        request.setTweetId(10L);

        when(likeRepository.findByUserIdAndTweetId(1L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> likeService.dislikeTweet(request, 1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
