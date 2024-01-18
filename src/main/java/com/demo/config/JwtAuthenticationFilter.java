package com.demo.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.services.JWTService;
import com.demo.services.UserService;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component

public class JwtAuthenticationFilter extends OncePerRequestFilter
{
	
	private final JWTService jwtService;
	private final UserService userService;
	

	public JwtAuthenticationFilter(JWTService jwtService, UserService userService) 
	{
		super();
		this.jwtService = jwtService;
		this.userService = userService;
	}



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
	
		final String authHeader=request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		
		if(StringUtils.isEmpty(authHeader)|| !org.apache.commons.lang3.StringUtils.startsWith(authHeader,"Bearer "))
		{
			filterChain.doFilter(request, response);
			return;
			
		}
		
		jwt=authHeader.substring(7);
		userEmail=jwtService.extractUserName(jwt);
		
		if(StringUtils.isNotEmpty(userEmail)&& SecurityContextHolder.getContext().getAuthentication()==null)
		{ 
			 UserDetails userDetails=userService.userDetailsService().loadUserByUsername(userEmail);
			 
			 
			 if(jwtService.isTokenValid(jwt, userDetails))
			 {
				 
				 SecurityContext securityContext=SecurityContextHolder.createEmptyContext();
				 
				 UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(
						 userDetails,null,userDetails.getAuthorities());
				 
				 
				 token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				 
				 securityContext.setAuthentication(token);
				 SecurityContextHolder.setContext(securityContext);
				 
				 
				 
			 }
		
			
			filterChain.doFilter(request, response);
				}
		
		
	}

}
