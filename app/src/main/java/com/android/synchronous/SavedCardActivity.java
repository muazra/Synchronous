//package com.android.synchronous;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.TextView;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class SavedCardActivity extends Activity {
//
//    public static final String PASSED_IN_NAME = "name";
//    public static final String PASSED_IN_EMAIL = "email";
//    public static final String PASSED_IN_PHONE = "phone";
//    public static final String PASSED_IN_COMPANY = "company";
//    public static final String PASSED_IN_TITLE = "title";
//    public static final String PASSED_IN_IMAGE = "photo";
//
//    private CircleImageView mImage;
//    private TextView mName;
//    private TextView mEmail;
//    private TextView mNumber;
//    private TextView mCompany;
//    private TextView mTitle;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_mycard);
//
//        String name = getIntent().getStringExtra(PASSED_IN_NAME);
//        String email = getIntent().getStringExtra(PASSED_IN_EMAIL);
//        String phone = getIntent().getStringExtra(PASSED_IN_PHONE);
//        String company = getIntent().getStringExtra(PASSED_IN_COMPANY);
//        String title = getIntent().getStringExtra(PASSED_IN_TITLE);
//        byte[] imageBytes = getIntent().getByteArrayExtra(PASSED_IN_IMAGE);
//
//        mImage = (CircleImageView) findViewById(R.id.cardImage);
//        Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
//                imageBytes.length);
//        mImage.setImageBitmap(imageBitmap);
//
//        mName = (TextView) findViewById(R.id.cardName);
//        mName.setText(name);
//
//        mEmail = (TextView) findViewById(R.id.cardEmail);
//        mEmail.setText(email);
//
//        mNumber = (TextView) findViewById(R.id.cardNumber);
//        mNumber.setText(phone);
//
//        mCompany = (TextView) findViewById(R.id.cardCompany);
//        mCompany.setText(company);
//
//        mTitle = (TextView) findViewById(R.id.cardTitle);
//        mTitle.setText(title);
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setTitle("View Card");
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                Intent intent = new Intent(this, SavedCardsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                return true;
//        }
//        return true;
//    }
//}
