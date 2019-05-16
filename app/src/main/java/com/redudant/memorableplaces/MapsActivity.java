package com.redudant.memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    LocationManager locationManager;

    LocationListener locationListener;

    //method ini menambahkan hasil izin
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //kemudian kita cek apakah hsail grand lebih bersar dari nol
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                //kaemudian kami mendapatkan lokasi user dan kami kirimkan lokasi user pada peta menggunkan lokasi terakhir user yang diketahui
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                //dan lokasi user akan menjadi judul
                centerMapsOnLocation(lastKnownLocation, "Your location.");
            }
        }
    }

    //untuk membuat titik lokasi baru
    public void centerMapsOnLocation(Location location, String title) {

        //menetukan locasi user
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.clear(); //menghapus semua penanda location

        //lakukan pemerikasaan apakah judul sama dengan lokasi anda
        if (title != "Your location") {

            //menambah penanda baru dan lokasi yang kita gunakan (userLocation) judul lokasi kita berada
            //maka kita akan menambahkan arameter String title pada method centerMapsOnLocation
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
        }

        //ini akam zoom penanda lokasi kita dan jarak nya bebas disini saya menggunakan 10
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //menambahkan kode ini untu mendapatkan peta google
        mMap.setOnMapLongClickListener(this);

        Intent intent = getIntent();
        //Toast.makeText(getApplicationContext(), intent.getStringExtra("placesNumber"), Toast.LENGTH_LONG).show();

        if (intent.getIntExtra("placesNumber", 0) == 0) {

            //Zoom in on user's location

            //kita akan mendapatkan lokasi pengguna dengan locationmanager
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            //ini akan memberikan metode informasi lokasi baru
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    //kami akan memusatkan lokasinya dan kita letakan method centerMapsOnLocation disini
                    centerMapsOnLocation(location, "your location..");

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

            try {

                //sekarang kita cek kemali, apakah pengguna menggunakan versi 23 marshmellow
                if (Build.VERSION.SDK_INT > 23) {

                    //kami meminta pembaruan lokasi dan Lparameter Location kita ambil dari method
                    //GPS_PROVIDER 0,0 saya pikir jarak dan waktu dari pembaruagetLastKnownLocationn terakhir dari LocationListener mengigatkan saya pada manifes pada saat
                    //kita membuat MapsActivity <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                } else {
                    //kalo tidak, kita perlu memerikas apakah kita punya izin akses lokasi dengan PackageManager.PERMISSION_GRANTED mendapatkan izi yang diberikan user
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //jadi itu artinya kita mendapatkan izin dari user dan mendapatkan lokasi user dan kami mendapatkan lokasi yang diketahui
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                        //kaemudian kami mendapatkan lokasi user dan kami kirimkan lokasi user pada peta menggunkan lokasi terakhir user yang diketahui
                        Location lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        //dan lokasi user akan menjadi judul
                        centerMapsOnLocation(lastKnowLocation, "Your location.");

                    } else {

                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }

            }
            catch (SecurityException e)  {
                Log.e("Exception: %s", e.getMessage());
            }

        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        //penanda peta tempat yang baru
        mMap.addMarker(new MarkerOptions().position(latLng).title("Your new memorable places"));
    }

}
