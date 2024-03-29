module application{
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens application to javafx.fxml;
    exports application;
    exports application.controller;
    opens application.controller to javafx.fxml;
    exports domain;
    exports anexe;
    exports service;
}