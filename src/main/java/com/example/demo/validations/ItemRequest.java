package com.example.demo.validations;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Path.Node;
import javax.validation.constraints.*;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.AddressModel;
import com.example.demo.models.ItemModel;
import com.example.demo.models.SubcategoryModel;

public class ItemRequest {
	private static final int ONE_HOUR_MILIS=60*60*1000;
	private static final int ONE_DAY_MILIS=ONE_HOUR_MILIS*24;
	private static final int FIVE_DAYS_MILIS=ONE_DAY_MILIS*5;

	@NotBlank(message = "Item name can't be blank")
	private String name;
	@NotBlank(message = "Item description can't be blank")
	private String description;
	@NotNull(message="Start price is required")
	@DecimalMin(value = "0.01",message="Start price must be grater than 0")
	private BigDecimal startingprice;
	@NotNull(message="Start time is required")
	private Timestamp starttime;
	@NotNull(message="End time is required")
	private Timestamp endtime;
	@NotNull(message="Subcategory is required")
	private SubcategoryModel subcategory;
	
	private AddressModel address;

	@NotNull(message="Required at least 3 images")
	@Size(min=3, message = "Required at least 3 images" )
	private List<byte[]> imageFiles;
	
	
	public ItemRequest(ItemModel model) {
		this.name=model.getName();
		this.description=model.getDescription();
		this.startingprice=model.getStartingprice();
		this.starttime=model.getStarttime();
		this.endtime=model.getEndtime();
		this.address=model.getAddress();
		this.imageFiles=model.getImageFiles();
		this.subcategory=model.getSubcategory();
	}
	
	public Map<String,String> validate(){
		Map<String,String> problems=validateAnnotated();
		if(address!=null)
			if(address.getId()==0) 
				problems.putAll((new AddressRequest(address)).validate());
		if(!problems.containsKey("starttime")&&!problems.containsKey("endtime"))
			problems.putAll(validateStartEndTime());
		return problems;
	}
	
	private Map<String,String> validateAnnotated(){
		Set<ConstraintViolation<ItemRequest>> violations=Validation.buildDefaultValidatorFactory().getValidator().validate(this);
		Map<String, String> problems=new HashMap<>();
		for(ConstraintViolation<ItemRequest> cv:violations) {
			String last="";
			Iterator<Node> iterator=cv.getPropertyPath().iterator();
			while(iterator.hasNext())
				last=iterator.next().getName();
			problems.put(last, cv.getMessage());
		}
		return problems;
	}
	
	private Map<String,String> validateStartEndTime(){
		Map<String,String> problems=new HashMap<>();
		if(this.starttime.before(new Timestamp(System.currentTimeMillis() - ONE_DAY_MILIS))) {
			problems.put("starttime", "Start time cannot be older than one day");
		}
		if(this.starttime.before(new Timestamp(System.currentTimeMillis() - FIVE_DAYS_MILIS))) {
			problems.put("starttime", "Start time cannot be more than five days in future");
		}
		if(this.endtime.before(this.starttime)) {
			problems.put("endtime", "End time cannot be before start time");
		}
		if(this.endtime.before(new Timestamp(this.starttime.getTime()+ONE_DAY_MILIS))) {
			problems.put("endtime", "End time must be at least day after start time");
		}
		return problems;
	}

	private Map<String,String> validateImagesJpg(){
		Map<String,String> problems=new HashMap<>();
		//if byte data check needed	
		return problems;
	}
}
