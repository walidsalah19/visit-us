package com.example.visitus.admin.map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.example.visitus.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MapsActivity extends FragmentActivity implements

        OnMapReadyCallback{
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
           @Override
           public void onMapClick(@NonNull LatLng latLng) {
               Log.d("location",latLng.longitude+"");
               //Success
              SweetAlertDialog pDialogSuccess= new SweetAlertDialog(MapsActivity.this, SweetAlertDialog.NORMAL_TYPE);
              pDialogSuccess.setTitleText(getString(R.string.select_location));
               pDialogSuccess.setConfirmText(getString(R.string.dialog_ok));
               pDialogSuccess.setCancelText("No");
               pDialogSuccess.setConfirmClickListener(sweetAlertDialog -> {
                   move_location.setLatitude(latLng.latitude+"");
                   move_location.setLongitude(latLng.longitude+"");
                   pDialogSuccess.dismiss();
                   finish();
               });
               pDialogSuccess.setCancelClickListener(sweetAlertDialog ->
               {
                   pDialogSuccess.dismiss();
               });
               pDialogSuccess.setCancelable(false);
               pDialogSuccess.show();
           }
       });
    }
}