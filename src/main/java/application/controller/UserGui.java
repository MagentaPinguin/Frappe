package application.controller;

import domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import repository.RepositoryException;
import service.Service;


public class UserGui extends AbstractController{

    public TextField textFieldUsername;
    public TextField textFieldFirstname;
    public TextField textFieldLastName;
    //! User data

    public PasswordField inputPasswd;


    //"Fields
    private Service service;
    private User account;

    public AnchorPane anchorPane;
    //"Layout

    public Tab tabFriends;
    public Tab tabChat;
    public Tab tabRequest;
    public Tab tabProfile;
    //"Tabs

    public Circle circlePicture;
    //"Shape

    public Button changePasswd;
    public Button buttonSaveModify;
    public Button buttonCancel;

    public Button buttonModify;
    //"Buttons

    public Group groupUpdate;
    //"Groups
    @FXML
    void initialize() {
        tabProfile.setGraphic(new ImageView("images/account.png"));
        tabFriends.setGraphic(new ImageView("images/friends.png"));
        tabRequest.setGraphic(new ImageView("images/request.png"));
        tabChat.setGraphic(new ImageView("images/chat.png"));
        circlePicture.setFill(new ImagePattern(new Image("images/Prf.jpg")));

        inputPasswd.setVisible(false);
        groupUpdate.setVisible(false);


    }

    public void setService(Service service) {
        this.service=service;
    }
    private void uploadUserData(){
        textFieldUsername.setText(account.getUserName());
        textFieldFirstname.setText(account.getFirstName());
        textFieldLastName.setText(account.getLastName());
    }
    public void setUser(User user) {
        this.account=user;
        uploadUserData();
    }

    public void modify() {

        textFieldUsername.setDisable(!textFieldUsername.isDisabled());
        textFieldFirstname.setDisable(!textFieldFirstname.isDisabled());
        textFieldLastName.setDisable(!textFieldLastName.isDisabled());

        inputPasswd.setVisible(!inputPasswd.isVisible());
        inputPasswd.setText("");
        groupUpdate.setVisible(!groupUpdate.isVisible());
        buttonModify.setVisible(!buttonModify.isVisible());
    }

    public void doUpdate() {
     var updated=new User(account);
     String aux=inputPasswd.getText();
     if(!aux.isBlank())
         updated.setPasswd(aux);

     aux=textFieldUsername.getText();
     if(!account.getUserName().equals(aux))
         updated.setUserName(aux);
     //Username

     aux=textFieldFirstname.getText();
     if(!account.getFirstName().equals(aux))
         updated.setFirstName(aux);
     //Firstname
     aux=textFieldLastName.getText();
     if(!account.getLastName().equals(aux))
         updated.setLastName(aux);
     //Lastname
        try {
            service.update(updated);
            condirmationShow("Update was a success!");
            setUser(updated);
            modify();

        } catch (RepositoryException e) {
            errorShow(e.getMessage());
        }


    }

    public void cancelUpdate(ActionEvent actionEvent) {
        uploadUserData();
        modify();
    }
}
