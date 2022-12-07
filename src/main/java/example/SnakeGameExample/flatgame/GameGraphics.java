package example.SnakeGameExample.flatgame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;

/**
 * abstract class for implementing a game in 2d {@break}
 * Instructions: First, override abstract methods. In constructor of derived class, call {@code super(...)},
 * then do further JFrame adjusting, add a KeyListener, create all needed objects (especially the Paintable ones),
 * invoke {@code setVisible(true)} and finally, start the game with {@code startGameTimer()}
 */
public abstract class GameGraphics extends JFrame {

    private GameLogic gameLogic;
    private final int areaWidthPxl, areaHeightPxl;
    private int tickSpeed;
    private final Timer gameTimer;


    public GameGraphics(int width, int height, int tickSpeed, GameLogic gameLogic) {
        super();
        this.areaWidthPxl = width;
        this.areaHeightPxl = height;
        this.gameLogic = gameLogic;
        setTickSpeed(tickSpeed);
        super.setSize(getAreaWidthPxl(), getAreaHeightPxl());
        super.setUndecorated(true);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setLocationRelativeTo(null);
        super.setResizable(false);
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                paintGame(g);
            }
        };
        panel.setSize(getAreaWidthPxl(), getAreaHeightPxl());
        super.add(panel);
        //super.setVisible(true);

        // sometimes a repaint is necessary to prevent bugs
        //this.repaint();

        gameTimer = new Timer(tickSpeed, e -> {
            tick();
        });
    }

    /**
     * starts the game timer, that ticks in the specified tick speed
     */
    public void startGameTimer() {
        gameTimer.start();
    }

    /**
     * stops the game timer
     */
    public void stopGameTimer() {
        gameTimer.stop();
    }

    /**
     * tick method that can be invoked from outside, or is invoked every tick
     */
    public void tick() {
        gameLogic.tick();
        repaint();
    }

    public void paint() {
        repaint();
    }


    /**
     * abstract method that is invoked every tick, after updateGame(). Use g to pass to Paintable objects, which can draw "themselves"
     * @param g Graphics object which can be used to draw. Is gathered from paintComponent() method from JPanel
     */
    public abstract void paintGame(Graphics g);


    public int getAreaWidthPxl() {
        return areaWidthPxl;
    }

    public int getAreaHeightPxl() {
        return areaHeightPxl;
    }

    /**
     * sets the tick speed, ensures the tick speed is not smaller than 10ms
     * @param tickSpeed tick speed in ms
     * @throws IllegalArgumentException when tick speed is not 0 and below 10
     */
    public void setTickSpeed(int tickSpeed) {
        if(tickSpeed == 0) {
            if (gameTimer != null && gameTimer.isRunning()) {
                stopGameTimer();
            }
        }
        else if(tickSpeed < 10)
            throw new IllegalArgumentException("tick speed must be greater than 10ms to avoid bugs");


        this.tickSpeed = tickSpeed;
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.setInitialDelay(tickSpeed);
            gameTimer.setDelay(tickSpeed);
            gameTimer.restart();
        }
    }

    public int getTickSpeed() {
        return tickSpeed;
    }
}
