package application.controller;

import domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.Service;
import service.ServiceException;

import java.util.ArrayList;
import java.util.List;

public class CreateChatGui extends AbstractController{

    public PasswordField fieldPassword;
    public TextField fieldChatroomName;
    //"Fields
    public Button buttonDone;
    public Button buttonAbort;
    public RadioButton buttonRadioPrivate;
    //" Buttons

    private Service service;
    private User account;

    @FXML
    void initialize(){
            fieldPassword.setDisable(true);
            buttonRadioPrivate.pressedProperty().addListener(e->checkPressed());
    }
    private void checkPressed(){
        buttonRadioPrivate.setSelected(!buttonRadioPrivate.isSelected());
        fieldPassword.setDisable(buttonRadioPrivate.isSelected());
    }

    public void setUp(Service s, User u){
        this.service=s;
        this.account=u;
    };
    void closeStage(ActionEvent actionEvent){
        Node source=(Node)actionEvent.getSource();
        Stage crtStage=(Stage) source.getScene().getWindow();
        crtStage.close();
    }

    public void buttonCreateChatroom(ActionEvent actionEvent) {

        List<String> arg=new ArrayList<>();
        arg.add(fieldChatroomName.getText());
        if(buttonRadioPrivate.isSelected()){
            arg.add("1");
            arg.add(fieldPassword.getText());
        }
        else{
            arg.add("0");
        }
        try {
            service.createChatRoom(arg,account);
            confirmationShow("Chatroom created!");
        } catch (ServiceException e) {
            errorShow(e.getMessage());
        }

        closeStage(actionEvent);
    }

    public void buttonAbortChatroom(ActionEvent actionEvent) {


        closeStage(actionEvent);
    }
}
