package Models;

public class userSession {

    private String userName;
    private int userID;

    //Overloaded constructor that will be used in other functions.
    public userSession(String userName, int userID) {
        this.userName = userName;
        this.userID = userID;
    }

    // Getters and setters for the userSession and user/userid variables.
    public userSession(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
