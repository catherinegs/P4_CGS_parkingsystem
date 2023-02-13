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

		Long diff = outHour - inHour;


		Long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
		float diffHours = diffMinutes.floatValue() / 60.0f;

		if (ticket.getParkingSpot() != null) {

			switch (ticket.getParkingSpot().getParkingType()) {

			case CAR: {

				if (diffHours <= 0.5) {

					ticket.setPrice(0.00 * Fare.CAR_RATE_PER_HOUR);

				}  else {

					ticket.setPrice(1 * diffHours * Fare.CAR_RATE_PER_HOUR);
				}

				break;
			}
			case BIKE: {

				if (diffHours <= 0.5) {

					ticket.setPrice(0.00 * Fare.BIKE_RATE_PER_HOUR);

				}  else {

					ticket.setPrice(diffHours * 1 * Fare.BIKE_RATE_PER_HOUR);
				}

				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
		}

	}

}