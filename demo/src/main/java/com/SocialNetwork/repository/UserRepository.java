package com.SocialNetwork.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SocialNetwork.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
	Optional<User> findByEmail(String email);
	@Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<User> searchByUsernameOrEmail(@Param("keyword") String keyword);
	boolean existsByEmail(String string);
	List<User> findByUsernameContainingIgnoreCase(String keyword);
	List<User> findByRoleNot(String role);

}
