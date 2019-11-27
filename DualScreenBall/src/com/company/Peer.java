package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Peer implements Runnable {
    private Socket socket;
    private OutputHandler outputHandler;
    private InputHandler inputHandler;
    private int port = 4321;
    private BallWorld bw;

    public Peer(BallWorld bw) {
        this.bw = bw;
    }

    @Override
    public void run() {
        // start server
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            System.out.println("Server started");
        } catch (IOException e) {
            System.exit(-1);
        }
        initHandlers(); // initialize input and output handlers
    }

    void connectToServer(String hostname) {
        System.out.println("Client started");
        System.out.print("Connecting to server");
        while (true) {
            try {
                socket = new Socket(hostname, port);
                System.out.println("\nConnected to server");
                break;
            } catch (Exception ignored) {
                System.out.print(".");
            }
        }
        initHandlers(); // initialize input and output handlers
    }

    private void initHandlers() {
        outputHandler = new OutputHandler(socket);
        new Thread(outputHandler).start();

        inputHandler = new InputHandler(socket);
        new Thread(inputHandler).start();

        inputHandler.setBallWorld(bw);
    }

    public void sendObject(Object obj) {
        outputHandler.setOutbox(obj);
    }
}
