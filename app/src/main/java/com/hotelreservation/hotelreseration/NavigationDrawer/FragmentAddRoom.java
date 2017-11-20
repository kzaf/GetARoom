package com.hotelreservation.hotelreseration.NavigationDrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hotelreservation.hotelreseration.R;

public class FragmentAddRoom extends Fragment {

    public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_make_booking, container, false);
        
        return rootView;
    }
}