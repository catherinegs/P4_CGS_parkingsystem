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
import static org.junit.jupiter.api.Assertions.assertFalse;
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


    @Mock
    private static InputReaderUtil inputReaderUtil;


    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        ticket = new Ticket();
        ticket.setId(1);
        parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        ParkingSpot parkingSpot= parkingSpotDAO.getParkingSpot(1);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("21");
        Date  inTime = new Date(2022, 06, 8, 17, 22, 17);
        ticket.setInTime(inTime);
        ticketDAO.saveTicket(ticket);
        
       // Mockito.when(ticket.getInTime()).thenReturn(inTime);
        //Mockito.when(ticket.getOutTime()).thenReturn(outTime);

        
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
        

        //check that a ticket is actualy saved in DB

       assertNotNull(TicketDAO.getTicket("21"));
       
       Ticket ticket1 = TicketDAO.getTicket("21");
       
       ticket1.inTime= new Date("2022/06/08-17:22:17"); 

       
       ticketDAO.saveTicket(ticket1);            

              
       //check that Parking table is updated with availability
       
	   ParkingSpot parkingSpot1 = ticket1.getParkingSpot();
	   
	   assertFalse(parkingSpot1.isAvailable());
	   
    }


	@SuppressWarnings("deprecation")
	@Test
    public void testParkingLotExit(){
		
        testParkingACar();
                
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
      
 
                   
        parkingService.processExitingVehicle(); 
        
        Ticket ticket1 =TicketDAO.getTicket("21");
                
 
        
        ticket1.outTime = new Date("2022/07/13-19:50:02");  




        ticketDAO.updateTicket(ticket1); 
       
       


       assertEquals((36), ticket1.getPrice());
        
        //TODO: check that the fare generated and out time are populated correctly in the database
    }

}
