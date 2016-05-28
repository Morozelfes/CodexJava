/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Character;
import Model.RDADatabase;
import Views.RDAView;
import Views.addPersoFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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


public class Controller {
    private SecurityController security;
    
    private ListSelectionListener characterSelectionListener;
    private ActionListener updateCharacterListener, addCharacterListener,cancelAddListener, confirmAddListener, deleteCharacterListener, addPictureListener;
    
    
    private addPersoFrame newCharacterFrame;
    private RDAView mainFrame;
    private JFileChooser pictureChooser;
    
    
    private RDADatabase dataBase;
    
    ArrayList<Character> characters;
    DefaultListModel<Character> characterModel;
    private Character selectedCharacter;
    
    
    public Controller() throws ClassNotFoundException, SQLException, IOException
    {
        mainFrame = new RDAView();
        dataBase = RDADatabase.getInstance();

        initMainFrame();

    }
    
    
    public void initMainFrame() throws ClassNotFoundException, SQLException, IOException
    {
        refreshList();
        
        initListeners();
  
        mainFrame.getListePersonnages().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainFrame.getListePersonnages().setLayoutOrientation(JList.VERTICAL);
        
        mainFrame.getListePersonnages().setVisibleRowCount(10);
        mainFrame.getListePersonnages().setVisible(true);
        
        
        mainFrame.setVisible(true);
        
    }
    
    
    
    public void initListeners()
    {
        characterSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
                if (!lse.getValueIsAdjusting())
                    persoSelection();
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
    
        
        mainFrame.getListePersonnages().addListSelectionListener(characterSelectionListener);
        mainFrame.getSaveChange().addActionListener(updateCharacterListener);
        mainFrame.getNewEntry().addActionListener(addCharacterListener);
        mainFrame.getDelete().addActionListener(deleteCharacterListener);
        mainFrame.getChangeImage().addActionListener(addPictureListener);
        
    }
    
    
    public void updateCharacter(Character character) throws ClassNotFoundException, SQLException
    {
        String name, race, unknown = "Inconnu";
        int level;
        
        if (selectedCharacter == null)
        {
            JOptionPane.showMessageDialog(mainFrame, "Veuillez selectionner un personnage a modifier.", "Aucun personnage selectionne", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        name = mainFrame.getTextName().getText();
        if ("".equals(name))
            name = unknown;
        
        race = mainFrame.getTextRace().getText();
        if ("".equals(race))
            race = unknown;
        
        if (!"".equals(mainFrame.getTextLevel().getText()))
            level = Integer.parseInt(mainFrame.getTextLevel().getText());
        else
            level = 0;
        
        
        character.setChanges(name, race, level, mainFrame.getTextDescription().getText());
        dataBase.updateCharacter(character);

    }
    
    
    public void persoSelection()
    {        
        try
        {
            selectedCharacter = mainFrame.getListePersonnages().getSelectedValue();

            mainFrame.getTextName().setText(selectedCharacter.getName());
            mainFrame.getTextRace().setText(selectedCharacter.getRace());
            mainFrame.getTextLevel().setText(Integer.toString(selectedCharacter.getLevel()));
            mainFrame.getTextDescription().setText(selectedCharacter.getDescription());

            mainFrame.getImagePanel().setImage(selectedCharacter.getPicture());
        }
        catch(NullPointerException npe)
        {
            System.out.println("NullPointerException cought at character selection.");
        }
        
    }
    
    
    
    
    public void openAddForm()
    {
        newCharacterFrame = new addPersoFrame();
        
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
    
    
    
    public void confirmAddCharacter() throws IOException, SQLException, ClassNotFoundException
    {
        String unknown = "Inconnu", name, race;
        int level;
        
        name = newCharacterFrame.getNameField().getText();
        if ("".equals(name))
            name = unknown;
        
        race = newCharacterFrame.getRaceField().getText();
        if("".equals(race))
            race = unknown;
        
        
        if (!"".equals(newCharacterFrame.getLevelField().getText()))
            level = Integer.parseInt(newCharacterFrame.getLevelField().getText());
        else
            level = 0;
        
        
        Character newCharacter = new Character(characters.size()+1, name, race, newCharacterFrame.getDescriptionField().getText(), level, null);
        characters.add(newCharacter);
        
        dataBase.addCharacter(newCharacter);
        characterModel.add(characters.size()-1, newCharacter);
        
        newCharacterFrame.setVisible(false);
    }    

    
    
    
    
    public void refreshList() throws ClassNotFoundException, SQLException, IOException
    {      
        characters = dataBase.findAll();
        
        characterModel = new DefaultListModel<Character>();
        
        for(int i=0; i<characters.size(); i++)
            characterModel.add(i, characters.get(i));
        
       try{
            mainFrame.getListePersonnages().setModel(characterModel);
        }
        catch(NullPointerException e){
            
            System.out.println("NullPointerException cought at refrechlist(). ");
        
        }
        
    }
    
    
    public void deleteCharacter(Character character) throws SQLException, ClassNotFoundException, IOException
    {
        try
        {
            characterModel.removeElement(character);
            characters.remove(character);        
            dataBase.deleteCharacter(character);               

            mainFrame.getListePersonnages().setSelectedIndex(0);
            selectedCharacter = mainFrame.getListePersonnages().getSelectedValue();
        }
        catch(NullPointerException e)
        {
            JOptionPane.showMessageDialog(mainFrame, "Veuillez selectionner un personnage a supprimer.", "Aucun personnage selectionne", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void addPicture(Character character) throws IOException, ClassNotFoundException, SQLException
    {
        String type;
        File chosenFile;
        pictureChooser = new JFileChooser();
        
        if (character == null)
        {
            JOptionPane.showMessageDialog(mainFrame, "Veuillez selectionner un personnage a modifier.", "Aucun personnage selectionne", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int choice = pictureChooser.showOpenDialog(mainFrame);
        
        if(choice != JFileChooser.APPROVE_OPTION)
            return;
        
        chosenFile = pictureChooser.getSelectedFile();       
        
        type = URLConnection.guessContentTypeFromName(chosenFile.getAbsolutePath());
                
        if ((type.equals("image/png")) || (type.equals("image/jpg")) && (type != null))
            character.setPicture(ImageIO.read(chosenFile));
        else
            JOptionPane.showMessageDialog(mainFrame, "Ce type de fichier n'est pas pris en compte pour l'image. Merci de choisir une image type .jpg ou .png", "Erreur: type d'image", JOptionPane.ERROR_MESSAGE);
      
    }
    
    

    
}
