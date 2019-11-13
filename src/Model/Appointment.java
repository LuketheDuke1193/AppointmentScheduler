package Model;

import java.time.ZonedDateTime;

public class Appointment {

    int customerID;
    String customerName;
    ZonedDateTime start;
    ZonedDateTime end;
    String type;
    String location;

    public Appointment(int customerID, String customerName, ZonedDateTime start, ZonedDateTime end, String type, String location){
        this.customerID = customerID;
        this.customerName = customerName;
        this.start = start;
        this.end = end;
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

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd(){
        return end;
    }

    public void setEnd(ZonedDateTime end){
        this.end = end;
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
