package com.SocialNetwork.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SocialNetwork.dto.PostDTO;
import com.SocialNetwork.entity.Post;
import com.SocialNetwork.entity.User;
import com.SocialNetwork.repository.PostRepository;
import com.SocialNetwork.repository.ProfilePictureRepository;
import com.SocialNetwork.service.UserServiceImpl;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileController {
	 @Autowired
	    private PostRepository postRepository;

	    @Autowired
	    private UserServiceImpl userService;

	    @Autowired
	    private ProfilePictureRepository profilePictureRepository;

	    @GetMapping("/posts-with-profiles")
	    public List<PostDTO> getPostsWithProfiles() {
	        List<Post> posts = postRepository.findAll();
	        List<PostDTO> postDTOs = new ArrayList<>();

	        for (Post post : posts) {
	            PostDTO dto = new PostDTO();
	            dto.setId(post.getId());
	            dto.setContent(post.getContent());

	            User user = userService.getUserProfile(post.getUser().getId());
	            dto.setUsername(user.getUsername()); 
	            String profilePicUrl = profilePictureRepository
	                    .findTopByUserIdOrderByIdDesc(user.getId())
	                    .map(pic -> pic.getImageUrl())
	                    .orElse(null);

	            dto.setProfilePicUrl(profilePicUrl);
	            postDTOs.add(dto);
	        }

	        return postDTOs;
	    }
	}

