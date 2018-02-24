package com.techelevator.campground.model;

import java.time.LocalDate;
import java.util.List;

public interface CampsiteDAO {

public List<Campsite> getAvailableCampsites(Integer parent_campground_id, LocalDate arrivalDate, LocalDate departureDate);

public Long getSiteIDfromUserInput(Integer site_number ,Integer parent_CampgroundID);
}
