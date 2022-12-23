package application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.stage.Stage;
import service.Service;
import service.ServiceException;

import java.util.List;
import java.util.stream.Stream;


public class RegisterGui extends AbstractController {


    public Button registerNetwork;
    Service service;
    //" Service
    public ImageView imgFrappe;
    //" Image

    public TextField inputUsername;
    public TextField inputFirstName;
    public TextField inputLastName;
    public PasswordField inputPasswd;
    //" Input fields

    public Button exitRegister;
    public Button joinNetwork;
    //" buttons
    @FXML
    void initialize(){

          inputUsername.textProperty().addListener(e->frappeEvolve());
          inputFirstName.textProperty().addListener(e->frappeEvolve());
          inputLastName.textProperty().addListener(e->frappeEvolve());
          inputPasswd.textProperty().addListener(e->frappeEvolve());

    }
    public void frappeEvolve() {

        var x= (int)Stream.of(inputUsername,inputFirstName,inputLastName,inputPasswd).filter(e->!e.getText().isBlank()).count();
        if (x == 1) {
            imgFrappe.setImage( new Image("images/Frappe_stage_2.jpg"));
        } else if (x == 2) {
            imgFrappe.setImage( new Image("images/Frappe_stage_3.jpg"));
        } else if (x==3) {
            imgFrappe.setImage( new Image("images/Frappe_stage_4.jpg"));
        } else if (x==4) {
            imgFrappe.setImage( new Image("images/Frappe_stage_5.jpg"));
        }else {
            imgFrappe.setImage( new Image("images/Frappe_stage_1.jpg"));}
    }

    public void setService(Service s){
        this.service=s;
    }

    public void abortRegister(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage crtStage = (Stage) source.getScene().getWindow();
        crtStage.close();

    }

    public void joinNetwork( ) {
        try {
            service.addUser(List.of(inputUsername.getText(),inputPasswd.getText(),inputFirstName.getText(),inputLastName.getText()));
            Alert a= new Alert(Alert.AlertType.CONFIRMATION,"Welcome to the club, Brew!",new ButtonType("YEY"));
            //Alert created
            Image img = new Image("images/success_coffee.png", 120, 120, true, true);
            //Changed icon of the information
            Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("images/frappe_icon.png"));
            //Set icon of a stage

            a.setTitle("Brew created");
            a.setGraphic( new ImageView(img));
            //Final settings
            a.show();

        } catch (ServiceException e) {
            errorShow(e.getMessage());
        };

    }
}