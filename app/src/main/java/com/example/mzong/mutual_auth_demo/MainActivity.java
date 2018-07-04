package com.example.mzong.mutual_auth_demo;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        final HttpClient client = new SecureHttpClient(443, this.getApplicationContext());

        // Provide ip or address to where your test server is runnning
        HttpGet request = new HttpGet("https://10.0.0.222:9443/example-server");
        HttpResponse response = null;

        try {
            response = client.execute(request);
        } catch(IOException exception) {
            exception.printStackTrace();
        }

        if(response != null)
            Log.d("ExampleActivity", "Response code: " + response.getStatusLine().getStatusCode());
        else
            Log.d("ExampleActivity", "Response is NULL!!!!!!!!!!!!!!!!!!!!");
    }
}
