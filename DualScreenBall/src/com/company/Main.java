package com.company;

import javax.swing.*;
import java.net.UnknownHostException;

/*
Code by Ibrahim Tigrek

This code allows a ball to be sent from one screen to another.
One of the screens starts the server, and the other starts the client to connect to the server.
Therefore, two instances of the program should be running on separate computers for this to work.

NOTE: This demo will currently run on a single screen with two windows.
To run it on two computers, open the project on both computers and comment out the second block in the main
function of the first computer and comment out the first block in the main function of the second computer.
 */


public class Main {

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        ///////////////// COMPUTER 1
        BallWorld bw1 = new BallWorld(640, 480);
        Peer peer1 = new Peer(bw1);
        new Thread(peer1).start(); // thread opens socket and waits for client to connect
        bw1.initBall(); // initialize the ball
        bw1.setDisplay(peer1, "left");

        JFrame frame1 = new JFrame("Peer 1 (left)");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setContentPane(bw1);
        frame1.pack();
        frame1.setVisible(true);
        ///////////////////////////////////////////////////////////////


        ///////////////// COMPUTER 2
        BallWorld bw2 = new BallWorld(640, 480);
        Peer peer2 = new Peer(bw2);
        // connects to server at specified ip
        peer2.connectToServer("10.52.10.12"); // (ip of server)
        bw2.setDisplay(peer2, "right");
        Thread.sleep(300);

        JFrame frame2 = new JFrame("Peer 2 (right)");
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setContentPane(bw2);
        frame2.pack();
        frame2.setVisible(true);
        ///////////////////////////////////////////////////////////////
    }
}
