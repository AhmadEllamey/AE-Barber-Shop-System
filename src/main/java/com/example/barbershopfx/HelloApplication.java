package com.example.barbershopfx;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application{


    private static Stage myStage ;
    private static Stage announcementStage;


    private static Stage reportScreen;
    public static Stage getReportScreen() {
        return reportScreen;
    }



    public static String BillPath = "";
    public static String PayWithPoints = "";

    public static Stage getTheStage(){
        return myStage;
    }

    public static Stage getAnnouncementStage() {
        URL urlIcon = HelloApplication.class.getResource("app.jpg");
        assert urlIcon != null;
        announcementStage.getIcons().add(new Image(urlIcon.toExternalForm()));
        return announcementStage;
    }

    @Override
    public void start(Stage stage) {
        reportScreen = new Stage();
        announcementStage = new Stage();
        Splash splash = new Splash(this);
        splash.show();

        Stage splashStage = new Stage();
        //splashStage.setOpacity(0.2);
        Scene splashScene = splash.getSplashScene();
        splashStage.setScene(splashScene);
        splashStage.setResizable(false);

        URL urlIconS = this.getClass().getResource("aka.jpeg");
        System.out.println(urlIconS);
        assert urlIconS != null;
        splashStage.getIcons().add(new Image(urlIconS.toExternalForm()));

        splashStage.initStyle(StageStyle.UNDECORATED);
        splash.getSequentialTransition().setOnFinished(e -> {
            Timeline timeline = new Timeline();
            KeyFrame key = new KeyFrame(Duration.millis(800),
                    new KeyValue(splash.getSplashScene().getRoot().opacityProperty(), 0));
            timeline.getKeyFrames().add(key);
            timeline.setOnFinished((event) -> {
                try {
                    URL urlIcon = this.getClass().getResource("aka.jpeg");
                    System.out.println(urlIcon);
                    assert urlIcon != null;
                    stage.getIcons().add(new Image(urlIcon.toExternalForm()));
                    stage.setResizable(true);
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginScreen.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 500, 600);

                    URL url = this.getClass().getResource("application.css");
                    if (url == null) {
                        System.out.println("Resource not found. Aborting.");
                        System.exit(-1);
                    }
                    String css = url.toExternalForm();
                    scene.getStylesheets().add(css);

                    stage.setTitle("AKA Systems");
                    stage.setScene(scene);
                    myStage = stage;
                    stage.show();
                    splashStage.close();

                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            timeline.play();
        });
        splashStage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}