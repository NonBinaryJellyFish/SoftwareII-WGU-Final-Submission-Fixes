import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.JDBC;


public class softwareSchedulerMain extends Application {

    @FXML
    private Parent root;


    public static void main(String[] args){
        launch(args);
    }

    //This is the main program that initiates the login.properties screen.
    @Override
    public void start(Stage stageMain) throws Exception {
        // Initial scene
        this.root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXML/loginScreen.fxml")));
        Scene scene = new Scene(root);
        stageMain.setScene(scene);
        stageMain.setTitle("Appointments Scheduler");
        stageMain.setResizable(false);
        stageMain.show();

        //Scene2


    }
}
