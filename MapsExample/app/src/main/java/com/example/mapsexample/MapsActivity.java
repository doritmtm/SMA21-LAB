package com.example.mapsexample;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapsexample.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
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
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;

                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                checkPermission();
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                LatLng pos1=new LatLng(44.431924,26.103461);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(pos1));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                IconGenerator icongen=new IconGenerator(MapsActivity.this);
                icongen.setColor(ContextCompat.getColor(MapsActivity.this,android.R.color.holo_blue_dark));
                icongen.setTextAppearance(R.color.textColor);
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(icongen.makeIcon()))
                        .position(pos1));
                mMap.addPolyline(new PolylineOptions()
                            .add(sydney)
                            .add(pos1)
                            .color(Color.GREEN)
                            .width(8)

                );
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        Toast.makeText(MapsActivity.this, "Distance: "+Double.toString(SphericalUtil.computeDistanceBetween(pos1,sydney)),Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }
        });
    }

    private void checkPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},5);
        }
        else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermission();
    }
}