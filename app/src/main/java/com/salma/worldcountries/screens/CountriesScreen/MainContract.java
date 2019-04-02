package com.salma.worldcountries.screens.CountriesScreen;

import android.graphics.Bitmap;

import com.salma.worldcountries.model.Country;

import java.util.List;

public interface MainContract {
    interface MainPresenter {
        void retrieveCountry(int index);
        void readJson();
        void setCountiesList(List<Country> countryObjList);
        void receiveFlagImg(Bitmap bitmap);
    }

    interface MainView {
        void showCountry(Country country);
        void showCountryFlag(Bitmap bitmap);
    }
}
