/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import javax.swing.JOptionPane;

/**
 *
 * @author julianacb
 */
public class AI extends Thread{

    //variables of the AI
    int[] movelist = new int[250];  	//valid move control
    int movecounter = 0;
    int color = 1;				//color of the player that can move
    Thread th = null;			//AI thread
    int deep = 0;				//actual deep
    int target = 4;				//target deep
    float value = 0;			//minimax
    float minimax[] = new float[10];
    float alphabeta[] = new float[10];	//Alpha Beta
    boolean ababort = false;
    int move;				//move of the AI
    Board board;
    
    //variables for the evaluation
    float[] posvalues = {0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f,
        0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f,
        0.00f, 0.00f, 0.01f, 0.02f, 0.03f, 0.03f, 0.02f, 0.01f, 0.00f, 0.00f,//8
        0.00f, 0.01f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.01f, 0.00f,//7
        0.00f, 0.03f, 0.04f, 0.06f, 0.06f, 0.06f, 0.06f, 0.04f, 0.02f, 0.00f,//6
        0.00f, 0.03f, 0.04f, 0.06f, 0.08f, 0.08f, 0.06f, 0.04f, 0.03f, 0.00f,//5
        0.00f, 0.03f, 0.04f, 0.06f, 0.08f, 0.08f, 0.06f, 0.04f, 0.03f, 0.00f,//4
        0.00f, 0.02f, 0.04f, 0.06f, 0.06f, 0.06f, 0.06f, 0.04f, 0.02f, 0.00f,//3
        0.00f, 0.01f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.01f, 0.00f,//2
        0.00f, 0.00f, 0.01f, 0.02f, 0.03f, 0.03f, 0.02f, 0.01f, 0.00f, 0.00f,//1
        0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f,
        0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f};

    public AI(Board board) {
        this.board = board;
    }
    
    public void newgame(Board board){
        
        //kill AI thread
	if (th != null)
            th.stop();
	th = null;	
        
        movecounter = 0;
	color = 1;	
	deep = 0;
	target = 1;
	genmove(board);
	board.code = 0;
    }
    
    //evaluate a position
    public float evaluation(Board board){
        float value = 0;
        float figur = 0;

        for (int i = 21; i < 99; i++) {
            if (board.board[i] != 0) {
                //material
                switch (board.board[i] % 10) {
                    case 1:
                        figur = 1.0f;
                        break;
                    case 2:
                    case 3:
                        figur = 3.0f;
                        break;
                    case 4:
                        figur = 4.5f;
                        break;
                    case 5:
                        figur = 9.0f;
                        break;
                    case 6:
                        figur = 0.0f;
                }

                //position
                figur += posvalues[i];

                if (board.board[i] % 100 / 10 == color) {
                    value += figur;
                } else {
                    value -= figur;
                }
            }

            if (i % 10 == 8) {
                i += 2;
            }
        }
        return value;
    }
//execute a move

    public void execute(Board board, int start, int end) {
        board.board[end] = board.board[start];
        board.board[start] = 0;

        //Rochade ?
        if (board.board[end] % 10 == 6) {
            if (end == start + 2) {	//little
                board.board[start + 1] = board.board[start + 3] % 100;
                board.board[start + 3] = 0;

                board.graphboard[start + 1] = board.board[start + 1];
                board.graphboard[start + 3] = 0;

                board.paintField(start + 3);
                board.paintField(start + 1);
            }
            if (end == start - 2) {	//big
                board.board[start - 1] = board.board[start - 4] % 100;
                board.board[start - 4] = 0;

                board.graphboard[start - 1] = board.board[start - 1];
                board.graphboard[start - 4] = 0;

                board.paintField(start - 4);
                board.paintField(start - 1);
            }
        }

        //change pawn?
        if ((board.board[end] % 10 == 1) && ((end < 29) || (end > 90))) {
            board.board[end] += 4;
        };

        board.graphboard[start] = board.board[start];
        board.graphboard[end] = board.board[end];

        board.paintField(end);
        board.paintField(start);

        //change player
        if (color == 1) {
            color = 2;

            //look for best move
            th = new Thread(this);
            th.setPriority(10);
            th.start();

        } else {
            color = 1;

            //calculate valid moves
            movecounter = 0;
            deep = 0;
            target = 1;
            genmove(board);

            if (movecounter == 0) //no valid moves -> end of game
            {
                if (ischeck(board)) {
                    JOptionPane.showMessageDialog(board, "Las negras ganaron");
                } else {
                    JOptionPane.showMessageDialog(board, "Empate");
                }
            }
        }
    }
//generates valid moves

