package com.android.synchronous.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import com.android.synchronous.R;
import com.android.synchronous.adapters.RequestsActivityListAdapter;
import com.android.synchronous.task.CheckNetworkTask;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestsActivity extends ListActivity {

    private List<ParseUser> mRequestsList;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        CheckNetworkTask.check(this);

        updateList();

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
                builder.setMessage("Accept Request?");

                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String usernameAccepted = mRequestsList.get(position).getUsername();

                        //remove from requests list
                        mRequestsList.remove(position);
                        JSONArray requestArray = new JSONArray();
                        for (ParseUser u : mRequestsList)
                            requestArray.put(u.getUsername());
                        ParseUser.getCurrentUser().put("requests", requestArray);

                        //add to saved
                        JSONArray savedArray = ParseUser.getCurrentUser().getJSONArray("saved");
                        savedArray.put(usernameAccepted);
                        ParseUser.getCurrentUser().put("saved", savedArray);

                        //update parse database
                        try {
                            ParseUser.getCurrentUser().save();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        updateList();
                    }
                });

                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String usernameAccepted = mRequestsList.get(position).getUsername();

                        //remove from requests
                        mRequestsList.remove(position);
                        JSONArray requestArray = new JSONArray();
                        for (ParseUser u : mRequestsList) {
                            requestArray.put(u.getUsername());
                        }
                        ParseUser.getCurrentUser().put("requests", requestArray);
                        //update parse database
                        try {
                            ParseUser.getCurrentUser().save();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        updateList();
                    }
                });
                builder.show();
            }
        });

    }

    private void updateList(){
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
            query.whereContainedIn("username", Arrays.asList(requestsStringArray));
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, ParseException e) {
                    if(parseUsers != null) {
                        mRequestsList = new ArrayList<ParseUser>(parseUsers);
                        RequestsActivityListAdapter adapter = new
                                RequestsActivityListAdapter(MainActivity.mContext, mRequestsList);
                        setListAdapter(adapter);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
}
