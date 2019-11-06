package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class AddAppointmentController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField date;

    @FXML
    private TextField type;

    @FXML
    private TextField appointmentLocation;

    @FXML
    private ChoiceBox<Customer> customerChoice;

    @FXML
    private Button save;

    @FXML
    private Button cancel;

    @FXML
    void cancelHandler(ActionEvent event) {

    }

    @FXML
    void saveHandler(ActionEvent event) {

    }

    @FXML
    void initialize() {
       customerChoice.setItems(MainScreenController.customerRoster.getCustomerList().;);

    }
}
