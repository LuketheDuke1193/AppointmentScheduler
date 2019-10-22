package Model;

import javafx.collections.ObservableList;

public class Customer {
    protected int id;
    protected String customerName;
    protected String address;
    protected String phone;

    public Customer(int id, String customerName, String address, String phone){
        this.id = id;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
    }

    public Customer(int id, String customerName){
        this.id = id;
        this.customerName = customerName;
    }

    public Customer(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
