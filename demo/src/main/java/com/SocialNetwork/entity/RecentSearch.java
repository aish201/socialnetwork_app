package com.SocialNetwork.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class RecentSearch {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    private User searcher; // The user who searched

	    @ManyToOne
	    private User searchedUser; // The user being searched

	    private LocalDateTime searchedAt;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public User getSearcher() {
			return searcher;
		}

		public void setSearcher(User searcher) {
			this.searcher = searcher;
		}

		public User getSearchedUser() {
			return searchedUser;
		}

		public void setSearchedUser(User searchedUser) {
			this.searchedUser = searchedUser;
		}

		public LocalDateTime getSearchedAt() {
			return searchedAt;
		}

		public void setSearchedAt(LocalDateTime searchedAt) {
			this.searchedAt = searchedAt;
		}
	    
}
