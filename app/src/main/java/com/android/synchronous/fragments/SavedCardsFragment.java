package com.android.synchronous.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.synchronous.R;
import com.android.synchronous.activities.CardActivity;
import com.android.synchronous.adapters.SavedCardsListAdapter;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SavedCardsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_savedcards, container, false);

        JSONArray savedContacts = ParseUser.getCurrentUser().getJSONArray("saved");
        final List<ParseUser> savedContactsList = new ArrayList<ParseUser>();

        for (int i = 0; i < savedContacts.length(); i++) {
            try {
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("name", savedContacts.get(i));
                query.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        savedContactsList.add(parseUser);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SavedCardsListAdapter adapter = new SavedCardsListAdapter(getActivity(), savedContactsList);
        ListView savedContactsListView = (ListView) root.findViewById(R.id.saved_list);
        savedContactsListView.setAdapter(adapter);

        savedContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = savedContactsList.get(position).getUsername();
                Intent intent = new Intent(getActivity(), CardActivity.class);
                intent.putExtra(CardActivity.CARD_USERNAME, username);
                startActivity(intent);
            }
        });

        return root;
    }

}
