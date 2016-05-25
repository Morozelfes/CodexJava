/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Personnage;
import Views.RefletdAcide;
import java.sql.SQLException;
import java.util.ArrayList;


public class Controller {
    
    public Controller() throws ClassNotFoundException, SQLException
    {
        RefletdAcide fenetre = new RefletdAcide();       
        ArrayList<Personnage> liste = Personnage.findAll();
        
        Object[] personnagesListe = liste.toArray();
        
        fenetre.getListePersonnages().setListData(personnagesListe);

    }
    
    
    
    
}
