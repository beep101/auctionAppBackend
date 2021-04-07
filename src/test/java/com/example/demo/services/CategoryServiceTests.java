package com.example.demo.services;

import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import com.example.demo.entities.Subcategory;
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
	
<<<<<<< HEAD
	@Test
	public void getAllCategoriesMultipleChecks() {
		ArrayList<Category> listCat1=new ArrayList<>();
		Category cat1=new Category();
		Subcategory sub1=new Subcategory();
		sub1.setCategory(cat1);
		List<Subcategory> subs1=new ArrayList<>();
		subs1.add(sub1);
		cat1.setSubcategories(subs1);
		listCat1.add(cat1);
		
		ArrayList<Category> listCat10=new ArrayList<>();
		for(int i=0;i<10;i++) {
			Category cat2=new Category();
			Subcategory sub2=new Subcategory();
			sub2.setCategory(cat2);
			List<Subcategory> subs2=new ArrayList<>();
			subs2.add(sub2);
			cat2.setSubcategories(subs2);
			listCat10.add(cat2);
		}
		
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
=======

		@Test
		public void getAllCategoriesMultipleChecks() {
			ArrayList<Category> listCat1=new ArrayList<>();
			Category cat1=new Category();
			Subcategory sub1=new Subcategory();
			sub1.setCategory(cat1);
			List<Subcategory> subs1=new ArrayList<>();
			subs1.add(sub1);
			cat1.setSubcategories(subs1);
			listCat1.add(cat1);
			
			ArrayList<Category> listCat10=new ArrayList<>();
			for(int i=0;i<10;i++) {
				Category cat2=new Category();
				Subcategory sub2=new Subcategory();
				sub2.setCategory(cat2);
				List<Subcategory> subs2=new ArrayList<>();
				subs2.add(sub2);
				cat2.setSubcategories(subs2);
				listCat10.add(cat2);
			}
			
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
>>>>>>> AddItem
}
