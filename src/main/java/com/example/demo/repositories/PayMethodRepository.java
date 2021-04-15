package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.PayMethod;

public interface PayMethodRepository extends JpaRepository<PayMethod, Integer>{

}
