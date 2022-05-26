package Aplicacao.Login;

import Aplicacao.Main;
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

/**
 * FXML Controller class
 *
 * @author jpc
 */
public class LoginViewController implements Initializable {
    private Main m;
    @FXML
    TextField txfUsername;
    @FXML
    PasswordField txfPassword;
    @FXML
    Label lblErro;
    @FXML
    Button btLogin;
    @FXML
    Button btRegistar;

    @FXML
    public void buttonRegistar(ActionEvent e) throws IOException {
        Parent RegistarParent = FXMLLoader.load(getClass().getResource("../Registar/RegistarView.fxml"));
        Scene RegistarScene = new Scene(RegistarParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(RegistarScene);
        window.show();
    }

    @FXML
    public void buttonLogin(ActionEvent e) throws IOException {
        String username = txfUsername.getText();
        String password = txfPassword.getText();
        try { // Vai buscar as passwords e verifica se existe alguma igual
            Connection con = Conectar.Conect.getCon();
            Statement st = con.createStatement();
            String query = "SELECT * FROM ihc WHERE username = '" + username + "' and password = md5('" + password + "');";
            // CONTROLO System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {

                m.setUser(username);
                Parent MenuParent = FXMLLoader.load(getClass().getResource("../Menu/MenuView.fxml"));
                Scene MenuScene = new Scene(MenuParent);
                
                Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
                window.setScene(MenuScene);
                
                window.show();
            } else {

                lblErro.setText("Os dados que inseriu não estão válidos!");
            }
        } catch (SQLException c) {
            System.out.println(c.getMessage());
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }
    	public void setMainApp(Main mainApp) {
		this.m = mainApp;
	}

}
