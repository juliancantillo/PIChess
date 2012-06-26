/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import core.Board;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

/**
 *
 * @author julianacb
 */
public class Game extends JFrame implements ActionListener{
    
    private Board gameboard;
    private GUIMenu menu;

    public Game(String title){
        super(title);
        
        menu = new GUIMenu(this);
        setJMenuBar(menu);
        
        gameboard = new Board();
        
        add(gameboard);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700,700);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == menu.NewGame()){
            gameboard.newGame();
        }
        if(e.getSource() == menu.getReset()){
            gameboard.resetField();
        }
        
    }
    
}
