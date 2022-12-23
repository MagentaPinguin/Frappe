package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import service.Service;

public class UserGui {
    Service service;
    public Button buttonAccount;
    public Button buttonChat;
    public Button buttonRequests;
    public Button buttonFriends;
    public Button buttonSettings;
    //"Buttons


    @FXML
    void initialize(){
        buttonAccount.setGraphic(new ImageView("images/account.png"));
        buttonChat.setGraphic(new ImageView("images/chat.png"));
        buttonFriends.setGraphic(new ImageView("images/friends.png"));
        buttonRequests.setGraphic(new ImageView("images/request.png"));
    }

    public void setService(Service service) {
        this.service=service;
    }
}
