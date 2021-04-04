package com.example.demo.models;

import java.math.BigDecimal;

public class HistogramEntry {
	private BigDecimal upperBound;
	private Integer count;
	
	public BigDecimal getUpperBound() {
		return upperBound;
	}
	public void setUpperBound(BigDecimal upperBound) {
		this.upperBound = upperBound;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	
}
