package com.nelsonpantaleon.musicstreamer;

import android.content.Context;
import android.content.Intent;
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

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Alucard on 6/23/2015.
 * Custom Adapter for listView that adds artist name and picture
 * and stores the artist ID required by the Top 10 tracks later on
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {

    private int mLayoutResourceId;

    public ArtistAdapter(Context context, int textViewResourceId) {
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

            // get artist
            final Artist artist = getItem(position);
            // get artist ID
            String mArtistID = artist.id;
//            Toast.makeText(getContext(), mArtistID, Toast.LENGTH_SHORT).show();
            // get image
            List<Image> imagesList = artist.images;
            Image image = new Image();
            String imageURL = "";
            if(imagesList.size() > 0) {
                image = imagesList.get(1);
                imageURL = image.url;
            }
            // Get layout views
            TextView artistNameTextView = (TextView) v.findViewById(R.id.list_item_results);
            ImageView artistImageView = (ImageView) v.findViewById(R.id.artist_ImageView);

            String artistName = ""; // declared outside to also be used on listener function

            // set values
            if(artist.name != null) {
                // deal with char encoding
                artistName = decodeString(artist.name);
                artistNameTextView.setText(artistName);
                // set image
                if(!imageURL.equals(""))
                    Picasso.with(getContext()).load(imageURL).into(artistImageView);
                }else{
                Toast.makeText(getContext(),artist.name + " Artist not found", Toast.LENGTH_SHORT).show();
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getContext(), "Clicked " + decodeString(artist.name), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), TopTracksActivity.class);
                    intent.putExtra("ARTIST_ID", artist.id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            });

            return v;
        } catch (Exception ex) {
            Log.e("ArtistAdapter", "error", ex);
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