//package com.android.synchronous;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.parse.GetDataCallback;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseUser;
//
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class ContactListAdapter extends ArrayAdapter<ParseUser> {
//    private final List<ParseUser> mModelList;
//    private final Context mContext;
//
//    public ContactListAdapter(Context context, List<ParseUser> models){
//        super(context, R.layout.card_row, models);
//        mContext = context;
//        mModelList = models;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater) mContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View modelRow = inflater.inflate(R.layout.card_row, parent, false);
//
//        ParseUser model = mModelList.get(position);
//
//        TextView modelNameTextView = (TextView) modelRow.findViewById(R.id.card_row_name);
//        modelNameTextView.setText(model.get("name").toString());
//
//        final CircleImageView modelCircleImageView = (CircleImageView) modelRow.findViewById(R.id.card_row_photo);
//
//        ParseFile imageFile = (ParseFile) model.get("photo");
//        imageFile.getDataInBackground(new GetDataCallback() {
//            @Override
//            public void done(byte[] bytes, ParseException e) {
//                Bitmap imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                modelCircleImageView.setImageBitmap(imageBitmap);
//            }
//        });
//
//        return modelRow;
//    }
//}
