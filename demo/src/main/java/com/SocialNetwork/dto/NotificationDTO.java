package com.SocialNetwork.dto;

import java.time.LocalDateTime;

public class NotificationDTO {
	private String type; // LIKE or COMMENT
    private String message;
    private Long postId;
    private String sender;
    private Long senderId;
    private String senderProfilePicUrl;
    private String recipient;
    private LocalDateTime timestamp;
    private boolean read;

    public NotificationDTO(String type, String message, Long postId, String sender, Long senderId,
                           String senderProfilePicUrl, String recipient, LocalDateTime timestamp) {
        this.type = type;
        this.message = message;
        this.postId = postId;
        this.sender = sender;
        this.senderId = senderId;
        this.senderProfilePicUrl = senderProfilePicUrl;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.read = false;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public String getSenderProfilePicUrl() {
		return senderProfilePicUrl;
	}

	public void setSenderProfilePicUrl(String senderProfilePicUrl) {
		this.senderProfilePicUrl = senderProfilePicUrl;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
    
}

