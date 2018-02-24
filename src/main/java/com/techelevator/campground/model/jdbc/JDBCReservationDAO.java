package com.techelevator.campground.model.jdbc;

import java.sql.Date;
import java.time.LocalDate;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO{
	
	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Long createReservation(Integer parent_site_id, LocalDate fromDate, LocalDate toDate ,String reservationName) {
	
		Date sqlFromDate =Date.valueOf(fromDate);
		Date sqltoDate = Date.valueOf(toDate);
	    Date create_date = Date.valueOf(LocalDate.now());
	    
	        
		String sqlMakeReservation = 
				"INSERT INTO reservation (name, site_id, from_date, to_date, create_date)"
				+ " VALUES (?, ?, ?, ?, ?) RETURNING reservation_id";
		
	
		return jdbcTemplate.queryForObject(sqlMakeReservation,Long.class,reservationName,parent_site_id,sqlFromDate,sqltoDate,create_date);
	}
	
	@Override 
	public Reservation getReservationById (Long id) {
		Reservation reservationIdObject = null;
		String getReservationQuery = "SELECT * FROM reservation WHERE reservation_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(getReservationQuery, id);
		
	    if (results.next()) {
	    		reservationIdObject = mapRowToReservation(results);
	    }
	    return reservationIdObject;
	}
	
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation theReservation;
		theReservation = new Reservation();
		theReservation.setId(results.getLong("reservation_id"));
		theReservation.setName(results.getString("name"));
		theReservation.setParent_site_id(results.getInt("site_id"));
		theReservation.setTo_date(results.getDate("to_date").toLocalDate());
		theReservation.setTo_date(results.getDate("from_date").toLocalDate());
		theReservation.setTo_date(results.getDate("create_date").toLocalDate());

		return theReservation;
		
	}

}
