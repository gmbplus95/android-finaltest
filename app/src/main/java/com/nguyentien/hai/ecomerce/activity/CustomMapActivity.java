package com.nguyentien.hai.ecomerce.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nguyentien.hai.ecomerce.R;
import com.nguyentien.hai.ecomerce.ultil.CheckConnection;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Spinner spType;
    ArrayList<String> arrType;
    ArrayAdapter<String> adapterType;
    ProgressDialog progressDialog;
    //21.004256, 105.842054 nuce shop
    GoogleMap.OnMyLocationChangeListener listener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if (mMap != null) {
                mMap.clear();
                Marker marker = mMap.addMarker(new MarkerOptions().position(loc));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_map);
        addControls();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            addEvents();
        } else {
            CheckConnection.showToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối !");
            finish();
        }
    }

    private void addEvents() {
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeGoogleMapView(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void changeGoogleMapView(int position) {
        switch (position) {
            case 0:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case 4:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
        }
    }

    private void addControls() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spType = (Spinner) findViewById(R.id.spiner);
        arrType = new ArrayList<>();
        arrType.addAll(Arrays.asList(getResources().getStringArray(R.array.arrType)));
        adapterType = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrType);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapterType);

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("Thông báo");
        progressDialog.setMessage("Đang tải GoogleMap, vui lòng chờ trong giây lát !");
        progressDialog.setCanceledOnTouchOutside(false);
        //progressDialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                progressDialog.dismiss();
                LatLng nuceShop = new LatLng(21.004256, 105.842054);
                mMap.addMarker(new MarkerOptions().position(nuceShop).title("Nuce Shop").snippet("Cửa hàng công nghệ Nuce Shop"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(nuceShop));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationChangeListener(listener);
            }
        });
    }

}
