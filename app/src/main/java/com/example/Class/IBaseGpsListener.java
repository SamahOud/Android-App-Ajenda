package com.example.Class;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

public interface IBaseGpsListener extends LocationListener , GpsStatus.Listener {
    @Override
    public default void onLocationChanged(@NonNull Location locations) {

    }

    @Override
    public default void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public default void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public default void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public default void onGpsStatusChanged(int event) {

    }
}
