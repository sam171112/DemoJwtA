package com.demo.config;

import org.apache.catalina.Manager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.demo.entities.Role;
import com.demo.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration 
{

private final JwtAuthenticationFilter jwtAuthenticationFilter;
private final UserService userService;

public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, UserService userService) 
{
	super();
	this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	this.userService = userService;
}
	

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception
{
	http.csrf(AbstractHttpConfigurer::disable)
	.authorizeHttpRequests(request->request.requestMatchers("api/auth/**")
			.permitAll()
			.requestMatchers("/api/admin").hasAnyAuthority(Role.admin.name())
			.requestMatchers("/api/user").hasAnyAuthority(Role.user.name())
			.anyRequest().authenticated())
	
	     .sessionManagement(manager-> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	     .authenticationProvider(authenticationProvider()).addFilterBefore(
	    		 jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	     
	     
	    return http.build();
}

  @Bean
  public AuthenticationProvider authenticationProvider()
  {
	  DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
	  daoAuthenticationProvider.setUserDetailsService(userService.userDetailsService());
	  daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
	  return daoAuthenticationProvider;
			  
	  
	  
  }


  @Bean
  public PasswordEncoder passwordEncoder()
 {

	  return new BCryptPasswordEncoder();	  
 }
  
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
{
  
  return config.getAuthenticationManager();
}

}