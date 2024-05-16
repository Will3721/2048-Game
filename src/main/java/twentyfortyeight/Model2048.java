package twentyfortyeight;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * This class is a model for 2048.
 */
public class Model2048 {

    private Tile[][] board;
    private boolean gameWon;
    private boolean gameOver;
    private int score;

    private LinkedList<Tile[][]> moves = new LinkedList<>();
    private LinkedList<Integer> scores = new LinkedList<>();

    public Model2048() {
        reset();
    }

    // for testing only, so that I can pass in a set board layout
    public Model2048(Tile[][] testBoard) {
        reset();
        board = testBoard;
    }

    public void reset() {
        moves.clear();
        scores.clear();
        board = new Tile[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[j][i] = new Tile();
            }
        }
        gameWon = false;
        gameOver = false;
        score = 0;
        addTile();
        addTile();
        moves.add(getBoard());
        scores.add(score);
    }

    public Tile[][] getBoard() {
        Tile[][] copy = new Tile[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Tile copyTile = new Tile(board[i][j].getValue());
                copy[i][j] = copyTile;
            }
        }
        return copy;
    }

    public Tile getTile(int r, int c) {
        Tile copy = new Tile(board[r][c].getValue());
        return copy;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public int getScore() {
        return score;
    }

    // slides all the way to the right but does not merge
    public void slideRight(int row) {
        LinkedList<Tile> newRow = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            if (board[row][i].getValue() != 0) {
                newRow.add(board[row][i]);
            }
        }
        for (int i = newRow.size(); i < 4; i++) {
            newRow.addFirst(new Tile());
        }
        for (int i = 0; i < 4; i++) {
            board[row][i] = newRow.get(i);
        }
    }

    // slides and then merges tiles with the same value. slides one more time
    // afterward (if it can)
    // returns the points added from the merges
    public int combineRight(int row) {
        int pointsAdded = 0;
        Tile[] slided = slideRight(board[row]);

        for (int j = slided.length - 1; j > 0; j--) {
            if (slided[j].getValue() == slided[j - 1].getValue()) {
                slided[j].setValue(slided[j].getValue() * 2);
                slided[j - 1].setValue(0);
                pointsAdded += slided[j].getValue();
            }
        }
        board[row] = slideRight(slided); // slide one more time

        return pointsAdded;
    }

    // uses combineRight() on the board field, iterating through each row
    // also adds a new tile if board state changed
    public void moveRight() {
        boolean hasMoved = false;
        for (int i = 0; i < 4; i++) {
            Tile[] temp = board[i];
            int points = combineRight(i);
            if (!Arrays.equals(temp, board[i])) {
                hasMoved = true;
            }
            score += points;
        }
        if (hasMoved) {
            addTile();
        }
    }

    // moveUp, moveDown, moveLeft all use moveRight but with rotations

    public void moveUp() {
        rotateCW();
        moveRight();
        rotateCCW();
    }

    public void moveDown() {
        rotateCCW();
        moveRight();
        rotateCW();
    }

    public void moveLeft() {
        rotateCCW();
        rotateCCW();
        moveRight();
        rotateCW();
        rotateCW();
    }

    // general move method that gets called in GameBoard.java where the key event
    // listeners are
    // also adds to the history
    public void move(int dir) {
        switch (dir) {
            case 1:
                if (!isGameOver()) {
                    moveUp();
                    moves.add(getBoard());
                    scores.add(score);
                }
                break;
            case 2:
                if (!isGameOver()) {
                    moveDown();
                    moves.add(getBoard());
                    scores.add(score);
                }
                break;
            case 3:
                if (!isGameOver()) {
                    moveLeft();
                    moves.add(getBoard());
                    scores.add(score);
                }
                break;
            case 4:
                if (!isGameOver()) {
                    moveRight();
                    moves.add(getBoard());
                    scores.add(score);
                }
                break;
            default:
                break;
        }
    }

    // adapted from HW6
    public void rotateCW() {
        Tile[][] src = getBoard();
        Tile[][] tgt = new Tile[4][4];

        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                tgt[col][4 - row - 1] = src[row][col]; // swap coordinates
            }
        }
        board = tgt;
    }

    // adapted from HW6
    public void rotateCCW() {
        Tile[][] src = getBoard();
        Tile[][] tgt = new Tile[4][4];

        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                tgt[4 - col - 1][row] = src[row][col]; // swap coordinates
            }
        }
        board = tgt;
    }

    public void printGameState() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j].getValue());
                if (j < 3) {
                    System.out.print(" | ");
                }
            }
            if (i < 3) {
                System.out.println("\n-------------");
            }
        }
    }

    /**
     * spawns a new tile with either value 2 or 4 on an empty tile
     * a 2 will be spawned with probability 0.9 and 4 with probability 0.1
     */
    public void addTile() {
        boolean isEmpty = true;
        int val;
        while (isEmpty) {
            double ran = Math.random();
            int r = (int) (Math.random() * 4);
            int c = (int) (Math.random() * 4);
            if (board[r][c].getValue() == 0) {
                if (ran < 0.9) {
                    val = 2;
                } else {
                    val = 4;
                }
                board[r][c] = new Tile(val);
                isEmpty = false;
            }
        }
    }

    // undo previous move
    public void undo() {
        if (moves.size() > 1) {
            moves.removeLast();
            scores.removeLast();
            board = moves.getLast();
            score = scores.getLast();
            gameOver = false;
        }
    }

    // save game state into txt file
    public void save() {
        moves.add(getBoard());
        scores.add(score);

        try {
            FileWriter fWriter = new FileWriter("files/saved_game.txt");
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            for (int k = 0; k < moves.size(); k++) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        bWriter.write(moves.get(k)[i][j].getValue() + " ");
                    }
                }
                bWriter.write(scores.get(k) + " ");
                bWriter.write("\n");
            }
            bWriter.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null, "Couldn't write to file", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // load game from txt file
    public void load() {
        FileLineIterator li = new FileLineIterator("files/saved_game.txt");
        while (li.hasNext()) {
            String curr = li.next();
            String[] currState = curr.split(" ");
            scores.add(Integer.parseInt(currState[16]));
            Tile[][] currBoard = new Tile[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (i == 0) {
                        currBoard[i][j] = new Tile(Integer.parseInt(currState[j]));
                    } else if (i == 1) {
                        currBoard[i][j] = new Tile(Integer.parseInt(currState[j + 4]));
                    } else if (i == 2) {
                        currBoard[i][j] = new Tile(Integer.parseInt(currState[j + 8]));
                    } else {
                        currBoard[i][j] = new Tile(Integer.parseInt(currState[j + 12]));
                    }
                }
            }
            moves.add(currBoard);
        }
        board = moves.getLast();
        score = scores.getLast();
    }

    // checks if 2048 is on the board
    public boolean checkWin() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c].getValue() == 2048) {
                    gameWon = true;
                    return true;
                }
            }
        }
        gameWon = false;
        return false;
    }

    // returns the number of nonzero tiles
    public int numTiles() {
        int count = 0;
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c].getValue() != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    // checks if game is over
    public boolean isGameOver() {
        if (numTiles() != 16) {
            gameOver = false;
            return false;
        } else {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if (r == 0 && c == 0) {
                        if (board[r][c].getValue() == board[r + 1][c].getValue()
                                || board[r][c].getValue() == board[r][c + 1].getValue()) {
                            gameOver = false;
                            return false;
                        }
                    } else if (r == 0 && c == 3) {
                        if (board[r][c].getValue() == board[r + 1][c].getValue()
                                || board[r][c].getValue() == board[r][c - 1].getValue()) {
                            gameOver = false;
                            return false;
                        }
                    } else if (r == 3 && c == 0) {
                        if (board[r][c].getValue() == board[r - 1][c].getValue()
                                || board[r][c].getValue() == board[r][c + 1].getValue()) {
                            gameOver = false;
                            return false;
                        }
                    } else if (r == 3 && c == 3) {
                        if (board[r][c].getValue() == board[r - 1][c].getValue()
                                || board[r][c].getValue() == board[r][c - 1].getValue()) {
                            gameOver = false;
                            return false;
                        }
                    } else if (r == 0) {
                        if (board[r][c].getValue() == board[r][c - 1].getValue()
                                || board[r][c].getValue() == board[r][c + 1].getValue()
                                || board[r][c].getValue() == board[r + 1][c].getValue()) {
                            gameOver = false;
                            return false;
                        }
                    } else if (r == 3) {
                        if (board[r][c].getValue() == board[r][c - 1].getValue()
                                || board[r][c].getValue() == board[r][c + 1].getValue()
                                || board[r][c].getValue() == board[r - 1][c].getValue()) {
                            gameOver = false;
                            return false;
                        }
                    } else if (c == 0) {
                        if (board[r][c].getValue() == board[r][c + 1].getValue()
                                || board[r][c].getValue() == board[r - 1][c].getValue()
                                || board[r][c].getValue() == board[r + 1][c].getValue()) {
                            gameOver = false;
                            return false;
                        }
                    } else if (c == 3) {
                        if (board[r][c].getValue() == board[r][c - 1].getValue()
                                || board[r][c].getValue() == board[r - 1][c].getValue()
                                || board[r][c].getValue() == board[r + 1][c].getValue()) {
                            gameOver = false;
                            return false;
                        }
                    } else {
                        if (board[r][c].getValue() == board[r][c - 1].getValue()
                                || board[r][c].getValue() == board[r][c + 1].getValue()
                                || board[r][c].getValue() == board[r + 1][c].getValue()
                                || board[r][c].getValue() == board[r - 1][c].getValue()) {
                            gameOver = false;
                            return false;
                        }
                    }

                }
            }
            gameOver = true;
            return true;
        }
    }

    public static void main(String[] args) {
        Model2048 t = new Model2048();
        t.printGameState();
        t.move(1);
        System.out.println("\n");
        t.printGameState();
    }

    /**
     * methods used for testing only
     * same logic, but with different parameter and return type
     * so that I can control what is inputted and outputted
     */

    public Tile[] slideRight(Tile[] row) {
        Tile[] slided = new Tile[4];
        LinkedList<Tile> newRow = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            if (row[i].getValue() != 0) {
                newRow.add(row[i]);
            }
        }
        for (int i = newRow.size(); i < 4; i++) {
            newRow.addFirst(new Tile());
        }
        for (int i = 0; i < 4; i++) {
            slided[i] = newRow.get(i);
        }
        return slided;
    }

    public Tile[] combineRight(Tile[] row) {
        Tile[] combined;
        int pointsAdded = 0;
        Tile[] slided = slideRight(row);

        for (int j = slided.length - 1; j > 0; j--) {
            if (slided[j].getValue() == slided[j - 1].getValue()) {
                slided[j].setValue(slided[j].getValue() * 2);
                slided[j - 1].setValue(0);
                pointsAdded += slided[j].getValue();
            }
        }
        combined = slideRight(slided); // slide one more time

        score += pointsAdded;
        return combined;
    }

    public Tile[][] moveRight(Tile[][] board) {
        Tile[][] moved = new Tile[4][4];
        for (int i = 0; i < 4; i++) {
            moved[i] = combineRight(board[i]);
        }
        return moved;
    }
}
