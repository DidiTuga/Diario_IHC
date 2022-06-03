package Aplicacao.Diario;

import Aplicacao.Menu.MenuViewController;
import Config.User;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.security.Key;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DiarioViewController implements Initializable {
    static User m = new User();
    private final String algo = "Blowfish";
    private String path = "C:/Users/diogo/OneDrive - Universidade da Beira Interior/2ª Ano/2ºSemestre/Interacao Humana com o Computador/ProjetoIHC/src/Txt/"
            + m.getUsername()+"_"+LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-uuuu")) + ".txt";
    @FXML
    TextField txftitulo;
    @FXML
    HTMLEditor text;
    @FXML
    Label dtData;
    @FXML 
    Label lblNome;
    
    
    // Volta para a janela anterior
    @FXML
    public void buttonVoltar(ActionEvent e) throws IOException {
        Parent MenuParent = FXMLLoader.load(getClass().getResource("../Menu/MenuView.fxml"));
        Scene MenuScene = new Scene(MenuParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setScene(MenuScene);

        window.show();
        window.setTitle("Menu");
        MenuViewController.setUser(m);
    }
    // Vai buscar as strings do titulo e do Text e coloca os dentro de um ficheiro nomedoU_data.txt e depois encripta-os
    @FXML
    public void buttonCriar(ActionEvent e) throws IOException {

        String titulo = txftitulo.getText();

        String texto = titulo + "\n" + text.getHtmlText();
            File ficheiro = new File(path);
            //escreve no arquivo
            FileOutputStream fos = new FileOutputStream(ficheiro);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            Writer writer = new BufferedWriter(osw);
            writer.write(texto + "\n");

            writer.close();
            fos.close();
            osw.close();
            

        encriptar(m.getPassword(), path, path); // USAR A PASSWROD PARA encriptar
        buttonVoltar(e);
    }
    // mete a variavel static user com o valor da "janela que o chama" 
    public static void setUser(User m){
        DiarioViewController.m = m;
    }
    
    // Inicializa os parametros com o dia e o user e se existir ja um ficheiro do dia
    // vai desencriptar e vai o colocar no HTMLEditor e Titulo
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dtData.setText(LocalDate.now().toString());
        lblNome.setText(m.getUsername());
        File ficheiro = new File(path);
        boolean exist = ficheiro.exists();
        String texto = new String();
        if (exist) {
            desencriptar( m.getPassword(), path, path); // USAR A PASSWROD PARA desencriptar
            try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
                String line = new String();
                int i = 0;
                while ((line = br.readLine()) != null) {
                    if (i == 0) {
                        txftitulo.setText(line);
                        i++;
                    } else {
                        texto = texto + line;
                    }
                }
                text.setHtmlText(texto);
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            encriptar( m.getPassword(), path, path); // USAR A PASSWROD PARA encriptar
        }
    }
    
    //encripta o ficheirodeEntrada conforme a password e guarda no ficheiroSaida
       public void encriptar(String password, String ficheiroEntrada, String ficheiroSaida) {
        try {
            byte[] passwordInBytes = password.getBytes("ISO-8859-1");
            Key chave = new SecretKeySpec(passwordInBytes, algo);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, chave);

            byte[] textoClaro = abrirFichiero(ficheiroEntrada);
            byte[] textoCryptado = cipher.doFinal(textoClaro);
            salvarFichiero(ficheiroSaida, textoCryptado);
        } catch (Exception e) {
            System.out.println("Erro quando tentava encriptar os dados do fichiero de entrada" + e.getMessage());
        }
    }
    // desencripta o ficheirodeEntrada conforme a password e guarda no ficheiroSaida
    public void desencriptar(String password, String ficheiroEntrada, String ficheiroSaida) {
        try {
            byte[] passwordInBytes = password.getBytes("ISO-8859-1");
            Key clef = new SecretKeySpec(passwordInBytes, algo);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, clef);

            byte[] textoCryptado = abrirFichiero(ficheiroEntrada);
            byte[] textoClaro = cipher.doFinal(textoCryptado);
            salvarFichiero(ficheiroSaida, textoClaro);
        } catch (Exception e) {
            System.out.println("Erro quando tentava desencriptar os dados do ficheiro de entrada");
        }
    }
    // abre o ficheiro e passa o conteudo para Byte[]
    private byte[] abrirFichiero(String filename) {
        try {
            File ficheiro = new File(filename);
            byte[] resultado = new byte[(int) ficheiro.length()];
            FileInputStream in = new FileInputStream(filename);
            in.read(resultado);
            in.close();
            return resultado;
        } catch (Exception e) {
            System.out.println("Problema quando estava para ler o ficheiro : " + e.getMessage());
            return null;
        }
    }
    // guarda o ficheiro com bytes 
    private void salvarFichiero(String filename, byte[] data) {
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(data);
            out.close();
        } catch (Exception e) {
            System.out.println("Problema quando de estava para salvar o fichiero : " + e.getMessage());
        }
    }

}
