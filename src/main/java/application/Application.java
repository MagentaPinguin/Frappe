package application;

import application.controller.RegisterGui;
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

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("register-gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load()) ;

        RegisterGui ctrl=fxmlLoader.getController();
        ctrl.setService(service);

        stage.setTitle("FRAPPE");
        stage.getIcons().add(new Image("images/frappe_icon.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}