package com.techelevator;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.view.Menu;

public class CampgroundCLI {
	// MAIN SCREEN
	private static final String MAIN_MENU_OPTION_PARKS = "Parks";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_PARKS, MAIN_MENU_OPTION_EXIT };

	// PARK_INFORMATION_SCREEN
	private static final String PARK_INFORMATION_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String PARK_INFORMATION_SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String PARK_INFORMATION_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";
	private static final String[] PARK_INFORMATION = new String[] { PARK_INFORMATION_VIEW_CAMPGROUNDS,
			PARK_INFORMATION_SEARCH_FOR_RESERVATION, PARK_INFORMATION_RETURN_TO_PREVIOUS_SCREEN };

	// CAMPGROUND SCREEN
	private static final String CAMPGROUND_AVAILABLE_RESERVATION_SEARCH = "Search for Available Reservation";
	private static final String CAMPGROUND_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_AVAILABLE_RESERVATION_SEARCH, CAMPGROUND_RETURN_TO_PREVIOUS_SCREEN };


	
	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campreservation");
		dataSource.setUsername("postgres");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
		parkDAO = new JDBCParkDAO(datasource);
		campgroundDAO = new JDBCCampgroundDAO(datasource);
	}

	public void run() {
		while (true) {
			printHeading("Main Menu");
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (choice.equals(MAIN_MENU_OPTION_PARKS)) {
				handleSelectNationalPark();
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.exit(0);
			}

		}
	}

	private void handleSelectNationalPark() {
		printHeading("Select a Park for Further Details");
		
		List<Object> allParks = parkDAO.getAllParks();
		allParks.add("quit");
		
		Object selectedObject = (Object) menu.getChoiceFromOptions(allParks.toArray());
		if(selectedObject.equals("quit")) {
			System.exit(0);
		}
		Park selectedPark = (Park) selectedObject;
		
		handleNationalParkInfo(selectedPark);

	}
		
private void handleNationalParkInfo(Park park) {

		Park selectedPark = park;
		System.out.println(selectedPark.getName());
		System.out.println(selectedPark.getLocation());
		System.out.println(selectedPark.getEstablishedDate());
		System.out.println(selectedPark.getArea());
		System.out.println(selectedPark.getAnnualVisitorCount());
		System.out.println("\n" + selectedPark.getDescription()+"\n");
		
		
		String choice = (String) menu.getChoiceFromOptions(PARK_INFORMATION);

		if (choice.equals(PARK_INFORMATION_VIEW_CAMPGROUNDS)) {
			handleViewCampgrounds(selectedPark);
		} else if (choice.equals(PARK_INFORMATION_SEARCH_FOR_RESERVATION)) {
			// handleDepartmentSearch(); this is bonus
		} else if (choice.equals(PARK_INFORMATION_RETURN_TO_PREVIOUS_SCREEN)) {
			handleSelectNationalPark();
		}
	}
	
	
	
	private void handleViewCampgrounds(Park park) {
		printHeading(park.getName() + " National Park Campgrounds");
		List<Campground> allCampgrounds = campgroundDAO.getAllCampgrounds(park.getId().intValue());
		listCampgrounds(allCampgrounds);
		System.out.println("\nSelect a Command");

		String choice = (String) menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);

		if (choice.equals(CAMPGROUND_AVAILABLE_RESERVATION_SEARCH)) {
			//handleViewCampgrounds(selectedPark);
		} else if (choice.equals(PARK_INFORMATION_RETURN_TO_PREVIOUS_SCREEN)) {
			handleNationalParkInfo(park);
		}
	}

	private void listParks(List<Park> parks) {
		System.out.println();
		if (parks.size() > 0) {
			for (Park parkelement : parks) {
				System.out.println(parkelement.getName());
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}

	private void printHeading(String headingText) {
		System.out.println("\n" + headingText);
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
	
	private void listCampgrounds(List<Campground> campgrounds) {
	System.out.println("Name \t\t\t Open \t\t Close \t\tDaily Fee");
	if (campgrounds.size() > 0) {
		for (Campground campgroundElement : campgrounds) {
			System.out.print(campgroundElement.getName() + "\t\t");
			System.out.print(campgroundElement.getNumberToMonth(campgroundElement.getOpen_from_mm()) + "\t\t");
			System.out.print(campgroundElement.getNumberToMonth(campgroundElement.getOpen_to_mm()) + "\t\t");
			System.out.println(campgroundElement.getDaily_fee());

		}
	} else {
		System.out.println("\n*** No results ***");
	}
	
	}

}
