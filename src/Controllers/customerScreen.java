package Controllers;

import Containers.countryDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.sql;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class customerScreen implements Initializable {

    @FXML
    private TextField addr;
    @FXML
    private Button clearButton;
    @FXML
    private ComboBox<String> countryCombo;
    @FXML
    private TextField customerID;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField postalCode;
    @FXML
    private Button saveButton;
    @FXML
    private Text province;
    @FXML
    private Text state;
    @FXML
    private Text country;
    @FXML
    private ComboBox<String> stateProvinceCombo;


    /**
     * Validates whether the customer is valid and returns a string of errors based on what fails in the validation checks.
     * @param exception
     * @return
     * @throws SQLException
     */
    public String isCustomerValid(String exception) throws SQLException {

        if(firstName.getText().length() == 0)
            exception = exception + "You must enter a first name.\n";

        if(lastName.getText().length() == 0)
            exception = exception + "You must enter a last name.\n";

        if(addr.getText().length() == 0)
            exception = exception + "You must enter an address.\n";

        if(postalCode.getText().isEmpty())
            exception = exception + "You must enter a postal code.\n";

        if(phoneNumber.getText().isEmpty())
            exception = exception + "You must enter a phone number.\n";

        if(doesCustomerExist((firstName.getText() + " " + lastName.getText()), addr.getText()))
            exception = exception + "This customer already exists. Please check the customer record";

        return exception;
    }

    /**
     * This function validates whether the customer exists already or not within the database. It returns a boolean value depending on what the query returns.
     * @param name
     * @param addr
     * @return
     * @throws SQLException
     */
    public boolean doesCustomerExist(String name, String addr) throws SQLException {

        PreparedStatement ps = sql.getConn().prepareStatement("SELECT * FROM customers WHERE Customer_Name=? AND Address=?;");
        ps.setString(1, name);
        ps.setString(2, addr);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    /**
     * Closes the current scene and returns back to the main window.
     * @param event
     * @throws IOException
     */
    @FXML
    public void onCancel(Event event) throws IOException {
        Parent modifyInstance = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../FXML/mainScreen.fxml")));
        Scene scene = new Scene(modifyInstance);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * Function runs validation checks based on what is in the form, and then passes it to a sql query to add the user to the database as long as checks dont fail.
     * @throws SQLException
     */
    @FXML
    private void onSave() throws SQLException {

        String cName = firstName.getText() + " " + lastName.getText();
        String pNumber = phoneNumber.getText();
        String stateProv = stateProvinceCombo.getSelectionModel().getSelectedItem();
        String address = addr.getText();
        String pCode = postalCode.getText();
        String exception = "";
        String user = LoginScreen.getSession().getUserName();
        String datetime;

        DateTimeFormatter ldtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        ZonedDateTime zdtUTC = zdt.withZoneSameInstant(ZoneOffset.UTC);

        exception = isCustomerValid(exception);

        int dID = 1;
        if(exception.length() == 0) {
            PreparedStatement ps1 = sql.getConn().prepareStatement("SELECT Division_ID FROM first_level_divisions WHERE Division=?;");
            ps1.setString(1, stateProv);
            ResultSet rs1 = ps1.executeQuery();

            while (rs1.next()) {
                dID = Integer.parseInt(rs1.getString("Division_ID"));
            }


            PreparedStatement ps = sql.getConn().prepareStatement("INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By ,Division_ID) VALUES (?,?,?,?,?,?,?,?,?);");
            ps.setString(1, cName);
            ps.setString(2, address);
            ps.setString(3, pCode);
            ps.setString(4, pNumber);
            ps.setTimestamp(5, Timestamp.valueOf(zdtUTC.toLocalDateTime()));
            ps.setString(6, user);
            ps.setTimestamp(7, Timestamp.valueOf(zdtUTC.toLocalDateTime()));
            ps.setString(8, user);
            ps.setInt(9, dID);
            ps.executeUpdate();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unable to create customer.");
            alert.setHeaderText("Error");
            alert.setContentText(exception);
            alert.showAndWait();
        }
    }

    /**
     * Fills the countries and first level divisions within the table. Also changes a text field depending on the value of the country.
     */
    @FXML
    private void comboBoxFiller() {

        String countrySel = countryCombo.getSelectionModel().getSelectedItem().toString();
        String cID = null;
        ObservableList<String> allFLD = FXCollections.observableArrayList();
        ResultSet rs;
        PreparedStatement ps;

        try {
            ps = sql.getConn().prepareStatement("SELECT Country_ID FROM countries WHERE Country=?;");
            ps.setString(1, countrySel.trim());
            rs = ps.executeQuery();

            while (rs.next()) {
                cID = rs.getString("Country_ID");
            }

            ps = sql.getConn().prepareStatement("SELECT * FROM first_level_divisions WHERE Country_ID=? ORDER BY Division;");
            ps.setString(1, cID.trim());
            rs = ps.executeQuery();

            while (rs.next()) {
                String fld = rs.getString("Division");
                allFLD.add(fld);
            }

            if(countrySel.trim().equals("U.S"))
            {
                state.setVisible(true);
                province.setVisible(false);
                country.setVisible(false);
            }
            else if (countrySel.trim().equals("UK"))
            {
                province.setVisible(false);
                state.setVisible(false);
                country.setVisible(true);
            }
            else if (countrySel.trim().equals("Canada")){
                province.setVisible(true);
                state.setVisible(false);
                country.setVisible(false);
            }

            stateProvinceCombo.setVisible(true);
            stateProvinceCombo.setItems(allFLD);

        } catch (SQLException ex){
            System.out.print(ex);
        }
    }

    /**
     * Initializes the screen by loading region/language preferences, as well as set the text fields to invisible.
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stateProvinceCombo.setVisible(false);
        state.setVisible(false);
        province.setVisible(false);
        country.setVisible(false);

        countryCombo.setItems(countryDB.getAllCountries());
    }
}
