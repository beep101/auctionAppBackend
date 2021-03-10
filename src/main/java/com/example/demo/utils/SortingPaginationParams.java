package com.example.demo.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SortingPaginationParams extends PaginationParams {
	public SortingPaginationParams(int page,int count,ItemSorting sort) {
		super(page,count);
		pageable=PageRequest.of(page, count,sort.getSort());
	}
}
