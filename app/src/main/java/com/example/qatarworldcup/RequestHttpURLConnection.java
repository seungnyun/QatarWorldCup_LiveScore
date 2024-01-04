package com.example.qatarworldcup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestHttpURLConnection {
    public String request(final URL url){
        HttpURLConnection urlConnection = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(3000);
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestMethod("GET");
            if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new ConnectException();
            }
            String page = "";
            try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream(),"UTF-8"))){
                String line;
                while((line = reader.readLine()) != null){
                    page += line;
                    }
                }
                return page;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
