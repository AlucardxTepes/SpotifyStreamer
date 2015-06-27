package com.nelsonpantaleon.musicstreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


public class TopTracksActivity extends ActionBarActivity {

    String mArtistID;
    ListView mListView;
    TrackAdapter mTrackAdapter;
    TracksResults mTracksResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);

        if(savedInstanceState == null){
            // Get artist ID from previous activity
            mArtistID = getIntent().getStringExtra("ARTIST_ID");
        }
        if(savedInstanceState != null && !savedInstanceState.isEmpty()){
            // receive saved bundle items
            mTracksResults = savedInstanceState.getParcelable("TopTracksResults");
            try {
                // init listview
                mListView = (ListView) findViewById(R.id.topTracks_listView);
                // create local TracksResults list and adapter
                List<Track> tracksResultsList = mTracksResults.getTracksResultsList();
                TrackAdapter refreshTrackAdapter = new TrackAdapter(getApplicationContext(),R.layout.list_item_top_tracks);
                // add saved local items to adapter and set adapter to listview
                refreshTrackAdapter.addAll(tracksResultsList);
                mListView.setAdapter(refreshTrackAdapter);
                // remove data to prevent crashes
                savedInstanceState.remove("TopTracksResults");
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }else {
            // init listview
            mListView = (ListView) findViewById(R.id.topTracks_listView);
            searchTracks();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_tracks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("TopTracksResults", mTracksResults);
        super.onSaveInstanceState(outState);
    }

    private void searchTracks(){
        // get artist search results
        GetTopTracksTask getTopTracks = new GetTopTracksTask();
        getTopTracks.execute(mArtistID);
    }



    public class GetTopTracksTask extends AsyncTask<String, Void, Tracks> {

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();



        @Override
        protected Tracks doInBackground(String... params) {
            try {
                // get top tracks using string argument
                Map<String, Object> options = new HashMap<String, Object>();
                options.put("country", "DO");
                return spotify.getArtistTopTrack(params[0], options);
            } catch (RuntimeException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Tracks topTracks) {
            try{
                super.onPostExecute(topTracks);
//            // obtain top tracks collection from artistID
                List<Track> topTracksList = topTracks.tracks;
//            // init, fill up and set the adapter
                mTrackAdapter = new TrackAdapter(getApplicationContext(),R.layout.list_item_top_tracks);
                mTrackAdapter.addAll(topTracksList);
                mListView.setAdapter(mTrackAdapter);
                // Save results to keep in 'savedinstancestate'
                mTracksResults = new TracksResults();
                mTracksResults.setTracksResultsList(topTracksList);
            } catch (RuntimeException e){
                e.printStackTrace();
            }

        }
    }
}
