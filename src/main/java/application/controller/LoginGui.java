package application.controller;

import application.Application;

import domain.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import service.Service;
import service.ServiceException;
import java.io.IOException;

public class LoginGui extends AbstractController{
    public TextField txtFieldUsername;
    public TextField txtFieldPasswd;

    public Button buttonLogin;
    Service service;
    User account;



    @FXML
    void initialize(){
    }

    @FXML
    public void login(){

        try {
            var found=service.getUserByUsername(txtFieldUsername.getText());
            if(!found.getPasswd().equals(txtFieldPasswd.getText()))
                throw  new ServiceException("Incorrect password!");
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("user-gui.fxml"));
            Scene scene = new Scene(fxmlLoader.load()) ;
            UserGui ctrl=fxmlLoader.getController();
            ctrl.setService(service);
            ctrl.setUser(found);

            Stage stage=new Stage();
            stage.setTitle("FRAPPE");
            stage.setScene(scene);
            stage.getIcons().add(new Image("images/frappe_icon.png"));
            stage.setResizable(false);

            stage.show();


        } catch (ServiceException | IOException e) {
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
