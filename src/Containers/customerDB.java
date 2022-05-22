package Containers;

import Models.Customers;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class customerDB {

    /**
     * The getAllCountries() function calls a sql function to obtain a list of customers in the database.
     * @return - custAll - The ObservableList object that contains all customers from the database.
     */
    public static ObservableList<Customers> getAllCustomers() throws SQLException {
        
        PreparedStatement ps = sql.getConn().prepareStatement("SELECT * FROM customers ORDER BY Customer_ID;");
        ResultSet rs = ps.executeQuery();
        ObservableList<Customers> custAll = FXCollections.observableArrayList();
        while(rs.next())
        {
            IntegerProperty CID = new SimpleIntegerProperty(rs.getInt("Customer_ID"));
            StringProperty cName = new SimpleStringProperty(rs.getString("Customer_Name"));
            StringProperty addr = new SimpleStringProperty(rs.getString("Address"));
            StringProperty pCode = new SimpleStringProperty(rs.getString("Postal_Code"));
            StringProperty cDate = new SimpleStringProperty(rs.getString("Create_date"));
            StringProperty cBy = new SimpleStringProperty(rs.getString("Created_By"));
            StringProperty Last_Update = new SimpleStringProperty(rs.getString("Last_Update"));
            StringProperty Last_Updated_By = new SimpleStringProperty(rs.getString("Last_Updated_By"));
            StringProperty phone = new SimpleStringProperty(rs.getString("Phone"));
            IntegerProperty divisionID = new SimpleIntegerProperty(rs.getInt("Division_ID"));
            Customers tmp = new Customers(CID, cName, addr, pCode, cDate, cBy, Last_Update, Last_Updated_By, divisionID, phone);

            custAll.add(tmp);
        }
        return custAll;
    }
}