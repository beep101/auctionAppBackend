package com.example.demo.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PaginationParams {
	protected Pageable pageable;
	
	public PaginationParams(int page,int count) {
		pageable=PageRequest.of(page, count);
	}
	
	public Pageable getPageable() {
		return pageable;
	}
}
