package com.parkit.parkingsystem.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		
		Date inTime = new Date();
		LocalDateTime ldtInTime = LocalDateTime.ofInstant(inTime.toInstant(),
		                                             ZoneId.systemDefault());
		Date outTime = new Date();
		LocalDateTime ldtOutTime = LocalDateTime.ofInstant(outTime.toInstant(),
		                                             ZoneId.systemDefault());

	    long inHour = ticket.getInTime().getTime();
	    long outHour = ticket.getOutTime().getTime();
	    long duration = (outHour- inHour);
	    double hour = (double) duration/(60*60*1000);
	    int hourInt = (int) hour;
	    
	    Duration dur = Duration.between(ldtInTime, ldtOutTime);
	    int hours = (int) dur.toHours();
	    
	    
	    Duration someDuration = Duration.ofMinutes((long) (hour * 60));	    
	    
		Duration durationDay = Duration.ofDays(1);
		Duration freeDuration = Duration.ofMinutes(30);
		
		Duration averageDuration = Duration.ofMinutes(45);
		int result = dur.compareTo(freeDuration);
		int result2 = dur.compareTo(averageDuration);
		int result3 = dur.compareTo(durationDay);

		if (ticket.getParkingSpot() != null) {

			switch (ticket.getParkingSpot().getParkingType()) {

			case CAR: {

				if (result < 0) {

					ticket.setPrice(0.00 * Fare.CAR_RATE_PER_HOUR);

				} else if (result >= 0 && result2 < 0) {

					ticket.setPrice(0.75 * Fare.CAR_RATE_PER_HOUR);

				} else if (result3 == 0) {

					ticket.setPrice(24 * Fare.CAR_RATE_PER_HOUR);

				} else {

					ticket.setPrice(1 * hours * Fare.CAR_RATE_PER_HOUR);
				}

				break;
			}
			case BIKE: {

				if (result <= 0) {

					ticket.setPrice(0.00 * Fare.BIKE_RATE_PER_HOUR);

				} else if (result > 0 && result2 <= 0) {

					ticket.setPrice(0.75 * Fare.BIKE_RATE_PER_HOUR);

				} else {

					ticket.setPrice(hourInt * 1 * Fare.BIKE_RATE_PER_HOUR);
				}

				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
		}

	}

}