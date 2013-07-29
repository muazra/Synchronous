// Muaz Rahman & Muntaser Ahmed
// Birthday: 29 July 2013

package app.synchronous;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class ContactCard extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_card);


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
		switch(item.getItemId()) {
		case R.id.toggleButton:	
			item.setTitle("Toggle Sharing");

			int clickNumber = 0;
			clickNumber++;
			if ( (clickNumber % 2) == 1) { 
				item.setIcon(R.drawable.on);
			}
			if ( (clickNumber % 2) == 0) { 
				item.setIcon(R.drawable.off);
			}

			item.setIcon(R.drawable.on);
			break;
		}
		return true;
	}


}
