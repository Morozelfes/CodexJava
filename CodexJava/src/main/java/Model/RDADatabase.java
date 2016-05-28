/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

    
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Oliver\\Desktop\\ProjetJavaDelahaye\\CodexJava\\RDACodex.sqlite");
    }
    
    
    
    
    public ArrayList<Character> findAll() throws ClassNotFoundException, SQLException, IOException
    {
        ArrayList<Character> personnages = new ArrayList();  
        connection = getConnection();       
        Statement statement = connection.createStatement();
        
        ResultSet resultSet = statement.executeQuery("SELECT Id, Name, Race, Description, Picture, Lvl FROM personnages ORDER BY name;");
        
        while(resultSet.next())
        {
            int id = -1;
            String name = "Not found";
            String race = null;
            String description = null;
            byte[] picture = null;
            int level = -1;
            
            id = resultSet.getInt("Id");
            name = resultSet.getString("Name");
            race = resultSet.getString("Race");
            description = resultSet.getString("Description");
            picture = resultSet.getBytes("Picture");
            level = resultSet.getInt("Lvl");
            
            personnages.add(new Character(id, name, race, description, level, picture));
        }
        
        resultSet.close();
        statement.close();
        connection.close();
        
        return personnages;
    }
    
    
    public void updateCharacter(Character p) throws ClassNotFoundException, SQLException
    {
        connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE personnages SET Name = ?, Race = ?, Lvl = ?, Description = ? WHERE Id = ?");
        
        statement.setString(1, p.getName());
        statement.setString(2, p.getRace());
        statement.setInt(3, p.getLevel());
        statement.setString(4, p.getDescription());
        statement.setInt(5, p.getId());
        
        statement.executeUpdate();
        
        statement.close();
        connection.close();
    }
    
    //A MODIFIER
    public void addCharacter(Character p) throws SQLException, ClassNotFoundException
    {
        connection = getConnection();
        
        PreparedStatement statement = connection.prepareStatement("INSERT INTO personnages (Id, Name, Race, Description, Lvl) VALUES (?, ?, ?, ?, ?);");
        statement.setInt(1, p.getId());
        statement.setString(2, p.getName());
        statement.setString(3, p.getRace());
        statement.setString(4, p.getDescription());
        statement.setInt(5, p.getLevel());
        
        statement.executeUpdate();
        
        statement.close();
        connection.close();
        
    }
    
    
    public void deleteCharacter(Character character) throws SQLException
    {
        connection = getConnection();
        
        PreparedStatement statement = connection.prepareStatement("DELETE FROM personnages WHERE Id = ?;");
        statement.setInt(1, character.getId());
        statement.executeUpdate();
                
        statement.close();
        connection.close();
    }
    
    
}
