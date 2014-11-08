package com.android.synchronous;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class NewUserActivity extends Activity {

    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private TextView mUsername;
    private TextView mPassword;
    private TextView mName;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mCompany;
    private TextView mTitle;
    private Button mSubmit;
    private Button mUploadPhoto;
    private Button mTakePhoto;
    private ImageView mImage;

    private ContactModel mContactModel;
    private Context mContext = this;

    private Uri mFileUri;

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
        mUploadPhoto = (Button) findViewById(R.id.upload_new);
        mSubmit = (Button) findViewById(R.id.submit_new);
        mImage = (ImageView) findViewById(R.id.image_new);
        mTakePhoto = (Button) findViewById(R.id.take_photo_new);

        mUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create folder to store image
                File imagesFolder = new File(Environment.getExternalStorageDirectory(),"Camera");
                imagesFolder.mkdirs();

                //create image file reference
                File imageFile = new File(imagesFolder,"image.jpg");

                mFileUri = Uri.fromFile(imageFile);
                Log.d("cameraActivity", mFileUri.getPath());

                //create intent and launch camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,mFileUri);

                startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
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

                Intent intent = new Intent(mContext, CardActivity.class);
                intent.putExtra(CardActivity.EXTRA_CONTACT_CARD, mContactModel);
                startActivity(intent);

//                ParseUser user = new ParseUser();
//                user.setUsername(mContactModel.getUsername());
//                user.setPassword(mContactModel.getPassword());
//                user.put("contactModel", mContactModel);
//
//                user.signUpInBackground(new SignUpCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if(e == null){
//                            Intent intent = new Intent(mContext, CardActivity.class);
//                            intent.putExtra(CardActivity.EXTRA_CONTACT_CARD, mContactModel);
//                            startActivity(intent);
//                        } else {
//                            e.printStackTrace();
//                        }
//                    }
//                });
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
            if(requestCode == REQUEST_IMAGE_CAPTURE){
                Bitmap imageBitmap = BitmapFactory.decodeFile(mFileUri.getPath());
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
