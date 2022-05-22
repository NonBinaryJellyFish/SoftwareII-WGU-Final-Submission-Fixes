package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.JDBC;
import utils.sql;
import Models.userSession;


import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginScreen implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button resetButton;
    @FXML
    private Label timeZoneLabel;
    @FXML
    private Text usernameLabel;
    @FXML
    private Text passwordLabel;
    @FXML
    private Button loginButton;
    private static userSession session;

    public LoginScreen() throws IOException {
    }

    /**
     * This validates the input on the login.properties screen.
     * @param conn - The static connection that will be used throughout the application.
     * @return
     * @throws Exception
     */
    private boolean loginQuery() throws Exception{
        try {
            userSession tmp = null;
            if(getUsernameTextField() != null)
                tmp= new userSession(getUsernameTextField().getText().trim());
            ResourceBundle resourceBundle = ResourceBundle.getBundle("utils/lang/login", Locale.getDefault());
            String sql_stmt = "SELECT * FROM users where User_Name=? AND Password=?;";
            PreparedStatement ps = sql.getConn().prepareStatement(sql_stmt);
            if(passwordTextField != null || usernameTextField != null){
                ps.setString(1, getUsernameTextField().getText());
                ps.setString(2, getPasswordField().getText());
            }
            if (getUsernameTextField().getText().length() == 0 || getPasswordField().getText().length() == 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Unable to login");
                    alert.setHeaderText("Invalid login");
                    alert.setContentText("Please enter your username and password.");
                    alert.showAndWait();
                    return false;
                }
            else {
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.isBeforeFirst() || resultSet.next()) {
                    while(resultSet.next())
                    {
                        tmp.setUserID(resultSet.getInt("User_ID"));
                        session = tmp;
                    }
                    return true;
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Unable to login");
                    alert.setHeaderText("Invalid login");
                    alert.setContentText(resourceBundle.getString("Incorrect"));
                    alert.showAndWait();
                    return false;
                }
            }
            } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            throw sqlExcept;
        }
    }

    /**
     * This clears the login.properties screen, so that the user can input new credentials easily.
     */
    @FXML
    public void clearLogin() {
        usernameTextField.clear();
        passwordTextField.clear();
    }

    /**
     * This function is used to log the user into the database after performing a validation check for both the username, password, and invalid entries.
     * @param event
     * @throws IOException
     */
    @FXML
    private void loginProc(ActionEvent event) throws IOException {
        FileWriter fileWriter = new FileWriter("login_activity.txt", true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        LocalDateTime dt = LocalDateTime.now();
        boolean tmp = false;
        try{
            tmp = loginQuery();
            if(tmp){
                outputFile.print(getSession().getUserName() + ": Logged in on - " + dt.toLocalDate() + ": " + dt.toLocalTime() + "\n");
                Parent modifyInstance = FXMLLoader.load(getClass().getResource("../FXML/mainScreen.fxml"));
                Scene scene = new Scene(modifyInstance);
                Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
            }
            else {
                outputFile.print(usernameTextField.getText() + ": attempted to logon on - " + dt.toLocalDate() + ": " + dt.toLocalTime() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        outputFile.close();
    }


    //Getter Setter functions for the JavaFX items on screen.
    public TextField getUsernameTextField() {
        return usernameTextField;
    }

    public void setUsernameTextField(TextField usernameTextField) {
        this.usernameTextField = usernameTextField;
    }

    public TextField getPasswordField() {
        return passwordTextField;
    }

    public void setPasswordField(PasswordField passwordTextField) {
        this.passwordTextField = passwordTextField;
    }

    /**
     * This returns the userSession statically so that it can be referred to later when obtaining the userID and userName for the logged in user.
     * @return
     */
    public static userSession getSession() {
        return session;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sql.openConnection();
        Locale systemConf = Locale.getDefault();
        Locale.setDefault(systemConf);

        ZoneId system = ZoneId.systemDefault();

        timeZoneLabel.setText(timeZoneLabel.getText() + " " + system.toString());

        resourceBundle = ResourceBundle.getBundle("utils/lang/login", Locale.getDefault());
        usernameLabel.setText(resourceBundle.getString("Username"));
        passwordLabel.setText(resourceBundle.getString("Password"));
        loginButton.setText(resourceBundle.getString("Login"));
        resetButton.setText(resourceBundle.getString("Reset"));
        timeZoneLabel.setText(resourceBundle.getString("Location") + ": " +system);
    }
}
