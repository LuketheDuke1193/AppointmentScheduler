package Controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;

import Model.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class UpdateAppointmentController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<String> hourChoice;

    @FXML
    private ChoiceBox<String> minutesChoice;

    int appointmentID;

    @FXML
    void cancelHandler(ActionEvent event) {

    }

    @FXML
    void saveHandler(ActionEvent event) throws SQLException {
        int j = 0;
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

            MainScreenController.selectedAppointment.setCustomerName(customerName);
            MainScreenController.selectedAppointment.setDate(dateTimestamp);
            MainScreenController.selectedAppointment.setType(type);
            MainScreenController.selectedAppointment.setLocation(location);

            PreparedStatement customerDB = Main.conn.prepareStatement("SELECT customerId FROM U06aua.customer WHERE customerName = ?;");
            customerDB.setString(1, customerName);
            ResultSet customerDBRS = customerDB.executeQuery();
            customerDBRS.next();
            int customerId = customerDBRS.getInt("customerId");

            PreparedStatement appointmentUpdate = Main.conn.prepareStatement("UPDATE U06aua.appointment SET " +
                    "customerId = ?,  location = ?, type = ? ,start = ?, lastUpdate = now(), lastUpdateBy = ? WHERE" +
                    " appointmentId = ?");
            appointmentUpdate.setInt(1, customerId);
            appointmentUpdate.setString(2, location);
            appointmentUpdate.setString(3, type);
            appointmentUpdate.setString(4, dateTime);
            appointmentUpdate.setString(5, Main.user.getUsername());
            appointmentUpdate.setInt(6, appointmentID);
            appointmentUpdate.execute();
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
    void initialize() throws SQLException {
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        ObservableList<String> hoursList = FXCollections.observableArrayList();
        ObservableList<String> minutesList = FXCollections.observableArrayList();

        hoursList.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutesList.addAll("00", "15", "30", "45");

        hourChoice.setItems(hoursList);
        minutesChoice.setItems(minutesList);

        for (int i = 0; i < MainScreenController.customerRoster.getCustomerListSize(); i++){
            customerNames.add(MainScreenController.customerRoster.getCustomerList().get(i).getCustomerName());
        }
        customerChoice.setItems(customerNames);
        customerChoice.setValue(MainScreenController.selectedAppointment.getCustomerName());
        LocalDate date = MainScreenController.selectedAppointment.getDate().toLocalDateTime().toLocalDate();
        datePicker.setValue(date);
        String dateTime = MainScreenController.selectedAppointment.getDate().toString();
        String hours = dateTime.substring(11, 13);
        String minutes = dateTime.substring(14, 16);
        hourChoice.setValue(hours);
        minutesChoice.setValue(minutes);
        appointmentType.setText(MainScreenController.selectedAppointment.getType());
        appointmentLocation.setText(MainScreenController.selectedAppointment.getLocation());

        PreparedStatement selectAppointment = Main.conn.prepareStatement("SELECT appointmentID FROM U06aua.appointment WHERE start = ? AND type = ?;");
        selectAppointment.setString(1, dateTime);
        selectAppointment.setString(2, appointmentType.getText().trim());
        ResultSet selectAppointmentRS = selectAppointment.executeQuery();
        selectAppointmentRS.next();
        appointmentID = selectAppointmentRS.getInt("appointmentId");



    }
}
