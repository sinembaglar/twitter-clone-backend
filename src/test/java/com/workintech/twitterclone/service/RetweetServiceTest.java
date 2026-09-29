package com.workintech.twitterclone.service;

import com.workintech.twitterclone.dto.RetweetRequest;
import com.workintech.twitterclone.entity.Retweet;
import com.workintech.twitterclone.entity.Tweet;
import com.workintech.twitterclone.entity.User;
import com.workintech.twitterclone.exception.DuplicateActionException;
import com.workintech.twitterclone.exception.UnauthorizedActionException;
import com.workintech.twitterclone.repository.RetweetRepository;
import com.workintech.twitterclone.repository.TweetRepository;
import com.workintech.twitterclone.repository.UserRepository;
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
class RetweetServiceTest {

    @Mock
    private RetweetRepository retweetRepository;

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RetweetService retweetService;

    @Test
    void retweet_dahaOnceRetweetlenmemisseKaydeder() {
        RetweetRequest request = new RetweetRequest();
        request.setTweetId(10L);

        when(retweetRepository.findByUserIdAndTweetId(1L, 10L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        Tweet tweet = new Tweet();
        tweet.setContent("Orijinal tweet");
        when(tweetRepository.findById(10L)).thenReturn(Optional.of(tweet));

        Retweet saved = new Retweet();
        saved.setId(50L);
        saved.setTweet(tweet);
        saved.setUser(new User());
        when(retweetRepository.save(org.mockito.ArgumentMatchers.any(Retweet.class))).thenReturn(saved);

        assertThatCode(() -> retweetService.retweet(request, 1L)).doesNotThrowAnyException();
    }

    @Test
    void retweet_zatenRetweetlenmisseDuplicateExceptionFirlatir() {
        RetweetRequest request = new RetweetRequest();
        request.setTweetId(10L);

        when(retweetRepository.findByUserIdAndTweetId(1L, 10L)).thenReturn(Optional.of(new Retweet()));

        assertThatThrownBy(() -> retweetService.retweet(request, 1L))
                .isInstanceOf(DuplicateActionException.class);
    }

    @Test
    void deleteRetweet_sahibiOlmayanKullaniciSilemez() {
        User owner = new User();
        owner.setId(1L);

        Retweet retweet = new Retweet();
        retweet.setId(50L);
        retweet.setUser(owner);

        when(retweetRepository.findById(50L)).thenReturn(Optional.of(retweet));

        assertThatThrownBy(() -> retweetService.deleteRetweet(50L, 2L))
                .isInstanceOf(UnauthorizedActionException.class);
    }

    @Test
    void deleteRetweet_sahibiSilebilir() {
        User owner = new User();
        owner.setId(1L);

        Retweet retweet = new Retweet();
        retweet.setId(50L);
        retweet.setUser(owner);

        when(retweetRepository.findById(50L)).thenReturn(Optional.of(retweet));

        retweetService.deleteRetweet(50L, 1L);

        verify(retweetRepository).delete(retweet);
    }
}
