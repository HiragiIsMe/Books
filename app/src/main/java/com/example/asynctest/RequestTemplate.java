package com.example.asynctest;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestTemplate {

    public static JSONObject GetJsonUrlParam(String url, String param){

        JSONObject jsonObject = null;
        try{
            URL requestUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
            try {

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("POST");

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(param);
                writer.flush();

                int CodeResult = conn.getResponseCode();
                if(CodeResult == 200){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer buffer = new StringBuffer();
                    String line;

                    while ((line=reader.readLine()) != null){
                        buffer.append(line + "\n");
                    }

                    jsonObject = new JSONObject(buffer.toString());

                }else{
                    jsonObject = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static int getResponseSignup(String url, String params){
        int response = 0;

        try{
            URL urlRequest = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlRequest.openConnection();
            try{
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("POST");

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(params);
                writer.flush();

                response = conn.getResponseCode();
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }

    public static String getData(String url, String token){
        String result = "";
        try{
            URL getUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) getUrl.openConnection();

            conn.setRequestProperty("Authorization", "Bearer "+ token);
            InputStreamReader reader = new InputStreamReader(conn.getInputStream());

            int data = reader.read();
            while (data != -1){
                result += (char) data;
                data = reader.read();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
