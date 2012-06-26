/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

/**
 *
 * @author julianacb
 */
public class GUIMenu extends JMenuBar{
    
    ActionListener action;
    
    private JMenu mnGame;
    private JMenuItem gameNew, gameExit, reset;
    private JSeparator exitGap;

    public GUIMenu(ActionListener action) {
        this.action = action;
        
        mnGame = new JMenu("Juego");
        
        gameNew = new JMenuItem("Nuevo");
        reset = new JMenuItem("Reset");
        exitGap = new JSeparator();
        gameExit = new JMenuItem("Salir");
        
        mnGame.add(gameNew);
        mnGame.add(reset);
        mnGame.add(exitGap);
        mnGame.add(gameExit);
        
        gameNew.addActionListener(action);
        reset.addActionListener(action);
        gameExit.addActionListener(action);
        
        
        this.add(mnGame);
    }

    public JMenuItem NewGame() {
        return gameNew;
    }

    public JMenuItem getReset() {
        return reset;
    }
    
}
