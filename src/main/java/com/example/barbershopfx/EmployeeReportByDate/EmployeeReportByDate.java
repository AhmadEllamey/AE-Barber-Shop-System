package com.example.barbershopfx.EmployeeReportByDate;


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


public class EmployeeReportByDate implements Initializable {


    String myText ;
    LocalDate myDate;

    public EmployeeReportByDate() {
        this.myText = textToTravel;
        this.myDate = getTheChosenDate;
        System.out.println("hello con" + myText);
        textToTravel = null;
        getTheChosenDate = null;
        System.out.println("hello con" + myText);
    }

    @FXML
    private Label dateText;

    @FXML
    private Label nameText;

    @FXML
    private TableView<Item> myTable;

    private ObservableList<Item> dataForItemTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Item, String> employeeNameColumn;

    @FXML
    private TableColumn<Item, String> noteColumn;

    @FXML
    private TableColumn<Item, String> cashColumn;

    @FXML
    private TableColumn<Item, String> typeColumn;

    @FXML
    private TableColumn<Item, String> timeColumn;

    public static class Item{
        private final SimpleStringProperty employeeName ;
        private final SimpleStringProperty note ;
        private final SimpleStringProperty cash ;
        private final SimpleStringProperty type ;
        private final SimpleStringProperty time ;

        public Item(String employeeName, String note, String cash, String type, String time) {
            this.employeeName = new SimpleStringProperty(employeeName);
            this.note = new SimpleStringProperty(note);
            this.cash = new SimpleStringProperty(cash);
            this.type = new SimpleStringProperty(type);
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

        public String getNote() {
            return note.get();
        }

        public SimpleStringProperty noteProperty() {
            return note;
        }

        public void setNote(String note) {
            this.note.set(note);
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
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        cashColumn.setCellValueFactory(new PropertyValueFactory<>("cash"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
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

            PreparedStatement transactionsStmt =
                    connection.prepareStatement("SELECT * FROM transactions WHERE" +
                            " last_time_updated >= ? AND last_time_updated < ? " +
                            "AND username = ?");

            transactionsStmt.setTimestamp(1, timestampStart);
            transactionsStmt.setTimestamp(2, timestampEnd);
            transactionsStmt.setString(3, myText);

            System.out.println(transactionsStmt);
            ResultSet transactionsResultSet = transactionsStmt.executeQuery();

            System.out.println(transactionsStmt);
            while (transactionsResultSet.next()){

                String type ;
                if(transactionsResultSet.getString("type").equals("W")){
                    type = "سحب";
                }else{
                    type = "ايداع";
                }
                dataForItemTable.add(new Item(
                        transactionsResultSet.getString("username"),
                        transactionsResultSet.getString("note"),
                        transactionsResultSet.getString("cash"),
                        type,
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

        Calendar calendar = new GregorianCalendar
                (myDate.getYear(), myDate.getMonthValue()-1, myDate.getDayOfMonth());

        dateText.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        nameText.setText(myText);
        // set the table
        setTheItemsTable();
        LoadTheItemsTable();

    }
}
