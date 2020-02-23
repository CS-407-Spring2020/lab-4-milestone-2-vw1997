package com.example.lab4milestone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    public void startListening(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            startListening();

        }
    }



    public void updateLocationInfo(Location location){
        Log.i("Logged", location.toString());

        TextView lat = (TextView)findViewById(R.id.Latitude);
        TextView lon = (TextView)findViewById(R.id.Longitude);
        TextView acc = (TextView)findViewById(R.id.Accuracy);
        TextView alt = (TextView)findViewById(R.id.Altitude);
        TextView add = (TextView)findViewById(R.id.Address);

        lat.setText("Latitude: " + location.getLatitude());
        lon.setText("Longitude: " + location.getLongitude());
        acc.setText("Accuracy: " + location.getAccuracy());
        alt.setText("Altitude: " + location.getAltitude() + " metres");

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            String address = "Could not find Address";
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(listAddresses != null){

                address = "Address: \n";

                if(listAddresses.get(0).getSubThoroughfare()!=null){
                    address+=listAddresses.get(0).getSubThoroughfare() + " ";
                }

                if(listAddresses.get(0).getThoroughfare()!=null){
                    address+=listAddresses.get(0).getThoroughfare() + "\n";
                }

                if(listAddresses.get(0).getLocality()!=null){
                    address+=listAddresses.get(0).getLocality() + "\n";
                }

                if(listAddresses.get(0).getPostalCode()!=null){
                    address+=listAddresses.get(0).getPostalCode() + "\n";
                }

                if(listAddresses.get(0).getCountryName()!=null){
                    address+=listAddresses.get(0).getCountryName();
                }
            }

            TextView addressTextView = (TextView) findViewById(R.id.Address);
            add.setText(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
                //Log.i("Location", location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        startListening();

        if(Build.VERSION.SDK_INT >= 23 &&
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        }

        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location!=null){
                updateLocationInfo(location);
            }
        }





    }
}
