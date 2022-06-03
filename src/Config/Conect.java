
package Config;

import java.sql.*;


public class Conect {
    public static Connection getCon() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/login";
            Connection connection = DriverManager.getConnection(url,"root","zaXXfbWqVDCNE29");
            if(connection != null){
                System.out.println("Conectar ao servidor...");
            }
           return connection;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao conectar!");
            return null;
        }
    }
}
