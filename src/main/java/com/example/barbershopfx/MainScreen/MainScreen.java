package com.example.barbershopfx.MainScreen;

import com.example.barbershopfx.HelloApplication;
import com.example.barbershopfx.ItemHistory.ItemHistoryScreen;
import com.example.barbershopfx.Login.LoginScreen;
import com.example.barbershopfx.UpdateItem.UpdateTheItemScreen;
import com.jfoenix.controls.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import static com.example.barbershopfx.Database.DBConnection.*;

public class MainScreen implements Initializable {

    public static LocalDate getTheChosenDate = null;
    public static String textToTravel = null;
    public static int mode = 0;

    // manage tabs

    @FXML
    private TabPane employeeTabPane;


    // manage the MainScreen page

    @FXML
    private JFXTextField cashForWithdrawText;

    @FXML
    private JFXTextArea reasonForWithdrawText;

    @FXML
    private JFXTextField cashForDepositText;

    @FXML
    private JFXTextArea reasonForDepositText;

    @FXML
    void CheckTheDepositFunction(ActionEvent event) {
        if(!cashForDepositText.getText().trim().isEmpty() && !reasonForDepositText.getText().trim().isEmpty()){
            double cash = 0.0;
            cash = Double.parseDouble(cashForDepositText.getText().trim());
            manageTransactions(cash,"D",reasonForDepositText.getText().trim());
            cashForDepositText.setText("0.0");
            reasonForDepositText.setText("");
        }
    }

    @FXML
    void CheckTheWithdrawFunction(ActionEvent event) {
        if(!cashForDepositText.getText().trim().isEmpty() && !reasonForWithdrawText.getText().trim().isEmpty()){
            double cash = 0.0;
            cash = Double.parseDouble(cashForWithdrawText.getText().trim())*-1;
            manageTransactions(cash,"W",reasonForWithdrawText.getText().trim());
            cashForWithdrawText.setText("0.0");
            reasonForWithdrawText.setText("");
        }
    }

    public void manageTransactions(Double cash , String type , String note){
        try {
            OpenConnection();
            PreparedStatement stmt = connection.
                    prepareStatement("insert into transactions (username, cash, note, type) VALUES (?,?,?,?)");
            stmt.setString(1, LoginScreen.getTheUsername());
            stmt.setString(2,cash.toString());
            stmt.setString(3,note);
            stmt.setString(4,type);
            stmt.executeUpdate();
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }


    // manage the employee page

    @FXML
    private JFXTextField employeePhoneText;

    @FXML
    private JFXTextField employeeAddressText;

    @FXML
    private JFXRadioButton employeeManagerRB;

    @FXML
    private JFXTextField employeeSalaryText;

    @FXML
    private JFXTextField employeeUsernameText;

    @FXML
    private JFXTextField employeePasswordText;

    @FXML
    private JFXTextField employeeFullNameText;

    @FXML
    private JFXTextField employeeIdText;

    @FXML
    private JFXTextField employeeAgeText;

    @FXML
    private JFXComboBox<Employee> chooseEmployeeComboBox;

    @FXML
    private JFXTextField employeePhoneEditText;

    @FXML
    private JFXTextField employeeAddressEditText;

    @FXML
    private JFXRadioButton employeeManagerEditRB;


    @FXML
    private JFXRadioButton employeeEmployeeEditRB;

    @FXML
    private JFXTextField employeeSalaryEditText;

    @FXML
    private JFXTextField employeeUsernameEditText;

    @FXML
    private JFXTextField employeePasswordEditText;

    @FXML
    private JFXTextField employeeFullNameEditText;

    @FXML
    private JFXTextField employeeIdEditText;

    @FXML
    private JFXTextField employeeAgeEditText;

    @FXML
    private JFXComboBox<Employee> chooseEmployeeForSalaryComboBox;

    @FXML
    private JFXTextField employeeUsernameForSalaryText;

    @FXML
    private JFXTextField employeeNameForSalaryText;

    @FXML
    private JFXTextField employeeSalaryForSalaryText;

    @FXML
    private JFXTextField employeeBalanceForSalaryText;

    @FXML
    private JFXTextField employeeDeptForSalaryText;

    @FXML
    private JFXTextField searchEmployeeTableByPhoneText;

    @FXML
    private JFXTextField searchEmployeeTableByUsernameText;

    @FXML
    private JFXRadioButton employeeCommissionRB;

    @FXML
    private JFXRadioButton employeeCommissionEditRB;

    @FXML
    private JFXRadioButton employeeNoCommissionEditRB;

    @FXML
    private TableView<EmployeeX> employeeTable;

    private ObservableList<EmployeeX> dataForEmployeeTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<EmployeeX,String> usernameEmployeeTableColumn;

    @FXML
    private TableColumn<EmployeeX,String> passwordEmployeeTableColumn;

    @FXML
    private TableColumn<EmployeeX,String> nameEmployeeTableColumn;

    @FXML
    private TableColumn<EmployeeX,String> nationalIdEmployeeTableColumn;

    @FXML
    private TableColumn<EmployeeX,String> ageEmployeeTableColumn;

    @FXML
    private TableColumn<EmployeeX,String> phoneEmployeeTableColumn;

    @FXML
    private TableColumn<EmployeeX,String> addressEmployeeTableColumn;

    @FXML
    private TableColumn<EmployeeX,String> jobTypeEmployeeTableColumn;

    @FXML
    private TableColumn<EmployeeX,String> salaryEmployeeTableColumn;

    @FXML
    private TableColumn<EmployeeX,String> balanceEmployeeTableColumn;

    @FXML
    private TableColumn<EmployeeX,String> deptEmployeeTableColumn;

    @FXML
    private JFXComboBox<EmployeeMoneyTransaction> chooseEmployeeForMoneyComboBox;

    @FXML
    private JFXTextField moneyToWithdrawText;

    @FXML
    private JFXTextField employeeTotalForSalaryText;



    @FXML
    private JFXRadioButton employeeEmployeeRB;

    @FXML
    private JFXRadioButton employeeNoCommissionRB;

    // first tab
    @FXML
    void addEmployeeFunction(ActionEvent event) {

        if(!employeeUsernameText.getText().trim().isEmpty()&&
                !employeePasswordText.getText().trim().isEmpty()&&
                !employeeFullNameText.getText().trim().isEmpty()&&
                !employeeIdText.getText().trim().isEmpty()&&
                !employeeAgeText.getText().trim().isEmpty()&&
                !employeePhoneText.getText().trim().isEmpty()&&
                !employeeAddressText.getText().trim().isEmpty()&&
                !employeeSalaryText.getText().trim().isEmpty()){
            try {
                OpenConnection();
                PreparedStatement stmt = connection.
                        prepareStatement("insert into " +
                                "users_info (user_name, password, worker_name, national_id, age, phone_number," +
                                " address, worker_type, commission, salary) VALUES (?,?,?,?,?,?,?,?,?,?)");

                String workerType = "E";
                String commission = "N";

                if(employeeManagerRB.isSelected()){
                    workerType = "M";
                }

                if(employeeCommissionRB.isSelected()){
                    commission = "Y";
                }

                stmt.setString(1,employeeUsernameText.getText().trim());
                stmt.setString(2,employeePasswordText.getText().trim());
                stmt.setString(3,employeeFullNameText.getText().trim());
                stmt.setString(4,employeeIdText.getText().trim());
                stmt.setString(5,employeeAgeText.getText().trim());
                stmt.setString(6,employeePhoneText.getText().trim());
                stmt.setString(7,employeeAddressText.getText().trim());
                stmt.setString(8,workerType);
                stmt.setString(9,commission);
                stmt.setString(10,employeeSalaryText.getText().trim());
                stmt.executeUpdate();
                loadTheEmployeeMoneyTransactionComboBox();
                employeeUsernameText.setText("");
                employeePasswordText.setText("");
                employeeFullNameText.setText("");
                employeeIdText.setText("");
                employeeAgeText.setText("");
                employeePhoneText.setText("");
                employeeAddressText.setText("");
                employeeSalaryText.setText("");

                employeeEmployeeRB.setSelected(false);
                employeeManagerRB.setSelected(false);

                employeeCommissionRB.setSelected(false);
                employeeNoCommissionRB.setSelected(false);

                CloseConnection();

            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
                // show alert tell the user to use another username
                showAlert("حدث خطأ !","حدث خطأ في التسجيل , حاول مره اخري !","ربما يكون اسم المستخدم موجود بالفعل\n غير اسم المستخدم و حاول مره اخري ! ");
            }
        }
    }

    public void showAlert(String title, String header ,String content){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(content);
        a.show();
    }

    // second tab

    class Employee {
        private  String user_name ;
        private String password ;
        private String worker_name ;
        private String national_id ;
        private int age ;
        private String phone_number ;
        private String address ;
        private String worker_type ;
        private String commission ;
        private double salary ;
        private double balance ;
        private double withdraw ;

        public Employee(String user_name,
                        String password,
                        String worker_name,
                        String national_id,
                        int age,
                        String phone_number,
                        String address,
                        String worker_type,
                        String commission,
                        double salary,
                        double balance,
                        double withdraw ) {
            this.user_name = user_name;
            this.password = password;
            this.worker_name = worker_name;
            this.national_id = national_id;
            this.age = age;
            this.phone_number = phone_number;
            this.address = address;
            this.worker_type = worker_type;
            this.commission = commission;
            this.salary = salary;
            this.balance = balance;
            this.withdraw = withdraw;
        }

        public double getBalance() {
            return balance;
        }

        public double getWithdraw() {
            return withdraw;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getPassword() {
            return password;
        }

        public String getWorker_name() {
            return worker_name;
        }

        public String getNational_id() {
            return national_id;
        }

        public int getAge() {
            return age;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public String getAddress() {
            return address;
        }

        public String getWorker_type() {
            return worker_type;
        }

        public String getCommission() {
            return commission;
        }

        public double getSalary() {
            return salary;
        }
    }

    public static class EmployeeX {
        private final SimpleStringProperty user_name ;
        private final SimpleStringProperty password ;
        private final SimpleStringProperty worker_name ;
        private final SimpleStringProperty national_id ;
        private final SimpleStringProperty age ;
        private final SimpleStringProperty phone_number ;
        private final SimpleStringProperty address ;
        private final SimpleStringProperty worker_type ;
        private final SimpleStringProperty commission ;
        private final SimpleStringProperty salary ;
        private final SimpleStringProperty balance ;
        private final SimpleStringProperty withdraw ;

        public EmployeeX(String user_name, String password, String worker_name, String national_id, String age, String phone_number, String address, String worker_type, String commission, String salary, String balance, String withdraw) {
            this.user_name = new SimpleStringProperty(user_name);
            this.password = new SimpleStringProperty(password);
            this.worker_name = new SimpleStringProperty(worker_name);
            this.national_id = new SimpleStringProperty(national_id);
            this.age = new SimpleStringProperty(age);
            this.phone_number = new SimpleStringProperty(phone_number);
            this.address = new SimpleStringProperty(address);
            this.worker_type = new SimpleStringProperty(worker_type);
            this.commission = new SimpleStringProperty(commission);
            this.salary = new SimpleStringProperty(salary);
            this.balance = new SimpleStringProperty(balance);
            this.withdraw = new SimpleStringProperty(withdraw);
        }


        public String getUser_name() {
            return user_name.get();
        }

        public SimpleStringProperty user_nameProperty() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name.set(user_name);
        }

        public String getPassword() {
            return password.get();
        }

        public SimpleStringProperty passwordProperty() {
            return password;
        }

        public void setPassword(String password) {
            this.password.set(password);
        }

        public String getWorker_name() {
            return worker_name.get();
        }

        public SimpleStringProperty worker_nameProperty() {
            return worker_name;
        }

        public void setWorker_name(String worker_name) {
            this.worker_name.set(worker_name);
        }

        public String getNational_id() {
            return national_id.get();
        }

        public SimpleStringProperty national_idProperty() {
            return national_id;
        }

        public void setNational_id(String national_id) {
            this.national_id.set(national_id);
        }

        public String getAge() {
            return age.get();
        }

        public SimpleStringProperty ageProperty() {
            return age;
        }

        public void setAge(String age) {
            this.age.set(age);
        }

        public String getPhone_number() {
            return phone_number.get();
        }

        public SimpleStringProperty phone_numberProperty() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number.set(phone_number);
        }

        public String getAddress() {
            return address.get();
        }

        public SimpleStringProperty addressProperty() {
            return address;
        }

        public void setAddress(String address) {
            this.address.set(address);
        }

        public String getWorker_type() {
            return worker_type.get();
        }

        public SimpleStringProperty worker_typeProperty() {
            return worker_type;
        }

        public void setWorker_type(String worker_type) {
            this.worker_type.set(worker_type);
        }

        public String getCommission() {
            return commission.get();
        }

        public SimpleStringProperty commissionProperty() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission.set(commission);
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

        public String getBalance() {
            return balance.get();
        }

        public SimpleStringProperty balanceProperty() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance.set(balance);
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
    }

    public void loadTheEmployeesComboBox(){
        chooseEmployeeComboBox.getItems().clear();

        try {
            OpenConnection();
            PreparedStatement stmt = connection.
                    prepareStatement("SELECT * FROM users_info");
            ArrayList<Employee> names = new ArrayList<>();
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                names.add(new Employee(resultSet.getString("user_name"),
                        resultSet.getString("password"),
                        resultSet.getString("worker_name"),
                        resultSet.getString("national_id"),
                        Integer.parseInt(resultSet.getString("age")),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address"),
                        resultSet.getString("worker_type"),
                        resultSet.getString("commission"),
                        Double.parseDouble(resultSet.getString("salary")),
                        Double.parseDouble(resultSet.getString("balance")),
                        Double.parseDouble(resultSet.getString("withdraw"))));
            }
            chooseEmployeeComboBox.getItems().addAll(names);

            chooseEmployeeComboBox.setConverter(new StringConverter<MainScreen.Employee>() {
                @Override
                public String toString(MainScreen.Employee object) {
                    return object.getWorker_name();
                }

                @Override
                public MainScreen.Employee fromString(String string) {
                    return null;
                }
            });

            chooseEmployeeComboBox.valueProperty().addListener((obs, oldVal, newVal) ->{
                // load the data with the new object info
                if(newVal!= null){
                    employeeUsernameEditText.setText(newVal.getUser_name());
                    employeePasswordEditText.setText(newVal.getPassword());
                    employeeFullNameEditText.setText(newVal.getWorker_name());
                    employeeIdEditText.setText(newVal.getNational_id());
                    employeeAgeEditText.setText(String.valueOf(newVal.getAge()));
                    employeePhoneEditText.setText(newVal.getPhone_number());
                    employeeAddressEditText.setText(newVal.getAddress());
                    employeeSalaryEditText.setText(String.valueOf(newVal.getSalary()));

                    if(newVal.getCommission().equals("Y")){
                        employeeCommissionEditRB.setSelected(true);
                    }else {
                        employeeNoCommissionEditRB.setSelected(true);
                    }

                    if(newVal.getWorker_type().equals("M")){
                        employeeManagerEditRB.setSelected(true);
                    }else {
                        employeeEmployeeEditRB.setSelected(true);
                    }
                }
            });

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }


    }

