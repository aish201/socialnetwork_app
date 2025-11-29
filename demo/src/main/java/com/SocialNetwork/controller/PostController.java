package com.SocialNetwork.controller;

import com.SocialNetwork.dto.CommentDTO;
import com.SocialNetwork.dto.PostDTO;
import com.SocialNetwork.entity.Comment;
import com.SocialNetwork.entity.Notification;
import com.SocialNetwork.entity.Post;
import com.SocialNetwork.entity.PostLike;
import com.SocialNetwork.entity.User;
import com.SocialNetwork.repository.CommentRepository;
import com.SocialNetwork.repository.PostLikeRepository;
import com.SocialNetwork.repository.PostRepository;
import com.SocialNetwork.repository.UserRepository;
import com.SocialNetwork.repository.ProfilePictureRepository;
import com.SocialNetwork.security.JwtUtil;
import com.SocialNetwork.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfilePictureRepository profilePictureRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NotificationService notificationService;

    // --- Fetch all posts ---
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(@RequestHeader("Authorization") String authHeader) {
        String currentUserEmail = jwtUtil.extractUsername(authHeader.substring(7));
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<PostDTO> dtos = posts.stream()
                                  .map(post -> convertToDTO(post, currentUserEmail))
                                  .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // --- Fetch posts of a user ---
    @GetMapping("/myposts")
    public ResponseEntity<List<PostDTO>> getUserPosts(@RequestParam String email,
                                                      @RequestHeader("Authorization") String authHeader) {
        String currentUserEmail = jwtUtil.extractUsername(authHeader.substring(7));
        List<Post> posts = postRepository.findAllByUserEmailWithComments(email);
        List<PostDTO> dtos = posts.stream()
                                  .map(post -> convertToDTO(post, currentUserEmail))
                                  .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // --- Create new post ---
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestHeader("Authorization") String authHeader,
                                              @RequestBody PostDTO postDTO) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found at email: " + email));

        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setImageUrl(postDTO.getImageUrl());
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setLikes(0);

        Post savedPost = postRepository.save(post);
        PostDTO responseDto = convertToDTO(savedPost, email);
        return ResponseEntity.ok(responseDto);
    }

    // --- Like post & send notification ---
    @PutMapping("/{id}/like")
    public ResponseEntity<?> likePost(@PathVariable Long id,
                                      @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) return ResponseEntity.status(404).body("User not found");

        User sender = optionalUser.get();

        return postRepository.findById(id).map(post -> {
            // Check if already liked
            boolean alreadyLiked = postLikeRepository.existsByPostAndUser(post, sender);
            if (!alreadyLiked) {
                post.setLikes(post.getLikes() + 1);
                postRepository.save(post);

                // Save PostLike
                PostLike postLike = new PostLike(post, sender);
                postLikeRepository.save(postLike);

                // Send notification if not self
                if (!sender.getId().equals(post.getUser().getId())) {
                    Notification notification = new Notification();
                    notification.setType("LIKE");
                    notification.setMessage(sender.getUsername() + " liked your post");
                    notification.setPostId(post.getId());
                    notification.setSenderId(sender.getId());
                    notification.setSenderUsername(sender.getUsername());
                    notification.setSenderProfilePicUrl(sender.getProfilePictureUrl());
                    notification.setRecipientEmail(post.getUser().getEmail());
                    notification.setRecipientUsername(post.getUser().getUsername());
                    notification.setCreatedAt(LocalDateTime.now());
                    notificationService.createAndSend(notification);
                }
            }
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- Unlike post ---
    @PutMapping("/{id}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable Long id,
                                        @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) return ResponseEntity.status(404).body("User not found");

        User sender = optionalUser.get();

        return postRepository.findById(id).map(post -> {
            Optional<PostLike> likeOpt = postLikeRepository.findByPostAndUser(post, sender);
            if (likeOpt.isPresent()) {
                post.setLikes(post.getLikes() - 1);
                postRepository.save(post);
                postLikeRepository.delete(likeOpt.get());
            }
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- Add comment & send notification ---
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long id,
                                                 @RequestHeader("Authorization") String authHeader,
                                                 @RequestBody Comment comment) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        Post post = postRepository.findById(id).orElse(null);
        if (post == null) return ResponseEntity.notFound().build();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        comment.setAuthor(user);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        // Notification
        if (!user.getId().equals(post.getUser().getId())) {
            Notification notification = new Notification();
            notification.setType("COMMENT");
            notification.setMessage(user.getUsername() + " commented on your post");
            notification.setPostId(post.getId());
            notification.setSenderId(user.getId());
            notification.setSenderUsername(user.getUsername());
            notification.setSenderProfilePicUrl(user.getProfilePictureUrl());
            notification.setRecipientEmail(post.getUser().getEmail());
            notification.setRecipientUsername(post.getUser().getUsername());
            notification.setCreatedAt(LocalDateTime.now());
            notificationService.createAndSend(notification);
        }

        CommentDTO dto = new CommentDTO();
        dto.setId(savedComment.getId());
        dto.setText(savedComment.getText());
        dto.setAuthorUsername(user.getUsername());
        dto.setCreatedAt(savedComment.getCreatedAt());
        dto.setProfilePicUrl(user.getProfilePictureUrl());

        return ResponseEntity.ok(dto);
    }

    // --- Delete post ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Post deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    // --- Upload post image ---
    @PostMapping("/upload/post-image")
    public ResponseEntity<?> uploadPostImage(@RequestParam("image") MultipartFile image) {
        try {
            String uploadDir = "uploads/post-images/";
            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
            Path filePath = Paths.get(uploadDir + fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "http://localhost:8080/" + uploadDir + fileName;
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload failed");
        }
    }

    // --- Convert Post to PostDTO with liked info ---
    private PostDTO convertToDTO(Post post, String currentUserEmail) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setLikes(post.getLikes());
        dto.setUsername(post.getUser().getUsername());
        dto.setImageUrl(post.getImageUrl());

        profilePictureRepository.findTopByUserIdOrderByIdDesc(post.getUser().getId())
                .ifPresent(pic -> dto.setProfilePicUrl(pic.getImageUrl()));

        // Set liked
        Optional<User> currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser.isPresent()) {
            boolean liked = postLikeRepository.existsByPostAndUser(post, currentUser.get());
            dto.setLiked(liked);
        } else {
            dto.setLiked(false);
        }

        // Comments
        List<CommentDTO> commentDTOs = post.getComments().stream().map(comment -> {
            CommentDTO dtoC = new CommentDTO();
            dtoC.setId(comment.getId());
            dtoC.setText(comment.getText());
            dtoC.setAuthorUsername(comment.getAuthor().getUsername());
            dtoC.setCreatedAt(comment.getCreatedAt());
            profilePictureRepository.findTopByUserIdOrderByIdDesc(comment.getAuthor().getId())
                    .ifPresent(pic -> dtoC.setProfilePicUrl(pic.getImageUrl()));
            return dtoC;
        }).collect(Collectors.toList());

        dto.setComments(commentDTOs);
        return dto;
    }
}
