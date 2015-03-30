package com.example.rcarley.uesapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

//import com.github.eventsource.client.EventSource;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class main extends ActionBarActivity {
    streamConnection sC = null;
    private TextView textView;
    InputStream in;
    BufferedReader bufferedReader;
    HttpURLConnection urlConnection;
    ArrayList<String> aList = new ArrayList<String>();
    int startCount = 0;
    boolean canUpdate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textView = (TextView) findViewById(R.id.textView);
        //socketListener sl = new socketListener();
        //sC = new streamConnection();
        //sC.createConnection();
        startListen();
        updateTime();



    }

    public void startListen(){
        startCount++;
        System.out.println("Starting Listener:" + startCount);
        setupStreamConnection();

        Listener task = new Listener();
        task.execute(new String[] { "nothing" });

    }

    public void refreshList(){

        //if(!canUpdate){return;} //Don't load too often

        ScrollView deviceScrollView   = (ScrollView)findViewById(R.id.sensorList);
        LinearLayout mLayout=(LinearLayout)findViewById(R.id.linearLayoutScroll);

        /*while(aList.size() > 50){
            aList.remove(0);
        }*/


        deviceScrollView.removeAllViews();
        mLayout.removeAllViews();
        for(int i = aList.size() - 1 ; i > aList.size() - 50 && i >= 0 ;i--){
            String ts = aList.get(i);

            //TextView tv = new TextView(this);
            //textView.setText(ts);
           //mLayout.addView(textView);
            //deviceScrollView.addView(mLayout);

            TextView tv = new TextView(this);
            tv.setText(ts);
            mLayout.addView(tv);


        }
        deviceScrollView.addView(mLayout);
        canUpdate=false;
        //updateTime();
    }

    public void updateTime(){
        Timer timer;
        timer = new Timer();
        timer.schedule(new RemindTask(),  50); //Every half second
    }



    public void setupStreamConnection(){
        URL url = null;
        URL url2 = null;
        JSONObject json = new JSONObject();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.1.5.35/api/sub/");

            System.out.println("In createConnection");
            try{
                url = new URL("http://10.1.5.35/api/sub/alert");
                //url = new URL("http://10.1.5.35/api/sub/");

            } catch(Exception e){
                System.out.println("Exception making URL(???):" + e);
            }

        HttpResponse response = null;
        String responseText = "";
       /* try {
            // Add type=alerts
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            json.put("type", "alerts");
            StringEntity se = new StringEntity( json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);

            // Execute HTTP Post Request
            response = httpclient.execute(httppost);

            System.out.println("response:" + response.getEntity().toString());

            try{
                // Convert String to json object
                JSONObject jres = new JSONObject(responseText);

                // get results json object
                //JSONObject json_results = jres.getJSONObject("results");

                // get value from LL Json Object
                String str_value=jres.getString("results");
                System.out.println("str_value:" + str_value);

            } catch(Exception e) {
                System.out.println("Exception:" + e);
            }


        } catch (ClientProtocolException e) {
            System.out.println("ClientProtocolException:" + e);

        } catch (IOException e) {
            System.out.println("IOException:" + e);
        }
        catch(Exception e) {
            System.out.println("Exception:" + e);
        }*/


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





    public void listSensors(View v){
        String[] arr = null;

        httpRequests req = new httpRequests();
        JSONObject response = req.requestGet("device", "?types=sensor");
        try{
            JSONArray jsonArr= response.getJSONArray("devices");
            arr=new String[jsonArr.length()];
            for(int i=0;i<jsonArr.length();i++) {
                arr[i] = jsonArr.get(i).toString();
                System.out.println("Device:" + i + " = " + arr[i]);
            }
        } catch(Exception e){

        }

        ScrollView deviceScrollView   = (ScrollView)findViewById(R.id.scrollView2);
        if(!arr[0].equals("")){
            View mTableRow = null;
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            deviceScrollView.removeAllViews();
            for(int i = 0; i < arr.length;i++){


                // Add text
                TextView tv = new TextView(this);
                tv.setText(arr[i]);
                ll.addView(tv);


                // setContentView(v);
                //mTableRow = (TableRow) View.inflate(getActivity(), R.layout.mRowLayout, null);
            }
            deviceScrollView.addView(ll);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            httpRequests req = new httpRequests();
            req.logout();
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class Listener extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... urls) {
            // Need a way to restart connection when it dies
            String response = "";
            try {
                String line = null;


                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println("Got in on httpURLConnection:" + line);
                    response += line;
                    publishProgress(line);
                }
            } catch (Exception e) {
                System.out.println("Exception listening:" + e);
                //send message that it died, tell main activity to restart it?
            }
            return response;
        }

        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            //prgDialog.setProgress(Integer.parseInt(progress[0]));
            System.out.println("got progress[0]:" + progress[0]);
            //textView.setText(progress[0]);
            if(progress[0].equals("") || progress[0].equals("event:alert")){
                return;
            }
            try{
                // Convert String to json object
                JSONObject json = new JSONObject("{" + progress[0] + "}");
                // get data json object
                JSONObject json_data = json.getJSONObject("data");

                //str_timestamp
                String str_timestamp = "";
                try{
                    str_timestamp=json_data.getString("timestamp");
                    String[] spl = str_timestamp.split("T");
                    String[] spl2 = spl[1].split("\\.");
                    str_timestamp = spl[0] + " " + spl2[0];

                    System.out.println("str_timestamp:" + str_timestamp);
                } catch(Exception e){

                }


                //src_ip
                String src_ip = "";
                try{
                    src_ip = json_data.getString("src_ip");
                    System.out.println("src_ip:" + src_ip);
                } catch(Exception e){

                }



                //dst_ip
                String dst_ip = "";
                try{
                    dst_ip=json_data.getString("dest_ip");
                    System.out.println("dst_ip:" + dst_ip);
                } catch(Exception e){

                }



                //textView.setText(str_timestamp);
                ScrollView deviceScrollView   = (ScrollView)findViewById(R.id.sensorList);
                LinearLayout mLayout=(LinearLayout)findViewById(R.id.linearLayoutScroll);
                //textView = (TextView) findViewById(R.id.textView);
                //mLayout.removeAllViews();
                //mLayout.addView(textView);
                if(aList.size() > 100){
                    aList.clear();
                }
                aList.add(src_ip + " -> " + dst_ip + "  " + str_timestamp);
                //updateList();
                refreshList();


                //ScrollView deviceScrollView   = (ScrollView)findViewById(R.id.sensorList);
                //deviceScrollView.removeAllViews();
                //deviceScrollView.addView(mLayout);
                // get value from LL Json Object
                //String str_event=json.getString("event");
               // System.out.println("str_event:" + str_event);

            } catch(Exception e) {
                System.out.println("Exception:" + e);
            }

        }
        public void updateList(){
            ScrollView deviceScrollView   = (ScrollView)findViewById(R.id.sensorList);
            LinearLayout mLayout = (LinearLayout)findViewById(R.id.linearLayoutScroll);
            mLayout.removeAllViews();
            deviceScrollView.removeAllViews();

            for(int i = 0; i < aList.size();i++){
                String ts = aList.get(i);

                //TextView tv = new TextView(this);
                textView.setText(ts);
                mLayout.addView(textView);
                deviceScrollView.addView(mLayout);

            }
        }



        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
            startListen();

            /*try{
                // Convert String to json object
                JSONObject json = new JSONObject(result);
                // get data json object
                JSONObject json_data = json.getJSONObject("data");

                String str_timestamp=json.getString("timestamp");
                System.out.println("str_timestamp:" + str_timestamp);
                //textView.setText(str_timestamp);

                //src_ip
                String src_ip=json.getString("src_ip");
                System.out.println("str_timestamp:" + src_ip);

                // get value from LL Json Object
                String str_event=json.getString("event");
                System.out.println("str_event:" + str_event);

            } catch(Exception e) {
                System.out.println("Exception:" + e);
            }*/
        }

    }
    class RemindTask extends TimerTask {
        public void run() {
            System.out.println("Time's up!");
            canUpdate = true;

            //timer.cancel(); //Not necessary because we call System.exit
            //System.exit(0); //Stops the AWT thread (and everything else)
        }
    }
}
