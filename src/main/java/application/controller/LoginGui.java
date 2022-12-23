package application.controller;

import javafx.fxml.FXML;
import service.Service;

public class LoginGui {
    Service service;

    @FXML
    void initialize(){

    }


    public void setService(Service service) {
        this.service=service;
    }
}
