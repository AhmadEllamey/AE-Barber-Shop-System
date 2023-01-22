package com.example.barbershopfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import static com.example.barbershopfx.Database.DBConnection.*;
import static com.example.barbershopfx.Database.DBConnection.CloseConnection;
import static com.example.barbershopfx.MainScreen.MainScreen.mode;
import static com.example.barbershopfx.MainScreen.MainScreen.textToTravel;

public class DetailsAboutBillNumber implements Initializable {


    String myText ;


    public DetailsAboutBillNumber() {
        this.myText = textToTravel;
        textToTravel = null;
    }

    @FXML
    private Label dateText;

    @FXML
    private Label nameText;

    @FXML
    private TableView<Item> myTable;

    private ObservableList<Item> dataForItemTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Item, String> barcodeColumn;

    @FXML
    private TableColumn<Item, String> itemNameColumn;

    @FXML
    private TableColumn<Item, String> itemPriceColumn;

    @FXML
    private TableColumn<Item, String> itemTypeColumn;

    @FXML
    private TableColumn<Item, String> timeColumn;

    public static class Item{
        private final SimpleStringProperty barcode ;
        private final SimpleStringProperty itemName ;
        private final SimpleStringProperty itemPrice ;
        private final SimpleStringProperty itemType ;
        private final SimpleStringProperty time ;

        public Item(String barcode, String itemName, String itemPrice, String itemType, String time) {
            this.barcode = new SimpleStringProperty(barcode);
            this.itemName = new SimpleStringProperty(itemName);
            this.itemPrice = new SimpleStringProperty(itemPrice);
            this.itemType = new SimpleStringProperty(itemType);
            this.time = new SimpleStringProperty(time);
        }


        public String getBarcode() {
            return barcode.get();
        }

        public SimpleStringProperty barcodeProperty() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode.set(barcode);
        }

        public String getItemName() {
            return itemName.get();
        }

        public SimpleStringProperty itemNameProperty() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName.set(itemName);
        }

        public String getItemPrice() {
            return itemPrice.get();
        }

        public SimpleStringProperty itemPriceProperty() {
            return itemPrice;
        }

        public void setItemPrice(String itemPrice) {
            this.itemPrice.set(itemPrice);
        }

        public String getItemType() {
            return itemType.get();
        }

        public SimpleStringProperty itemTypeProperty() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType.set(itemType);
        }

        public String getTime() {
            return time.get();
        }

        public SimpleStringProperty timeProperty() {
            return time;
        }

        public void setTime(String time) {
            this.time.set(time);
        }
    }

    public void setTheItemsTable(){

        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        myTable.setItems(dataForItemTable);
        myTable.setPlaceholder(new Label("لا يوجد عناصر بعد !"));
    }

    public void LoadTheItemsTable(){
        try {
            OpenConnection();

            dataForItemTable.clear();

            PreparedStatement billInfoStmt =
                    connection.prepareStatement("SELECT * FROM bills_info WHERE bill_number = ?");
            billInfoStmt.setString(1, myText);

            ResultSet billsInfoResultSet = billInfoStmt.executeQuery();

            while (billsInfoResultSet.next()){
                dataForItemTable.add(new Item(
                        billsInfoResultSet.getString("item_barcode"),
                        billsInfoResultSet.getString("item_name"),
                        billsInfoResultSet.getString("item_price"),
                        billsInfoResultSet.getString("item_type").equals("S") ? "خدمه" :"سلعه",
                        billsInfoResultSet.getString("last_time_updated")
                        )
                );
            }

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }

    @FXML
    void DoneFunction(ActionEvent event) {
        Stage stage = (Stage) myTable.getScene().getWindow();
        stage.close();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Calendar calendar = new GregorianCalendar
                (LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1, LocalDate.now().getDayOfMonth());

        dateText.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        nameText.setText(" فاتوره رقم : "+myText);


        setTheItemsTable();
        LoadTheItemsTable();


    }


}
