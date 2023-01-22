package com.example.barbershopfx.ItemHistory;

import com.jfoenix.controls.JFXButton;
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
import java.util.ResourceBundle;

import static com.example.barbershopfx.Database.DBConnection.*;
import static com.example.barbershopfx.Database.DBConnection.CloseConnection;

public class ItemHistoryScreen implements Initializable {



    String barcode ;

    public static class Item{
        private final SimpleStringProperty name ;
        private final SimpleStringProperty barcode ;
        private final SimpleStringProperty cost ;
        private final SimpleStringProperty price ;
        private final SimpleStringProperty amount ;
        private final SimpleStringProperty vendor ;
        private final SimpleStringProperty time ;

        public Item(String name, String barcode, String cost, String price, String amount ,String vendor , String time) {
            this.name = new SimpleStringProperty(name);
            this.barcode = new SimpleStringProperty(barcode);
            this.cost = new SimpleStringProperty(cost);
            this.price = new SimpleStringProperty(price);
            this.amount = new SimpleStringProperty(amount);
            this.vendor = new SimpleStringProperty(vendor);
            this.time = new SimpleStringProperty(time);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
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

        public String getCost() {
            return cost.get();
        }

        public SimpleStringProperty costProperty() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost.set(cost);
        }

        public String getPrice() {
            return price.get();
        }

        public SimpleStringProperty priceProperty() {
            return price;
        }

        public void setPrice(String price) {
            this.price.set(price);
        }

        public String getAmount() {
            return amount.get();
        }

        public SimpleStringProperty amountProperty() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount.set(amount);
        }

        public String getVendor() {
            return vendor.get();
        }

        public SimpleStringProperty vendorProperty() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor.set(vendor);
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

    @FXML
    private TableView<Item> itemHistoryTable;

    private ObservableList<Item> dataForItemTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Item,String> nameColumn;

    @FXML
    private TableColumn<Item,String> barcodeColumn;

    @FXML
    private TableColumn<Item,String> costColumn;

    @FXML
    private TableColumn<Item,String> priceColumn;

    @FXML
    private TableColumn<Item,String> amountColumn;

    @FXML
    private TableColumn<Item,String> vendorColumn;

    @FXML
    private TableColumn<Item,String> timeColumn;

    @FXML
    private JFXButton doneButton;

    @FXML
    void dismissTheScreenFunction(ActionEvent event) {
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }

    void loadTheHistoryTable(){
        try {
            System.out.println(barcode);
            OpenConnection();
            dataForItemTable.clear();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM items_details WHERE barcode = ?");
            statement.setString(1,barcode);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                dataForItemTable.add(
                        new Item(resultSet.getString("name"),
                                resultSet.getString("barcode"),
                                resultSet.getString("original_price"),
                                resultSet.getString("public_price"),
                                resultSet.getString("amount"),
                                resultSet.getString("vendor"),
                                resultSet.getString("last_time_updated"))
                );
            }
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }


    public void setBarcode(String barcode) {
        this.barcode = barcode;
        loadTheHistoryTable();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        vendorColumn.setCellValueFactory(new PropertyValueFactory<>("vendor"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        itemHistoryTable.setItems(dataForItemTable);
        itemHistoryTable.setPlaceholder(new Label("لا يوجد تاريخ بعد !"));
    }
}
