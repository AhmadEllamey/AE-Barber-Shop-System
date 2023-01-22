package com.example.barbershopfx.UpdateItem;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static com.example.barbershopfx.Database.DBConnection.*;
import static com.example.barbershopfx.Database.DBConnection.CloseConnection;

public class UpdateTheItemScreen implements Initializable {


    String barcode ;
    String name ;

    @FXML
    private Label itemNameText;

    @FXML
    private JFXTextField itemAmountText;

    @FXML
    private JFXTextField itemPriceText;

    @FXML
    private JFXTextField itemVendorText;

    @FXML
    private JFXTextField publicItemPriceText;

    @FXML
    private JFXButton updateItemButton;

    @FXML
    void updateItemFunction(ActionEvent event) {

        if(!itemPriceText.getText().trim().isEmpty()&&
                !itemVendorText.getText().trim().isEmpty()&&
                !publicItemPriceText.getText().trim().isEmpty()&&
                !publicItemPriceText.getText().trim().equals("0.0")&&
                !itemAmountText.getText().trim().isEmpty()){
            try {
                OpenConnection();

                // update the details table
                PreparedStatement stmt = connection.
                        prepareStatement("Insert Into items_details " +
                                "(name, barcode, original_price, public_price, amount, vendor) " +
                                "VALUES (?,?,?,?,?,?)");

                stmt.setString(1,name);
                stmt.setString(2,barcode);
                stmt.setString(3,itemPriceText.getText().trim());
                stmt.setString(4,publicItemPriceText.getText().trim());
                stmt.setString(5,itemAmountText.getText().trim());
                stmt.setString(6,itemVendorText.getText().trim());
                stmt.executeUpdate();

                // update the items table with the item numbers
                PreparedStatement stmtTwo = connection.
                        prepareStatement("UPDATE items_info SET available = available + ? ,public_price = ? WHERE barcode = ?");

                stmtTwo.setString(1,itemAmountText.getText().trim());
                stmtTwo.setString(2,publicItemPriceText.getText().trim());
                stmtTwo.setString(3,barcode);
                stmtTwo.executeUpdate();

                // close the window
                Stage stage = (Stage) updateItemButton.getScene().getWindow();
                stage.close();

                CloseConnection();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }
        }

    }



    public void setBarcode(String barcode){
        this.barcode =barcode;
        System.out.println("--------> "+barcode);
        // set the item name
        try {
            OpenConnection();
            PreparedStatement stmt = connection.
                    prepareStatement("SELECT name FROM items_info WHERE barcode = ?");

            stmt.setString(1,barcode);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                itemNameText.setText(resultSet.getString("name"));
                name = resultSet.getString("name") ;
            }
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }

    public UpdateTheItemScreen(){
        System.out.println("--------> Constructor");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("--------> initializer");

        // make the numbers texts accepts only numbers
        Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };
        StringConverter<Double> converter = new StringConverter<Double>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0 ;
                } else {
                    return Double.valueOf(s);
                }
            }
            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };

        TextFormatter<Double> filterOne = new TextFormatter<>(converter,0.0, filter);
        TextFormatter<Double> filterTwo = new TextFormatter<>(converter, 0.0, filter);
        TextFormatter<Double> filterThree = new TextFormatter<>(converter, 0.0, filter);
        itemAmountText.setTextFormatter(filterOne);
        publicItemPriceText.setTextFormatter(filterTwo);
        itemPriceText.setTextFormatter(filterThree);


    }
}




