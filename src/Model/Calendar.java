package Model;

import com.mysql.jdbc.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Calendar {
    public ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    public Calendar(ObservableList<Appointment> appointmentList){
        this.appointmentList = appointmentList;
    }

    public Calendar(){

    }

    public void populateCalendar() throws SQLException {
        PreparedStatement appointmentDetails = Main.conn.prepareStatement("SELECT * FROM U06aua.appointment;");
        ResultSet appointmentDetailsRS = appointmentDetails.executeQuery();
        appointmentList.clear();
        while (appointmentDetailsRS.next()){
            PreparedStatement appointmentCustomer = Main.conn.prepareStatement("SELECT * FROM U06aua.customer;");
            ResultSet appointmentCustomerRS = appointmentCustomer.executeQuery();
            Appointment appointment = new Appointment();
            int customerId = appointmentDetailsRS.getInt("customerId");
            Timestamp startUTCTimestamp = appointmentDetailsRS.getTimestamp("start");
            LocalDateTime startUTCLDT = startUTCTimestamp.toLocalDateTime();
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime startZDTUTC = ZonedDateTime.of(startUTCLDT, ZoneId.of("UTC"));
            ZonedDateTime startZDTLocal = startZDTUTC.withZoneSameInstant(zoneId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            startZDTLocal.format(formatter);

            Timestamp endUTCTimestamp = appointmentDetailsRS.getTimestamp("end");
            LocalDateTime endUTCLDT = endUTCTimestamp.toLocalDateTime();
            ZonedDateTime endZDT = ZonedDateTime.of(endUTCLDT, ZoneId.of("UTC"));
            ZonedDateTime endZDTLocal = endZDT.withZoneSameInstant(zoneId);
            endZDTLocal.format(formatter);
            String type = appointmentDetailsRS.getString("type");
            String location = appointmentDetailsRS.getString("location");
            while(appointmentCustomerRS.next()){
                if (appointmentCustomerRS.getInt("customerId") == customerId){
                    String customerName = appointmentCustomerRS.getString("customerName");
                    appointment.setCustomerName(customerName);
                }
            }
            appointment.setCustomerID(customerId);
            appointment.setStart(startZDTLocal);
            appointment.setEnd(endZDTLocal);
            appointment.setType(type);
            appointment.setLocation(location);
            appointmentList.add(appointment);
        }

    }

    public void populateCalendarByConsultant(String user) throws SQLException {
        PreparedStatement userDetails = Main.conn.prepareStatement("SELECT userId FROM U06aua.user WHERE userName = ?;");
        userDetails.setString(1, user);
        ResultSet userDetailsRS = userDetails.executeQuery();
        userDetailsRS.next();
        int userId = userDetailsRS.getInt("userId");
        PreparedStatement appointmentDetails = Main.conn.prepareStatement("SELECT * FROM U06aua.appointment WHERE userId = ?;");
        appointmentDetails.setInt(1, userId);
        ResultSet appointmentDetailsRS = appointmentDetails.executeQuery();
        appointmentList.clear();
        while (appointmentDetailsRS.next()){
            PreparedStatement appointmentCustomer = Main.conn.prepareStatement("SELECT * FROM U06aua.customer;");
            ResultSet appointmentCustomerRS = appointmentCustomer.executeQuery();
            Appointment appointment = new Appointment();
            int customerId = appointmentDetailsRS.getInt("customerId");
            Timestamp startUTCTimestamp = appointmentDetailsRS.getTimestamp("start");
            LocalDateTime startUTCLDT = startUTCTimestamp.toLocalDateTime();
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime startZDTUTC = ZonedDateTime.of(startUTCLDT, ZoneId.of("UTC"));
            ZonedDateTime startZDTLocal = startZDTUTC.withZoneSameInstant(zoneId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            startZDTLocal.format(formatter);

            Timestamp endUTCTimestamp = appointmentDetailsRS.getTimestamp("end");
            LocalDateTime endUTCLDT = endUTCTimestamp.toLocalDateTime();
            ZonedDateTime endZDT = ZonedDateTime.of(endUTCLDT, ZoneId.of("UTC"));
            ZonedDateTime endZDTLocal = endZDT.withZoneSameInstant(zoneId);
            endZDTLocal.format(formatter);
            String type = appointmentDetailsRS.getString("type");
            String location = appointmentDetailsRS.getString("location");
            while(appointmentCustomerRS.next()){
                if (appointmentCustomerRS.getInt("customerId") == customerId){
                    String customerName = appointmentCustomerRS.getString("customerName");
                    appointment.setCustomerName(customerName);
                }
            }
            appointment.setCustomerID(customerId);
            appointment.setStart(startZDTLocal);
            appointment.setEnd(endZDTLocal);
            appointment.setType(type);
            appointment.setLocation(location);
            appointmentList.add(appointment);
        }
    }

    public void addAppointment(Appointment appointment){
        appointmentList.add(appointment);
    }

    public void updateAppointment(int id, Appointment selectedAppointment){
        this.appointmentList.set(id, selectedAppointment);
    }

    public void deleteAppointment(Appointment selectedAppointment){
        this.appointmentList.remove(selectedAppointment);
    }

    public ObservableList<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(ObservableList<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }
}
