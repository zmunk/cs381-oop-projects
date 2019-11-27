package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class OutputHandler implements Runnable {
    private Object outbox = null;
    private Socket socket;

    public OutputHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        ObjectOutputStream objectOutputStream = null;
        try {
            OutputStream outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            if (outbox != null && objectOutputStream != null) {
                try {
                    System.out.println("sending ball");
                    objectOutputStream.writeObject(outbox);
                    outbox = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOutbox(Object obj) {
        outbox = obj;
    }
}
