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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

public class SavedCardsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_savedcards, container, false);

        try{
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereContainedIn("username", Arrays.asList(ParseUser.getCurrentUser().getJSONArray("saved")));
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(final List<ParseUser> parseUsers, ParseException e) {
                    SavedCardsListAdapter adapter = new SavedCardsListAdapter(getActivity(), parseUsers);
                    ListView savedContactsListView = (ListView) root.findViewById(R.id.saved_list);
                    savedContactsListView.setAdapter(adapter);

                    savedContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String username = parseUsers.get(position).getUsername();
                            Intent intent = new Intent(getActivity(), CardActivity.class);
                            intent.putExtra(CardActivity.CARD_USERNAME, username);
                            intent.putExtra(CardActivity.CARD_POSITION, position);
                            startActivity(intent);
                        }
                    });

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return root;
    }

}
