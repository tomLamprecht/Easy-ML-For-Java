package example.SnakeGameExample.snakegame;

import java.awt.*;

public class Part extends Item {

    public Part(int x, int y) {
        super(x, y);
    }

    @Override
    public void paint(Graphics g) {
        g.fillRect(getX() * SnakeGame.SQUARE_SIZE, getY() * SnakeGame.SQUARE_SIZE, SnakeGame.SQUARE_SIZE, SnakeGame.SQUARE_SIZE);
    }

}
