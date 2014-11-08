package com.android.synchronous;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SavedCardsActivity extends ListActivity {

    private Context mContext = this;
    private List<ParseUser> savedContacts;

    private Button mRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedcards);

        savedContacts = new ArrayList<ParseUser>();
        updateList();

        mRefresh = (Button) findViewById(R.id.refresh_savedcards);
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList();
            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView name = (TextView) view.findViewById(R.id.card_row_name);
                for(int i=0; i<savedContacts.size(); i++){
                    if(name.getText().toString().equals(savedContacts.get(i).get("name"))){
                        final Intent intent = new Intent(mContext, SavedCardActivity.class);
                        intent.putExtra(SavedCardActivity.PASSED_IN_NAME, savedContacts.get(i).get("name").toString());
                        intent.putExtra(SavedCardActivity.PASSED_IN_EMAIL, savedContacts.get(i).get("email").toString());
                        intent.putExtra(SavedCardActivity.PASSED_IN_COMPANY, savedContacts.get(i).get("company").toString());
                        intent.putExtra(SavedCardActivity.PASSED_IN_TITLE, savedContacts.get(i).get("title").toString());
                        intent.putExtra(SavedCardActivity.PASSED_IN_PHONE, savedContacts.get(i).get("phone").toString());

                        ParseFile imageFile = (ParseFile) savedContacts.get(i).get("photo");
                        imageFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                intent.putExtra(SavedCardActivity.PASSED_IN_IMAGE, bytes);
                                startActivity(intent);

                            }
                        });
                        break;
                    }

                }

            }
        });

    }

    private void updateList(){
        JSONArray users = ParseUser.getCurrentUser().getJSONArray("savedContacts");

        if(users.length() != 0) {
            for (int i = 0; i < users.length(); i++) {
                try {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("name", users.get(i));
                    query.getFirstInBackground(new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            savedContacts.add(parseUser);

                            ContactListAdapter adapter = new ContactListAdapter(mContext, savedContacts);
                            setListAdapter(adapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

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
