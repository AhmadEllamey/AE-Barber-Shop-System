package com.example.barbershopfx;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.net.URL;

public class Splash
{

    static Scene splash;
    static Rectangle rect = new Rectangle();
    final private Pane pane;
    VBox vBox1 = new VBox();
    final private VBox vBox = new VBox();
    final private HBox hBox = new HBox();
    final private SequentialTransition seqT;

    HelloApplication helloApplication ;

    public Splash(HelloApplication helloApplication)
    {

        this.helloApplication = helloApplication;
        pane = new Pane();
        pane.setStyle("-fx-background-color:black");
        vBox.setStyle("-fx-background-color:black");
        hBox.setStyle("-fx-background-color:black");
        vBox1.setStyle("-fx-background-color:black");
        splash = new Scene(vBox1,400,300);
        seqT = new SequentialTransition();
    }

    public void show()
    {
        /*
         * Part 1:
         * This is the rolling square animation.
         * This animation looks cool for a loading screen,
         * so I made this. Only the lines of code for fading
         * from Stack Overflow.
         */
        //rectangle insertion
        int scale = 30;
        int dur = 400;
        rect = new Rectangle(100 - 2 * scale, 20, scale, scale);
        rect.setFill(Color.WHITE);

        //actual animations
        //initialising the sequentialTranslation
        //umm, ignore this, just some configs that work magic
        int[] rotins = {scale, 2 * scale, 3 * scale, 4 * scale, 5 * scale,6 * scale,7 * scale,8 * scale, -9 * scale, -8 * scale , -7 * scale, -6 * scale, -5 * scale, -4 * scale , -3 * scale, -2 * scale};
        int x, y;
        for (int i : rotins) {
            //rotating the square
            RotateTransition rt = new RotateTransition(Duration.millis(dur), rect);
            rt.setByAngle(i / Math.abs(i) * 90);
            rt.setCycleCount(1);
            //moving the square horizontally
            TranslateTransition pt = new TranslateTransition(Duration.millis(dur), rect);
            x = (int) (rect.getX() + Math.abs(i));
            y = (int) (rect.getX() + Math.abs(i) + (Math.abs(i) / i) * scale);
            pt.setFromX(x);
            pt.setToX(y);
            //parallelly execute them and you get a rolling square
            ParallelTransition pat = new ParallelTransition();
            pat.getChildren().addAll(pt, rt);
            pat.setCycleCount(1);
            seqT.getChildren().add(pat);
        }
        //playing the animation
        seqT.play();
        //lambda code sourced from StackOverflow, fades away stage
        seqT.setNode(rect);
        //The text part
        Label label = new Label("AKA Systems");
        label.setFont(new Font("Verdana", 40));
        label.setStyle("-fx-text-fill:white");
        label.setLayoutX(140);
        label.setLayoutY(70);
        Label lab = new Label("Launching...");
        lab.setFont(new Font("Times New Roman", 10));
        lab.setStyle("-fx-text-fill:white");
        lab.setLayoutX(170);
        lab.setLayoutY(180);
        //A complimentary image

        URL url = helloApplication.getClass().getResource("aka.jpeg");
        System.out.println(url);
        assert url != null;
        Image image = new Image(url.toExternalForm());
        ImageView iv = new ImageView(image);
        iv.setFitWidth(80);
        iv.setFitHeight(80);
        iv.setX(174);
        iv.setY(130);
        //now adding everything to position, opening the stage, start the animation

        // Vbox
        // have an HBox that start from the left
        // have an HBox that start from the center >>> have an VBox that start from center

        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.CENTER_LEFT);
        hBox1.getChildren().addAll(rect);

        hBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(25);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label, lab, iv);
        hBox.getChildren().addAll(vBox);

        vBox1.setSpacing(25);
        vBox1.setPadding(new Insets(10,10,10,10));
        vBox1.getChildren().addAll(hBox1 , vBox);




        //pane.getChildren().addAll(hBox);

        seqT.play();
    }

    public Scene getSplashScene()
    {
        return splash;
    }

    public SequentialTransition getSequentialTransition()
    {
        return seqT;
    }
}
