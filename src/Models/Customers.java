package Models;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customers {

    private StringProperty phone;
    private IntegerProperty customerID;
    private StringProperty customerName;
    private StringProperty addr;
    private StringProperty postalCode;
    private StringProperty createdDate;
    private StringProperty createdBy;
    private StringProperty lastUpdated;
    private StringProperty lastUpdatedBy;
    private IntegerProperty divisionID = new SimpleIntegerProperty();
    private StringProperty fld = new SimpleStringProperty();
    private StringProperty country = new SimpleStringProperty();


    public Customers(IntegerProperty customerID, StringProperty customerName, StringProperty addr,
                     StringProperty postalCode, StringProperty createdDate, StringProperty createdBy,
                     StringProperty lastUpdated, StringProperty lastUpdatedBy, IntegerProperty divisionID, StringProperty phone) throws SQLException {
        this.customerID = customerID;
        this.customerName = customerName;
        this.addr = addr;
        this.postalCode = postalCode;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.phone = phone;
        this.divisionID = divisionID;
        getFLD(divisionID.get());
    }

    public Customers() {
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public int getCustomerID() {
        return customerID.get();
    }

    public IntegerProperty customerIDProperty() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public String getAddr() {
        return addr.get();
    }

    public StringProperty addrProperty() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr.set(addr);
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getCreatedDate() {
        return createdDate.get();
    }

    public StringProperty createdDateProperty() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate.set(createdDate);
    }

    public String getCreatedBy() {
        return createdBy.get();
    }

    public StringProperty createdByProperty() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy.set(createdBy);
    }

    public String getLastUpdated() {
        return lastUpdated.get();
    }

    public StringProperty lastUpdatedProperty() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated.set(lastUpdated);
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy.get();
    }

    public StringProperty lastUpdatedByProperty() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy.set(lastUpdatedBy);
    }

    public String getFld() {
        return fld.get();
    }

    public StringProperty fldProperty() {
        return fld;
    }

    public void setFld(String fld) {
        this.fld.set(fld);
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public int getDivisionID() {
        return divisionID.get();
    }

    public IntegerProperty divisionIDProperty() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID.set(divisionID);
    }
    public String getCountry() {
        return country.get();
    }


    /**
     * Gets all first level divisions and then uses another function to determine what the country is by passing the country ID.
     * @param divisionID
     * @throws SQLException
     */
    public void getFLD(int divisionID) throws SQLException {
        int cid = 0;
        ResultSet rs;
        PreparedStatement ps = sql.getConn().prepareStatement("SELECT * FROM first_level_divisions WHERE Division_ID=?");
        ps.setInt(1, divisionID);
        rs=ps.executeQuery();

        while(rs.next()) {
            this.fld.set(rs.getString("Division"));
            cid = rs.getInt("Country_ID");
        }
        getCountrysql(cid);

    }

    /**
     * Sets the country name after querying the database with Country_ID.
     * @param countryID
     * @throws SQLException
     */
    public void getCountrysql(int countryID) throws SQLException {
        ResultSet rs;
        PreparedStatement ps = sql.getConn().prepareStatement("SELECT * FROM countries WHERE Country_ID=?");
        ps.setInt(1, countryID);
        rs=ps.executeQuery();
        while(rs.next()){
            this.country.set(rs.getString("Country"));
        }
    }
}
