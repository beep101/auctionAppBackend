package com.example.demo.models;

import java.util.List;

public interface ModelWithImages {
	int getId();
	void setImages(List<String> images);
	List<String> getImages();
}
