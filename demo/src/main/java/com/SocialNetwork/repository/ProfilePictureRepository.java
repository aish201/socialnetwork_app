package com.SocialNetwork.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SocialNetwork.entity.ProfilePicture;
import com.SocialNetwork.entity.User;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
	List<ProfilePicture> findByUser(User user);
    Optional<ProfilePicture> findByIdAndUser(Long id, User user);
    Optional<ProfilePicture> findTopByUserIdOrderByIdDesc(Long userId);
    List<ProfilePicture> findByUserEmailOrderByIdAsc(String email);
}
