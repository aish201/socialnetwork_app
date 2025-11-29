package com.SocialNetwork.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.SocialNetwork.dto.ApiResponse;
import com.SocialNetwork.dto.PostResponseDTO;
import com.SocialNetwork.entity.Complaint;
import com.SocialNetwork.entity.User;
import com.SocialNetwork.enums.ComplaintStatus;
import com.SocialNetwork.repository.CommentRepository;
import com.SocialNetwork.repository.ComplaintRepository;
import com.SocialNetwork.repository.PostRepository;
import com.SocialNetwork.repository.UserRepository;
import com.SocialNetwork.dto.CommentResponseDTO;
import com.SocialNetwork.dto.ComplaintResponseDTO;
import com.SocialNetwork.dto.ComplaintStatusDTO;
@RestController
@RequestMapping(value = "/admin", produces = "application/json")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private ComplaintRepository complaintRepository;
    // ------------------- USERS -------------------

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findByRoleNot("ROLE_ADMIN");
        return ResponseEntity.ok(Map.of("users", users));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse("User updated."));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}/password")
    public ResponseEntity<ApiResponse> resetPassword(@PathVariable Long id, @RequestParam String newPassword) {
        return userRepository.findById(id).map(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse("Password reset."));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse> changeRole(@PathVariable Long id, @RequestParam String role) {
        return userRepository.findById(id).map(user -> {
            user.setRole(role);
            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse("Role updated."));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Long id, @RequestParam boolean active) {
        return userRepository.findById(id).map(user -> {
            user.setActive(active);
            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse("User status updated."));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            user.getPosts().forEach(post -> post.getComments().clear());
            user.getPosts().clear();
            userRepository.delete(user);
            return ResponseEntity.ok(new ApiResponse("User deleted successfully."));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ------------------- POSTS -------------------

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postRepository.findAll().stream()
            .map(post -> new PostResponseDTO(
                post.getId(),
                post.getContent(),
                post.getImageUrl(),
                post.getUser() != null ? post.getUser().getUsername() : "Unknown",
                post.getCreatedAt(),
                post.getLikes()
            ))
            .toList();

        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse("Post deleted."));
        }
        return ResponseEntity.notFound().build();
    }

    // ------------------- COMMENTS -------------------
    // You can add similar endpoints for comments if needed
    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDTO>> getAllComments() {
        List<CommentResponseDTO> comments = commentRepository.findAll().stream()
            .map(comment -> new CommentResponseDTO(
                comment.getId(),
                comment.getText(), // fixed field name
                comment.getAuthor() != null ? comment.getAuthor().getUsername() : "Unknown", // fixed field name
                comment.getPost() != null ? comment.getPost().getId() : null,
                comment.getCreatedAt()
            ))
            .toList();

        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse("Comment deleted successfully."));
        }
        return ResponseEntity.notFound().build();
    }
    
 // ------------------- COMPLAINTS -------------------

    @GetMapping
    public ResponseEntity<List<ComplaintResponseDTO>> allComplaints() {
        List<Complaint> complaints = complaintRepository.findAllWithUser();

        List<ComplaintResponseDTO> dtos = complaints.stream()
            .map(c -> new ComplaintResponseDTO(
                    c.getId(),
                    c.getSubject(),
                    c.getCategory(),
                    c.getPriority().name(),
                    c.getDescription(),
                    c.getAttachmentUrl(),
                    c.getStatus(),
                    c.getCreatedAt(),
                    c.getUpdatedAt(),
                    c.getUser().getUsername(),
                    c.getUser().getEmail()
            ))
            .toList();

        return ResponseEntity.ok(dtos);
    }


    @PutMapping("/complaints/{id}/status")
    public ResponseEntity<ApiResponse> updateComplaintStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return complaintRepository.findById(id)
            .map(c -> {
                c.setStatus(ComplaintStatus.valueOf(status)); // convert string to enum
                complaintRepository.save(c);
                return ResponseEntity.ok(new ApiResponse("Complaint status updated."));
            })
            .orElse(ResponseEntity.notFound().build());
    }

 
}
