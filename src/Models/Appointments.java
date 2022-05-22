package Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Appointments {

    private IntegerProperty apptID;
    private StringProperty title;
    private StringProperty desc;
    private StringProperty loc;
    private StringProperty type;
    private StringProperty startDateTime;
    private StringProperty endDateTime;
    private StringProperty createdDateTime;
    private StringProperty createdBy;
    private StringProperty lastUpdate;
    private StringProperty lastUpdateBy;

    //TODO External methods, find way to incorporate them from the parent classes.
    private IntegerProperty customerID;
    private IntegerProperty userID;
    private IntegerProperty contactID;
    private StringProperty contactName;
    private StringProperty customerName;
    private StringProperty startDate;
    private StringProperty endDate;

    //Overloaded constructors for different case scenarios.
    public Appointments(int apptID, String title, String desc, String loc, String type, String startDateTime,
                        String endDateTime, String createdDateTime, String createdBy, String lastUpdate, String lastUpdateBy,
                        int customerID, int userID, int contactID, String contactName, String customerName, String startDate, String endDate) {
        this.apptID = new SimpleIntegerProperty(apptID);
        this.title = new SimpleStringProperty(title);
        this.desc = new SimpleStringProperty(desc);
        this.loc = new SimpleStringProperty(loc);
        this.type = new SimpleStringProperty(type);
        this.startDateTime = new SimpleStringProperty(startDateTime);
        this.endDateTime = new SimpleStringProperty(endDateTime);
        this.createdDateTime = new SimpleStringProperty(createdDateTime);
        this.createdBy = new SimpleStringProperty(createdBy);
        this.lastUpdate = new SimpleStringProperty(lastUpdate);
        this.lastUpdateBy = new SimpleStringProperty(lastUpdateBy);
        this.customerID = new SimpleIntegerProperty(customerID);
        this.userID = new SimpleIntegerProperty(userID);
        this.contactID = new SimpleIntegerProperty(contactID);
        this.contactName = new SimpleStringProperty(contactName);
        this.customerName = new SimpleStringProperty(customerName);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
    }

    public Appointments(String title, String desc, String loc, String type, String startDateTime,
                        String endDateTime, String createdDateTime, String createdBy, String lastUpdate, String lastUpdateBy,
                        int customerID, int userID, int contactID, String contactName, String customerName, String startDate) {
        this.title = new SimpleStringProperty(title);
        this.desc = new SimpleStringProperty(desc);
        this.loc = new SimpleStringProperty(loc);
        this.type = new SimpleStringProperty(type);
        this.startDateTime = new SimpleStringProperty(startDateTime);
        this.endDateTime = new SimpleStringProperty(endDateTime);
        this.createdDateTime = new SimpleStringProperty(createdDateTime);
        this.createdBy = new SimpleStringProperty(createdBy);
        this.lastUpdate = new SimpleStringProperty(lastUpdate);
        this.lastUpdateBy = new SimpleStringProperty(lastUpdateBy);
        this.customerID = new SimpleIntegerProperty(customerID);
        this.userID = new SimpleIntegerProperty(userID);
        this.contactID = new SimpleIntegerProperty(contactID);
        this.contactName = new SimpleStringProperty(contactName);
        this.customerName = new SimpleStringProperty(customerName);
        this.startDate = new SimpleStringProperty(startDate);
    }

    public Appointments(int apptID, String title, String desc, String loc, String startDateTime, String endDateTime, int customerID, String contactID) {

        this.apptID = new SimpleIntegerProperty(apptID);
        this.title = new SimpleStringProperty(title);
        this.desc = new SimpleStringProperty(desc);
        this.loc = new SimpleStringProperty(loc);
        this.startDateTime = new SimpleStringProperty(startDateTime);
        this.endDateTime = new SimpleStringProperty(endDateTime);
        this.customerID = new SimpleIntegerProperty(customerID);
        this.contactID.setValue(Integer.parseInt(contactID.trim()));
    }

    /**
     * Gets the Timestamp and converts it to localtime. It returns as a string.
     * @return
     */
    public String getTime() {

        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime local = LocalDateTime.parse(this.startDateTime.getValue(), dtFormat);

        ZonedDateTime utc = local.atZone(ZoneId.of("UTC"));
        ZoneId zID = ZoneId.systemDefault();
        ZonedDateTime utcDate = utc.withZoneSameInstant(zID);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("kk:mm");
        LocalTime localT = LocalTime.parse(utcDate.toString().substring(11,16), timeFormatter);

        return localT.toString();
    }

    /**
     * Static function that gets the contactName from the database, based on the value of the contactID.
     * @param contactID
     * @return
     * @throws SQLException
     */
    public static String getContactNameDB(String contactID) throws SQLException {
        ResultSet rs;
        PreparedStatement ps = sql.getConn().prepareStatement("SELECT Contact_name FROM contacts WHERE Contact_ID=?;");
        ps.setInt(1, Integer.parseInt(contactID));
        rs = ps.executeQuery();
        if(rs.next())
        {
            return rs.getString("Contact_Name");
        }
        else {
            return null;
        }
    }
    // Below are getter and setter functions for the above variables.
    public int getApptID() {
        return apptID.get();
    }

    public IntegerProperty apptIDProperty() {
        return apptID;
    }

    public void setApptID(int apptID) {
        this.apptID.set(apptID);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getDesc() {
        return desc.get();
    }

    public StringProperty descProperty() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc.set(desc);
    }

    public String getLoc() {
        return loc.get();
    }

    public StringProperty locProperty() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc.set(loc);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getStartDateTime() {
        return startDateTime.get();
    }

    public StringProperty startDateTimeProperty() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime.set(startDateTime);
    }

    public String getEndDateTime() {
        return endDateTime.get();
    }

    public StringProperty endDateTimeProperty() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime.set(endDateTime);
    }

    public String getCreatedDateTime() {
        return createdDateTime.get();
    }

    public StringProperty createdDateTimeProperty() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime.set(createdDateTime);
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

    public String getLastUpdate() {
        return lastUpdate.get();
    }

    public StringProperty lastUpdateProperty() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate.set(lastUpdate);
    }

    public String getLastUpdateBy() {
        return lastUpdateBy.get();
    }

    public StringProperty lastUpdateByProperty() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy.set(lastUpdateBy);
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

    public int getUserID() {
        return userID.get();
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public int getContactID() {
        return contactID.get();
    }

    public IntegerProperty contactIDProperty() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID.set(contactID);
    }

    public String getContactName() {
        return contactName.get();
    }

    public StringProperty contactNameProperty() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName.set(contactName);
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


    public String getStartDate() {
        return startDate.get();
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public String getEndDate() {
        return endDate.get();
    }

    public StringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }



}


