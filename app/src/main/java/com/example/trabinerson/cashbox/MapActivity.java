package com.example.trabinerson.cashbox;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LoaderManager.LoaderCallbacks<List<Merchant>> , GoogleMap.OnMarkerClickListener {

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    MapFragment mapFragment;
    String pinText = "Current Location";
    GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        getSupportLoaderManager().initLoader(0, null, this);
        CameraPosition mPosition = CameraPosition.builder()
                .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                .zoom(14)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mPosition));
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                .title(pinText));
        //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        showPermissionDialog();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mLastLocation != null) {

                    }

                } else {

                    mLastLocation = new Location("Office");
                    mLastLocation.setLatitude(32.0628141);
                    mLastLocation.setLongitude(34.7704065);
                    pinText = "Dummy Location";
                }
                mapFragment.getMapAsync(this);
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void showPermissionDialog() {
        if (!checkPermission()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION },
                    1);
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {


            } else {

                mLastLocation = new Location("Office");
                mLastLocation.setLatitude(32.0628141);
                mLastLocation.setLongitude(34.7704065);
                pinText = "Dummy Location";
            }
            mapFragment.getMapAsync(this);
        }
    }

    public boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public Loader<List<Merchant>> onCreateLoader(int id, Bundle args) {
        return new PlacesLoader(this,mLastLocation.getLatitude(),mLastLocation.getLongitude());
    }

    @Override
    public void onLoadFinished(Loader<List<Merchant>> loader, List<Merchant> data) {
        if (data == null) {
            Toast.makeText(this, "Server error... :(", Toast.LENGTH_LONG).show();
            return;
        }
        for(int i=0;i<data.size();i++){
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(data.get(i).getGeometry().location.getLat(), data.get(i).getGeometry().location.getLng()))
                    .title(data.get(i).getName())
                    .snippet(data.get(i).getVicinity())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
        //load pins
    }

    @Override
    public void onLoaderReset(Loader<List<Merchant>> loader) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(marker.getTitle());
        dialog.setMessage(marker.getSnippet());
        dialog.setNegativeButton("Cancel", null);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        dialog.show();
        return true;
    }
}
