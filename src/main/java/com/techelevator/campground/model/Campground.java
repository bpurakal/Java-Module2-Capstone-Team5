package com.techelevator.campground.model;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;

public class Campground  extends Park  {
	
	private Long campground_id;
	private Integer park_id;
	private String name;
	private Integer open_from_mm;
	private Integer open_to_mm;
	private BigDecimal daily_fee;
	//private String openMonth;
	//private Integer date;
	
	public Long getCampground_id() {
		return campground_id;
	}
	public void setCampground_id(Long campground_id) {
		this.campground_id = campground_id;
	}
	public Integer getPark_id() {
		return park_id;
	}
	public void setPark_id(Integer park_id) {
		this.park_id = park_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getOpen_from_mm() {
		return open_from_mm;
	}
	public void setOpen_from_mm(Integer open_from_mm) {
		this.open_from_mm = open_from_mm;
	}
	public Integer getOpen_to_mm() {
		return open_to_mm;
	}
	public void setOpen_to_mm(Integer open_to_mm) {
		this.open_to_mm = open_to_mm;
	}
	public BigDecimal getDaily_fee() {
		return daily_fee;
	}
	public void setDaily_fee(BigDecimal daily_fee) {
		this.daily_fee = daily_fee;
	}

	public String toString() {
		return this.name;
		//return String.format("%-15s %-10s %-10s %-10s", name, getNumberToMonth(openMonth), getNumberToMonth(openTo), dailyFee);
		
	}
	
	public String getNumberToMonth(Integer month) {
	return new DateFormatSymbols().getMonths()[month-1];
	
	}
	
	
}
