package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	private static ParkingSpotDAO parkingSpotDAO;

	private static TicketDAO ticketDAO;

	private static DataBasePrepareService dataBasePrepareService;

	private static Ticket ticket;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	
	@SuppressWarnings({ "deprecation", "static-access" })
	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
		ticket = new Ticket();
		//ticket.setId(1);
		int available = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		ParkingSpot parkingSpot = parkingSpotDAO.getParkingSpot(available);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("77");
		Date inTime = new Date();
		//Date inTime = new Date(2023,01,14,19,03,43);
		ticket.setInTime(inTime);
		ticketDAO.saveTicket(ticket);

	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("77");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testParkingACar() {

		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		parkingService.processIncomingVehicle();

		// check that a ticket is actualy saved in DB

		assertNotNull(ticketDAO.getTicket("77"));

		Ticket ticket1 = ticketDAO.getTicket("77");

		ticket1.inTime = new Date();

		ticketDAO.saveTicket(ticket1);

		// check that Parking table is updated with availability

		ParkingSpot parkingSpot1 = ticket1.getParkingSpot();

		assertFalse(parkingSpot1.isAvailable());

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testParkingLotExit() {

		testParkingACar();

		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		
		ticket = ticketDAO.getTicket("77");
		
		//long inTime = System.currentTimeMillis() - (  24 * 60 * 60 * 1000); 
		


		Date inTime = ticket.getInTime();	
		Date outTime = new Date(inTime.getTime() + (1000 * 60 * 60 * 24));

		
		
		//Date inTime = new Date(new Date("2023-01-14 19:03:43.0").getTime() - 86400000);
		//Date inTime = new Date(new Date(2023,01,14,19,03,43).getTime() - (1000 * 60 * 60 * 24));
		
		
		//Date outTime = new Date();

		
		//LocalDateTime.from(inTime.toInstant().atZone(ZoneId.of("UTC"))).plusDays(1);
		
		//LocalDateTime.from(inTime.toInstant()).plusDays(1);
		
		ticketDAO.updateTicket(ticket);

		


		parkingService.processExitingVehicle();
		
		


		// check that the fare generated and out time are populated correctly in the
		// database

		assertEquals((36), ticket.getPrice());

	}

}