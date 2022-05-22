package Controllers;

import Models.Reports;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import utils.JDBC;
import utils.sql;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ReportsScreen implements Initializable {

    @FXML
    private NumberAxis appointmentNumberAxis;

    @FXML
    private Tab apptByMonthTab;

    @FXML
    private Tab apptByTypeTab;

    @FXML
    private TableColumn<Reports, String> apptTypeCol;

    @FXML
    private AreaChart<String, Integer> areaChart;

    @FXML
    private Button goBack;

    @FXML
    private CategoryAxis monthAxis;

    @FXML
    private TableColumn<Reports, String> monthCol;

    @FXML
    private TableColumn<Reports, Number> numOfApptCol;

    @FXML
    private TableView<Reports> reportTable;

    @FXML
    private TableColumn<Reports, String> yearCol;


    /**
     * Closes the current scene and returns the user to the main screen.
     * @param event
     * @throws IOException
     */
    @FXML
    void onGoBackClick(ActionEvent event) throws IOException {
        Parent modifyInstance = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../FXML/mainScreen.fxml")));
        Scene scene = new Scene(modifyInstance);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     *     Populates the Appointments by month chart.
     * @throws SQLException
     */
    private void popAppointmentChart() throws SQLException {
        ObservableList<XYChart.Data<String, Integer>> dataInput = FXCollections.observableArrayList();
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        PreparedStatement ps = sql.getConn().prepareStatement("Select MONTHNAME(Start) AS \"Month\", YEAR(Start) as \"year\", COUNT(MONTHNAME(Start)) AS \"numberofAppt\" FROM appointments" +
                " WHERE YEAR(Start)=YEAR(CURDATE()) GROUP BY MONTHNAME(Start), Year(Start)");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            System.out.println("Test");
            String month = rs.getString("Month");
            String year = rs.getString("Year");
            int numberOfAppt = rs.getInt("numberOfAppt");
            dataInput.add(new XYChart.Data<>(month, numberOfAppt));
        }

        series.getData().addAll(dataInput);
        areaChart.getData().add(series);
    }

    private void popApptByTypeTable() throws SQLException {
        ObservableList<Reports> types = FXCollections.observableArrayList();

        ResultSet rs;
        PreparedStatement ps = sql.getConn().prepareStatement("SELECT Type, MONTHNAME(Start) AS \"Month\",YEAR(Start) AS \"Year\", COUNT(Type) as \"numberOfAppt\" FROM appointments WHERE YEAR(Start) = YEAR(CURDATE()) GROUP BY Type, MONTHNAME(Start), Year(Start);");
        rs = ps.executeQuery();

        while(rs.next())
        {
            String year = rs.getString("Year");
            String month = rs.getString("Month");
            int numofAppts = rs.getInt("numberOfAppt");
            String type = rs.getString("Type");
            Reports tmp = new Reports(new SimpleStringProperty(year), new SimpleStringProperty(month), new SimpleIntegerProperty(numofAppts), new SimpleStringProperty(type));
            types.add(tmp);
        }

        apptTypeCol.setCellValueFactory(cd -> cd.getValue().appointmentTypeProperty());
        numOfApptCol.setCellValueFactory(cd -> cd.getValue().numberOfApptsProperty());
        monthCol.setCellValueFactory(cd -> cd.getValue().monthProperty());
        yearCol.setCellValueFactory(cd -> cd.getValue().yearProperty());
        reportTable.setItems(types);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            System.out.println("Before");
            popApptByTypeTable();
            popAppointmentChart();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}




