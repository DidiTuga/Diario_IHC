package Aplicacao.Menu;

import Aplicacao.Diario.DiarioViewController;
import Aplicacao.Pesquisar.PesquisarViewController;
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

    // Volta para a janela de login
    @FXML
    public void buttonSair(ActionEvent e) throws IOException {
        Parent MenuParent = FXMLLoader.load(getClass().getResource("../Login/LoginView.fxml"));
        Scene MenuScene = new Scene(MenuParent);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(MenuScene);
        window.setTitle("Login");
        window.show();

    }

    // Entra na janela diario
    @FXML
    public void buttonNovaEntra(ActionEvent e) throws IOException {
        DiarioViewController.setUser(m);
        Parent MenuParent = FXMLLoader.load(getClass().getResource("../Diario/DiarioView.fxml"));
        Scene MenuScene = new Scene(MenuParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(MenuScene);
        window.setTitle("Editar o diario de Hoje");

        window.show();

    }

    // entra na janela pesquisar
    @FXML
    public void buttonPesquisar(ActionEvent e) throws IOException {
        PesquisarViewController.setUser(m);
        Parent MenuParent = FXMLLoader.load(getClass().getResource("../Pesquisar/PesquisarView.fxml"));
        Scene MenuScene = new Scene(MenuParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(MenuScene);
        window.setTitle("Pesquisar Diario");
        PesquisarViewController.setUser(m);
        window.show();
    }

    /**
     * Mete as labels com a data e o nome do username
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        lblData.setText(ola.toString());
        lblUser.setText(m.getUsername());

    }
    // mete a variavel static user com o valor da "janela que o chama" 

    public static void setUser(User m) {
        MenuViewController.m = m;
    }

}
