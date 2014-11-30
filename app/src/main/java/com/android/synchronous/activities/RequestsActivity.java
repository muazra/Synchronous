package com.android.synchronous.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import com.android.synchronous.R;
import com.android.synchronous.adapters.RequestsActivityListAdapter;
import com.android.synchronous.task.JSONArrayRemoveTask;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestsActivity extends ListActivity {

    private List<ParseUser> mRequestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        mRequestsList = new ArrayList<ParseUser>();
        updateList();

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setMessage("Accept Request?");

                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String usernameAccepted = mRequestsList.get(position).getUsername();

                        //remove from requests list
                        mRequestsList.remove(position);
                        JSONArray requestArray = new JSONArray();
                        for(ParseUser u: mRequestsList){
                            requestArray.put(u.getUsername());
                        }
                        ParseUser.getCurrentUser().put("requests", requestArray);

                        //add to saved
                        JSONArray savedArray = ParseUser.getCurrentUser().getJSONArray("saved");
                        savedArray.put(usernameAccepted);
                        ParseUser.getCurrentUser().put("saved", savedArray);

                        //remove from sending users requests_sent
                        try {
                            ParseQuery<ParseUser> query = ParseUser.getQuery();
                            query.whereEqualTo("username", usernameAccepted);
                            query.getFirstInBackground(new GetCallback<ParseUser>() {
                                @Override
                                public void done(ParseUser parseUser, ParseException e) {
                                    try {
                                        JSONArray sentArray = parseUser.getJSONArray("requests_sent");

                                        for (int i = 0; i < sentArray.length(); i++) {
                                            if (sentArray.get(i).equals(usernameAccepted))
                                                sentArray = JSONArrayRemoveTask.remove(i, sentArray);
                                        }

                                        parseUser.put("requests_sent", sentArray);
                                        parseUser.saveInBackground();
                                    } catch (Exception j){
                                        j.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //update parse database
                        ParseUser.getCurrentUser().saveInBackground();
                        updateList();
                    }
                });

                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String usernameAccepted = mRequestsList.get(position).getUsername();

                        //remove from requests
                        mRequestsList.remove(position);
                        JSONArray requestArray = new JSONArray();
                        for(ParseUser u: mRequestsList){
                            requestArray.put(u.getUsername());
                        }
                        ParseUser.getCurrentUser().put("requests", requestArray);

                        //remove from sending users requests_sent
                        try {
                            ParseQuery<ParseUser> query = ParseUser.getQuery();
                            query.whereEqualTo("username", usernameAccepted);
                            query.getFirstInBackground(new GetCallback<ParseUser>() {
                                @Override
                                public void done(ParseUser parseUser, ParseException e) {
                                    try {
                                        JSONArray sentArray = parseUser.getJSONArray("requests_sent");

                                        for (int i = 0; i < sentArray.length(); i++) {
                                            if (sentArray.get(i).equals(usernameAccepted))
                                                sentArray = JSONArrayRemoveTask.remove(i, sentArray);
                                        }

                                        parseUser.put("requests_sent", sentArray);
                                        parseUser.saveInBackground();
                                    } catch (Exception j){
                                        j.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //update parse database
                        ParseUser.getCurrentUser().saveInBackground();
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

    }

    private void updateList(){
        mRequestsList = new ArrayList<ParseUser>();
        JSONArray requestsArray = ParseUser.getCurrentUser().getJSONArray("requests");

        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereContainedIn("username", Arrays.asList(requestsArray));
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    for(ParseUser object: objects)
                        mRequestsList.add(object);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestsActivityListAdapter adapter = new RequestsActivityListAdapter(this, mRequestsList);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewcard_menu, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
}
