package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;


	public class JDBCParkDAOTest {
	private static SingleConnectionDataSource dataSource;
	private Long newParkId;
	
	JdbcTemplate jdbcTemplate;
	Park newPark;

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
	    jdbcTemplate = new JdbcTemplate(dataSource);
	    String sqlNewPark = "INSERT INTO park(name, location, establish_date, area, visitors, description) "
	            + "VALUES(?, ?, ?, ?, ?, ?) RETURNING park_id";
	    newParkId = jdbcTemplate.queryForObject(sqlNewPark, Long.class, "parks dont have names", "Space Helicopter",
	            LocalDate.parse("2018-01-03"), 666, 13, "parks are cool");
	    
	}

	@After
	public void tearDown() throws Exception {
	    dataSource.getConnection().rollback();
	}

	@Test
	public void testGetAllParks() {
	    String sqlGetNewPark = "SELECT * FROM park " + "WHERE park_id = ?";
	    SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetNewPark, newParkId);

	    while(results.next()) {
	        newPark = mapRowToPark(results);
	    }
	    assertEquals("parks dont have names", newPark.getName());
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