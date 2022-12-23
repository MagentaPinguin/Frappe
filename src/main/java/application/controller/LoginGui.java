package application.controller;

import domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import service.Service;
import service.ServiceException;

public class LoginGui extends AbstractController{
    public TextField txtFieldUsername;
    public TextField txtFieldPasswd;
    Service service;
    User account;



    @FXML
    void initialize(){
    }

    @FXML
    void login(){

        try {
            var found=service.getUserByUsername(txtFieldUsername.getText());

        } catch (ServiceException e) {

            errorShow(e.getMessage());

        }

    }

    public void setService(Service service) {
        this.service=service;
    }
    public void setAccount(User account) {
        this.account=account;
    }
}
