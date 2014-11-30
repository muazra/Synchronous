package com.android.synchronous.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.android.synchronous.R;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardActivity extends Activity {

    public final static String CARD_USERNAME =
            "com.android.synchronous.cardactivity.card_username";
    private ParseUser mParseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mycard);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", getIntent().getStringExtra(CARD_USERNAME));
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                mParseUser = parseUser;
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewcard_menu, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
}
