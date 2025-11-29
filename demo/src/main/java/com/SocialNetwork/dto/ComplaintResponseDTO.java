package com.SocialNetwork.dto;

import java.time.LocalDateTime;

import com.SocialNetwork.enums.ComplaintStatus;

public class ComplaintResponseDTO {
	private Long id;
    private String subject;
    private String category;
    private String priority;
    private String description;
    private String attachmentUrl;
    private ComplaintStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String username;
    private String email;
	public ComplaintResponseDTO(Long id, String subject, String category, String priority, String description,
			String attachmentUrl, ComplaintStatus status, LocalDateTime createdAt, LocalDateTime updatedAt,
			String username, String email) {
		super();
		this.id = id;
		this.subject = subject;
		this.category = category;
		this.priority = priority;
		this.description = description;
		this.attachmentUrl = attachmentUrl;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.username = username;
		this.email = email;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAttachmentUrl() {
		return attachmentUrl;
	}
	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}
	public ComplaintStatus getStatus() {
		return status;
	}
	public void setStatus(ComplaintStatus status) {
		this.status = status;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
