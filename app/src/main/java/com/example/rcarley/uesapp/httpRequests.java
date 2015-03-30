package com.example.rcarley.uesapp;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

/**
 * Created by rcarley on 3/27/2015.
 */
public class httpRequests {

    public JSONObject requestGet(String targetURI, String data) {
        JSONObject json = new JSONObject();
        BufferedReader in = null;
        HttpResponse response = null;

        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet request = new HttpGet();
            URI website = new URI("http://10.1.5.35/api/" + targetURI + data);
            //request.setEntity(new UrlEncodedFormEntity(Data));
            request.setURI(website);

            response = httpclient.execute(request);
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
        }

        System.out.println("responseText:" + responseText);

        try{
            // Convert String to json object
            json = new JSONObject(responseText);

            // get results json object
            //JSONObject json_results = jres.getJSONObject("results");

            // get value from LL Json Object
            //String str_value=jres.getString("results");
            //System.out.println("str_value:" + str_value);

        } catch(Exception e) {
            System.out.println("Exception:" + e);
        }

        return json;
    }

    public void logout(){

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.1.5.35/api/user/logout");
        HttpResponse response = null;

        try {
            response = httpclient.execute(httppost);
        } catch (ClientProtocolException e) {
            System.out.println("ClientProtocolException:" + e);

        } catch (IOException e) {
            System.out.println("IOException:" + e);
        }
        catch(Exception e) {
            System.out.println("Exception:" + e);
        }
    }
}
