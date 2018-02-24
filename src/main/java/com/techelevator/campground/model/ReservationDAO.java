package com.techelevator.campground.model;

import java.time.LocalDate;

public interface ReservationDAO {

	public Long createReservation(Integer parent_site_id, LocalDate arrivalDate, LocalDate departureDate, String reservationName);

	Reservation getReservationById(Long id);	
}
