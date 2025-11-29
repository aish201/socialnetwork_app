package com.SocialNetwork.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SocialNetwork.dto.UserSearchDTO;
import com.SocialNetwork.entity.RecentSearch;
import com.SocialNetwork.entity.User;
import com.SocialNetwork.repository.RecentSearchRepository;
import com.SocialNetwork.repository.UserRepository;
import com.SocialNetwork.security.JwtUtil;

@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "http://localhost:4200")
public class SearchController {
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private RecentSearchRepository recentSearchRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/record")
    public ResponseEntity<?> recordSearch(@RequestHeader("Authorization") String authHeader,
                                          @RequestParam String searchedEmail) {
        String token = authHeader.substring(7);
        String searcherEmail = jwtUtil.extractUsername(token);

        User searcher = userRepository.findByEmail(searcherEmail).orElseThrow();
        User searched = userRepository.findByEmail(searchedEmail).orElseThrow();

        // Check if a record already exists
        Optional<RecentSearch> existing = recentSearchRepository.findBySearcherAndSearchedUser(searcher, searched);
        
        if (existing.isPresent()) {
            // Update timestamp
            RecentSearch rs = existing.get();
            rs.setSearchedAt(LocalDateTime.now());
            recentSearchRepository.save(rs);
        } else {
            // Create new
            RecentSearch rs = new RecentSearch();
            rs.setSearcher(searcher);
            rs.setSearchedUser(searched);
            rs.setSearchedAt(LocalDateTime.now());
            recentSearchRepository.save(rs);
        }

        return ResponseEntity.ok("Recorded");
    }

    @GetMapping("/recent")
    public ResponseEntity<List<UserSearchDTO>> getRecentSearches(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        User searcher = userRepository.findByEmail(email).orElseThrow();

        List<RecentSearch> recent = recentSearchRepository.findTop5BySearcherOrderBySearchedAtDesc(searcher);

        List<UserSearchDTO> dtoList = recent.stream().map(rs -> {
            User searchedUser = rs.getSearchedUser();

            UserSearchDTO dto = new UserSearchDTO();
            dto.setUsername(searchedUser.getUsername());
            dto.setEmail(searchedUser.getEmail());
           

            // Set profile picture if available
            searchedUser.getProfilePictures().stream()
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .findFirst()
                .ifPresent(pic -> dto.setProfilePicUrl(pic.getImageUrl()));

            return dto;
        }).toList();

        return ResponseEntity.ok(dtoList);
    }

}



