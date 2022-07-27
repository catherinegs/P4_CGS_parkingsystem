package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    
    private static ParkingSpotDAO parkingSpotDAO;
    
    private static TicketDAO ticketDAO;
        
    private static DataBasePrepareService dataBasePrepareService;

    private static Ticket ticket;
    

    @InjectMocks
    Date inTime = new Date("2022/06/8-17:22:17");
    @InjectMocks
    Date outTime = new Date("2022/07/13-19:50:02");


    

	


    @Mock
    private static InputReaderUtil inputReaderUtil;


    

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

	@BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("21");
        dataBasePrepareService.clearDataBaseEntries();
        
	}


    @AfterAll
    private static void tearDown(){

    }

	@SuppressWarnings("deprecation")
	@Test
    public void testParkingACar(){

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        

  

        parkingService.processIncomingVehicle();
        
        
        when(ticket.getInTime()).thenReturn(inTime);


        //check that a ticket is actualy saved in DB

       assertNotNull(ticketDAO.getTicket("21"));

       
	   ParkingSpot parkingSpot = ticket.getParkingSpot();
	  assertTrue(parkingSpotDAO.updateParking(parkingSpot));

        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }


	@Test
    public void testParkingLotExit(){
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
         
       //ticket.setOutTime(outTime);



        parkingService.processExitingVehicle();
        
        when(ticket.getOutTime()).thenReturn(outTime);


       // Ticket ticket = ticketDAO.getTicket("21"); 


        assertEquals((36), ticket.getPrice());
        
        //TODO: check that the fare generated and out time are populated correctly in the database
    }

}
