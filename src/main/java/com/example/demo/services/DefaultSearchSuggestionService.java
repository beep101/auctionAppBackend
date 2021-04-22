package com.example.demo.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.text.similarity.LevenshteinDistance;

import com.example.demo.repositories.ItemsRepository;
import com.example.demo.services.interfaces.SearchSuggestionService;

public class DefaultSearchSuggestionService implements SearchSuggestionService{
	private ItemsRepository itemsRepo;
	
	public DefaultSearchSuggestionService(ItemsRepository itemsRepo) {
		this.itemsRepo=itemsRepo;
	}

	@Override
	public String getSuggestion(String term) {
		if(term.isBlank())
			return null;
		final String upperTerm=term.toLowerCase();
		LevenshteinDistance distanceCalc=new LevenshteinDistance();
		List<String> names=getNames();
		List<Pair> distances=Stream.concat(names.stream(), getDictionary(names).keySet().stream()).map(x->new Pair(x,distanceCalc.apply(upperTerm, x))).collect(Collectors.toList());
		distances.sort(new Comparator<Pair>(){
			public int compare(Pair o1, Pair o2){
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		if(distances.get(0).getValue()==0) {
			return null;
		}else {
			if(distances.get(0).getValue()>3&&term.length()/2<distances.get(0).getValue())
				return null;
			if(distances.get(0).getValue()!=distances.get(1).getValue()) {
				return distances.get(0).getKey();
			}else {
				List<String> candidates=new ArrayList<>();
				Iterator<Pair> i=distances.iterator();
				while(i.hasNext()) {
					Pair p=i.next();
					if(p.getValue()==distances.get(0).getValue())
						candidates.add(p.getKey());
					else
						break;
				}
				return candidates.stream().min(Comparator.comparingInt(String::length)).get();
			}
		}
	}
	
	private List<String> getNames(){
		return itemsRepo.getAllNamesForActiveItems(new Timestamp(System.currentTimeMillis())).stream().map(String::toLowerCase).collect(Collectors.toList());
	}
	
	private Map<String,Long> getDictionary(List<String> names){
		return names.stream().map(x->x.split(" ")).flatMap(Arrays::stream).collect(Collectors.groupingBy(Function.identity(), 
		         Collectors.counting()));
	}
}

class Pair{
	private String key;
	private Integer value;
	
	Pair(String key,Integer value){
		this.key=key;
		this.value=value;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
}
