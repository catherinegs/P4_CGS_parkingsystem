package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.Period;
import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	

public void calculateFare(Ticket ticket){
    if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
        throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
    }
    
    
    Long inHour = ticket.getInTime().getTime();
    Long outHour = ticket.getOutTime().getTime();        
    long duration = outHour - inHour;
    long durationInMin = (duration / (60 * 1000));
    long diffInHours = (duration / (1000 * 60 * 60));
    long diffInDays = (duration / (1000 * 60 * 60 * 24));
   
    if (ticket.getParkingSpot() != null) {
          
    switch (ticket.getParkingSpot().getParkingType()){
    
        case CAR: {
        	           	           	
        	if (durationInMin <= 30) {
        		
            	ticket.setPrice(duration *0.00 * Fare.CAR_RATE_PER_HOUR);
            	
        	} else if (durationInMin > 30 && durationInMin <= 45) {
        		
              	ticket.setPrice(0.75 * Fare.CAR_RATE_PER_HOUR);

        		
        	}  else if (duration >= 24) {
        		
            	ticket.setPrice(24 * diffInDays * Fare.CAR_RATE_PER_HOUR);

        	} else if (diffInHours <= 23) {
        		
            	ticket.setPrice(1 * diffInHours * Fare.CAR_RATE_PER_HOUR);
        	}
    	
            break;
        }
        case BIKE: {
        	          	
        	if (durationInMin <= 30) {
        		
            	ticket.setPrice(duration * 0.00 * Fare.BIKE_RATE_PER_HOUR);
            	
        	} else if (durationInMin > 30 && durationInMin <= 45) {
        	
        		
            	ticket.setPrice(0.75 * Fare.BIKE_RATE_PER_HOUR);

        	}   else  {
        		
            	ticket.setPrice(diffInHours * 1 * Fare.BIKE_RATE_PER_HOUR);
        	}
        		
            break;
        }
        default: throw new IllegalArgumentException("Unkown Parking Type");
    }

    }


    }     

}