package com.example.hotelreseration.NavigationDrawer;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.hotelreseration.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import static com.example.hotelreseration.NavigationDrawer.MainActivity.dboFKey;

public class Fragment_Booked extends Fragment {
    static ListView myBookingslistView;
    static SimpleAdapter adapter;
    static ArrayList<HashMap<String,String>> records = new ArrayList<>(); //Dhmiourgw HashMap gia na mporw na valw polla String na fainontai se kathe stoixeio ths listas

    static TextView nobookings;

	@Override
	public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState){
	  	    View view = inflater.inflate(R.layout.fragment_traveler_booked, container,false);

		nobookings=(TextView) view.findViewById(R.id.nobookingtxt);

        myBookingslistView = (ListView) view.findViewById(R.id.myBookingsList);
        adapter = new SimpleAdapter(getActivity(), records, R.layout.mytextview, new String[] {"hotelname","dates"}, new int[] {R.id.tv,R.id.sub});
        myBookingslistView.setAdapter(adapter);// Assign adapter to ListView

        ((MainActivity) getActivity()).loadMyBookings(dboFKey);

	    if(myBookingslistView.getCount()==0){
    		nobookings.setVisibility(View.VISIBLE);
    	}else{
    		nobookings.setVisibility(View.GONE);
    	}
	    
	    return view;
	}
}