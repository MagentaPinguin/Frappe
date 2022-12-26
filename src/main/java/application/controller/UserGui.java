package application.controller;

import domain.Friendship;
import domain.Request;
import domain.User;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
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

import javafx.util.Pair;
import service.Service;
import service.ServiceException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;


import static anexe.Constants.FORMATTER;


public class UserGui extends AbstractController {

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
    public Button buttonDeleteFriend;
    //"Buttons

    public Group groupUpdate;
    //"Groups

    public TableView<User> tableNetwork;
    public TableColumn<User, String> colUsernameNetwork;
    public TableColumn<User, String> colLastnameNetwork;
    public TableColumn<User, String> colFirstnameNetwork;
    ObservableList<User> usersModel = FXCollections.observableArrayList();
    //"Table users

    public TableView<Pair<User, LocalDateTime>> tableRequests;
    public TableColumn<Pair<User, LocalDateTime>, String> colRequestUsername;
    public TableColumn<Pair<User, LocalDateTime>, String> colRequestDate;
    ObservableList<Pair<User, LocalDateTime>> requestModel = FXCollections.observableArrayList();

    //"Table request

    public TableView<Pair<User, LocalDateTime>> tableFriends;

    public TableColumn<Pair<User, LocalDateTime>, String> colDateFriends;
    public TableColumn<Pair<User, LocalDateTime>, String> colLastnameFriends;
    public TableColumn<Pair<User, LocalDateTime>, String> colFirstnameFriends;
    ObservableList<Pair<User, LocalDateTime>> friendsModel = FXCollections.observableArrayList();
    //"Table friends


