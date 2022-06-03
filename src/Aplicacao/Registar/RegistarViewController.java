package Aplicacao.Registar;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * FXML Controller class
 *
 * @author jpc
 */
public class RegistarViewController implements Initializable {

    @FXML
    TextField txfUsername;
    @FXML
    TextField txfemail;
    @FXML
    PasswordField txfPassword;
    @FXML
    PasswordField txfConfPassword;
    @FXML
    Label lblErro;
    @FXML
    Button cancelar;

    @FXML
    public void buttonRegistar(ActionEvent e) {
        String Username = txfUsername.getText();
        String email = txfemail.getText();
        String password = txfPassword.getText();
        String confpass = txfConfPassword.getText();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        if (Username.equals("") || email.equals("") | password.equals("") || confpass.equals("")) { // estar algum vazio
            lblErro.setText("Tem de ter todos os dados preenchidos!");
        } else {
            if (password.equals(confpass)) { // passwords iguais
                // Verificar se existe algum igual na base de dados

                try { // Vai buscar as passwords e verifica se existe alguma igual
                    Connection con = Config.Conect.getCon();
                    Statement st = con.createStatement();
                    String query = "SELECT * FROM ihc WHERE username = '" + Username + "' or email = '" + email + "';";
                    // CONTROLO System.out.println(query);
                    ResultSet rs = st.executeQuery(query);
                    if (rs.next()) {

                        JOptionPane.showMessageDialog(null, "Já existe um user com esses dados!", "Aviso", JOptionPane.WARNING_MESSAGE);

                    } else { // quer dizer que passou em todas as condições e assim podemos adicionar a base de dados!
                        query = "Insert into ihc(username, email, password) values('" + Username + "', '" + email + "', md5('" + password + "'));";
                        st.executeUpdate(query);

                        JOptionPane.showMessageDialog(null, "Foi registado com sucesso!", "Registo", JOptionPane.INFORMATION_MESSAGE);
                        cancelar.setText("Voltar");
                    }
                } catch (SQLException c) {
                    System.out.println(c.getMessage());
                }
            } else { // passwords diferentes

                JOptionPane.showMessageDialog(null, "Passwords Diferentes!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @FXML
    public void buttonCancelar(ActionEvent e) throws IOException {
        Parent RegistarParent = FXMLLoader.load(getClass().getResource("../Login/LoginView.fxml"));
        Scene RegistarScene = new Scene(RegistarParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(RegistarScene);
        window.setTitle("Login");
        window.show();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
