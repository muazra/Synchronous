package com.android.synchronous.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.synchronous.R;
import com.android.synchronous.adapters.FindCardsListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindCardsFragment extends Fragment {

    private ListView mFindContactsListView;
    private List<ParseUser> mFindContactsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_findcards, container, false);
        mFindContactsListView = (ListView) root.findViewById(R.id.find_list);

        JSONArray savedContactsList = ParseUser.getCurrentUser().getJSONArray("saved");

        mFindContactsList = new ArrayList<ParseUser>();
        String userCity = ParseUser.getCurrentUser().getString("city");

        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.whereNotContainedIn("username", Arrays.asList(savedContactsList));
            query.whereEqualTo("city", userCity);
            query.whereEqualTo("discover", true);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    for(ParseUser object: objects)
                         mFindContactsList.add(object);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateList();

        mFindContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setMessage("Send Request");

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ParseUser userRequested = mFindContactsList.get(position);
                        JSONArray array = new JSONArray();
                        array.put(ParseUser.getCurrentUser().getUsername());
                        userRequested.put("pending", array);
                        userRequested.saveInBackground();

                        mFindContactsList.remove(position);
                        updateList();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        return root;
    }

    private void updateList(){
        FindCardsListAdapter adapter = new FindCardsListAdapter(getActivity(),  mFindContactsList);
        mFindContactsListView.setAdapter(adapter);
    }

}
