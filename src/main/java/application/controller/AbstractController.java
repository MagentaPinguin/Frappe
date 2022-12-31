package application.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public abstract class AbstractController {


     protected void errorShow(String msg){
        Alert a= new Alert(Alert.AlertType.ERROR,msg,new ButtonType("Okey...."));

        Image img = new Image("images/error_coffee.png", 120, 120, true, true);

        Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("images/frappe_icon.png"));

        a.setTitle("ERROR");
        a.setGraphic( new ImageView(img));

        a.show();
    }

    protected void confirmationShow(String msg){
        Alert a= new Alert(Alert.AlertType.CONFIRMATION,msg,new ButtonType("Okey...."));

        Image img = new Image("images/success_coffee.png", 120, 120, true, true);

        Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("images/frappe_icon.png"));

        a.setTitle("ERROR");
        a.setGraphic( new ImageView(img));

        a.show();
    }


}
