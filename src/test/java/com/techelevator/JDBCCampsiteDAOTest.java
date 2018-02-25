package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.model.Campsite;
import com.techelevator.campground.model.jdbc.JDBCCampsiteDAO;

public class JDBCCampsiteDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCCampsiteDAO sut;
	private Integer newCampgroundId;
	private Long newParkId;
	//private Long newCampgroundIdForNewPark;
	private Long newCampsiteId;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campreservation");
		dataSource.setUsername("postgres");

		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dataSource.destroy();
	}

	@Before
	public void setUp() throws Exception {
		sut = new JDBCCampsiteDAO(dataSource);
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

		// // selecting campground id that has our park id
		// String campgroundIDForNewPark = "select campground_id from campground where
		// park_id = ? ";
		// newCampgroundIdForNewPark =
		// jdbcTemplate.queryForObject(campgroundIDForNewPark, Long.class,
		// newParkId.intValue());
		// creating a campground
		String newCampsite = "INSERT INTO site (campground_id, site_number, max_occupancy, accessible,max_rv_length, utilities ) VALUES (?, ?, ?, ?, ?, ?) RETURNING site_id";
		newCampsiteId = jdbcTemplate.queryForObject(newCampsite, Long.class, newCampgroundId,1, 6, true, 11,true);
	
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();

	}

	@Test
	public void testGetAvailableCampsites() {
		LocalDate arriveDate = LocalDate.now();
		LocalDate departDate = LocalDate.now();

		List <Campsite> campsiteForNewCampground = sut.getAvailableCampsites(newCampgroundId.intValue(), arriveDate, departDate);
		
		Campsite site = campsiteForNewCampground.get(0);
		
		assertEquals(newCampgroundId,site.getParent_Campground_id());
		
		
	}

}
