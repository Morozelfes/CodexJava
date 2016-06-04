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
import javax.imageio.ImageIO;

/**
 * Character model class.
 * @author Oliver
 */
public class Character {

    private int id;
    private String name;
    private String race;
    private String description;
    private BufferedImage picture;
    private int level;

    /**
     * 
     * @param id
     * @param name
     * @param race
     * @param description
     * @param level
     * @param pic
     * @throws IOException
     */
    public Character(int id, String name, String race, String description, int level, byte[] pic) throws IOException {

        ByteArrayInputStream imageInput = null;
        File blankImageFile = null;

        this.id = id;
        this.name = name;
        this.race = race;
        this.description = description;
        this.level = level;

        if (pic != null) {
            imageInput = new ByteArrayInputStream(pic);
            this.picture = ImageIO.read(imageInput);
        } else {
            blankImageFile = new File("..\\image\\Blank.png");
            this.picture = ImageIO.read(blankImageFile);
        }
    }

    /**
     * Calls setters for name, race, level and description for character instance.
     * @param name
     * @param race
     * @param level
     * @param description
     */
    public void setChanges(String name, String race, int level, String description) {
        setName(name);
        setRace(race);
        setLevel(level);
        setDescription(description);
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getRace() {
        return race;
    }

    /**
     *
     * @param race
     */
    public void setRace(String race) {
        this.race = race;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param decription
     */
    public void setDescription(String decription) {
        this.description = decription;
    }

    /**
     *
     * @return
     */
    public BufferedImage getPicture() {
        return picture;
    }

    /**
     *
     * @param picture
     */
    public void setPicture(BufferedImage picture) {
        this.picture = picture;
    }

    /**
     *
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     *
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return name + ", level = " + level;
    }

}