    public void genmove(Board board) {
        deep++;
        ababort = false;

        //find checkmath and initialize alphabeta
        if (deep % 2 != 0) {	//Computer
            minimax[deep] = 2000.0f;
            alphabeta[deep] = 3000.0f;
        } else {
            //human
            minimax[deep] = -2000.0f;
            alphabeta[deep] = -3000.0f;
        }

        for (int i = 21; i < 99; i++) {
            if (board.board[i] % 100 / 10 == color) //check color
            {
                switch (board.board[i] % 10) {
                    case 1:	//pawn	
                        if (color == 1) //white pawn ?
                        {
                            if (board.board[i - 10] == 0) {
                                simulize(board, i, i - 10);
                            }
                            if (board.board[i - 9] % 100 / 10 == 2) {
                                simulize(board, i, i - 9);
                            }
                            if (board.board[i - 11] % 100 / 10 == 2) {
                                simulize(board, i, i - 11);
                            }
                            if ((i > 80) && ((board.board[i - 10] == 0) && (board.board[i - 20] == 0))) {
                                simulize(board, i, i - 20);
                            }
                        } else {	//black pawn
                            if (board.board[i + 10] == 0) {
                                simulize(board, i, i + 10);
                            }
                            if (board.board[i + 9] % 100 / 10 == 1) {
                                simulize(board, i, i + 9);
                            }
                            if (board.board[i + 11] % 100 / 10 == 1) {
                                simulize(board, i, i + 11);
                            }
                            if ((i < 39) && ((board.board[i + 10] == 0) && (board.board[i + 20] == 0))) {
                                simulize(board, i, i + 20);
                            }
                            //en passant
                        }
                        break;
                    case 2:	//knight	
                        simulize(board, i, i + 12);
                        simulize(board, i, i - 12);
                        simulize(board, i, i + 21);
                        simulize(board, i, i - 21);
                        simulize(board, i, i + 19);
                        simulize(board, i, i - 19);
                        simulize(board, i, i + 8);
                        simulize(board, i, i - 8);
                        break;
                    case 5:	//queen
                    case 3:	//bishop
                        multisimulize(board, i, -9);
                        multisimulize(board, i, -11);
                        multisimulize(board, i, +9);
                        multisimulize(board, i, +11);

                        if (board.board[i] % 10 == 3) {
                            break;
                        }
                    case 4:	//rook
                        multisimulize(board, i, -10);
                        multisimulize(board, i, +10);
                        multisimulize(board, i, -1);
                        multisimulize(board, i, +1);
                        break;
                    case 6:	//king
                        if ((board.board[i] / 100 == 1) && (!ischeck(board))) {
                            if (((board.board[i + 1] == 0) && (board.board[i + 2] == 0)) && (board.board[i + 3] / 100 == 1)) {	//little casteling				
                                board.board[i + 1] = board.board[i] % 100;
                                board.board[i] = 0;

                                if (!ischeck(board)) {
                                    //king back
                                    board.board[i] = board.board[i + 1];

                                    //move rook
                                    board.board[i + 1] = board.board[i + 3] % 100;
                                    board.board[i + 3] = 0;

                                    simulize(board, i, i + 2);

                                    //takeback
                                    board.board[i + 3] = board.board[i + 1] + 100;
                                    board.board[i + 1] = board.board[i];
                                }

                                //rebuild original position
                                board.board[i] = board.board[i + 1] + 100;
                                board.board[i + 1] = 0;
                            }

                            if (((board.board[i - 1] == 0) && (board.board[i - 2] == 0)) && ((board.board[i - 3] == 0) && (board.board[i - 4] / 100 == 1))) {	//big casteling
                                board.board[i - 1] = board.board[i] % 100;
                                board.board[i] = 0;

                                if (!ischeck(board)) {
                                    //king back
                                    board.board[i] = board.board[i - 1];

                                    //move rook
                                    board.board[i - 1] = board.board[i - 4] % 100;
                                    board.board[i - 4] = 0;

                                    simulize(board, i, i - 2);

                                    //tackeback
                                    board.board[i - 4] = board.board[i - 1] + 100;
                                    board.board[i - 1] = board.board[i];
                                }

                                //rebuild original position
                                board.board[i] = board.board[i - 1] + 100;
                                board.board[i - 1] = 0;
                            }
                        }

                        simulize(board, i, i + 1);
                        simulize(board, i, i - 1);
                        simulize(board, i, i + 10);
                        simulize(board, i, i - 10);
                        simulize(board, i, i + 9);
                        simulize(board, i, i - 9);
                        simulize(board, i, i + 11);
                        simulize(board, i, i - 11);
                }
            }

            if (i % 10 == 8) {
                i += 2;
            }
        }

        deep--;
        ababort = false;
    }
//is king in check?

