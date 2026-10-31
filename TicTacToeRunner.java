import javax.swing.SwingUtilities;

/**
 * Starts the Tic Tac Toe GUI game.
 *
 * @author Asante
 * @version 1.0
 */
public class TicTacToeRunner
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            TicTacToeFrame frame = new TicTacToeFrame();
            frame.setVisible(true);
        });
    }
}
