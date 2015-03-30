package com.example.rcarley.uesapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by rcarley on 3/27/2015.
 */
public class socketListener {

    private Socket socket = null;
    private BufferedReader in = null;
    //private String host = "10.1.5.35/api/sub/alert";
    private String host = "10.1.5.35";
    private int port = 80;

    private Listener listener;

    public socketListener() {
        System.out.println("In socketListener");
        try {

            if (socket == null) {
                socket = new Socket(this.host, this.port);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Starting socket");
            }

            if (listener == null) {
                listener = new Listener();
                Thread thread = new Thread(listener);
                System.out.println("Starting listener Thread");
                thread.start();
            }

        } catch (Exception e) {
            // ...
            System.out.println("Exception Starting socket:" + e);
        }
    }

    public class Listener implements Runnable {

        @Override
        public void run() {

            try {
                String line = null;

                while ((line = in.readLine()) != null) {
                    // Do something. Never gets here
                    System.out.println("Got in on socket:" + line);

                }
            } catch (Exception e) {
                // ...
            }

        }

    }
}




