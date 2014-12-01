package com.android.synchronous.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
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

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedCardsFragment extends ListFragment {

    private List<ParseUser> mSavedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_savedcards, container, false);

        Log.d("MUAZ", "saved_cards fragment onCreateView");
        JSONArray savedContactsArray = ParseUser.getCurrentUser().getJSONArray("saved");
        String[] savedStringArray = new String[savedContactsArray.length()];
        for(int i = 0; i < savedContactsArray.length(); i++){
            try{
                savedStringArray[i] = savedContactsArray.get(i).toString();
                Log.d("MUAZ", "savedStringArray = >> " + savedStringArray[i]);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        try{
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereContainedIn("username", Arrays.asList(savedStringArray));
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
        Intent intent = new Intent(MainActivity.mContext, CardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(CardActivity.CARD_USERNAME, mSavedList.get(position).getUsername());
        intent.putExtra(CardActivity.CARD_POSITION, position);
        startActivity(intent);
    }

}
