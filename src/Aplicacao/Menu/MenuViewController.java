package Aplicacao.Menu;

import Aplicacao.Diario.DiarioViewController;
import Aplicacao.Main;
import Config.User;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jpc
 */
public class MenuViewController implements Initializable {

    static User m = new User();
    @FXML
    Label lblUser;
    @FXML
    Label lblData;
    
    LocalDate ola = LocalDate.now();
    
    @FXML
    public void buttonSair(ActionEvent e) throws IOException {
        Parent MenuParent = FXMLLoader.load(getClass().getResource("../Login/LoginView.fxml"));
        Scene MenuScene = new Scene(MenuParent);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(MenuScene);
        window.show();
    }
    
    @FXML
    public void buttonNovaEntra(ActionEvent e) throws IOException {
                DiarioViewController.setUser(m);
        Parent MenuParent = FXMLLoader.load(getClass().getResource("../Diario/DiarioView.fxml"));
        Scene MenuScene = new Scene(MenuParent);
        
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(MenuScene);
        
        window.show();

    }
    
    @FXML
    public void buttonPesquisar(ActionEvent e) {
        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        lblData.setText(ola.toString());
        lblUser.setText("Bem-vindo " + m.getUsername());
        
    }

    public static void setUser(User m) {
        MenuViewController.m = m;
    }
    
}
