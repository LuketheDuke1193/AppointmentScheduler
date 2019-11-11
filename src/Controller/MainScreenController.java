package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    private TableColumn<?, ?> date;

    @FXML
    private TableColumn<?, ?> type;

    @FXML
    private TableColumn<?, ?> locationColumn;
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

    @FXML
    void appointmentTableHandler(ActionEvent event) {
        //TODO
    }

    @FXML
    void customerTableHandler(ActionEvent event) {
        //TODO
    }

    @FXML
    void deleteAppointmentHandler(ActionEvent event) throws SQLException {
        PreparedStatement deleteAppointment = Main.conn.prepareStatement("DELETE FROM U06aua.appointment WHERE start = ?");
        String dateTime = appointmentTable.getSelectionModel().getSelectedItem().getDate().toString();
        deleteAppointment.setString(1, dateTime);
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
        //TODO
    }

    @FXML
    void generateAvgPerWeekHandler(ActionEvent event) {
        //TODO
    }

    @FXML
    void generateConsultantScheduleHandler(ActionEvent event) {

    }

    @FXML
    void generateTypeCountHandler(ActionEvent event) {
        //TODO
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(date));
    }

    @FXML
    void viewByWeekHandler(ActionEvent event) {
        //TODO
    }

    public void generateCustomerTable() throws SQLException {
        customerRoster.populateRoster();
        customerTable.setItems(customerRoster.getCustomerList());
    }

    public void generateAppointmentTable() throws SQLException {
        calendar.populateCalendar();
        appointmentTable.setItems(calendar.getAppointmentList());
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        generateCustomerTable();
        customerTable.refresh();
        generateAppointmentTable();




        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerAppointmentName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));


    }
}
