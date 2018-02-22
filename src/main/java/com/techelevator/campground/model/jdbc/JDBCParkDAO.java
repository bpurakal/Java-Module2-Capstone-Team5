package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;

public class JDBCParkDAO  implements ParkDAO{

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);

	}

	
	
	@Override
	public List<Park> getAllParks() {
		ArrayList<Park> parks = new ArrayList<>();
		String sqlfindAllParks = 
		"SELECT park_id,	name,location,establish_date,area,visitors,description FROM park  order by name asc";
		
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlfindAllParks);
		while (results.next()) {
			Park thePark = mapRowToPark(results);
			parks.add(thePark);
		}
		return parks;
	}

	private Park mapRowToPark(SqlRowSet results) {
		Park thePark;
		thePark = new Park();
		thePark.setId(results.getLong("park_id"));
		thePark.setName(results.getString("name"));
		thePark.setLocation(results.getString("location"));
		thePark.setEstablishedDate(results.getDate("establish_date").toLocalDate());
		thePark.setArea(results.getInt("area"));
		thePark.setAnnualVisitorCount(results.getInt("visitors"));
		thePark.setDescription(results.getString("description"));


		return thePark;
	}
	
}
