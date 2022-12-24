package application.controller;

import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import service.Service;
import service.ServiceException;

import java.util.List;
import java.util.function.Predicate;


public class UserGui extends AbstractController{

    public TextField textFieldUsername;
    public TextField textFieldFirstname;
    public TextField textFieldLastName;
    //! User data
    public PasswordField inputPasswd;
    public TextField searchbar;
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


    public Button buttonSaveModify;
    public Button buttonCancel;
    public Button buttonModify;

    public Button buttonRequest;
    public Button buttonAcceptRequest;
    public Button buttonDenyRequest;
    public Button deleteFriend;
    //"Buttons

    public Group groupUpdate;
    //"Groups

    public TableView<User> tableNetwork;
    public TableColumn<User,String> colUsername;
    public TableColumn<User,String> colLastname;
    public TableColumn<User,String> colFirstname;
    ObservableList<User> usersModel= FXCollections.observableArrayList();
    //"Table users

    public TableView tableRequests;
    //"Table request

    public TableView tableFriends;
    //"Table friends

    public TableView tableRequest;
    //"Table request

    protected void populateUsers(){
        List<User> list=null;
        try {
            list = service.getUsers();
        } catch (ServiceException e) {
            errorShow(e.getMessage());
            return;
        }
        Predicate<User> notMe=e-> !e.equals(account);
        Predicate<User> search=e-> e.getUsername().contains(searchbar.getText());
        var users=list.stream().filter(notMe.and(search)).toList();
        usersModel.setAll(users);
    }

    @FXML
    void initialize() {
        tabProfile.setGraphic(new ImageView("images/account.png"));
        tabFriends.setGraphic(new ImageView("images/friends.png"));
        tabRequest.setGraphic(new ImageView("images/request.png"));
        tabChat.setGraphic(new ImageView("images/chat.png"));
        circlePicture.setFill(new ImagePattern(new Image("images/Prf.jpg")));

        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFirstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        colLastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        tableNetwork.setItems(usersModel);

        searchbar.textProperty().addListener(e->populateUsers());

        inputPasswd.setVisible(false);
        groupUpdate.setVisible(false);


    }

    public  void setUp(Service service, User user){
        setService(service);
        setUser(user);
        uploadUserData();
        populateUsers();
    }

    public void setService(Service service) {
        this.service=service;

    }
    private void uploadUserData(){
        textFieldUsername.setText(account.getUsername());
        textFieldFirstname.setText(account.getFirstname());
        textFieldLastName.setText(account.getLastname());
    }
    public void setUser(User user) {
        this.account=user;
    }

    public void modify() {

        textFieldUsername.setDisable(!textFieldUsername.isDisabled());
        textFieldFirstname.setDisable(!textFieldFirstname.isDisabled());
        textFieldLastName.setDisable(!textFieldLastName.isDisabled());
        searchbar.textProperty().addListener(e->populateUsers());

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
     if(!account.getUsername().equals(aux))
         updated.setUsername(aux);
     //Username

     aux=textFieldFirstname.getText();
     if(!account.getFirstname().equals(aux))
         updated.setFirstname(aux);
     //Firstname

     aux=textFieldLastName.getText();
     if(!account.getLastname().equals(aux))
         updated.setLastname(aux);
     //Lastname

     try {
            service.update(updated);
            condirmationShow("Update was a success!");
            setUser(updated);
            modify();

     } catch (ServiceException e) {
         errorShow(e.getMessage());
     }
    }
    public void cancelUpdate() {
        uploadUserData();
        modify();
    }

    public void sendRequest() {
        var user=tableNetwork.getSelectionModel().getSelectedItem();
        if(user==null){
            errorShow("Unselected user!");
            return;
        }

        try {
            service.addRequest(account,user);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            errorShow(e.getMessage());
        }
    }

    public void acceptRequest(ActionEvent actionEvent) {
        service.updateRequest("acepted");
    }

    public void denyRequest(ActionEvent actionEvent) {
        service.updateRequest("deny");
    }
}
