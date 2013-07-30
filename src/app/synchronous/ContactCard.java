// Muaz Rahman & Muntaser Ahmed
// Birthday: 29 July 2013

package app.synchronous;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ContactCard extends Activity {

	int clickCount = 0;
	int proximityChoice = 0;

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
			
		case R.id.proximity_setting:
			showProximityDialog();
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
	
	/*
	 * Radio Button Dialog Method
	 */
	
	private void showProximityDialog() {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Proximity");
		final String[] proximities = {"Close", "Nearby", "Far"};
		dialogBuilder.setSingleChoiceItems(proximities, proximityChoice, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				proximityChoice = which;
				dialog.dismiss();
			}
		});
		
		AlertDialog alertDialog = dialogBuilder.create();
		alertDialog.show();
		
		
	}


}
