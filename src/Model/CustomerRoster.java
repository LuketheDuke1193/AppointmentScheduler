package Model;

import com.mysql.jdbc.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRoster {
    public static ObservableList<Customer> customerList = FXCollections.observableArrayList();

    public CustomerRoster(ObservableList<Customer> customerList){
        this.customerList = customerList;
    }

    public CustomerRoster(){
        customerList = FXCollections.observableArrayList();
    }

    public void addCustomer(Customer customer){
        this.customerList.add(customer);
    }

    public void updateCustomer(int id, Customer selectedCustomer){
        for (int i = 0; i < customerList.size(); i++){
            if (customerList.get(i).getId() == id){
                this.customerList.set(i, selectedCustomer);
            }
        }
    }

    public void deleteCustomer(Customer selectedCustomer){
        this.customerList.remove(selectedCustomer);
    }

    public void populateRoster() throws SQLException {
        Statement customerDetails = (Statement) Main.conn.createStatement(); //Creates statement object
        Statement customerAddressDetails = (Statement) Main.conn.createStatement();
        ResultSet rsCustomer = customerDetails.executeQuery("SELECT * FROM U06aua.customer;"); //Creates resultset and executes query to grab all customers.
        customerList.clear(); //Clean slate on customerList.
        while (rsCustomer.next()){
            Customer customer = new Customer();
            customer.setId(rsCustomer.getInt(1));
            customer.setCustomerName(rsCustomer.getString(2));
            int customerAddressId = rsCustomer.getInt(3);
            ResultSet rsCustomerAddress = customerAddressDetails.executeQuery("SELECT * FROM U06aua.address WHERE addressId = '" + customerAddressId + "';");
            rsCustomerAddress.next();
            customer.setAddress(rsCustomerAddress.getString(2));
            customer.setPhone(rsCustomerAddress.getString(6));
            customerList.add(customer);
        }
    }

    public ObservableList<Customer> getCustomerList(){
        return customerList;
    }

    public int getCustomerListSize(){
        return customerList.size();
    }
}
