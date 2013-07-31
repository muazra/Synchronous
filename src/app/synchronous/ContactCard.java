/**
 * Muaz Rahman
 * Muntaser Ahmed
 * 29 July 2013
 */

package app.synchronous;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
	
	private String myDrawerTitle = "";
	private String[] people = {"Person A", "Person B"};

	int clickCount = 0;
	int proximityChoice = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_card);
		
		/**
		 * NAVIGATION DRAWER
		 */
		
		myDrawerTitle = "Available";
		myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
        //myDrawerList.setAdapter(new ArrayAdapter<String>(this,
        //		R.layout.drawer_list_item, people));
        
        //myDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_card, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
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

//	private class DrawerItemClickListener implements ListView.OnItemClickListener {
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            selectItem(position);
//        }
//    }
	
	private void selectItem(int position) {
		
		// USE FRAGMENT HERE
		
//		myDrawerList.setItemChecked(position, true);
//        myDrawerLayout.closeDrawer(myDrawerList);
		
	}

}
