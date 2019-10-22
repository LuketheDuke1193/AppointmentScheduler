package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    public ObservableList<Customer> customerDBList = FXCollections.observableArrayList();


    @FXML
    void addAppointmentHandler(ActionEvent event) {
        //TODO
    }

    @FXML
    void addCustomerHandler(ActionEvent event) {
        //TODO
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
    void deleteAppointmentHandler(ActionEvent event) {
        //TODO
    }

    @FXML
    void deleteCustomerHandler(ActionEvent event) {
        //TODO
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
    void updateAppointmentHandler(ActionEvent event) {
        //TODO
    }

    @FXML
    void updateCustomerHandler(ActionEvent event) {
        //TODO
    }

    @FXML
    void viewByAllHandler(ActionEvent event) {
        //TODO
    }

    @FXML
    void viewByMonthHandler(ActionEvent event) {
        //TODO
    }

    @FXML
    void viewByWeekHandler(ActionEvent event) {
        //TODO
    }

    public void generateCustomerTable() throws SQLException {
        CustomerRoster customerRoster = new CustomerRoster();
        customerRoster.populateRoster();
        customerTable.setItems(customerRoster.getCustomerList());
    }

    public void generateAppointmentTable() throws SQLException {
        Calendar calendar = new Calendar();
        calendar.populateCalendar();
        appointmentTable.setItems(calendar.getAppointmentList());
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        generateCustomerTable();
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
