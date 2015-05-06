package com.detroitteatime.myflickr;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mark on 5/1/15.
 */
public class PhotoFragment extends Fragment {
    private String photoURL;
    private ImageView mImageView;
    private String mImageString;
    private Bitmap mBitmap;
    private ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_fragment, container, false);
        mImageView = (ImageView) view.findViewById(R.id.imageView);
        progress = (ProgressBar)view.findViewById(R.id.progressBar2);

        Bundle bundle = getArguments();
        photoURL = bundle.getString("URL", "");
        Log.i(Constants.TAG, "url in fragment: " + photoURL);

        AsyncTask<String, Void, Long> task = new GetPhoto();
        task.execute();
        return view;
    }

    private class GetPhoto extends AsyncTask<String, Void, Long> {
        InputStream is = null;
        HttpURLConnection connection = null;

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(String... strings) {

            try{
                URL imageUrl = new URL(photoURL);
                connection = (HttpURLConnection)imageUrl.openConnection();
                connection.connect();
                is = connection.getInputStream();
                mBitmap = BitmapFactory.decodeStream(is);
                return(0L);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return(1L);
            } catch (IOException e) {
                e.printStackTrace();
                return(1L);
            }finally{
                connection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(Long aLong) {
            mImageView.setImageBitmap(mBitmap);
            progress.setVisibility(View.GONE);
            super.onPostExecute(aLong);
        }
    }
}
