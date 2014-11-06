package com.android.synchronous;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class CardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        getActionBar().hide();
        return true;
    }
}
