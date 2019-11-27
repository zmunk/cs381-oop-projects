package com.company;

import java.io.IOException;

/**
 *
@author Hock-Chuan Chua
        * @version 31 October 2010
*game thread is declared as a separate class not inner class
* modified by e gul
*/
public class GameThread extends Thread {
    BallWorld bw;
    int updaterate;

    public GameThread(BallWorld bw , int updaterate) {   // constructor
        this.bw = bw;
        this.updaterate =updaterate;
    }
    public void run() {
        while (true) {
            // Execute one time-step for the game
            bw.gameUpdate();
            // Refresh the display
            bw.repaint();
            // Delay and give other thread a chance
            try {
                Thread.sleep(1000 / updaterate);
            } catch (InterruptedException ex) {

            }
        }
    }

}
