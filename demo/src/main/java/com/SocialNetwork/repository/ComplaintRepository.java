package com.SocialNetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SocialNetwork.entity.Complaint;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // For logged-in user’s own complaints
    List<Complaint> findByUserEmailOrderByCreatedAtDesc(String email);

    // For admin — load all complaints with user info
    @Query("SELECT c FROM Complaint c JOIN FETCH c.user")
    List<Complaint> findAllWithUser();
}
