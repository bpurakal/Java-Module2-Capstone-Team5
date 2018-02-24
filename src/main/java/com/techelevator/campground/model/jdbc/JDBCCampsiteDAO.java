package com.techelevator.campground.model.jdbc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campsite;
import com.techelevator.campground.model.CampsiteDAO;

public class JDBCCampsiteDAO implements CampsiteDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCCampsiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);

	}

	@Override
	public List<Campsite> getAvailableCampsites(Integer parent_campground_id, LocalDate arrivalDate,
			LocalDate departureDate) {
		List<Campsite> campsites = new ArrayList<>();
		Date sqlArrivalDate =Date.valueOf(arrivalDate);
		Date sqlDepartureDate = Date.valueOf(departureDate);
		
		String sqlFindAllAvailableCampsites = 
				
				"select site.*,(campground.daily_fee*( date_part('day',age( ?, ? )))) as total_fee "
				+ " from site "
				+ " join campground on site.campground_id = campground.campground_id "
				+"where site.campground_id = ? and site_id not in "
				+"(select site_id from reservation where "
				+"( ?  BETWEEN reservation.from_date AND reservation.to_date) and "
				+"( ?  BETWEEN reservation.from_date AND reservation.to_date)) limit 5";


		
		SqlRowSet results = jdbcTemplate.queryForRowSet(
				sqlFindAllAvailableCampsites,sqlDepartureDate, sqlArrivalDate,parent_campground_id,sqlDepartureDate, sqlArrivalDate );  
		while (results.next()) {
			Campsite theCampsite = mapRowToCampsite(results);
			campsites.add(theCampsite);
		}
		return campsites;
		
	}
	
	

	private Campsite mapRowToCampsite(SqlRowSet results) {
		Campsite theCampsite;
		theCampsite = new Campsite();
		theCampsite.setSite_id(results.getLong("site_id"));
		theCampsite.setParent_Campground_id(results.getInt("campground_id"));
		theCampsite.setSite_number(results.getInt("site_number"));
		theCampsite.setMax_occupancy(results.getInt("max_occupancy"));
		theCampsite.setAccessible(results.getBoolean("accessible"));
		theCampsite.setMax_rv_length(results.getInt("max_rv_length"));
		theCampsite.setUtilities(results.getBoolean("utilities"));
		theCampsite.setTotalFee(results.getBigDecimal("total_fee"));

		return theCampsite;
	}

	@Override
	public Long getSiteIDfromUserInput(Integer site_number ,Integer parent_CampgroundID) {
		
		String sqlFindSiteID  = 
				"select site_id from site where site_number = ? and campground_id = ?";
		 Long site_id = jdbcTemplate.queryForObject(sqlFindSiteID, Long.class,site_number,parent_CampgroundID);

		
		return site_id;
	}
	

	
	
}
