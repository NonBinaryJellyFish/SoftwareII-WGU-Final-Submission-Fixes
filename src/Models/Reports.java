package Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Reports {

    private StringProperty year;
    private StringProperty month;
    private IntegerProperty numberOfAppts;

    private StringProperty appointmentType;

    public Reports(StringProperty year, StringProperty month, IntegerProperty numberOfAppts, StringProperty appointmentType) {
        this.year = year;
        this.month = month;
        this.numberOfAppts = numberOfAppts;
        this.appointmentType = appointmentType;
    }

    public Reports(StringProperty year, StringProperty month, IntegerProperty numberOfAppts) {
        this.year = year;
        this.month = month;
        this.numberOfAppts = numberOfAppts;
    }


    public Reports() {
    }


    // Getter setter functions
    public String getYear() {
        return year.get();
    }

    public StringProperty yearProperty() {
        return year;
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public String getMonth() {
        return month.get();
    }

    public StringProperty monthProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }

    public int getNumberOfAppts() {
        return numberOfAppts.get();
    }

    public IntegerProperty numberOfApptsProperty() {
        return numberOfAppts;
    }

    public void setNumberOfAppts(int numberOfAppts) {
        this.numberOfAppts.set(numberOfAppts);
    }

    public String getAppointmentType() {
        return appointmentType.get();
    }

    public StringProperty appointmentTypeProperty() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType.set(appointmentType);
    }
}
