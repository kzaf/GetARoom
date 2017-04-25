package com.example.hotelreseration.NavigationDrawer;

import android.os.Bundle;
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
 
/** 
 * A fragment that launches other parts of the demo application. 
 */ 
public class Fragment_maps extends android.support.v4.app.Fragment
	implements OnMapLongClickListener, OnMapClickListener, OnMapReadyCallback  {
 
    MapView mMapView; 
    private GoogleMap googleMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //This is added now, along with OnMapReadyCallback. In any case remove them
    }

    @Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
                             Bundle savedInstanceState) { 
        // inflate and return the layout 
        View v = inflater.inflate(R.layout.fragment_maps, container, 
                false); 
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
        // Detect location
        googleMap.setMyLocationEnabled(true);
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

        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMapClickListener(this);


        // create marker
        MarkerOptions thessaloniki = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Thessaloniki");
        //----------------
        MarkerOptions LeykosPyrgos = new MarkerOptions().position(new LatLng(40.626401, 22.948352)).title("Leykos Pyrgos");

        // Changing marker icon
        thessaloniki.icon(BitmapDescriptorFactory .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        //----------------
        LeykosPyrgos.icon(BitmapDescriptorFactory .fromResource(R.drawable.whitetower));

        // adding marker
        googleMap.addMarker(thessaloniki);
        CameraPosition cameraPositionThess = new CameraPosition.Builder() .target(new LatLng(40.639350, 22.944607)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory .newCameraPosition(cameraPositionThess));
        //----------------
        googleMap.addMarker(LeykosPyrgos);
        CameraPosition cameraPositionLeykosPyrgos = new CameraPosition.Builder() .target(new LatLng(40.626401, 22.948352)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory .newCameraPosition(cameraPositionLeykosPyrgos));


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
        	googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);;
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
    	 if (SelectUserActivity.flagOwner==true){
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