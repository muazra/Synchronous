package com.android.synchronous;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SavedCardsActivity extends ListActivity {

    private Context mContext = this;
    private List<ParseUser> savedContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedcards);

        savedContacts = new ArrayList<ParseUser>();
        updateList();

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    private void updateList(){
        JSONArray users = (JSONArray) ParseUser.getCurrentUser().get("savedContacts");
        for(int i=0; i<users.length(); i++){
            try{
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("name", users.get(i));
                query.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        savedContacts.add(parseUser);
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        ContactListAdapter adapter = new ContactListAdapter(mContext, savedContacts);
        setListAdapter(adapter);

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
