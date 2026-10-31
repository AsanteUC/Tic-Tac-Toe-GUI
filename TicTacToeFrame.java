import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

/**
 * The Tic Tac Toe game window.
 * <p>
 * Two human players take turns clicking squares on a 3 x 3 grid of
 * TicTacToeTile buttons, starting with X. The frame enforces the rules,
 * checks for a win starting with the 5th move and for a tie starting with
 * the 7th move, and offers to play again when a game ends. All messages to
 * the user are shown with JOptionPane; there is no console output.
 * <p>
 * The board data and game logic are the same as the console version.
 *
 * @author Asante
 * @version 1.0
 */
public class TicTacToeFrame extends JFrame
{
    private static final int ROW = 3;
    private static final int COL = 3;
    private static final int MOVES_FOR_WIN = 5;
    private static final int MOVES_FOR_TIE = 7;

    // Internal data representation of the board (same as the console game)
    private final String[][] board = new String[ROW][COL];

    // The GUI squares, parallel to the board array
    private final TicTacToeTile[][] tiles = new TicTacToeTile[ROW][COL];

    private String player = "X";
    private int moveCnt = 0;

    private JLabel statusLabel;

    private final Font titleFont = new Font("SansSerif", Font.BOLD, 28);
    private final Font statusFont = new Font("SansSerif", Font.PLAIN, 18);
    private final Font tileFont = new Font("SansSerif", Font.BOLD, 60);
    private final Font buttonFont = new Font("SansSerif", Font.BOLD, 18);

