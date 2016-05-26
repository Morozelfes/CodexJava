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
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Controller {
    
    private ListSelectionListener characterSelectionListener;
    private ActionListener changeButtonListener, addButtonListener,cancelAddListener, confirmAddListener;
    
    
    private addPersoFrame newCharacterFrame;
    private RDAView mainFrame;
    private RDADatabase dataBase;
    
    ArrayList<Character> characters;
    Character[] arrayCharacters;
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
        
        characterSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
                if (!lse.getValueIsAdjusting())
                    persoSelection();
            }
        };
        
        
        changeButtonListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    updatePerso();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        
        addButtonListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                openAddForm();
            }
        };
    
        
        mainFrame.getListePersonnages().addListSelectionListener(characterSelectionListener);
        mainFrame.getSaveChange().addActionListener(changeButtonListener);
        mainFrame.getNewEntry().addActionListener(addButtonListener);
        
        
        mainFrame.getListePersonnages().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainFrame.getListePersonnages().setLayoutOrientation(JList.VERTICAL);
        
        mainFrame.getListePersonnages().setVisibleRowCount(10);
        mainFrame.getListePersonnages().setVisible(true);
        
        
        mainFrame.setVisible(true);
        
    }
    
    
    public void updatePerso() throws ClassNotFoundException, SQLException
    {
        selectedCharacter.setChanges(selectedCharacter.getId(), mainFrame.getTextName().getText(), mainFrame.getTextRace().getText(), Integer.parseInt(mainFrame.getTextLevel().getText()), mainFrame.getTextDescription().getText());
        dataBase.updatePerso(selectedCharacter);
    }
    
    
    public void persoSelection()
    {
        selectedCharacter = mainFrame.getListePersonnages().getSelectedValue();
        
        mainFrame.getTextName().setText(selectedCharacter.getName());
        mainFrame.getTextRace().setText(selectedCharacter.getRace());
        mainFrame.getTextLevel().setText(Integer.toString(selectedCharacter.getLevel()));
        mainFrame.getTextDescription().setText(selectedCharacter.getDescription());
        
        mainFrame.getImagePanel().setImage(selectedCharacter.getPicture());
        
    }
    
    
    
    
    public void openAddForm()
    {
        System.out.println("Passe par openAddForm()");
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
        Character newCharacter = new Character(characters.size()+1, newCharacterFrame.getNameField().getText(), newCharacterFrame.getRaceField().getText(), newCharacterFrame.getDescriptionField().getText(), Integer.parseInt(newCharacterFrame.getLevelField().getText()),null);
        characters.add(newCharacter);
        
        dataBase.addCharacter(newCharacter);
        refreshList();
        newCharacterFrame.setVisible(false);
    }
    
    
    
    
    
    public void refreshList() throws ClassNotFoundException, SQLException, IOException
    {
        characters = dataBase.findAll();
        arrayCharacters = new Character[characters.size()];
        arrayCharacters = characters.toArray(arrayCharacters);
        mainFrame.getListePersonnages().setListData(arrayCharacters);
    }

    
}
