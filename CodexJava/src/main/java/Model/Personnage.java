/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Oliver
 */
public class Personnage {
    
    private int id;
    private String name;
    private String race;
    private String description;
    private BufferedImage picture;
    private int level;

    public Personnage(int id, String name, String race, String description, int level, byte[] pic) throws IOException {
        
        ByteArrayInputStream imageInput = new ByteArrayInputStream(pic);
        
        this.id = id;
        this.name = name;
        this.race = race;
        this.description = description;
        this.picture = ImageIO.read(imageInput);
        this.level = level;
    }
    
    
    public static ArrayList<Personnage> findAll() throws ClassNotFoundException, SQLException, IOException
    {
        ArrayList<Personnage> personnages = new ArrayList();
   
        Connection connection = RDADatabase.getInstance().getConnection();
        
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
            
            personnages.add(new Personnage(id, name, race, description, level, picture));
        }
        
        resultSet.close();
        statement.close();
        connection.close();
        
        return personnages;
    }
    
    
    
    public static void updatePerso(Personnage p) throws ClassNotFoundException, SQLException
    {
        Connection connection = RDADatabase.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE personnages SET Name = ?, Race = ?, Lvl = ?, Description = ? WHERE Id = ?");
        
        statement.setString(1, p.name);
        statement.setString(2, p.race);
        statement.setInt(3, p.level);
        statement.setString(4, p.description);
        statement.setInt(5, p.id);
        
        statement.executeUpdate();
        
        statement.close();
        connection.close();
    }
    
    
    public void setChanges(int id, String name, String race, int level, String description)
    {
        setId(id);
        setName(name);
        setRace(race);
        setLevel(level);
        setDescription(description);
    }
    
    //A MODIFIER
    public static void addCharacter(Personnage p) throws SQLException, ClassNotFoundException
    {
        
        Connection connection = RDADatabase.getInstance().getConnection();
        
        PreparedStatement statement = connection.prepareStatement("INSERT INTO personnages VALUES ?, ?, ?, ?, ?");
        statement.setInt(1, p.id);
        statement.setString(2, p.name);
        statement.setString(3, p.race);
        statement.setInt(4, p.level);
        statement.setString(5, p.description);
        
        statement.executeUpdate();
        
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

    public BufferedImage getPicture() {
        return picture;
    }

    public void setPicture(BufferedImage picture) {
        this.picture = picture;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return name + ", level = " + level;
    }
    
    
    
    
}
