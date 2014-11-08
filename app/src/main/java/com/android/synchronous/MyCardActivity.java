package com.android.synchronous;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCardActivity extends Activity {

    private CircleImageView mImage;
    private TextView mName;
    private TextView mEmail;
    private TextView mNumber;
    private TextView mCompany;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycard);

        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseFile imageFile = (ParseFile) currentUser.get("photo");
        imageFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {

                mImage = (CircleImageView) findViewById(R.id.cardImage);
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(bytes, 0,
                        bytes.length);
                mImage.setImageBitmap(imageBitmap);

            }
        });

        mName = (TextView) findViewById(R.id.cardName);
        mName.setText(currentUser.get("name").toString());

        mEmail = (TextView) findViewById(R.id.cardEmail);
        mEmail.setText(currentUser.getEmail());

        mNumber = (TextView) findViewById(R.id.cardNumber);
        mNumber.setText(currentUser.get("phone").toString());

        mCompany = (TextView) findViewById(R.id.cardCompany);
        mCompany.setText(currentUser.get("company").toString());

        mTitle = (TextView) findViewById(R.id.cardTitle);
        mTitle.setText(currentUser.get("title").toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_findcards:
                Intent intent = new Intent(this, FindCardsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;
            case R.id.action_savedcards:
                intent = new Intent(this, SavedCardsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;
            case R.id.action_discovery:
                item.setIcon(R.drawable.ic_discovery_on);
                return true;
            case R.id.action_logout:
                ParseUser.logOut();
                intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;

        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Quit if back is pressed
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
