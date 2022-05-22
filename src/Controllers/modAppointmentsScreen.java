package Controllers;

import Containers.customerDB;
import Models.Appointments;
import Models.Contacts;
import Models.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.sql;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class modAppointmentsScreen implements Initializable {

    @FXML
    private TableView<Customers> customerTable;
    @FXML
    private TableColumn<Customers, Number> custIDCol;
    @FXML
    private TableColumn<Customers, String> cNameCol;
    @FXML
    private TableColumn<Customers, String> cPhoneCol;
    @FXML
    private ComboBox<String> apptContact;
    @FXML
    private TextField apptDesc;
    @FXML
    private DatePicker apptEndDate;
    @FXML
    private ComboBox<String> apptEndTime;
    @FXML
    private TextField apptID;
    @FXML
    private Label apptLabel;
    @FXML
    private TextField apptLoc;
    @FXML
    private DatePicker apptStartDate;
    @FXML
    private ComboBox<String> apptStartTime;
    @FXML
    private TextField apptTitle;
    @FXML
    private TextField apptType;
    @FXML
    private Button cancelButton;
    @FXML
    private Label createdByText;
    @FXML
    private TextField customerID;
    @FXML
    private Button saveButton;
    @FXML
    private TextField userID_TF;
    @FXML
    private ObservableList<String> contact = FXCollections.observableArrayList();
    private Appointments appt;


    /**
     * Closes the current scene and returns the user to the main screen.
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
     * Saves the modified the appointment after running validation checks.s
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    public void saveModAppointment(ActionEvent event) throws SQLException, IOException {
        String apptTitle = this.apptTitle.getText();
        String apptDesc = this.apptDesc.getText();
        String apptLoc = this.apptLoc.getText();
        String apptContact = this.apptContact.getSelectionModel().getSelectedItem();
        String strCustID = customerID.getText();
        String contactID = Integer.toString(getContactID(apptContact));
        String apptType = this.apptType.getText();
        LocalDate apptStartDate = this.apptStartDate.getValue();
        String apptStartTime = this.apptStartTime.getSelectionModel().getSelectedItem();
        LocalDate apptEndDate = this.apptEndDate.getValue();
        String apptEndTime = this.apptEndTime.getSelectionModel().getSelectedItem();
        String createdBy = LoginScreen.getSession().getUserName();
        String created = new String();
        String exception = new String();
        boolean isValidEnd = false;
        boolean isSame = false;
        boolean isSameAppt = false;

        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter ldtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter ztFormatter = DateTimeFormatter.ofPattern("HH:mm");
        created = ldtFormatter.format(ldt.atZone(ZoneId.of("UTC")));
        LocalDateTime startTimeUTC;
        ZonedDateTime zdtstartTimeUTC = null;
        LocalDateTime endTimeUTC = null;
        ZonedDateTime zdtendTimeUTC = null;
        Timestamp startToSql = null;
        Timestamp endToSql = null;
        Timestamp ltdToSql = null;
        ZonedDateTime zdlocalCreated = ldt.atZone(ZoneId.systemDefault());
        zdlocalCreated = zdlocalCreated.withZoneSameInstant(ZoneOffset.UTC);

        if((apptStartTime != null || apptEndDate != null || apptStartDate != null || apptEndTime != null) || (apptStartTime == null || apptEndDate == null || apptStartDate != null || apptEndTime != null)) {

            if (apptStartTime != null && apptStartDate != null) {
                startTimeUTC = LocalDateTime.of(apptStartDate, LocalTime.parse(apptStartTime, ztFormatter));
                zdtstartTimeUTC = ZonedDateTime.of(startTimeUTC, ZoneId.systemDefault());
                zdtstartTimeUTC = zdtstartTimeUTC.withZoneSameInstant(ZoneOffset.UTC);
            }
            if (apptEndTime != null && apptEndDate != null) {
                endTimeUTC = LocalDateTime.of(apptEndDate, LocalTime.parse(apptEndTime, ztFormatter));
                zdtendTimeUTC = ZonedDateTime.of(endTimeUTC, ZoneId.systemDefault());
                zdtendTimeUTC = zdtendTimeUTC.withZoneSameInstant(ZoneOffset.UTC);
            }

            if((apptEndTime != null && apptEndDate != null) && (apptStartTime != null && apptStartDate != null)) {
                startToSql = Timestamp.valueOf(zdtstartTimeUTC.toLocalDateTime());
                endToSql = Timestamp.valueOf(zdtendTimeUTC.toLocalDateTime());
                ltdToSql = Timestamp.valueOf(zdlocalCreated.toLocalDateTime());
                Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
                Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
                Date d1 = new Date(startToSql.getTime());
                Date d2 = new Date(endToSql.getTime());
                c1.setTime(d1);
                c2.setTime(d2);
                isSame = c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
            }

            exception = isApptValid(apptTitle, apptDesc, apptLoc, apptType, strCustID.trim(), contactID, exception, startToSql, endToSql, isSame);

            if(startToSql != null && endToSql != null) {
                isValidEnd = isApptEndTimeValid(startToSql.toString(), endToSql.toString(), Integer.parseInt(apptID.getText()));
            }

            if(exception.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Unable to create appointment");
                alert.setHeaderText("Error: ");
                alert.setContentText(exception);
                alert.showAndWait();
            }

            System.out.println("Start + End: " + isValidEnd);

            if(!isValidEnd && (apptEndTime != null && apptEndTime != null)) {
                String endTime;
                exception = "An appointment already exists between the selected times.";
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Unable to create appointment");
                alert.setHeaderText("Error: ");
                alert.setContentText(exception);
                alert.showAndWait();
            }


            if(exception.length() == 0 && isValidEnd) {
                int custID = Integer.parseInt(strCustID.trim());

                try {

                    PreparedStatement ps = sql.getConn().prepareStatement("UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Created_By=?, Last_Update=?, " +
                            "Last_Updated_By=?, Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID=?;");

                    ps.setString(1, apptTitle);
                    ps.setString(2, apptDesc);
                    ps.setString(3, apptLoc);
                    ps.setString(4, apptType);
                    ps.setString(5, String.valueOf(startToSql));
                    ps.setString(6, String.valueOf(endToSql));
                    ps.setString(7, createdBy);
                    ps.setString(8, String.valueOf(ltdToSql));
                    ps.setString(9, createdBy);
                    ps.setInt(10, custID);
                    ps.setInt(11, LoginScreen.getSession().getUserID());
                    ps.setInt(12, Integer.parseInt(contactID));
                    ps.setInt(13, appt.getApptID());

                    try {
                        ps.executeUpdate();
                    } catch (SQLException sqlException) {
                        throw new Exception(sqlException);
                    }
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error: Blank Fields Detected");
                    alert.setHeaderText("Error");
                    alert.setContentText("Form contains blank fields, or you have entered incorrect values. Please verify and try again.");
                    alert.showAndWait();
                }

                Parent modifyInstance = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../FXML/mainScreen.fxml")));
                Scene scene = new Scene(modifyInstance);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            }
        }
    }

    /**
     * Validates where the start time is valid for the appointment or not.
     * @param start
     * @param end
     * @return
     * @throws SQLException
     */

    /**
     * Validates where the end time is valid for the appointment or not.
     * @param start
     * @param end
     * @return
     * @throws SQLException
     */
    public boolean isApptEndTimeValid(String start, String end, int apptID) throws SQLException {

        int counter = 0;
        ResultSet rs;

        PreparedStatement ps = sql.getConn().prepareStatement("SELECT * FROM appointments WHERE Start=? AND End=? AND User_ID=?;");
        ps.setTimestamp(1, Timestamp.valueOf(start));
        ps.setTimestamp(2, Timestamp.valueOf(end));
        ps.setInt(3, LoginScreen.getSession().getUserID());

        rs = ps.executeQuery();

        while(rs.next()) {
            if((rs.getString("Start").equals(start) || rs.getString("End").equals(end)) ||
                    (!Objects.equals(rs.getString("Start"), start) && Objects.equals(rs.getString("End"), end)) ||
                    (Objects.equals(rs.getString("Start"), start) && Objects.equals(rs.getString("End"), end))) {
                return true;
            } else {
                counter += 1;
            }
        }

        return counter <= 0;
    }

    /**
     * The function, isApptValid(), validates whether the appointment can be created and returns an exception if any of the fields are blank. If any of the data is checked again the MySQL DB,
     * and is also invalid, it throws an error suggesting for the user to validate information.
     *
     * @param apptTitle - The title of the appointment.
     * @param apptDesc - The description of the appointment.
     * @param apptLoc - The location of the appointment.
     * @param apptType - The type of appointment.
     * @param custID - The customer ID.
     * @param contactID - The contact ID.
     * @param exception - The exception message that will be displayed if call fails validations checks.
     * @param startToSql - The start date value converted to timestamp.
     * @param endToSql - The end date value converted to timestamp.
     * @param isSame - The boolean value determining if dates are on the same day or not.
     * @return message - Returns the concatenated value of the overall errors.
     * @throws SQLException
     */
    public String isApptValid(String apptTitle, String apptDesc, String apptLoc, String apptType, String custID, String contactID,
                              String exception, Timestamp startToSql, Timestamp endToSql, boolean isSame) throws SQLException {

        String message = new String();

        if(apptTitle.length() == 0)
        {
            message = message + ("The Title section cannot be empty. \n");
        }
        if(apptDesc.length() == 0)
        {
            message = message + ("The Description section cannot be empty. \n");
        }
        if(apptLoc.length() == 0)
        {
            message = message + ("The Location section cannot be empty. \n");
        }
        if(apptType.length() == 0) {
            message = message + ("The Appointment Type section cannot be empty. \n");
        }
        if(!isContactValid(contactID)) {
            message = message + ("A contact has not been selected. Please confirm the contact name. \n");
        }
        if(custID.length() > 0) {
            if (!isCustomerValid(custID)) {
                message = message + ("The Customer ID does not match any existing contact. Please confirm the customer ID. \n");
            }
        }
        if(custID.length() == 0) {
            message = message + ("Please enter a value for the Customer ID before proceeding.\n");
        }

        if(apptStartDate.getValue() == null && apptStartTime.getValue() != null ){
            message = message + ("Start Date is either invalid or blank. Please verify and try again.\n");
        }

        if (apptStartTime.getValue() == null && apptStartDate.getValue() != null){
            message = message + ("Start Time is either invalid or blank. Please verify and try again.\n");
        }

        if (apptStartTime.getValue() == null && apptStartDate.getValue() == null) {
            message = message + ("The fields: Start Date and Start Time are either invalid or blank. Please verify and try again.\n");
        }

        if(apptEndDate.getValue() == null && apptEndTime.getValue() != null) {
            message = message + ("End Date is either invalid or blank. Please verify and try again.\n");
        }

        if (apptEndDate.getValue() != null && apptEndTime.getValue() == null){
            message = message + ("End Time is either invalid or blank. Please verify and try again.\n");
        }

        if(apptEndDate.getValue() == null && apptEndTime.getValue() == null ){
            message = message + ("The fields: End Date and End Time are either invalid or blank. Please verify and try again.\n");
        }

        if((startToSql != null && endToSql != null) && (startToSql.after(endToSql) || endToSql.before(startToSql) || !isSame)) {
            message = message + "Please confirm that the dates are correct, and on the same day.\n";
        }

        return message;

    }

    /**
     * getContactID() obtains the contact id by searching the database and then returns it.
     * @param contactName
     * @return
     */
    public int getContactID(String contactName) {
        ResultSet rs;
        try {
            int contactID = 0;

            PreparedStatement ps = sql.getConn().prepareStatement("SELECT Contact_ID FROM contacts WHERE Contact_Name=?;");
            ps.setString(1, contactName);
            rs = ps.executeQuery();

            while(rs.next()) {
                contactID = rs.getInt("Contact_ID");
            }
            return contactID;

        } catch (SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
    }

    /**
     * isContactValid() is a validation check to confirm whether the contact exists within the database. Returns a boolean value.
     * @param contact
     * @return
     * @throws SQLException
     */
    public boolean isContactValid(String contact) throws SQLException {
        ResultSet rs;
        PreparedStatement ps = sql.getConn().prepareStatement("SELECT * FROM contacts WHERE Contact_ID=?;");
        ps.setInt(1, Integer.parseInt(contact));
        rs = ps.executeQuery();
        return rs.next();
    }

    /**
     * isCustomerValid() is a validation check to confirm whether the customer exists within the database. Returns a boolean value.
     * @param customer
     * @return
     * @throws SQLException
     */
    public boolean isCustomerValid(String customer) throws SQLException {
        ResultSet rs;
        PreparedStatement ps = sql.getConn().prepareStatement("SELECT * FROM customers WHERE Customer_ID=?;");
        ps.setInt(1, Integer.parseInt(customer.trim()));
        rs = ps.executeQuery();
        return rs.next();
    }

    /**
     * Initializes the scene by passing in language values, and other needed functions for the start/end time ranges for 8am-10pm. It also prepopulates data passed from the main screen, and populates the combo boxes.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int contactindex;
        LocalTime firstAvail = LocalTime.MIN.plusHours(8);
        LocalTime lastAvail = LocalTime.MAX.minusHours(1).minusMinutes(45);

        ObservableList<String> allApptTimes = FXCollections.observableArrayList();
        ObservableList<Customers> allCustomers = FXCollections.observableArrayList();

        try {
            allCustomers = customerDB.getAllCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(!firstAvail.equals(0) || !lastAvail.equals(0)) {
            while(firstAvail.isBefore(lastAvail)){
                allApptTimes.add(firstAvail.toString());
                firstAvail = firstAvail.plusMinutes(15);
            }
        }
        apptStartTime.setItems(allApptTimes);
        apptEndTime.setItems(allApptTimes);

        appt = mainScreen.getApptList().get(mainScreen.getApptIndex());
        contact = Contacts.getContactsNames();
        contactindex = contact.indexOf(appt.getContactName());

        apptID.setText(String.valueOf(appt.getApptID()));
        apptTitle.setText(appt.getTitle());
        apptDesc.setText(appt.getDesc());
        apptLoc.setText(appt.getLoc());

        apptContact.setItems(contact);
        apptContact.getSelectionModel().select(contactindex);
        customerID.setText(Integer.toString(appt.getCustomerID()));
        apptType.setText(appt.getType());


        apptStartDate.setValue(LocalDate.parse(appt.getEndDate()));
        apptEndDate.setValue(LocalDate.parse(appt.getStartDate()));
        apptStartTime.setValue(appt.getStartDateTime());
        apptEndTime.setValue(appt.getEndDateTime());

        userID_TF.setText("ID: " + LoginScreen.getSession().getUserID()
                + ",  User: "  + LoginScreen.getSession().getUserName());

        custIDCol.setCellValueFactory(cd -> cd.getValue().customerIDProperty());
        cNameCol.setCellValueFactory(cd -> cd.getValue().customerNameProperty());
        cPhoneCol.setCellValueFactory(cd -> cd.getValue().phoneProperty());
        customerTable.setItems(allCustomers);

        customerTable.getSelectionModel().selectedItemProperty().addListener((ob, oldSelect, newSelect) -> {
            customerID.setText(String.valueOf(newSelect.getCustomerID()));
        });

    }
}