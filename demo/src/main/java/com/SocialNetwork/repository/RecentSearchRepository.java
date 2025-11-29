package com.SocialNetwork.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SocialNetwork.entity.RecentSearch;
import com.SocialNetwork.entity.User;

public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long>{
	List<RecentSearch> findTop5BySearcherOrderBySearchedAtDesc(User searcher);
    boolean existsBySearcherAndSearchedUser(User searcher, User searchedUser);
    Optional<RecentSearch> findBySearcherAndSearchedUser(User searcher, User searchedUser);
}