    protected void populateUsers() {
        List<User> list = null;
        try {
            list = service.getUsers();
        } catch (ServiceException e) {
            errorShow(e.getMessage());
            return;
        }
        List<User>x;
        List<User>y;
        try {
            Predicate<Request<UUID>> notDeny=e-> !e.getStatus().equals("DENY");
            x=service.getRequestsFor(account).stream().filter(notDeny).map(e ->service.getUserById(e.getSender())).toList();
            y=service.getRequestsSent(account).stream().filter(notDeny).map(e ->service.getUserById(e.getReceiver())).toList();

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        Predicate<User> notInRequests = e -> !x.contains(e);
        Predicate<User> alreadySent = e -> !y.contains(e);

        Predicate<User> notMe = e -> !e.equals(account);
        Predicate<User> search = e -> e.getUsername().contains(searchbar.getText());
        var users = list.stream().filter(notMe.and(search).and(notInRequests).and(alreadySent)).toList();
        usersModel.setAll(users);
    }

    protected void populateRequests() {
        List<Request<UUID>> list = null;
        try {
            list = service.getRequestsFor(account);
        } catch (ServiceException e) {
            errorShow(e.getMessage());
            return;
        }
        Predicate<Request<UUID>> pending = e -> e.getStatus().equals("PENDING");
        var requests = list.stream()
                .filter(pending)
                .map(e -> new Pair<>(service.getUserById(e.getSender()), e.getDate())).toList();
        requestModel.setAll(requests);
    }

    protected void populateFriends() {
        List<Friendship<UUID>> list = null;
        try {
            list = service.getFriendsFor(account);
        } catch (ServiceException e) {
            errorShow(e.getMessage());
            return;
        }

        var friends = list.stream()
                .map(e ->
                        new Pair<>((e.getFirst().equals(account.getId())) ?
                                service.getUserById(e.getSecond()) : service.getUserById(e.getFirst()),
                                e.getDate())).toList();
        friendsModel.setAll(friends);
    }

    @FXML
    void initialize() {
        tabProfile.setGraphic(new ImageView("images/account.png"));
        tabFriends.setGraphic(new ImageView("images/friends.png"));
        tabRequest.setGraphic(new ImageView("images/request.png"));
        tabChat.setGraphic(new ImageView("images/chat.png"));
        circlePicture.setFill(new ImagePattern(new Image("images/Prf.jpg")));

        colUsernameNetwork.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFirstnameNetwork.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        colLastnameNetwork.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        tableNetwork.setItems(usersModel);
        //Table network

        colRequestDate.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getValue().format(FORMATTER)));
        colRequestUsername.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getKey().getUsername()));
        tableRequests.setItems(requestModel);
        //Table requests

        colFirstnameFriends.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getKey().getFirstname()));
        colLastnameFriends.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getKey().getLastname()));
        colDateFriends.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getValue().format(FORMATTER)));
        tableFriends.setItems(friendsModel);
        //Table friends

        searchbar.textProperty().addListener(e -> populateUsers());

        inputPasswd.setVisible(false);
        groupUpdate.setVisible(false);


    }

    public void setUp(Service service, User user) {
        setService(service);
        setUser(user);
        uploadUserData();
        populateUsers();
        populateFriends();
        populateRequests();

    }

    public void setService(Service service) {
        this.service = service;

    }

    private void uploadUserData() {
        textFieldUsername.setText(account.getUsername());
        textFieldFirstname.setText(account.getFirstname());
        textFieldLastName.setText(account.getLastname());
    }

    public void setUser(User user) {
        this.account = user;
    }

    public void modify() {

        textFieldUsername.setDisable(!textFieldUsername.isDisabled());
        textFieldFirstname.setDisable(!textFieldFirstname.isDisabled());
        textFieldLastName.setDisable(!textFieldLastName.isDisabled());
        searchbar.textProperty().addListener(e -> populateUsers());

        inputPasswd.setVisible(!inputPasswd.isVisible());
        inputPasswd.setText("");
        groupUpdate.setVisible(!groupUpdate.isVisible());
        buttonModify.setVisible(!buttonModify.isVisible());
    }

    public void doUpdate() {
        var updated = new User(account);
        String aux = inputPasswd.getText();
        if (!aux.isBlank())
            updated.setPasswd(aux);

        aux = textFieldUsername.getText();
        if (!account.getUsername().equals(aux))
            updated.setUsername(aux);
        //Username

        aux = textFieldFirstname.getText();
        if (!account.getFirstname().equals(aux))
            updated.setFirstname(aux);
        //Firstname

        aux = textFieldLastName.getText();
        if (!account.getLastname().equals(aux))
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
        var user = tableNetwork.getSelectionModel().getSelectedItem();
        if (user == null) {
            errorShow("Unselected user!");
            return;
        }
        try {
            service.addRequest(account, user);
            condirmationShow("Request sent!");
            populateUsers();
            populateRequests();
        } catch (ServiceException e) {

            errorShow(e.getMessage());
        }
    }


    private Request<UUID> getSelectedRequest() {
        var sender = tableRequests.getSelectionModel().getSelectedItem().getKey();
        Request<UUID> found=new Request<>();
        try {
            return service.getRequest(sender, account);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public void acceptRequest() {
        try {
            var req=getSelectedRequest();
            service.addFriend(req);
            req.setStatus("accepted");
            service.updateRequest(req);
            populateFriends();
            populateRequests();
            populateUsers();
            condirmationShow("You are now friends with this user");
        } catch (ServiceException e) {
            e.printStackTrace();
            errorShow("Error on accepting request!");
        }
    }

    public void denyRequest() {
        try {
            var req=getSelectedRequest();
            req.setStatus("deny");
            service.updateRequest(req);
            populateFriends();
            populateRequests();
            populateUsers();
            condirmationShow("You successfully deny this friend request");
        } catch (ServiceException e) {
            e.printStackTrace();
            errorShow("Error on accepting request!");
        }

    }
    public void deleteFriend() {
        var friend=tableFriends.getSelectionModel().getSelectedItem().getKey();
        if(friend==null){
            errorShow("Unselected friend!");
            return;
        }
        try {
            service.deleteFriendship(account,friend);
            populateFriends();
            populateRequests();
            populateUsers();
            errorShow("You and"+friend.getUsername()+" are no longer friends now");

        } catch (ServiceException e) {
            errorShow(e.getMessage());
        }

    }
}
