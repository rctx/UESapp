package com.example.rcarley.uesapp;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rcarley on 3/27/2015.
 */
public class streamConnection {
    InputStream in;
    BufferedReader bufferedReader;
    //private Listener listener;
    HttpURLConnection urlConnection;
    int listen;

    public void streamConnection(){
        URL url = null;
        listen = 1;
        System.out.println("In createConnection");
        try{
            url = new URL("http://10.1.5.35/api/sub/alert");
        } catch(Exception e){
            System.out.println("Exception making URL(???):" + e);
        }

        urlConnection = null;
        try{
            System.setProperty("http.keepAlive", "true");
            urlConnection = (HttpURLConnection) url.openConnection();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //in = new BufferedInputStream(urlConnection.getInputStream());
            //in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            //in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            //if (listener == null) {
                //listener = new Listener();
                //Thread thread = new Thread(listener);
                //System.out.println("Starting listener Thread");
                //thread.start();
                urlConnection.setReadTimeout((int)300000);//Open for 5 min
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public class Listener extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            // Need a way to restart connection when it dies
            String response = "";
            try {
                String line = null;


                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println("Got in on httpURLConnection:" + line);
                    response += line;
                }
            } catch (Exception e) {
                System.out.println("Exception listening:" + e);
                //send message that it died, tell main activity to restart it?
            }
            return response;
        }

        /*@Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }*/

    //}



    /*public class Listener implements Runnable {

        @Override
        public void run() {
            // Need a way to restart connection when it dies
            //while(listen == 1) {// Probably a bad idea
                try {
                    String line = null;


                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println("Got in on httpURLConnection:" + line);

                    }
                } catch (Exception e) {
                    System.out.println("Exception listening:" + e);
                    //send message that it died, tell main activity to restart it?
                }
            //}
        }

    }*/
}
