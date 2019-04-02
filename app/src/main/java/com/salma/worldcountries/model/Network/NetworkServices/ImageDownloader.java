package com.salma.worldcountries.model.Network.NetworkServices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.salma.worldcountries.model.Network.HttpConnectionHandler;
import com.salma.worldcountries.screens.CountriesScreen.MainContract;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader extends AsyncTask<URL, Void, Bitmap> {

    private MainContract.MainPresenter mainPresenter;

    public ImageDownloader(MainContract.MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    @Override
    protected Bitmap doInBackground(URL... urls) {
        Bitmap image;
        image = download(urls[0]);
        return image;
    }

    private Bitmap download(URL url) {
        //HttpsURLConnection httpsURLConnection;
        InputStream inputStream = null;
        Bitmap flagImage = null;
        try {
            HttpConnectionHandler httpConnectionHandler = new HttpConnectionHandler();
            HttpURLConnection httpURLConnection = httpConnectionHandler.handleHttpConnection(url);
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
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
        mainPresenter.receiveFlagImg(bitmap);
    }
}

