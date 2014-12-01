package com.android.synchronous.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.synchronous.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class UserLoginActivity extends Activity {

    private TextView mUsername;
    private TextView mPassword;
    private Button mLoginButton;
    private Button mFBLoginButton;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Parse.initialize(this, "TmZHxDzLuiQBpzODDec0zDix04RCF2fSdzLSnBLB", "5Aedz07mBLrFQuVT6Gj7OVh2dVqEPD35531pTBPk");
        ParseFacebookUtils.initialize("569756256459862");

        if(ParseUser.getCurrentUser() != null) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        mUsername = (TextView) findViewById(R.id.username_login);
        mPassword = (TextView) findViewById(R.id.password_login);

        TextView signupTextView = (TextView) findViewById(R.id.link_signup);
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserSignupActivity.class);
                startActivity(intent);
            }
        });

        mLoginButton = (Button) findViewById(R.id.button_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logInInBackground(mUsername.getText().toString(), mPassword.getText().toString(),
                        new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            Intent intent = new Intent(mContext, MainActivity.class);
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

        mFBLoginButton = (Button) findViewById(R.id.button_fb_login);
        mFBLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logIn(UserLoginActivity.this, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser == null) {
                            Toast.makeText(mContext, "Facebook Login unsuccessful.", Toast.LENGTH_SHORT).show();

                        } else if (parseUser.isNew()) {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(mContext, "Welcome to Synchronous!", Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(mContext, "Welcome Back!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActionBar().setTitle("Welcome");
        return true;
    }

}

