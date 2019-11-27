package com.company;
import java.awt.*;
import java.io.Serializable;

/**
 * The bouncing ball.
 *
 * @author Hock-Chuan Chua
 * @version v0.4 (31 October 2010)
 */
public class Ball implements Serializable {
    float x, y;           // Ball's center x and y (package access)
    float speed;
    float speedX, speedY; // Ball's speed per step in x and y (package access)
    float radius;         // Ball's radius (package access)
    private Color color;  // Ball's color
    private static final Color DEFAULT_COLOR = Color.BLUE;
    private BallWorld bw;
    /**
     * Constructor: For user friendliness, user specifies velocity in speed and
     * moveAngle in usual Cartesian coordinates. Need to convert to speedX and
     * speedY in Java graphics coordinates for ease of operation.
     */
    public Ball(float x, float y, float radius, float speed, float angleInDegree) {
        this.x = x;
        this.y = y;
        // Convert (speed, angle) to (x, y), with y-axis inverted
        this.speed = speed;
        this.speedX = (float)(speed * Math.cos(Math.toRadians(angleInDegree)));
        this.speedY = (float)(-speed * Math.sin(Math.toRadians(angleInDegree)));
        this.radius = radius;
        this.color = DEFAULT_COLOR;
    }

    public void setBallWorld(BallWorld bw) {
        this.bw = bw;
    }

    public boolean moveOneStepWithCollisionDetection(ContainerBox box) {
        // Get the ball's bounds, offset by the radius of the ball
        float ballMinX = box.minX + radius;
        float ballMinY = box.minY + radius;
        float ballMaxX = box.maxX - radius;
        float ballMaxY = box.maxY - radius;

        // Calculate the ball's new position
        x += speedX;
        y += speedY;
        // Check if the ball moves over the bounds. If so, adjust the position and speed.
        if (x < ballMinX || x > ballMaxX) {
            bw.checkTransfer(ballMinX, ballMaxX);
        }
        // May cross both x and y bounds
        if (y < ballMinY) {
            speedY = -speedY;
            y = ballMinY;
        } else if (y > ballMaxY) {
            speedY = -speedY;
            y = ballMaxY;
        }
        return true;
    }

    /** Draw itself using the given graphics context. */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    /** Return the magnitude of speed. */
    public float getSpeed() {
        return (float)Math.sqrt(speedX * speedX + speedY * speedY);
    }

    /** Return the direction of movement in degrees (counter-clockwise). */
    public float getMoveAngle() {
        return (float)Math.toDegrees(Math.atan2(-speedY, speedX));
    }

    /** Describe itself. */
    public String toString() {
        return "";
    }
}
