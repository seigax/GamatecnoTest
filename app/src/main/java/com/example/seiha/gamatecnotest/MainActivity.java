package com.example.seiha.gamatecnotest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.seiha.gamatecnotest.Adapter.AdapterFlight;
import com.example.seiha.gamatecnotest.Item.ItemFlight;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppCompatTextView TV_error;
    ProgressBar loading_spinner;
    RecyclerView RV_penerbangan;
    List<ItemFlight> list;
    AdapterFlight adapterFlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TV_error= (AppCompatTextView) findViewById(R.id.TV_error);
        loading_spinner = (ProgressBar) findViewById(R.id.loading_spinner);

        list = new ArrayList<ItemFlight>();
        RV_penerbangan = (RecyclerView) findViewById(R.id.RV_penerbangan);
        adapterFlight = new AdapterFlight(this, list);
        RV_penerbangan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RV_penerbangan.setAdapter(adapterFlight);

        new URLCON().execute("http://jobs.gamatechno.com/androiddev/fids.php");

    }

    public class URLCON extends AsyncTask<String, Void, String> {
        String server_response;
        int rescode;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                rescode = urlConnection.getResponseCode();

                if (rescode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (rescode == HttpURLConnection.HTTP_OK) {
                try {
                    JSONObject fetchdata = new JSONObject(server_response);
                    JSONArray fidsData = fetchdata.getJSONArray("fidsData");
                    for (int i = 0; i < fidsData.length(); i++) {
                        JSONObject data = fidsData.getJSONObject(i);
                        ItemFlight itemFlight = new ItemFlight();
                        itemFlight.setAirlineName(data.getString("airlineName"));
                        itemFlight.setAirportCode(data.getString("airlineCode"));
                        itemFlight.setAirlineLogo(fetchdata.getString("logoPath") + data.getString("airlineLogo") + "." + fetchdata.getString("logoType"));
                        itemFlight.setFlight(data.getString("flight"));
                        itemFlight.setRemarks(data.getString("remarks"));
                        itemFlight.setAirportCode(data.getString("airportCode"));
                        itemFlight.setAirportName(data.getString("airportName"));
                        itemFlight.setDestination(data.getString("destination"));
                        itemFlight.setTime(data.getString("time"));
                        list.add(itemFlight);
                    }
                    adapterFlight.notifyDataSetChanged();
                    loading_spinner.setVisibility(View.GONE);
                    RV_penerbangan.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                TV_error.setVisibility(View.VISIBLE);
                loading_spinner.setVisibility(View.GONE);
            }
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
