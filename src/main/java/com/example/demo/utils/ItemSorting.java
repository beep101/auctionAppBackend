package com.example.demo.utils;

import org.springframework.data.domain.Sort;

public enum ItemSorting {
	NEWNESS,TIMELEFT,PRICEASC,PRICEDESC,DEFAULT;
	
	public Sort getSort() {
		switch(this) {
			case NEWNESS:
				return Sort.by("starttime").descending();
			case TIMELEFT:
				return Sort.by("endtime").ascending();
			case PRICEASC:
				return Sort.by("startingprice").ascending();
			case PRICEDESC:
				return Sort.by("startingprice").descending();
			case DEFAULT:
				return Sort.by("name").ascending();
			default:
				return Sort.by("name").ascending();	
		}
	}
}
