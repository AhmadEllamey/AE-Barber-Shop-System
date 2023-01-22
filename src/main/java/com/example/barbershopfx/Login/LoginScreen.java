package com.example.barbershopfx.Login;

import com.example.barbershopfx.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static com.example.barbershopfx.Database.DBConnection.*;

public class LoginScreen {



    @FXML
    private JFXTextField UserNameText;

    @FXML
    private JFXPasswordField PasswordText;

    private static String TheCurrentUser;

    public static String getTheUsername(){
        return TheCurrentUser;
    }

    private static String userType ;

    public static String getUserType() {
        return userType;
    }

    @FXML
    void HowToContactUsFun() {
        // show alert that show contact ways
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("تواصل معنا");
        a.setHeaderText("كيف تتواصل معنا ؟");
        a.setContentText("اتصل --> 01061619332 \nانضم الي صفحه AE Systems علي فيس بوك\nارسل الي Ahmad Ellamey علي فيسبوك");
        a.show();
    }

    @FXML
    void loginManagement() {

        System.out.println("login function");
        if(!UserNameText.getText().trim().isEmpty() && !PasswordText.getText().trim().isEmpty()){
            try {
                OpenConnection();

                PreparedStatement stmt = connection.prepareStatement("SELECT * From users_info WHERE user_name = ? AND password = ?");
                stmt.setString(1,UserNameText.getText().trim());
                stmt.setString(2,PasswordText.getText().trim());
                ResultSet resultSet = stmt.executeQuery();

                if(resultSet.next()){
                    // we found the user
                    if(PasswordText.getText().trim().equals(resultSet.getString("password"))){
                        // we checked the password again with capital and small cases
                        TheCurrentUser = UserNameText.getText().trim();
                        if(resultSet.getString("worker_type").equals("M")){
                            userType = "M";
                        }else {
                            userType = "E";
                        }
                        // open the main screen
                        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MainScreen.fxml"));
                        Parent mainCallWindowFXML = loader.load();
                        HelloApplication.getTheStage().setTitle("AKA Management System !");
                        Scene scene = new Scene(mainCallWindowFXML,800,600);

                        // how to set the style sheets
                        URL url = HelloApplication.class.getResource("mytab.css");
                        if (url == null) {
                            System.out.println("Resource not found. Aborting.");
                            System.exit(-1);
                        }
                        String css = url.toExternalForm();
                        scene.getStylesheets().add(css);


                        HelloApplication.getTheStage().setScene(scene);
                        //mainScreenStage.setFullScreen(true);
                        HelloApplication.getTheStage().show();
                    }else {
                        showLoginAlert();
                    }
                }else {
                    // show alert that username or password is wrong
                    showLoginAlert();
                }
                CloseConnection();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }

        }

    }


    public void showLoginAlert(){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("فشل تسجيل الدخول");
        a.setHeaderText("تحقق لماذا فشل تسجيل الدخول");
        a.setContentText("اسم المستخدم او كلمه المرور خاطئه , حاول مره اخري !");
        a.show();
    }





}
