package com.example.demo.services;

import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.easymock.internal.LastControl;
import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.demo.entities.Category;
import com.example.demo.models.CategoryModel;
import com.example.demo.repositories.CategoriesRepository;

@RunWith(EasyMockRunner.class)
public class CategoryServiceTests extends EasyMockSupport{
	
	@Mock
	CategoriesRepository categoriesRepo;
	
	@TestSubject
	CategoryService categoryService=new CategoryService(categoriesRepo);
	
	  @Before
	  public void before(){
	    LastControl.pullMatchers();
	  }

	  @After
	  public void after(){
	    LastControl.pullMatchers();
	  }
	
	@Test
	public void getAllCategoriesMultipleChecks() {
		ArrayList<Category> listCat1=new ArrayList<>();
		listCat1.add(new Category());
		
		ArrayList<Category> listCat10=new ArrayList<>();
		for(int i=0;i<10;i++)
			listCat10.add(new Category());
		
		expect(categoriesRepo.findAll()).andReturn(new ArrayList<Category>()).once();
		expect(categoriesRepo.findAll()).andReturn(listCat1).once();
		expect(categoriesRepo.findAll()).andReturn(listCat10).once();
		replayAll();
		
		Collection<CategoryModel> result;
		
		result=categoryService.getAllCategories();
		assertEquals(0, result.size());
		result=categoryService.getAllCategories();
		assertEquals(1, result.size());
		result=categoryService.getAllCategories();
		assertEquals(10, result.size());
		
		verifyAll();
	}
}
