package com.cts.madhura.cts_sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/*
*   This class parses the JSON file using a third party parser called "Glide".
*   It uses a RecyclerView Adapter to feed the data to a list view and display the results from the
*   parser as given in the problem.
*/
public class DisplayActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRecyclerView;
    private TestAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        new HttpRequestTask().execute();
    }

    /* An AsyncTask and HTTPUrlConnection is used to fetch JSON data.*/
    private class HttpRequestTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog = new ProgressDialog(DisplayActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {

            //A "loading" progress bar is shown on the UI thread while we establish the connection
            // and fetch the JSON data.
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return e.toString();
            }

            try {

                //Setup HttpURLConnection class to receive data
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String res) {

            // Once the background task of fetching data is done, dismiss the progressDialog and
            // display the results in Listview.
            progressDialog.dismiss();
            ArrayList<Test> data = new ArrayList<>();
            progressDialog.dismiss();

            try {
                JSONObject results = new JSONObject(res);

                String title = results.getString("title");
                getSupportActionBar().setTitle(title); // for set actionbar title
                JSONArray rows = results.getJSONArray("rows");

                for (int i = 0; i < rows.length(); i++)
                {
                    JSONObject json_data = rows.getJSONObject(i);
                    Test testData = new Test();
                    testData.imageHref = json_data.getString("imageHref");
                    testData.description = json_data.getString("description");
                    testData.title = json_data.getString("title");
                    data.add(testData);
                }

                //Setup and Handover data to recyclerview
                mRecyclerView = (RecyclerView) findViewById(R.id.testListView);
                mAdapter = new TestAdapter(DisplayActivity.this, data);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(DisplayActivity.this));

            } catch (JSONException e) {
                Toast.makeText(DisplayActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}