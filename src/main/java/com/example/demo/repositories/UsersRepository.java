package com.example.demo.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.User;

public interface UsersRepository extends JpaRepository<User, Integer>{
	List<User> findByEmail(String email);
	List<User> findByFpTokenAndFpTokenEndtimeAfter(String token,Timestamp crr);
}
