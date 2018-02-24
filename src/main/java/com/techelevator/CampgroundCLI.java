package com.techelevator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Campsite;
import com.techelevator.campground.model.CampsiteDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCCampsiteDAO;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.model.jdbc.JDBCReservationDAO;
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
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_AVAILABLE_RESERVATION_SEARCH,
			CAMPGROUND_RETURN_TO_PREVIOUS_SCREEN };

	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private CampsiteDAO campsiteDAO;
	private ReservationDAO reservationDAO;


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
		campsiteDAO = new JDBCCampsiteDAO(datasource);
		reservationDAO =new JDBCReservationDAO(datasource);
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
		if (selectedObject.equals("quit")) {
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
		System.out.println("\n" + selectedPark.getDescription() + "\n");

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
			handleCampsiteReservation(park);
		} else if (choice.equals(PARK_INFORMATION_RETURN_TO_PREVIOUS_SCREEN)) {
			handleNationalParkInfo(park);
		}
	}

	private void handleCampsiteReservation(Park park) {
		printHeading("Search for Campground Reservation");

		List<Campground> allCampgrounds = campgroundDAO.getAllCampgrounds(park.getId().intValue());
		listCampgrounds(allCampgrounds);

		// List<Campsite> campsitesAvailable =
		// campsiteDAO.getAvailableCampsites(parent_campground_id, arrivalDate,
		// departureDate);
		// listCampgrounds(allCampgrounds);
		Scanner scan = new Scanner(System.in);
		System.out.println("\n Which campground (enter 0 to cancel)? __");
		Integer desiredCampgroundId = scan.nextInt();
		scan.nextLine();
		if (desiredCampgroundId == 0) {
			handleViewCampgrounds(park);
		}
		System.out.println("What is the arrival date? __/__/____");
		String desiredArrivalDateString = scan.nextLine();
		System.out.println("What is the departure date? __/__/____");
		String desiredDepartureDateString = scan.nextLine();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		LocalDate arriveDateParsed = LocalDate.parse(desiredArrivalDateString, formatter);
		LocalDate departDateParsed = LocalDate.parse(desiredDepartureDateString, formatter);

		List<Campsite> allCampsites = campsiteDAO.getAvailableCampsites(desiredCampgroundId, arriveDateParsed,
				departDateParsed);
		listCampsites(allCampsites, park,desiredCampgroundId,arriveDateParsed, departDateParsed);

	}

	private void printHeading(String headingText) {
		System.out.println("\n" + headingText);
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	private void listCampgrounds(List<Campground> campgrounds) {
		if (campgrounds.size() > 0) {
			System.out.printf("%-3s%-38s%-15s%-15s%-15s%n", " ", "Name", "Open", "Close", "Daily Fee");
			for (Campground camp : campgrounds) {
				System.out.printf("%-1s%-2d%-38s%-15s%-15s%1s%-15.2f%n", "#", camp.getId(), camp.getName(),
						camp.getNumberToMonth(camp.getOpen_from_mm()), camp.getNumberToMonth(camp.getOpen_to_mm()), "$",
						camp.getDaily_fee());
			}

		} else {
			System.out.println("\n*** No results ***");
		}

	}

	private void listCampsites(List<Campsite> campsites, Park park, Integer desiredCampgroundId, LocalDate arriveDateParsed,LocalDate departDateParsed) {
		printHeading("Results Matching Your Search Criteria");

		System.out.println("Site No: \tMax Occup: \t Accessible? \t Max RV Length \t Utility \tCost");
		if (campsites.size() > 0) {
			for (Campsite campsiteElement : campsites) {
				System.out.print(campsiteElement.getSite_number() + "\t\t");
				System.out.print(campsiteElement.getMax_occupancy() + "\t\t");
				System.out.print(campsiteElement.isAccessible() + "\t\t");
				System.out.print(campsiteElement.getMax_rv_length() + "\t\t");
				System.out.print(campsiteElement.isUtilities() + "\t\t");
				System.out.println("$" + campsiteElement.getTotalFee() + "\t\t");

		

			}
			Scanner scan = new Scanner(System.in);
			System.out.println("Which site should be reserved (enter 0 to cancel)?");
			Integer campsiteNumber = scan.nextInt();
			scan.nextLine();
			if (campsiteNumber.equals(0)) {
				handleViewCampgrounds(park);
			}
			System.out.println("What name should the reservation be made under?");
			String reservationName = scan.nextLine();
			
			Integer parent_campground_id = campsites.get(0).getParent_Campground_id();
			Long reservedSiteID = campsiteDAO.getSiteIDfromUserInput(campsiteNumber, parent_campground_id);
			
			
			System.out.println("The reservation has been made and the confirmation id is {"+reservationDAO.createReservation(reservedSiteID.intValue(), arriveDateParsed, departDateParsed, reservationName)+"}");
			

		} else {
			System.out.println("\n*** No results ***");
			System.out.println("\n Would You like to would like to enter in an alternate date range? (yes) or (no)");
			Scanner scan = new Scanner(System.in);
			String searchOtherDatesYesNo = scan.nextLine();
			if (searchOtherDatesYesNo.equals("yes")) {
				handleCampsiteReservation(park);
			} else if (searchOtherDatesYesNo.equals("no")) {
				run();
			}

		}

	}

}
