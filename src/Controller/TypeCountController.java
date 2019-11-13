package Controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Model.Main;
import Model.ReportType;
import Model.Type;
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

public class TypeCountController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button cancel;

    @FXML
    private ChoiceBox<String> monthChoice;

    @FXML
    private Button generate;

    @FXML
    private TableView<Type> typeTable;

    @FXML
    private TableColumn<?, ?> type;

    @FXML
    private TableColumn<?, ?> quantity;

    private ReportType collection = new ReportType();

    @FXML
    void cancelHandler(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void generateHandler(ActionEvent event) throws SQLException {
        collection.getTypes().clear();
        String month = monthChoice.getValue();
        PreparedStatement groupByType = Main.conn.prepareStatement("SELECT type, COUNT(*) as quantity FROM U06aua.appointment WHERE  Month(start) = ? GROUP BY type;");
        groupByType.setString(1, month);
        groupByType.execute();
        ResultSet groupByTypeRS = groupByType.executeQuery();
        while(groupByTypeRS.next()){
            Type type = new Type();
            type.setType(groupByTypeRS.getString("type"));
            type.setQuantity(groupByTypeRS.getInt("quantity"));
            collection.addType(type);
        }
        System.out.println(collection.getTypes().get(0).getType());
        typeTable.setItems(collection.getTypes());
        typeTable.refresh();
    }

    @FXML
    void initialize() {
        ObservableList<String> monthsList = FXCollections.observableArrayList();
        monthsList.addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        monthChoice.setItems(monthsList);
        monthChoice.setValue("01");

        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

    }
}
