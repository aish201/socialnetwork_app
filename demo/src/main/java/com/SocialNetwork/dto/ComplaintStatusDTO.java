package com.SocialNetwork.dto;

import com.SocialNetwork.enums.ComplaintStatus;

public class ComplaintStatusDTO {
	 private Long id;
	 private ComplaintStatus status;

	 public ComplaintStatusDTO(Long id, ComplaintStatus status) {
	        this.id = id;
	        this.status = status;
	  }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ComplaintStatus getStatus() {
		return status;
	}

	public void setStatus(ComplaintStatus status) {
		this.status = status;
	}
	 
	    
}
