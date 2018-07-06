package com.example.mzong.mutual_auth_demo;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HttpsRequestIntentService extends IntentService {

    private static final String TAG = HttpsRequestIntentService.class.getSimpleName();

    public HttpsRequestIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final HttpClient client = new SecureHttpClient(443, getApplicationContext());

        // Provide ip or address to where your test server is runnning
        HttpGet request = new HttpGet("https://10.31.13.223:9443/example-server");
        HttpResponse response = null;

        try {
            response = client.execute(request);
        } catch(IOException exception) {
            exception.printStackTrace();
        }

        if(response != null) {
            Log.d("ExampleActivity", "Response code: " + response.getStatusLine().getStatusCode());

            String responseContent = "";
            try {
                InputStream stream = response.getEntity().getContent();
                Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8.name());
                responseContent = scanner.useDelimiter("\\A").next();
            } catch(IOException exception) {
                exception.printStackTrace();
            }
            Log.d("ExampleActivity", "Response content" + responseContent);
        }
        else
            Log.d("ExampleActivity", "Response is NULL!!!!!!!!!!!!!!!!!!!!");
    }

}
