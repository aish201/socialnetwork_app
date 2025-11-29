package com.SocialNetwork.controller;
import com.SocialNetwork.entity.Notification;
import com.SocialNetwork.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

import java.util.Map;
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Fetch notifications for logged-in user via Principal (set by Spring security)
    @GetMapping("/my")
    public ResponseEntity<List<Notification>> getMyNotifications(Principal principal) {
        String email = principal.getName();
        List<Notification> list = notificationService.getNotificationsFor(email);
        return ResponseEntity.ok(list);
    }

    // Mark single notification as read
    @PutMapping("/{id}/mark-read")
    public ResponseEntity<?> markRead(@PathVariable Long id) {
        return notificationService.markRead(id)
                .map(n -> ResponseEntity.ok(Map.of("message", "marked")))
                .orElse(ResponseEntity.notFound().build());
    }

    // Mark all notifications for current user as read
    @PutMapping("/mark-all")
    public ResponseEntity<?> markAllRead(Principal principal) {
        notificationService.markAllRead(principal.getName());
        return ResponseEntity.ok(Map.of("message", "all marked"));
    }

    // Count unread
    @GetMapping("/count-unread")
    public ResponseEntity<?> countUnread(Principal principal) {
        long c = notificationService.countUnread(principal.getName());
        return ResponseEntity.ok(Map.of("unread", c));
    }

    // (Optional) manual send endpoint (for testing)
    @PostMapping("/send")
    public ResponseEntity<Notification> sendTest(@RequestBody Notification n) {
        Notification saved = notificationService.createAndSend(n);
        return ResponseEntity.ok(saved);
    }
}