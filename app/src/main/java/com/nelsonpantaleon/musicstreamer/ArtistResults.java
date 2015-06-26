package com.nelsonpantaleon.musicstreamer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Alucard on 6/26/2015.
 */
public class ArtistResults implements Parcelable {

    List<Artist> mArtistsList;

    public List<Artist> getArtistsList() {
        return mArtistsList;
    }

    public void setArtistsList(List<Artist> artistsList) {
        mArtistsList = new ArrayList<>();
        mArtistsList = artistsList;
    }

    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try{
            parcel.writeList(mArtistsList);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }

}
