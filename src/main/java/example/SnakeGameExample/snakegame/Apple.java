package example.SnakeGameExample.snakegame;

import java.awt.*;

public class Apple extends Item {
    private int score;

    public Apple(int x, int y, int score) {
        super(x, y);
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(0xFF0000));
        g.fillRect(getX() * SnakeGame.SQUARE_SIZE, getY() * SnakeGame.SQUARE_SIZE, SnakeGame.SQUARE_SIZE, SnakeGame.SQUARE_SIZE);
    }
}
