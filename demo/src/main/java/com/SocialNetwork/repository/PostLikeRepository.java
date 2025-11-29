package com.SocialNetwork.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SocialNetwork.entity.Post;
import com.SocialNetwork.entity.PostLike;
import com.SocialNetwork.entity.User;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
	    boolean existsByPostAndUser(Post post, User user);
	    Optional<PostLike> findByPostAndUser(Post post, User user);
	}


