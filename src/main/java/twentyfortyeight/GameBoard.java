package twentyfortyeight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Model2048 model; // model for the game
    private JLabel status; // current status text
    private JLabel score; // current score text

    // Game constants
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit, JLabel scoreInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        model = new Model2048(); // initializes model for the game
        status = statusInit;
        score = scoreInit; // initializes the status JLabel

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!model.getGameOver()) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        model.move(3);
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        model.move(4);
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        model.move(2);
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        model.move(1);
                    }
                    updateScore();
                    repaint();
                }
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        model.reset();
        updateScore();
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void instructions() {
        final JFrame frame = new JFrame("Instructions");
        frame.setLocation(0, 0);

        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.NORTH);

        String text = "<html>Use the arrow keys to move the tiles. Tiles with the same number " +
                "merge into one tile with double the value when they touch. <br>You " +
                "lose if the board fills up "
                +
                "and you can't make "
                +
                "any more moves. You win if you reach 2048! <br>" +
                "Your score increases by the value(s) of the new block(s) you create. <br>" +
                "You can reset the game to start over, save the game so you can " +
                "pause and play later, "
                +
                "load your last saved game, or undo your last move.";

        final JLabel manual = new JLabel();
        manual.setText(text);
        status_panel.add(manual);

        frame.pack();
        frame.setVisible(true);
        // makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void save() {
        model.save();
        requestFocusInWindow();
    }

    public void load() {
        model.load();
        repaint();
        updateScore();
        requestFocusInWindow();
    }

    public void undo() {
        model.undo();
        updateScore();
        repaint();
        requestFocusInWindow();
    }

    private void updateScore() {
        if (!model.getGameOver()) {
            status.setText("Game in progress... ");
            score.setText("Score: " + model.getScore());
        } else {
            status.setText("You lose! ");
            score.setText("Final score: " + model.getScore());
        }
        if (model.checkWin()) {
            status.setText("You win! Keep going... ");
            score.setText("Score: " + model.getScore());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        g.drawLine(100, 0, 100, 400);
        g.drawLine(200, 0, 200, 400);
        g.drawLine(300, 0, 300, 400);
        g.drawLine(0, 100, 400, 100);
        g.drawLine(0, 200, 400, 200);
        g.drawLine(0, 300, 400, 300);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Tile curr = model.getTile(i, j);
                g.setColor(curr.getColor());
                g.fillRect(1 + 100 * j, 1 + 100 * i, 99, 99);
                g.setColor(Color.BLACK);
                if (curr.getValue() != 0) {
                    g.drawString(String.valueOf(curr.getValue()), 45 + 100 * j, 50 + 100 * i);
                }
            }
        }

    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
