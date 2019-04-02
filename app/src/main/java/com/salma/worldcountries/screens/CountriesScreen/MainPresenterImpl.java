package com.salma.worldcountries.screens.CountriesScreen;

import android.graphics.Bitmap;

import com.salma.worldcountries.model.Country;
import com.salma.worldcountries.model.Network.NetworkServices.ImageDownloader;
import com.salma.worldcountries.model.Network.NetworkServices.JsonReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainPresenterImpl implements MainContract.MainPresenter {
    private MainContract.MainView mainView;
    private List<Country> countryObjList;

    public MainPresenterImpl(MainContract.MainView mainView) {
        this.mainView = mainView;
    }


    @Override
    public void readJson() {
        try {
            URL url = new URL("http://www.androidbegin.com/tutorial/jsonparsetutorial.txt");
            JsonReader jsonReader = new JsonReader(url, this);
            Thread thread = new Thread(jsonReader);
            thread.start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCountiesList(List<Country> countryObjList) {
        this.countryObjList = countryObjList;
        retrieveCountry(0);
    }

    @Override
    public void receiveFlagImg(Bitmap bitmap) {
        mainView.showCountryFlag(bitmap);
    }

    @Override
    public void retrieveCountry(int index) {
        sendCountryToView(index);
        downloadFlagImg(index);
    }


    private void sendCountryToView(int index) {
        mainView.showCountry(countryObjList.get(index));
    }

    private void downloadFlagImg(int index) {
        ImageDownloader imageDownloader = new ImageDownloader(this);
        URL imgUrl = null;
        try {
            imgUrl = new URL(countryObjList.get(index).getFlagImgUrlStr());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        imageDownloader.execute(imgUrl);
    }


}
