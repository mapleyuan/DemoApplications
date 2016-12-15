package com.maple.yuanweinan.demoapplication.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import com.maple.yuanweinan.demoapplication.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yuanweinan on 16/12/14.
 */
public class DemoLocationController implements LocationListener {

    /**
     *
     */
    public interface GPSLocationListener {
        void onFinish(Location location);
        void onFail();
    }

    public static DemoLocationController getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DemoLocationController.class) {
                if (sInstance == null) {
                    sInstance = new DemoLocationController(context);
                }
            }
        }
        return sInstance;
    }

    public void addGPSLocationListener(GPSLocationListener locationListener) {

        if (locationListener == null) {
            return;
        }
        mGPSLocationListeners.add(locationListener);
    }
    //TODO
//    public void removeGPSLocationListener() {}

    public void refresh() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void onDestroy() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(Location location) {
        LogUtils.d("-------------------->>onLocationChanged");
        for(GPSLocationListener listener : mGPSLocationListeners) {
            listener.onFinish(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LogUtils.d("-------------------->>onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        LogUtils.d("-------------------->>onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        LogUtils.d("-------------------->>onProviderDisabled");
    }



    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }




    private volatile static DemoLocationController sInstance;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private Context mContext;
    private LocationManager mLocationManager;
    private List<GPSLocationListener> mGPSLocationListeners = new ArrayList<>();

    private DemoLocationController(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null!!!");
        }
        mContext = context.getApplicationContext();
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
