package com.android.synchronous;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

public class NewUserActivity extends Activity {

    private static final int SELECT_PICTURE = 1;

    private TextView mUsername;
    private TextView mPassword;
    private TextView mName;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mCompany;
    private TextView mTitle;
    private Button mSubmit;
    private Button mUpload;
    private ImageView mImage;

    private ContactModel mContactModel;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);

        mContactModel = new ContactModel();

        mUsername = (TextView) findViewById(R.id.username_new);
        mPassword = (TextView) findViewById(R.id.password_new);
        mName = (TextView) findViewById(R.id.name_new);
        mEmail = (TextView) findViewById(R.id.email_new);
        mPhone = (TextView) findViewById(R.id.phone_new);
        mCompany = (TextView) findViewById(R.id.company_new);
        mTitle = (TextView) findViewById(R.id.title_new);
        mUpload = (Button) findViewById(R.id.upload_new);
        mSubmit = (Button) findViewById(R.id.submit_new);
        mImage = (ImageView) findViewById(R.id.image_new);

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContactModel.setUsername(mUsername.getText().toString());
                mContactModel.setPassword(mPassword.getText().toString());
                mContactModel.setName(mName.getText().toString());
                mContactModel.setEmail(mEmail.getText().toString());
                mContactModel.setPhone(mPhone.getText().toString());
                mContactModel.setCompany(mCompany.getText().toString());
                mContactModel.setTitle(mTitle.getText().toString());

                ParseUser user = new ParseUser();
                user.setUsername(mContactModel.getUsername());
                user.setPassword(mContactModel.getPassword());
                user.put("contactModel", mContactModel);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Intent intent = new Intent(mContext, CardActivity.class);
                            intent.putExtra(CardActivity.EXTRA_CONTACT_CARD, mContactModel);
                            startActivity(intent);
                        } else {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap imageBitmap = BitmapFactory.decodeFile(filePath);
                Bitmap compressedBitmap = scaleDownBitmap(imageBitmap, 75, mContext);
                mImage.setImageBitmap(compressedBitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                compressedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                mContactModel.setPhoto(byteArray);

            }
        }
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight*densityMultiplier);
        int w = (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);
        return photo;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

}
