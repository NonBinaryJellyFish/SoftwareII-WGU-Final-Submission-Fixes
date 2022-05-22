package Containers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class countryDB {

    /**
     * The getAllCountries() function calls a sql function to obtain a list of countries in the database.
     * @return - clist - The ObservableList object that contains all countries from the database.
     */
    public static ObservableList<String> getAllCountries() {

        ObservableList<String> cList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = sql.getConn().prepareStatement("SELECT * FROM countries ORDER BY Country;");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String country = rs.getString("Country");
                cList.add(country);
            }
        } catch (SQLException ex){
            System.out.print(ex);
        }
        return cList;
    }

}