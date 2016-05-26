/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
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
public class Character {
    
    private int id;
    private String name;
    private String race;
    private String description;
    private BufferedImage picture;
    private int level;

    public Character(int id, String name, String race, String description, int level, byte[] pic) throws IOException {
        
        ByteArrayInputStream imageInput = null;
        File blankImageFile = null;
        
        if (pic != null)
            imageInput = new ByteArrayInputStream(pic);
        else
            blankImageFile = new File("C:\\Users\\Oliver\\Desktop\\ProjetJavaDelahaye\\CodexJava\\image\\Blank.png");
        
        this.id = id;
        this.name = name;
        this.race = race;
        this.description = description;
        if (imageInput != null)
            this.picture = ImageIO.read(imageInput);
        else
            this.picture = ImageIO.read(blankImageFile);
        this.level = level;
    }
    

    
    public void setChanges(int id, String name, String race, int level, String description)
    {
        setId(id);
        setName(name);
        setRace(race);
        setLevel(level);
        setDescription(description);
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
