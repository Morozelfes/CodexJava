/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Oliver
 */
public class Personnage {
    
    private int id;
    private String name;
    private String race;
    private String description;
    private Blob picture;
    private int level;

    public Personnage(int id, String name, String race, String description, Blob picture, int level) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.description = description;
        this.picture = picture;
        this.level = level;
    }
    
    
    public static ArrayList<Personnage> findAll() throws ClassNotFoundException, SQLException
    {
        ArrayList<Personnage> personnages = new ArrayList();
        
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Oliver\\Desktop\\ProjetJavaDelahaye\\CodexJava\\codexRDA.sqlite");
        
        Statement statement = connection.createStatement();
        
        ResultSet resultSet = statement.executeQuery("SELECT * FROM personnnages ORDER BY name;");
        
        while(resultSet.next())
        {
            int id = -1;
            String name = "Not found";
            String race = null;
            String description = null;
            Blob picture = null;
            int level;
            
            id = resultSet.getInt("Id");
            name = resultSet.getString("Name");
            race = resultSet.getString("Race");
            description = resultSet.getString("Description");
            picture = resultSet.getBlob("Picture");
            level = resultSet.getInt("Level");
            
            personnages.add(new Personnage(id, name, race, description, picture, level));
        }
        
        return personnages;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String decription) {
        this.description = decription;
    }

    public Blob getPicture() {
        return picture;
    }

    public void setPicture(Blob picture) {
        this.picture = picture;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
    
    
    
}
