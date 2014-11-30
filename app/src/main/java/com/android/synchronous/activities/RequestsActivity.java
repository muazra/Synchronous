package com.android.synchronous.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.synchronous.R;
import com.android.synchronous.adapters.RequestsActivityListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestsActivity extends Activity {

    private List<ParseUser> mRequestsList;
    private ListView mRequestsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        mRequestsList = new ArrayList<ParseUser>();
        mRequestsListView = (ListView) findViewById(R.id.requests_list);

        updateList();

        mRequestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setMessage("Accept Request?");

                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String usernameAccepted = mRequestsList.get(position).getUsername();

                        for(ParseUser user: mRequestsList){
                            if(user.getUsername().equals(usernameAccepted))
                                mRequestsList.remove(user);
                        }
                        ParseUser.getCurrentUser().put("requests", new JSONArray(mRequestsList));

                        JSONArray savedArray = ParseUser.getCurrentUser().getJSONArray("saved");
                        savedArray.put(usernameAccepted);
                        ParseUser.getCurrentUser().put("saved", savedArray);

                        ParseUser.getCurrentUser().saveInBackground();
                        updateList();
                    }
                });

                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String usernameAccepted = mRequestsList.get(position).getUsername();

                        for(ParseUser user: mRequestsList){
                            if(user.getUsername().equals(usernameAccepted))
                                mRequestsList.remove(user);
                        }
                        ParseUser.getCurrentUser().put("requests", new JSONArray(mRequestsList));
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
        mRequestsListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewcard_menu, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
}
