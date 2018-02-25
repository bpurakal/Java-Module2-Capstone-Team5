package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;

public class JDBCCampgroundDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundDAO sut;
	private Long newCampgroundId;
	private Long newParkId;
	private Long newCampgroundIdForNewPark;
	Campground newCampground;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campreservation");
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
		sut = new JDBCCampgroundDAO(dataSource);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		//making park for campground to be apart of 
		String sEstablishDate = "2001/04/01";
		Date EstablishDate = new SimpleDateFormat("YYYY/MM/dd").parse(sEstablishDate);
		String sqlNewPark = "INSERT INTO park (name, location, establish_date, area, visitors, description) VALUES (?, ?, ?, ?, ?, ?) RETURNING park_id";
		newParkId = jdbcTemplate.queryForObject(sqlNewPark, Long.class, "Name Test", "Location Test", EstablishDate,
				10000, 2000, "Description of Park");

		
		// creating a campground
		String newCampground = "INSERT INTO campground (park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES (?, ?, ?, ?, ?) RETURNING campground_id";
		newCampgroundId = jdbcTemplate.queryForObject(newCampground, Long.class, newParkId.intValue(), "campground Test", 03, 13, 11);
		
		//selecting campground id that has our park id
		String campgroundIDForNewPark = "select campground_id from campground where park_id = ? ";
		newCampgroundIdForNewPark = jdbcTemplate.queryForObject(campgroundIDForNewPark, Long.class, newParkId.intValue());

	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testGetAllCampgrounds() {
		List <Campground> campgroundForNewPark = sut.getAllCampgrounds(newParkId.intValue());

		assertEquals(1,campgroundForNewPark.size());
		
		assertEquals(newCampgroundId, newCampgroundIdForNewPark);
	
	}
	

	

	}
