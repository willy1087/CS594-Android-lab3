package com.detroitteatime.myflickr;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    ProgressBar progress;
    ArrayList<FlickrPhoto> mPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = (ProgressBar) findViewById(R.id.progressBar);

        if (isOnline()) {
            LoadPhotos task = new LoadPhotos();
            task.execute();
        } else {
            progress.setVisibility(View.GONE);
            Toast.makeText(this, "Not online", Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<FlickrPhoto> getmPhotos() {
        return mPhotos;
    }

    public void setmPhotos(ArrayList<FlickrPhoto> mPhotos) {
        this.mPhotos = mPhotos;
    }


    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    public void showList() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, new FlickerFragment());
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoadPhotos extends AsyncTask<String, Long, Long> {
        HttpURLConnection connection = null;
        ArrayList<FlickrPhoto> photos;

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Long doInBackground(String... strings) {
            String dataString = "https://api.flickr.com/services/rest/" +
                    "?method=flickr.photos.search&api_key="+Constants.API_KEY+"&min_upload_date=04%2F25%2F2015&lat="+Constants.LATITUDE+"&lon=" +
                    Constants.LONGITUDE+"&radius="+Constants.RADIUS+"&radius_units=km&format=json&nojsoncallback=1";

//            String dataString = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api+key=" +
//                   Constants.API_KEY + "&per_page=" + Constants.NUM_PHOTOS + "&format=json&nojsoncallback=1";
            Log.i(Constants.TAG, dataString);
            try {
                URL dataUrl = new URL(dataString);
                connection = (HttpURLConnection) dataUrl.openConnection();
                connection.connect();
                int status = connection.getResponseCode();
                Log.d("TAG", "status " + status);
                //if it is successful
                if (status == 200) {
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String responseString;
                    StringBuilder sb = new StringBuilder();

                    while ((responseString = reader.readLine()) != null) {
                        sb = sb.append(responseString);
                    }
                    String photoData = sb.toString();

                    photos = FlickrPhoto.makePhotoList(photoData);
                    Log.d(Constants.TAG, photoData);

                    return 0l;
                } else {
                    return 1l;
                }
            } catch (MalformedURLException e) {
                Log.i(Constants.TAG, "Malformed Url");
                e.printStackTrace();
                return 1l;
            } catch (IOException e) {
                e.printStackTrace();
                return 1l;
            } catch (JSONException e) {
                e.printStackTrace();
                return 1l;
            } finally {
                if (connection != null)
                    connection.disconnect();
            }

        }



        @Override
        protected void onPostExecute(Long result) {
            if (result != 1l) {
                setmPhotos(photos);
                showList();

            } else {
                Toast.makeText(getApplicationContext(), "AsyncTask didn't complete", Toast.LENGTH_LONG).show();
            }
            progress.setVisibility(View.GONE);

        }
    }


}
