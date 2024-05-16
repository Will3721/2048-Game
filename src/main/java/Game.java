import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        Runnable game = new twentyfortyeight.RunTwentyFortyEight();
        SwingUtilities.invokeLater(game);

    }
}
