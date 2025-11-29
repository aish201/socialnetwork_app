package com.SocialNetwork.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import com.SocialNetwork.dto.ComplaintResponseDTO;
import com.SocialNetwork.dto.ComplaintStatusDTO;
import com.SocialNetwork.entity.Complaint;
import com.SocialNetwork.entity.User;
import com.SocialNetwork.enums.ComplaintStatus;
import com.SocialNetwork.repository.ComplaintRepository;
import com.SocialNetwork.repository.UserRepository;
import com.SocialNetwork.security.JwtUtil;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "http://localhost:4200")
public class ComplaintController {

    @Autowired 
    private ComplaintRepository complaintRepository;
    
    @Autowired 
    private UserRepository userRepository;
    
    @Autowired 
    private JwtUtil jwtUtil;
    
    // ✅ 1) Submit complaint (user)
    @PostMapping
    public ResponseEntity<ComplaintResponseDTO> submitComplaint(
            @RequestHeader("Authorization") String auth,
            @RequestBody Complaint payload) {

        String email = jwtUtil.extractUsername(auth.substring(7));
        User user = userRepository.findByEmail(email).orElseThrow();

        Complaint c = new Complaint();
        c.setUser(user);
        c.setSubject(payload.getSubject());
        c.setCategory(payload.getCategory());
        c.setPriority(payload.getPriority());
        c.setDescription(payload.getDescription());
        c.setAttachmentUrl(payload.getAttachmentUrl());
        c.setStatus(ComplaintStatus.PENDING);
        c.setCreatedAt(LocalDateTime.now());

        Complaint saved = complaintRepository.save(c);

        ComplaintResponseDTO dto = new ComplaintResponseDTO(
                saved.getId(),
                saved.getSubject(),
                saved.getCategory(),
                saved.getPriority().name(),
                saved.getDescription(),
                saved.getAttachmentUrl(),
                saved.getStatus(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getUser().getUsername(),
                saved.getUser().getEmail()
        );

        return ResponseEntity.ok(dto);
    }

    // ✅ 2) Upload file (optional)
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        try {
            String dir = "uploads/complaints/";
            File folder = new File(dir);
            if (!folder.exists()) folder.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path path = Paths.get(dir + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            String url = "http://localhost:8080/" + dir + fileName;
            return ResponseEntity.ok(Map.of("url", url));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Upload failed"));
        }
    }

    // ✅ 3) User — view their own complaints
    @GetMapping("/me")
    public ResponseEntity<List<ComplaintResponseDTO>> myComplaints(@RequestHeader("Authorization") String auth) {
        String email = jwtUtil.extractUsername(auth.substring(7));
        List<Complaint> complaints = complaintRepository.findByUserEmailOrderByCreatedAtDesc(email);

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

    // ✅ 4) Admin — view all complaints with user info
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

    // ✅ 5) Admin — update complaint status
    @PutMapping("/{id}/status")
    public ResponseEntity<ComplaintStatusDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        return complaintRepository.findById(id).map(c -> {
            String status = body.get("status");
            c.setStatus(ComplaintStatus.valueOf(status));
            c.setUpdatedAt(LocalDateTime.now());
            complaintRepository.save(c);

            return ResponseEntity.ok(new ComplaintStatusDTO(c.getId(), c.getStatus()));
        }).orElse(ResponseEntity.notFound().build());
    }
}
