package Controller;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import Model.Appointment;
import Model.Main;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddAppointmentController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField appointmentType;

    @FXML
    private TextField appointmentLocation;

    @FXML
    private ChoiceBox<String> customerChoice;

    @FXML
    private Button save;

    @FXML
    private Button cancel;

    @FXML
    private ChoiceBox<String> hourChoice;

    @FXML
    private ChoiceBox<String> minutesChoice;

    @FXML
    void cancelHandler(ActionEvent event) {

    }

    @FXML
    void saveHandler(ActionEvent event) throws SQLException {
        if (nullCheck()){
            Alert alert = new Alert (Alert.AlertType.WARNING);
            alert.setTitle("One or more fields missing values.");
            alert.setContentText("Please ensure all fields have a value entered.");
            alert.show();
        } else {
            String customerName = customerChoice.getValue().toString().trim();
            String location = appointmentLocation.getText().trim();
            String type = appointmentType.getText().trim();
            LocalDate date = datePicker.getValue();
            String hour = hourChoice.getValue();
            String minutes = minutesChoice.getValue();
            String dateTime = date.toString() + " " + hour + ":" + minutes + ":" + "00";
            Timestamp dateTimestamp = Timestamp.valueOf(dateTime);

            PreparedStatement selectCustomer = Main.conn.prepareStatement("SELECT customerId FROM U06aua.customer WHERE customerName = ?;");
            PreparedStatement insertAppointment = Main.conn.prepareStatement("INSERT INTO U06aua.appointment (customerId, " +
                    "userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "now(), ?, now(), ?);");
            selectCustomer.setString(1, customerName);
            ResultSet selectCustomerRS = selectCustomer.executeQuery();
            selectCustomerRS.next();
            int customerId = selectCustomerRS.getInt("customerId");
            insertAppointment.setInt(1, customerId);
            PreparedStatement userIdDB = Main.conn.prepareStatement("SELECT userId FROM U06aua.user WHERE userName = ?;");
            userIdDB.setString(1, Main.user.getUsername());
            ResultSet userIdDBRS = userIdDB.executeQuery();
            userIdDBRS.next();
            int userId = userIdDBRS.getInt("userId");
            insertAppointment.setInt(2, userId);
            insertAppointment.setString(3, "");
            insertAppointment.setString(4, "");
            insertAppointment.setString(5, location);
            insertAppointment.setString(6, "");
            insertAppointment.setString(7, type);
            insertAppointment.setString(8, "");
            insertAppointment.setString(9, dateTime);
            insertAppointment.setString(10, "0000-00-00 00:00:00");
            insertAppointment.setString(11, Main.user.getUsername());
            insertAppointment.setString(12, Main.user.getUsername());
            insertAppointment.execute();

            Appointment newAppointment = new Appointment(customerId, customerName, dateTimestamp, type, location);
            MainScreenController.calendar.addAppointment(newAppointment);
            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();
        }
    }

    private boolean nullCheck(){ //RETURNS TRUE IF A FIELD IS EMPTY.
        if (customerChoice.getSelectionModel().isEmpty() || datePicker.getValue().equals("") || hourChoice.getSelectionModel().isEmpty() || minutesChoice.getSelectionModel().isEmpty() || appointmentType.getText().isEmpty() || appointmentLocation.getText().isEmpty()) {
            return true;
        } else {
            return false;
        }

    }

    @FXML
    void initialize() {

        ObservableList<String> customerNames = FXCollections.observableArrayList();
        for (int i = 0; i < MainScreenController.customerRoster.getCustomerListSize(); i++){
            customerNames.add(MainScreenController.customerRoster.getCustomerList().get(i).getCustomerName());
        }
       customerChoice.setItems(customerNames);

        ObservableList<String> hoursList = FXCollections.observableArrayList();
        ObservableList<String> minutesList = FXCollections.observableArrayList();

        hoursList.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutesList.addAll("00", "15", "30", "45");

        hourChoice.setItems(hoursList);
        minutesChoice.setItems(minutesList);


    }
}
