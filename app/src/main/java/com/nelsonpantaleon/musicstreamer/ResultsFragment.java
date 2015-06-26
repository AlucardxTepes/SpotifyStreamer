package com.nelsonpantaleon.musicstreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * Fragment containing ListView for artist search results (Name and picture)
 */
public class ResultsFragment extends Fragment {

    ArtistAdapter mResultsAdapter;
    ListView mListView;

    public ResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        // init mListView
        mListView = (ListView)rootView.findViewById(R.id.results_listView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Event Listeners
        MainActivity.mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || keyEvent.getAction() == KeyEvent.ACTION_DOWN ) {
                    searchArtist();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save state to outState
        List<Artist> currentArtists = new ArrayList<>();


    }

    private void searchArtist(){
        // Prevent null pointer exception
        if(!MainActivity.mSearchEditText.getText().toString().equals("")) {
            // get artist search results
            GetArtistTask getArtistTask = new GetArtistTask();
            getArtistTask.execute(MainActivity.mSearchEditText.getText().toString());
        }
    }

    public class GetArtistTask extends AsyncTask<String, Void, ArtistsPager> {

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();

        @Override
        protected ArtistsPager doInBackground(String... params) {
            // run artist search using string argument
            return spotify.searchArtists(params[0]);
        }

        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {
            super.onPostExecute(artistsPager);
            // obtain artist collection from results
            List<Artist> artistsList = artistsPager.artists.items;
            // init, fill up and set the adapter
            mResultsAdapter = new ArtistAdapter(getActivity().getApplicationContext(),R.layout.list_item_results);
            mResultsAdapter.addAll(artistsList);
            mListView.setAdapter(mResultsAdapter);
            // if no results found, make Toast
            if (artistsList.isEmpty()) {
                Toast.makeText(getActivity().getApplicationContext(), "No matching artists found.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
