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
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, TweetRepository tweetRepository,
                        UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public void likeTweet(LikeRequest request, Long currentUserId) {
        if (likeRepository.existsByUserIdAndTweetId(currentUserId, request.getTweetId())) {
            throw new DuplicateActionException("Bu tweete zaten like attiniz");
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanici bulunamadi: " + currentUserId));
        Tweet tweet = tweetRepository.findById(request.getTweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Tweet bulunamadi: " + request.getTweetId()));

        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);
        likeRepository.save(like);
    }

    public void dislikeTweet(LikeRequest request, Long currentUserId) {
        Like like = likeRepository.findByUserIdAndTweetId(currentUserId, request.getTweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Bu tweete atilmis bir like bulunamadi"));

        likeRepository.delete(like);
    }
}
