package example.SnakeGameExample.snakegame;


import example.SnakeGameExample.flatgame.GameGraphics;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SnakeGame extends GameGraphics implements KeyListener{
    public static final int SQUARE_SIZE = 40;

    private SnakeGameLogic logic;

    public SnakeGame(SnakeGameLogic logic) {
        super(SQUARE_SIZE * SnakeGameLogic.FIELD_WIDTH, SQUARE_SIZE * SnakeGameLogic.FIELD_HEIGHT,
                32, logic);
        this.logic = logic;
        super.setTitle("Score: 0");
        super.setUndecorated(true);
        super.addKeyListener(this);


        super.setVisible(true);
        //startGameTimer();
    }

    @Override
    public void paintGame(Graphics g) {
        // paint background
        g.setColor(new Color(0x575757));
        g.fillRect(0, 0, getAreaWidthPxl(), getAreaHeightPxl());


        //paint snake
        logic.getSnake().paint(g);

        //paint apple
        logic.getApple().paint(g);

        // paint score
        g.setColor(Color.white);
        g.drawString("Score: " + logic.getScore(), 5, 15);

        g.setColor(Color.white);
        for(int x = 0; x < SnakeGameLogic.FIELD_WIDTH; x++) {
            g.drawLine(x*SQUARE_SIZE, 0, x*SQUARE_SIZE, SnakeGameLogic.FIELD_HEIGHT*SQUARE_SIZE);
        }
        for(int y = 0; y < SnakeGameLogic.FIELD_HEIGHT; y++) {
            g.drawLine(0, y*SQUARE_SIZE, SnakeGameLogic.FIELD_WIDTH*SQUARE_SIZE, y*SQUARE_SIZE);
        }

    }




    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_X)
            System.exit(0);

        else {
            /*
            if(e.getKeyCode() == KeyEvent.VK_W)
                logic.getSnake().setDirection(Snake.Direction.UP);

            else if(e.getKeyCode() == KeyEvent.VK_A)
                logic.getSnake().setDirection(Snake.Direction.LEFT);

            else if(e.getKeyCode() == KeyEvent.VK_S)
                logic.getSnake().setDirection(Snake.Direction.DOWN);

            else if(e.getKeyCode() == KeyEvent.VK_D)
                logic.getSnake().setDirection(Snake.Direction.RIGHT);*/

            if(e.getKeyCode() == KeyEvent.VK_U) {
                if(SnakeAi.gamespeed > 10) SnakeAi.gamespeed -= 10;
            }
            if(e.getKeyCode() == KeyEvent.VK_D)
                SnakeAi.gamespeed += 10;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
