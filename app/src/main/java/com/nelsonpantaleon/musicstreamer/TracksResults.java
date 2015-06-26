package com.nelsonpantaleon.musicstreamer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Alucard on 6/26/2015.
 */
public class TracksResults implements Parcelable {

    List<Track> mTracksResultsList;

    public List<Track> getTracksResultsList() {
        return mTracksResultsList;
    }

    public void setTracksResultsList(List<Track> tracksResultsList) {
        mTracksResultsList = new ArrayList<>();
        mTracksResultsList = tracksResultsList;
    }

    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try{
            parcel.writeList(mTracksResultsList);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }
}
