package twentyfortyeight;

import javax.swing.*;
import java.awt.*;

public class RunTwentyFortyEight implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("2048");
        frame.setLocation(400, 400);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Game in progress... ");
        status_panel.add(status);
        final JLabel score = new JLabel("Score: 0");
        status_panel.add(score);

        // Game board
        final GameBoard board = new GameBoard(status, score);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(e -> board.instructions());
        control_panel.add(instructions);

        final JButton save = new JButton("Save");
        save.addActionListener(e -> board.save());
        control_panel.add(save);

        final JButton load = new JButton("Load");
        load.addActionListener(e -> board.load());
        control_panel.add(load);

        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        control_panel.add(undo);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}