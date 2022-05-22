package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Contacts {

    private int contactID;
    private String contactName;
    private String email;

    //Constructor for different case scenarios.
    public Contacts(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }
    //Empty constructor used for generating null objects.
    public Contacts() {

    }

    /**
     * Gets all contact names from the database and returns them as an object.
     * @return
     */
    public static ObservableList<String> getContactsNames() {
        ObservableList<String> allContacts =  FXCollections.observableArrayList();
        ResultSet rs;

        try {
            PreparedStatement ps = sql.getConn().prepareStatement("SELECT Contact_Name FROM contacts;");
            rs = ps.executeQuery();

            while(rs.next()){
                String contact = rs.getString("Contact_Name");
                allContacts.add(contact);
            }

        } catch (SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
        return allContacts;
    }

    //Getter and setter functions for the above variables.
    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
