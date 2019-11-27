package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;

/**
 * The control logic and main display panel for game.
 *
 * @author Hock-Chuan Chua
 * @version October 2010
 * modified by e gul
 */
public class BallWorld extends JPanel {
    private static final int UPDATE_RATE = 30;    // Frames per second (fps)
    private Ball ball;
    private ContainerBox box;  // The container rectangular box

    private DrawCanvas canvas; // Custom canvas for drawing the box/ball
    private int canvasWidth;
    private int canvasHeight;

    private Peer peer;
    private String screen;


    public BallWorld(int width, int height) {
        canvasWidth = width;
        canvasHeight = height;
        ball = null;
        // Init the Container Box to fill the screen
        box = new ContainerBox(0, 0, canvasWidth, canvasHeight, Color.BLACK, Color.WHITE);

        // Init the custom drawing panel for drawing the game
        canvas = new DrawCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);

        // Handling window resize.
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component c = (Component)e.getSource();
                Dimension dim = c.getSize();
                canvasWidth = dim.width;
                canvasHeight = dim.height;
                // Adjust the bounds of the container to fill the window
                box.set(0, 0, canvasWidth, canvasHeight);
            }
        });

        // Start the ball bouncing
        gameStart();
    }

    public void initBall() {
        // Init the ball at a random location (inside the box) and moveAngle
        Random rand = new Random();

        int radius = 20;
        int x = rand.nextInt(canvasWidth - radius * 2 - 20) + radius + 10;
        int y = rand.nextInt(canvasHeight - radius * 2 - 20) + radius + 10;

        int speed = 10 + rand.nextInt(10);
        int angleInDegree = rand.nextInt(360);
        ball = new Ball(x, y, radius, speed, angleInDegree);
        ball.setBallWorld(this);
    }

    public void addBall(Ball ball) {
        this.ball = ball;
        this.ball.setBallWorld(this);
    }


    public void gameStart(){
        GameThread gmthr = new GameThread(this, UPDATE_RATE);
        gmthr.start();

    }

    /**
     * One game time-step.
     * Update the game objects, with proper collision detection and response.
     */
    public void gameUpdate() {
        if (ball == null) {
            return;
        }
        ball.moveOneStepWithCollisionDetection(box);
    }

    public void setDisplay(Peer peer, String screen) {
        this.peer = peer;
        this.screen = screen;
    }

    public void checkTransfer(float minX, float maxX) {
        if (ball.x < minX) {
            if (screen.equals("left")) {
                ball.speedX = -ball.speedX;
                ball.x = minX;
            } else {
                float angle = (float) (- Math.atan2(ball.speedY, ball.speedX) * 180 / Math.PI);
                peer.sendObject(new Ball(maxX, ball.y, ball.radius, ball.speed, angle));
                ball = null;
            }
        } else if (ball.x > maxX) {
            if (screen.equals("left")) {
                float angle = (float) (- Math.atan2(ball.speedY, ball.speedX) * 180 / Math.PI);
                peer.sendObject(new Ball(minX, ball.y, ball.radius, ball.speed, angle));
                ball = null;
            } else {
                ball.speedX = -ball.speedX;
                ball.x = maxX;
            }
        }
    }


    /** The custom drawing panel for the bouncing ball (inner class). */
    class DrawCanvas extends JPanel {
        /** Custom drawing codes */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);    // Paint background
            // Draw the box and the ball
            box.draw(g);
            if (ball != null) {
                ball.draw(g);
            }
        }

        /** Called back to get the preferred size of the component. */
        @Override
        public Dimension getPreferredSize() {
            return (new Dimension(canvasWidth, canvasHeight));
        }
    }
}
