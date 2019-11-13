package Controller;

import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import Model.Appointment;
import Model.Main;
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
    private ChoiceBox<String> hourLengthChoice;

    @FXML
    private ChoiceBox<String> minutesLengthChoice;

    @FXML
    void cancelHandler(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveHandler(ActionEvent event) throws SQLException {
        if (nullCheck()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
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
            LocalTime time = LocalTime.parse(hour + ":" + minutes + ":00");

            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime startZdt = ZonedDateTime.of(date, time, zoneId); //Local Time
            ZonedDateTime startZDTUTC = startZdt.withZoneSameInstant(ZoneOffset.UTC); //UTC Time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startTimeUTC = startZDTUTC.format(formatter); //UTC DB format.
            String startTime = startZdt.format(formatter); //Local DB format.
            LocalTime appointmentBegin = startZDTUTC.toLocalTime();

            long hoursLength = Long.valueOf(hourLengthChoice.getValue());
            long minutesLength = Long.valueOf((String) minutesLengthChoice.getValue());
            ZonedDateTime endZdt = startZdt.plusHours(hoursLength);
            ZonedDateTime endZDTUTC = endZdt.withZoneSameInstant(ZoneOffset.UTC);
            endZdt.plusMinutes(minutesLength);
            endZDTUTC.plusMinutes(minutesLength);
            String endTimeUTC = endZDTUTC.format(formatter);
            LocalTime appointmentEnd = endZDTUTC.toLocalTime();


            if ((appointmentBegin.isAfter(Main.closedForBusiness) || appointmentBegin.isBefore(Main.openForBusiness)) || (appointmentEnd.isAfter(Main.closedForBusiness) || appointmentEnd.isBefore(Main.openForBusiness))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Appointment Hours Error");
                alert.setContentText("Appointments must be scheduled during business hours.");
                alert.show();

            } else {
                boolean overlappingAppointment = false;
                for (int i = 0; i < MainScreenController.calendar.getAppointmentList().size(); i++) {
                    ZonedDateTime checkAppointmentStart = MainScreenController.calendar.getAppointmentList().get(i).getStart();
                    ZonedDateTime checkAppointmentEnd = MainScreenController.calendar.getAppointmentList().get(i).getEnd();
                    if ((!(startZdt.isBefore(checkAppointmentStart)) && !(startZdt.isAfter(checkAppointmentEnd))) || (!(endZdt.isBefore(checkAppointmentStart)) && !(endZdt.isAfter(checkAppointmentEnd)))) {
                        overlappingAppointment = true;
                    }
                }

                if (overlappingAppointment == true) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Overlapping Appointments");
                    alert.setContentText("The appointment you are attempting to schedule overlaps with another appointment. Please ensure that appointments do not overlap to continue.");
                    alert.show();
                } else {
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
                    insertAppointment.setString(9, startTimeUTC);
                    insertAppointment.setString(10, endTimeUTC);
                    insertAppointment.setString(11, Main.user.getUsername());
                    insertAppointment.setString(12, Main.user.getUsername());
                    insertAppointment.execute();

                    Appointment newAppointment = new Appointment(customerId, customerName, startZdt, endZdt, type, location);
                    MainScreenController.calendar.addAppointment(newAppointment);
                    Stage stage = (Stage) save.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }


    private boolean nullCheck() { //RETURNS TRUE IF A FIELD IS EMPTY.
        if (customerChoice.getSelectionModel().isEmpty() || datePicker.getValue().equals("") || hourChoice.getSelectionModel().isEmpty() || minutesChoice.getSelectionModel().isEmpty() || appointmentType.getText().isEmpty() || appointmentLocation.getText().isEmpty()) {
            return true;
        } else {
            return false;
        }

    }

    @FXML
    void initialize() {

        ObservableList<String> customerNames = FXCollections.observableArrayList();
        for (int i = 0; i < MainScreenController.customerRoster.getCustomerListSize(); i++) {
            customerNames.add(MainScreenController.customerRoster.getCustomerList().get(i).getCustomerName());
        }
        customerChoice.setItems(customerNames);

        ObservableList<String> hoursList = FXCollections.observableArrayList();
        ObservableList<String> minutesList = FXCollections.observableArrayList();

        hoursList.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutesList.addAll("00", "15", "30", "45");

        hourChoice.setItems(hoursList);
        minutesChoice.setItems(minutesList);

        hourLengthChoice.setItems(hoursList);
        minutesLengthChoice.setItems(minutesList);


    }
}
