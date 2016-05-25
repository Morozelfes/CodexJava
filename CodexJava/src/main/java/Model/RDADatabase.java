/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Oliver
 */
public class RDADatabase{
    
    private Connection connection = null;
    private static RDADatabase INSTANCE = null;
    
    private RDADatabase() throws SQLException, ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
    }
    
    public static RDADatabase getInstance() throws SQLException, ClassNotFoundException
    {
        if (INSTANCE == null)
            INSTANCE = new RDADatabase();
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Oliver\\Desktop\\ProjetJavaDelahaye\\CodexJava\\RDACodex.sqlite");
    }
    
    
    
}