    public boolean ischeck(Board board) {
        int king = 0;

        //search king
        for (int i = 21; i < 99; i++) {
            if ((board.board[i] % 100 / 10 == color) && (board.board[i] % 10 == 6)) {
                king = i;
                break;
            }

            if (i % 10 == 8) {
                i += 2;
            }
        }

        //knight
        if ((board.board[king - 21] % 10 == 2) && (board.board[king - 21] % 100 / 10 != color)) {
            return true;
        }
        if ((board.board[king + 21] % 10 == 2) && (board.board[king + 21] % 100 / 10 != color)) {
            return true;
        }
        if ((board.board[king - 19] % 10 == 2) && (board.board[king - 19] % 100 / 10 != color)) {
            return true;
        }
        if ((board.board[king + 19] % 10 == 2) && (board.board[king + 19] % 100 / 10 != color)) {
            return true;
        }
        if ((board.board[king - 8] % 10 == 2) && (board.board[king - 8] % 100 / 10 != color)) {
            return true;
        }
        if ((board.board[king + 8] % 10 == 2) && (board.board[king + 8] % 100 / 10 != color)) {
            return true;
        }
        if ((board.board[king - 12] % 10 == 2) && (board.board[king - 12] % 100 / 10 != color)) {
            return true;
        }
        if ((board.board[king + 12] % 10 == 2) && (board.board[king + 12] % 100 / 10 != color)) {
            return true;
        }

        //ishop
        int j = king;
        while (board.board[j - 9] != 99) {
            j -= 9;
            if (board.board[j] % 100 / 10 == color) {
                break;
            }
            if (board.board[j] == 0) {
                continue;
            }
            if ((board.board[j] % 10 == 3) || (board.board[j] % 10 == 5)) {
                return true;
            } else {
                break;
            }
        }

        j = king;
        while (board.board[j + 9] != 99) {
            j += 9;
            if (board.board[j] % 100 / 10 == color) {
                break;
            }
            if (board.board[j] == 0) {
                continue;
            }
            if ((board.board[j] % 10 == 3) || (board.board[j] % 10 == 5)) {
                return true;
            } else {
                break;
            }
        }

        j = king;
        while (board.board[j - 11] != 99) {
            j -= 11;
            if (board.board[j] % 100 / 10 == color) {
                break;
            }
            if (board.board[j] == 0) {
                continue;
            }
            if ((board.board[j] % 10 == 3) || (board.board[j] % 10 == 5)) {
                return true;
            } else {
                break;
            }
        }

        j = king;
        while (board.board[j + 11] != 99) {
            j += 11;
            if (board.board[j] % 100 / 10 == color) {
                break;
            }
            if (board.board[j] == 0) {
                continue;
            }
            if ((board.board[j] % 10 == 3) || (board.board[j] % 10 == 5)) {
                return true;
            } else {
                break;
            }
        }

        //rook
        j = king;
        while (board.board[j - 10] != 99) {
            j -= 10;
            if (board.board[j] % 100 / 10 == color) {
                break;
            }
            if (board.board[j] == 0) {
                continue;
            }
            if ((board.board[j] % 10 == 4) || (board.board[j] % 10 == 5)) {
                return true;
            } else {
                break;
            }
        }
        j = king;
        while (board.board[j + 10] != 99) {
            j += 10;
            if (board.board[j] % 100 / 10 == color) {
                break;
            }
            if (board.board[j] == 0) {
                continue;
            }
            if ((board.board[j] % 10 == 4) || (board.board[j] % 10 == 5)) {
                return true;
            } else {
                break;
            }
        }
        j = king;
        while (board.board[j - 1] != 99) {
            j -= 1;
            if (board.board[j] % 100 / 10 == color) {
                break;
            }
            if (board.board[j] == 0) {
                continue;
            }
            if ((board.board[j] % 10 == 4) || (board.board[j] % 10 == 5)) {
                return true;
            } else {
                break;
            }
        }
        j = king;
        while (board.board[j + 1] != 99) {
            j += 1;
            if (board.board[j] % 100 / 10 == color) {
                break;
            }
            if (board.board[j] == 0) {
                continue;
            }
            if ((board.board[j] % 10 == 4) || (board.board[j] % 10 == 5)) {
                return true;
            } else {
                break;
            }
        }

        //pawn
        if (color == 1) {
            if ((board.board[king - 11] % 10 == 1) && (board.board[king - 11] % 100 / 10 == 2)) {
                return true;
            }
            if ((board.board[king - 9] % 10 == 1) && (board.board[king - 9] % 100 / 10 == 2)) {
                return true;
            }
        } else {
            if ((board.board[king + 11] % 10 == 1) && (board.board[king + 11] % 100 / 10 == 1)) {
                return true;
            }
            if ((board.board[king + 9] % 10 == 1) && (board.board[king + 9] % 100 / 10 == 1)) {
                return true;
            }
        }

        //king
        if (board.board[king + 1] % 10 == 6) {
            return true;
        }
        if (board.board[king - 1] % 10 == 6) {
            return true;
        }
        if (board.board[king + 10] % 10 == 6) {
            return true;
        }
        if (board.board[king - 10] % 10 == 6) {
            return true;
        }
        if (board.board[king + 11] % 10 == 6) {
            return true;
        }
        if (board.board[king - 11] % 10 == 6) {
            return true;
        }
        if (board.board[king + 9] % 10 == 6) {
            return true;
        }
        if (board.board[king - 9] % 10 == 6) {
            return true;
        }

        return false;
    }
//checks if a human move is valid

