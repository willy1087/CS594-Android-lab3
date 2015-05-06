package com.detroitteatime.myflickr;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by mark on 4/29/15.
 */
public class FlickerFragment extends Fragment implements AdapterView.OnItemClickListener{
    String[] mTitles;
    ArrayList<FlickrPhoto> photos;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        MainActivity activity = (MainActivity)this.getActivity();
        photos = activity.getmPhotos();
        mTitles = new String[photos.size()];

        for(int i = 0; i< mTitles.length; i++){
            mTitles[i] = photos.get(i).title;
        }

        ListView lv =(ListView)view.findViewById(R.id.listView);
        lv.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, mTitles));
        lv.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(getActivity(), mTitles[i], Toast.LENGTH_LONG).show();
        String photoURL = photos.get(i).getPhotoURL(true);
        PhotoFragment pf = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString("URL", photoURL);
        pf.setArguments(args);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, pf);
        ft.addToBackStack("Image");
        ft.commit();
    }
}
