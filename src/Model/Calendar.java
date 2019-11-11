package Model;

import com.mysql.jdbc.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
            Timestamp date = appointmentDetailsRS.getTimestamp("start");
            String type = appointmentDetailsRS.getString("type");
            String location = appointmentDetailsRS.getString("location");
            while(appointmentCustomerRS.next()){
                if (appointmentCustomerRS.getInt("customerId") == customerId){
                    String customerName = appointmentCustomerRS.getString("customerName");
                    appointment.setCustomerName(customerName);
                }
            }
            appointment.setCustomerID(customerId);
            appointment.setDate(date);
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
