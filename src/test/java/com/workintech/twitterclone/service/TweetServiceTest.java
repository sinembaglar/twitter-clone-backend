package com.workintech.twitterclone.service;

import com.workintech.twitterclone.dto.TweetRequest;
import com.workintech.twitterclone.dto.TweetResponse;
import com.workintech.twitterclone.entity.Tweet;
import com.workintech.twitterclone.entity.User;
import com.workintech.twitterclone.exception.ResourceNotFoundException;
import com.workintech.twitterclone.exception.UnauthorizedActionException;
import com.workintech.twitterclone.repository.TweetRepository;
import com.workintech.twitterclone.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TweetService tweetService;

    private User owner;
    private Tweet tweet;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setUsername("sinem");
        owner.setEmail("sinem@example.com");
        owner.setPassword("hashed-password");

        tweet = new Tweet();
        tweet.setId(10L);
        tweet.setContent("Ilk tweetim");
        tweet.setUser(owner);
    }

    @Test
    void createTweet_kullaniciVarsaTweetiOlusturmali() {
        TweetRequest request = new TweetRequest();
        request.setContent("Yeni tweet");

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);

        TweetResponse response = tweetService.createTweet(request, 1L);

        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("sinem");
        verify(tweetRepository).save(any(Tweet.class));
    }

    @Test
    void findById_tweetYoksaResourceNotFoundFirlatmali() {
        when(tweetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tweetService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateTweet_sahibiOlmayanKullaniciGuncelleyemez() {
        TweetRequest request = new TweetRequest();
        request.setContent("Guncellenmis icerik");

        when(tweetRepository.findById(10L)).thenReturn(Optional.of(tweet));

        assertThatThrownBy(() -> tweetService.updateTweet(10L, request, 2L))
                .isInstanceOf(UnauthorizedActionException.class);
    }

    @Test
    void deleteTweet_sahibiSilebilir() {
        when(tweetRepository.findById(10L)).thenReturn(Optional.of(tweet));

        tweetService.deleteTweet(10L, 1L);

        verify(tweetRepository).delete(tweet);
    }

    @Test
    void deleteTweet_sahibiOlmayanKullaniciSilemez() {
        when(tweetRepository.findById(10L)).thenReturn(Optional.of(tweet));

        assertThatThrownBy(() -> tweetService.deleteTweet(10L, 2L))
                .isInstanceOf(UnauthorizedActionException.class);
    }
}
