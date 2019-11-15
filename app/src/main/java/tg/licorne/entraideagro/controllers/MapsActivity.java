package tg.licorne.entraideagro.controllers;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import tg.licorne.entraideagro.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    // [5.4138, -0.7551]

    private double longitude, latitude;
    private String region, prefecture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            longitude = intentExtrat.getDoubleExtra("LONGITUDE", 0);
            latitude = intentExtrat.getDoubleExtra("LATITUDE", 0);
            region = intentExtrat.getStringExtra("REGION");
            prefecture = intentExtrat.getStringExtra("PREFECTURE");
        }

        Toast.makeText(this, region, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng reg = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(reg).title(region+" "+prefecture));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(reg));
    }
}
