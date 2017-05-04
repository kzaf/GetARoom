package com.example.hotelreseration.NavigationDrawer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hotelreseration.R;
import com.example.hotelreseration.SelectUserActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_maps extends android.support.v4.app.Fragment implements OnMapLongClickListener, OnMapClickListener, OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        // Detect location
        if(checkPermission())
            googleMap.setMyLocationEnabled(true);
        else askPermission();

        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Turns traffic layer on
        googleMap.setTrafficEnabled(true);

        // Enables indoor maps
        googleMap.setIndoorEnabled(true);

        // Enables indoor maps
        googleMap.setIndoorEnabled(true);

        // Turns on 3D buildings
        googleMap.setBuildingsEnabled(true);

        // Show Zoom buttons
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        
        // latitude and longitude
        double latitude = 40.639350;
        double longitude = 22.944607;

//        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//
//        Location location = locationManager.getLastKnownLocation(locationManager
//                .getBestProvider(criteria, false));
//        double lat = location.getLatitude();
//        double lon = location.getLongitude();

        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMapClickListener(this);
        
        // create marker
        MarkerOptions thessaloniki = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Thessaloniki");
        MarkerOptions LeykosPyrgos = new MarkerOptions().position(new LatLng(40.626401, 22.948352)).title("Leykos Pyrgos");

        // Changing marker icon
        thessaloniki.icon(BitmapDescriptorFactory .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        LeykosPyrgos.icon(BitmapDescriptorFactory .fromResource(R.drawable.whitetower));

        // adding marker
        googleMap.addMarker(thessaloniki);
        CameraPosition cameraPositionThess = new CameraPosition.Builder() .target(new LatLng(latitude, longitude)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory .newCameraPosition(cameraPositionThess));

        googleMap.addMarker(LeykosPyrgos);
//        CameraPosition cameraPositionLeykosPyrgos = new CameraPosition.Builder() .target(new LatLng(40.626401, 22.948352)).zoom(12).build();
//        googleMap.animateCamera(CameraUpdateFactory .newCameraPosition(cameraPositionLeykosPyrgos));
    }

    public void onLocationChanged(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }
    // Asks for permission
    private void askPermission() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                1
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case 1: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    if(checkPermission())
                        googleMap.setMyLocationEnabled(true);

                } else {
                    // Permission denied

                }
                break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate and return the layout 
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        //activate menu button
        setHasOptionsMenu(true);

        mMapView.onResume();// needed to get the map to display immediately 

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //get the map
        mMapView.getMapAsync(this);

        // Perform any camera updates here
        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.fragment_maps, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("onOptionsItemSelected","yes");
        switch (item.getItemId()) {
        case R.id.HYBRID:
        	googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            return true;
        case R.id.SATELLITE:
        	googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        case R.id.TERRAIN:
        	googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            return true;
        case R.id.NORMAL:
        	googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapClick(LatLng point) {
		Toast.makeText(getActivity(),point.toString(),Toast.LENGTH_SHORT).show();
		googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
    }
    
    @Override 
    public void onMapLongClick(LatLng point) {
    	 if (SelectUserActivity.flagOwner){
    		 googleMap.addMarker(new MarkerOptions()
             .position(point)
             .title(MainActivity.onomaxarth)           
             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    		Toast.makeText(getActivity(),"Hotel pinned at: "+point.toString(),Toast.LENGTH_SHORT).show();
         }else{
     		Toast.makeText(getActivity(),point.toString(),Toast.LENGTH_SHORT).show();
         }
    }
 
    @Override 
    public void onResume() { 
        super.onResume(); 
        mMapView.onResume(); 
    } 
 
    @Override 
    public void onPause() { 
        super.onPause(); 
        mMapView.onPause(); 
    } 
 
    @Override 
    public void onDestroy() { 
        super.onDestroy(); 
        mMapView.onDestroy(); 
    } 
 
    @Override 
    public void onLowMemory() { 
        super.onLowMemory(); 
        mMapView.onLowMemory(); 
    }
}