    public boolean editEmployee(){
        return (!employeeUsernameEditText.getText().trim().isEmpty()&&
                !employeePasswordEditText.getText().trim().isEmpty()&&
                !employeeFullNameEditText.getText().trim().isEmpty()&&
                !employeeIdEditText.getText().trim().isEmpty()&&
                !employeeAgeEditText.getText().trim().isEmpty()&&
                !employeePhoneEditText.getText().trim().isEmpty()&&
                !employeeAddressEditText.getText().trim().isEmpty()&&
                !employeeSalaryEditText.getText().trim().isEmpty());
    }

    @FXML
    void modifyEmployeeFunction(ActionEvent event) {

        if(editEmployee()){
            try {
                OpenConnection();
                PreparedStatement stmt = connection.
                        prepareStatement("UPDATE users_info SET password = ? , worker_name = ? , national_id = ? ," +
                                "age = ? , phone_number = ? , address = ?, worker_type = ? , commission = ? , salary = ? WHERE user_name = ?");

                String workerType = "E";
                String commission = "N";

                if(employeeManagerEditRB.isSelected()){
                    workerType = "M";
                }

                if(employeeCommissionEditRB.isSelected()){
                    commission = "Y";
                }

                stmt.setString(1,employeePasswordEditText.getText().trim());
                stmt.setString(2,employeeFullNameEditText.getText().trim());
                stmt.setString(3,employeeIdEditText.getText().trim());
                stmt.setString(4,employeeAgeEditText.getText().trim());
                stmt.setString(5,employeePhoneEditText.getText().trim());
                stmt.setString(6,employeeAddressEditText.getText().trim());
                stmt.setString(7,workerType);
                stmt.setString(8,commission);
                stmt.setString(9,employeeSalaryEditText.getText().trim());
                stmt.setString(10,employeeUsernameEditText.getText().trim());

                stmt.executeUpdate();
                employeePasswordEditText.setText("");
                employeeFullNameEditText.setText("");
                employeeIdEditText.setText("");
                employeeAgeEditText.setText("");
                employeePhoneEditText.setText("");
                employeeAddressEditText.setText("");
                employeeSalaryEditText.setText("");
                employeeUsernameEditText.setText("");
                chooseEmployeeComboBox.valueProperty().set(null);

                employeeManagerEditRB.setSelected(false);
                employeeEmployeeEditRB.setSelected(false);

                employeeCommissionEditRB.setSelected(false);
                employeeNoCommissionEditRB.setSelected(false);


                CloseConnection();
                loadTheEmployeesComboBox();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
                // show alert tell the user to use another username
                showAlert("حدث خطأ !","حدث خطأ في التسجيل , حاول مره اخري !","تواصل مع الدعم الفني !!! ");
            }
        }


    }

    // third tab


    String currentEmpName = "NA" ;

    class EmployeeMoneyTransaction{

        private String username ;

        public EmployeeMoneyTransaction(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }

    public void loadTheEmployeeMoneyTransactionComboBox(){

        chooseEmployeeForMoneyComboBox.getItems().clear();

        try {
            OpenConnection();
            PreparedStatement stmt = connection.
                    prepareStatement("SELECT user_name FROM users_info");
            ArrayList<EmployeeMoneyTransaction> names = new ArrayList<>();
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                names.add(new EmployeeMoneyTransaction(resultSet.getString("user_name")));
            }
            CloseConnection();
            chooseEmployeeForMoneyComboBox.getItems().addAll(names);

            chooseEmployeeForMoneyComboBox.setConverter(new StringConverter<EmployeeMoneyTransaction>() {
                @Override
                public String toString(MainScreen.EmployeeMoneyTransaction object) {
                    return object.getUsername();
                }

                @Override
                public MainScreen.EmployeeMoneyTransaction fromString(String string) {
                    return null;
                }
            });

            chooseEmployeeForMoneyComboBox.valueProperty().addListener((obs, oldVal, newVal) ->{
                // load the data with the new object info
                if(newVal!= null){
                    currentEmpName = newVal.getUsername();
                }else {
                    currentEmpName = "NA";
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }



    }

    public void manageEmployeeTransactions(String columnName){
        if(!currentEmpName.equals("NA") && !moneyToWithdrawText.getText().trim().isEmpty()
                && Double.parseDouble(moneyToWithdrawText.getText().trim()) > 0){

            try {
                OpenConnection();
                // withdraw = withdraw +
                // balance = balance +
                PreparedStatement stmt = connection.
                        prepareStatement("Update users_info set "+columnName+" ? WHERE user_name = ?");
                stmt.setString(1,moneyToWithdrawText.getText().trim());
                stmt.setString(2,currentEmpName);
                stmt.executeUpdate();
                chooseEmployeeForMoneyComboBox.valueProperty().set(null);
                moneyToWithdrawText.setText("");
                CloseConnection();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
                showAlert("حدث خطأ !","حدث خطأ في التسجيل , حاول مره اخري !","تواصل مع الدعم الفني !!! ");
            }
        }
    }

    @FXML
    void depositFunction(ActionEvent event) {
        manageEmployeeTransactions("balance = balance + ");
    }

    @FXML
    void withdrawFunction(ActionEvent event) {
        manageEmployeeTransactions("withdraw = withdraw + ");
    }


    // forth tab


    public void loadTheEmployeeSalaryComboBox(){

        chooseEmployeeForSalaryComboBox.getItems().clear();

        try {
            OpenConnection();
            PreparedStatement stmt = connection.
                    prepareStatement("SELECT * FROM users_info");
            ArrayList<Employee> names = new ArrayList<>();
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                names.add(new Employee(resultSet.getString("user_name"),
                        resultSet.getString("password"),
                        resultSet.getString("worker_name"),
                        resultSet.getString("national_id"),
                        Integer.parseInt(resultSet.getString("age")),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address"),
                        resultSet.getString("worker_type"),
                        resultSet.getString("commission"),
                        Double.parseDouble(resultSet.getString("salary")),
                        Double.parseDouble(resultSet.getString("balance")),
                        Double.parseDouble(resultSet.getString("withdraw"))));
            }
            chooseEmployeeForSalaryComboBox.getItems().addAll(names);

            chooseEmployeeForSalaryComboBox.setConverter(new StringConverter<MainScreen.Employee>() {
                @Override
                public String toString(MainScreen.Employee object) {
                    return object.getWorker_name();
                }

                @Override
                public MainScreen.Employee fromString(String string) {
                    return null;
                }
            });

            chooseEmployeeForSalaryComboBox.valueProperty().addListener((obs, oldVal, newVal) ->{
                // load the data with the new object info
                if(newVal!= null){
                    employeeUsernameForSalaryText.setText(newVal.getUser_name());
                    employeeNameForSalaryText.setText(newVal.getWorker_name());
                    employeeSalaryForSalaryText.setText(String.valueOf(newVal.getSalary()));
                    employeeBalanceForSalaryText.setText(String.valueOf(newVal.getBalance()));
                    employeeDeptForSalaryText.setText(String.valueOf(newVal.getWithdraw()));
                    employeeTotalForSalaryText.setText(String.valueOf(newVal.getSalary()+newVal.getBalance()-newVal.getWithdraw()
                    ));
                }
            });

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }

    @FXML
    void manageTheSalaryFunction(ActionEvent event) {

        if(Double.parseDouble(employeeTotalForSalaryText.getText().trim()) > 0){
            try{
                OpenConnection();

                // save the record in the
                PreparedStatement stmt = connection.
                        prepareStatement("Insert Into expenses (username, salary, balance, withdraw) VALUES (?,?,?,?)");
                stmt.setString(1,employeeUsernameForSalaryText.getText().trim());
                stmt.setString(2,employeeSalaryForSalaryText.getText().trim());
                stmt.setString(3,employeeBalanceForSalaryText.getText().trim());
                stmt.setString(4,employeeDeptForSalaryText.getText().trim());

                stmt.executeUpdate();

                PreparedStatement updateStatement = connection.
                        prepareStatement("Update users_info SET balance = 0 , withdraw = 0 WHERE user_name = ?");
                updateStatement.setString(1,employeeUsernameForSalaryText.getText().trim());

                updateStatement.executeUpdate();

                CloseConnection();

                showAlert("تمت العمليه بنجاح !","تمت تسويه مستحقات : "+employeeNameForSalaryText.getText().trim() ,"لاجمالي : "+ employeeTotalForSalaryText.getText().trim());

                chooseEmployeeForMoneyComboBox.valueProperty().set(null);

                employeeUsernameForSalaryText.setText("");
                employeeNameForSalaryText.setText("");
                employeeSalaryForSalaryText.setText("");
                employeeBalanceForSalaryText.setText("");
                employeeDeptForSalaryText.setText("");
                employeeTotalForSalaryText.setText("0.0");
                loadTheEmployeeSalaryComboBox();
                loadTheEmployeesComboBox();

            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
                showAlert("حدث خطأ !","حدث خطأ في التسجيل , حاول مره اخري !","تواصل مع الدعم الفني !!! ");
            }

        }
    }


    // fifth tab

    String currentEmployeeSelectedOne = "NA" ;

    public void LoadTheEmployeeInfoTable(String Text , int mode , int searchType){

        try {
            OpenConnection();

            dataForEmployeeTable.clear();

            Statement GetTheInfoRow =  connection.createStatement();

            String Row ;

            if(mode==0){
                Row = "SELECT * FROM users_info ";
            }else {
                if(searchType==0){
                    Row = "SELECT * FROM users_info WHERE user_name LIKE '%"+Text+"%'";
                }else {
                    Row = "SELECT * FROM users_info WHERE users_info.phone_number LIKE '%"+Text+"%'";
                }
            }

            ResultSet resultSet = GetTheInfoRow.executeQuery(Row);
            while (resultSet.next()){
                dataForEmployeeTable.add(new EmployeeX(
                        resultSet.getString("user_name"),
                        resultSet.getString("password"),
                        resultSet.getString("worker_name"),
                        resultSet.getString("national_id"),
                        resultSet.getString("age"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address"),
                        resultSet.getString("worker_type"),
                        resultSet.getString("commission"),
                        resultSet.getString("salary"),
                        resultSet.getString("balance"),
                        resultSet.getString("withdraw")));
            }

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("sss");
            CloseConnection();
        }

    }

    public void setTheEmployeeTable(){

        usernameEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        passwordEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        nameEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("worker_name"));
        nationalIdEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("national_id"));
        ageEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        phoneEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        addressEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        jobTypeEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("worker_type"));
        salaryEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        balanceEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        deptEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("withdraw"));

