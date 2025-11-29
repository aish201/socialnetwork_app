package com.SocialNetwork.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SocialNetwork.entity.ProfilePicture;
import com.SocialNetwork.entity.User;
import com.SocialNetwork.repository.ProfilePictureRepository;
import com.SocialNetwork.repository.UserRepository;
import com.SocialNetwork.security.JwtUtil;
@Service
public class UserServiceImpl {
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private ProfilePictureRepository picRepo;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ImageUploadService uploadService;
	
	public User getCurrentUser(String authHeader) {
		String token = authHeader.substring(7);
		String email = jwtUtil.extractUsername(token);
		User user = userRepository.findByEmail(email).orElseThrow();
		return user;
	}
	public String uploadProfilePic(MultipartFile image, String authHeader) throws IOException {
		User user = getCurrentUser(authHeader);
		String imageUrl = uploadService.saveImage(image, "profile");
		ProfilePicture pic = new ProfilePicture();
		pic.setImageUrl(imageUrl);
		pic.setUser(user);
		picRepo.save(pic);
		return imageUrl;
	}
	
	public List<ProfilePicture> getAllProfile( String authHeader){
		User user = getCurrentUser(authHeader);
		List<ProfilePicture> byUser = picRepo.findByUser(user);
		return byUser;
	}
	 public User getUserProfile(Long userId) {
	        return userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found"));
	    }

	    public User getUserProfile(String email) {
	        return userRepository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("User not found"));
	    }
	    public void updateBio(String bio, String authHeader) {
	        String token = authHeader.substring(7);
	        String email = jwtUtil.extractUsername(token);
	        User user = userRepository.findByEmail(email).orElseThrow();
	        user.setBio(bio);
	        userRepository.save(user);
	    }

	    public String getBio(String authHeader) {
	        String token = authHeader.substring(7);
	        String email = jwtUtil.extractUsername(token);
	        User user = userRepository.findByEmail(email).orElseThrow();
	        return user.getBio();
	    }

}
