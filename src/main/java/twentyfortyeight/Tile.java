package twentyfortyeight;

import java.awt.*;

public class Tile {
    private int value;
    private Color color;

    public Tile() {
        value = 0;
        color = Color.WHITE;
    }

    public Tile(int value) {
        this.value = value;
        setColor();
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        setColor();
        return color;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setColor() {
        switch (value) {
            case 0:
                color = Color.WHITE;
                break;
            case 2:
                color = new Color(238, 228, 218);
                break;
            case 4:
                color = new Color(237, 224, 200);
                break;
            case 8:
                color = new Color(242, 177, 121);
                break;
            case 16:
                color = new Color(245, 149, 99);
                break;
            case 32:
                color = new Color(246, 124, 95);
                break;
            case 64:
                color = new Color(246, 94, 59);
                break;
            case 128:
                color = new Color(237, 207, 114);
                break;
            case 256:
                color = new Color(237, 204, 97);
                break;
            case 512:
                color = new Color(237, 200, 80);
                break;
            case 1024:
                color = new Color(237, 197, 63);
                break;
            case 2048:
                color = new Color(237, 194, 46);
                break;
            default:
                color = Color.WHITE;
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.getValue() == ((Tile) o).getValue();
    }
}
