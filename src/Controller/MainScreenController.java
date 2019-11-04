package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
    @FXML
    void addAppointmentHandler(ActionEvent event) {
        //TODO
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
    void updateCustomerHandler(ActionEvent event) throws SQLException, IOException {
        //TODO: Grab selected customer and pass it to the UpdateCustomer screen and controller.
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
