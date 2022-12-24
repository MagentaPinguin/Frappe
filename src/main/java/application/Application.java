package application;

import application.controller.LoginGui;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import service.Service;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        Service service=new Service();
        //" Service

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load()) ;
        LoginGui ctrl=fxmlLoader.getController();
        ctrl.setService(service);
        stage.setTitle("FRAPPE");
        stage.setScene(scene);

        stage.getIcons().add(new Image("images/frappe_icon.png"));
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}