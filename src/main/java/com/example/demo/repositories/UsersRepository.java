package com.example.demo.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.User;

public interface UsersRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
	Optional<User> findByForgotPasswordTokenAndForgotPasswordTokenEndTimeAfter(String token,Timestamp crr);
}
