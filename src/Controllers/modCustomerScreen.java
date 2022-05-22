package Controllers;

import Containers.countryDB;
import Containers.customerDB;
import Containers.fldDB;
import Models.Appointments;
import Models.Customers;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class modCustomerScreen implements Initializable {

    @FXML
    private TextField addr;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> countryCombo;

    @FXML
    private TextField customerID;

    @FXML
    private TableColumn<Customers, Number> customerIDCol;

    @FXML
    private TableColumn<Customers, String> customerNameCol;

    @FXML
    private TableView<Customers> customerTable;

    @FXML
    private TableColumn<Customers, String> customerPhoneCol;

    @FXML
    private TableColumn<Customers, String> customerAddrCol;

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
    private Text province;

    @FXML
    private Button saveButton;

    @FXML
    private TextField searchField;

    @FXML
    private Text state;

    @FXML
    private Text country;

    @FXML
    private ComboBox<String> stateProvinceCombo;

    private ObservableList<Customers> customers;

    /**
     * Validates whether the customer is valid and returns a string of errors based on what fails in the validation checks.
     * @param exception
     * @return
     * @throws SQLException
     */
    private String isCustomerValid(String exception) throws SQLException {

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

        return exception;
    }

    /**
     * Deletes the customer and any appointments associated to the user. It runs a validation check and alerts the user as to whether they want to complete
     * this action or not.
     * @param event
     * @throws SQLException
     */
    @FXML
    void onDelete(ActionEvent event) throws SQLException {
        if(customerTable.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Null Customer");
            alert.setHeaderText("Customer Selection Invalid");
            alert.setContentText("You did not select a customer!");
            alert.showAndWait();
        } else {
            int id = customerTable.getSelectionModel().getSelectedItem().getCustomerID();
            PreparedStatement ps1 = sql.getConn().prepareStatement("DELETE FROM appointments WHERE Customer_ID=?;");
            PreparedStatement ps2 = sql.getConn().prepareStatement("DELETE FROM customers WHERE Customer_ID=?;");
            ps1.setInt(1, id);
            ps2.setInt(1, id);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText("Customer is about to be deleted.");
            alert.setContentText("Are you sure you want to delete this customer: " + firstName.getText() + " " + lastName.getText() + "?\n" +
                    "This will delete all of their appointments as well!");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get() == ButtonType.OK) {
               ps1.executeUpdate();
               ps2.executeUpdate();
               customers = customerDB.getAllCustomers();
               customerTable.setItems(customers);
            }
            else if(option.get() == ButtonType.CANCEL) {
                alert.close();
            }
        }
    }

    /**
     * Function runs validation checks based on what is in the form, and then passes it to a sql query to add the user to the database as long as checks dont fail.
     * @throws SQLException
     */
    @FXML
    void onSave(ActionEvent event) throws SQLException {
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


            PreparedStatement ps = sql.getConn().prepareStatement("UPDATE customers SET Customer_Name=? , Address=? , Postal_Code=? , Phone=? , Create_Date=? , Created_By=? , Last_Update =?, Last_Updated_By=? ,Division_ID=? WHERE Customer_ID=?;");
            ps.setString(1, cName);
            ps.setString(2, address);
            ps.setString(3, pCode);
            ps.setString(4, pNumber);
            ps.setTimestamp(5, Timestamp.valueOf(zdtUTC.toLocalDateTime()));
            ps.setString(6, user);
            ps.setTimestamp(7, Timestamp.valueOf(zdtUTC.toLocalDateTime()));
            ps.setString(8, user);
            ps.setInt(9, dID);
            ps.setInt(10, Integer.parseInt(customerID.getText().trim()));
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
     * Closes the current scene and returns back to the main window.
     * @param event
     * @throws IOException
     */
    @FXML
    public void onCancel(ActionEvent event) throws IOException {
        Parent modifyInstance = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../FXML/mainScreen.fxml")));
        Scene scene = new Scene(modifyInstance);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * Fills the countries and first level divisions within the table. Also changes a text field depending on the value of the country.
     */
    @FXML
    private void comboBoxFiller() {

        String countrySel = countryCombo.getValue();
        String cID = customerID.getText();
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
     * Validates whether the search field value is an int.
     * @return
     */
    public boolean isInt() {
        int x = 0;
        try {
            x = Integer.parseInt(searchField.getText());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Initializes the screen by loading region/language preferences, as well as set the text fields to invisible.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        state.setVisible(false);
        country.setVisible(false);
        province.setVisible(false);

        stateProvinceCombo.setVisible(false);
        customers = FXCollections.observableArrayList();

        try {
            customers = customerDB.getAllCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        countryCombo.setItems(countryDB.getAllCountries());
        customerIDCol.setCellValueFactory(cd -> cd.getValue().customerIDProperty());
        customerNameCol.setCellValueFactory(cd -> cd.getValue().customerNameProperty());
        customerPhoneCol.setCellValueFactory(cd -> cd.getValue().phoneProperty());
        customerAddrCol.setCellValueFactory(cd -> Bindings.createStringBinding(
                () -> cd.getValue().getAddr() + ", " + cd.getValue().getFld() + ", " +
                        cd.getValue().getCountry()
        ));
        customerTable.setItems(customers);

        customerTable.getSelectionModel().selectedItemProperty().addListener((ob, oldSelect, newSelect) -> {

            try {
                stateProvinceCombo.setItems(fldDB.getAllDiv(String.valueOf(newSelect.getDivisionID())));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            customerID.setText(String.valueOf(newSelect.getCustomerID()));
            String fullName = newSelect.getCustomerName().trim();
            String[] fullNamesplitting = fullName.split("\\s+");
            firstName.setText(fullNamesplitting[0]);
            lastName.setText(fullNamesplitting[1]);
            phoneNumber.setText(newSelect.getPhone());
            countryCombo.setValue(newSelect.getCountry());
            stateProvinceCombo.setValue(newSelect.getFld());
            stateProvinceCombo.setVisible(true);
            addr.setText(newSelect.getAddr());
            postalCode.setText(newSelect.getPostalCode());
        });

        //Same lambda expression as a previous screen, justification for use here is all for accessibility so that users are able
        //to easily select customers so that they can modify them.

        FilteredList<Customers> filteredList = new FilteredList<>(customers, p -> true);
        searchField.textProperty().addListener((observable, oldV, newV) -> {
            filteredList.setPredicate(customers -> {
                if(newV == null || newV.isEmpty()){
                    return true;
                }

                String lowerCase = newV.toLowerCase();
                int apptIDv = customers.getCustomerID();

                if(customers.getCustomerName().toLowerCase().contains(lowerCase)) {
                    return true;
                }
                else if (isInt() && Integer.parseInt(newV) == apptIDv) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Customers> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(customerTable.comparatorProperty());
        customerTable.setItems(sortedList);

    }
}
