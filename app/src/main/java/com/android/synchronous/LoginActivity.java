package com.android.synchronous;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

    private TextView mUsername;
    private TextView mPassword;

    private TextView signupTextView;
    private Button loginButton;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Parse.initialize(this, "TmZHxDzLuiQBpzODDec0zDix04RCF2fSdzLSnBLB", "5Aedz07mBLrFQuVT6Gj7OVh2dVqEPD35531pTBPk");

        mUsername = (TextView) findViewById(R.id.username_login);
        mPassword = (TextView) findViewById(R.id.password_login);

        signupTextView = (TextView) findViewById(R.id.link_signup);
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewUserActivity.class);
                startActivity(intent);
            }
        });

        loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logInInBackground(mUsername.getText().toString(), mPassword.getText().toString(),
                        new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            Intent intent = new Intent(mContext, CardActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(mContext, "Login unsuccessful. Check credentials.",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActionBar().hide();
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
