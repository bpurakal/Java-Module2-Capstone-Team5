package com.techelevator.campground.model;

import java.time.LocalDate;
import java.util.List;

public interface CampgroundDAO {
	
	public List<Campground> getAllCampgrounds(Integer parent_Park_Id);
	
	//searchForReservationAtAllCampsites is a bonus
	public List<Campground> searchForReservationAtAllCampsites(LocalDate wantedReservationDateStart,LocalDate wantedReservationDateEnd);

}

