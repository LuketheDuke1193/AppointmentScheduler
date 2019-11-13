package Controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import Model.Customer;
import Model.CustomerRoster;
import Model.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.plaf.nimbus.State;
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the Add Customer window?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void saveButtonHandler(ActionEvent event) throws SQLException {
        if (nullCheck()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("One or more fields missing values.");
            alert.setContentText("Please ensure all fields have a value entered.");
            alert.show();
        }
        if (validCheck()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Field");
            alert.setContentText("Please ensure all fields have a valid value entered.");
            alert.show();
        } else {
            String customerName = nameField.getText().trim();
            String address = addressField.getText().trim();
            String city = cityField.getText().trim();
            String postalCode = postalCodeField.getText().trim();
            String country = countryField.getText().trim();
            String phoneNumber = phoneNumberField.getText().trim();
            String username = Main.user.getUsername().trim();
            String customerNameDB;
            String addressDB;
            int addressIdDB = 0;
            int cityIdDB = 0;
            String postalCodeDB;
            int countryIdDB = 0;
            String phoneNumberDB;

            //Checks if Country already exits (grabs countryId if so). If it doesn't exist, it inserts the record into the country table.
            Statement countrySearch = (Statement) Main.conn.createStatement();
            ResultSet countryRS = countrySearch.executeQuery("SELECT countryId, country FROM U06aua.country WHERE country = '" + country + "';");
            if (countryRS.next()) {
                countryIdDB = countryRS.getInt(1);
            } else {
                PreparedStatement countryInsert = Main.conn.prepareStatement("INSERT INTO U06aua.country (country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, now(), ?, now(), ?);");
                countryInsert.setString(1, country);
                countryInsert.setString(2, username);
                countryInsert.setString(3, username);
                countryInsert.executeUpdate();
                Statement newCountry = (Statement) Main.conn.createStatement();
                ResultSet newCountryIdRS = newCountry.executeQuery("SELECT countryId FROM U06aua.country WHERE country = '" + country + "';");
                newCountryIdRS.next();
                countryIdDB = newCountryIdRS.getInt(1);
            }
            //Checks if City already exists (grabs cityId if so). If it doesn't exist, it inserts the record into the country table.
            Statement citySearch = (Statement) Main.conn.createStatement();
            ResultSet cityRS = citySearch.executeQuery("SELECT cityId, city FROM U06aua.city WHERE city = '" + city + "' AND countryId = '" + countryIdDB + "';");
            if (cityRS.next()) {
                cityIdDB = cityRS.getInt("cityId");
            } else {
                PreparedStatement cityInsert = Main.conn.prepareStatement("INSERT INTO U06aua.city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, now(), ?, now(), ?);");
                cityInsert.setString(1, city);
                cityInsert.setInt(2, countryIdDB);
                cityInsert.setString(3, username);
                cityInsert.setString(4, username);
                cityInsert.executeUpdate();
                Statement newCityStatement = (Statement) Main.conn.createStatement();
                ResultSet newCityRS = newCityStatement.executeQuery("SELECT cityId FROM U06aua.city WHERE city ='" + city + "';");
                newCityRS.next();
                cityIdDB = newCityRS.getInt(1);
            }
            //Checks if Address already exists (grabs address and addressId if so). If it doesn't exist, it inserts the record into the country table.
            Statement addressSearch = (Statement) Main.conn.createStatement();
            ResultSet addressRS = addressSearch.executeQuery("SELECT addressId, address FROM U06aua.address WHERE address = '" + address + "';");
            if (addressRS.next()) {
                addressIdDB = addressRS.getInt(1);
                addressDB = addressRS.getString(2);
            } else {
                PreparedStatement addressInsert = Main.conn.prepareStatement("INSERT INTO U06aua.address (address, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, now(), ?, now(), ?);");
                addressInsert.setString(1, address);
                addressInsert.setInt(2, cityIdDB);
                addressInsert.setString(3, postalCode);
                addressInsert.setString(4, phoneNumber);
                addressInsert.setString(5, username);
                addressInsert.setString(6, username);
                addressInsert.executeUpdate();
                Statement newAddress = (Statement) Main.conn.createStatement();
                ResultSet newAddressRS = newAddress.executeQuery("SELECT addressId, address FROM U06aua.address WHERE address = '" + address + "';");
                newAddressRS.next();
                addressIdDB = newAddressRS.getInt(1);

            }

            //Checks if customer already exists. If it doesn't, it adds in the record.
            Statement customerSearch = (Statement) Main.conn.createStatement();
            ResultSet customerRS = customerSearch.executeQuery("SELECT customerName FROM U06aua.customer WHERE customerName = '" + customerName + "' AND addressId = '" + addressIdDB + "';");
            if (customerRS.next()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Customer already exists.");
                alert.setContentText("The customer is already present in the database.");
                alert.show();
            } else {
                PreparedStatement customerInsert = Main.conn.prepareStatement("INSERT INTO U06aua.customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, 0, now(), ?, now(), ?);");
                customerInsert.setString(1, customerName);
                customerInsert.setInt(2, addressIdDB);
                customerInsert.setString(3, username);
                customerInsert.setString(4, username);
                customerInsert.executeUpdate();
                Statement newCustomer = (Statement) Main.conn.createStatement();
                ResultSet newCustomerRS = newCustomer.executeQuery("SELECT customerId FROM U06aua.customer WHERE customerName = '" + customerName + "';");
                newCustomerRS.next();
                int customerID = newCustomerRS.getInt(1);
                Customer customer = new Customer();
                customer.setCustomerName(customerName);
                customer.setAddress(address);
                customer.setPhone(phoneNumber);
                customer.setId(customerID);
                MainScreenController.customerRoster.addCustomer(customer);
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            }
        }
    }



    private boolean nullCheck() { //RETURNS TRUE IF A FIELD IS EMPTY.
        if (nameField.getText().isEmpty() || addressField.getText().isEmpty() || cityField.getText().isEmpty() || postalCodeField.getText().isEmpty() || countryField.getText().isEmpty() || phoneNumberField.getText().isEmpty()) {
            return true;
        } else {
            return false;
        }

    }

    private boolean validCheck() { //RETURNS TRUE IF ONE OR MORE FIELDS IN INVALID
        if (Integer.parseInt(postalCodeField.getText().trim()) < 0 || phoneNumberField.getText().charAt(0) == '-') {
            return true;
        } else {
            return false;
        }
    }


    @FXML
    void initialize() {
        ID.setDisable(true);
    }
}
