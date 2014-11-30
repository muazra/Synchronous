package com.android.synchronous.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.android.synchronous.R;
import com.android.synchronous.fragments.FindCardsFragment;
import com.android.synchronous.fragments.MyCardFragment;
import com.android.synchronous.fragments.SavedCardsFragment;
import com.android.synchronous.task.SetUserLocationTask;
import com.parse.ParseUser;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private ViewPager mViewPager;
    private MenuItem mRefreshIcon;
    private Context mContext = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if(ParseUser.getCurrentUser().getJSONArray("pending").length() > 0) {
            menu.findItem(R.id.action_pending).setIcon(R.drawable.requestspending_on);
        } else {
            menu.findItem(R.id.action_pending).setIcon(R.drawable.requestspending_off);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                item.setActionView(R.layout.progress_loading);
                mViewPager.setAdapter(mAppSectionsPagerAdapter);
                mRefreshIcon.setActionView(null);
                return true;

            case R.id.action_discovery:
                if(ParseUser.getCurrentUser().getBoolean("discover")) {
                    item.setIcon(R.drawable.ic_discovery_off);
                    ParseUser.getCurrentUser().put("discover", false);
                    ParseUser.getCurrentUser().saveInBackground();
                    return true;
                }
                item.setIcon(R.drawable.ic_discovery_on);
                SetUserLocationTask.setLocation(mContext);
                return true;

            case R.id.action_requests:
                Intent intent = new Intent(this, RequestsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            default:
                return true;
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
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
            switch (i) {
                case 0:
                    return new MyCardFragment();
                case 1:
                    return new FindCardsFragment();
                case 2:
                    return new SavedCardsFragment();
                default:
                    return new Fragment();
            }
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
