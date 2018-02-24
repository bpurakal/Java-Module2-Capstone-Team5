package com.techelevator.campground.model.jdbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);

	}

	@Override
	public List<Campground> searchForReservationAtAllCampsites(LocalDate wantedReservationDateStart,
			LocalDate wantedReservationDateEnd) {
		// TODO Auto-generated method stub // bonus method
		return null;
	}
	

	
	@Override
	public List<Campground> getAllCampgrounds( Integer parent_Park_Id) {
		List<Campground> campgrounds = new ArrayList<>();
		String sqlFindAllCampgroundsForAPark = 
		"SELECT campground_id, park_id,	name, open_from_mm, open_to_mm, daily_fee FROM campground where park_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindAllCampgroundsForAPark, parent_Park_Id);
		while (results.next()) {
			Campground theCampground = mapRowToCampground(results);
			campgrounds.add(theCampground);
		}
		return campgrounds;
	}

	private Campground mapRowToCampground(SqlRowSet results) {
		Campground theCampground;
		theCampground = new Campground();
		theCampground.setId(results.getLong("campground_id"));
		theCampground.setPark_id(results.getInt("park_id"));
		theCampground.setName(results.getString("name"));
		theCampground.setOpen_from_mm(Integer.parseInt(results.getString("open_from_mm")));
		theCampground.setOpen_to_mm(Integer.parseInt(results.getString("open_to_mm")));
		theCampground.setDaily_fee(results.getBigDecimal("daily_fee"));


		return theCampground;
	}


}
