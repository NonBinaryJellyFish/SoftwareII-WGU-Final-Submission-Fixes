package Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class FLD {

    private IntegerProperty divisionID;
    private StringProperty Division;
    private StringProperty createDate;
    private StringProperty createdBy;
    private StringProperty lastUpdate;
    private StringProperty lastUpdateBy;
    private IntegerProperty Country_ID;

    /**
     * Overloaded constructor for setting the values of first level divison.
     * @param divisionID
     * @param division
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdateBy
     * @param country_ID
     */
    public FLD(IntegerProperty divisionID, StringProperty division, StringProperty createDate, StringProperty createdBy, StringProperty lastUpdate, StringProperty lastUpdateBy, IntegerProperty country_ID) {
        this.divisionID = divisionID;
        Division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        Country_ID = country_ID;
    }

    public FLD() {
    }



}
