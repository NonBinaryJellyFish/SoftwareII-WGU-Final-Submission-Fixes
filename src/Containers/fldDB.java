package Containers;

import Models.FLD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class fldDB {

    /**
     * The getAllCountries() function calls a sql function to obtain a list of first-level-divisions in the database.
     * @return - fldList - The ObservableList object that contains all first-level-divisions from the database.
     */

    public static ObservableList<String> getAllDiv(String countryID) throws SQLException {
        PreparedStatement ps = sql.getConn().prepareStatement("SELECT Division FROM first_level_divisions WHERE Country_ID=?;");
        ps.setString(1, countryID);
        ResultSet rs = ps.executeQuery();
        ObservableList<String> fldList = FXCollections.observableArrayList();

        return fldList;
    }
}
