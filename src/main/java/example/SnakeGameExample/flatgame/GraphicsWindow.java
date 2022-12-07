package example.SnakeGameExample.flatgame;

import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;

public abstract class GraphicsWindow extends JFrame {
    private Timer timer;

    /**
     * instructions: extend from this class. In the constructor define other properties if you want.
     * overwrite paint method (describes how your window will be painted)
     * if done, call display().
     * repeatedly call repaint() to update your frame.
     * @param width
     * @param height
     */
    public GraphicsWindow(int width, int height) {
        super();
        super.setSize(width, height);
        super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        super.setLocationRelativeTo(null);
        super.setResizable(false);

        JPanel root = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                paint(g);
            }
        };
        root.setSize(width, height);
        super.add(root);
    }

    public void display() {
        super.setVisible(true);
        super.pack();
    }

    public void activateFrameRate(int period) {
        timer = new Timer(period, e -> repaint());
        timer.start();
    }

    public void deactivateFrameRate() {
        timer.stop();
        timer = null;
    }

    public abstract void paint(Graphics g);

}
