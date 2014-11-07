package com.android.synchronous;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardActivity extends Activity {

    public static final String EXTRA_CONTACT_CARD =
            "com.android.synchronous.contact_card";

    private CircleImageView mImage;
    private TextView mName;
    private TextView mEmail;
    private TextView mNumber;
    private TextView mCompany;
    private TextView mTitle;

    private ContactModel mContactModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        mContactModel = (ContactModel) getIntent().getSerializableExtra(EXTRA_CONTACT_CARD);

        mImage = (CircleImageView) findViewById(R.id.cardImage);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(mContactModel.getPhoto(), 0,
                mContactModel.getPhoto().length);
        mImage.setImageBitmap(imageBitmap);

        mName = (TextView) findViewById(R.id.cardName);
        mName.setText(mContactModel.getName());

        mEmail = (TextView) findViewById(R.id.cardEmail);
        mEmail.setText(mContactModel.getEmail());

        mNumber = (TextView) findViewById(R.id.cardNumber);
        mNumber.setText(mContactModel.getPhone());

        mCompany = (TextView) findViewById(R.id.cardCompany);
        mCompany.setText(mContactModel.getCompany());

        mTitle = (TextView) findViewById(R.id.cardTitle);
        mTitle.setText(mContactModel.getTitle());
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
        }
        return true;
    }
}
