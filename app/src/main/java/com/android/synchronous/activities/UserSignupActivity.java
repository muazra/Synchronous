package com.android.synchronous.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
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
import android.widget.Toast;
import android.graphics.Matrix;

import com.android.synchronous.R;
import com.android.synchronous.task.CheckNetworkTask;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class UserSignupActivity extends Activity {

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

    private ParseUser mParseUser;
    private Context mContext = this;

    private Uri mFileUri;
    private byte[] imageBytes;
    private ParseFile imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersignup);

        CheckNetworkTask.check(this);

        mParseUser = new ParseUser();

        mUsername = (TextView) findViewById(R.id.username_new);
        mPassword = (TextView) findViewById(R.id.password_new);

        mName = (TextView) findViewById(R.id.name_new);
        mEmail = (TextView) findViewById(R.id.email_new);
        mPhone = (TextView) findViewById(R.id.phone_new);
        mCompany = (TextView) findViewById(R.id.company_new);
        mTitle = (TextView) findViewById(R.id.title_new);

        mUploadPhoto = (Button) findViewById(R.id.upload_new);
        mSubmit = (Button) findViewById(R.id.submit_new);
        mTakePhoto = (Button) findViewById(R.id.take_photo_new);

        mImage = (ImageView) findViewById(R.id.image_new);

        mUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File imagesFolder = new File(Environment.getExternalStorageDirectory(),"Camera");
                imagesFolder.mkdirs();

                File imageFile = new File(imagesFolder,"image.jpg");

                mFileUri = Uri.fromFile(imageFile);
                Log.d("cameraActivity", mFileUri.getPath());

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,mFileUri);

                startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mParseUser.setUsername(mUsername.getText().toString());
                mParseUser.setPassword(mPassword.getText().toString());
                mParseUser.setEmail(mEmail.getText().toString());
                mParseUser.put("name", mName.getText().toString());
                mParseUser.put("phone", mPhone.getText().toString());
                mParseUser.put("company", mCompany.getText().toString());
                mParseUser.put("title", mTitle.getText().toString());
                mParseUser.put("discover", false);
                mParseUser.put("city", "none");
                mParseUser.put("saved", new JSONArray());
                mParseUser.put("requests", new JSONArray());

                imageFile = new ParseFile("image.png", imageBytes);
                imageFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        mParseUser.put("photo", imageFile);

                        mParseUser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    Intent intent = new Intent(mContext, UserLoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    Toast.makeText(mContext, "Signup successful. Welcome!",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    e.printStackTrace();
                                    Toast.makeText(mContext, "Signup unsuccessful. Please try again.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
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
                imageBytes = stream.toByteArray();

            }
            if(requestCode == REQUEST_IMAGE_CAPTURE){
                Bitmap imageBitmap = BitmapFactory.decodeFile(mFileUri.getPath());
                Bitmap compressedBitmap = scaleDownBitmap(imageBitmap, 75, mContext);
                Bitmap validatedBitmap = imageOreintationValidator(compressedBitmap, mFileUri.getPath());
                mImage.setImageBitmap(validatedBitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                validatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();
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

    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

}
