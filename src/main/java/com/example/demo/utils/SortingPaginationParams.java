package com.example.demo.utils;

import org.springframework.data.domain.PageRequest;

public class SortingPaginationParams extends PaginationParams {
	public SortingPaginationParams(int page,int count,ItemSorting sort) {
		super(page,count);
		pageable=PageRequest.of(page, count,sort.getSort());
	}
}
