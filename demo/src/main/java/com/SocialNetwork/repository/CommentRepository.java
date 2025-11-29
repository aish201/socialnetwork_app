package com.SocialNetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
