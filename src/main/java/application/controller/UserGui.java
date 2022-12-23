package application.controller;

import javafx.fxml.FXML;

import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import service.Service;
import javafx.stage.Stage;

public class UserGui extends AbstractMethodError {
    public AnchorPane anchorPane;
    Service service;

    public Tab tabFriends;
    public Tab tabChat;
    public Tab tabRequest;
    public Tab tabProfile;
    //"Tabs

    public Circle circlePicture;
    //"Shape

    @FXML
    void initialize() {
        tabProfile.setGraphic(new ImageView("images/account.png"));
        tabFriends.setGraphic(new ImageView("images/friends.png"));
        tabRequest.setGraphic(new ImageView("images/request.png"));
        tabChat.setGraphic(new ImageView("images/chat.png"));
        circlePicture.setFill(new ImagePattern(new Image("images/Prf.jpg")));
    }

    public void setService(Service service) {
        this.service=service;
    }
}
