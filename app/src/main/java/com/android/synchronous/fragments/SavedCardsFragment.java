package com.android.synchronous.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.synchronous.R;
import com.android.synchronous.activities.CardActivity;
import com.android.synchronous.activities.MainActivity;
import com.android.synchronous.adapters.SavedCardsListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedCardsFragment extends ListFragment {

    private List<ParseUser> mSavedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_savedcards, container, false);

        try{
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereContainedIn("username", Arrays.asList(ParseUser.getCurrentUser().getJSONArray("saved")));
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, ParseException e) {
                    if(parseUsers != null) {
                        mSavedList = new ArrayList<ParseUser>(parseUsers);
                        updateList();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return root;
    }

    private void updateList(){
        SavedCardsListAdapter adapter = new SavedCardsListAdapter(MainActivity.mContext, mSavedList);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String username = mSavedList.get(position).getUsername();
        Intent intent = new Intent(getActivity(), CardActivity.class);
        intent.putExtra(CardActivity.CARD_USERNAME, username);
        intent.putExtra(CardActivity.CARD_POSITION, position);
        startActivity(intent);
    }

}
