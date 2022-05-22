//TODO add methods that search for partial texts

package Controllers;


import Containers.apptDB;
import Models.Appointments;
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
import javafx.stage.Stage;
import utils.sql;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TimeZone;


public class mainScreen implements Initializable {

    private static int apptIndex;
    @FXML
    private Label timezoneLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button modifyCustomerButton;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button modifyAppointmentButton;
    @FXML
    private Button reportsButton;
    @FXML
    private RadioButton viewWeekRadio;
    @FXML
    private RadioButton viewMonthRadio;
    @FXML
    private RadioButton viewAllRadio;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Appointments> apptTableView;
    @FXML
    private TableColumn<Appointments, Number> apptIDColumn;
    @FXML
    private TableColumn<Appointments, String> customerColumn;
    @FXML
    private TableColumn<Appointments, String>  titleColumn;
    @FXML
    private TableColumn<Appointments, String>  descColumn;
    @FXML
    private TableColumn<Appointments, String>  locColumn;
    @FXML
    private TableColumn<Appointments, String> typeColumn;
    @FXML
    private TableColumn<Appointments, String>  dateColumn;
    @FXML
    private TableColumn<Appointments, String>  startTimeColumn;
    @FXML
    private TableColumn<Appointments, String>  endTimeColumn;

    @FXML
    private ToggleGroup toggleGroup;
    private static ObservableList<Appointments> apptList = FXCollections.observableArrayList();

    /**
     * Gets the index for the appointment to be used in another scene to populate data.
     * @return
     */
    public static int getApptIndex() {
        return apptIndex;
    }

    /**
     * Sets the index for the appointment to be used in another scene to populate data.
     * @param apptIndex
     */
    public static void setApptIndex(int apptIndex) {
        mainScreen.apptIndex = apptIndex;
    }

    /**
     * Collects the data from the Table and passes it to the next screen. The function also calls the next screen if an appointment if selected, throws an alert if not.
     * @param event
     * @throws IOException
     */
    @FXML
    public void onClickModAppt(Event event) throws IOException {

        if(apptTableView.getSelectionModel().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Null appointment");
            alert.setHeaderText("Appointment Invalid");
            alert.setContentText("You did not select an appointment!");
            alert.showAndWait();
        }
        else {
            setApptIndex(apptTableView.getSelectionModel().getSelectedIndex());

            Parent modifyInstance = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../FXML/modAppointmentsScreen.fxml")));
            Scene scene = new Scene(modifyInstance);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        }
    }

    /**
     * Takes the user to the customer screen to modify the customer.
     * @param event
     * @throws IOException
     */
    @FXML
    public void onClickModCustomer(Event event) throws IOException {
            Parent modifyInstance = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../FXML/modCustomerScreen.fxml")));
            Scene scene = new Scene(modifyInstance);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
    }

    /**
     * This function updates the appointment values in the table upon refresh.
     * @throws IOException
     */
    @FXML
    public void updateTables() throws IOException {

        apptList = apptDB.setApptTable();

        apptIDColumn.setCellValueFactory(cd -> cd.getValue().apptIDProperty());
        customerColumn.setCellValueFactory(cd -> cd.getValue().customerNameProperty());
        titleColumn.setCellValueFactory(cd -> cd.getValue().titleProperty());
        descColumn.setCellValueFactory(cd -> cd.getValue().descProperty());
        locColumn.setCellValueFactory(cd -> cd.getValue().locProperty());
        typeColumn.setCellValueFactory(cd -> cd.getValue().typeProperty());
        startTimeColumn.setCellValueFactory(cd -> cd.getValue().startDateTimeProperty());
        endTimeColumn.setCellValueFactory(cd -> cd.getValue().endDateTimeProperty());
        dateColumn.setCellValueFactory(cd -> cd.getValue().startDateProperty());
        apptTableView.sort();
        apptTableView.setItems(apptList);

    }

