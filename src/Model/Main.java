package Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    private static final String databaseName = "U06aua";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/" + databaseName;
    private static final String username = "U06aua";
    private static final String password = "53688712395";
    private static final String driver = "com.mysql.jdbc.Driver";
    public static Connection conn;

    public static Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        conn = (Connection) DriverManager.getConnection(DB_URL, username, password);
        System.out.println("Connection successful.");
        return null;
    }

    public static void closeConnection() throws SQLException {
        conn.close();
        System.out.println("Connection closed.");
    }
        @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LoginScreen.fxml"));
        Stage stage = new Stage();
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        makeConnection();
        launch(args);
        closeConnection();
    }
}
