package com.SocialNetwork.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.SocialNetwork.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientEmailOrderByCreatedAtDesc(String recipientEmail);
    long countByRecipientEmailAndReadStatusFalse(String recipientEmail);
}