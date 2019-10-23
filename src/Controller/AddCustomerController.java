package Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import Model.Customer;
import Model.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.xml.transform.Result;

public class AddCustomerController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField ID;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    void cancelButtonHandler(ActionEvent event) {

    }

    @FXML
    void saveButtonHandler(ActionEvent event) throws SQLException {
        String country;
        String city;
        String address;
        String customerName;
        String phoneNumber;
        int countryID = 0;
        int cityID = 0;
        int addressID = 0;
        int customerID = 0;
        Statement countryStatement = (Statement) Main.conn.createStatement();
        Statement cityStatement = (Statement)   Main.conn.createStatement();
        Statement addressStatement = (Statement) Main.conn.createStatement();
        Statement customerStatement = (Statement) Main.conn.createStatement();
        ResultSet countryResults = countryStatement.executeQuery("SELECT country FROM U06aua.country WHERE country =" +
                " '" + countryField.getText().trim() + "';");
        if (countryResults.next()){
            country = countryResults.getString("country");
        } else {
            countryStatement.executeUpdate("INSERT INTO U06aua.country (country, createDate, createdBy, lastUpdate, " +
                    "lastUpdateBy) VALUES ('" + countryField.getText().trim() +"', now(), '" + Main.user.getUsername() +
                    "', now(), '" + Main.user.getUsername() + "');");
            countryResults = countryStatement.executeQuery("SELECT countryId FROM U06aua.country WHERE country = '"
                    + countryField.getText().trim() + "';");
            countryResults.next();
            countryID = countryResults.getInt(1);
        }
        ResultSet cityResults = cityStatement.executeQuery("SELECT city from U06aua.city WHERE city = '" + cityField.getText().trim() + "';");
        if (cityResults.next()){
            city = cityResults.getString("city");
        } else {
            cityStatement.executeUpdate("INSERT INTO U06aua.city (city, countryID, createDate, createdBy, lastUpdate," +
                    " lastUpdateBy) VALUES ('" + cityField.getText().trim() + "', '" + countryID + "', now(), '" +
                    Main.user.getUsername() + "', now(), '" + Main.user.getUsername() + "';" ); //LEFT OFF HERE
            cityResults = cityStatement.executeQuery("SELECT cityId FROM U06aua.city WHERE city = '"+
                    cityField.getText().trim() + "';");
            cityResults.next();
            cityID = cityResults.getInt(1);
        }
        ResultSet addressResults = addressStatement.executeQuery("SELECT address FROM U06aua.address WHERE address = " +
                "'" + addressField.getText().trim() + "';");
        if(addressResults.next()){
            address = addressResults.getString("address");
            phoneNumber = addressResults.getString("phone");
        } else {
            addressStatement.executeUpdate("INSERT INTO U06aua.address (address, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ('" +
                    addressField.getText().trim() + "', '" + cityID + "', '" + postalCodeField.getText().trim() +"', '"
                    + phoneNumberField.getText().trim() + "', now(), '" + Main.user.getUsername() + "', now(), '" + Main.user.getUsername() + "';");
            addressResults = addressStatement.executeQuery("SELECT * FROM U06aua.address WHERE address = '" + addressField.getText().trim() + "';");
            addressResults.next();
            addressID = addressResults.getInt(addressID);
            phoneNumber = addressResults.getString("phone");

        }
        ResultSet customerResults = customerStatement.executeQuery("SELECT customerName FROM U06aua.customer WHERE customerName = '" + nameField.getText().trim() + "';");
        if (customerResults.next()){
            customerName = customerResults.getString("customerName");
            customerID = customerResults.getInt("customerId");
        } else {
            customerStatement.executeUpdate("INSERT INTO U06aua.customer (customerName, addressId, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES ('" + nameField.getText().trim() + "', '" + addressID + "', now(), '" + Main.user.getUsername() + "', now(), '" + Main.user.getUsername() + "');");
            customerResults = customerStatement.executeQuery("SELECT * FROM U06aua.customer WHERE customerName = '" + nameField.getText().trim() + "';");
            customerResults.next();
            customerID = customerResults.getInt("customerId");
            customerName = customerResults.getString("customerName");
        }

        Customer customer  = new Customer();
        customer.setId(customerID);
        customer.setCustomerName(customerName);
        customer.setPhone(phoneNumber);

    }


    @FXML
    void initialize() {
        assert ID != null : "fx:id=\"ID\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert nameField != null : "fx:id=\"nameField\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert addressField != null : "fx:id=\"addressField\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert cityField != null : "fx:id=\"cityField\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert countryField != null : "fx:id=\"countryField\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert phoneNumberField != null : "fx:id=\"phoneNumberField\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'AddCustomer.fxml'.";
        ID.setDisable(true);
    }
}
