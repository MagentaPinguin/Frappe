package application;

import application.controller.RegisterGui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.Service;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Service service=new Service();
        //" Service

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load()) ;

        RegisterGui ctrl=fxmlLoader.getController();
        ctrl.setService(service);

        stage.setTitle("FRAPPE");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}