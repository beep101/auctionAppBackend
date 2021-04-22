package com.example.demo.services;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.demo.repositories.ItemsRepository;

@RunWith(EasyMockRunner.class)
public class SearchSuggestionserviceTests extends EasyMockSupport {
	
	@Mock
	ItemsRepository itemsRepoMock;
	
	@TestSubject
	DefaultSearchSuggestionService searchSerivce=new DefaultSearchSuggestionService(itemsRepoMock);
	
	@Test
	public void testGetSuggestionBlankTermShouldReturnNull() throws Exception{

		replayAll();
		
		String result=searchSerivce.getSuggestion("");
		
		assertNull(result);
		
		verifyAll();
	}
	
	@Test
	public void testGetSuggestionsExistingTermShouldReturnNull() {
		String term="term";
		
		List<String> terms=new ArrayList<>();
		terms.add("term abcd");
		terms.add("axsasa");
		terms.add("ollik cnhgg");
		terms.add("erf ede eff");
		
		expect(itemsRepoMock.getAllNamesForActiveItems(anyObject())).andReturn(terms);
		replayAll();
		
		String result=searchSerivce.getSuggestion(term);
		
		assertNull(result);
		
		verifyAll();
	}
	
	@Test
	public void testGetSuggestionsNoCloseMatchShouldReturnNull() {
		String term="term";
		
		List<String> terms=new ArrayList<>();
		terms.add("efrecrcereverv");
		terms.add("axsa vdvf dfdsa");
		terms.add("");
		
		expect(itemsRepoMock.getAllNamesForActiveItems(anyObject())).andReturn(terms);
		replayAll();
		
		String result=searchSerivce.getSuggestion(term);
		
		assertNull(result);
		
		verifyAll();
	}

	
	@Test
	public void testGetSuggestionsOneCloseMatchShouldReturnIt() {
		String term="trm";
		
		List<String> terms=new ArrayList<>();
		terms.add("term erfef");
		terms.add("termes");
		terms.add("");
		
		expect(itemsRepoMock.getAllNamesForActiveItems(anyObject())).andReturn(terms);
		replayAll();
		
		String result=searchSerivce.getSuggestion(term);
		
		assertEquals(result,"term");
		
		verifyAll();
	}
	
	@Test
	public void testGetSuggestionsMultipleCloseMatchesShouldReturnShortest() {
		String term="abc";
		
		List<String> terms=new ArrayList<>();
		terms.add("abcd erefeerf");
		terms.add("ab refer");
		terms.add("");
		
		expect(itemsRepoMock.getAllNamesForActiveItems(anyObject())).andReturn(terms);
		replayAll();
		
		String result=searchSerivce.getSuggestion(term);
		
		assertEquals(result,"ab");
		
		verifyAll();
	}
}
