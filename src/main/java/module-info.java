module com.example.barbershopfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.sql;
    requires jasperreports;
    requires java.desktop;


    opens com.example.barbershopfx to javafx.fxml;
    exports com.example.barbershopfx;
    exports com.example.barbershopfx.Login;
    opens com.example.barbershopfx.Login to javafx.fxml;
    exports com.example.barbershopfx.MainScreen;
    opens com.example.barbershopfx.MainScreen to javafx.fxml;
    exports com.example.barbershopfx.UpdateItem;
    opens com.example.barbershopfx.UpdateItem to javafx.fxml;
    exports com.example.barbershopfx.ItemHistory;
    opens com.example.barbershopfx.ItemHistory to javafx.fxml;
    exports com.example.barbershopfx.DailyReport;
    opens com.example.barbershopfx.DailyReport to javafx.fxml;
    exports com.example.barbershopfx.EmployeeReportByDate;
    opens com.example.barbershopfx.EmployeeReportByDate to javafx.fxml;
}