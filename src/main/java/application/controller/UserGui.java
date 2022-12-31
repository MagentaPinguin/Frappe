package application.controller;

import anexe.Constants;
import anexe.Observer;
import application.Application;
import domain.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import javafx.stage.Stage;
import javafx.util.Pair;
import service.Service;
import service.ServiceException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;


import static anexe.Constants.FORMATTER;


public class UserGui extends AbstractController implements Observer {


    private Chatroom<UUID> openedChatroom;
    private Service service;
    private User account;
    //" Account data

    public AnchorPane paneChatBackground;
    public AnchorPane anchorPane;

    public TextArea areaMessages;
    //"Layout

    //"Pannels

    public TextField textFieldUsername;
    public TextField textFieldFirstname;
    public TextField textFieldLastName;
    //! User data
    public PasswordField inputPasswd;

    public TextField fieldInputMessage;
    public TextField searchbar;
    //"Fields


    public Tab tabFriends;
    public Tab tabChat;
    public Tab tabRequest;
    public Tab tabProfile;
    //"Tabs


    public Circle circlePicture;
    //"Shape



    public Button buttonEexitChatroom;
    public Button buttonSendMessage;
    public Button buttonSaveModify;
    public Button buttonCancel;
    public Button buttonModify;
    public Button buttonRequest;
    public Button buttonAcceptRequest;
    public Button buttonDenyRequest;
    public Button buttonDeleteFriend;
    public Button buttonChatCreate;
    public Button buttonOpenChat;

    public Button buttonJoinChat;
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

    public TableColumn<Chatroom<UUID>,String> columnChatroomMember;
    public TableView<Chatroom<UUID>> tableChatroom;
    public TableColumn<Chatroom<UUID>,String> columnChatroomName;
    public TableColumn<Chatroom<UUID>,String> columnChatroomType;
    ObservableList<Chatroom<UUID>> chatroomModel = FXCollections.observableArrayList();
    //"Table chatroom

    private void populateUsers() {
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

    private void populateRequests() {
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

    private void populateFriends() {
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
    private void populateChatrooms() {

        List<Chatroom<UUID>> list = null;
        try {
            list = service.getAllChatroom();
        } catch (ServiceException e) {
            errorShow(e.getMessage());
            return;
        }
        chatroomModel.setAll(list);

    }
    private void populateWithMessages() {

        try {

            var messages = service.getAllMessagesFor(openedChatroom);
            messages.sort(Comparator.comparing(Message::getData));
            var finale =messages.stream().map(e->e.getContext()+" |"+service.getUserById(e.getSender()).getUsername()+"\n").collect(Collectors.joining());
            areaMessages.setText(finale);

        } catch (ServiceException e) {
            errorShow(e.getMessage());
        }

    }

    @FXML
    void initialize() {
        paneChatBackground.setVisible(false);
        tabProfile.setGraphic(new ImageView("images/account.png"));
        tabFriends.setGraphic(new ImageView("images/friends.png"));
        tabRequest.setGraphic(new ImageView("images/request.png"));
        tabChat.setGraphic(new ImageView("images/chat.png"));


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

        columnChatroomName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnChatroomType.setCellValueFactory(e-> (e.getValue().getType()==0)? new SimpleStringProperty("Public"):new SimpleStringProperty("Private"));
        columnChatroomMember.setCellValueFactory(e->(e.getValue().getParticipants().contains(account.getId()))?new SimpleStringProperty("Yes"):new SimpleStringProperty("No") );
        tableChatroom.setItems(chatroomModel);
        //Table chatrooms
        areaMessages.setEditable(false);
        //MessageArea

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
        populateChatrooms();

    }

    public void setService(Service service) {
        this.service = service;
        this.service.register(this);

    }

    private void uploadUserData() {
        textFieldUsername.setText(account.getUsername());
        textFieldFirstname.setText(account.getFirstname());
        textFieldLastName.setText(account.getLastname());
        circlePicture.setFill(new ImagePattern(new Image(account.getPictureReference())));

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
        service.notifyObservers();
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
            service.updateUser(updated);
            confirmationShow("Update was a success!");
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
            service.sentRequest(account, user);
            confirmationShow("Request sent!");
            service.notifyObservers();
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
            service.addFriendship(req);
            req.setStatus("accepted");
            service.updateRequest(req);
            service.notifyObservers();
            confirmationShow("You are now friends with this user");
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
            service.notifyObservers();
            confirmationShow("You successfully deny this friend request");
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
            service.notifyObservers();
            errorShow("You and "+friend.getUsername()+" are no longer friends now");

        } catch (ServiceException e) {
            errorShow(e.getMessage());
        }

    }
    public void closeChatPane(MouseEvent mouseEvent) {
        paneChatBackground.setVisible(false);
        
    }

    public void openSelectedChat()
    {
        openedChatroom=tableChatroom.getSelectionModel().getSelectedItem();
        if(openedChatroom==null){
            errorShow("Select a chatroom!");
            return;
        };
        populateWithMessages();
        paneChatBackground.setVisible(true);
    }


    public void createChat() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("create-chat-gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load()) ;
        CreateChatGui ctrl=fxmlLoader.getController();
        ctrl.setUp(service,account);
        Stage stage=new Stage();
        stage.setTitle("FRAPPE");
        stage.setScene(scene);
        stage.getIcons().add(new Image("images/frappe_icon.png"));
        stage.setResizable(false);
        stage.show();

    }

    public void joinChatroom(ActionEvent actionEvent){
        var selected= tableChatroom.getSelectionModel().getSelectedItem();
        if(selected==null) {
            errorShow("Select a chatroom to join!");
            return;
        }
        if(selected.getType()==1){
            TextInputDialog passwd=new TextInputDialog();
            passwd.setTitle("Enter Chatroom password");
            passwd.setGraphic(new ImageView(new Image("images/img_lock.jpg")));
            var entered=passwd.showAndWait();

            if(!selected.getPasswd().equals(entered.get())){
                errorShow("Incorrect password");
                return;
            }
        }

        try {
            service.addMemberToChat(selected,account);
            confirmationShow("You was accepted to "+selected.getName());
            service.notifyObservers();
        } catch (ServiceException e) {
            errorShow(e.getMessage());
        }
    }

    public void addMessage() {

        var message=fieldInputMessage.getText();
        try {
            service.addMessage(openedChatroom.getId(),account.getId(),message);
            service.notifyObservers();
        } catch (ServiceException e) {
           errorShow(e.getMessage());
        }

    }

    @Override
    public void update() {
        if(openedChatroom!=null)
        populateWithMessages();
        populateChatrooms();
        populateRequests();
        populateUsers();
        populateFriends();

    }

    public void exitChatroom(ActionEvent actionEvent) {
        if(openedChatroom==null)
            errorShow("Select a chatroom!");

        if(openedChatroom.getParticipants().size()==1){
            ChoiceDialog<String> choice=new ChoiceDialog<>("No","Yes");
            var x=choice.showAndWait();
            if(x.isPresent() && x.get().equals("Yes")){
                service.deleteChat(openedChatroom);
                confirmationShow("You exit "+openedChatroom.getName()+", and the chatroom got deleted.");
            }

        } else
            try {
                service.exitChat(openedChatroom,account);
                confirmationShow("You exit "+openedChatroom.getName());

            } catch (ServiceException e) {
                throw new RuntimeException(e);
            }
            service.notifyObservers();

    }
}
