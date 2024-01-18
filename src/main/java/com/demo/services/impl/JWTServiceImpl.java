package com.demo.services.impl;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.demo.services.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.websocket.DecodeException;

@Service
public class JWTServiceImpl implements JWTService
{

	public String generateToken(UserDetails userDetails)
	{
		return Jwts.builder().setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
				.signWith(getSiginKey(),SignatureAlgorithm.HS256)
				.compact();
		
	}
	public String generateRefreshToken(Map<String ,Object>extraClaims,UserDetails userDetails)
	{
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+604800000))
				.signWith(getSiginKey(),SignatureAlgorithm.HS256)
				.compact();
		
	}
	
	private <T> T extractClaims(String token,Function<Claims, T> claimResolvers)
	{
		final Claims claims=extractAllClaims(token);
		return claimResolvers.apply(claims);
		
	}
	
	private Claims extractAllClaims(String token)
	{
		return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody();
	}
	
	public String extractUserName(String token)
	{
		return extractClaims(token,Claims::getSubject);
	}
	
	
	private Key getSiginKey()
	{
		byte[] key=Decoders.BASE64.decode("413F4428472B4B6250655368566D5970337336763979244226452948404D6351");
		return Keys.hmacShaKeyFor(key);
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails)
	{
		final String username=extractUserName(token);
		return (username.equals(userDetails.getUsername())&& !isTokenExpaired(token));
		
		
	}
	
	
	private boolean isTokenExpaired(String token)
	{
      return extractClaims(token,Claims::getExpiration).before(new Date());
		
		
	}
}


