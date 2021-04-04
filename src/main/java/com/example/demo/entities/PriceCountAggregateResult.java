package com.example.demo.entities;

import java.math.BigDecimal;

import com.example.demo.models.HistogramEntry;

public class PriceCountAggregateResult {
	private BigDecimal startingprice;
	private long count;
	
	public PriceCountAggregateResult(BigDecimal startingprice, long count) {
		super();
		this.startingprice = startingprice;
		this.count = count;
	}
	public BigDecimal getStartingprice() {
		return startingprice;
	}
	public void setStartingprice(BigDecimal startingprice) {
		this.startingprice = startingprice;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	public HistogramEntry toHistogramEntry() {
		HistogramEntry he=new HistogramEntry();
		he.setUpperBound(this.getStartingprice());
		he.setCount((int)this.getCount());
		return he;
	}
		
}
