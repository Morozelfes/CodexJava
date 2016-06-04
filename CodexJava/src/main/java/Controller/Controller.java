/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Character;
import Model.RDADatabase;
import Views.RDAView;
import Views.AddCharacterFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.IOException;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Programs main controller.
 * @author Oliver
 */
public class Controller {

    private SecurityController security;

    private ListSelectionListener characterSelectionListener;
    private ActionListener updateCharacterListener, addCharacterListener, cancelAddListener, confirmAddListener, deleteCharacterListener, addPictureListener;

    private AddCharacterFrame newCharacterFrame;
    private final RDAView mainFrame;
    private JFileChooser pictureChooser;

    private final RDADatabase dataBase;

    ArrayList<Character> characters;
    DefaultListModel<Character> characterModel;
    private Character selectedCharacter;

    /**
     * Constructs our main view and affects our DAO instance to {@code dataBase} attribute.
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public Controller() throws ClassNotFoundException, SQLException, IOException {
        mainFrame = new RDAView();
        dataBase = RDADatabase.getInstance();

        initMainFrame();

    }

    /**
     * This method initiates our main frames components..
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public void initMainFrame() throws ClassNotFoundException, SQLException, IOException {
        refreshList();

        initListeners();

        mainFrame.getCharacterList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainFrame.getCharacterList().setLayoutOrientation(JList.VERTICAL);

        mainFrame.getCharacterList().setVisibleRowCount(10);
        mainFrame.getCharacterList().setVisible(true);

        mainFrame.setVisible(true);

    }

    /**
     * Creates listeners instances and adds them to correct components on main frame.
     */
    public void initListeners() {
        characterSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
                if (!lse.getValueIsAdjusting()) {
                    characterSelection();
                }
            }
        };

        updateCharacterListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    updateCharacter(selectedCharacter);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        addCharacterListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                openAddForm();
            }
        };

        deleteCharacterListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    deleteCharacter(selectedCharacter);
                } catch (SQLException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        addPictureListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    addPicture(selectedCharacter);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        mainFrame.getCharacterList().addListSelectionListener(characterSelectionListener);
        mainFrame.getSaveChange().addActionListener(updateCharacterListener);
        mainFrame.getNewEntry().addActionListener(addCharacterListener);
        mainFrame.getDelete().addActionListener(deleteCharacterListener);
        mainFrame.getChangeImage().addActionListener(addPictureListener);

    }

    /**
     * Triggered on {@code updateCharacterListener}. Calls {@code character}s {@code setChanges} method and
     * DAOs {@code updateCharacter} method.
     * @param character
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public void updateCharacter(Character character) throws ClassNotFoundException, SQLException, IOException {
        String name, race, unknown = "Inconnu";
        int level;

        if (selectedCharacter == null) {
            JOptionPane.showMessageDialog(mainFrame, "Veuillez selectionner un personnage a modifier.", "Aucun personnage selectionne", JOptionPane.ERROR_MESSAGE);
            return;
        }

        name = mainFrame.getTextName().getText();
        if ("".equals(name)) {
            name = unknown;
        }

        race = mainFrame.getTextRace().getText();
        if ("".equals(race)) {
            race = unknown;
        }

        if (!"".equals(mainFrame.getTextLevel().getText())) {
            level = Integer.parseInt(mainFrame.getTextLevel().getText());
        } else {
            level = 0;
        }

        character.setChanges(name, race, level, mainFrame.getTextDescription().getText());
        dataBase.updateCharacter(character);

    }

    /**
     * Triggered on {@code characterSelectionListener}. Updates texfields and picture panel 
     * to selected characters information.
     */
    public void characterSelection() {
        try {
            selectedCharacter = mainFrame.getCharacterList().getSelectedValue();

            mainFrame.getTextName().setText(selectedCharacter.getName());
            mainFrame.getTextRace().setText(selectedCharacter.getRace());
            mainFrame.getTextLevel().setText(Integer.toString(selectedCharacter.getLevel()));
            mainFrame.getTextDescription().setText(selectedCharacter.getDescription());

            mainFrame.getImagePanel().setImage(selectedCharacter.getPicture());
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException cought at character selection.");
        }

    }

    /**
     * Triggered on {@code addCharacterListener}. Opens new instance of {@code addCharacterFrame}
     * and initiates components and listeners of that frame.
     */
    public void openAddForm() {
        newCharacterFrame = new AddCharacterFrame();

        confirmAddListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    confirmAddCharacter();
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        cancelAddListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                newCharacterFrame.setVisible(false);
            }
        };

        newCharacterFrame.getConfirmButton().addActionListener(confirmAddListener);
        newCharacterFrame.getCancelButton().addActionListener(cancelAddListener);

        newCharacterFrame.setVisible(true);
    }

    /**
     * Triggered by {@code confirmAddListener}. Checks if textfields are filled out
     * and calls our DAOs instance {@link Model.RDADatabase.addCharacter} method. Also adds
     * new entry in {@link Views.RDAView.charcterList}.
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void confirmAddCharacter() throws IOException, SQLException, ClassNotFoundException {
        String unknown = "Inconnu", name, race;
        int level;

        name = newCharacterFrame.getNameField().getText();
        if ("".equals(name)) {
            name = unknown;
        }

        race = newCharacterFrame.getRaceField().getText();
        if ("".equals(race)) {
            race = unknown;
        }

        if (!"".equals(newCharacterFrame.getLevelField().getText())) {
            level = Integer.parseInt(newCharacterFrame.getLevelField().getText());
        } else {
            level = 0;
        }

        Character newCharacter = new Character(characters.size() + 1, name, race, newCharacterFrame.getDescriptionField().getText(), level, null);
        characters.add(newCharacter);

        dataBase.addCharacter(newCharacter);
        characterModel.add(characters.size() - 1, newCharacter);

        newCharacterFrame.setVisible(false);
    }

    /**
     * Updates list displayed in {@code Jlist}.
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public void refreshList() throws ClassNotFoundException, SQLException, IOException {
        characters = dataBase.findAll();

        characterModel = new DefaultListModel<Character>();

        for (int i = 0; i < characters.size(); i++) {
            characterModel.add(i, characters.get(i));
        }

        try {
            mainFrame.getCharacterList().setModel(characterModel);
        } catch (NullPointerException e) {

            System.out.println("NullPointerException cought at refrechlist(). ");

        }

    }

    /**
     * Triggerd by {@link Controller.Controller.deleteCharacterListener}.
     * Deletes selected character.
     * @param character
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void deleteCharacter(Character character) throws SQLException, ClassNotFoundException, IOException {
        try {
            characterModel.removeElement(character);
            characters.remove(character);
            dataBase.deleteCharacter(character);

            mainFrame.getCharacterList().setSelectedIndex(0);
            selectedCharacter = mainFrame.getCharacterList().getSelectedValue();
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(mainFrame, "Veuillez selectionner un personnage a supprimer.", "Aucun personnage selectionne", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Triggered by {@link Controller.Controller.addPictureListener}. Opens
     * {@link javax.swing.JFileChooser} that allows user to choose picture to
     * character.
     * @param character
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void addPicture(Character character) throws IOException, ClassNotFoundException, SQLException {
        String type;
        File chosenFile;
        pictureChooser = new JFileChooser();

        if (character == null) {
            JOptionPane.showMessageDialog(mainFrame, "Veuillez selectionner un personnage a modifier.", "Aucun personnage selectionne", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = pictureChooser.showOpenDialog(mainFrame);

        if (choice != JFileChooser.APPROVE_OPTION) {
            return;
        }

        chosenFile = pictureChooser.getSelectedFile();

        type = URLConnection.guessContentTypeFromName(chosenFile.getAbsolutePath());

        if ((type.equals("image/png")) || (type.equals("image/jpg")) && (type != null)) {
            character.setPicture(ImageIO.read(chosenFile));
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Ce type de fichier n'est pas pris en compte pour l'image. Merci de choisir une image type .jpg ou .png", "Erreur: type d'image", JOptionPane.ERROR_MESSAGE);
        }

        dataBase.updateCharacter(character);

    }

}
