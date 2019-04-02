package com.salma.worldcountries.screens.CountriesScreen;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.salma.worldcountries.R;
import com.salma.worldcountries.model.Country;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements MainContract.MainView {

    int countryCount = 0;
    TextView countryRank;
    TextView countryPopulation;
    TextView countryName;
    ImageView flagImg;
    Button backBtn;
    Button nextBtn;
    MainContract.MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        countryRank = findViewById(R.id.textRank);
        countryPopulation = findViewById(R.id.textPopulation);
        countryName = findViewById(R.id.textCountryName);
        flagImg = findViewById(R.id.imgFlag);

        backBtn = findViewById(R.id.btnBack);
        nextBtn = findViewById(R.id.btnForward);

        mainPresenter = new MainPresenterImpl(this);
        mainPresenter.readJson();
    }


    @Override
    protected void onResume() {
        super.onResume();
       // asyncDownload = new AsyncDownload();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countryCount == 0)
                    countryCount = 10;
                countryCount--;
                mainPresenter.retrieveCountry(countryCount);

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countryCount == 9)
                    countryCount = -1;
                countryCount++;
                mainPresenter.retrieveCountry(countryCount);
            }
        });

    }



    @Override
    public void showCountry(Country country) {
        countryRank.setText(country.getRank());
        countryPopulation.setText(country.getPopulation());
        countryName.setText(country.getName());
    }

    @Override
    public void showCountryFlag(Bitmap bitmap) {
        flagImg.setImageBitmap(bitmap);
    }




}
