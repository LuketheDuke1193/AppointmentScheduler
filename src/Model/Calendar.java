package Model;

import com.mysql.jdbc.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Calendar {
    public ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    public Calendar(ObservableList<Appointment> appointmentList){
        this.appointmentList = appointmentList;
    }

    public Calendar(){

    }

    public void populateCalendar() throws SQLException {
        Statement appointmentDetails = (Statement) Main.conn.createStatement(); //Creates statement object
        Statement appointmentCustomerDetails = (Statement) Main.conn.createStatement();
        ResultSet rsAppointmentDetails = appointmentDetails.executeQuery("SELECT * FROM U06aua.appointment");
        ResultSet rsAppointmentCustomerDetails = appointmentCustomerDetails.executeQuery("SELECT customerId, customerName FROM U06aua.customer");
        while (rsAppointmentDetails.next()){
            Appointment appointment = new Appointment();
            appointment.setDate(rsAppointmentDetails.getTimestamp(10));
            appointment.setLocation(rsAppointmentDetails.getString(6));
            appointment.setType(rsAppointmentDetails.getString(8));
            rsAppointmentCustomerDetails.next();
            appointment.setCustomerID(rsAppointmentCustomerDetails.getInt(1));
            appointment.setCustomerName(rsAppointmentCustomerDetails.getString(2));
            appointmentList.add(appointment);
            }

        }


    public void addAppointment(Appointment appointment){
        appointmentList.add(appointment);
    }

    public void updateAppointment(int id, Appointment selectedAppointment){
        this.appointmentList.set(id, selectedAppointment);
    }

    public void deleteCustomer(Appointment selectedAppointment){
        this.appointmentList.remove(selectedAppointment);
    }

    public ObservableList<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(ObservableList<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }
}
