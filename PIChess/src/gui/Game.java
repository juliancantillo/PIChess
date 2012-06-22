/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import core.Board;
import javax.swing.JFrame;

/**
 *
 * @author julianacb
 */
public class Game extends JFrame{
    
    private Board gameboard;

    public Game(String title){
        super(title);
        
        gameboard = new Board();
        
        add(gameboard);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);
        setVisible(true);
        
    }
    
}
