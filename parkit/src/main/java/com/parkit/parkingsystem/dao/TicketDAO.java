package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TicketDAO {

	private static final Logger logger = LogManager.getLogger("TicketDAO");

	public static  DataBaseConfig dataBaseConfig = new DataBaseConfig();

	public static TreeMap<String, Integer> tmap;

	public boolean saveTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)			
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
			ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
			return ps.execute();
		}  catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);

		} finally {	
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);		

		}
		  return false;
	}

	public Ticket getTicket(String vehicleRegNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Ticket ticket = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			while (rs.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(rs.getTimestamp(4));
				ticket.setOutTime(rs.getTimestamp(5));
			}	
			

		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} 
		  finally {
			dataBaseConfig.closeResultSet(rs);  
			dataBaseConfig.closePreparedStatement(ps);    
			dataBaseConfig.closeConnection(con);			
		}
		  return ticket;
	}

	public boolean updateTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
			ps.setDouble(1, ticket.getPrice());
			ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
			ps.setInt(3, ticket.getId());
			ps.execute();
			return true;
		} catch (Exception ex) {
			logger.error("Error saving ticket info", ex);
		} finally {
			dataBaseConfig.closePreparedStatement(ps);  
			dataBaseConfig.closeConnection(con);
		}
		return false;
	}

	public static List<String>  getCurrentClient() throws ClassNotFoundException, SQLException {
		ArrayList<String> numList = new ArrayList<>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String count = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.CURRENT_CUSTOMER);
			rs = ps.executeQuery();
			while (rs.next()) { // Position the cursor
				count = rs.getString("VEHICLE_REG_NUMBER"); // Retrieve only the three column value
				numList.add(count);
			}

		} catch (Exception e) {
			logger.error("Unable to count vehicle reg number", e);
		}

		finally {
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
		}
		return numList;

	}

	

}
