package com.example.piotrek.myapplication;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Piotrek on 29.03.2017.
 */

public class MyServerAuthenticator implements IServerAuthenticator{

    private static Map<String, String> mCredentialsRepo;
    private JSONObject loginJSON;

    static {
        Map<String, String> credentials = new HashMap<String, String>();
        credentials.put("demo@example.com", "demo");
        credentials.put("foo@example.com", "foobar");
        credentials.put("user@example.com", "pass");
        mCredentialsRepo = Collections.unmodifiableMap(credentials);
    }

    @Override
    public String signUp(String email, String username, String password) {
        // TODO: register new user on the server and return its auth token
        return null;
    }

    @Override
    public String signIn(String login, String hashPassword) {
        String authToken = null;
        String urlParameters  = "login=" + login + "&hash=" + "4fe925b66e73da710dad18d451d26830923794aa";
        byte[] postData       = urlParameters.getBytes( Charset.forName("UTF-8") );
        int    postDataLength = postData.length;
        String serverLoginAdress = "http://192.168.1.101:8080/mct/rest/user/login";
        URL url = null;
        try {
            url = new URL(serverLoginAdress);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            urlConnection.setDoOutput(true);
            urlConnection.getOutputStream().write(postData);
            int statusCode = urlConnection.getResponseCode();
            String t = String.valueOf(statusCode);
            Log.d("KOD:", t);
            InputStream in = null;
            if (statusCode != HttpURLConnection.HTTP_ACCEPTED) {
                in = new BufferedInputStream(urlConnection.getInputStream());
            }

            String resultstring = readStream(in);
            System.out.println(resultstring);

            try {
                loginJSON = new JSONObject(resultstring);
                authToken = loginJSON.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return authToken;

        } catch (ConnectException e) {
            //Toast.makeText(getApplicationContext(), "Server Unreachable.", Toast.LENGTH_LONG).show();
            //LoginActivity.mAuthTask.cancel(true);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            //LoginActivity.mAuthTask.cancel(true);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    private static String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;) {
            sb.append((char)c);
        }
        return sb.toString();
    }
}
