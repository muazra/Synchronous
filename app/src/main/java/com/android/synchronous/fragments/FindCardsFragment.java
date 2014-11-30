package com.android.synchronous.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.synchronous.R;
import com.android.synchronous.activities.MainActivity;
import com.android.synchronous.adapters.FindCardsListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindCardsFragment extends ListFragment {

    private List<ParseUser> mFindList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_findcards, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        JSONArray savedContactsArray = ParseUser.getCurrentUser().getJSONArray("saved");
        JSONArray requestsSentArray = ParseUser.getCurrentUser().getJSONArray("requests_sent");

        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.whereNotContainedIn("username", Arrays.asList(savedContactsArray));
            query.whereNotContainedIn("username", Arrays.asList(requestsSentArray));
            query.whereEqualTo("city", ParseUser.getCurrentUser().getString("city"));
            query.whereEqualTo("discover", true);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, ParseException e) {
                    if(parseUsers != null) {
                        mFindList = new ArrayList<ParseUser>(parseUsers);
                        updateList();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateList(){
        FindCardsListAdapter adapter = new FindCardsListAdapter(MainActivity.mContext, mFindList);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setMessage("Send Request");

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ParseUser userRequested = mFindList.get(position);
                JSONArray requestsArray = userRequested.getJSONArray("requests");
                requestsArray.put(ParseUser.getCurrentUser().getUsername());
                userRequested.put("requests", requestsArray);

                JSONArray sentArray = ParseUser.getCurrentUser().getJSONArray("requests_sent");
                sentArray.put(userRequested.getUsername());
                ParseUser.getCurrentUser().put("requests_sent", sentArray);

                userRequested.saveInBackground();
                mFindList.remove(position);

                updateList();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();

    }

}
