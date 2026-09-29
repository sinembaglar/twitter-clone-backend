package com.workintech.twitterclone.repository;

import com.workintech.twitterclone.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {

    Optional<Retweet> findByUserIdAndTweetId(Long userId, Long tweetId);
}
