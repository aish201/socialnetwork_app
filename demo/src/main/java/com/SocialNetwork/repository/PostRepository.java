package com.SocialNetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  
////    // Get posts by user, ordered by date
//    List<Post> findByUserEmailOrderByCreatedAtDesc(String email);
	@Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.user.email = :email ORDER BY p.createdAt DESC")
	List<Post> findAllByUserEmailWithComments(@Param("email") String email);

    // Get all posts with their comments (LEFT JOIN FETCH for eager loading)
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.comments ORDER BY p.createdAt DESC")
    List<Post> findAllWithComments();
}
