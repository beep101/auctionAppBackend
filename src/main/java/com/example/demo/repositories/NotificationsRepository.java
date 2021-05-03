package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Notification;
import com.example.demo.entities.User;

public interface NotificationsRepository extends JpaRepository<Notification,Integer>{
	List<Notification> findByUserEqualsOrderByTimeDesc(User user,Pageable pageable);
}
