package com.workintech.twitterclone.service;

import com.workintech.twitterclone.dto.TweetRequest;
import com.workintech.twitterclone.dto.TweetResponse;
import com.workintech.twitterclone.entity.Tweet;
import com.workintech.twitterclone.entity.User;
import com.workintech.twitterclone.exception.ResourceNotFoundException;
import com.workintech.twitterclone.exception.UnauthorizedActionException;
import com.workintech.twitterclone.repository.TweetRepository;
import com.workintech.twitterclone.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public TweetResponse createTweet(TweetRequest request, Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanici bulunamadi: " + currentUserId));

        Tweet tweet = new Tweet();
        tweet.setContent(request.getContent());
        tweet.setUser(user);

        Tweet savedTweet = tweetRepository.save(tweet);
        return toResponse(savedTweet);
    }

    public List<TweetResponse> findByUserId(Long userId) {
        return tweetRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public TweetResponse findById(Long tweetId) {
        Tweet tweet = getTweetOrThrow(tweetId);
        return toResponse(tweet);
    }

    public TweetResponse updateTweet(Long tweetId, TweetRequest request, Long currentUserId) {
        Tweet tweet = getTweetOrThrow(tweetId);

        if (!tweet.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedActionException("Bu tweeti guncelleme yetkiniz yok");
        }

        tweet.setContent(request.getContent());
        Tweet updatedTweet = tweetRepository.save(tweet);
        return toResponse(updatedTweet);
    }

    public void deleteTweet(Long tweetId, Long currentUserId) {
        Tweet tweet = getTweetOrThrow(tweetId);

        if (!tweet.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedActionException("Bu tweeti silme yetkiniz yok, sadece tweet sahibi silebilir");
        }

        tweetRepository.delete(tweet);
    }

    private Tweet getTweetOrThrow(Long tweetId) {
        return tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet bulunamadi: " + tweetId));
    }

    private TweetResponse toResponse(Tweet tweet) {
        return new TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUser().getId(),
                tweet.getUser().getUsername()
        );
    }
}
