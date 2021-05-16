package com.example.demo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.exceptions.BadInitializatinException;
import com.example.demo.models.CountryCodeData;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class DefaultCountryCodeUtil implements CountryCodeUtil{
	public static final String COUNTRY_CODES_FILE_LOCATION="/country_codes.csv";
	
	List<CountryCodeData> countryCodes;
	
	public DefaultCountryCodeUtil() throws BadInitializatinException {
		try {
	        CsvSchema schema = CsvSchema.emptySchema().withHeader();
	        CsvMapper mapper = new CsvMapper();
	        
		    InputStream inputStream = getClass().getResourceAsStream(COUNTRY_CODES_FILE_LOCATION);			
	        MappingIterator<CountryCodeData> readValues;

				readValues = mapper.reader(CountryCodeData.class).with(schema).readValues(inputStream);
				countryCodes=readValues.readAll();
		} catch (IOException e) {
			throw new BadInitializatinException();
		}

	}
	
	@Override
	public String getCode(String name) {
		List<CountryCodeData> filteredData=countryCodes.stream().filter(x->x.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
		if(filteredData.size()==0) {
			return null;
		}else {
			return filteredData.get(0).getLen2();
		}
	}

}
