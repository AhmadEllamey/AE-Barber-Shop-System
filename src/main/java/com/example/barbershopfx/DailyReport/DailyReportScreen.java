package com.example.barbershopfx.DailyReport;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import static com.example.barbershopfx.Database.DBConnection.*;
import static com.example.barbershopfx.MainScreen.MainScreen.getTheChosenDate;

public class DailyReportScreen implements Initializable {

    LocalDate myDate;


    public DailyReportScreen() {
        this.myDate = getTheChosenDate;
        getTheChosenDate = null;
    }


    @FXML
    private JFXTextField depositText;

    @FXML
    private JFXTextField withdrawText;

    @FXML
    private JFXTextField paidTodayText;

    @FXML
    private JFXTextField totalSellsText;

    @FXML
    private JFXTextField totalLoseText;

    @FXML
    private JFXTextField totalExpensesText;

    @FXML
    private JFXTextField totalIncomeText;

    @FXML
    void doneFunction(ActionEvent event) {
        // close the window
        Stage stage = (Stage) depositText.getScene().getWindow();
        stage.close();
    }

    @FXML
    void printFunction(ActionEvent event) {
        // ToDo --> print the daily bill.
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            OpenConnection();
            // get the transactions by the current day
            double totalWithdraw = 0 ;
            double totalDeposit = 0 ;
            double cash;

            PreparedStatement transactionsStmt ;
            PreparedStatement expensesStmt;
            PreparedStatement sellsStmt;
            if(myDate == null){
                transactionsStmt =
                        connection.prepareStatement("SELECT * FROM transactions WHERE" +
                                " last_time_updated >= CURDATE() AND last_time_updated < DATE_ADD(CURDATE(),INTERVAL 1 DAY)");

                expensesStmt =
                        connection.prepareStatement("SELECT * FROM expenses WHERE" +
                                " last_time_updated >= CURDATE() AND last_time_updated < DATE_ADD(CURDATE(),INTERVAL 1 DAY)");

                sellsStmt =
                        connection.prepareStatement("SELECT * FROM bills WHERE" +
                                " last_time_updated >= CURDATE() AND last_time_updated < DATE_ADD(CURDATE(),INTERVAL 1 DAY)");

            }else{
                // get the start and the end date
                Calendar calendar = new GregorianCalendar(myDate.getYear(), myDate.getMonthValue()-1, myDate.getDayOfMonth());
                long start = calendar.getTimeInMillis();
                Timestamp timestampStart = new Timestamp(start);
                System.out.println("the start -->" + calendar.getTime());
                calendar.add(Calendar.DATE,1);
                System.out.println("the end -->" + calendar.getTime());
                long end = calendar.getTimeInMillis();
                Timestamp timestampEnd = new Timestamp(end);

                transactionsStmt =
                        connection.prepareStatement("SELECT * FROM transactions WHERE " +
                                "last_time_updated >= ? AND last_time_updated < ?");
                transactionsStmt.setTimestamp(1, timestampStart);
                transactionsStmt.setTimestamp(2, timestampEnd);

                expensesStmt =
                        connection.prepareStatement("SELECT * FROM expenses WHERE " +
                                "last_time_updated >= ? AND last_time_updated < ?");
                expensesStmt.setTimestamp(1, timestampStart);
                expensesStmt.setTimestamp(2, timestampEnd);

                sellsStmt =
                        connection.prepareStatement("SELECT * FROM bills WHERE " +
                                "last_time_updated >= ? AND last_time_updated < ?");
                sellsStmt.setTimestamp(1, timestampStart);
                sellsStmt.setTimestamp(2, timestampEnd);
            }


            ResultSet transactionsResultSet = transactionsStmt.executeQuery();
            while (transactionsResultSet.next()){
                try {
                    cash = Double.parseDouble(transactionsResultSet.getString("cash"));
                }catch (Exception e){
                    cash = 0 ;
                }
                if(cash >= 0){
                    totalDeposit+=cash;
                }else{
                    totalWithdraw-=cash;
                }
            }


            // get the expenses by the current day
            double totalSalary = 0 ;
            double salaryCash ;
            ResultSet expensesResultSet = expensesStmt.executeQuery();
            while (expensesResultSet.next()){
                try{
                    salaryCash = Double.parseDouble(expensesResultSet.getString("salary"))
                            + Double.parseDouble(expensesResultSet.getString("balance"))
                            - Double.parseDouble(expensesResultSet.getString("withdraw"));
                }catch (Exception e){
                    salaryCash = 0 ;
                }
                totalSalary += salaryCash ;
            }


            // get the sells by the current day
            double totalEarn = 0 ;
            double totalLose = 0 ;
            double tellerCash;

            ResultSet sellsResultSet = sellsStmt.executeQuery();
            while (sellsResultSet.next()){
                try {
                    tellerCash = Double.parseDouble(sellsResultSet.getString("cash"));
                }catch (Exception e){
                    tellerCash = 0 ;
                }
                if(tellerCash >= 0){
                    totalEarn+=tellerCash;
                }else{
                    totalLose+=tellerCash;
                }
            }

            totalLose*= -1;

            double totalEarnToday = totalDeposit + totalEarn;
            double totalLoseToday = totalWithdraw + totalSalary + totalLose;

            withdrawText.setText(String.valueOf(totalWithdraw));
            depositText.setText(String.valueOf(totalDeposit));
            paidTodayText.setText(String.valueOf(totalSalary));
            totalSellsText.setText(String.valueOf(totalEarn));
            totalLoseText.setText(String.valueOf(totalLose));
            totalIncomeText.setText(String.valueOf(totalEarnToday));
            totalExpensesText.setText(String.valueOf(totalLoseToday));

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }
}
