package com.example.demo.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ItemSortingConverter implements Converter<String,ItemSorting>{

	@Override
	public ItemSorting convert(String source) {
		try {
			return ItemSorting.valueOf(source.toUpperCase());
		}catch(IllegalArgumentException ex) {
			return ItemSorting.DEFAULT;
		}
	}

}
