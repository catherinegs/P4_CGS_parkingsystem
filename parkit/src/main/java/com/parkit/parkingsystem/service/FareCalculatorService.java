package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        if (ticket.getParkingSpot() != null);

        Long inHour = ticket.getInTime().getTime();
        Long outHour = ticket.getOutTime().getTime();
        

        //TODO: Some tests are failing here. Need to check if this logic is correct

        long duration = outHour - inHour;

        long durationInMin = (duration / (1000 * 60));
        long diffInHours = (duration / (1000 * 60 * 60));
        long diffInDays = (duration / (1000 * 60 * 60 * 24));
        
        
         
        switch (ticket.getParkingSpot().getParkingType()){
        
            case CAR: {
            	
            	
            	
            	if (durationInMin <= 30) {
            		
                	ticket.setPrice(0.00 * Fare.CAR_RATE_PER_HOUR);
                	
            	} else if (durationInMin > 30 && durationInMin <= 45) {
            		
                  	ticket.setPrice(0.75 * Fare.CAR_RATE_PER_HOUR);

            		
            	}  else if (diffInDays >= 1) {
            		
                	ticket.setPrice(24 * Fare.CAR_RATE_PER_HOUR);

            	} else if (diffInHours >= 1) {
            		
                	ticket.setPrice(1 * Fare.CAR_RATE_PER_HOUR);

            	}
        	
                break;
            }
            case BIKE: {
            	
            	
            	if (durationInMin <= 30) {
            		
                	ticket.setPrice(0.00 * Fare.BIKE_RATE_PER_HOUR);
                	
            	} else if (durationInMin > 30 && durationInMin <= 45) {
            	
            		
                	ticket.setPrice(0.75 * Fare.BIKE_RATE_PER_HOUR);

            	}   else  {
            		
                	ticket.setPrice(1 * Fare.BIKE_RATE_PER_HOUR);

            	}
            		

                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}