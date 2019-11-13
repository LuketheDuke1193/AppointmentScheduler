package Controller;

import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import Model.CustomerRoster;
import Model.Main;
import Model.User;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.log.Log;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jdk.jfr.internal.Logger;
import Utilities.*;

import javax.swing.text.Utilities;

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
    private Label appLabel;

    @FXML
    private Label hoursLabel;
    private LoggerHandler LoggerHandler;
    private static final String fileName = "log.txt";

    public static void log(boolean successfulLogin, String user) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter outputStream = new PrintWriter(bufferedWriter);
        if (successfulLogin == true) {
            String success = "successfully";
            outputStream.println(ZonedDateTime.now() + " - " + user + " tried to log in " + success + ".");
            outputStream.close();
        } else {
            String success = "unsuccessfully";
            outputStream.println(ZonedDateTime.now() + " - " + user + " tried to log in " + success + ".");
            outputStream.close();
        }
    }


    @FXML
    void englishButtonHandler(ActionEvent event) {
        if (englishButton.selectedProperty().getValue() == true || isInEnglishLocale()){
            changeButtonsToEnglish.change();
        }
    }

    boolean isInSpanishLocale(){
        String language = Locale.getDefault().getLanguage();
        if (language == "ES" || language == "es"){
            return true;
        } else {
            return false;
        }
    }

    boolean isInEnglishLocale(){
        String language = Locale.getDefault().getLanguage();
        if (language == "EN" || language == "en"){
            return true;
        } else {
            return false;
        }
    }

    @FXML
    void espanolButtonHandler(ActionEvent event) {
        if ((espanolButton.selectedProperty().getValue() == true) || isInSpanishLocale()){
            changeButtonsToSpanish.change();
        }
    }

    @FXML
    void loginButtonHandler(ActionEvent event) throws SQLException, IOException {
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
                    LoggerHandler.log(true, username);
                    Main.user.setUsername(username);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainScreen.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();//Tells main screen runtime to pause and wait for the new window to close before proceeding.
                    //Login and load next screen.
                    Stage loginStage = (Stage) loginButton.getScene().getWindow();
                    loginStage.close();
                } else {
                    if (espanolButton.selectedProperty().getValue() == true || isInSpanishLocale()) {
                        LoggerHandler.log(false, username);
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("El nombre de usuario y la contraseña no coinciden.");
                        alert.setContentText("Has ingresado una contraseña inválida. Inténtalo de nuevo.");
                        alert.show();
                    } else if (englishButton.selectedProperty().getValue() == true || isInEnglishLocale()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        LoggerHandler.log(false, username);
                        alert.setTitle("Username and password did not match.");
                        alert.setContentText("You have entered an invalid password. Please try again.");
                        alert.show();
                    }
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                if (espanolButton.selectedProperty().getValue() == true || isInSpanishLocale()) {
                    LoggerHandler.log(false, username);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error de inicio de sesión");
                    alert.setContentText("Hubo un error durante el inicio de sesión. O su nombre de usuario es incorrecto o no está en nuestro sistema.");
                    alert.show();
                } else if (englishButton.selectedProperty().getValue() == true || isInEnglishLocale()) {
                    LoggerHandler.log(false, username);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Login Error");
                    alert.setContentText("There was an error during login. Either your username is incorrect or it is not in our system.");
                    alert.show();
                }
            }
        } else {
            if (espanolButton.selectedProperty().getValue() == true || isInSpanishLocale()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error de inicio de sesión");
                alert.setContentText("Asegúrese de que los campos de nombre de usuario y contraseña no estén vacíos.");
                alert.show();
            } else if (englishButton.selectedProperty().getValue() == true || isInEnglishLocale()) {
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

    LanguageChange changeButtonsToSpanish = () -> { //Implemented Lambda here so button/label changing functions wouldn't have to be entered 3 separate times.
        loginButton.setText("Iniciar Sesión");
        usernameField.setPromptText("Nombre de usuario");
        passwordField.setPromptText("Contraseña");
        exitButton.setText("Salida");
        appLabel.setText("Programador de citas");
        hoursLabel.setText("Horas de trabajo");
    };

    LanguageChange changeButtonsToEnglish = () -> { //Implemented Lambda here so button changing functions wouldn't have to be entered 3 separate times.
        loginButton.setText("Login");
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");
        exitButton.setText("Exit");
        appLabel.setText("Appointment Scheduler");
        hoursLabel.setText("Business Hours");
    };

    public interface LanguageChange {
        void change();
    }

    @FXML
    void initialize() {
        String locale = Locale.getDefault().getLanguage();
        CustomerRoster customerRoster = new CustomerRoster();
        if (isInSpanishLocale()){
            System.out.println(isInSpanishLocale());
            changeButtonsToSpanish.change();
            englishButton.selectedProperty().setValue(false);
            espanolButton.selectedProperty().setValue(true);
        }
    }
}
