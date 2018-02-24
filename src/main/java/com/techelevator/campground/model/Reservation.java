package com.techelevator.campground.model;

import java.time.LocalDate;

public class Reservation extends Campsite {
	
	private Long reservation_id;
	private Integer parent_site_id;
	private LocalDate create_date;
	private LocalDate from_date;
	private LocalDate to_date;
	private String name;
	
	public Long getReservation_id() {
		return reservation_id;
	}
	public void setReservation_id(Long reservation_id) {
		this.reservation_id = reservation_id;
	}
	public Integer getParent_site_id() {
		return parent_site_id;
	}
	public void setParent_site_id(Integer parent_site_id) {
		this.parent_site_id = parent_site_id;
	}
	public LocalDate getCreate_date() {
		return create_date;
	}
	public void setCreate_date(LocalDate create_date) {
		this.create_date = create_date;
	}
	public LocalDate getFrom_date() {
		return from_date;
	}
	public void setFrom_date(LocalDate from_date) {
		this.from_date = from_date;
	}
	public LocalDate getTo_date() {
		return to_date;
	}
	public void setTo_date(LocalDate to_date) {
		this.to_date = to_date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
