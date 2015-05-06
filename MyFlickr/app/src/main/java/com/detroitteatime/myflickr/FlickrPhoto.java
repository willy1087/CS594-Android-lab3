package com.detroitteatime.myflickr;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by mark on 4/30/15.
 */
public class FlickrPhoto {
    String id, owner, secret, server, farm, title;
    Boolean isPublic, isFriend, isFamily;

    public FlickrPhoto(JSONObject jsonPhoto) throws JSONException {
        this.id = (String) jsonPhoto.optString("id");
        this.secret = (String) jsonPhoto.optString("secret");
        this.owner = (String) jsonPhoto.optString("owner");
        this.server = (String) jsonPhoto.optString("server");
        this.farm = (String) jsonPhoto.optString("farm");
        this.title = (String)jsonPhoto.optString("title");
        this.isPublic = (Boolean) jsonPhoto.optBoolean("ispublic");
        this.isFriend = (Boolean) jsonPhoto.optBoolean("isfriend");
        this.isFamily = (Boolean) jsonPhoto.optBoolean("isfamily");
    }

    public static ArrayList<FlickrPhoto> makePhotoList (String photoData) throws JSONException{
        ArrayList<FlickrPhoto> flickrPhotos = new ArrayList<>();
        JSONObject data = new JSONObject(photoData);
        JSONObject photos = data.optJSONObject("photos");
        JSONArray photoArray = photos.optJSONArray("photo");

        for(int i = 0; i < photoArray.length(); i++){
            JSONObject photo = (JSONObject)photoArray.get(i);
            FlickrPhoto currentPhoto = new FlickrPhoto(photo);
            flickrPhotos.add(currentPhoto);
        }
        return flickrPhotos;
    }

    public String getPhotoURL(Boolean big){
        String opt = "n";
        if(big){
            opt = "c";
        }
        String photoURI = "http://farm" + this.farm + ".staticflickr.com/"+this.server+"/"
                + this.id+"_"+this.secret+"_"+opt+".jpg";
        Log.i(Constants.TAG, "Photo url: " + photoURI);
        return photoURI;
    }



}