    /**
     * Builds the game window and all of its components.
     */
    public TicTacToeFrame()
    {
        super("Tic Tac Toe");
        setLayout(new BorderLayout());

        add(createTopPanel(), BorderLayout.NORTH);
        add(createBoardPanel(), BorderLayout.CENTER);
        add(createQuitPanel(), BorderLayout.SOUTH);

        clearBoard();

        setSize(450, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Creates the title and the label showing whose turn it is.
     *
     * @return the top panel
     */
    private JPanel createTopPanel()
    {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel title = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        title.setFont(titleFont);

        statusLabel = new JLabel("Player X's turn", SwingConstants.CENTER);
        statusLabel.setFont(statusFont);

        panel.add(title);
        panel.add(statusLabel);
        return panel;
    }

    /**
     * Creates the 3 x 3 grid of tiles. One ActionListener instance is shared
     * by every tile; it asks the clicked tile for its row and column.
     *
     * @return the board panel
     */
    private JPanel createBoardPanel()
    {
        JPanel panel = new JPanel(new GridLayout(ROW, COL, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // A single listener for all nine tiles
        ActionListener tileListener = ae ->
        {
            TicTacToeTile tile = (TicTacToeTile) ae.getSource();
            handleMove(tile.getRow(), tile.getCol());
        };

        for (int row = 0; row < ROW; row++)
        {
            for (int col = 0; col < COL; col++)
            {
                TicTacToeTile tile = new TicTacToeTile(row, col);
                tile.setText(" ");
                tile.setFont(tileFont);
                tile.setFocusPainted(false);
                tile.addActionListener(tileListener);
                tiles[row][col] = tile;
                panel.add(tile);
            }
        }
        return panel;
    }

    /**
     * Creates the panel holding the Quit button.
     *
     * @return the quit panel
     */
    private JPanel createQuitPanel()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        JButton quitButton = new JButton("Quit");
        quitButton.setFont(buttonFont);
        quitButton.addActionListener(ae -> quitGame());
        panel.add(quitButton);
        return panel;
    }

    /**
     * Handles a click on a square: rejects illegal moves, records legal ones,
     * checks for a win or tie and otherwise passes the turn.
     *
     * @param row the row of the clicked square (0-2)
     * @param col the column of the clicked square (0-2)
     */
    private void handleMove(int row, int col)
    {
        if (!isValidMove(row, col))
        {
            JOptionPane.showMessageDialog(this,
                    "That square is already taken. Please choose an empty square.",
                    "Illegal Move", JOptionPane.ERROR_MESSAGE);
            return; // stay on the same player until a legal move is made
        }

        board[row][col] = player;
        tiles[row][col].setText(player);
        tiles[row][col].setForeground(player.equals("X") ? Color.BLUE : Color.RED);
        moveCnt++;

        boolean gameOver = false;

        if (moveCnt >= MOVES_FOR_WIN && isWin(player))
        {
            JOptionPane.showMessageDialog(this,
                    "Player " + player + " wins!",
                    "We Have a Winner", JOptionPane.INFORMATION_MESSAGE);
            gameOver = true;
        }
        else if (moveCnt >= MOVES_FOR_TIE && isTie())
        {
            String reason = (moveCnt == ROW * COL)
                    ? "The board is full."
                    : "Neither player can win.";
            JOptionPane.showMessageDialog(this,
                    "It's a tie! " + reason,
                    "Tie Game", JOptionPane.INFORMATION_MESSAGE);
            gameOver = true;
        }

        if (gameOver)
        {
            askPlayAgain();
        }
        else
        {
            player = player.equals("X") ? "O" : "X";
            statusLabel.setText("Player " + player + "'s turn");
        }
    }

    /**
     * Asks whether to play another game. Yes resets the board, No exits.
     */
    private void askPlayAgain()
    {
        int choice = JOptionPane.showConfirmDialog(this,
                "Would you like to play another game?",
                "Play Again?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION)
        {
            clearBoard();
        }
        else
        {
            System.exit(0);
        }
    }

    /**
     * Confirms with the user and then exits the program.
     */
    private void quitGame()
    {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to quit?",
                "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION)
        {
            JOptionPane.showMessageDialog(this, "Thanks for playing. Goodbye!",
                    "Quit", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Resets the board data, the tiles and the turn for a new game.
     */
    private void clearBoard()
    {
        for (int row = 0; row < ROW; row++)
        {
            for (int col = 0; col < COL; col++)
            {
                board[row][col] = " ";
                tiles[row][col].setText(" ");
            }
        }
        player = "X";
        moveCnt = 0;
        statusLabel.setText("Player X's turn");
    }

    /**
     * Checks that a square is empty.
     *
     * @param row the row (0-2)
     * @param col the column (0-2)
     * @return true if the square is empty
     */
    private boolean isValidMove(int row, int col)
    {
        return board[row][col].equals(" ");
    }

    /**
     * Checks for a win for the specified player.
     *
     * @param player "X" or "O"
     * @return true if the player has three in a row
     */
    private boolean isWin(String player)
    {
        return isColWin(player) || isRowWin(player) || isDiagonalWin(player);
    }

    private boolean isColWin(String player)
    {
        for (int col = 0; col < COL; col++)
        {
            if (board[0][col].equals(player) &&
                board[1][col].equals(player) &&
                board[2][col].equals(player))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isRowWin(String player)
    {
        for (int row = 0; row < ROW; row++)
        {
            if (board[row][0].equals(player) &&
                board[row][1].equals(player) &&
                board[row][2].equals(player))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isDiagonalWin(String player)
    {
        if (board[0][0].equals(player) &&
            board[1][1].equals(player) &&
            board[2][2].equals(player))
        {
            return true;
        }
        return board[0][2].equals(player) &&
               board[1][1].equals(player) &&
               board[2][0].equals(player);
    }

    /**
     * Checks for a tie: true only if every one of the 8 winning lines
     * already contains both an X and an O, so no win is possible. Call
     * isWin first.
     *
     * @return true if no win is possible for either player
     */
    private boolean isTie()
    {
        // 3 rows
        for (int row = 0; row < ROW; row++)
        {
            if (!lineHasBoth(row, 0, row, 1, row, 2))
            {
                return false;
            }
        }
        // 3 columns
        for (int col = 0; col < COL; col++)
        {
            if (!lineHasBoth(0, col, 1, col, 2, col))
            {
                return false;
            }
        }
        // 2 diagonals
        if (!lineHasBoth(0, 0, 1, 1, 2, 2))
        {
            return false;
        }
        return lineHasBoth(0, 2, 1, 1, 2, 0);
    }

    /**
     * Checks whether a line of three squares contains at least one X and at
     * least one O.
     *
     * @return true if the line has both marks
     */
    private boolean lineHasBoth(int r1, int c1, int r2, int c2, int r3, int c3)
    {
        String[] line = { board[r1][c1], board[r2][c2], board[r3][c3] };
        boolean hasX = false;
        boolean hasO = false;
        for (String s : line)
        {
            if (s.equals("X"))
            {
                hasX = true;
            }
            else if (s.equals("O"))
            {
                hasO = true;
            }
        }
        return hasX && hasO;
    }
}
