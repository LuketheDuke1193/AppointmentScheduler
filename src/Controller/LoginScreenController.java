package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import Model.CustomerRoster;
import Model.Main;
import Model.User;
import com.mysql.jdbc.Statement;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button exitButton;

    @FXML
    private RadioButton englishButton;

    @FXML
    private ToggleGroup language;

    @FXML
    private RadioButton espanolButton;


    @FXML
    void englishButtonHandler(ActionEvent event) {
        if (englishButton.selectedProperty().getValue() == true){
            loginButton.setText("Login");
            usernameField.setPromptText("Username");
            passwordField.setPromptText("Password");
            exitButton.setText("Exit");

        }
    }

    @FXML
    void espanolButtonHandler(ActionEvent event) {
        if (espanolButton.selectedProperty().getValue() == true){
            loginButton.setText("Iniciar Sesión");
            usernameField.setPromptText("Nombre de usuario");
            passwordField.setPromptText("Contraseña");
            exitButton.setText("Salida");
        }
    }

    @FXML
    void loginButtonHandler(ActionEvent event) throws SQLException {
        if ((!usernameField.getText().isEmpty()) && (!passwordField.getText().isEmpty())){
            //Grabs text from usernameField/passwordField
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            try {
                //Grabs user and pass from MySQL based on input text.
                Statement loginDetails = (Statement) Main.conn.createStatement();
                ResultSet rsUser = loginDetails.executeQuery("SELECT userName FROM U06aua.user WHERE userName='" + username + "';");
                rsUser.next();
                String dbUsername = rsUser.getString(1);
                ResultSet rsPass = loginDetails.executeQuery("SELECT password FROM U06aua.user WHERE userName='" + username + "';");
                rsPass.next();
                String dbPassword = rsPass.getString(1);

                if (username.equals(dbUsername) && password.equals(dbPassword)) {
                    System.out.println("login success");
                    Main.user.setUsername(username);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainScreen.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show(); //Tells main screen runtime to pause and wait for the new window to close before proceeding.
                    //Login and load next screen.
                } else {
                    System.out.println("User and pass did not match");
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                if (espanolButton.selectedProperty().getValue() == true) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error de inicio de sesión");
                    alert.setContentText("Hubo un error durante el inicio de sesión. O su nombre de usuario es incorrecto o no está en nuestro sistema.");
                    alert.show();
                } else if (englishButton.selectedProperty().getValue() == true) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Login Error");
                    alert.setContentText("There was an error during login. Either your username is incorrect or it is not in our system.");
                    alert.show();
                }
            }
        } else {
            if (espanolButton.selectedProperty().getValue() == true) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error de inicio de sesión");
                alert.setContentText("Asegúrese de que los campos de nombre de usuario y contraseña no estén vacíos.");
                alert.show();
            } else if (englishButton.selectedProperty().getValue() == true) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login Error");
                alert.setContentText("Please ensure that both the username and password fields are not empty.");
                alert.show();
            }
        }
    }


    @FXML
    void exitButtonHandler(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void initialize() {
        Locale locale = Locale.getDefault();

        CustomerRoster customerRoster = new CustomerRoster();

    }
}
