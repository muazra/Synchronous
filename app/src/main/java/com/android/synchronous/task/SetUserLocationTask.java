package com.android.synchronous.task;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.synchronous.R;
import com.android.synchronous.activities.MainActivity;
import com.android.synchronous.util.GeocodeUtil;
import com.android.synchronous.util.LocationEnabledUtil;
import com.parse.ParseUser;

import java.util.Locale;

public class SetUserLocationTask {

    public static void setLocation(final Context mContext, final MenuItem item){

        LocationManager mlocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        LocationEnabledUtil.checkLocationEnabled(mlocationManager, mContext);

        LocationListener mlocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                GeocodeUtil geocode = new GeocodeUtil(mContext.getApplicationContext(), Locale.getDefault());
                ParseUser.getCurrentUser().put("city", geocode.find(location).get(0).getLocality());
                ParseUser.getCurrentUser().put("discover", true);

                try {
                    ParseUser.getCurrentUser().save();
                    item.setIcon(R.drawable.ic_discovery_on);
                    Toast.makeText(MainActivity.mContext, "Sharing enabled", Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onProviderDisabled(String s) {}
        };
        mlocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mlocationListener, null);
    }
}
