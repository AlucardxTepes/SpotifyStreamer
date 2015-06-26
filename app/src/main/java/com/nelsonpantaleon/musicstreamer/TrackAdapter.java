package com.nelsonpantaleon.musicstreamer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Alucard on 6/25/2015.
 */
public class TrackAdapter extends ArrayAdapter<Track> {

    private int mLayoutResourceId;

    public TrackAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutResourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            View v = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(mLayoutResourceId, null);
            } else {
                v = convertView;
            }

            // get Tracks
            final Track track = getItem(position);
            // get image
            List<Image> imagesList = track.album.images;
            Image image = new Image();
            String imageURL = "";
            if(imagesList.size() > 0) {
                image = imagesList.get(1);
                imageURL = image.url;
            }
            // Get layout views
            TextView trackNameTextView = (TextView) v.findViewById(R.id.track_name);
            TextView trackAlbumTextView = (TextView) v.findViewById(R.id.album_name);
            ImageView trackImageView = (ImageView) v.findViewById(R.id.track_ImageView);

            String trackName = ""; // declared outside to also be used on listener function
            String trackAlbum = "";

            // set values
            if(track.name != null) {
                // deal with char encoding
                trackName = decodeString(track.name);
                trackNameTextView.setText(trackName);
                trackAlbum = decodeString(track.album.name);
                trackAlbumTextView.setText(trackAlbum);

                // set image
                if(!imageURL.equals(""))
                    Picasso.with(getContext()).load(imageURL).into(trackImageView);
            }else{
                Toast.makeText(getContext(), track.name + " Track not found", Toast.LENGTH_SHORT).show();
            }
            return v;
        } catch (Exception ex) {
            Log.e("TrackAdapter", "error", ex);
            return null;
        }
    }

    public String decodeString(String text) {
        try {
            byte decoding[] = text.getBytes("ISO-8859-1");
            return new String(decoding, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return text; // unsuccessful
        }
    }

}