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
import java.util.ResourceBundle;

import static com.example.barbershopfx.Database.DBConnection.*;
import static com.example.barbershopfx.Database.DBConnection.CloseConnection;
import static com.example.barbershopfx.MainScreen.MainScreen.mode;

public class ShowAllTransactions implements Initializable {


    @FXML
    private TableView<Item> myTable;

    private ObservableList<Item> dataForItemTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Item,String> billNumberColumn;

    @FXML
    private TableColumn<Item,String> employeeNameColumn;

    @FXML
    private TableColumn<Item,String> totalColumn;

    @FXML
    private TableColumn<Item,String> noteColumn;

    @FXML
    private TableColumn<Item,String> typeColumn;

    @FXML
    private TableColumn<Item,String> timeColumn;

    public static class Item{
        private final SimpleStringProperty id ;
        private final SimpleStringProperty employeeName ;
        private final SimpleStringProperty total ;
        private final SimpleStringProperty note ;
        private final SimpleStringProperty type ;
        private final SimpleStringProperty time ;

        public Item(String id, String employeeName, String total, String note,String type, String time) {
            this.id = new SimpleStringProperty(id);
            this.employeeName = new SimpleStringProperty(employeeName);
            this.total = new SimpleStringProperty(total);
            this.note = new SimpleStringProperty(note);
            this.type = new SimpleStringProperty(type);
            this.time = new SimpleStringProperty(time);
        }

        public String getId() {
            return id.get();
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public String getEmployeeName() {
            return employeeName.get();
        }

        public SimpleStringProperty employeeNameProperty() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName.set(employeeName);
        }

        public String getTotal() {
            return total.get();
        }

        public SimpleStringProperty totalProperty() {
            return total;
        }

        public void setTotal(String total) {
            this.total.set(total);
        }

        public String getNote() {
            return note.get();
        }

        public SimpleStringProperty noteProperty() {
            return note;
        }

        public void setNote(String note) {
            this.note.set(note);
        }

        public String getType() {
            return type.get();
        }

        public SimpleStringProperty typeProperty() {
            return type;
        }

        public void setType(String type) {
            this.type.set(type);
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

        billNumberColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        myTable.setItems(dataForItemTable);
        myTable.setPlaceholder(new Label("لا يوجد عناصر بعد !"));
    }

    public void LoadTheItemsTable(){
        try {
            OpenConnection();

            dataForItemTable.clear();

            PreparedStatement transactionsStmt =
                    connection.prepareStatement("SELECT * FROM transactions");


            ResultSet transactionsResultSet = transactionsStmt.executeQuery();

            while (transactionsResultSet.next()){
                dataForItemTable.add(new Item(
                        transactionsResultSet.getString("id"),
                        transactionsResultSet.getString("username"),
                        transactionsResultSet.getString("cash"),
                        transactionsResultSet.getString("note"),
                        transactionsResultSet.getString("type").equals("W") ? "سحب" : "ايداع",
                        transactionsResultSet.getString("last_time_updated")
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
        setTheItemsTable();
        LoadTheItemsTable();
    }
}
