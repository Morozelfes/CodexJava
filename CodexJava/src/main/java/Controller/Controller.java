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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Controller {
    
    private ListSelectionListener characterSelectionListener;
    private ActionListener changeButtonListener, addButtonListener,cancelAddListener, confirmAddListener, deleteButtonListener;
    
    
    private addPersoFrame newCharacterFrame;
    private RDAView mainFrame;
    private RDADatabase dataBase;
    
    ArrayList<Character> characters;
    DefaultListModel<Character> characterModel;
    private Character selectedCharacter;
    
    //private DefaultListModel listModel;
    
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
        
        
        deleteButtonListener = new ActionListener() {
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
    
        
        mainFrame.getListePersonnages().addListSelectionListener(characterSelectionListener);
        mainFrame.getSaveChange().addActionListener(changeButtonListener);
        mainFrame.getNewEntry().addActionListener(addButtonListener);
        mainFrame.getDelete().addActionListener(deleteButtonListener);
        
        
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
        Character newCharacter = new Character(characters.size()+1, newCharacterFrame.getNameField().getText(), newCharacterFrame.getRaceField().getText(), newCharacterFrame.getDescriptionField().getText(), Integer.parseInt(newCharacterFrame.getLevelField().getText()),null);
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
        System.out.println(character);
        
        characterModel.removeElement(character);
        System.out.println("passe 1");
        
        characters.remove(character);
        
        System.out.println("passe2");
        dataBase.deleteCharacter(character);               
        
        mainFrame.getListePersonnages().setSelectedIndex(0);
        selectedCharacter = mainFrame.getListePersonnages().getSelectedValue();
        System.out.println("selectinne apres suppression: " + selectedCharacter);
    }
    
    
    public void removeFromModel(DefaultListModel<Character> model, Character c)
    {
        DefaultListModel<Character> temp;
        
        temp = model;
        
        for (int i=0, j=0; i<=model.getSize(); i++)
        {
            if(model.get(i) != c)
            {
                temp.add(j, c);
                j++;
            }
        }
        
        model = temp;
    }

    
}
