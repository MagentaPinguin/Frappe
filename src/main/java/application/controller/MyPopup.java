package application.controller;



import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MyPopup extends Dialog<Object> {

    public ImageView imageView;
    public TextArea txtShow;
    public MyPopup() {

    }
    public MyPopup(String s, String s1) {
        setImageView(s);
        setTxtShow(s1);
    }
    @FXML
    void initialize(){

    }

    public void setImageView(String url) {
       // imageView.setImage(new Image("images/"+url));
    }

    public void setTxtShow(String txt) {
        //txtShow.setText(txt);
    }


}
