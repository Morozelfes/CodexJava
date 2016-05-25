/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Personnage;
import Views.RDAView;
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
    private ActionListener al;
    RDAView fenetre;
    
    private Personnage selected;
    
    
    public Controller() throws ClassNotFoundException, SQLException, IOException
    {
        fenetre = new RDAView();       
        ArrayList<Personnage> liste = Personnage.findAll();
        Personnage[] arrayPerso = new Personnage[liste.size()];
        
        arrayPerso = liste.toArray(arrayPerso);
        
        displayedPerso = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
                if (!lse.getValueIsAdjusting())
                    persoSelection();
            }
        };
        
        
        al = new ActionListener() {
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
    
        
        fenetre.getListePersonnages().addListSelectionListener(displayedPerso);
        fenetre.getSaveChange().addActionListener(al);
        
        
        fenetre.getListePersonnages().setListData(arrayPerso);
        fenetre.getListePersonnages().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fenetre.getListePersonnages().setLayoutOrientation(JList.VERTICAL);
        
        fenetre.getListePersonnages().setVisibleRowCount(10);
        fenetre.getListePersonnages().setVisible(true);
        
        
        fenetre.setVisible(true);


    }
    
    
    public void updatePerso() throws ClassNotFoundException, SQLException
    {
        selected.setChanges(selected.getId(), fenetre.getTextName().getText(), fenetre.getTextRace().getText(), Integer.parseInt(fenetre.getTextLevel().getText()), fenetre.getTextDescription().getText());
        Personnage.updatePerso(selected);
    }
    
    public void persoSelection()
    {
        selected = fenetre.getListePersonnages().getSelectedValue();
        
        fenetre.getTextName().setText(selected.getName());
        fenetre.getTextRace().setText(selected.getRace());
        fenetre.getTextLevel().setText(Integer.toString(selected.getLevel()));
        fenetre.getTextDescription().setText(selected.getDescription());
        
        fenetre.getImagePanel().setImage(selected.getPicture());
        
    }
    
    
    
    
}
