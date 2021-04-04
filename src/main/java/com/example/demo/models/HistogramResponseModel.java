package com.example.demo.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.example.demo.entities.PriceCountAggregateResult;

public class HistogramResponseModel {
	private BigDecimal min;
	private BigDecimal max;
	private List<HistogramEntry> histogram;
	
	public BigDecimal getMin() {
		return min;
	}
	public void setMin(BigDecimal min) {
		this.min = min;
	}
	public BigDecimal getMax() {
		return max;
	}
	public void setMax(BigDecimal max) {
		this.max = max;
	}
	public List<HistogramEntry> getHistogram() {
		return histogram;
	}
	public void setHistogram(List<HistogramEntry> histogram) {
		this.histogram = histogram;
	}

	
}
