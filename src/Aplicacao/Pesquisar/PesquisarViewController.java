package Aplicacao.Pesquisar;

import Aplicacao.Menu.MenuViewController;
import Config.User;
import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.Units;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.jsoup.Jsoup;

public class PesquisarViewController implements Initializable {

    static User m = new User();
    private final String algo = "Blowfish";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu");
    private String path = "C:/Users/diogo/OneDrive - Universidade da Beira Interior/2ª Ano/2ºSemestre/Interacao Humana com o Computador/ProjetoIHC/src/Txt/";
    @FXML
    VBox x;
    @FXML
    TextField txftitulo;
    @FXML
    HTMLEditor text;
    @FXML
    DatePicker dtData;
    @FXML
    DatePicker dtData_ate;
    @FXML
    TextField txfpesquisa;
    @FXML
    ComboBox filtro;
    @FXML
    ListView<String> listagem = new ListView<String>();

    String valorUltimo = null;
    ArrayList<String> ficheiros_u = new ArrayList<>();
    ArrayList<String> titulo_u = new ArrayList<>();
    ArrayList<String> texto_u = new ArrayList<>();
    
    // volta para a janela do menu
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
    
    // Vai ver nos ficheiros existentes do user os que estao dentro da data e os que tem a palavra 
    @FXML
    public void buttonPesquisar(ActionEvent e) throws IOException {

        LocalDate datai = dtData.getValue();
        LocalDate dataf = dtData_ate.getValue();
        if (datai != null && dataf != null) {

            ObservableList<String> items = FXCollections.observableArrayList();
            String palavra = txfpesquisa.getText();

            for (int i = 0; i < ficheiros_u.size(); i++) {
                LocalDate f = LocalDate.parse(ficheiros_u.get(i).substring(0, ficheiros_u.get(i).length() - 4), formatter);
                if ((f.isBefore(dataf) || f.isEqual(dataf)) && (f.isAfter(datai) || f.isEqual(datai))) {
                    if ((texto_u.get(i).contains(palavra) || titulo_u.get(i).contains(palavra))) {
                        items.add(ficheiros_u.get(i));
                    } else if (palavra.equals("")) {
                        items.add(ficheiros_u.get(i));
                    }

                }

            }

            listagem.setItems(items);
            ordenar(e);
        } else {
            dtData_ate.setValue(LocalDate.now());
            dtData.setValue(LocalDate.now());
        }
    }
    // Basicamente mete o conteudo do ficheiro dentro de uma label e devolve a label
    public Node getPrintableText() {
        Label texto = new Label();
        StringBuilder stringBuilder = new StringBuilder();
        if (valorUltimo != null) {
            String teste = null;
            for (int i = 0; i < ficheiros_u.size(); i++) {
                if (ficheiros_u.get(i).equalsIgnoreCase(valorUltimo)) {
                    teste = Jsoup.parse(texto_u.get(i)).html();
                    stringBuilder.append("Titulo: " + titulo_u.get(i) + "\n\nConteudo:");
                }
            }
            String teste1 = Jsoup.parse(teste).wholeText();
            stringBuilder.append(teste1);
            texto.setText(stringBuilder.toString());
            return texto;
        } else {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
            JOptionPane.showMessageDialog(null, "Ficheiro nao selecionado: Tem que selecionar um ficheiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }
    // se a label nao for null imprime a label
    public void handle(ActionEvent e) {
        Node node = getPrintableText();
        if (node != null) {
            PrinterJob printerJob = PrinterJob.createPrinterJob();

            if (printerJob != null) {
                boolean success = printerJob.printPage(node);
                if (success) {
                    printerJob.endJob();
                }
            }
        }
    }
    
    // ordernar do mais antigo para o mais recente e virse versa
    @FXML
    public void ordenar(ActionEvent e) {

        ObservableList<String> d = listagem.getItems();
        int tam = d.size();
        String n = (String) filtro.getValue();
        if (n != null) {
            if (n.equals("Recente para o antigo") && tam > 1) {
                int i, j;
                String aux;
                for (i = 0; i < tam; i++) {
                    for (j = 1; j < tam; j++) {
                        LocalDate f = LocalDate.parse(d.get(j).substring(0, d.get(j).length() - 4), formatter);
                        LocalDate f1 = LocalDate.parse(d.get(j - 1).substring(0, d.get(j).length() - 4), formatter);
                        if (f.isAfter(f1)) {
                            aux = d.get(j - 1);
                            d.set(j - 1, d.get(j));
                            d.set(j, aux);
                        }
                    }
                }

            } else if (tam > 1) {
                int i, j;
                String aux;
                for (i = 0; i < tam; i++) {
                    for (j = 1; j < tam; j++) {
                        LocalDate f = LocalDate.parse(d.get(j).substring(0, d.get(j).length() - 4), formatter);
                        LocalDate f1 = LocalDate.parse(d.get(j - 1).substring(0, d.get(j).length() - 4), formatter);
                        if (f.isBefore(f1)) {
                            aux = d.get(j - 1);
                            d.set(j - 1, d.get(j));
                            d.set(j, aux);
                        }
                    }
                }
            }
            listagem.setItems(d);
        }
    }
    // inicializa a listview e a combobox
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ObservableList<String> items = FXCollections.observableArrayList();
        filtro.setPromptText("Ordem de Pesquisa");
        File ficheiros = new File(path);
        for (File file : ficheiros.listFiles()) {
            String nome_file = "";
            nome_file = file.getName();
            String[] split = nome_file.split("_");
            if (split[0].equals(m.getUsername())) {
                items.add(split[1]);
                atualizarTexto(split[1]);
                ficheiros_u.add(split[1]);
            }
        }
        listagem.setItems(items);
        ObservableList<String> filtros
                = FXCollections.observableArrayList(
                        "Antigo para o recente",
                        "Recente para o antigo"
                );
        filtro.setItems(filtros);

        // Quando seleciona uma cena da lista
        listagem.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                for (int i = 0; i < ficheiros_u.size(); i++) {
                    if (ficheiros_u.get(i).equals(newValue)) {
                        valorUltimo = newValue;
                        text.setHtmlText(texto_u.get(i));
                        txftitulo.setText(titulo_u.get(i));

                    }
                }

            }
        });
        txftitulo.setEditable(false);

    }
    // vai meter os ficheiros do user para arraylist
    public void atualizarTexto(String nomeFicheiro) {
        String path_ficheiro = path + m.getUsername() + "_" + nomeFicheiro;
        String texto = "";
        File diario = new File(path_ficheiro);
        desencriptar(m.getPassword(), path_ficheiro, path_ficheiro); // USAR A PASSWROD PARA desencriptar
        try (BufferedReader br = new BufferedReader(new FileReader(diario))) {
            String line = new String();
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i == 0) {
                    titulo_u.add(line);
                    i++;
                } else {
                    texto = texto + line;
                }
            }
            texto_u.add(texto);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        encriptar(m.getPassword(), path_ficheiro, path_ficheiro); // USAR A PASSWROD PARA encriptar
    }

    // Para passar o user de janela em janela
    public static void setUser(User m) {
        PesquisarViewController.m = m;
    }

    // FUNÇOES DE ENCRIPTAR O FICHEIRO
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
