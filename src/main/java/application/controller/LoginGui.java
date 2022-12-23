package application.controller;

import domain.User;
import javafx.fxml.FXML;
import service.Service;

public class LoginGui {
    Service service;
    User account;
    @FXML
    void initialize(){
    }

    void login(){

    }

    public void setService(Service service) {
        this.service=service;
    }
    public void setAccount(User account) {
        this.account=account;
    }
}
