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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import static com.example.barbershopfx.Database.DBConnection.*;
import static com.example.barbershopfx.Database.DBConnection.CloseConnection;
import static com.example.barbershopfx.MainScreen.MainScreen.getTheChosenDate;
import static com.example.barbershopfx.MainScreen.MainScreen.textToTravel;

public class EmployeeDailyStatesScreen implements Initializable {

    String myText ;
    LocalDate myDate;

    public EmployeeDailyStatesScreen() {
        this.myText = textToTravel;
        this.myDate = getTheChosenDate;
        textToTravel = null;
        getTheChosenDate = null;
    }

    @FXML
    private Label dateText;

    @FXML
    private Label nameText;

    @FXML
    private TableView<Item> myTable;

    private ObservableList<Item> dataForItemTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Item,String> billNumberColumn;

    @FXML
    private TableColumn<Item,String> cashierNameColumn;

    @FXML
    private TableColumn<Item,String> employeeNameColumn;

    @FXML
    private TableColumn<Item,String> cashColumn;

    @FXML
    private TableColumn<Item,String> timeColumn;


    public static class Item{
        private final SimpleStringProperty billNumber ;
        private final SimpleStringProperty cashierName ;
        private final SimpleStringProperty employeeName ;
        private final SimpleStringProperty cash ;
        private final SimpleStringProperty time ;

        public Item(String billNumber, String cashierName, String employeeName, String cash, String time) {
            this.billNumber = new SimpleStringProperty(billNumber);
            this.cashierName = new SimpleStringProperty(cashierName);
            this.employeeName = new SimpleStringProperty(employeeName);
            this.cash = new SimpleStringProperty(cash);
            this.time = new SimpleStringProperty(time);
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


        public String getCash() {
            return cash.get();
        }

        public SimpleStringProperty cashProperty() {
            return cash;
        }

        public void setCash(String cash) {
            this.cash.set(cash);
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

        public String getBillNumber() {
            return billNumber.get();
        }

        public SimpleStringProperty billNumberProperty() {
            return billNumber;
        }

        public void setBillNumber(String billNumber) {
            this.billNumber.set(billNumber);
        }

        public String getCashierName() {
            return cashierName.get();
        }

        public SimpleStringProperty cashierNameProperty() {
            return cashierName;
        }

        public void setCashierName(String cashierName) {
            this.cashierName.set(cashierName);
        }
    }

    public void setTheItemsTable(){
        billNumberColumn.setCellValueFactory(new PropertyValueFactory<>("billNumber"));
        cashierNameColumn.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        cashColumn.setCellValueFactory(new PropertyValueFactory<>("cash"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        myTable.setItems(dataForItemTable);
        myTable.setPlaceholder(new Label("لا يوجد عناصر بعد !"));
    }

    public void LoadTheItemsTable(){
        try {
            OpenConnection();

            dataForItemTable.clear();

            Calendar calendar = new GregorianCalendar
                    (myDate.getYear(), myDate.getMonthValue()-1, myDate.getDayOfMonth());
            long start = calendar.getTimeInMillis();
            Timestamp timestampStart = new Timestamp(start);


            calendar.add(Calendar.DATE,1);

            long end = calendar.getTimeInMillis();
            Timestamp timestampEnd = new Timestamp(end);

            PreparedStatement billsStmt =
                    connection.prepareStatement("SELECT * FROM bills WHERE" +
                            " last_time_updated >= ? AND last_time_updated < ? " +
                            "AND worker_user_name = ?");

            billsStmt.setTimestamp(1, timestampStart);
            billsStmt.setTimestamp(2, timestampEnd);
            billsStmt.setString(3, myText);

            ResultSet billsResultSet = billsStmt.executeQuery();

            while (billsResultSet.next()){
                dataForItemTable.add(new Item(
                        billsResultSet.getString("bill_number"),
                        billsResultSet.getString("cashier_user_name"),
                        billsResultSet.getString("worker_user_name"),
                        billsResultSet.getString("cash"),
                        billsResultSet.getString("last_time_updated")
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
                (myDate.getYear(), myDate.getMonthValue()-1, myDate.getDayOfMonth());

        dateText.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        nameText.setText(myText);
        // set the table
        setTheItemsTable();
        LoadTheItemsTable();
    }



}
