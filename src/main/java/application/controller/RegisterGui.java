package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;

import java.util.stream.Stream;


public class RegisterGui {

    public ImageView imgFrappe;
    //" Image

    public TextField inputUsername;
    public TextField inputFirstName;
    public TextField inputLastName;
    public PasswordField inputPasswd;
    //" Input fields
    @FXML
    void initialize(){

          inputUsername.textProperty().addListener(e->frappeEvolve());;
          inputFirstName.textProperty().addListener(e->frappeEvolve());;
          inputLastName.textProperty().addListener(e->frappeEvolve());;
          inputPasswd.textProperty().addListener(e->frappeEvolve());

    }
    public void frappeEvolve() {

        var x= (int)Stream.of(inputUsername,inputFirstName,inputLastName,inputPasswd).filter(e->!e.getText().isBlank()).count();
        System.out.println(x);
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

}