package com.SocialNetwork.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SocialNetwork.dto.UserProfileDTO;
import com.SocialNetwork.dto.UserSearchDTO;
import com.SocialNetwork.entity.ProfilePicture;
import com.SocialNetwork.entity.User;
import com.SocialNetwork.repository.ProfilePictureRepository;
import com.SocialNetwork.repository.UserRepository;
import com.SocialNetwork.security.JwtUtil;
import com.SocialNetwork.service.UserServiceImpl;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserServiceImpl userService;
  @Autowired
  private ProfilePictureRepository profilePictureRepository;
  @Autowired
  private JwtUtil jwtUtil;
  
  @PostMapping("/upload/profile-pic")
  public ResponseEntity<?> uploadProfilePic(@RequestParam("image") MultipartFile image,
                                            @RequestHeader("Authorization") String authHeader) {
      try {
    	  String imageUrl = userService.uploadProfilePic(image, authHeader);

          return ResponseEntity.ok().body(Map.of("imageUrl", imageUrl));

      } catch (Exception e) {
          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
      }
  }
  
  @GetMapping("/profile-pics")
  public ResponseEntity<?> getAllProfilePics(@RequestHeader("Authorization") String authHeader) {
      try {
          List<ProfilePicture> pictures = userService.getAllProfile(authHeader);
          return ResponseEntity.ok(pictures);
      } catch (Exception e) {
          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch profile pictures");
      }
  }

  @GetMapping("/search")
  public ResponseEntity<List<UserSearchDTO>> searchUsers(@RequestParam String keyword) {
      List<User> users = userRepository.findByUsernameContainingIgnoreCase(keyword);

      List<UserSearchDTO> result = users.stream().map(user -> {
          UserSearchDTO dto = new UserSearchDTO();
          dto.setUsername(user.getUsername());
          dto.setEmail(user.getEmail());
       

          profilePictureRepository.findTopByUserIdOrderByIdDesc(user.getId())
              .ifPresentOrElse(
                  pic -> dto.setProfilePicUrl(pic.getImageUrl()),
                  () -> dto.setProfilePicUrl("http://localhost:8080/assets/default_profile.png")
              );

          return dto;
      }).collect(Collectors.toList());

      return ResponseEntity.ok(result);
  }

//  @GetMapping("/{email}")
//  public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
//      return userRepository.findByEmail(email)
//              .map(ResponseEntity::ok)
//              .orElse(ResponseEntity.notFound().build());
//  }
  @GetMapping("/{email}")
  public ResponseEntity<UserProfileDTO> getUserByEmail(@PathVariable String email) {
      User user = userRepository.findByEmail(email)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));

      UserProfileDTO dto = new UserProfileDTO();
      dto.setUsername(user.getUsername());
      dto.setEmail(user.getEmail());
      dto.setBio(user.getBio()); // <- ensure this is included

      // Load latest profile picture
      profilePictureRepository.findTopByUserIdOrderByIdDesc(user.getId())
          .ifPresentOrElse(
              pic -> dto.setProfilePicUrl(pic.getImageUrl()),
              () -> dto.setProfilePicUrl("http://localhost:8080/assets/default_profile.png")
          );

      return ResponseEntity.ok(dto);
  }

  @GetMapping("/{email}/profile-pictures")
  public ResponseEntity<List<ProfilePicture>> getProfilePicsByEmail(@PathVariable String email) {
      List<ProfilePicture> pics = profilePictureRepository.findByUserEmailOrderByIdAsc(email);
      return ResponseEntity.ok(pics);
  }
//  @PutMapping("/{email}/bio")
//  public ResponseEntity<?> updateBio(
//          @PathVariable String email,
//          @RequestBody Map<String, String> payload,
//          @RequestHeader("Authorization") String authHeader
//  ) {
//      try {
//          // Extract token and email from token
//          String token = authHeader.substring(7);
//          String tokenEmail = jwtUtil.extractUsername(token);
//
//          // Validate that the email in path matches email in token
//          if (!tokenEmail.equals(email)) {
//              return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to update this bio.");
//          }
//
//          String bio = payload.get("bio");
//          User user = userRepository.findByEmail(email)
//                  .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//          user.setBio(bio);
//          userRepository.save(user);
//
//          return ResponseEntity.ok("Bio updated successfully");
//
//      } catch (Exception e) {
//          e.printStackTrace();
//          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update bio.");
//      }
//  }

//  @PutMapping("/{email}/bio")
//  public ResponseEntity<?> updateBio(
//          @PathVariable String email,
//          @RequestBody Map<String, String> payload,
//          @RequestHeader("Authorization") String authHeader) {
//      try {
//          String bio = payload.get("bio");
//          userService.updateBio(bio, authHeader);
//          return ResponseEntity.ok("Bio updated successfully.");
//      } catch (Exception e) {
//          e.printStackTrace();
//          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update bio.");
//      }
//  }


//  @PutMapping("/bio")
//  public ResponseEntity<?> updateBio(@RequestParam String bio, @RequestHeader("Authorization") String authHeader) {
//      try {
//          userService.updateBio(bio, authHeader);
//          return ResponseEntity.ok("Bio updated successfully.");
//      } catch (Exception e) {
//          e.printStackTrace();
//          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update bio.");
//      }
//  }
//  @PutMapping("/bio")
//  public ResponseEntity<?> updateBio(@RequestBody Map<String, String> payload,
//                                     @RequestHeader("Authorization") String authHeader) {
//      try {
//          String bio = payload.get("bio");
//          userService.updateBio(bio, authHeader);
//          return ResponseEntity.ok("Bio updated successfully.");
//      } catch (Exception e) {
//          e.printStackTrace();
//          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update bio.");
//      }
//  }
//  @GetMapping("/profile")
//  public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String authHeader) {
//      User user = userService.getCurrentUser(authHeader);
//      return ResponseEntity.ok(user);
//  }
  @GetMapping("/current")
  public ResponseEntity<UserProfileDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
      User user = userService.getCurrentUser(authHeader);
      UserProfileDTO dto = new UserProfileDTO();
      dto.setUsername(user.getUsername());
      dto.setEmail(user.getEmail());

      profilePictureRepository.findTopByUserIdOrderByIdDesc(user.getId())
          .ifPresentOrElse(
              pic -> dto.setProfilePicUrl(pic.getImageUrl()),
              () -> dto.setProfilePicUrl("http://localhost:8080/assets/default_profile.png")
          );

      dto.setBio(user.getBio());
      return ResponseEntity.ok(dto);
  }

  @PutMapping("/updateBio")
  public ResponseEntity<String> updateBio(@RequestHeader("Authorization") String authHeader,
                                          @RequestBody Map<String, String> payload) {
      String bio = payload.get("bio");
      userService.updateBio(bio, authHeader);
      return ResponseEntity.ok("Bio updated successfully");
  }

  @GetMapping("/bio")
  public ResponseEntity<?> getBio(@RequestHeader("Authorization") String authHeader) {
      try {
          String bio = userService.getBio(authHeader);
          return ResponseEntity.ok(bio);
      } catch (Exception e) {
          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch bio.");
      }
  }


}

