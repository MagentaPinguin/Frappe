package application.controller;

import anexe.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.Service;
import service.ServiceException;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;


public class RegisterGui extends AbstractController {
    private  Service service;
    //" Service

    public Button registerNetwork;
    public Circle picImgPreview;
    public  ImageView imgFrappe;
    private String optionalProfileImage="images/profilePics/pic_basic.jpg";
    //" Image & forms

    public TextField inputUsername;
    public TextField inputFirstName;
    public TextField inputLastName;
    public PasswordField inputPasswd;
    //" Input fields

    public Button exitRegister;
    //" buttons


    /***
     * Sets some properties of the window's widgets .
     */
    @FXML
    void initialize()
    {

        picImgPreview.setFill(new ImagePattern(new Image(optionalProfileImage)));
        inputUsername.textProperty().addListener(e->frappeEvolve());
        inputFirstName.textProperty().addListener(e->frappeEvolve());
        inputLastName.textProperty().addListener(e->frappeEvolve());
        inputPasswd.textProperty().addListener(e->frappeEvolve());

    }

    /***
     * Simulates an animation in register window.
     */
    public void frappeEvolve() {

        var x= (int)Stream.of(inputUsername,inputFirstName,inputLastName,inputPasswd).filter(e->!e.getText().isBlank()).count();
        if (x == 1) {
            imgFrappe.setImage( new Image("images/stages/Frappe_stage_2.jpg"));
        } else if (x == 2) {
            imgFrappe.setImage( new Image("images/stages/Frappe_stage_3.jpg"));
        } else if (x==3) {
            imgFrappe.setImage( new Image("images/stages/Frappe_stage_4.jpg"));
        } else if (x==4) {
            imgFrappe.setImage( new Image("images/stages/Frappe_stage_5.jpg"));
        }else {
            imgFrappe.setImage( new Image("images/stages/Frappe_stage_1.jpg"));}
    }


    /***
     * Handles the cancel registration process
     * @param actionEvent
     */
    public void closeWindow(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage crtStage = (Stage) source.getScene().getWindow();
        crtStage.close();
    }

    /***
     * Handles the register process.
     */
    public void joinNetwork( ) {
        try {
            service.addUser(List.of(inputUsername.getText(),inputPasswd.getText(),inputFirstName.getText(),inputLastName.getText(),optionalProfileImage));
            condirmationShow("Welcome to the brew!");

        } catch (ServiceException e) {
            errorShow(e.getMessage());
        }
    }

    /***
     * Handles the profile picture chooser.
     */
    public void pickPicture() {

        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Profile picture");

        var url= this.getClass().getClassLoader().getResource("images").toString();


        fileChooser.setInitialDirectory(new File(url.toString()));
        optionalProfileImage=fileChooser.showOpenDialog(new Stage()).toString().substring(49);
        picImgPreview.setFill(new ImagePattern(new Image(optionalProfileImage)));
    }

    /***
     * Sets the service object for this window
     * @param service:
     */
    public void setService(Service service){
        this.service=service;
    }

}