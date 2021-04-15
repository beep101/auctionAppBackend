package com.example.demo.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GenderConverter implements Converter<String,Gender>{

	@Override
	public Gender convert(String source) {
		try {
			return Gender.valueOf(source);
		}catch(IllegalArgumentException ex) {
			return null;
		}
	}

}
