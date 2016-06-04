/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.imageio.ImageIO;
//import java.util.Base64;

/**
 * This is our main DAO (and only given the fact that
 * 'characters' is our only database table.
 * @author Oliver
 */
public class RDADatabase {

    private Connection connection = null;
    private static RDADatabase INSTANCE = null;

    private RDADatabase() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    /**
     * As a DAO this class is obviously Singleton.
     * @return Only nstance of this class.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static RDADatabase getInstance() throws SQLException, ClassNotFoundException {
        if (INSTANCE == null) {
            INSTANCE = new RDADatabase();
        }
        return INSTANCE;
    }

    /**
     * 
     * @return Connection linked to the database. Sufficient to open connection.
     * @throws SQLException 
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:..\\RDACodex.sqlite");
    }

    /**
     *
     * @return List of all characters in database.
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public ArrayList<Character> findAll() throws ClassNotFoundException, SQLException, IOException {
        ArrayList<Character> characters = new ArrayList();
        connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT Id, Name, Race, Description, Picture, Lvl FROM personnages ORDER BY name;");

        while (resultSet.next()) {
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

            characters.add(new Character(id, name, race, description, level, picture));
        }

        resultSet.close();
        statement.close();
        connection.close();

        return characters;
    }

    /**
     * Searches for {@code character} in database and updates it's record
     * with information provided by the characters instance attributes.
     * @param character
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public void updateCharacter(Character character) throws ClassNotFoundException, SQLException, IOException {
        ByteArrayOutputStream picStream = new ByteArrayOutputStream();
        ImageIO.write(character.getPicture(), "jpg", picStream);
        byte[] pictureByte = picStream.toByteArray();

        connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE personnages SET Name = ?, Race = ?, Lvl = ?, Description = ?, Picture = ? WHERE Id = ?");

        statement.setString(1, character.getName());
        statement.setString(2, character.getRace());
        statement.setInt(3, character.getLevel());
        statement.setString(4, character.getDescription());
        statement.setInt(5, character.getId());
        statement.setBytes(6, pictureByte);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    /**
     * Adds {@code character} to database.
     * @param character
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void addCharacter(Character character) throws SQLException, ClassNotFoundException {
        connection = getConnection();

        PreparedStatement statement = connection.prepareStatement("INSERT INTO personnages (Id, Name, Race, Description, Lvl) VALUES (?, ?, ?, ?, ?);");
        statement.setInt(1, character.getId());
        statement.setString(2, character.getName());
        statement.setString(3, character.getRace());
        statement.setString(4, character.getDescription());
        statement.setInt(5, character.getLevel());

        statement.executeUpdate();

        statement.close();
        connection.close();

    }

    /**
     * Deletes {@code character} to database.
     * @param character
     * @throws SQLException
     */
    public void deleteCharacter(Character character) throws SQLException {
        connection = getConnection();

        PreparedStatement statement = connection.prepareStatement("DELETE FROM personnages WHERE Id = ?;");
        statement.setInt(1, character.getId());
        statement.executeUpdate();

        statement.close();
        connection.close();
    }

}
