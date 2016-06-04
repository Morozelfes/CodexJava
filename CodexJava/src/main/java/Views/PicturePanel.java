/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Oliver
 * <p>
 * This class provides a container for our characters picture to be displayed.
 */
public class PicturePanel extends JPanel {

    private BufferedImage picture;

    /**
     *
     */
    public PicturePanel() {
        super();
    }

    /**
     * This setter calls repaint() so is sufficient for picture displaying.
     * @param bi
     */
    public void setImage(BufferedImage bi) {
        picture = bi;
        repaint();
    }

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        if (picture != null) {
            g.drawImage(picture, 0, 0, this.getSize().width, this.getSize().height, this);
        }

    }

}
