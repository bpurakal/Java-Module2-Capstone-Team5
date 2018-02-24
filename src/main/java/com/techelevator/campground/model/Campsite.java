package com.techelevator.campground.model;

import java.math.BigDecimal;

public class Campsite extends Campground {

	private Long site_id	;
	private Integer parent_campground_id;	
	private Integer site_number;
	private Integer max_occupancy;	
	private boolean accessible;
	private Integer max_rv_length;
	private boolean utilities;
	private BigDecimal totalFee;
	public Long getSite_id() {
		return site_id;
	}
	public void setSite_id(Long site_id) {
		this.site_id = site_id;
	}
	public Integer getParent_Campground_id() {
		return parent_campground_id;
	}
	public void setParent_Campground_id(Integer parent_campground_id) {
		this.parent_campground_id = parent_campground_id;
	}
	public Integer getSite_number() {
		return site_number;
	}
	public void setSite_number(Integer site_number) {
		this.site_number = site_number;
	}
	public Integer getMax_occupancy() {
		return max_occupancy;
	}
	public void setMax_occupancy(Integer max_occupancy) {
		this.max_occupancy = max_occupancy;
	}
	public boolean isAccessible() {
		return accessible;
	}
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}
	public Integer getMax_rv_length() {
		return max_rv_length;
	}
	public void setMax_rv_length(Integer max_rv_length) {
		this.max_rv_length = max_rv_length;
	}
	public boolean isUtilities() {
		return utilities;
	}
	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}
	public BigDecimal getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}
	
	
	
	
	
}
