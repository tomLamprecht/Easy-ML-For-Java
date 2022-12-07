package example.SnakeGameExample.snakegame;


import example.SnakeGameExample.flatgame.Paintable;

import java.awt.*;
import java.util.ArrayList;

public class Snake implements Paintable {
    private ArrayList<Part> parts;
    private Direction direction;
    private int rgb;
    private int frame;
    public static final int COLOR_INTERVAL = 0xFFFFFF / 26;

    public enum Direction {LEFT, UP, RIGHT, DOWN};

    public Snake(int x, int y, int initialLength) {
        if (initialLength < 2)
            initialLength = 2;
        parts = new ArrayList<>();
        for (int i = 0; i < initialLength; i++) {
            parts.add(new Part(x, y + i));
        }
        direction = Direction.UP;
    }

    public void setDirection(Direction direction) {
        switch (direction) {
            case LEFT:
                if (this.direction != Direction.RIGHT)
                    this.direction = direction;
                break;
            case RIGHT:
                if (this.direction != Direction.LEFT)
                    this.direction = direction;
                break;
            case UP:
                if (this.direction != Direction.DOWN)
                    this.direction = direction;
                break;
            case DOWN:
                if (this.direction != Direction.UP)
                    this.direction = direction;
                break;
        }
    }

    public boolean move() {
        // get coordinates of head
        int x = parts.get(0).getX();
        int y = parts.get(0).getY();

        boolean outOfBorder = false;
        // update new coordinates
        switch (direction) {
            case LEFT:
                x--;
                if (x < 0) {
                    x = SnakeGameLogic.FIELD_WIDTH - 1;
                    outOfBorder = true;
                }
                break;
            case RIGHT:
                x++;
                if (x >= SnakeGameLogic.FIELD_WIDTH) {
                    x = 0;
                    outOfBorder = true;
                }
                break;
            case UP:
                y--;
                if (y < 0) {
                    y = SnakeGameLogic.FIELD_HEIGHT - 1;
                    outOfBorder = true;
                }
                break;
            case DOWN:
                y++;
                if (y >= SnakeGameLogic.FIELD_HEIGHT) {
                    y = 0;
                    outOfBorder = true;
                }
                break;
        }


        // update body
        for (Part p : parts) {
            int tempX = p.getX();
            int tempY = p.getY();
            p.setPos(x, y);
            x = tempX;
            y = tempY;
        }
        return outOfBorder;
    }

    public boolean collidesWithHead(Item item) {
        return collidesWithHead(item.getX(), item.getY());
    }

    public boolean collidesWithHead(int x, int y) {
        int headX = parts.get(0).getX();
        int headY = parts.get(0).getY();
        if(x == headX && y == headY)
            return true;
        return false;
    }

    public boolean collidesWith(int x, int y) {
        for(int i = 0; i < parts.size(); i++) {
            int partX = parts.get(i).getX();
            int partY = parts.get(i).getY();
            if(x == partX && y == partY)
                return true;
        }
        return false;
    }

    public boolean collidesWithSelf() {
        for(int i = 1; i < parts.size(); i++) {
            if(collidesWithHead(parts.get(i)))
                return true;
        }
        return false;
    }

    public void grow() {
        // get coordinates of last part
        int x = parts.get(parts.size()-1).getX();
        int y = parts.get(parts.size()-1).getY();
        // add new part
        parts.add(new Part(x, y));
    }

    public void paint(Graphics g) {
        // comment in if snake should blink
        /*frame++;
        frame %= 4;
        if (frame == 0) {
            rgb += COLOR_INTERVAL % 0xFFFFFF;
        }*/
        g.setColor(new Color(rgb));
        for (Part p : parts) {
            p.paint(g);
        }
    }

    public ArrayList<Part> getParts() {
        return parts;
    }

    public Item getHead() {
        return parts.get(0);
    }

}
