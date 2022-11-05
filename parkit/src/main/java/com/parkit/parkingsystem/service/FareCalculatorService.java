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
    
    int inHour = ticket.getInTime().getMinutes();
    int outHour = ticket.getOutTime().getMinutes();

    //TODO: Some tests are failing here. Need to check if this logic is correct
    int duration = Math.abs(outHour - inHour);
    Duration durationInHours = Duration.ofHours(duration);
    Duration durationInMinutes = Duration.ofMinutes(duration);
    Duration durationInDay = Duration.ofDays(duration);
    int min1 = 30;
    int min2 = 45;
    int day = 1440;
    

    double coef;
    if (durationInMinutes.toMinutes() <= min1)  {
    	 coef = durationInMinutes.toMinutes() * 0;
    } else if (durationInMinutes.toMinutes() > min1 && durationInMinutes.toMinutes() <= min2)  {
    	coef = durationInMinutes.toMinutes() * 0.75;
    } else if (durationInDay.toDays() >= 1)  {
    	coef = durationInDay.toDays() * 24.00;
    }  else {
    	coef = durationInHours.toHours() * 1;
    }
    

    switch (ticket.getParkingSpot().getParkingType()){
        case CAR: {
        		
            	ticket.setPrice( coef * Fare.CAR_RATE_PER_HOUR);
        	
            break;
        }
        case BIKE: {
        	
            ticket.setPrice(coef * Fare.BIKE_RATE_PER_HOUR);
            
            break;
        }
        default: throw new IllegalArgumentException("Unkown Parking Type");
    }
 
  }     

}