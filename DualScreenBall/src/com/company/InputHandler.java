package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class InputHandler implements Runnable {
    private Socket socket;
    private BallWorld bw;

    public InputHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        try {
            InputStream inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            System.out.println("input stream ready");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            Ball ball;
            try {
                if (objectInputStream != null) {
                    ball = (Ball) objectInputStream.readObject();
                    bw.addBall(ball);
                    System.out.println("received ball");
                }
            } catch (IOException | ClassNotFoundException ignored) {
            }
        }
    }

    public void setBallWorld(BallWorld bw) {
        this.bw = bw;
    }
}
