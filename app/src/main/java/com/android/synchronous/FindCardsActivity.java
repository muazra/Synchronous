package com.android.synchronous;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.List;

public class FindCardsActivity extends ListActivity{

    private Context mContext = this;
    private Button mRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findcards);

        mRefresh = (Button) findViewById(R.id.refresh_findcards);
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList();
            }
        });

        updateList();

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
                builder.setMessage("Save Contact?");
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        TextView name = (TextView) view.findViewById(R.id.card_row_name);

                        JSONArray users = (JSONArray) ParseUser.getCurrentUser().get("savedContacts");
                        users.put(name.getText().toString());
                        ParseUser.getCurrentUser().put("savedContacts", users);

                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                updateList();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                builder.show();
            }
        });
    }

    private void updateList(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {

                if(parseUsers.size() != 0){
                    JSONArray savedContacts = ParseUser.getCurrentUser().getJSONArray("savedContacts");

                    for(int i=0; i<parseUsers.size(); i++){
                        for(int j=0; j<savedContacts.length(); j++){
                            try{
                                if(savedContacts.get(j).equals(parseUsers.get(i).get("name"))){
                                    parseUsers.remove(i);
                                    break;
                                }
                            } catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }

                    ContactListAdapter adapter = new ContactListAdapter(mContext, parseUsers);
                    setListAdapter(adapter);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MyCardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return true;
    }

}
