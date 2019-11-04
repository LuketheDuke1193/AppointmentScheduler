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
        if (nullCheck() == true){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Values");
            alert.setContentText("One or more fields is missing a value. To continue, ensure all fields have values.");
            alert.show();
        } else {
            //COUNTRY UPDATE
            PreparedStatement selectCountry = Main.conn.prepareStatement("SELECT country, countryId FROM U06aua.country WHERE country = '" + countryField.getText().trim() + "';");
            ResultSet countryDB = selectCountry.executeQuery();
            PreparedStatement selectOldCountry = Main.conn.prepareStatement("SELECT country, countryId FROM U06aua.country WHERE country = '" + country + "';");
            ResultSet cityOldDb = selectOldCountry.executeQuery();
            if (!countryDB.next()){
                //COUNTRY INSERT AND CITY UPDATE
                PreparedStatement insertCountry = Main.conn.prepareStatement("INSERT INTO U06aua.country (country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, now(), ?, now(), ?);");
                insertCountry.setString(1, countryField.getText().trim());
                insertCountry.setString(2, Main.user.getUsername());
                insertCountry.setString(3, Main.user.getUsername());
                insertCountry.execute();
                PreparedStatement countryDBId = Main.conn.prepareStatement("SELECT countryId FROM U06aua.country WHERE country = ?;");
                countryDBId.setString(1, countryField.getText().trim());
                ResultSet countryDBIdRS = countryDBId.executeQuery();
                countryDBIdRS.next();
                int newCountryId = countryDBIdRS.getInt("countryId");

                cityOldDb.next();
                int oldCountryId = cityOldDb.getInt("countryId");

                PreparedStatement cityUpdate = Main.conn.prepareStatement("UPDATE U06aua.city SET countryId = ? WHERE countryId = ?;");
                cityUpdate.setString(1, String.valueOf(newCountryId));
                cityUpdate.setString(2, String.valueOf(oldCountryId));
                cityUpdate.execute();
            } else {
                //COUNTRY UPDATE
                PreparedStatement updateCountry = Main.conn.prepareStatement("UPDATE U06aua.country SET country = ? WHERE country = ?;");
                updateCountry.setString(1, countryField.getText().trim());
                updateCountry.setString(2, country);
                updateCountry.execute();
            }

            //CITY UPDATE
            PreparedStatement selectCity = Main.conn.prepareStatement("SELECT city, cityId FROM U06aua.city WHERE city = '" + cityField.getText().trim() + "';");
            ResultSet cityDB = selectCity.executeQuery();
            PreparedStatement selectOldCity = Main.conn.prepareStatement("SELECT city, cityId FROM U06aua.city WHERE city = '" + city + "';");
            ResultSet cityOldDB = selectOldCity.executeQuery();
            if (!cityDB.next()){
                //CITY INSERT AND ADDRESS
                PreparedStatement countryDBId = Main.conn.prepareStatement("SELECT countryId FROM U06aua.country WHERE country = ?;");
                countryDBId.setString(1, countryField.getText().trim());
                ResultSet countryDBIdRS = countryDBId.executeQuery();
                countryDBIdRS.next();
                int newCountryId = countryDBIdRS.getInt("countryId");

                PreparedStatement insertCity = Main.conn.prepareStatement("INSERT INTO U06aua.city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, now(), ?, now(), ?);");
                insertCity.setString(1, cityField.getText().trim());
                insertCity.setInt(2, newCountryId);
                insertCity.setString(3, Main.user.getUsername());
                insertCity.setString(4, Main.user.getUsername());
                insertCity.execute();
                PreparedStatement cityDBId = Main.conn.prepareStatement("SELECT cityId FROM U06aua.city WHERE city = ?;");
                cityDBId.setString(1, cityField.getText().trim());
                ResultSet cityDBIdRS = cityDBId.executeQuery();
                cityDBIdRS.next();
                int newCityId = cityDBIdRS.getInt("cityId");

                cityOldDb.next();
                int cityOldId = cityOldDb.getInt("cityId");

                PreparedStatement addressUpdate = Main.conn.prepareStatement("UPDATE U06aua.city SET countryId = ? WHERE countryId = ?;");
                addressUpdate.setString(1, String.valueOf(newCityId));
                addressUpdate.setString(2, String.valueOf(cityOldId));
                addressUpdate.execute();
            } else {
                //CITY UPDATE
                PreparedStatement updateCity = Main.conn.prepareStatement("UPDATE U06aua.city SET city = ? WHERE city = ?;");
                updateCity.setString(1, cityField.getText().trim());
                updateCity.setString(2, city);
                updateCity.execute();
            }
            //ADDRESS UPDATE
            PreparedStatement selectAddress = Main.conn.prepareStatement("SELECT address, addressId FROM U06aua.address WHERE address = '" + addressField.getText().trim() + "';");
            ResultSet addressDB = selectAddress.executeQuery();
            PreparedStatement selectOldAddress = Main.conn.prepareStatement("SELECT address, addressId FROM U06aua.address WHERE address = '" + address + "';");
            ResultSet addressOldDB = selectOldAddress.executeQuery();
            if (!addressDB.next()){
                //ADDRESS INSERT AND CUSTOMER
                PreparedStatement cityDBId = Main.conn.prepareStatement("SELECT cityId FROM U06aua.city WHERE city = ?;");
                cityDBId.setString(1, cityField.getText().trim());
                ResultSet cityDBIdRS = cityDBId.executeQuery();
                cityDBIdRS.next();
                int newCityId = cityDBIdRS.getInt("cityId");

                PreparedStatement insertAddress = Main.conn.prepareStatement("INSERT INTO U06aua.address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, now(), ?, now(), ?);");
                insertAddress.setString(1, addressField.getText().trim());
                insertAddress.setString(2, "");
                insertAddress.setInt(3, newCityId);
                insertAddress.setString(4, postalCodeField.getText().trim());
                insertAddress.setString(5, phoneNumberField.getText().trim());
                insertAddress.setString(6, Main.user.getUsername());
                insertAddress.setString(7, Main.user.getUsername());
                insertAddress.execute();
                PreparedStatement addressDBId = Main.conn.prepareStatement("SELECT addressId FROM U06aua.address WHERE address = ?;");
                addressDBId.setString(1, addressField.getText().trim());
                ResultSet addressDBIdRS = addressDBId.executeQuery();
                addressDBIdRS.next();
                int newAddressId = addressDBIdRS.getInt("addressId");

                addressOldDB.next();
                int addressOldId = addressOldDB.getInt("addressId");

                PreparedStatement customerUpdate = Main.conn.prepareStatement("UPDATE U06aua.customer SET addressId = ? WHERE customerName = ?;");
                customerUpdate.setString(1, String.valueOf(newAddressId));
                customerUpdate.setString(2, String.valueOf(customerName));
                customerUpdate.execute();
            } else {
                //ADDRESS UPDATE
                PreparedStatement updateAddress = Main.conn.prepareStatement("UPDATE U06aua.address SET address = ?, address2 = ?, postalCode = ?, phone = ? WHERE address = ?;");
                updateAddress.setString(1, addressField.getText().trim());
                updateAddress.setString(2, "");
                updateAddress.setString(3, postalCodeField.getText().trim());
                updateAddress.setString(4, phoneNumberField.getText().trim());
                updateAddress.setString(5, address);
                updateAddress.execute();
            }

            //CUSTOMER UPDATE
            PreparedStatement selectCustomer = Main.conn.prepareStatement("SELECT customerName FROM U06aua.customer WHERE customerName = ?;");
            selectCustomer.setString(1, nameField.getText().trim());
            ResultSet selectCustomerRS = selectCustomer.executeQuery();
            selectCustomerRS.next();

            PreparedStatement updateCustomer = Main.conn.prepareStatement("UPDATE U06aua.customer SET customerName = ? WHERE customerName = ?;");
            updateCustomer.setString(1, nameField.getText().trim());
            updateCustomer.setString(2, customerName);
            updateCustomer.execute();
            Customer updatedCustomer = new Customer(Integer.parseInt(ID.getText().trim()), nameField.getText().trim(), addressField.getText().trim(), phoneNumberField.getText().trim());
            MainScreenController.customerRoster.updatecustomer(Integer.parseInt(ID.getText().trim()), updatedCustomer);
            System.out.println(MainScreenController.customerRoster.getCustomerList().get(1).getAddress());
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();

        }
    }

    boolean nullCheck(){
        if (nameField.getText().isEmpty() || addressField.getText().isEmpty() || cityField.getText().isEmpty() || postalCodeField.getText().isEmpty() || countryField.getText().isEmpty() || phoneNumberField.getText().isEmpty()){
            return true;
        } else {
            return false;
        }
    }


