/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Blob;

/**
 *
 * @author Oliver
 */
public class Personnage {
    
    private int id;
    private String name;
    private String race;
    private String decription;
    private Blob picture;
    private int level;

    public Personnage(int id, String name, String race, String decription, Blob picture, int level) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.decription = decription;
        this.picture = picture;
        this.level = level;
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

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
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
