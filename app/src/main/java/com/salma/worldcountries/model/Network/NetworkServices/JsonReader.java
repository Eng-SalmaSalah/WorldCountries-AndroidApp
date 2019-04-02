package com.salma.worldcountries.model.Network.NetworkServices;

import android.annotation.SuppressLint;
import android.os.Message;

import com.salma.worldcountries.model.Country;
import com.salma.worldcountries.model.Network.HttpConnectionHandler;
import com.salma.worldcountries.screens.CountriesScreen.MainContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;


public class JsonReader implements Runnable {

    private List<Country> countryObjList = new ArrayList<>();
    private Handler handler;
    private URL url;
    private MainContract.MainPresenter _mainPresenter;


    @SuppressLint("HandlerLeak")
    public JsonReader(URL url, final MainContract.MainPresenter mainPresenter) {
        this.url = url;
        this._mainPresenter=mainPresenter;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                _mainPresenter.setCountiesList(countryObjList);
            }
        };

    }

    @Override
    public void run() {
        try {
            HttpConnectionHandler httpConnectionHandler = new HttpConnectionHandler();
            HttpURLConnection httpURLConnection = httpConnectionHandler.handleHttpConnection(url);
            InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            String response = convertStreamToString(inputStream);
            parseToJson(response);
        } catch (IOException e) {
            e.printStackTrace();
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
                handler.sendEmptyMessage(0);
            }
        }
    }

}
