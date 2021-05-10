package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.PushSub;

public interface PushSubsRepository extends JpaRepository<PushSub,Integer>{
	Optional<PushSub> findOneByUrl(String url);
}
