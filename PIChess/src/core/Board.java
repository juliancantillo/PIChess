/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

/**
 *
 * @author julianacb
 */
public class Board extends JComponent implements MouseListener, MouseMotionListener {

    //Representacion logica del Tablero
    private int[] board = new int[120];
    //Representacion grafica del Tablero
    private int[] graphboard = new int[120];
    private JLayeredPane layeredPane;
    private JPanel chessBoard;
    private JPanel[][] square;
    private Piece[] pieces;

    public Board() {
        
        newGame();
        
        initializePieces();
        initializeBoard();
    }

    public final void initializeBoard() {

        setLayout(new FlowLayout());

        Dimension boardSize = new Dimension(500, 500);
        layeredPane = new JLayeredPane();

        layeredPane.setPreferredSize(boardSize);
        layeredPane.addMouseListener(this);
        layeredPane.addMouseMotionListener(this);

        //Add a chess board to the Layered Pane 

        chessBoard = new JPanel();
        chessBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
        chessBoard.setLayout(new GridLayout(8, 8));
        chessBoard.setPreferredSize(boardSize);
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);
        
        
        square = new JPanel[8][8];

//        for (int i = 0; i < 64; i++) {
//            JPanel square = new JPanel(new BorderLayout());
//            chessBoard.add(square);
//
//            int row = (i / 8) % 2;
//            if (row == 0) {
//                square.setBackground(i % 2 == 0 ? Color.BLACK : Color.WHITE);
//            } else {
//                square.setBackground(i % 2 == 0 ? Color.WHITE : Color.BLACK);
//            }
//        }
        for ( int i = 21; i < 99; i++){
		paintField (i);		
		if ( i%10 == 8)
			i += 2;
	}

        this.add(layeredPane);
        

    }

    public final void initializePieces() {
        pieces = new Piece[18];

        pieces[1] = new Piece(Piece.WHITE_PAWN);
        pieces[2] = new Piece(Piece.WHITE_KNIGHT);
        pieces[3] = new Piece(Piece.WHITE_BISHOP);
        pieces[4] = new Piece(Piece.WHITE_ROOK);
        pieces[5] = new Piece(Piece.WHITE_QUEEN);
        pieces[6] = new Piece(Piece.WHITE_KING);

        pieces[11] = new Piece(Piece.BLACK_PAWN);
        pieces[12] = new Piece(Piece.BLACK_KNIGHT);
        pieces[13] = new Piece(Piece.BLACK_BISHOP);
        pieces[14] = new Piece(Piece.BLACK_ROOK);
        pieces[15] = new Piece(Piece.BLACK_QUEEN);
        pieces[16] = new Piece(Piece.BLACK_KING);

    }
    
    public void paintField(int index){
        //calculate x and y
	int x = (index - 21) % 10;
	int y = (index - 21) / 10;
        
        square[x][y] = new JPanel(new BorderLayout());

	//paint ground field
	if ( (x*11 + y) % 2 == 0){
            square[x][y].setBackground(Color.BLACK);
            System.out.print("(Hell, "+x+","+y+")");
            
        }else{
            square[x][y].setBackground(Color.WHITE);
            System.out.print("(Dunkel, "+x+","+y+")");
        }
        
        chessBoard.add(square[x][y]);
        
        try{
            square[x][y].add(new JLabel(pieces[graphboard [index] % 100 - 10]));
        }catch(ArrayIndexOutOfBoundsException e){}
	//paint piece

//      (pieces [graphboard [index] % 100 - 10], x * 40, y * 40, 40, 40, parent);

    }

    public void newGame() {

        int[] initial = {
            99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
            99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
            99, 124, 22, 23, 25, 126, 23, 22, 124, 99,
            99, 21, 21, 21, 21, 21, 21, 21, 21, 99,
            99, 00, 00, 00, 00, 00, 00, 00, 00, 99,
            99, 00, 00, 00, 00, 00, 00, 00, 00, 99,
            99, 00, 00, 00, 00, 00, 00, 00, 00, 99,
            99, 00, 00, 00, 00, 00, 00, 00, 00, 99,
            99, 11, 11, 11, 11, 11, 11, 11, 11, 99,
            99, 114, 12, 13, 15, 116, 13, 12, 114, 99,
            99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
            99, 99, 99, 99, 99, 99, 99, 99, 99, 99};

        for (int i = 0; i < 120; i++) {
            board[i] = initial[i];
            graphboard[i] = initial[i];
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
