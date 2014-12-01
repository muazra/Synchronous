package com.android.synchronous.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.synchronous.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_mycard, container, false);
        ParseUser currentUser = ParseUser.getCurrentUser();

        Log.d("MUAZ", "my_cards fragment onCreateView");

        ParseFile imageFile = (ParseFile) currentUser.get("photo");
        imageFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                CircleImageView mImage = (CircleImageView) root.findViewById(R.id.cardImage);
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(bytes, 0,
                        bytes.length);
                mImage.setImageBitmap(imageBitmap);
            }
        });

        TextView mName = (TextView) root.findViewById(R.id.cardName);
        mName.setText(currentUser.get("name").toString());

        TextView mEmail = (TextView) root.findViewById(R.id.cardEmail);
        mEmail.setText(currentUser.getEmail());

        TextView mNumber = (TextView) root.findViewById(R.id.cardNumber);
        mNumber.setText(currentUser.get("phone").toString());

        TextView mCompany = (TextView) root.findViewById(R.id.cardCompany);
        mCompany.setText(currentUser.get("company").toString());

        TextView mTitle = (TextView) root.findViewById(R.id.cardTitle);
        mTitle.setText(currentUser.get("title").toString());

        return root;
    }

}
