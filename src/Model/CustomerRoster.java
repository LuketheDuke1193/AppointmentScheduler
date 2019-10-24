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

    public void updatecustomer(int id, Customer selectedCustomer){
        this.customerList.set(id, selectedCustomer);
    }

    public void deleteCustomer(Customer selectedCustomer){
        this.customerList.remove(selectedCustomer);
    }

    public void populateRoster() throws SQLException {
        Statement customerDetails = (Statement) Main.conn.createStatement(); //Creates statement object
        Statement customerAddressDetails = (Statement) Main.conn.createStatement();
        ResultSet rsCustomer = customerDetails.executeQuery("SELECT * FROM U06aua.customer;"); //Creates resultset and executes query to grab all customers.
        ResultSet rsCustomerAddress = customerAddressDetails.executeQuery("SELECT * FROM U06aua.address;");
        while (rsCustomer.next()){
            Customer customer = new Customer();
            customer.setId(rsCustomer.getInt(1));
            customer.setCustomerName(rsCustomer.getString(2));
            rsCustomerAddress.next();
            customer.setAddress(rsCustomerAddress.getString(1));
            customer.setPhone(rsCustomerAddress.getString(6));
            customerList.add(customer);
        }
    }

    public ObservableList<Customer> getCustomerList(){
        return customerList;
    }
}
