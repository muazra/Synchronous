// Muaz Rahman & Muntaser Ahmed
// Birthday: 29 July 2013


package app.synchronous;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ContactCard extends Activity {

	int clickCount = 0;

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
			
			/*
			 * Toggle Mechanism: Sharing icon switches images based on parity (evenness or oddness).
			 * Also, a Toast displays the change in state (Sharing On, Sharing Off). 
			 */

			clickCount++;

			if ( (clickCount % 2) == 1) {
				item.setIcon(R.drawable.on);
				Toast.makeText(getApplicationContext(), "Sharing On", Toast.LENGTH_SHORT).show();
			}
			else {
				item.setIcon(R.drawable.off);
				Toast.makeText(getApplicationContext(), "Sharing Off", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return true;
	}


}
