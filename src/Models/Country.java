package Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Country {
    IntegerProperty cID;
    StringProperty country;

    //Overloaded functions that will be needed in other functions.
    public Country(StringProperty country) {
        this.country = country;
    }

    public Country(IntegerProperty cID, StringProperty country) {

        this.cID = cID;
        this.country = country;
    }

    // Getter and setter functions for hte variables.

    public int getcID() {
        return cID.get();
    }

    public IntegerProperty cIDProperty() {
        return cID;
    }

    public void setcID(int cID) {
        this.cID.set(cID);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }
}
