package com.SocialNetwork;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.SocialNetwork.entity.User;
import com.SocialNetwork.repository.UserRepository;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	public CommandLineRunner regAdmin(UserRepository userRepository) {
		return args ->{
			if(!userRepository.existsByEmail("admin@gmail.com")) {
				final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
				User admin=new User();
				admin.setUsername("Admin");
				admin.setEmail("admin@gmail.com");
				admin.setPassword(encoder.encode("admin123"));
//				admin.setRole("ADMIN");
				admin.setRole("ROLE_ADMIN");
				admin.setActive(true);
				userRepository.save(admin);
				
			}else {
				System.out.println("Admin Exist");
			}
		};
	}
	
}