    /**
     * Moves the user to the customer creation screen.
     * @param event
     * @throws IOException
     */
    @FXML
    public void addCustomer(Event event) throws IOException {
        Parent modifyInstance = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../FXML/customerScreen.fxml")));
        Scene scene = new Scene(modifyInstance);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * Sends the user to the appointment creation screen.
     * @param event
     * @throws IOException
     */
    @FXML
    public void addAppointment(Event event) throws IOException {
        appointmentsScreen screen = new appointmentsScreen();
        Parent modifyInstance = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../FXML/appointmentsScreen.fxml")));
        Scene scene = new Scene(modifyInstance);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        //TODO Below code just closes if window is no longer focused.

        // window.focusedProperty().addListener((ov, onHidden, onShow) ->
        //        {
        //           if(window.isShowing()){
        //               Platform.runLater(() -> window.close());
        //           }
        //        });
    }

    /**
     * Logs the user out of the program and clears sql session. This function requires the user to validate whether they want to logout or not.
     * @param event
     * @throws SQLException
     */
    @FXML
    public void logout(Event event) throws SQLException {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            String alertS = "Are you sure you would like to log out?";
            String alertH = "Logout";
            alert.setContentText(alertS);
            alert.setHeaderText(alertH);
            alert.setTitle("Logout?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                Parent modifyInstance = FXMLLoader.load(getClass().getResource("../FXML/LoginScreen.fxml"));
                Scene scene = new Scene(modifyInstance);
                Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a boolean value based on whether the value in the search field is an int.
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
     * Returns an observable list of appointments.
     * @return
     */
    public static ObservableList<Appointments> getApptList() {
        return apptList;
    }


    /**
     * Sends the user to the reports screen.
     * @param event
     * @throws IOException
     */
    @FXML
    private void onReportsClick(Event event) throws IOException {
        Parent modifyInstance = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../FXML/reportsScreen.fxml")));
        Scene scene = new Scene(modifyInstance);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public void viewByWeekCheck(ActionEvent actionEvent) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ObservableList<Appointments> weeklyAppts = FXCollections.observableArrayList();
        LocalDateTime start = LocalDateTime.now().minusWeeks(1);
        LocalDateTime end = LocalDateTime.now().plusWeeks(1);
        final LocalDateTime[] apptStart = {null};
        final LocalDateTime[] apptEnd = {null};


        if(apptList != null ) {
            apptList.forEach(appointments -> {
                apptStart[0] = LocalDateTime.parse(appointments.getEndDate() + " " + appointments.getEndDateTime(), format);
                apptEnd[0] = LocalDateTime.parse(appointments.getStartDate() + " " + appointments.getStartDateTime(), format);

                if(apptStart[0].isAfter(start) && apptEnd[0].isBefore(end)){
                    weeklyAppts.add(appointments);
                }
                apptTableView.setItems(weeklyAppts);
            });
        }
    }

    public void viewByMonthCheck(ActionEvent actionEvent) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ObservableList<Appointments> monthlyAppts = FXCollections.observableArrayList();
        LocalDateTime start = LocalDateTime.now().minusMonths(1);
        LocalDateTime end = LocalDateTime.now().plusMonths(1);
        final LocalDateTime[] apptStart = {null};
        final LocalDateTime[] apptEnd = {null};


        if(apptList != null ) {
            apptList.forEach(appointments -> {
                apptStart[0] = LocalDateTime.parse(appointments.getEndDate() + " " + appointments.getEndDateTime(), format);
                apptEnd[0] = LocalDateTime.parse(appointments.getStartDate() + " " + appointments.getStartDateTime(), format);

                if(apptEnd[0].getMonth().getValue() < end.getMonth().getValue()
                        && apptStart[0].getMonth().getValue() > start.getMonth().getValue() && apptStart[0].getYear() == start.getYear()){
                   monthlyAppts.add(appointments);
               }
               apptTableView.setItems(monthlyAppts);
            });
        }
    }

    public void viewAllCheck(ActionEvent actionEvent) {
        apptTableView.setItems(apptList);
    }

    /**
     * Contains a double-lambda expression for the search field to search for appointments by the name, or the appt id. It also sets the table with appointments from the database.
     * @param url
     * @param resourceBundle
     */
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleGroup = new ToggleGroup();
        viewAllRadio.setToggleGroup(toggleGroup);
        viewMonthRadio.setToggleGroup(toggleGroup);
        viewWeekRadio.setToggleGroup(toggleGroup);
        try {
            updateTables();
            try {
                Appointments appt = apptDB.apptAlert();

                if(appt != null)
                {
                    ResultSet rs;
                    PreparedStatement ps = sql.getConn().prepareStatement("SELECT Customer_Name FROM customers where Customer_ID=?;");
                    ps.setInt(1,appt.getCustomerID());
                    rs = ps.executeQuery();

                    if(rs.next()){
                        String alertString = "You have an appointment for: \n" + appt.getTitle() + "\n" + rs.getString("Customer_Name") + "\n" + appt.getTime();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Appointment Alert");
                        alert.setHeaderText("Appointment Alert");
                        alert.setContentText(alertString);
                        alert.showAndWait();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Lambda expression that handles the search field by searching for either the appointment ID or the Customer's name.
        FilteredList<Appointments> filteredList = new FilteredList<>(apptList, p -> true);
        searchField.textProperty().addListener((observable, oldV, newV) -> {
            filteredList.setPredicate(appointments -> {
                if(newV == null || newV.isEmpty()){
                    return true;
                }

                String lowerCase = newV.toLowerCase();
                int apptIDv = appointments.getApptID();

                if(appointments.getCustomerName().toLowerCase().contains(lowerCase)) {
                    return true;
                }
                else if (isInt() && Integer.parseInt(newV) == apptIDv) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Appointments> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(apptTableView.comparatorProperty());
        apptTableView.setItems(sortedList);


    }


}