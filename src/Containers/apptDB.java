package Containers;

import Controllers.LoginScreen;
import Models.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.sql;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class apptDB {

    /**
     * setApptTable() uses a sql connection to gather information to create a new Appointments object.
     * The appointment object is generated in a while loop so that it can create an observable list.
     * @return
     * @throws IOException
     */
    public static ObservableList<Appointments> setApptTable() throws IOException {
        PreparedStatement stmt;
        ResultSet rs;
        ObservableList<Appointments> apptList = FXCollections.observableArrayList();

        try {
            stmt = sql.getConn().prepareStatement("Select * FROM appointments, customers, contacts "
                    + "WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.Contact_ID = contacts.Contact_ID ORDER BY appointments.Start;");

            rs = stmt.executeQuery();

            while(rs.next())
            {
                int apptID = rs.getInt("Appointment_ID");
                int CID = rs.getInt("Customer_ID");
                int UserID = rs.getInt("User_ID");
                int ContactID = rs.getInt("Contact_ID");
                String Title = rs.getString("Title");
                String setType = rs.getString("Type");
                String startDateTime = rs.getTime("start").toString();
                String endDateTime = rs.getTime("end").toString();
                String setCreatedDateTime = rs.getString("Create_Date");
                String setCreatedBy = rs.getString("Created_By");
                String setLastUpdate = rs.getString("Last_Update");
                String LastUpdateBy = rs.getString("Last_Updated_By");
                String Loc = rs.getString("Location");
                String Desc = rs.getString("Description");
                String CustomerName = rs.getString("Customer_Name");
                String startDate = rs.getDate("start").toString();
                String endDate = rs.getDate("end").toString();
                String ContactName = rs.getString("Contact_Name");

                String start = startDate + " " + startDateTime;
                String end = endDate + " " + endDateTime;

                Timestamp ts1 = Timestamp.valueOf(start);
                Timestamp ts2 = Timestamp.valueOf(end);

                LocalDateTime ldt1 = ts1.toLocalDateTime();
                ZonedDateTime zdt1 = ldt1.atZone(ZoneId.of("UTC"));

                zdt1.withZoneSameInstant(ZoneId.of("US/Eastern"));

                LocalDateTime ldt2 = ts2.toLocalDateTime();
                ZonedDateTime zdt2= ldt2.atZone(ZoneId.of("UTC"));

                zdt1 = zdt1.withZoneSameInstant(ZoneId.systemDefault());
                zdt2 = zdt2.withZoneSameInstant(ZoneId.systemDefault());

                startDate = zdt1.toLocalDateTime().toLocalDate().toString();
                endDate = zdt2.toLocalDateTime().toLocalDate().toString();
                startDateTime = zdt1.toLocalDateTime().toLocalTime().toString();
                endDateTime = zdt2.toLocalDateTime().toLocalTime().toString();

                Appointments tmp = new Appointments(apptID, Title, Desc, Loc, setType, startDateTime, endDateTime,setCreatedDateTime, setCreatedBy, setLastUpdate, LastUpdateBy, CID, UserID, ContactID, ContactName, CustomerName, startDate, endDate);

                apptList.add(tmp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return apptList;
    }

    /**
     * apptAlert() calls a sql statement to check if there is an appointment that is scheduled for the current system time of the user.
     * @return
     * @throws SQLException
     */
    public static Appointments apptAlert() throws SQLException {
        Appointments appt = null;
        LocalDateTime instance = LocalDateTime.now();
        ZoneId zID = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = instance.atZone(zID);

        LocalDateTime local = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime localAlert = local.plusMinutes(15);
        int user = LoginScreen.getSession().getUserID();

        ResultSet rs;
        PreparedStatement ps = sql.getConn().prepareStatement("SELECT * FROM appointments WHERE Start BETWEEN ? AND ? AND User_ID=?;");
        ps.setTimestamp(1, Timestamp.valueOf(local));
        ps.setTimestamp(2, Timestamp.valueOf(localAlert));
        ps.setInt(3, user);
        rs = ps.executeQuery();


        if(rs.next()) {
            int apptID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String desc = rs.getString("Description");
            String loc = rs.getString("Location");
            String startDateTime = rs.getString("Start");
            String endDate = rs.getString("End");
            int customerID = rs.getInt("Customer_ID");
            String contact_ID = rs.getString("Contact_ID");
            appt = new Appointments(apptID, title, desc, loc, startDateTime, endDate, customerID, Appointments.getContactNameDB(contact_ID));
        }
        return appt;
    }

}