package com.SocialNetwork.service;


import com.SocialNetwork.entity.Notification;
import com.SocialNetwork.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Notification createAndSend(Notification notification) {
        Notification saved = notificationRepository.save(notification);

        // Real-time send to user's /user/queue/notifications
        // Note: destination must match frontend subscribe path (/user/queue/notifications)
        messagingTemplate.convertAndSendToUser(
                notification.getRecipientEmail(),
                "/queue/notifications",
                mapToDto(saved)
        );
        return saved;
    }

    public List<Notification> getNotificationsFor(String recipientEmail) {
        return notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(recipientEmail);
    }

    public Optional<Notification> markRead(Long id) {
        Optional<Notification> n = notificationRepository.findById(id);
        n.ifPresent(nt -> {
            nt.setReadStatus(true);
            notificationRepository.save(nt);
        });
        return n;
    }

    public void markAllRead(String recipientEmail) {
        var list = notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(recipientEmail);
        list.forEach(n -> n.setReadStatus(true));
        notificationRepository.saveAll(list);
    }

    public long countUnread(String recipientEmail) {
        return notificationRepository.countByRecipientEmailAndReadStatusFalse(recipientEmail);
    }

    // Simple DTO mapping to send lighter payload in STOMP message
    private Notification mapToDto(Notification n) {
        // For now we just return same entity (JPA entity is okay), or create a separate DTO class
        return n;
    }
}