    public boolean isvalid(int move) {
        for (int i = 0; i < movecounter; i++) {
            if (movelist[i] == move) {
                return true;
            }
        }
        return false;
    }

    //simulation for queen, rook and bishop
    public void multisimulize(Board board, int start, int inc) {
        int to = start;

        while ((board.board[to + inc] != 99) && (board.board[to + inc] % 100 / 10 != color)) {
            to += inc;

            if (board.board[to] != 0) {
                simulize(board, start, to);
                return;
            }
            simulize(board, start, to);
        }
        simulize(board, start, to);
    }

    //here we simulize the move
    public void simulize(Board board, int start, int end) {
        if ((board.board[end] == 99) || (board.board[end] % 100 / 10 == color)) {
            return;
        }

        if (ababort) //alpha beta
        {
            return;
        }

        //simulize move
        int orgstart = board.board[start];
        int orgend = board.board[end];

        board.board[end] = board.board[start];
        board.board[start] = 0;

        //change pawn
        if ((board.board[end] % 10 == 1) && ((end < 29) || (end > 90))) {
            board.board[end] += 4;
        }

        if (!ischeck(board)) {
            if (deep == 1) {
                movelist[movecounter] = start * 100 + end;
                movecounter++;
            }

            //calculate value of this node
            if (target == deep) {
                value = evaluation(board);
            } else {
                if (color == 1) {
                    color = 2;
                } else {
                    color = 1;
                }

                genmove(board);
                value = minimax[deep + 1];

                //change alpha beta field?
                if (deep % 2 != 0) {	//computer
                    if (value < alphabeta[deep]) {
                        alphabeta[deep] = value;
                    }
                } else {
                    //human
                    if (value > alphabeta[deep]) {
                        alphabeta[deep] = value;
                    }
                }

                if (color == 1) {
                    color = 2;
                } else {
                    color = 1;
                }
            }

            //minimax
            if (deep % 2 == 0) {	//human
                if (value > minimax[deep]) {
                    minimax[deep] = value;
                }
                //alphabeta
                if (value > alphabeta[deep - 1]) {
                    ababort = true;
                }

            } else {
                //computer
                if (value <= minimax[deep]) {
                    minimax[deep] = value;
                    if (deep == 1) {
                        move = start * 100 + end;
                    }
                }
                //alphabeta
                if (value < alphabeta[deep - 1]) {
                    ababort = true;
                }
            }
        }

        //undo move
        board.board[start] = orgstart;
        board.board[end] = orgend;
    }

    @Override
    public void run() {
        //no access to the movelist
        board.code = 1;

        deep = 0;
        target = 4;

        //look for best move
        movecounter = 0;
        genmove(board);

        if (movecounter == 0) //no moves -> end of game
        {
            if (ischeck(board)) {
                //parent.getAppletContext().showStatus("white wins!");
            } else {
                //parent.getAppletContext().showStatus("game is a draw!");
            }
            return;
        }
        //execute move
        execute(board, move / 100, move % 100);

        //give accesss to the movelist
        board.code = 0;
    }
    
}
