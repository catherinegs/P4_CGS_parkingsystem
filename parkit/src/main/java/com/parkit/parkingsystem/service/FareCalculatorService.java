package com.parkit.parkingsystem.service;

import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    @SuppressWarnings("deprecation")
	public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        int inHour = ticket.getInTime().getHours();
        int outHour = ticket.getOutTime().getHours();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        int duration = outHour - inHour;
        long diff = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
        
         
        switch (ticket.getParkingSpot().getParkingType()){
             
            case CAR: {
            	
            	if (duration < 0.75) {
            		
                	ticket.setPrice(0.75 * Fare.CAR_RATE_PER_HOUR);
                	
            	} else if (duration >= 1 & duration < 24) {
            		
                	ticket.setPrice(1 * Fare.CAR_RATE_PER_HOUR);

            	} else if (duration >=24) {
            		
                	ticket.setPrice(24 * Fare.CAR_RATE_PER_HOUR);

            	}
            		
            	
                break;
            }
            case BIKE: {
            	
            	if (duration < 0.75) {
            		
                	ticket.setPrice(0.75 * Fare.BIKE_RATE_PER_HOUR);

            	} else if (duration >= 1 & duration < 24) {
            		
                	ticket.setPrice(1 * Fare.BIKE_RATE_PER_HOUR);

            	} else if (duration >=24) {
            		
                	ticket.setPrice(24 * Fare.BIKE_RATE_PER_HOUR);

            	}
            		

                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
       }
    
}