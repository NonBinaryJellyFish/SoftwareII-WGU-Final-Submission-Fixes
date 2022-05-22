/*
 The purpose of this file is to later implement a system where the user is prompted to create a database if there is not a sql.properties that already exists. The file is irrelevant to the PA.
 */

CREATE TABLE IF NOT EXISTS Countries(
    Country_ID INT(10),
    Country VARCHAR(50),
    Create_Date DATETIME,
    Created_By VARCHAR(50),
    Last_Update TIMESTAMP,
    Last_Updated_By VARCHAR(50),
    PRIMARY KEY (Country_ID)
    );

CREATE TABLE IF NOT EXISTS First_Level_Divisions(
    Division_ID INT(10),
    Division VARCHAR(50),
    Create_Date DATETIME,
    Created_By VARCHAR(50),
    Last_Update TIMESTAMP,
    Last_Updated_By VARCHAR(50),
    Country_ID INT(10),
    Primary Key (Division_ID),
    FOREIGN KEY (Country_ID) REFERENCES Countries(Country_ID)
    );

CREATE TABLE IF NOT EXISTS Customers(
    Customer_ID INT(10) NOT NULL AUTO_INCREMENT,
    Customer_Name VARCHAR(5),
    Address VARCHAR(100),
    Postal_Code VARCHAR(50),
    Phone VARCHAR(50),
    Create_Date DATETIME,
    Created_By VARCHAR(50),
    Last_Update TIMESTAMP,
    Last_Updated_By VARCHAR(50),
    Division_ID INT(10),
    PRIMARY KEY (Customer_ID),
    FOREIGN KEY (Division_ID) REFERENCES First_Level_Divisions(Division_ID)
    );

CREATE TABLE IF NOT EXISTS Users(
    User_ID INT(10) NOT NULL AUTO_INCREMENT,
    User_Name VARCHAR(50) UNIQUE,
    User_Password TEXT,
    Create_Date DATETIME,
    Created_By VARCHAR(50),
    Last_Update TIMESTAMP,
    Last_Updated_By VARCHAR(50),
    PRIMARY KEY (User_ID)
    );

CREATE TABLE IF NOT EXISTS Contacts(
    Contact_ID INT(10) NOT NULL AUTO_INCREMENT,
    Contact_Name VARCHAR(50),
    Email VARCHAR(50),
    PRIMARY KEY (Contact_ID)
    );

CREATE TABLE IF NOT EXISTS Appointments(
    Appointment_ID INT(10) NOT NULL AUTO_INCREMENT,
    Title VARCHAR(50),
    Description VARCHAR(50),
    Location VARCHAR(50),
    Appt_Type VARCHAR(50),
    Start_Date_Time DATETIME,
    End_Date_Time DATETIME,
    Create_Date DATETIME,
    Created_By VARCHAR(50),
    Last_Update TIMESTAMP,
    Last_Updated_By VARCHAR(50),
    Customer_ID INT(10),
    User_ID INT(10),
    Contact_ID INT(10),
    PRIMARY KEY (Appointment_ID),
    FOREIGN KEY (Customer_ID) REFERENCES Customers(Customer_ID),
    FOREIGN KEY (User_ID) REFERENCES Users(User_ID),
    FOREIGN KEY (Contact_ID) REFERENCES Contacts(Contact_ID)
    );