package com.workintech.twitterclone.service;

import com.workintech.twitterclone.dto.RetweetRequest;
import com.workintech.twitterclone.dto.RetweetResponse;
import com.workintech.twitterclone.entity.Retweet;
import com.workintech.twitterclone.entity.Tweet;
import com.workintech.twitterclone.entity.User;
import com.workintech.twitterclone.exception.DuplicateActionException;
import com.workintech.twitterclone.exception.ResourceNotFoundException;
import com.workintech.twitterclone.exception.UnauthorizedActionException;
import com.workintech.twitterclone.repository.RetweetRepository;
import com.workintech.twitterclone.repository.TweetRepository;
import com.workintech.twitterclone.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RetweetService {

    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public RetweetService(RetweetRepository retweetRepository, TweetRepository tweetRepository,
                           UserRepository userRepository) {
        this.retweetRepository = retweetRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public RetweetResponse retweet(RetweetRequest request, Long currentUserId) {
        if (retweetRepository.findByUserIdAndTweetId(currentUserId, request.getTweetId()).isPresent()) {
            throw new DuplicateActionException("Bu tweeti zaten retweetlediniz");
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanici bulunamadi: " + currentUserId));
        Tweet tweet = tweetRepository.findById(request.getTweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Tweet bulunamadi: " + request.getTweetId()));

        Retweet retweet = new Retweet();
        retweet.setUser(user);
        retweet.setTweet(tweet);

        Retweet savedRetweet = retweetRepository.save(retweet);
        return toResponse(savedRetweet);
    }

    public void deleteRetweet(Long retweetId, Long currentUserId) {
        Retweet retweet = retweetRepository.findById(retweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Retweet bulunamadi: " + retweetId));

        if (!retweet.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedActionException("Bu retweeti silme yetkiniz yok, sadece retweet sahibi silebilir");
        }

        retweetRepository.delete(retweet);
    }

    private RetweetResponse toResponse(Retweet retweet) {
        return new RetweetResponse(
                retweet.getId(),
                retweet.getTweet().getId(),
                retweet.getTweet().getContent(),
                retweet.getUser().getId(),
                retweet.getUser().getUsername()
        );
    }
}
