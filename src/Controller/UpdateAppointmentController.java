package Controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.ResourceBundle;

import Model.Customer;
import Model.CustomerRoster;
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

    @FXML
    private ChoiceBox<String> hourLengthChoice;

    @FXML
    private ChoiceBox<String> minutesLengthChoice;

    int appointmentID;

    @FXML
    void cancelHandler(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
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
            LocalTime time = LocalTime.parse(hour + ":" + minutes + ":00");

            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime startZdt = ZonedDateTime.of(date, time, zoneId); //Local Time
            ZonedDateTime startZDTUTC = startZdt.withZoneSameInstant(ZoneOffset.UTC); //UTC Time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startTimeUTC = startZDTUTC.format(formatter); //UTC DB format.
            String startTime = startZdt.format(formatter); //Local DB format.


            long hoursLength = Long.valueOf(hourLengthChoice.getValue());
            long minutesLength = Long.valueOf((String) minutesLengthChoice.getValue());
            ZonedDateTime endZdt = startZdt.plusHours(hoursLength);
            ZonedDateTime endZDTUTC = endZdt.withZoneSameInstant(ZoneOffset.UTC);
            endZdt.plusMinutes(minutesLength);
            endZDTUTC.plusMinutes(minutesLength);
            String endTimeUTC = endZDTUTC.format(formatter);
            LocalTime appointmentEnd = endZDTUTC.toLocalTime();
            LocalTime appointmentBegin = startZDTUTC.toLocalTime();

            if ((appointmentBegin.isAfter(Main.closedForBusiness) || appointmentBegin.isBefore(Main.openForBusiness)) || (appointmentEnd.isAfter(Main.closedForBusiness) || appointmentEnd.isBefore(Main.openForBusiness))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Appointment Hours Error");
                alert.setContentText("Appointments must be scheduled during business hours.");
                alert.show();

            } else {
                boolean overlappingAppointment = false;
                for (int i = 0; i < MainScreenController.calendar.getAppointmentList().size(); i++) {
                    if (MainScreenController.calendar.getAppointmentList().get(i) != MainScreenController.selectedAppointment){
                        ZonedDateTime checkAppointmentStart = MainScreenController.calendar.getAppointmentList().get(i).getStart();
                        ZonedDateTime checkAppointmentEnd = MainScreenController.calendar.getAppointmentList().get(i).getEnd();
                        if ((!(startZdt.isBefore(checkAppointmentStart)) && !(startZdt.isAfter(checkAppointmentEnd))) || (!(endZdt.isBefore(checkAppointmentStart)) && !(endZdt.isAfter(checkAppointmentEnd)))) {
                            overlappingAppointment = true;
                        }
                    }
                }

                if (overlappingAppointment == true) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Overlapping Appointments");
                    alert.setContentText("The appointment you are attempting to schedule overlaps with another appointment. Please ensure that appointments do not overlap to continue.");
                    alert.show();
                } else {
                    MainScreenController.selectedAppointment.setCustomerName(customerName);
                    MainScreenController.selectedAppointment.setStart(startZdt);
                    MainScreenController.selectedAppointment.setType(type);
                    MainScreenController.selectedAppointment.setLocation(location);

                    PreparedStatement customerDB = Main.conn.prepareStatement("SELECT customerId FROM U06aua.customer WHERE customerName = ?;");
                    customerDB.setString(1, customerName);
                    ResultSet customerDBRS = customerDB.executeQuery();
                    customerDBRS.next();
                    int customerId = customerDBRS.getInt("customerId");

                    PreparedStatement appointmentUpdate = Main.conn.prepareStatement("UPDATE U06aua.appointment SET " +
                            "customerId = ?,  location = ?, type = ? ,start = ?, end = ?, lastUpdate = now(), lastUpdateBy = ? WHERE" +
                            " appointmentId = ?");
                    appointmentUpdate.setInt(1, customerId);
                    appointmentUpdate.setString(2, location);
                    appointmentUpdate.setString(3, type);
                    appointmentUpdate.setString(4, startTimeUTC);
                    appointmentUpdate.setString(5, endTimeUTC);
                    appointmentUpdate.setString(6, Main.user.getUsername());
                    appointmentUpdate.setInt(7, appointmentID);
                    appointmentUpdate.execute();
                    Stage stage = (Stage) save.getScene().getWindow();
                    stage.close();


                }
            }
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
        hourLengthChoice.setItems(hoursList);
        minutesLengthChoice.setItems(minutesList);

        for (int i = 0; i < MainScreenController.customerRoster.getCustomerListSize(); i++){
            customerNames.add(MainScreenController.customerRoster.getCustomerList().get(i).getCustomerName());
        }
        customerChoice.setItems(customerNames);
        customerChoice.setValue(MainScreenController.selectedAppointment.getCustomerName());
        LocalDate date = MainScreenController.selectedAppointment.getStart().toLocalDateTime().toLocalDate();
        datePicker.setValue(date);
        ZonedDateTime start = MainScreenController.selectedAppointment.getStart();
        ZonedDateTime startUTC = start.withZoneSameInstant(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTime = startUTC.format(formatter);
        ZonedDateTime end = MainScreenController.selectedAppointment.getEnd();
        Duration length = Duration.between(start, end);
        int hoursLength = length.toHoursPart();
        int minutesLength = length.toMinutesPart();
        String formatHoursLength = String.format("%02d", hoursLength);
        String formatMinutesLength = String.format("%02d", minutesLength);
        int hours = start.getHour();
        int minutes = start.getMinute();
        String formatHours = String.format("%02d", hours);
        String formatMinutes = String.format("%02d", minutes);

        hourChoice.setValue(String.valueOf(formatHours));
        minutesChoice.setValue(String.valueOf(formatMinutes));
        hourLengthChoice.setValue(formatHoursLength);
        minutesLengthChoice.setValue(formatMinutesLength);

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
