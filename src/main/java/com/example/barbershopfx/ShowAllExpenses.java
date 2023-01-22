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

public class ShowAllExpenses implements Initializable {

    String myText ;
    LocalDate myDate;

    public ShowAllExpenses() {
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
    private TableColumn<Item,String> employeeNameColumn;

    @FXML
    private TableColumn<Item,String> salaryColumn;

    @FXML
    private TableColumn<Item,String> depositColumn;

    @FXML
    private TableColumn<Item,String> withdrawColumn;

    @FXML
    private TableColumn<Item,String> timeColumn;

    public static class Item{
        private final SimpleStringProperty id ;
        private final SimpleStringProperty employeeName ;
        private final SimpleStringProperty salary ;
        private final SimpleStringProperty deposit ;
        private final SimpleStringProperty withdraw ;
        private final SimpleStringProperty time ;

        public Item(String id,String employeeName, String salary, String deposit, String withdraw, String time) {
            this.id = new SimpleStringProperty(id);
            this.employeeName = new SimpleStringProperty(employeeName);
            this.salary = new SimpleStringProperty(salary);
            this.deposit = new SimpleStringProperty(deposit);
            this.withdraw = new SimpleStringProperty(withdraw);
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

        public String getSalary() {
            return salary.get();
        }

        public SimpleStringProperty salaryProperty() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary.set(salary);
        }

        public String getDeposit() {
            return deposit.get();
        }

        public SimpleStringProperty depositProperty() {
            return deposit;
        }

        public void setDeposit(String deposit) {
            this.deposit.set(deposit);
        }

        public String getWithdraw() {
            return withdraw.get();
        }

        public SimpleStringProperty withdrawProperty() {
            return withdraw;
        }

        public void setWithdraw(String withdraw) {
            this.withdraw.set(withdraw);
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
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        depositColumn.setCellValueFactory(new PropertyValueFactory<>("deposit"));
        withdrawColumn.setCellValueFactory(new PropertyValueFactory<>("withdraw"));
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

            PreparedStatement expensesStmt =
                    connection.prepareStatement("SELECT * FROM expenses WHERE" +
                            " last_time_updated >= ? AND last_time_updated < ? " );

            expensesStmt.setTimestamp(1, timestampStart);
            expensesStmt.setTimestamp(2, timestampEnd);


            ResultSet expensesResultSet = expensesStmt.executeQuery();

            while (expensesResultSet.next()){
                dataForItemTable.add(new Item(
                        expensesResultSet.getString("id"),
                        expensesResultSet.getString("username"),
                        expensesResultSet.getString("salary"),
                        expensesResultSet.getString("balance"),
                        expensesResultSet.getString("withdraw"),
                        expensesResultSet.getString("last_time_updated")
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
        nameText.setText("فواتير الرواتب");
        // set the table
        setTheItemsTable();
        LoadTheItemsTable();

    }


}
