package com.demo.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.Repository.UserRepository;
import com.demo.services.UserService;

@Service
public class UserServiceImpl implements UserService
{

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository)
	{
		super();
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetailsService userDetailsService()
	{
		return new UserDetailsService() 
		{
			
			@Override
			public UserDetails loadUserByUsername(String username) 
			{
			return userRepository.findByEmail(username)
					.orElseThrow(()-> new  UsernameNotFoundException("User Not Found"));
				
			}
		};
	}
	
	
}
