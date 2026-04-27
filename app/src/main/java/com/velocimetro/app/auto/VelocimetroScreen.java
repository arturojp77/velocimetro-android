package com.velocimetro.app.auto;

import androidx.lifecycle.LifecycleOwner;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.Pane;
import androidx.car.app.model.PaneTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;
import androidx.core.app.ActivityCompat;

public class VelocimetroScreen extends Screen implements LocationListener {

    private LocationManager locationManager;
    private float currentSpeedKmh = 0f;

    public VelocimetroScreen(@NonNull CarContext carContext) {
        super(carContext);
        startTracking();
    }

    private void startTracking() {
        locationManager = (LocationManager)
            getCarContext().getSystemService(CarContext.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getCarContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 2, this);
        }
    }

    @NonNull
    @Override
    public Template onGetTemplate() {
        String speedText = String.format("%.0f km/h", currentSpeedKmh);

        String label;
        if (currentSpeedKmh < 100f) {
            label = "Velocidad normal";
        } else if (currentSpeedKmh < 150f) {
            label = "⚠️ Velocidad alta";
        } else {
            label = "🚨 Reducir velocidad";
        }

        Row speedRow = new Row.Builder()
            .setTitle(speedText)
            .addText(label)
            .build();

        Pane pane = new Pane.Builder()
            .addRow(speedRow)
            .build();

        return new PaneTemplate.Builder(pane)
            .setHeaderAction(Action.APP_ICON)
            .setTitle("Velocímetro")
            .build();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location.hasSpeed()) {
            currentSpeedKmh = location.getSpeed() * 3.6f;
            invalidate(); // Redibuja la pantalla en Auto
        }
    }

    @Override public void onProviderEnabled(@NonNull String provider) {}
    @Override public void onProviderDisabled(@NonNull String provider) {}
    @Override public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }
}
