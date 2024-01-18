package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.demo.Repository.UserRepository;
import com.demo.entities.Role;
import com.demo.entities.User;

@SpringBootApplication
public class DemoJwtAApplication implements CommandLineRunner
{

	@Autowired
	private UserRepository userRepository;
	
	public static void main(String[] args)
	{
		SpringApplication.run(DemoJwtAApplication.class, args);
	}

	public void run(String ...args)
	{
		User adminAccount=userRepository.findByRole(Role.admin);
		if(null==adminAccount)
		{
			User user=new User();
			
			user.setEmail("admin@gmail.com");
			user.setFirstname("admin");
			user.setLastname("admin");
			user.setRole(Role.admin);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);
			
			
			
			
		}
		
		
	}
	
}