@FXML
    void initialize()throws SQLException{
            ID.setDisable(true);
            int addressId;
            int cityId;
            int countryId;
            String id=String.valueOf(MainScreenController.selectedCustomer.getId());
            customerName=MainScreenController.selectedCustomer.getCustomerName();
            address=MainScreenController.selectedCustomer.getAddress();
            phone=MainScreenController.selectedCustomer.getPhone();

            Statement customerStatement=(Statement)Main.conn.createStatement();
            Statement cityStatement=(Statement)Main.conn.createStatement();
            Statement countryStatement=(Statement)Main.conn.createStatement();
            Statement addressStatement=(Statement)Main.conn.createStatement();

            ResultSet customerRS=customerStatement.executeQuery("SELECT * FROM U06aua.customer WHERE customerId = '"+id+"';");
            customerRS.next();
            addressId=customerRS.getInt("addressId");

            ResultSet addressRS=addressStatement.executeQuery("SELECT postalCode, cityId FROM U06aua.address WHERE addressId = '"+addressId+"';");
            addressRS.next();
            postalCode=addressRS.getString("postalCode");
            cityId=addressRS.getInt("cityId");

            ResultSet cityRS=cityStatement.executeQuery("SELECT city, countryId FROM U06aua.city WHERE cityId = '"+cityId+"';");
            cityRS.next();
            city=cityRS.getString("city");
            countryId=cityRS.getInt("countryId");

            ResultSet countryRS=countryStatement.executeQuery("SELECT country FROM U06aua.country WHERE countryId = '"+countryId+"';");
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
