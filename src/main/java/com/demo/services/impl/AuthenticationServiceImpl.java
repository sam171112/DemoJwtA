package com.demo.services.impl;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.Repository.UserRepository;
import com.demo.dto.JwtAuthenticationResponse;
import com.demo.dto.RefreshTokenRequest;
import com.demo.dto.SignInRequest;
import com.demo.dto.SignUpRequest;
import com.demo.entities.Role;
import com.demo.entities.User;
import com.demo.services.AuthenticationService;
import com.demo.services.JWTService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService
{

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JWTService jwtService;
	
	
	
	
	

	public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JWTService jwtService) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}
	public User signup(SignUpRequest signUpRequest)
	 { 
		 User user=new User();
		 
		 user.setEmail(signUpRequest.getEmail());
		 user.setFirstname(signUpRequest.getFirstName());
		 user.setLastname(signUpRequest.getLastName());
		 user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		 user.setRole(Role.user);
		 
		 return userRepository.save(user);
	 }
public JwtAuthenticationResponse signin(SignInRequest signInRequest)
{
	
	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),signInRequest.getPassword()));
	
	var user=userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(()->new IllegalArgumentException("Invalid email or password"));
	var jwt=jwtService.generateToken(user);
	var refreshToken=jwtService.generateRefreshToken(new HashMap<>(),user);
	
	JwtAuthenticationResponse jwtAuthenticationResponse=new JwtAuthenticationResponse();
	
	jwtAuthenticationResponse.setToken(jwt);
	jwtAuthenticationResponse.setRefreshToken(refreshToken);
	
	return jwtAuthenticationResponse;
}
	

public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest)
{
	String userEmail=jwtService.extractUserName(refreshTokenRequest.getToken());
	
	User user=userRepository.findByEmail(userEmail).orElseThrow();
	
	if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user))
	{
		var jwt=jwtService.generateToken(user);
		JwtAuthenticationResponse jwtAuthenticationResponse=new JwtAuthenticationResponse();
		
		jwtAuthenticationResponse.setToken(jwt);
		jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
		return jwtAuthenticationResponse;

	}
	
	return null;
	
}

}


