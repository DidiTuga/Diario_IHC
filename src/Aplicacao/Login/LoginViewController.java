package Aplicacao.Login;

import Aplicacao.Menu.MenuViewController;
import Config.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * FXML Controller class
 *
 * @author jpc
 */
public class LoginViewController implements Initializable {

    User m;
    @FXML
    TextField txfUsername;
    @FXML
    PasswordField txfPassword;
    @FXML
    Button btLogin;
    @FXML
    Button btRegistar;
    // abre a janela de registar

    @FXML
    public void buttonRegistar(ActionEvent e) throws IOException {
        Parent RegistarParent = FXMLLoader.load(getClass().getResource("../Registar/RegistarView.fxml"));
        Scene RegistarScene = new Scene(RegistarParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(RegistarScene);
        window.setTitle("Registar");
        window.show();
    }

    // Verifica se o user existe e se exister entra
    @FXML
    public void buttonLogin(ActionEvent e) throws IOException {
        String username = txfUsername.getText();
        String password = txfPassword.getText();
        try { // Vai buscar as passwords e verifica se existe alguma igual
            Connection con = Config.Conect.getCon();
            Statement st = con.createStatement();
            String query = "SELECT * FROM ihc WHERE username = '" + username + "' and password = md5('" + password + "');";
            // CONTROLO System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {

                m = new User(username, password);  // inicializa o user 
                MenuViewController.setUser(m);
                Parent MenuParent = FXMLLoader.load(getClass().getResource("../Menu/MenuView.fxml"));
                Scene MenuScene = new Scene(MenuParent);

                Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
                window.setScene(MenuScene);
                window.setTitle("Menu");
                window.show();
            } else {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
                JOptionPane.showMessageDialog(null, "Dados Inválidos", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException c) {
            System.out.println(c.getMessage());
        }
    }

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

}
