package Controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import Model.Appointment;
import Model.Calendar;
import Model.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ConsultantTableController {

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
    private TableView<Appointment> appointmentTable;

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

    public static Calendar calendarConsultant = new Calendar();

    @FXML
    void cancelHandler(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void generateHandler(ActionEvent event) throws SQLException {
        String username = userChoice.getValue().toString();
        calendarConsultant.getAppointmentList().clear();
        calendarConsultant.populateCalendarByConsultant(username);
        appointmentTable.refresh();
        appointmentTable.setItems(calendarConsultant.getAppointmentList());
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
