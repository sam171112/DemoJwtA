package com.demo.services;

import com.demo.dto.JwtAuthenticationResponse;
import com.demo.dto.RefreshTokenRequest;
import com.demo.dto.SignInRequest;
import com.demo.dto.SignUpRequest;
import com.demo.entities.User;

public interface AuthenticationService
{

	User signup(SignUpRequest signUpRequest);
	JwtAuthenticationResponse signin(SignInRequest signInRequest);
	JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
