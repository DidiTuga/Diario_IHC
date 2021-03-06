package Aplicacao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author jpc
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("Login/LoginView.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Login Diário");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }

}
