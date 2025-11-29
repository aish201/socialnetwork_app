package com.SocialNetwork.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.SocialNetwork.entity.User;

import com.SocialNetwork.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("Loading user with email: " + email);
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
		System.out.println("User found: " + user.getEmail());
		return org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
				.password(user.getPassword()).disabled(!user.isActive()).roles(user.getRole().replace("ROLE_", "")) // e.g.,																									// ADMIN
				.build();
	}


}