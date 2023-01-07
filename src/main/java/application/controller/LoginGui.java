package application.controller;

import application.Application;

import domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.Service;
import service.ServiceException;
import java.io.IOException;

public class LoginGui extends AbstractController{
    public TextField txtFieldUsername;
    public TextField txtFieldPasswd;
    //" TextFields

    public Button buttonLogin;
    public AnchorPane background;
    //"Buttons

    private Service service;
    //"Service

    @FXML
    void initialize(){

    }

    /***
     * Handles the login procedure.
     */
    @FXML
    public void login(){

        try {
            var found=service.getUserByUsername(txtFieldUsername.getText());
            if(!found.getPasswd().equals(txtFieldPasswd.getText()))
                throw  new ServiceException("Incorrect password!");
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("user-gui.fxml"));
            Scene scene = new Scene(fxmlLoader.load()) ;
            UserGui ctrl=fxmlLoader.getController();
            ctrl.setUp(service,found);

            Stage stage=new Stage();
            stage.setTitle("FRAPPE");
            stage.setScene(scene);
            stage.getIcons().add(new Image("images/frappe_icon.png"));
            stage.setResizable(false);

            stage.show();


        } catch (ServiceException | IOException e) {
            errorShow(e.getMessage());
        }
        txtFieldUsername.clear();
        txtFieldPasswd.clear();
    }

    /***
     * Opens the register window.

     */
    public void registerUser(){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("register-gui.fxml"));
            Scene scene = null;
            scene = new Scene(fxmlLoader.load());
            RegisterGui ctrl=fxmlLoader.getController();
            ctrl.setService(service);
            Stage stage=new Stage();
            stage.setTitle("FRAPPE");
            stage.setScene(scene);
            stage.getIcons().add(new Image("images/frappe_icon.png"));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            errorShow(e.getMessage());
        }
    }

    /***
     * Sets the service.
     * @param service: service object
     */
    public void setService(Service service) {
        this.service=service;
        buttonLogin.requestFocus();
    }
}
