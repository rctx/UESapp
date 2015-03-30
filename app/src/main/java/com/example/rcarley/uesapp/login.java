package com.example.rcarley.uesapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);


        // For JSON responses
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public void loginButtonOnClick(View v) {
        // do something when the button is clicked
        System.out.println("loginButtonOnClick");

        boolean authenticated = loginPost();
        System.out.println("authenticated:" + authenticated);

        if(authenticated){
            try
            {
                Intent k = new Intent(login.this, main.class);
                startActivity(k);
            }catch(Exception e){

            }
        }else{
            // Tell the user the login failed
        }
    }

    public boolean loginPost(){

        boolean authenticated = false;

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.1.5.35/api/user/authentication");

        JSONObject json = new JSONObject();

        HttpResponse response = null;
        try {
            // Add login data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            //nameValuePairs.add(new BasicNameValuePair("userName", "testignore"));
            //nameValuePairs.add(new BasicNameValuePair("password", "test1Ignore"));
            TextView usernameText   = (TextView)findViewById(R.id.username);
            TextView passwordText   = (TextView)findViewById(R.id.password);
            json.put("userName", usernameText.getText());
            json.put("password", passwordText.getText());
            StringEntity se = new StringEntity( json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setEntity(se);

            // Execute HTTP Post Request
            response = httpclient.execute(httppost);

            System.out.println("response:" + response.getEntity().toString());

        } catch (ClientProtocolException e) {
            System.out.println("ClientProtocolException:" + e);

        } catch (IOException e) {
            System.out.println("IOException:" + e);
        }
        catch(Exception e) {
            System.out.println("Exception:" + e);
        }

        String responseText = null;
        try {
            responseText = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            //Log.i("IO Exception 2", e + "");
        }

        System.out.println("responseText:" + responseText);
        // Need to pull out user and role here in the future

        try{
            // Convert String to json object
            JSONObject jres = new JSONObject(responseText);

            // get results json object
            //JSONObject json_results = jres.getJSONObject("results");

            // get value from LL Json Object
            String str_value=jres.getString("results");
            System.out.println("str_value:" + str_value);
            if(str_value.equals("0")){
                return authenticated;
            }

        } catch(Exception e) {
            System.out.println("Exception:" + e);
        }

        try{
            // Convert String to json object
            JSONObject jres = new JSONObject(responseText);
            String user_value=jres.getString("user");
            System.out.println("user_value:" + user_value);
            if(!user_value.equals("")){
                authenticated = true;
            }
        } catch(Exception e) {
            System.out.println("Exception:" + e);
        }



        return authenticated;
    }



    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

        return super.onOptionsItemSelected(item);
    }*/
}
