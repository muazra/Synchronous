package com.android.synchronous.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Intents;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.synchronous.R;
import com.android.synchronous.task.CheckNetworkTask;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardActivity extends Activity {

    public final static String CARD_USERNAME =
            "com.android.synchronous.cardactivity.card_username";
    public final static String CARD_POSITION =
            "com.android.synchronous.cardactivity.card_position";

    private ParseUser mParseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        CheckNetworkTask.check(this);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", getIntent().getStringExtra(CARD_USERNAME));
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                mParseUser = parseUser;

                ParseFile imageFile = (ParseFile) mParseUser.get("photo");
                imageFile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        CircleImageView mImage = (CircleImageView) findViewById(R.id.cardImage);
                        Bitmap imageBitmap = BitmapFactory.decodeByteArray(bytes, 0,
                                bytes.length);
                        mImage.setImageBitmap(imageBitmap);
                    }
                });

                TextView mName = (TextView) findViewById(R.id.cardName);
                mName.setText(mParseUser.get("name").toString());

                TextView mEmail = (TextView) findViewById(R.id.cardEmail);
                mEmail.setText(mParseUser.getEmail());

                TextView mNumber = (TextView) findViewById(R.id.cardNumber);
                mNumber.setText(mParseUser.get("phone").toString());

                TextView mCompany = (TextView) findViewById(R.id.cardCompany);
                mCompany.setText(mParseUser.get("company").toString());

                TextView mTitle = (TextView) findViewById(R.id.cardTitle);
                mTitle.setText(mParseUser.get("title").toString());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_delete:
                Toast.makeText(MainActivity.mContext, "Deleting Contact..", Toast.LENGTH_SHORT).show();
                JSONArray savedList = ParseUser.getCurrentUser().getJSONArray("saved");
                JSONArray updatedSavedList = new JSONArray();
                for(int i = 0; i < savedList.length(); i++){
                    try{
                        if(i != getIntent().getIntExtra(CARD_POSITION, 0))
                            updatedSavedList.put(savedList.get(i));
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ParseUser.getCurrentUser().put("saved", updatedSavedList);
                ParseUser.getCurrentUser().saveInBackground();

                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                return true;

            case R.id.action_save:
                Toast.makeText(MainActivity.mContext, "Saving Contact to Address Book..", Toast.LENGTH_SHORT).show();
                intent = new Intent(Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(Intents.Insert.EMAIL, mParseUser.getEmail())
                        .putExtra(Intents.Insert.PHONE, ContactsContract.CommonDataKinds.Phone.TYPE_MAIN)
                        .putExtra(Intents.Insert.PHONE_TYPE, mParseUser.get("title").toString())
                        .putExtra(Intents.Insert.NAME, mParseUser.get("name").toString())
                        .putExtra(Intents.Insert.COMPANY, mParseUser.get("company").toString())
                        .putExtra(Intents.Insert.JOB_TITLE, mParseUser.get("title").toString());

                startActivity(intent);
                return true;

            case R.id.action_call:
                Toast.makeText(MainActivity.mContext, "Calling Contact..", Toast.LENGTH_SHORT).show();

                String number = "tel:" + mParseUser.get("phone").toString().trim();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(number));
                startActivity(callIntent);
                return true;

            case android.R.id.home:
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewcard_menu, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
}
