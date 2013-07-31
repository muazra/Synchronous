/**
 * Muaz Rahman
 * Muntaser Ahmed
 * 29 July 2013
 */

package app.synchronous;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ContactCard extends Activity {

	private DrawerLayout myDrawerLayout;
	private ListView myDrawerList;
	private ActionBarDrawerToggle myDrawerToggle;

	private String barTitle = "";
	private String myDrawerTitle = "";
	private String[] people = {"Person A", "Person B"};

	int clickCount = 0;
	int proximityChoice = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_card);

		barTitle = (String) getTitle();

		/**
		 * NAVIGATION DRAWER
		 */

		myDrawerTitle = "Available";
		myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		myDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		myDrawerList = (ListView) findViewById(R.id.left_drawer);

		myDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, people));
		myDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Action Bar icon toggles Drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		myDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				myDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				0,  /* USED 0 to bypass "open drawer" description for accessibility */
				0  /* USED 0 to bypass "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(barTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(myDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		myDrawerLayout.setDrawerListener(myDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_card, menu);
		return true;
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = myDrawerLayout.isDrawerOpen(myDrawerList);
		menu.findItem(R.id.editButton).setVisible(!drawerOpen);
		menu.findItem(R.id.toggleButton).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		/**
		 * NAVIGATION DRAWER
		 */

		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (myDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		/**
		 * ACTION BAR
		 */

		switch(item.getItemId()) {

		case R.id.editButton:
			Intent editIntent = new Intent(this, EditActivity.class);
			startActivity(editIntent);
			break;

		case R.id.toggleButton:	

			item.setTitle("Toggle Sharing");

			/*
			 * Toggle Mechanism: Sharing icon switches images based on parity (evenness or oddness).
			 * Also, a Toast displays the change in state (Sharing On, Sharing Off). 
			 */

			clickCount++;

			if ( (clickCount % 2) == 1) {
				item.setIcon(R.drawable.worldon);
				Toast.makeText(getApplicationContext(), "Sharing On", Toast.LENGTH_SHORT).show();
			}
			else {
				item.setIcon(R.drawable.worldoff);
				Toast.makeText(getApplicationContext(), "Sharing Off", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.feedback_setting:
			Intent feedbackIntent = new Intent(this, FeedbackActivity.class);
			startActivity(feedbackIntent);
			break;

		case R.id.help_setting:
			Intent helpIntent = new Intent(this, HelpActivity.class);
			startActivity(helpIntent);
			break;
		}
		return true;
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		myDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		myDrawerToggle.onConfigurationChanged(newConfig);
	}

}
