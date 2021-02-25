package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Thyroid dataset study");
        primaryStage.setScene(new Scene(root, 1152, 700));
        primaryStage.show();

        /*
        Parent root = FXMLLoader.load(getClass().getResource("Temp.fxml"));
        primaryStage.setTitle("Box plot - data input");
        primaryStage.setMinHeight(360);
        primaryStage.setMinWidth(640);
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();

         */
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static URL getLayoutResource(String s) {
        return Main.class.getClassLoader().getResource(s);
    }

}
