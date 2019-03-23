package com.salma.worldcountries;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    List<Country> countryObjList;
    Handler countryHandler;
    AsyncDownload asyncDownload;
    int countryCount = 0;
    TextView countryRank;
    TextView countryPopulation;
    TextView countryName;
    ImageView flagImg;
    Button backBtn;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryObjList = new ArrayList<>();

        countryRank = findViewById(R.id.textRank);
        countryPopulation = findViewById(R.id.textPopulation);
        countryName = findViewById(R.id.textCountryName);
        flagImg = findViewById(R.id.imgFlag);

        backBtn = findViewById(R.id.btnBack);
        nextBtn = findViewById(R.id.btnForward);
        ReadJson readJson = new ReadJson();
        Thread thread = new Thread(readJson);
        thread.start();
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onResume() {
        super.onResume();
        asyncDownload = new AsyncDownload();
        countryHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                showCountry();


            }
        };
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countryCount==0)
                    countryCount=10;
                countryCount--;
                showCountry();

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countryCount==9)
                    countryCount=-1;
                countryCount++;
                showCountry();

            }
        });

    }
    private void showCountry(){
        Country currentCountry;
        currentCountry = countryObjList.get(countryCount);
        countryRank.setText(currentCountry.getRank());
        countryPopulation.setText(currentCountry.getPopulation());
        countryName.setText(currentCountry.getName());
        try {
            asyncDownload=new AsyncDownload();
            URL imgUrl = new URL(currentCountry.getFlagImgUrlStr());
            asyncDownload.execute(imgUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }




private class ReadJson implements Runnable {
    URL url = null;
    HttpsURLConnection httpsURLConnection = null;
    InputStream inputStream = null;
    String response;

    @Override
    public void run() {
        handleHttpConnection();
        Log.i("Done", "Done");
    }

    private void handleHttpConnection() {
        try {
            url = new URL("http://www.androidbegin.com/tutorial/jsonparsetutorial.txt");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(inputStream);
            parseToJson(response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void parseToJson(String response) {
        JSONObject jsonObject;
        JSONArray countries;
        if (response != null) {
            try {
                jsonObject = new JSONObject(response);
                countries = jsonObject.getJSONArray("worldpopulation");
                for (int i = 0; i < countries.length(); i++) {
                    JSONObject countryObj = countries.getJSONObject(i);
                    String rank = countryObj.getString("rank");
                    String name = countryObj.getString("country");
                    String population = countryObj.getString("population");
                    String flagImgUrlStr = countryObj.getString("flag").replace("http", "https");
                    Country country = new Country(rank, name, population, flagImgUrlStr);
                    countryObjList.add(country);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                countryHandler.sendEmptyMessage(0);
            }
        }

    }

    private String convertStreamToString(InputStream inputStream) {
        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}

private class AsyncDownload extends AsyncTask<URL, Void, Bitmap> {


    @Override
    protected Bitmap doInBackground(URL... urls) {
        Bitmap image = null;
        image = download(urls[0]);
        return image;
    }

    private Bitmap download(URL url) {
        HttpsURLConnection httpsURLConnection;
        InputStream inputStream = null;
        Bitmap flagImage = null;
        try {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.connect();
            inputStream = httpsURLConnection.getInputStream();
            flagImage = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return flagImage;

        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            flagImg.setImageBitmap(bitmap);
        } else {
            Toast.makeText(MainActivity.this, "Cannot Download Flag Image", Toast.LENGTH_SHORT).show();

        }
    }
}

}
