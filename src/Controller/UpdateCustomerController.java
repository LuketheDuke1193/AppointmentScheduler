package Controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Model.Customer;
import Model.Main;
import com.mysql.jdbc.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.transform.Result;

public class UpdateCustomerController {

    int addressId;
    int cityId;
    int countryId;
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
    private TextField cityField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    private String country;
    private String address;
    private String phone;
    private String customerName;
    private String postalCode;
    private String city;

    @FXML
    void cancelButtonHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the Update Customer window?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void saveButtonHandler(ActionEvent event) throws SQLException {
        if (nullCheck() == true) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Values");
            alert.setContentText("One or more fields is missing a value. To continue, ensure all fields have values.");
            alert.show();
        }
        if (validCheck()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Field");
            alert.setContentText("Please ensure all fields have a valid value entered.");
            alert.show();
        } else {
            if (countryField.getText().trim() == country) {
                //Do nothing to country table then check on city match up.
                if (cityField.getText().trim() == city) {
                    //Do nothing to city table then check on address, phone and postal matchup in address table.
                    if (addressField.getText().trim() == address) {
                        //Do nothing to address table then check on name matchup in customer table.
                        if (nameField.getText().trim() == customerName) {
                            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                            MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        } else {
                            PreparedStatement updateCustomer = Main.conn.prepareStatement("UPDATE U06aua.customer SET customerName = ?, lastUpdate = now(), lastUpdateBy = ? WHERE customerId = ?;");
                            updateCustomer.setString(1, nameField.getText().trim());
                            updateCustomer.setString(2, Main.user.getUsername());
                            updateCustomer.setInt(3, MainScreenController.selectedCustomer.getId());
                            updateCustomer.execute();
                            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                            MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        }
                    } else {
                        //Update address table with new address info, phone and postal.
                        PreparedStatement updateAddress = Main.conn.prepareStatement("UPDATE U06aua.address SET address = ?, postalCode = ?, phone = ?, lastUpdate = now(), lastUpdateBy = ? WHERE addressId = ?;");
                        updateAddress.setString(1, addressField.getText().trim());
                        updateAddress.setString(2, postalCodeField.getText().trim());
                        updateAddress.setString(3, phoneNumberField.getText().trim());
                        updateAddress.setString(4, Main.user.getUsername());
                        updateAddress.setInt(5, addressId);
                        updateAddress.execute();
                        if (nameField.getText().trim() == customerName) {
                            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                            MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        } else {
                            PreparedStatement updateCustomer = Main.conn.prepareStatement("UPDATE U06aua.customer SET customerName = ?, lastUpdate = now(), lastUpdateBy = ? WHERE customerId = ?;");
                            updateCustomer.setString(1, nameField.getText().trim());
                            updateCustomer.setString(2, Main.user.getUsername());
                            updateCustomer.setInt(3, MainScreenController.selectedCustomer.getId());
                            updateCustomer.execute();
                            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                            MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        }
                    }
                } else {
                    //See if new city exists in the database.
                    PreparedStatement newCityCheck = Main.conn.prepareStatement("SELECT city, cityId FROM U06aua.city WHERE city = ?;");
                    newCityCheck.setString(1, cityField.getText().trim());
                    ResultSet newCityCheckRS = newCityCheck.executeQuery();
                    if (newCityCheckRS.next()) {//If it does it exist in database, update address to new cityId.
                        int newCityId = newCityCheckRS.getInt("cityId");
                        PreparedStatement updateAddress = Main.conn.prepareStatement("UPDATE U06aua.address SET address = ?, postalCode = ?, phone = ?, cityId = ?, lastUpdate = now(), lastUpdateBy = ? WHERE addressId = ?;");
                        updateAddress.setString(1, addressField.getText().trim());
                        updateAddress.setString(2, postalCodeField.getText().trim());
                        updateAddress.setString(3, phoneNumberField.getText().trim());
                        updateAddress.setInt(4, newCityId);
                        updateAddress.setString(5, Main.user.getUsername());
                        updateAddress.setInt(6, addressId);
                        updateAddress.execute();
                        if (nameField.getText().trim() == customerName) { //Then update customer info and close.
                            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                            MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        } else {
                            PreparedStatement updateCustomer = Main.conn.prepareStatement("UPDATE U06aua.customer SET customerName = ?, lastUpdate = now(), lastUpdateBy = ? WHERE customerId = ?;");
                            updateCustomer.setString(1, nameField.getText().trim());
                            updateCustomer.setString(2, Main.user.getUsername());
                            updateCustomer.setInt(3, MainScreenController.selectedCustomer.getId());
                            updateCustomer.execute();
                            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                            MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        }
                    } else {//If it does not exist, create it, then update address to new cityId;
                        PreparedStatement insertCity = Main.conn.prepareStatement("INSERT INTO U06aua.city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, now(), ?, now(), ?);");
                        insertCity.setString(1, cityField.getText().trim());
                        insertCity.setInt(2, countryId);
                        insertCity.setString(3, Main.user.getUsername());
                        insertCity.setString(4, Main.user.getUsername());
                        insertCity.execute();
                        PreparedStatement newCityIdDB = Main.conn.prepareStatement("SELECT cityId FROM U06aua.city WHERE city = ? AND countryId = ?;");
                        newCityIdDB.setString(1, cityField.getText().trim());
                        newCityIdDB.setInt(2, countryId);
                        ResultSet newCityIdDBRS = newCityIdDB.executeQuery();
                        newCityIdDBRS.next();
                        int newCityId = newCityIdDBRS.getInt("cityId");
                        PreparedStatement newAddress = Main.conn.prepareStatement("INSERT INTO U06aua.address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, now(), ?, now(), ?);");
                        newAddress.setString(1, addressField.getText().trim());
                        newAddress.setString(2, "");
                        newAddress.setInt(3, newCityId);
                        newAddress.setString(4, postalCodeField.getText().trim());
                        newAddress.setString(5, phoneNumberField.getText().trim());
                        newAddress.setString(6, Main.user.getUsername());
                        newAddress.setString(7, Main.user.getUsername());
                        newAddress.execute();
                        PreparedStatement newAddressIdDB = Main.conn.prepareStatement("SELECT addressId FROM U06aua.address WHERE address = ? AND cityId = ?;");
                        newAddressIdDB.setString(1, addressField.getText().trim());
                        newAddressIdDB.setInt(2, newCityId);
                        ResultSet newAddressIdDBRS = newAddressIdDB.executeQuery();
                        newAddressIdDBRS.next();
                        int newAddressId = newAddressIdDBRS.getInt("addressId");
                        if (nameField.getText().trim() == customerName) { //Then update customer info and close.
                            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                            MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        } else {
                            PreparedStatement updateCustomer = Main.conn.prepareStatement("UPDATE U06aua.customer SET customerName = ?, addressId = ?, lastUpdate = now(), lastUpdateBy = ? WHERE customerId = ?;");
                            updateCustomer.setString(1, nameField.getText().trim());
                            updateCustomer.setInt(2, newAddressId);
                            updateCustomer.setString(3, Main.user.getUsername());
                            updateCustomer.setInt(4, MainScreenController.selectedCustomer.getId());
                            updateCustomer.execute();
                            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                            MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        }
                    }
                }

            } else {
                PreparedStatement selectCountry = Main.conn.prepareStatement("SELECT country, countryId FROM U06aua.country WHERE country = '" + countryField.getText().trim() + "';");//See if new country exists in the database
                ResultSet countryDB = selectCountry.executeQuery();

                if (countryDB.next()) {//If it does exist in database, see if city already exists in DB tied to that country.
                    int newExistingCountryId = countryDB.getInt("countryId");
                    PreparedStatement cityCountryCheck = Main.conn.prepareStatement("SELECT city, cityId FROM U06aua.city WHERE city = ? AND countryId = ?;");
                    cityCountryCheck.setString(1, cityField.getText().trim());
                    cityCountryCheck.setInt(2, newExistingCountryId);
                    ResultSet cityCountryCheckRS = cityCountryCheck.executeQuery();
                    if (cityCountryCheckRS.next()) { //City tied to new country already exists. Update city, address, customer.
                        int newCityId = cityCountryCheckRS.getInt("cityId");
                        PreparedStatement updateCity = Main.conn.prepareStatement("UPDATE U06aua.city SET countryId = ?, lastUpdate = now(), lastUpdateBy = ? WHERE cityId = ?;");
                        updateCity.setInt(1, newExistingCountryId);
                        updateCity.setString(2, Main.user.getUsername());
                        updateCity.setInt(3, newCityId);
                        PreparedStatement updateAddress = Main.conn.prepareStatement("UPDATE U06aua.address SET address = ?, postalCode = ?, phone = ?, cityId = ?, lastUpdate = now(), lastUpdateBy = ? WHERE addressId = ?;");
                        updateAddress.setString(1, addressField.getText().trim());
                        updateAddress.setString(2, postalCodeField.getText().trim());
                        updateAddress.setString(3, phoneNumberField.getText().trim());
                        updateAddress.setInt(4, newCityId);
                        updateAddress.setString(5, Main.user.getUsername());
                        updateAddress.setInt(6, addressId);
                        updateAddress.execute();
                        if (nameField.getText().trim() == customerName) { //Then update customer info and close.
                            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                            MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        } else {
                            PreparedStatement updateCustomer = Main.conn.prepareStatement("UPDATE U06aua.customer SET customerName = ?, lastUpdate = now(), lastUpdateBy = ? WHERE customerId = ?;");
                            updateCustomer.setString(1, nameField.getText().trim());
                            updateCustomer.setString(2, Main.user.getUsername());
                            updateCustomer.setInt(3, MainScreenController.selectedCustomer.getId());
                            updateCustomer.execute();
                            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                            MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        }
                    } else { //Create new city record and update address, customer.
                        PreparedStatement insertCity = Main.conn.prepareStatement("INSERT INTO U06aua.city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, now(), ?, now(), ?);");
                        insertCity.setString(1, cityField.getText().trim());
                        insertCity.setInt(2, newExistingCountryId);
                        insertCity.setString(3, Main.user.getUsername());
                        insertCity.setString(4, Main.user.getUsername());
                        insertCity.execute();
                        PreparedStatement newCityIdDB = Main.conn.prepareStatement("SELECT cityId FROM U06aua.city WHERE city = ? AND countryId = ?;");
                        newCityIdDB.setString(1, cityField.getText().trim());
                        newCityIdDB.setInt(2, newExistingCountryId);
                        ResultSet newCityIdDBRS = newCityIdDB.executeQuery();
                        newCityIdDBRS.next();
                        int newCityId = newCityIdDBRS.getInt("cityId");
                        PreparedStatement newAddress = Main.conn.prepareStatement("INSERT INTO U06aua.address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, now(), ?, now(), ?);");
                        newAddress.setString(1, addressField.getText().trim());
                        newAddress.setString(2, "");
                        newAddress.setInt(3, newCityId);
                        newAddress.setString(4, postalCodeField.getText().trim());
                        newAddress.setString(5, phoneNumberField.getText().trim());
                        newAddress.setString(6, Main.user.getUsername());
                        newAddress.setString(7, Main.user.getUsername());
                        newAddress.execute();
                        PreparedStatement newAddressIdDB = Main.conn.prepareStatement("SELECT addressId FROM U06aua.address WHERE address = ? AND cityId = ?;");
                        newAddressIdDB.setString(1, addressField.getText().trim());
                        newAddressIdDB.setInt(2, newCityId);
                        ResultSet newAddressIdDBRS = newAddressIdDB.executeQuery();
                        newAddressIdDBRS.next();
                        int newAddressId = newAddressIdDBRS.getInt("addressId");
                        if (nameField.getText().trim() == customerName) { //Then update customer info and close.
                            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                            MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        } else {
                            PreparedStatement updateCustomer = Main.conn.prepareStatement("UPDATE U06aua.customer SET customerName = ?, addressId = ?, lastUpdate = now(), lastUpdateBy = ? WHERE customerId = ?;");
                            updateCustomer.setString(1, nameField.getText().trim());
                            updateCustomer.setInt(2, newAddressId);
                            updateCustomer.setString(3, Main.user.getUsername());
                            updateCustomer.setInt(4, MainScreenController.selectedCustomer.getId());
                            updateCustomer.execute();
                            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                            MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        }
                    }
                } else {//If it does not exist in database, create new country, then insert new city, update address.
                    PreparedStatement insertCountry = Main.conn.prepareStatement("INSERT INTO U06aua.country (country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, now(), ?, now(), ?);");
                    insertCountry.setString(1, countryField.getText().trim());
                    insertCountry.setString(2, Main.user.getUsername());
                    insertCountry.setString(3, Main.user.getUsername());
                    insertCountry.execute();
                    PreparedStatement selectNewCountryId = Main.conn.prepareStatement("SELECT countryId FROM U06aua.country WHERE country = ?;");
                    selectNewCountryId.setString(1, countryField.getText().trim());
                    ResultSet selectNewCountryIDRS = selectNewCountryId.executeQuery();
                    selectNewCountryIDRS.next();
                    int newCountryId = selectNewCountryIDRS.getInt("countryId");
                    PreparedStatement insertCity = Main.conn.prepareStatement("INSERT INTO U06aua.city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, now(), ?, now(), ?);");
                    insertCity.setString(1, cityField.getText().trim());
                    insertCity.setInt(2, newCountryId);
                    insertCity.setString(3, Main.user.getUsername());
                    insertCity.setString(4, Main.user.getUsername());
                    insertCity.execute();
                    PreparedStatement newCityIdDB = Main.conn.prepareStatement("SELECT cityId FROM U06aua.city WHERE city = ? AND countryId = ?;");
                    newCityIdDB.setString(1, cityField.getText().trim());
                    newCityIdDB.setInt(2, newCountryId);
                    ResultSet newCityIdDBRS = newCityIdDB.executeQuery();
                    newCityIdDBRS.next();
                    int newCityId = newCityIdDBRS.getInt("cityId");
                    PreparedStatement updateAddress = Main.conn.prepareStatement("UPDATE U06aua.address SET address = ?, postalCode = ?, phone = ?, cityId = ?, lastUpdate = now(), lastUpdateBy = ? WHERE addressId = ?;");
                    updateAddress.setString(1, addressField.getText().trim());
                    updateAddress.setString(2, postalCodeField.getText().trim());
                    updateAddress.setString(3, phoneNumberField.getText().trim());
                    updateAddress.setInt(4, newCityId);
                    updateAddress.setString(5, Main.user.getUsername());
                    updateAddress.setInt(6, addressId);
                    updateAddress.execute();
                    if (nameField.getText().trim() == customerName) { //Then update customer info and close.
                        Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                        MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                        Stage stage = (Stage) cancelButton.getScene().getWindow();
                        stage.close();
                    } else {
                        PreparedStatement updateCustomer = Main.conn.prepareStatement("UPDATE U06aua.customer SET customerName = ?, lastUpdate = now(), lastUpdateBy = ? WHERE customerId = ?;");
                        updateCustomer.setString(1, nameField.getText().trim());
                        updateCustomer.setString(2, Main.user.getUsername());
                        updateCustomer.setInt(3, MainScreenController.selectedCustomer.getId());
                        updateCustomer.execute();
                        Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
                        MainScreenController.customerRoster.updateCustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
                        Stage stage = (Stage) cancelButton.getScene().getWindow();
                        stage.close();
                    }
                }
            }
        }
    }

    boolean nullCheck() {
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
    void initialize() throws SQLException {
        ID.setDisable(true);

        String id = String.valueOf(MainScreenController.selectedCustomer.getId());
        customerName = MainScreenController.selectedCustomer.getCustomerName();
        address = MainScreenController.selectedCustomer.getAddress();
        phone = MainScreenController.selectedCustomer.getPhone();
        int customerId = MainScreenController.selectedCustomer.getId();

        Statement customerStatement = (Statement) Main.conn.createStatement();
        Statement cityStatement = (Statement) Main.conn.createStatement();
        Statement countryStatement = (Statement) Main.conn.createStatement();
        Statement addressStatement = (Statement) Main.conn.createStatement();

        ResultSet customerRS = customerStatement.executeQuery("SELECT * FROM U06aua.customer WHERE customerId = '" + id + "';");
        customerRS.next();
        addressId = customerRS.getInt("addressId");

        ResultSet addressRS = addressStatement.executeQuery("SELECT postalCode, cityId FROM U06aua.address WHERE addressId = '" + addressId + "';");
        addressRS.next();
        postalCode = addressRS.getString("postalCode");
        cityId = addressRS.getInt("cityId");

        ResultSet cityRS = cityStatement.executeQuery("SELECT city, countryId FROM U06aua.city WHERE cityId = '" + cityId + "';");
        cityRS.next();
        city = cityRS.getString("city");
        countryId = cityRS.getInt("countryId");

        ResultSet countryRS = countryStatement.executeQuery("SELECT country FROM U06aua.country WHERE countryId = '" + countryId + "';");
        countryRS.next();
        country = countryRS.getString("country");

        ID.setText(id);
        nameField.setText(customerName);
        addressField.setText(address);
        cityField.setText(city);
        postalCodeField.setText(postalCode);
        countryField.setText(country);
        phoneNumberField.setText(phone);


    }
}
