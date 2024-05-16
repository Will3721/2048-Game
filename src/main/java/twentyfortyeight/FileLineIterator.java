package twentyfortyeight;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.io.BufferedReader;
import java.util.NoSuchElementException;

public class FileLineIterator implements Iterator<String> {

    private BufferedReader reader;
    private String currentLine;

    public FileLineIterator(BufferedReader reader) {
        if (reader == null) {
            throw new IllegalArgumentException();
        }
        this.reader = reader;
        try {
            currentLine = reader.readLine();
        } catch (IOException e) {
            currentLine = null;
            JOptionPane.showMessageDialog(
                    null, "Couldn't read file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public FileLineIterator(String filePath) {
        this(fileToReader(filePath));
    }

    public static BufferedReader fileToReader(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException();
        }
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        }
        return bReader;
    }

    @Override
    public boolean hasNext() {
        if (currentLine != null) {
            return true;
        } else {
            try {
                reader.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        null, "Couldn't close reader", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }

    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        String next = currentLine;

        try {
            currentLine = reader.readLine();
        } catch (IOException e) {
            currentLine = null;
            JOptionPane.showMessageDialog(
                    null, "Couldn't read file", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return next;
    }
}
