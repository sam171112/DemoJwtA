package com.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.entities.Role;
import com.demo.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> 
{

	 Optional<User> findByEmail(String email);
	 
	 User findByRole(Role role);
	
}
