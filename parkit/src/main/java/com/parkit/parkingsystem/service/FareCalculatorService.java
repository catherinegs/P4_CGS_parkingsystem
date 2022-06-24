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
<<<<<<< Updated upstream
        int duration = outHour - inHour;
        long diff = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
=======

        long duration = outHour - inHour;

        long durationInMin = (duration / (1000 * 60));
        long diffInHours = (duration / (1000 * 60 * 60));
        long diffInDays = (duration / (1000 * 60 * 60 * 24));
        
>>>>>>> Stashed changes
        
         
        switch (ticket.getParkingSpot().getParkingType()){
             
            case CAR: {
            	
            	if (duration < 0.75) {
            		
                	ticket.setPrice(0.75 * Fare.CAR_RATE_PER_HOUR);
                	
<<<<<<< Updated upstream
            	} else if (duration >=24) {
=======
            	} else if (durationInMin > 30 && durationInMin <= 45) {
            		
                  	ticket.setPrice(0.75 * Fare.CAR_RATE_PER_HOUR);

            		
            	}  else if (durationInMin >=60 || diffInHours >=1) {
>>>>>>> Stashed changes
            		
                	ticket.setPrice(1 * Fare.CAR_RATE_PER_HOUR);

<<<<<<< Updated upstream
            	} else 
            		
                	ticket.setPrice(1 * Fare.CAR_RATE_PER_HOUR);
        	
=======
            	}
 
            	else if (diffInHours >= 24*60 || diffInDays >=1) {
            		
                	ticket.setPrice(24 * Fare.CAR_RATE_PER_HOUR);

            	} 
            	
>>>>>>> Stashed changes
                break;
            }
            case BIKE: {
            	
<<<<<<< Updated upstream
            	if (duration < 0.75) {
=======
            	
            	if (durationInMin <= 30) {
            		
                	ticket.setPrice(0.00 * Fare.BIKE_RATE_PER_HOUR);
                	
            	} else if (durationInMin > 30 && durationInMin <= 45) {
            	
>>>>>>> Stashed changes
            		
                	ticket.setPrice(0.75 * Fare.BIKE_RATE_PER_HOUR);

            	}  else if (duration >=24) {
            		
                	ticket.setPrice(24 * Fare.BIKE_RATE_PER_HOUR);

            	} else  
            		
                	ticket.setPrice(1 * Fare.BIKE_RATE_PER_HOUR);

                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
       }
    
}