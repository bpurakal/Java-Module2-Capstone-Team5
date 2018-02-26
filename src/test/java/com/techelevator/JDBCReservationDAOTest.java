package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campsite;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.jdbc.JDBCCampsiteDAO;
import com.techelevator.campground.model.jdbc.JDBCReservationDAO;

public class JDBCReservationDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	private Long newReservationId;
	private JDBCReservationDAO sut;
	
	JdbcTemplate jdbcTemplate;
	Reservation newReservation;
	private Long newParkId;
	private Integer newCampgroundId;
	private Long newCampsiteId;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	    dataSource = new SingleConnectionDataSource();
	    dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
	    dataSource.setUsername("postgres");

	    dataSource.setAutoCommit(false);
	    // Normally when you use the data source it automatically opens and closes a
	    // transaction after each command
	    // The above code will change this so that it does not auto commit after each
	    // transaction
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	    dataSource.destroy();
	}
	
	@Before
	public void setUp() throws Exception {
		sut = new JDBCReservationDAO(dataSource);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		// making park for campground to be apart of
		String sEstablishDate = "2001/04/01";
		Date EstablishDate = new SimpleDateFormat("YYYY/MM/dd").parse(sEstablishDate);
		String sqlNewPark = "INSERT INTO park (name, location, establish_date, area, visitors, description) VALUES (?, ?, ?, ?, ?, ?) RETURNING park_id";
		newParkId = jdbcTemplate.queryForObject(sqlNewPark, Long.class, "Name Test", "Location Test", EstablishDate,
				10000, 2000, "Description of Park");

		// creating a campground
		String newCampground = "INSERT INTO campground (park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES (?, ?, ?, ?, ?) RETURNING campground_id";
		newCampgroundId = jdbcTemplate.queryForObject(newCampground, Integer.class, newParkId.intValue(),
				"campground Test", 03, 13, 11);
		
		String newCampsite = "INSERT INTO site (campground_id, site_number, max_occupancy, accessible,max_rv_length, utilities ) VALUES (?, ?, ?, ?, ?, ?) RETURNING site_id";
		newCampsiteId = jdbcTemplate.queryForObject(newCampsite, Long.class, newCampgroundId,1, 6, true, 11,true);
	    
	}
	
	@After
	public void tearDown() throws Exception {
	    dataSource.getConnection().rollback();
	}

	@Test
	public void testCreateReservation() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Reservation> reservations = new ArrayList<>();

		LocalDate from_date = LocalDate.now();
		LocalDate to_date = LocalDate.now();
		
		Long createdReservationId = sut.createReservation(newCampsiteId.intValue(), from_date,  to_date, "Clifford");
		
	    String sqlGetNewReservation = "SELECT * FROM reservation " + "WHERE reservation_id = ?";
	    SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetNewReservation, createdReservationId);

	    while(results.next()) {
	        newReservation = mapRowToReservation(results);
	        reservations.add(newReservation);
	    }
	    assertEquals("Clifford", reservations.get(0).getName());
	}
	

	private Reservation mapRowToReservation(SqlRowSet results) {
	    Reservation theReservation;
	    theReservation = new Reservation();
	    theReservation.setReservation_id(results.getLong("reservation_id"));
	    theReservation.setParent_site_id(results.getInt("site_id"));
	    theReservation.setName(results.getString("name"));
	    theReservation.setFrom_date(results.getDate("from_date").toLocalDate());
	    theReservation.setTo_date(results.getDate("to_date").toLocalDate());
	    theReservation.setCreate_date(results.getDate("create_date").toLocalDate());
	    return theReservation;
	    
		

	}
}

