/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Personnage;
import Views.RDAView;
import Views.RefletdAcide;
import Views.listListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JList;
import javax.swing.ListSelectionModel;


public class Controller {
    
    private listListener ll;
    RDAView fenetre;
    
    
    public Controller() throws ClassNotFoundException, SQLException
    {
        fenetre = new RDAView();       
        ArrayList<Personnage> liste = Personnage.findAll();
        Personnage[] arrayPerso = new Personnage[liste.size()];
        ll = new listListener(this);
        
        arrayPerso = liste.toArray(arrayPerso);
        
        //ListSelectionListener listListener = new ListSelectionListener();
        fenetre.getListePersonnages().addListSelectionListener(ll);
        
        
        fenetre.getListePersonnages().setListData(arrayPerso);
        fenetre.getListePersonnages().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fenetre.getListePersonnages().setLayoutOrientation(JList.VERTICAL);
        
        fenetre.getListePersonnages().setVisibleRowCount(10);
        fenetre.getListePersonnages().setVisible(true);
        
        fenetre.setVisible(true);

    }
    
    public void persoSelection()
    {
        System.out.println(fenetre.getListePersonnages().getSelectedValue());
        
        fenetre.getTextName().setText(fenetre.getListePersonnages().getSelectedValue().getName());
        fenetre.getTextRace().setText(fenetre.getListePersonnages().getSelectedValue().getRace());
        fenetre.getTextLevel().setText(Integer.toString(fenetre.getListePersonnages().getSelectedValue().getLevel()));
        fenetre.getTextDescription().setText(fenetre.getListePersonnages().getSelectedValue().getDescription());
        
    }
    
    
    
    
}
