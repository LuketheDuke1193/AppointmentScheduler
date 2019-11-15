package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import Model.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;

public class MainScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private Button updateAppointment;

    @FXML
    private Button deleteAppointment;

    @FXML
    private Button addAppointment;

    @FXML
    private Button addCustomer;

    @FXML
    private Button updateCustomer;

    @FXML
    private Button deleteCustomer;

    @FXML
    private Button viewByMonth;

    @FXML
    private Button viewByWeek;

    @FXML
    private Button viewByAll;

    @FXML
    private Button generateTypeCount;

    @FXML
    private Button generateConsultantSchedule;

    @FXML
    private Button generateAvgPerWeek;

    @FXML
    private Button exitButton;

    @FXML
    private TableColumn<?, ?> customerName;

    @FXML
    private TableColumn<?, ?> phoneNumber;

    @FXML
    private TableColumn<?, ?> customerID;

    @FXML
    private TableColumn<?, ?> customerAppointmentName;

    @FXML
    private TableColumn<Appointment, String> date;

    @FXML
    private TableColumn<?, ?> type;

    @FXML
    private TableColumn<?, ?> locationColumn;

    @FXML
    private Label businessHours;

    public static CustomerRoster customerRoster = new CustomerRoster();
    public static Customer selectedCustomer;
    public static Calendar calendar = new Calendar();
    public static Appointment selectedAppointment;
    @FXML
    void addAppointmentHandler(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddAppointment.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        generateAppointmentTable();
    }

    @FXML
    void addCustomerHandler(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddCustomer.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        generateCustomerTable();
    }

   /* @FXML
    void appointmentTableHandler(ActionEvent event) {
        //TODO
    }

    @FXML
    void customerTableHandler(ActionEvent event) {
        //TODO
    }*/

    @FXML
    void deleteAppointmentHandler(ActionEvent event) throws SQLException {
        PreparedStatement deleteAppointment = Main.conn.prepareStatement("DELETE FROM U06aua.appointment WHERE start = ?");
        ZonedDateTime dateTime = appointmentTable.getSelectionModel().getSelectedItem().getStart();
        ZonedDateTime dateTimeUTC = dateTime.withZoneSameInstant(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeUTCDB = dateTimeUTC.format(formatter);
        deleteAppointment.setString(1, dateTimeUTCDB);
        deleteAppointment.execute();
        calendar.deleteAppointment(appointmentTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void deleteCustomerHandler(ActionEvent event) throws SQLException {
        String customerName;
        int customerId;
        customerName = customerTable.getSelectionModel().getSelectedItem().getCustomerName();
        customerId = customerTable.getSelectionModel().getSelectedItem().getId();
        customerRoster.deleteCustomer(customerTable.getSelectionModel().getSelectedItem());
        PreparedStatement deleteCustomer = Main.conn.prepareStatement("DELETE FROM U06aua.customer WHERE customerId = ?;");
        deleteCustomer.setInt(1, customerId);
        deleteCustomer.execute();
    }

    @FXML
    void exitButtonHandler(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void generateConsultantAppointmentCount(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentCountConsultant.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    void generateConsultantScheduleHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ConsultantTable.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    void generateTypeCountHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/TypeCount.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    void updateAppointmentHandler(ActionEvent event) throws IOException, SQLException {
        selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/UpdateAppointment.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        generateAppointmentTable();
    }



    @FXML
    void updateCustomerHandler(ActionEvent event) throws SQLException, IOException {
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/UpdateCustomer.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        generateCustomerTable();

    }

    @FXML
    void viewByAllHandler(ActionEvent event) throws SQLException {
        generateAppointmentTable();
    }

    @FXML
    void viewByMonthHandler(ActionEvent event) {
        LocalDate now = LocalDate.now();
        LocalDate oneMonthFromNow = now.plusMonths(1);

        Calendar calendarOneMonthOut = new Calendar();
        for (int i = 0; i < calendar.getAppointmentList().size(); i++){
            if (calendar.getAppointmentList().get(i).getStart().toLocalDateTime().toLocalDate().isBefore(oneMonthFromNow) && calendar.getAppointmentList().get(i).getStart().toLocalDateTime().toLocalDate().isAfter(now)){
                calendarOneMonthOut.addAppointment(calendar.getAppointmentList().get(i));
            }
        }
        appointmentTable.setItems(calendarOneMonthOut.getAppointmentList());
    }

    @FXML
    void viewByWeekHandler(ActionEvent event) {
        LocalDate now = LocalDate.now();
        LocalDate oneWeekFromNow = now.plusWeeks(1);

        Calendar calendarOneWeekOut = new Calendar();
        for (int i = 0; i < calendar.getAppointmentList().size(); i++){
            if (calendar.getAppointmentList().get(i).getStart().toLocalDateTime().toLocalDate().isBefore(oneWeekFromNow) && calendar.getAppointmentList().get(i).getStart().toLocalDateTime().toLocalDate().isAfter(now)){
                calendarOneWeekOut.addAppointment(calendar.getAppointmentList().get(i));
            }
        }
        appointmentTable.setItems(calendarOneWeekOut.getAppointmentList());
    }

    public void generateCustomerTable() throws SQLException {
        customerRoster.populateRoster();
        customerTable.setItems(customerRoster.getCustomerList());
    }

    public void generateAppointmentTable() throws SQLException {
        calendar.populateCalendar();
        appointmentTable.setItems(calendar.getAppointmentList());
    }

    void upcomingAppointment(){
        for (int i = 0; i < MainScreenController.calendar.getAppointmentList().size(); i++){
            ZonedDateTime upcomingAppointmentTime = MainScreenController.calendar.getAppointmentList().get(i).getStart();
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime fifteenMinutesFromNow = now.plusMinutes(15);
            if (upcomingAppointmentTime.isBefore(fifteenMinutesFromNow) && upcomingAppointmentTime.isAfter(now)){
                Alert upcomingAlert = new Alert(Alert.AlertType.WARNING, "You have an upcoming appointment " +"within the next fifteen minutes with " + MainScreenController.calendar.getAppointmentList().get(i).getCustomerName() + ".", ButtonType.OK, ButtonType.CANCEL);
                Stage stage = (Stage) upcomingAlert.getDialogPane().getScene().getWindow();
                stage.setAlwaysOnTop(true);
                stage.toFront();
                stage.show();
                System.out.println("showing alert...");
            }
        }
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException, InterruptedException {
        generateCustomerTable();
        customerTable.refresh();
        generateAppointmentTable();
        upcomingAppointment();

        businessHours.setText(LoginScreenController.businessHoursToLocal());




        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerAppointmentName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        date.setCellValueFactory(
                (Appointment) -> {
                    ZonedDateTime zdt = Appointment.getValue().getStart();
                    SimpleStringProperty dateValue = new SimpleStringProperty();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    dateValue.setValue(formatter.format(zdt));
                    return dateValue;
                }); //Rather than individually going through and reformatting the ZonedDateTime for each appointment instance, I utilize this lambda expression to reformat the data at the front-end.
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));



        }


    }

