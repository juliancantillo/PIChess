/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pichess;

import gui.Game;
import javax.swing.UIManager;

/**
 *
 * @author julianacb
 */
public class PIChess {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {}
        
        Game game = new Game("Juego de Ajedrez");
        
    }
}
