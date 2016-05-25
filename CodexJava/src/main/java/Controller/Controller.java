/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Personnage;
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
    
    private ListSelectionListener displayedPerso;
    private ActionListener changeButtonListener, addButtonListener,cancelAddListener, confirmAddListener;
    
    
    private addPersoFrame addPerso;
    RDAView mainFrame;
    
    ArrayList<Personnage> characters;
    Personnage[] arrayCharacters;
    private Personnage selected;
    
    
    public Controller() throws ClassNotFoundException, SQLException, IOException
    {
        mainFrame = new RDAView();       

        refreshList();
        
        displayedPerso = new ListSelectionListener() {
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
    
        
        mainFrame.getListePersonnages().addListSelectionListener(displayedPerso);
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
        selected.setChanges(selected.getId(), mainFrame.getTextName().getText(), mainFrame.getTextRace().getText(), Integer.parseInt(mainFrame.getTextLevel().getText()), mainFrame.getTextDescription().getText());
        Personnage.updatePerso(selected);
    }
    
    
    public void persoSelection()
    {
        selected = mainFrame.getListePersonnages().getSelectedValue();
        
        mainFrame.getTextName().setText(selected.getName());
        mainFrame.getTextRace().setText(selected.getRace());
        mainFrame.getTextLevel().setText(Integer.toString(selected.getLevel()));
        mainFrame.getTextDescription().setText(selected.getDescription());
        
        mainFrame.getImagePanel().setImage(selected.getPicture());
        
    }
    
    
    public void openAddForm()
    {
        addPerso = new addPersoFrame();
        
        confirmAddListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    confirmAddCharacter();
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        
        cancelAddListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                addPerso.setVisible(false);
            }
        };
        
        addPerso.getConfirmButton().addActionListener(addButtonListener);
        addPerso.getCancelButton().addActionListener(cancelAddListener);
        
        addPerso.setVisible(true);
    }
    
    
    public void confirmAddCharacter() throws IOException
    {
        Personnage newCharacter = new Personnage(characters.size()+1, addPerso.getNameField().getText(), addPerso.getRaceField().getText(), addPerso.getDescriptionField().getText(), Integer.parseInt(addPerso.getLevelField().getText()),null);
        characters.add(newCharacter);
        
        Personnage.addCharacter(newCharacter);
    }
    
    public void refreshList() throws ClassNotFoundException, SQLException, IOException
    {
        characters = Personnage.findAll();
        arrayCharacters = new Personnage[characters.size()];
        arrayCharacters = characters.toArray(arrayCharacters);
        mainFrame.getListePersonnages().setListData(arrayCharacters);
    }

    
}
