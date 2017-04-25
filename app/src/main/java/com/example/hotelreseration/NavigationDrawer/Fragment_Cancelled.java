package com.example.hotelreseration.NavigationDrawer;

//import android.R;
import com.example.hotelreseration.R;
import com.example.hotelreseration.R.layout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_Cancelled extends Fragment {
	@Override
    public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_traveler_cancelled, container, false);
       
        return rootView;
    }   
}