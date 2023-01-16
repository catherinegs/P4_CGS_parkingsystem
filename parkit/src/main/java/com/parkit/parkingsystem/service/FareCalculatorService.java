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
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		Long inHour = ticket.getInTime().getTime();
		Long outHour = ticket.getOutTime().getTime();

		Long difference_In_Time = outHour - inHour;


		Long difference_In_Minutes = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time);
		float difference_In_Hours = difference_In_Minutes.floatValue() / 60.0f;

		if (ticket.getParkingSpot() != null) {

			switch (ticket.getParkingSpot().getParkingType()) {

			case CAR: {

				if (difference_In_Hours <= 0.5) {

					ticket.setPrice(0.00 * Fare.CAR_RATE_PER_HOUR);

				}  else {

					ticket.setPrice(1 * difference_In_Hours * Fare.CAR_RATE_PER_HOUR);
				}

				break;
			}
			case BIKE: {

				if (difference_In_Hours <= 0.5) {

					ticket.setPrice(0.00 * Fare.BIKE_RATE_PER_HOUR);

				}  else {

					ticket.setPrice(difference_In_Hours * 1 * Fare.BIKE_RATE_PER_HOUR);
				}

				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
		}

	}

}