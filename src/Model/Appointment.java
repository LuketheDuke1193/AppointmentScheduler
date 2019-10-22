package Model;

import java.sql.Timestamp;

public class Appointment {

    int customerID;
    String customerName;
    Timestamp date;
    String type;
    String location;

    public Appointment(int customerID, String customerName, Timestamp date, String type, String location){
        this.customerID = customerID;
        this.customerName = customerName;
        this.date = date;
        this.type = type;
        this.location = location;
    }

    public Appointment(){
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
