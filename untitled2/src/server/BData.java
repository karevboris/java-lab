package server;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by пк1 on 08.05.2017.
 */
public class BData {
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/test";
    private String user = "root";
    private String password = "root";

    public BData() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = null;
            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BData.class.getName()).log(Level.SEVERE, "Can't find driver", ex);
        }
    }

    public void Insert(String userName, String password, String shipName) {
        String insertTableSQL = "INSERT INTO ships "
                + "VALUES ('"+userName+"', '"+password+"', '"+shipName+"')";
        try {
            Statement statement = connection.createStatement();
            statement.execute(insertTableSQL);
            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean checkUser(String userName){
        int check = 0;
        try {
            String checkName = "SELECT shipName FROM ships WHERE userName ='" + userName + "'";
            PreparedStatement statement = connection.prepareStatement(checkName);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                check++;
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return check==0;
    }

    public boolean checkLogIn(String userName, String password){
        int check = 0;
        try {
            String checkLogIn = "SELECT shipName FROM ships WHERE userName ='" + userName + "' AND password = '" + password + "'";
            PreparedStatement statement = connection.prepareStatement(checkLogIn);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                check++;
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return check==1;
    }
    public void setShipName(String userName, String shipName) {
        try {
            String checkLogIn = "UPDATE ships SET shipName = '" + shipName + "' WHERE userName ='" + userName + "'";
            PreparedStatement statement = connection.prepareStatement(checkLogIn);
            statement.execute(checkLogIn);
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
