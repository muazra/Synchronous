package com.android.synchronous.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.synchronous.R;
import com.android.synchronous.fragments.FindCardsFragment;
import com.android.synchronous.fragments.MyCardFragment;
import com.android.synchronous.fragments.SavedCardsFragment;
import com.android.synchronous.task.CheckNetworkTask;
import com.android.synchronous.task.SetUserLocationTask;
import com.parse.ParseUser;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private ViewPager mViewPager;
    private MenuItem mRefreshIcon;
    private ActionBar.Tab mTab;
    public static Context mContext;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckNetworkTask.check(this);

        mContext = this;

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mRefreshIcon = menu.findItem(R.id.action_refresh);

        if(ParseUser.getCurrentUser().getBoolean("discover")) {
            menu.findItem(R.id.action_discovery).setIcon(R.drawable.ic_discovery_on);
        } else {
            menu.findItem(R.id.action_discovery).setIcon(R.drawable.ic_discovery_off);
        }

        if(ParseUser.getCurrentUser().getJSONArray("requests").length() > 0) {
            menu.findItem(R.id.action_requests).setIcon(R.drawable.requestspending_on);
        } else {
            menu.findItem(R.id.action_requests).setIcon(R.drawable.requestspending_off);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_refresh:
                item.setActionView(R.layout.progress_loading);
                mViewPager.setAdapter(mAppSectionsPagerAdapter);
                mViewPager.setCurrentItem(mTab.getPosition());
                mRefreshIcon.setActionView(null);
                return true;

            case R.id.action_discovery:
                if(ParseUser.getCurrentUser().getBoolean("discover")) {
                    ParseUser.getCurrentUser().put("discover", false);
                    try {
                        ParseUser.getCurrentUser().save();
                        item.setIcon(R.drawable.ic_discovery_off);
                        Toast.makeText(mContext, "Sharing Disabled", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    Toast.makeText(mContext, "Gathering Location and Enabling Sharing..", Toast.LENGTH_SHORT).show();
                    SetUserLocationTask.setLocation(mContext, item);
                }
                return true;

            case R.id.action_requests:
                intent = new Intent(this, RequestsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;

            case R.id.action_sendphone:
                Log.d("Muaz", "HI");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
                builder.setView(input);
                builder.setTitle("Send Contact Info via SMS");
                builder.setMessage("Enter Phone Number");

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String number = input.getText().toString().trim();
                        String contact = ("Name: " + ParseUser.getCurrentUser().get("name") +
                                "\n" + "Phone: " + ParseUser.getCurrentUser().get("phone") +
                                "\n" + "Email: " + ParseUser.getCurrentUser().getEmail());

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
                        intent.putExtra("sms_body", Html.fromHtml(contact).toString());
                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                return true;

            case R.id.action_logout:
                ParseUser.getCurrentUser().put("discover", false);
                ParseUser.getCurrentUser().saveInBackground();

                com.facebook.Session fbs = com.facebook.Session.getActiveSession();
                if (fbs == null) {
                    fbs = new com.facebook.Session(mContext);
                    com.facebook.Session.setActiveSession(fbs);
                }
                fbs.closeAndClearTokenInformation();

                ParseUser.logOut();

                intent = new Intent(mContext, UserLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
        }
        return true;
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mTab = tab;
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            CheckNetworkTask.check(mContext);
            switch (i) {
                case 0:
                    return new MyCardFragment();
                case 1:
                    return new FindCardsFragment();
                case 2:
                    return new SavedCardsFragment();
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0)
                return "My Card";
            if(position == 1)
                return "Find Cards";
            if(position == 2)
                return "Saved Cards";

            return "Synchronous";
        }
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
