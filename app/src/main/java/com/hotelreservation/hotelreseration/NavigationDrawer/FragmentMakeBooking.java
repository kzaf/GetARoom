package com.hotelreservation.hotelreseration.NavigationDrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hotelreservation.hotelreseration.R;

public class FragmentMakeBooking extends Fragment {

	OnFragmentChangedListener mCallback;
    // Container Activity must implement this interface
    public interface OnFragmentChangedListener {
        public void onButtonClicked(String name);
    }

    public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_add_hotel, container, false);

        return rootView;
    }
}