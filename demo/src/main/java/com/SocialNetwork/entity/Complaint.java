package com.SocialNetwork.entity;

import java.time.LocalDateTime;

import com.SocialNetwork.enums.ComplaintPriority;
import com.SocialNetwork.enums.ComplaintStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "complaints")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Complaint {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String subject;

	    private String category;          // e.g. Bug, Abuse, Payment etc.

	    @Enumerated(EnumType.STRING)
	    private ComplaintPriority priority = ComplaintPriority.LOW;

	    @Column(length = 2000)
	    private String description;

	    private String attachmentUrl;     // optional

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id")
	    private User user;                // who submitted

	    @Enumerated(EnumType.STRING)
	    private ComplaintStatus status = ComplaintStatus.PENDING;

	    private LocalDateTime createdAt = LocalDateTime.now();
	    private LocalDateTime updatedAt = LocalDateTime.now();

	    @PreUpdate
	    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }

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

		public ComplaintPriority getPriority() {
			return priority;
		}

		public void setPriority(ComplaintPriority priority) {
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

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
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
	    
}
