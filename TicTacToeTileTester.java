/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wulft
 */
public class TicTacToeTileTester {


    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        TicTacToeTile[][] board = new TicTacToeTile[3][3]; 
        
        for( int row = 0; row < 3; row++)
            for(int col= 0; col < 3; col++)
            {
                board[row][col] = new TicTacToeTile(row, col);
                board[row][col].setText(" ");
            }
    }
    
}
