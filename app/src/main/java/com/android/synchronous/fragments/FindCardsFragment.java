package com.android.synchronous.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.synchronous.R;
import com.android.synchronous.activities.MainActivity;
import com.android.synchronous.adapters.FindCardsListAdapter;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FindCardsFragment extends ListFragment {

    private List<ParseUser> mFindList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_findcards, container, false);

        JSONArray savedContactsArray = ParseUser.getCurrentUser().getJSONArray("saved");
        String[] savedStringArray = new String[savedContactsArray.length()];
        for(int i = 0; i < savedContactsArray.length(); i++){
            try{
                savedStringArray[i] = savedContactsArray.get(i).toString();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        JSONArray requestsArray = ParseUser.getCurrentUser().getJSONArray("requests");
        String[] requestsStringArray = new String[requestsArray.length()];
        for(int i = 0; i < requestsArray.length(); i++){
            try{
                requestsStringArray[i] = requestsArray.get(i).toString();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.whereEqualTo("city", ParseUser.getCurrentUser().getString("city"));
            query.whereEqualTo("discover", true);

            if(savedStringArray.length > 0)
                query.whereNotContainedIn("username", Arrays.asList(savedStringArray));

            if(requestsStringArray.length > 0)
                query.whereNotContainedIn("username", Arrays.asList(requestsStringArray));

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

        return rootView;
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

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("userId", userRequested.getObjectId());
                params.put("sendFrom", ParseUser.getCurrentUser().getUsername());
                ParseCloud.callFunctionInBackground("sendRequest", params, new FunctionCallback<ParseUser>() {
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            Log.d("MUAZ", "findcardsfragment sendRequest worked !!");
                        }else{
                            Log.d("MUAZ", "findcardsfragment sendRequest DID NOT WORK :/ !!");
                            e.printStackTrace();
                        }
                    }
                });

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
