package com.networktest.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = findViewById(R.id.seng_request);
        responseText = findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.seng_request){
            sendRequestWithOkhttp();
        }
    }

    private void sendRequestWithHttpURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader =null;
                try{
                    URL url = new URL("http://guolin.tech/api/china");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    showResponse(response.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (reader != null){
                        try {
                            reader.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void sendRequestWithOkhttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
               try{
                   OkHttpClient client = new OkHttpClient();
                   Request request = new Request.Builder()
                           .url("http://guolin.tech/api/china")
                           .build();
                   Response response = client.newCall(request).execute();
                   String responsedata = response.body().string();
                   showResponse(responsedata);
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        }).start();
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }
}
