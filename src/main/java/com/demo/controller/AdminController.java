package com.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/admin")
public class AdminController
{

	@GetMapping
	public ResponseEntity<String> AdminHello() 
	{
	  return ResponseEntity.ok("HI Admin");
	}
	
	
}
