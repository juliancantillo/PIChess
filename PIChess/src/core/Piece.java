/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.awt.Image;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author julianacb
 */
public class Piece extends ImageIcon{
    
    public static final String WHITE_PAWN = "bluePawn.gif";
    public static final String WHITE_KNIGHT = "blueKnight.gif";
    public static final String WHITE_KING = "blueKing.gif";
    public static final String WHITE_QUEEN = "blueQueen.gif";
    public static final String WHITE_ROOK = "blueRock.gif";
    public static final String WHITE_BISHOP = "blueBishop.gif";
    public static final String BLACK_PAWN = "redPawn.gif";
    public static final String BLACK_KNIGHT = "redKnight.gif";
    public static final String BLACK_KING = "redKing.gif";
    public static final String BLACK_QUEEN = "redQueen.gif";
    public static final String BLACK_ROOK = "redRock.gif";
    public static final String BLACK_BISHOP = "redBishop.gif";

    public Piece(String pieceName) {
        this.setImage(loadPieceImage(pieceName));
    }
    
    public final Image loadPieceImage(String name){
        Image image = null;
        
        URL imageURL = getClass().getResource("../images/"+name);
        
        try {
            image = ImageIO.read(imageURL);
        } catch (Exception io) {
            System.err.print("No se encontró la imagen "+name);
        }
        
        return image;
    }
    
}
