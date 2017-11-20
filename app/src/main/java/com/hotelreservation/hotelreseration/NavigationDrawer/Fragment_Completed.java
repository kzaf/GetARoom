package com.hotelreservation.hotelreseration.NavigationDrawer;

import com.hotelreservation.hotelreseration.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_Completed extends Fragment {
	@Override
    public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_traveler_completed, container, false);
        
        return rootView;
    }   
}