package Controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Model.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.xml.transform.Result;

public class AppointmentCountConsultant {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button cancel;

    @FXML
    private ChoiceBox<String> userChoice;

    @FXML
    private Button generate;

    @FXML
    private Label outputLine;

    @FXML
    void cancelHandler(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void generateHandler(ActionEvent event) throws SQLException {
        PreparedStatement selectUser = Main.conn.prepareStatement("SELECT userId FROM U06aua.user WHERE userName =?;");
        selectUser.setString(1, userChoice.getValue());
        ResultSet userIdRS = selectUser.executeQuery();
        userIdRS.next();
        int userId = userIdRS.getInt("userId");
        PreparedStatement selectAppointment = Main.conn.prepareStatement("SELECT COUNT(appointmentId) FROM U06aua.appointment WHERE userId = ?;");
        selectAppointment.setInt(1, userId);
        ResultSet countRS = selectAppointment.executeQuery();
        countRS.next();
        int count = countRS.getInt("COUNT(appointmentId)");

        outputLine.setText(userChoice.getValue() + " has " + count + " appointment(s) currently scheduled.");
        outputLine.visibleProperty().setValue(true);


    }

    @FXML
    void initialize() throws SQLException {
        ObservableList<String> userList = FXCollections.observableArrayList();
        PreparedStatement selectUsers = Main.conn.prepareStatement("SELECT userName FROM U06aua.user;");
        ResultSet selectUsersRS = selectUsers.executeQuery();
        while (selectUsersRS.next()){
            String username = selectUsersRS.getString("userName");
            userList.add(username);
        }
        userChoice.setItems(userList);

    }
}
