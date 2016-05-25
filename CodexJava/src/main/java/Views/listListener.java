/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Controller.Controller;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Oliver
 */

public class listListener implements ListSelectionListener {
    
    private Controller control;
    
    public listListener(Controller c)
    {
        control = c;
    }

    public void valueChanged(ListSelectionEvent lse) {
        if (!lse.getValueIsAdjusting())
            control.persoSelection();
    }
    
}