        employeeTable.setItems(dataForEmployeeTable);

        employeeTable.setPlaceholder(new Label("لا يوجد موظفين بعد !"));

    }

    @FXML
    void searchForEmployeeByTypingUsernameFunction (){
        LoadTheEmployeeInfoTable(searchEmployeeTableByUsernameText.getText().trim(),1,0);
    }

    @FXML
    void searchForEmployeeByTypingPhoneFunction (){
        LoadTheEmployeeInfoTable(searchEmployeeTableByPhoneText.getText().trim(),1,1);
    }


    @FXML
    void deleteTheCurrentEmployeeFunction(ActionEvent event) {
        if(!employeeTable.getSelectionModel().isEmpty()){

            currentEmployeeSelectedOne = employeeTable.getSelectionModel().getSelectedItem().getUser_name();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setHeaderText("طلب تحقق");
            alert.setContentText("هل انت متأكد من حذف الموظف : "+currentEmployeeSelectedOne + "؟");
            alert.setTitle("تأكيد الحذف");

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.OK);
            if (button == ButtonType.OK) {
                System.out.println("Ok pressed");
                try {
                    OpenConnection();
                    PreparedStatement stmt = connection.
                            prepareStatement("Delete FROM users_info WHERE user_name = ?");
                    stmt.setString(1,currentEmployeeSelectedOne);
                    stmt.executeUpdate();
                    employeeTable.getItems().remove(employeeTable.getSelectionModel().getSelectedItem());
                    CloseConnection();
                }catch (Exception e){
                    e.printStackTrace();
                    CloseConnection();
                    currentEmployeeSelectedOne = "NA";
                }
            }
        }
    }

    @FXML
    void showTheCurrentEmployeeFunction(ActionEvent event) {

        if(!employeeTable.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            EmployeeX emp = employeeTable.getSelectionModel().getSelectedItem();
            alert.setHeaderText("معلومات عامه");
            alert.setContentText(
                    "اسم الموظف : "+emp.getWorker_name()+"\n"+
                    "اسم المستخدم : "+emp.getUser_name()+"\n"+
                    "العمر : "+emp.getAge()+"\n"+
                    "العنوان : "+emp.getAddress()+"\n"+
                    "الرقم القومي : "+emp.getNational_id()+"\n"+
                    "المستحقات : "+emp.getBalance()+"\n"+
                    "الديون : "+emp.getWithdraw()+"\n"+
                    "الراتب الشهري : "+emp.getSalary()+"\n"+
                    "المسمي الوظيفي : "+emp.getWorker_type()+"\n"+
                    "يقبل العموله : "+emp.getCommission()+"\n"+
                    "رقم الهاتف : "+emp.getPhone_number()+"\n");
            alert.setTitle("معلومات عن الموظف : "+emp.getWorker_name());
            alert.show();
        }

    }


    // manage customers tab

    @FXML
    private TabPane customerTabPane;

    @FXML
    private JFXTextField customerPhoneText;

    @FXML
    private JFXTextField customerAddressText;

    @FXML
    private JFXTextField customerNameText;

    @FXML
    private JFXTextField customerBarcodeText;

    @FXML
    private DatePicker customerBDText;

    @FXML
    private JFXTextField customerSearchByBarcodeText;

    @FXML
    private JFXTextField editCustomerPhoneText;

    @FXML
    private JFXTextField editCustomerAddressText;

    @FXML
    private JFXTextField editCustomerNameText;

    @FXML
    private DatePicker editCustomerBDText;

    @FXML
    private JFXTextField searchCustomersTableByPhoneText;

    @FXML
    private JFXTextField searchCustomerTableByUsernameText;

    @FXML
    private TableView<Customer> customersTable;

    private ObservableList<Customer> dataForCustomerTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Customer,String> customerBarcodeColumn;

    @FXML
    private TableColumn<Customer,String> customerNameColumn;

    @FXML
    private TableColumn<Customer,String> customerBDColumn;

    @FXML
    private TableColumn<Customer,String> customerPhoneColumn;

    @FXML
    private TableColumn<Customer,String> customerAddressColumn;

    @FXML
    private TableColumn<Customer,String> customerPointsColumn;


    @FXML
    void addCustomerFunction(ActionEvent event) {

        if(!customerNameText.getText().trim().isEmpty()&&
                !customerPhoneText.getText().trim().isEmpty()&&
                !customerBarcodeText.getText().trim().isEmpty()&&
                !customerAddressText.getText().trim().isEmpty()&&
                !customerBDText.getEditor().getText().trim().isEmpty()){
            try {
                OpenConnection();
                PreparedStatement stmt = connection.
                        prepareStatement("insert into " +
                                "customers_info (name,phone_number,barcode,address,birth) VALUES (?,?,?,?,?)");
                stmt.setString(1,customerNameText.getText().trim());
                stmt.setString(2,customerPhoneText.getText().trim());
                stmt.setString(3,customerBarcodeText.getText().trim());
                stmt.setString(4,customerAddressText.getText().trim());
                stmt.setString(5,customerBDText.getEditor().getText().trim());
                stmt.executeUpdate();
                customerNameText.setText("");
                customerPhoneText.setText("");
                customerBarcodeText.setText("");
                customerAddressText.setText("");
                customerBDText.getEditor().setText("");
                CloseConnection();

            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
                // show alert tell the user to use another username
                showAlert("حدث خطأ !","حدث خطأ في التسجيل , حاول مره اخري !","ربما يكون الباركود موجود بالفعل\n غير الباركود و حاول مره اخري ! ");
            }
        }

    }


    String currentCustomerSelectedOne = "NA" ;
    String currentCustomerToEdit = "NA" ;


    public void LoadTheCustomersInfoTable(String Text , int mode , int searchType){

        try {
            OpenConnection();

            dataForCustomerTable.clear();

            Statement GetTheInfoRow =  connection.createStatement();

            String Row ;

            if(mode==0){
                Row = "SELECT * FROM customers_info ";
            }else {
                if(searchType==0){
                    Row = "SELECT * FROM customers_info WHERE customers_info.barcode LIKE '%"+Text+"%' OR customers_info.name LIKE '%"+Text+"%'";
                }else {
                    Row = "SELECT * FROM customers_info WHERE customers_info.phone_number LIKE '%"+Text+"%'";
                }
            }

            ResultSet resultSet = GetTheInfoRow.executeQuery(Row);
            while (resultSet.next()){
                dataForCustomerTable.add(
                        new Customer(
                                resultSet.getString("barcode"),
                                resultSet.getString("name"),
                                resultSet.getString("birth"),
                                resultSet.getString("phone_number"),
                                resultSet.getString("address"),
                                resultSet.getString("points"))
                );
            }

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }

    public void setTheCustomersTable(){
        customerBarcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerBDColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        customersTable.setItems(dataForCustomerTable);
        customersTable.setPlaceholder(new Label("لا يوجد عملاء بعد !"));
    }

    public static class Customer{
        private final SimpleStringProperty barcode ;
        private final SimpleStringProperty name ;
        private final SimpleStringProperty birthDate ;
        private final SimpleStringProperty phone ;
        private final SimpleStringProperty address ;
        private final SimpleStringProperty points ;

        public Customer(String barcode, String name, String birthDate, String phone, String address ,String points) {
            this.barcode = new SimpleStringProperty(barcode);
            this.name = new SimpleStringProperty(name);
            this.birthDate = new SimpleStringProperty(birthDate);
            this.phone = new SimpleStringProperty(phone);
            this.address = new SimpleStringProperty(address);
            this.points = new SimpleStringProperty(points);
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

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getBirthDate() {
            return birthDate.get();
        }

        public SimpleStringProperty birthDateProperty() {
            return birthDate;
        }

        public void setBirthDate(String birthDate) {
            this.birthDate.set(birthDate);
        }

        public String getPhone() {
            return phone.get();
        }

        public SimpleStringProperty phoneProperty() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone.set(phone);
        }

        public String getAddress() {
            return address.get();
        }

        public SimpleStringProperty addressProperty() {
            return address;
        }

        public void setAddress(String address) {
            this.address.set(address);
        }

        public String getPoints() {
            return points.get();
        }

        public SimpleStringProperty pointsProperty() {
            return points;
        }

        public void setPoints(String points) {
            this.points.set(points);
        }
    }

    @FXML
    void deleteTheCurrentCustomerFunction(ActionEvent event) {
        if(!customersTable.getSelectionModel().isEmpty()){

            currentCustomerSelectedOne = customersTable.getSelectionModel().getSelectedItem().getBarcode();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setHeaderText("طلب تحقق");
            alert.setContentText("هل انت متأكد من حذف العميل : "+currentCustomerSelectedOne + "؟");
            alert.setTitle("تأكيد الحذف");

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.OK);
            if (button == ButtonType.OK) {
                try {
                    OpenConnection();
                    PreparedStatement stmt = connection.
                            prepareStatement("Delete FROM customers_info WHERE barcode = ?");
                    stmt.setString(1,currentCustomerSelectedOne);
                    stmt.executeUpdate();
                    customersTable.getItems().remove(customersTable.getSelectionModel().getSelectedItem());
                    CloseConnection();
                }catch (Exception e){
                    e.printStackTrace();
                    CloseConnection();
                    currentCustomerSelectedOne = "NA";
                }
            }
        }
    }

    @FXML
    void modifyCustomerFunction(ActionEvent event) {
        if(!currentCustomerToEdit.equals("NA") && !editCustomerNameText.getText().trim().isEmpty()
                && !editCustomerPhoneText.getText().trim().isEmpty()){
            try {
                OpenConnection();
                PreparedStatement stmt = connection.
                        prepareStatement("UPDATE customers_info SET name = ? , phone_number = ? , address = ? , birth = ? " +
                                "WHERE barcode = ?");
                stmt.setString(1,editCustomerNameText.getText().trim());
                stmt.setString(2,editCustomerPhoneText.getText().trim());
                stmt.setString(3,editCustomerAddressText.getText().trim());
                stmt.setString(4,editCustomerBDText.getEditor().getText().trim());
                stmt.setString(5,currentCustomerToEdit);
                stmt.executeUpdate();
                currentCustomerToEdit = "NA";
                editCustomerNameText.setText("");
                editCustomerPhoneText.setText("");
                editCustomerAddressText.setText("");
                editCustomerBDText.getEditor().setText("");
                customerSearchByBarcodeText.setText("");
                CloseConnection();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }
        }
    }

    @FXML
    void showTheCurrentCustomersFunction(ActionEvent event) {
        if(!customersTable.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Customer customer = customersTable.getSelectionModel().getSelectedItem();
            alert.setHeaderText("معلومات عامه");
            alert.setContentText(
                    "الباركود : "+customer.getBarcode()+"\n"+
                            "اسم العميل : "+customer.getName()+"\n"+
                            "رقم الهاتف : "+customer.getPhone()+"\n"+
                            "تاريخ الميلاد : "+customer.getBirthDate()+"\n"+
                            "العنوان : "+customer.getAddress()+"\n"+
                            "النقاط : "+customer.getPoints()+"\n");
            alert.setTitle("معلومات عن العميل : "+customer.getName());
            alert.show();
        }
    }

    @FXML
    void searchCustomersByNameFunction(KeyEvent event) {
        LoadTheCustomersInfoTable(searchCustomerTableByUsernameText.getText().trim(),1,0);
    }

    @FXML
    void searchCustomersByPhoneFunction(KeyEvent event) {
        LoadTheCustomersInfoTable(searchCustomersTableByPhoneText.getText().trim(),1,1);
    }

    @FXML
    void getTheWantedCustomerForEditFunction(KeyEvent event) {
        try {
            OpenConnection();
            PreparedStatement stmt = connection.
                    prepareStatement("SELECT * FROM customers_info WHERE barcode = ?");
            stmt.setString(1,customerSearchByBarcodeText.getText().trim());
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                editCustomerNameText.setText(resultSet.getString("name"));
                editCustomerPhoneText.setText(resultSet.getString("phone_number"));
                editCustomerBDText.getEditor().setText(resultSet.getString("birth"));
                editCustomerAddressText.setText(resultSet.getString("address"));
                currentCustomerToEdit = resultSet.getString("barcode");
            }else {
                currentCustomerToEdit = "NA";
                editCustomerNameText.setText("");
                editCustomerPhoneText.setText("");
                editCustomerAddressText.setText("");
                editCustomerBDText.getEditor().setText("");
            }
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }


    // manage the items tab

    @FXML
    private TabPane itemsPane;

    @FXML
    private TabPane mainTab;

    @FXML
    private JFXTextField itemNameText;

    @FXML
    private JFXTextField publicPriceText;

    @FXML
    private JFXTextField availableText;

    @FXML
    private JFXTextField itemBarcodeText;

    @FXML
    private JFXTextField originalPriceText;

    @FXML
    private JFXRadioButton goodsItemRB;

    @FXML
    private JFXTextField itemEmployeePercentageText;


    @FXML
    private JFXTextField itemSearchByBarcodeText;

    @FXML
    private JFXTextField itemPriceEditText;

    @FXML
    private JFXRadioButton itemServiceEditRB;

    @FXML
    private JFXRadioButton goodsItemEditRB;

    @FXML
    private JFXTextField itemNameEditText;

    @FXML
    private JFXTextField itemPercentageEditText;

    @FXML
    private Tab allCustomersInfoTab1;

    @FXML
    private JFXTextField itemSearchByTypeText;

    @FXML
    private JFXTextField itemSearchByBarcodeForUpdateText;

    @FXML
    private TableView<Item> itemsTable;

    private ObservableList<Item> dataForItemTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Item,String> itemBarcodeColumn;

    @FXML
    private TableColumn<Item,String> itemNameColumn;

    @FXML
    private TableColumn<Item,String> itemOriginalPriceColumn;

    @FXML
    private TableColumn<Item,String> itemPublicPriceColumn;

    @FXML
    private TableColumn<Item,String> itemTypeColumn;

    @FXML
    private TableColumn<Item,String> itemPercentageColumn;

    @FXML
    private TableColumn<Item,String> itemAvailableColumn;

    @FXML
    private JFXRadioButton goodsSearchRB;

    @FXML
    private JFXRadioButton itemServiceRB;

    @FXML
    private JFXRadioButton serviceSearchRB;

    String currentItemSelectedOne = "NA" ;

    public static class Item{
        private final SimpleStringProperty barcode ;
        private final SimpleStringProperty name ;
        private final SimpleStringProperty originalPrice ;
        private final SimpleStringProperty publicPrice ;
        private final SimpleStringProperty available ;
        private final SimpleStringProperty percentage ;
        private final SimpleStringProperty type ;

        public Item(String barcode, String name, String originalPrice, String publicPrice, String available ,String percentage , String type) {
            this.barcode = new SimpleStringProperty(barcode);
            this.name = new SimpleStringProperty(name);
            this.originalPrice = new SimpleStringProperty(originalPrice);
            this.publicPrice = new SimpleStringProperty(publicPrice);
            this.available = new SimpleStringProperty(available);
            this.percentage = new SimpleStringProperty(percentage);
            this.type = new SimpleStringProperty(type);
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

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getOriginalPrice() {
            return originalPrice.get();
        }

        public SimpleStringProperty originalPriceProperty() {
            return originalPrice;
        }

        public void setOriginalPrice(String originalPrice) {
            this.originalPrice.set(originalPrice);
        }

        public String getPublicPrice() {
            return publicPrice.get();
        }

        public SimpleStringProperty publicPriceProperty() {
            return publicPrice;
        }

        public void setPublicPrice(String publicPrice) {
            this.publicPrice.set(publicPrice);
        }

        public String getAvailable() {
            return available.get();
        }

        public SimpleStringProperty availableProperty() {
            return available;
        }

        public void setAvailable(String available) {
            this.available.set(available);
        }

        public String getPercentage() {
            return percentage.get();
        }

        public SimpleStringProperty percentageProperty() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage.set(percentage);
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
    }

    @FXML
    void addItemFunction(ActionEvent event) {
        if(!itemBarcodeText.getText().trim().isEmpty()&&
                !itemNameText.getText().trim().isEmpty()&&
                !originalPriceText.getText().trim().isEmpty()&&
                !publicPriceText.getText().trim().isEmpty()&&
                !availableText.getText().trim().isEmpty()&&
                !itemEmployeePercentageText.getText().trim().isEmpty()){
            try {
                OpenConnection();
                PreparedStatement stmt = connection.
                        prepareStatement("insert into " +
                                "items_info (name, barcode, original_price, public_price, available, precentage, type) VALUES (?,?,?,?,?,?,?)");
                String type = "S";
                if(goodsItemRB.isSelected()){
                    type = "G";
                }
                stmt.setString(1,itemNameText.getText().trim());
                stmt.setString(2,itemBarcodeText.getText().trim());
                stmt.setString(3,originalPriceText.getText().trim());
                stmt.setString(4,publicPriceText.getText().trim());
                stmt.setString(5,availableText.getText().trim());
                stmt.setString(6,itemEmployeePercentageText.getText().trim());
                stmt.setString(7,type);
                stmt.executeUpdate();
                itemNameText.setText("");
                itemBarcodeText.setText("");
                originalPriceText.setText("");
                publicPriceText.setText("");
                availableText.setText("");
                itemEmployeePercentageText.setText("");

                goodsItemRB.setSelected(false);
                itemServiceRB.setSelected(false);

                CloseConnection();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
                // show alert tell the user to use another username
                showAlert("حدث خطأ !","حدث خطأ في التسجيل , حاول مره اخري !","ربما يكون الباركود موجود بالفعل\n غير الباركود و حاول مره اخري ! ");
            }
        }
    }

    public void LoadTheItemsInfoTable(String Text , int mode , int searchType){
        try {
            OpenConnection();

            dataForItemTable.clear();

            Statement GetTheInfoRow =  connection.createStatement();

            String Row ;

            if(mode==0){
                Row = "SELECT * FROM items_info ";
            }else {
                if(searchType==0){
                    Row = "SELECT * FROM items_info WHERE items_info.barcode LIKE '%"+Text+"%'";
                }else {
                    Row = "SELECT * FROM items_info WHERE items_info.type LIKE '%"+Text+"%'";
                }
            }
            ResultSet resultSet = GetTheInfoRow.executeQuery(Row);
            while (resultSet.next()){

                String type = "خدمه" ;
                if(resultSet.getString("type").equals("G")){
                    type = "سلعه" ;
                }
                dataForItemTable.add(
                        new Item(resultSet.getString("barcode"),
                                resultSet.getString("name"),
                                resultSet.getString("original_price"),
                                resultSet.getString("public_price"),
                                resultSet.getString("available"),
                                resultSet.getString("precentage"),
                                type)
                );
            }
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }

    public void setTheItemsTable(){
        itemBarcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemOriginalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("originalPrice"));
        itemPublicPriceColumn.setCellValueFactory(new PropertyValueFactory<>("publicPrice"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        itemPercentageColumn.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        itemAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        itemsTable.setItems(dataForItemTable);
        itemsTable.setPlaceholder(new Label("لا يوجد عناصر بعد !"));
    }

    @FXML
    void searchItemsByBarcodeFunction(KeyEvent event) {
        goodsSearchRB.setSelected(false);
        serviceSearchRB.setSelected(false);
        LoadTheItemsInfoTable(itemSearchByBarcodeText.getText().trim(),1,0);
    }

    @FXML
    void searchItemsByTypeFunction(ActionEvent event) {
        itemSearchByBarcodeText.setText("");
        if(goodsSearchRB.isSelected()){
            LoadTheItemsInfoTable("G",1,1);
        }else if(serviceSearchRB.isSelected()){
            LoadTheItemsInfoTable("S",1,1);
        }
    }

    @FXML
    void modifyItemFunction(ActionEvent event) {
        if(!itemNameEditText.getText().trim().isEmpty()
                &&!itemPriceEditText.getText().trim().isEmpty()
                &&!itemPercentageEditText.getText().trim().isEmpty()){
            try {
                String type ;
                if(itemServiceEditRB.isSelected()){
                    type = "S" ;
                }else {
                    type = "G" ;
                }

                OpenConnection();
                PreparedStatement stmt = connection.
                        prepareStatement("UPDATE items_info SET name = ? , public_price = ? , precentage = ? , type = ? WHERE barcode = ?");

                stmt.setString(1,itemNameEditText.getText().trim());
                stmt.setString(2,itemPriceEditText.getText().trim());
                stmt.setString(3,itemPercentageEditText.getText().trim());
                stmt.setString(4,type);
                stmt.setString(5,itemSearchByBarcodeForUpdateText.getText().trim());

                stmt.executeUpdate();
                itemNameEditText.setText("");
                itemPriceEditText.setText("");
                itemPercentageEditText.setText("");
                itemSearchByBarcodeForUpdateText.setText("");
                goodsItemEditRB.setSelected(false);
                itemServiceEditRB.setSelected(false);
                CloseConnection();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }
        }
    }

    @FXML
    void searchForCustomersFunction() {
        try {
            System.out.println("we are here");
            System.out.println(itemSearchByBarcodeForUpdateText.getText().trim());
            OpenConnection();
            PreparedStatement stmt = connection.
                    prepareStatement("SELECT * FROM items_info WHERE barcode = ?");

            stmt.setString(1,itemSearchByBarcodeForUpdateText.getText().trim());
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                itemNameEditText.setText(resultSet.getString("name"));
                itemPriceEditText.setText(resultSet.getString("public_price"));
                itemPercentageEditText.setText(resultSet.getString("precentage"));
                if(resultSet.getString("type").equals("S")){
                    itemServiceEditRB.setSelected(true);
                }else {
                    goodsItemEditRB.setSelected(true);
                }
            }else {
                itemNameEditText.setText("");
                itemPriceEditText.setText("");
                itemPercentageEditText.setText("");
                goodsItemEditRB.setSelected(false);
                itemServiceEditRB.setSelected(false);
            }
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }

    @FXML
    void showTheCurrentItemFunction(ActionEvent event) {
        if(!itemsTable.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Item item = itemsTable.getSelectionModel().getSelectedItem();
            alert.setHeaderText("معلومات عامه");
            String type = "خدمه";
            if(item.getType().equals("G")){
                type = "سلعه";
            }
            alert.setContentText(
                    "الباركود : "+item.getBarcode()+"\n"+
                            "اسم العنصر : "+item.getName()+"\n"+
                            "سعر الجمهور : "+item.getPublicPrice()+"\n"+
                            "السعر الاصلي : "+item.getOriginalPrice()+"\n"+
                            "المتوفر : "+item.getAvailable()+"\n"+
                            "نوع العنصر : "+type+"\n"+
                            "نسبه الموظف : "+item.getPercentage()+"\n");
            alert.setTitle("معلومات عن العنصر : "+item.getName());
            alert.show();
        }
    }

    @FXML
    void updateTheItemFunction(ActionEvent event) {
        if(!itemsTable.getSelectionModel().isEmpty()){
            try {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("UpdateTheItemScreen.fxml"));
                System.out.println(loader);
                Parent root = loader.load();
                // send data
                UpdateTheItemScreen controller = loader.<UpdateTheItemScreen>getController();
                controller.setBarcode(itemsTable.getSelectionModel().getSelectedItem().getBarcode());
                Scene UpdateTheItemScreen = new Scene(root);

                // how to set the style sheets
                URL url = HelloApplication.class.getResource("application.css");
                if (url == null) {
                    System.out.println("Resource not found. Aborting.");
                    System.exit(-1);
                }
                String css = url.toExternalForm();
                UpdateTheItemScreen.getStylesheets().add(css);


                //loader.<UpdateTheItemScreen>getController().setBarcode("i won");
                HelloApplication.getAnnouncementStage().setTitle("تحديث العنصر");
                HelloApplication.getAnnouncementStage().setScene(UpdateTheItemScreen);
                //HelloApplication.announcementStage.setResizable(false);
                HelloApplication.getAnnouncementStage().show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    void deleteTheCurrentItemFunction(ActionEvent event) {
        if(!itemsTable.getSelectionModel().isEmpty()){

            currentItemSelectedOne = itemsTable.getSelectionModel().getSelectedItem().getBarcode();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setHeaderText("طلب تحقق");
            alert.setContentText("هل انت متأكد من حذف العنصر : "+currentItemSelectedOne + "؟");
            alert.setTitle("تأكيد الحذف");

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.OK);
            if (button == ButtonType.OK) {
                try {
                    OpenConnection();
                    PreparedStatement stmt = connection.
                            prepareStatement("Delete FROM items_info WHERE barcode = ?");
                    stmt.setString(1,currentItemSelectedOne);
                    stmt.executeUpdate();
                    itemsTable.getItems().remove(itemsTable.getSelectionModel().getSelectedItem());
                    CloseConnection();
                }catch (Exception e){
                    e.printStackTrace();
                    CloseConnection();
                    currentItemSelectedOne = "NA";
                }
            }
        }
    }

    @FXML
    void ShowTheItemHistoryFunction(ActionEvent event) {
        if(!itemsTable.getSelectionModel().isEmpty()){
            try {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("ItemHistoryScreen.fxml"));
                Parent root = loader.load();
                // send data
                ItemHistoryScreen controller = loader.<ItemHistoryScreen>getController();
                controller.setBarcode(itemsTable.getSelectionModel().getSelectedItem().getBarcode());
                Scene ItemHistoryScreen = new Scene(root);

                // how to set the style sheets
                URL url = HelloApplication.class.getResource("application.css");
                if (url == null) {
                    System.out.println("Resource not found. Aborting.");
                    System.exit(-1);
                }
                String css = url.toExternalForm();
                ItemHistoryScreen.getStylesheets().add(css);


                HelloApplication.getAnnouncementStage().setTitle("سجل العنصر");
                HelloApplication.getAnnouncementStage().setScene(ItemHistoryScreen);
                HelloApplication.getAnnouncementStage().show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    // manage the sales screen

    @FXML
    private JFXTextField salesCustomerBarcodeText;

    @FXML
    private JFXTextField salesCustomerNameText;

    @FXML
    private JFXTextField salesCustomerPointsText;

    @FXML
    private JFXTextField salesItemBarcodeText;

    @FXML
    private JFXTextField totalText;

    public static class SalesItem{
        private final SimpleStringProperty barcode ;
        private final SimpleStringProperty name ;
        private final SimpleStringProperty price ;
        private final SimpleStringProperty type ;
        private final SimpleStringProperty commission ;


        public SalesItem(String barcode, String name, String price, String type , String commission) {
            this.barcode = new SimpleStringProperty(barcode);
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleStringProperty(price);
            this.type = new SimpleStringProperty(type);
            this.commission = new SimpleStringProperty(commission);
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

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
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

        public String getType() {
            return type.get();
        }

        public SimpleStringProperty typeProperty() {
            return type;
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public String getCommission() {
            return commission.get();
        }

        public SimpleStringProperty commissionProperty() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission.set(commission);
        }
    }

    @FXML
    private TableView<SalesItem> salesTable;

    private ObservableList<SalesItem> dataForSalesTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<SalesItem,String> barcodeSalesColumn;

    @FXML
    private TableColumn<SalesItem,String> nameSalesColumn;

    @FXML
    private TableColumn<SalesItem,String> priceSalesColumn;

    @FXML
    private TableColumn<SalesItem,String> typeSalesColumn;

    @FXML
    private JFXComboBox<Employee> employeeSalesComboBox;

    @FXML
    private JFXTextField paidMoneyText;

    @FXML
    private JFXTextField returnedMoneyText;

    Employee employeeUserName = null ;

    public void loadTheEmployeesComboBoxForSalesScreen(){
        employeeSalesComboBox.getItems().clear();

        try {
            OpenConnection();
            PreparedStatement stmt = connection.
                    prepareStatement("SELECT * FROM users_info");
            ArrayList<Employee> names = new ArrayList<>();
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                names.add(new Employee(resultSet.getString("user_name"),
                        resultSet.getString("password"),
                        resultSet.getString("worker_name"),
                        resultSet.getString("national_id"),
                        Integer.parseInt(resultSet.getString("age")),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address"),
                        resultSet.getString("worker_type"),
                        resultSet.getString("commission"),
                        Double.parseDouble(resultSet.getString("salary")),
                        Double.parseDouble(resultSet.getString("balance")),
                        Double.parseDouble(resultSet.getString("withdraw"))));
            }
            employeeSalesComboBox.getItems().addAll(names);

            employeeSalesComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(MainScreen.Employee object) {
                    return object.getWorker_name();
                }

                @Override
                public MainScreen.Employee fromString(String string) {
                    return null;
                }
            });

            employeeSalesComboBox.valueProperty().addListener((obs, oldVal, newVal) ->{
                // load the data with the new object info
                // what to do when we choose the employee
                employeeUserName = newVal;
            });

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }


    }

    public void setTheSalesTable(){
        barcodeSalesColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        nameSalesColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceSalesColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        typeSalesColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        salesTable.setItems(dataForSalesTable);
        salesTable.setPlaceholder(new Label("لا يوجد عناصر بعد !"));
    }

    @FXML
    void setTheCustomersInfoFunction(){
        System.out.println("setTheCustomersInfoFunction");
        try {
            OpenConnection();
            PreparedStatement stmt = connection.
                    prepareStatement("SELECT * FROM customers_info WHERE barcode = ?");

            stmt.setString(1,salesCustomerBarcodeText.getText().trim());
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                salesCustomerNameText.setText(resultSet.getString("name"));
                salesCustomerPointsText.setText(resultSet.getString("points"));
            }else {
                salesCustomerNameText.setText("");
                salesCustomerPointsText.setText("");
            }
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }

    @FXML
    void setTheItemInfoFunction(){
        System.out.println("setTheItemInfoFunction");
        try {
            OpenConnection();
            PreparedStatement stmt = connection.
                    prepareStatement("SELECT * FROM items_info WHERE barcode = ?");

            stmt.setString(1,salesItemBarcodeText.getText().trim());
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                dataForSalesTable.add(new SalesItem(
                        resultSet.getString("barcode")
                        ,resultSet.getString("name")
                        ,resultSet.getString("public_price")
                        ,resultSet.getString("type")
                        ,resultSet.getString("precentage")
                ));
                salesItemBarcodeText.setText("");
                updateTheTotalFunction();
            }
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }

    void updateTheTotalFunction(){
        // loop through table and calculate the prices
        double total = 0.0 ;
        for(SalesItem row : salesTable.getItems()){
            total = total + Double.parseDouble(row.getPrice());
        }
        totalText.setText(String.valueOf(total));
        calculateTheReturnedMoneyFunction();
    }

    @FXML
    void calculateTheReturnedMoneyFunction(){
        if(!totalText.getText().trim().isEmpty()){
            double returnedMoney = 0 ;
            if(!paidMoneyText.getText().trim().equals("")){
                returnedMoney = Double.parseDouble(paidMoneyText.getText().trim()) - Double.parseDouble(totalText.getText().trim()) ;
            }
            returnedMoneyText.setText(String.valueOf(returnedMoney));
        }else {
            returnedMoneyText.setText("");
        }
    }

    @FXML
    void removeSalesItemFunction(){
        System.out.println("removeSalesItemFunction");
        if(!salesTable.getSelectionModel().isEmpty()){
            SalesItem salesItem = salesTable.getSelectionModel().getSelectedItem();
            salesTable.getItems().remove(salesItem);
            updateTheTotalFunction();
            System.out.println(dataForSalesTable);
        }
    }

    @FXML
    void removeAllSalesItemsFunctions(){
        System.out.println("removeAllSalesItemsFunctions");
        dataForSalesTable.clear();
        updateTheTotalFunction();
    }


    private void PrintReportToPrinter(JasperPrint jp) {

        try {
            // TODO Auto-generated method stub
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            // printRequestAttributeSet.add(MediaSizeName.ISO_A4); //setting page size
            printRequestAttributeSet.add(new Copies(1));

            PrinterName printerName = new PrinterName("XP-C80", null); //gets printer

            PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
            printServiceAttributeSet.add(printerName);

            JRPrintServiceExporter exporter = new JRPrintServiceExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            exporter.exportReport();
        }catch (Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Error");
            alert.setResizable(true);
            alert.setContentText(e.toString());
            alert.setTitle("warning");
            alert.show();

        }

    }

    @FXML
    void finishTheSaleFunction(){

        // todo ---> implement the bill with jasper reports .
        List<Map<String,?>> dataSource = new ArrayList();
        HashMap parameter = new HashMap();

        System.out.println("finishTheSaleFunction");
        if(!totalText.getText().trim().isEmpty()){
            if(Double.parseDouble(totalText.getText().trim()) != 0
                    && employeeUserName != null
                    && !salesCustomerBarcodeText.getText().trim().isEmpty()
                    && !salesCustomerNameText.getText().trim().isEmpty()){

                try {
                    OpenConnection();
                    // set the bill info
                    PreparedStatement stmt = connection.prepareStatement("INSERT INTO bills " +
                            "(customer_barcode, cashier_user_name, worker_user_name, cash) " +
                            "VALUES (?,?,?,?)");
                    stmt.setString(1,salesCustomerBarcodeText.getText().trim());
                    stmt.setString(2,LoginScreen.getTheUsername());
                    stmt.setString(3,employeeUserName.user_name);
                    stmt.setString(4,totalText.getText().trim());
                    stmt.executeUpdate();

                    // update the user points
                    PreparedStatement stmtPoints = connection.prepareStatement("UPDATE customers_info SET points = points + ? WHERE barcode = ?");
                    stmtPoints.setString(1,String.valueOf(Double.parseDouble(totalText.getText().trim()) * 0.05));
                    stmtPoints.setString(2,salesCustomerBarcodeText.getText().trim());
                    stmtPoints.executeUpdate();

                    // get the bill number
                    String billNumber;
                    PreparedStatement stmtTwo = connection.prepareStatement("SELECT bill_number FROM " +
                            "bills ORDER BY bill_number DESC LIMIT 1");
                    ResultSet resultSet = stmtTwo.executeQuery();
                    if(resultSet.next()){
                        billNumber = resultSet.getString("bill_number");
                        System.out.println(billNumber);

                        // set the bill info
                        parameter.put("billNumber",billNumber);
                        parameter.put("total",totalText.getText().trim());
                        parameter.put("points",String.valueOf(Double.parseDouble(salesCustomerPointsText.getText().trim()) + Double.parseDouble(totalText.getText().trim()) * 0.10));
                        parameter.put("paidPoints","0.0");

                        // loop through the bill items to update the items , the worker info and add the bills details info
                        for(SalesItem row : salesTable.getItems()){

                            Map<String, Object> m = new HashMap();
                            m.put("name",row.getName());
                            m.put("price",row.getPrice());
                            dataSource.add(m);

                            // update the item
                            PreparedStatement stmtThree = connection.prepareStatement("UPDATE items_info SET available = available - ? WHERE barcode = ?");
                            stmtThree.setString(1,"1");
                            stmtThree.setString(2,row.getBarcode());
                            stmtThree.executeUpdate();

                            // update the worker
                            if(row.getType().equals("S") && employeeUserName.getCommission().equals("Y")){
                                PreparedStatement stmtFour = connection.prepareStatement("UPDATE users_info SET balance = balance + ? WHERE user_name = ?");
                                stmtFour.setString(1,row.getCommission());
                                stmtFour.setString(2,employeeUserName.user_name);
                                stmtFour.executeUpdate();
                            }

                            // update the item
                            PreparedStatement stmtFive = connection.prepareStatement("INSERT INTO bills_info " +
                                    "(bill_number, item_barcode, item_name, item_price, item_Type) " +
                                    "VALUES (?,?,?,?,?)");
                            stmtFive.setString(1,billNumber);
                            stmtFive.setString(2,row.getBarcode());
                            stmtFive.setString(3,row.getName());
                            stmtFive.setString(4,row.getPrice());
                            stmtFive.setString(5,row.getType());
                            stmtFive.executeUpdate();

                        }

                        // reset the transactions info
                        employeeUserName = null ;
                        salesCustomerBarcodeText.setText("");
                        salesCustomerNameText.setText("");
                        salesCustomerPointsText.setText("");
                        employeeSalesComboBox.getSelectionModel().clearSelection();
                        removeAllSalesItemsFunctions();
                        paidMoneyText.setText("");
                        returnedMoneyText.setText("");


                    }

                    // todo ---> enable to print the bills
                    try {
                        JRBeanCollectionDataSource dataSource1 = new JRBeanCollectionDataSource(dataSource);
                        JasperDesign jd = JRXmlLoader.load(HelloApplication.BillPath);
                        JasperReport jp = JasperCompileManager.compileReport(jd);
                        JasperPrint print = JasperFillManager.fillReport(jp, parameter, dataSource1);
                        //JasperViewer.viewReport(print, false);
                        PrintReportToPrinter(print);
                    }catch (Exception e){
                        e.printStackTrace();
//                        Alert alertX = new Alert(Alert.AlertType.CONFIRMATION);
//                        alert.setHeaderText("Error");
//                        alert.setResizable(true);
//                        alert.setContentText(e.toString() + "***" +HelloApplication.BillPath);
//                        alert.setTitle("warning");
//                        alert.show();
                    }

                    CloseConnection();
                }catch (Exception e){
                    e.printStackTrace();
                    CloseConnection();
                }

            }
        }

    }

    @FXML
    void finishTheSaleWithPointsFunction(){
        System.out.println("finishTheSaleWithPointsFunction");
        if(!totalText.getText().trim().isEmpty()){
            if(Double.parseDouble(totalText.getText().trim()) != 0
                    && employeeUserName != null
                    && !salesCustomerBarcodeText.getText().trim().isEmpty()
                    && !salesCustomerNameText.getText().trim().isEmpty()
                    && Double.parseDouble(salesCustomerPointsText.getText().trim()) >= Double.parseDouble(totalText.getText().trim())){
                try {
                    OpenConnection();
                    // set the bill info
                    PreparedStatement stmt = connection.prepareStatement("INSERT INTO bills " +
                            "(customer_barcode, cashier_user_name, worker_user_name, cash) " +
                            "VALUES (?,?,?,?)");
                    stmt.setString(1,salesCustomerBarcodeText.getText().trim());
                    stmt.setString(2,LoginScreen.getTheUsername());
                    stmt.setString(3,employeeUserName.user_name);
                    stmt.setString(4,String.valueOf(Double.parseDouble(totalText.getText().trim())*-1));
                    stmt.executeUpdate();

                    // update the user points with minus
                    PreparedStatement stmtPoints = connection.prepareStatement("UPDATE customers_info SET points = points - ? , redeamed = redeamed + ? WHERE barcode = ?");
                    stmtPoints.setString(1,totalText.getText().trim());
                    stmtPoints.setString(2,totalText.getText().trim());
                    stmtPoints.setString(3,salesCustomerBarcodeText.getText().trim());
                    stmtPoints.executeUpdate();

                    // get the bill number
                    String billNumber;
                    PreparedStatement stmtTwo = connection.prepareStatement("SELECT bill_number FROM " +
                            "bills ORDER BY bill_number DESC LIMIT 1");
                    ResultSet resultSet = stmtTwo.executeQuery();
                    if(resultSet.next()){
                        billNumber = resultSet.getString("bill_number");
                        System.out.println(billNumber);

                        // loop through the bill items to update the items , the worker info and add the bills details info
                        for(SalesItem row : salesTable.getItems()){

                            // update the item
                            PreparedStatement stmtThree = connection.prepareStatement("UPDATE items_info SET available = available - ? WHERE barcode = ?");
                            stmtThree.setString(1,"1");
                            stmtThree.setString(2,row.getBarcode());
                            stmtThree.executeUpdate();

                            // update the worker
                            if(row.getType().equals("S") && employeeUserName.getCommission().equals("Y")){
                                PreparedStatement stmtFour = connection.prepareStatement("UPDATE users_info SET balance = balance + ? WHERE user_name = ?");
                                stmtFour.setString(1,row.getCommission());
                                stmtFour.setString(2,employeeUserName.user_name);
                                stmtFour.executeUpdate();
                            }

                            // update the item
                            PreparedStatement stmtFive = connection.prepareStatement("INSERT INTO bills_info " +
                                    "(bill_number, item_barcode, item_name, item_price, item_Type) " +
                                    "VALUES (?,?,?,?,?)");
                            stmtFive.setString(1,billNumber);
                            stmtFive.setString(2,row.getBarcode());
                            stmtFive.setString(3,row.getName());
                            stmtFive.setString(4,row.getPrice());
                            stmtFive.setString(5,row.getType());
                            stmtFive.executeUpdate();

                        }

                        // reset the transactions info
                        employeeUserName = null ;
                        salesCustomerBarcodeText.setText("");
                        salesCustomerNameText.setText("");
                        salesCustomerPointsText.setText("");
                        employeeSalesComboBox.getSelectionModel().clearSelection();
                        removeAllSalesItemsFunctions();
                        paidMoneyText.setText("");
                        returnedMoneyText.setText("");
                    }
                    CloseConnection();
                }catch (Exception e){
                    e.printStackTrace();
                    CloseConnection();
//                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                    alert.setHeaderText("Error");
//                    alert.setResizable(true);
//                    alert.setContentText(e.toString());
//                    alert.setTitle("warning");
//                    alert.show();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("النقاط لا تكفي !");
                alert.setContentText("النقاط الحاليه لا تكفي الاجمالي !");
                alert.setTitle("تحذير");
                alert.show();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("النقاط لا تعمل حاليا !");
            alert.setContentText(" قم بتفعيل الدفع بالنقاط ثم حاول مره اخري!");
            alert.setTitle("تحذير");
            alert.show();
        }
    }

    @FXML
    void returnTheItemsFunctions(){

        System.out.println("returnTheItemsFunctions");

        if(!totalText.getText().trim().isEmpty()){
            if(Double.parseDouble(totalText.getText().trim()) != 0
                    && employeeUserName != null
                    && !salesCustomerBarcodeText.getText().trim().isEmpty()
                    && !salesCustomerNameText.getText().trim().isEmpty()){
                try {
                    OpenConnection();
                    // set the bill info
                    PreparedStatement stmt = connection.prepareStatement("INSERT INTO bills " +
                            "(customer_barcode, cashier_user_name, worker_user_name, cash) " +
                            "VALUES (?,?,?,?)");
                    stmt.setString(1,salesCustomerBarcodeText.getText().trim());
                    stmt.setString(2,LoginScreen.getTheUsername());
                    stmt.setString(3,employeeUserName.user_name);
                    stmt.setString(4,String.valueOf(Double.parseDouble(totalText.getText().trim()) * -1));
                    stmt.executeUpdate();

                    // update the user points
                    PreparedStatement stmtPoints = connection.prepareStatement("UPDATE customers_info SET points = points - ? WHERE barcode = ?");
                    stmtPoints.setString(1,String.valueOf(Double.parseDouble(totalText.getText().trim()) * 0.05));
                    stmtPoints.setString(2,salesCustomerBarcodeText.getText().trim());
                    stmtPoints.executeUpdate();

                    // get the bill number
                    String billNumber;
                    PreparedStatement stmtTwo = connection.prepareStatement("SELECT bill_number FROM " +
                            "bills ORDER BY bill_number DESC LIMIT 1");
                    ResultSet resultSet = stmtTwo.executeQuery();
                    if(resultSet.next()){
                        billNumber = resultSet.getString("bill_number");
                        System.out.println(billNumber);

                        // loop through the bill items to update the items , the worker info and add the bills details info
                        for(SalesItem row : salesTable.getItems()){

                            // update the item
                            PreparedStatement stmtThree = connection.prepareStatement("UPDATE items_info SET available = available + ? WHERE barcode = ?");
                            stmtThree.setString(1,"1");
                            stmtThree.setString(2,row.getBarcode());
                            stmtThree.executeUpdate();

                            // update the worker
                            if(row.getType().equals("S") && employeeUserName.getCommission().equals("Y")){
                                PreparedStatement stmtFour = connection.prepareStatement("UPDATE users_info SET balance = balance - ? WHERE user_name = ?");
                                stmtFour.setString(1,row.getCommission());
                                stmtFour.setString(2,employeeUserName.user_name);
                                stmtFour.executeUpdate();
                            }

                            // update the item
                            PreparedStatement stmtFive = connection.prepareStatement("INSERT INTO bills_info " +
                                    "(bill_number, item_barcode, item_name, item_price, item_Type) " +
                                    "VALUES (?,?,?,?,?)");
                            stmtFive.setString(1,billNumber);
                            stmtFive.setString(2,row.getBarcode());
                            stmtFive.setString(3,row.getName());
                            stmtFive.setString(4,row.getPrice());
                            stmtFive.setString(5,row.getType());
                            stmtFive.executeUpdate();

                        }

                        // reset the transactions info
                        employeeUserName = null ;
                        salesCustomerBarcodeText.setText("");
                        salesCustomerNameText.setText("");
                        salesCustomerPointsText.setText("");
                        employeeSalesComboBox.getSelectionModel().clearSelection();
                        removeAllSalesItemsFunctions();
                        paidMoneyText.setText("");
                        returnedMoneyText.setText("");


                    }
                    CloseConnection();
                }catch (Exception e){
                    e.printStackTrace();
                    CloseConnection();
                }

            }
        }




    }


    // manage the settings

    @FXML
    private JFXTextField billUrlPathText;

    @FXML
    private JFXButton payWithPoints;

    @FXML
    private JFXButton payWithPointsButton;

    void updateTheBillPath(){

        try {
            OpenConnection();
            PreparedStatement stmt = connection.prepareStatement("Select barcode FROM jasper_urls WHERE name = ?");
            stmt.setString(1,"bill");
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                HelloApplication.BillPath = resultSet.getString("barcode");
            }
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }

    void updatePayWithPointsPermission(){

        try {
            OpenConnection();
            PreparedStatement stmt = connection.prepareStatement("Select barcode FROM jasper_urls WHERE name = ?");
            stmt.setString(1,"payWithPoints");
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                HelloApplication.PayWithPoints = resultSet.getString("barcode");
            }
            if(HelloApplication.PayWithPoints.equals("No")){
                payWithPoints.setText("تفعيل");
                payWithPointsButton.setDisable(true);
            }else {
                payWithPoints.setText("ايقاف");
                payWithPointsButton.setDisable(false);
            }
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }

    @FXML
    void addBillUrlFunction(){
        if(!billUrlPathText.getText().trim().isEmpty()){

            try {
                OpenConnection();

                PreparedStatement stmt = connection.prepareStatement("UPDATE jasper_urls SET barcode = ? WHERE name = ?");
                stmt.setString(1,billUrlPathText.getText().trim());
                stmt.setString(2,"bill");
                stmt.executeUpdate();
                updateTheBillPath();
                billUrlPathText.setText("");
                CloseConnection();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }
        }
    }

    @FXML
    void enableThePointsPayFunction(){

        try {
            OpenConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE jasper_urls SET barcode = ? WHERE name = ?");
            if(!HelloApplication.PayWithPoints.equals("No")){
                stmt.setString(1,"No");
            }else{
                stmt.setString(1,"Yes");
            }
            stmt.setString(2,"payWithPoints");
            stmt.executeUpdate();
            updatePayWithPointsPermission();
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    // manage the Stats tap

    @FXML
    private DatePicker dailyReportDate;

    @FXML
    void openTheDailyReport(){

        if(HelloApplication.getReportScreen().isShowing()){
            HelloApplication.getReportScreen().close();
        }
        try {

            if(!dailyReportDate.getEditor().getText().trim().isEmpty()){
                getTheChosenDate = dailyReportDate.getValue();
            }

            URL urlIcon = HelloApplication.class.getResource("app.jpg");
            System.out.println(urlIcon);
            assert urlIcon != null;
            HelloApplication.getReportScreen().getIcons().add(new Image(urlIcon.toExternalForm()));
            HelloApplication.getReportScreen().setResizable(true);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("DailyReportScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 600);

            URL url = HelloApplication.class.getResource("application.css");
            if (url == null) {
                System.out.println("Resource not found. Aborting.");
                System.exit(-1);
            }
            String css = url.toExternalForm();
            scene.getStylesheets().add(css);

            HelloApplication.getReportScreen().setTitle("AE Systems *** Stats Screen");
            HelloApplication.getReportScreen().setScene(scene);
            HelloApplication.getReportScreen().show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    private DatePicker employeeReportStatesDateText;

    @FXML
    private JFXTextField employeeNameStateText;

    @FXML
    private DatePicker employeeReportTwoStatesDateText;

    @FXML
    private JFXTextField employeeNameTwoStateText;



    @FXML
    void showStatesForEmployeeByDate(){

        textToTravel = employeeNameStateText.getText().trim();
        if(!employeeReportStatesDateText.getEditor().getText().trim().isEmpty() && textToTravel != null){
            if(HelloApplication.getReportScreen().isShowing()){
                HelloApplication.getReportScreen().close();
            }
            try {
                getTheChosenDate = employeeReportStatesDateText.getValue();

                URL urlIcon = HelloApplication.class.getResource("app.jpg");
                System.out.println(urlIcon);
                assert urlIcon != null;
                HelloApplication.getReportScreen().getIcons().add(new Image(urlIcon.toExternalForm()));
                HelloApplication.getReportScreen().setResizable(true);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("EmployeeReportByDate.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 600);

                URL url = HelloApplication.class.getResource("application.css");
                if (url == null) {
                    System.out.println("Resource not found. Aborting.");
                    System.exit(-1);
                }
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);

                HelloApplication.getReportScreen().setTitle("AE Systems *** Stats Screen");
                HelloApplication.getReportScreen().setScene(scene);
                HelloApplication.getReportScreen().show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @FXML
    void showStatesTwoForEmployeeByDate(){

        textToTravel = employeeNameTwoStateText.getText().trim();
        if(!employeeReportTwoStatesDateText.getEditor().getText().trim().isEmpty() && textToTravel != null){
            if(HelloApplication.getReportScreen().isShowing()){
                HelloApplication.getReportScreen().close();
            }
            try {
                getTheChosenDate = employeeReportTwoStatesDateText.getValue();

                URL urlIcon = HelloApplication.class.getResource("app.jpg");
                assert urlIcon != null;
                HelloApplication.getReportScreen().getIcons().add(new Image(urlIcon.toExternalForm()));
                HelloApplication.getReportScreen().setResizable(true);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("EmployeeDailyStatesScreen.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 600);

                URL url = HelloApplication.class.getResource("application.css");
                if (url == null) {
                    System.out.println("Resource not found. Aborting.");
                    System.exit(-1);
                }
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);

                HelloApplication.getReportScreen().setTitle("AE Systems *** Stats Screen");
                HelloApplication.getReportScreen().setScene(scene);
                HelloApplication.getReportScreen().show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    // todo *************************************************************************************

    @FXML
    private JFXTextField employeeNameForBillsText;
    @FXML
    private DatePicker showEmployeeBillsByDateText;
    @FXML
    void showAllBillsForEmployeeByDate(){

        textToTravel = employeeNameForBillsText.getText().trim();
        mode = 0;
        if(!textToTravel.equals("")&& !showEmployeeBillsByDateText.getEditor().getText().trim().isEmpty()){
            if(HelloApplication.getReportScreen().isShowing()){
                HelloApplication.getReportScreen().close();
            }
            try {
                getTheChosenDate = showEmployeeBillsByDateText.getValue();

                URL urlIcon = HelloApplication.class.getResource("app.jpg");
                assert urlIcon != null;
                HelloApplication.getReportScreen().getIcons().add(new Image(urlIcon.toExternalForm()));
                HelloApplication.getReportScreen().setResizable(true);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AllBillsForEmployee.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 600);

                URL url = HelloApplication.class.getResource("application.css");
                if (url == null) {
                    System.out.println("Resource not found. Aborting.");
                    System.exit(-1);
                }
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);

                HelloApplication.getReportScreen().setTitle("AE Systems *** Stats Screen");
                HelloApplication.getReportScreen().setScene(scene);
                HelloApplication.getReportScreen().show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @FXML
    private JFXTextField customerNameForBillsText;
    @FXML
    private DatePicker showClientBillsByDateText;
    @FXML
    void showAllBillsForCustomerByDate(){

        textToTravel = customerNameForBillsText.getText().trim();
        mode = 1 ;
        if(!textToTravel.equals("")&& !showClientBillsByDateText.getEditor().getText().trim().isEmpty()){
            if(HelloApplication.getReportScreen().isShowing()){
                HelloApplication.getReportScreen().close();
            }
            try {
                getTheChosenDate = showClientBillsByDateText.getValue();

                URL urlIcon = HelloApplication.class.getResource("app.jpg");
                assert urlIcon != null;
                HelloApplication.getReportScreen().getIcons().add(new Image(urlIcon.toExternalForm()));
                HelloApplication.getReportScreen().setResizable(true);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AllBillsForEmployee.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 600);

                URL url = HelloApplication.class.getResource("application.css");
                if (url == null) {
                    System.out.println("Resource not found. Aborting.");
                    System.exit(-1);
                }
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);

                HelloApplication.getReportScreen().setTitle("AE Systems *** Stats Screen");
                HelloApplication.getReportScreen().setScene(scene);
                HelloApplication.getReportScreen().show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @FXML
    private JFXTextField cashierNameForBillsText;
    @FXML
    private DatePicker showCashierBillsByDateText;
    @FXML
    void showAllBillsForCashierByDate(){

        textToTravel = cashierNameForBillsText.getText().trim();
        mode = 2;
        if(!textToTravel.equals("")&& !showCashierBillsByDateText.getEditor().getText().trim().isEmpty()){
            if(HelloApplication.getReportScreen().isShowing()){
                HelloApplication.getReportScreen().close();
            }
            try {
                getTheChosenDate = showCashierBillsByDateText.getValue();

                URL urlIcon = HelloApplication.class.getResource("app.jpg");
                assert urlIcon != null;
                HelloApplication.getReportScreen().getIcons().add(new Image(urlIcon.toExternalForm()));
                HelloApplication.getReportScreen().setResizable(true);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AllBillsForEmployee.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 600);

                URL url = HelloApplication.class.getResource("application.css");
                if (url == null) {
                    System.out.println("Resource not found. Aborting.");
                    System.exit(-1);
                }
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);

                HelloApplication.getReportScreen().setTitle("AE Systems *** Stats Screen");
                HelloApplication.getReportScreen().setScene(scene);
                HelloApplication.getReportScreen().show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    // todo *************************************************************************************

    @FXML
    private JFXTextField billNumberText;
    @FXML
    void showBillDetails(){

        textToTravel = billNumberText.getText().trim();
        if(!textToTravel.equals("")){
            if(HelloApplication.getReportScreen().isShowing()){
                HelloApplication.getReportScreen().close();
            }
            try {
                URL urlIcon = HelloApplication.class.getResource("app.jpg");
                assert urlIcon != null;
                HelloApplication.getReportScreen().getIcons().add(new Image(urlIcon.toExternalForm()));
                HelloApplication.getReportScreen().setResizable(true);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("DetailsAboutBillNumber.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 600);

                URL url = HelloApplication.class.getResource("application.css");
                if (url == null) {
                    System.out.println("Resource not found. Aborting.");
                    System.exit(-1);
                }
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);

                HelloApplication.getReportScreen().setTitle("AE Systems *** Stats Screen");
                HelloApplication.getReportScreen().setScene(scene);
                HelloApplication.getReportScreen().show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


    // todo **********************************

    @FXML
    private DatePicker showAllBillsByDateText;

    @FXML
    void showAllBillsByDate(){

        textToTravel = employeeNameStateText.getText().trim();
        if(!showAllBillsByDateText.getEditor().getText().trim().isEmpty() && textToTravel != null){
            if(HelloApplication.getReportScreen().isShowing()){
                HelloApplication.getReportScreen().close();
            }
            try {
                getTheChosenDate = showAllBillsByDateText.getValue();

                URL urlIcon = HelloApplication.class.getResource("app.jpg");
                System.out.println(urlIcon);
                assert urlIcon != null;
                HelloApplication.getReportScreen().getIcons().add(new Image(urlIcon.toExternalForm()));
                HelloApplication.getReportScreen().setResizable(true);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShowAllBills.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 600);

                URL url = HelloApplication.class.getResource("application.css");
                if (url == null) {
                    System.out.println("Resource not found. Aborting.");
                    System.exit(-1);
                }
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);

                HelloApplication.getReportScreen().setTitle("AE Systems *** Stats Screen");
                HelloApplication.getReportScreen().setScene(scene);
                HelloApplication.getReportScreen().show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @FXML
    private DatePicker showAllExpensesByDateText;

    @FXML
    void showAllExpensesByDate(){

        textToTravel = employeeNameStateText.getText().trim();
        if(!showAllExpensesByDateText.getEditor().getText().trim().isEmpty() && textToTravel != null){
            if(HelloApplication.getReportScreen().isShowing()){
                HelloApplication.getReportScreen().close();
            }
            try {
                getTheChosenDate = showAllExpensesByDateText.getValue();

                URL urlIcon = HelloApplication.class.getResource("app.jpg");
                System.out.println(urlIcon);
                assert urlIcon != null;
                HelloApplication.getReportScreen().getIcons().add(new Image(urlIcon.toExternalForm()));
                HelloApplication.getReportScreen().setResizable(true);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShowAllExpenses.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 600);

                URL url = HelloApplication.class.getResource("application.css");
                if (url == null) {
                    System.out.println("Resource not found. Aborting.");
                    System.exit(-1);
                }
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);

                HelloApplication.getReportScreen().setTitle("AE Systems *** Stats Screen");
                HelloApplication.getReportScreen().setScene(scene);
                HelloApplication.getReportScreen().show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @FXML
    void showAllTransactions(){
            if(HelloApplication.getReportScreen().isShowing()){
                HelloApplication.getReportScreen().close();
            }
            try {
                URL urlIcon = HelloApplication.class.getResource("app.jpg");
                assert urlIcon != null;
                HelloApplication.getReportScreen().getIcons().add(new Image(urlIcon.toExternalForm()));
                HelloApplication.getReportScreen().setResizable(true);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShowAllTransactions.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 600);

                URL url = HelloApplication.class.getResource("application.css");
                if (url == null) {
                    System.out.println("Resource not found. Aborting.");
                    System.exit(-1);
                }
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);

                HelloApplication.getReportScreen().setTitle("AE Systems *** Stats Screen");
                HelloApplication.getReportScreen().setScene(scene);
                HelloApplication.getReportScreen().show();
            }catch (Exception e){
                e.printStackTrace();
            }

    }




    // todo **********************************




    // manage the exit tap
    @FXML
    private JFXButton exitButton;

    @FXML
    void signOutFunction(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("تأكيد !");
        alert.setContentText("هل تريد تسجيل الخروج من البرنامج ؟");
        alert.setTitle("تأكيد تسجيل الخروج");
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.OK);
        if (button == ButtonType.OK) {
            try {

                URL urlIcon = HelloApplication.class.getResource("app.jpg");
                System.out.println(urlIcon);
                assert urlIcon != null;
                HelloApplication.getTheStage().getIcons().add(new Image(urlIcon.toExternalForm()));
                HelloApplication.getTheStage().setResizable(true);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginScreen.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 600);

                URL url = HelloApplication.class.getResource("application.css");
                if (url == null) {
                    System.out.println("Resource not found. Aborting.");
                    System.exit(-1);
                }
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);

                HelloApplication.getTheStage().setTitle("AE Systems");
                HelloApplication.getTheStage().setScene(scene);
                HelloApplication.getTheStage().show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    void exitFunction(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("تأكيد !");
        alert.setContentText("هل تريد الخروج من البرنامج ؟");
        alert.setTitle("تأكيد الخروج");
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.OK);
        if (button == ButtonType.OK) {
            System.exit(0);
        }
    }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTheBillPath();
        updatePayWithPointsPermission();
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
        TextFormatter<Double> textFormatterForCashForWithdrawText = new TextFormatter<>(converter,0.0, filter);
        TextFormatter<Double> textFormatterForCashForDepositText = new TextFormatter<>(converter, 0.0, filter);
        TextFormatter<Double> textFormatterForSalary = new TextFormatter<>(converter, 0.0, filter);
        TextFormatter<Double> textFormatterForEditSalary = new TextFormatter<>(converter, 0.0, filter);
        TextFormatter<Double> textFormatterForEmpTransaction = new TextFormatter<>(converter, 0.0, filter);
        TextFormatter<Double> textFormatterForOriginalPrice = new TextFormatter<>(converter, 0.0, filter);
        TextFormatter<Double> textFormatterForPublicPrice = new TextFormatter<>(converter, 0.0, filter);
        TextFormatter<Double> textFormatterForPercentagePrice = new TextFormatter<>(converter, 0.0, filter);
        TextFormatter<Double> textFormatterForAvailableItem = new TextFormatter<>(converter, 0.0, filter);
        TextFormatter<Double> textFormatterForPaidMoneyText = new TextFormatter<>(converter, 0.0, filter);
        cashForWithdrawText.setTextFormatter(textFormatterForCashForWithdrawText);
        cashForDepositText.setTextFormatter(textFormatterForCashForDepositText);
        employeeSalaryText.setTextFormatter(textFormatterForSalary);
        employeeSalaryEditText.setTextFormatter(textFormatterForEditSalary);
        moneyToWithdrawText.setTextFormatter(textFormatterForEmpTransaction);
        originalPriceText.setTextFormatter(textFormatterForOriginalPrice);
        publicPriceText.setTextFormatter(textFormatterForPublicPrice);
        itemEmployeePercentageText.setTextFormatter(textFormatterForPercentagePrice);
        availableText.setTextFormatter(textFormatterForAvailableItem);
        paidMoneyText.setTextFormatter(textFormatterForPaidMoneyText);

        // set tables
        setTheEmployeeTable();
        setTheCustomersTable();
        setTheItemsTable();
        setTheSalesTable();


        // manage employees tabs actions
        employeeTabPane.getSelectionModel().selectedIndexProperty().addListener((ov, oldValue, newValue) -> {
            // do something...
            if(newValue.intValue() == 0){
                System.out.println("addEmployeeTab");
            }else if(newValue.intValue() == 1){
                System.out.println("editEmployeeTab");
                loadTheEmployeesComboBox();
            }else if(newValue.intValue() == 2){
                System.out.println("employeeTransactionsTab");
                loadTheEmployeeMoneyTransactionComboBox();
            }else if(newValue.intValue() == 3){
                System.out.println("employeeSalaryTab");
                loadTheEmployeeSalaryComboBox();
            }else if(newValue.intValue() == 4){
                System.out.println("employeeTableTab");
                LoadTheEmployeeInfoTable("",0,0);
            }
        });

        customerTabPane.getSelectionModel().selectedIndexProperty().addListener((ov, oldValue, newValue) -> {
            // do something...
            if(newValue.intValue() == 0){
                System.out.println("addCustomerTab");
            }else if(newValue.intValue() == 1){
                System.out.println("editCustomerTab");
            }else if(newValue.intValue() == 2){
                System.out.println("allCustomers");
                LoadTheCustomersInfoTable("",0,0);
            }
        });

        itemsPane.getSelectionModel().selectedIndexProperty().addListener((ov, oldValue, newValue) -> {
            // do something...
            if(newValue.intValue() == 0){
                System.out.println("addItemTab");
            }else if(newValue.intValue() == 1){
                System.out.println("editItemTab");
            }else if(newValue.intValue() == 2){
                System.out.println("allItems");
                LoadTheItemsInfoTable("",0,0);
            }
        });

        mainTab.getSelectionModel().selectedIndexProperty().addListener((ov, oldValue, newValue) -> {
            // do something...
            if(newValue.intValue() == 4){
                System.out.println("SalesTab");
                loadTheEmployeesComboBoxForSalesScreen();
            }
        });

        // permissions for the normal user
        if(LoginScreen.getUserType().equals("E")){
            mainTab.getTabs().remove(0);
            mainTab.getTabs().remove(0);
            mainTab.getTabs().remove(0);
            mainTab.getTabs().remove(0);
            mainTab.getTabs().remove(1);
            mainTab.getTabs().remove(1);
            loadTheEmployeesComboBoxForSalesScreen();
        }


        // set an alert for close button
        HelloApplication.getTheStage().setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            exitFunction();
        });



    }





}