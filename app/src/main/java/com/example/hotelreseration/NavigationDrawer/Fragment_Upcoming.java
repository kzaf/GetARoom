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

public class Fragment_Upcoming extends Fragment {

    static ArrayList<HashMap<String,String>> records = new ArrayList<>();
	static ListView UpcomminglistView;
    static SimpleAdapter adapter;
    static TextView txt;

	@Override
	public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState){
	  	    View view = inflater.inflate(R.layout.fragment_owner_upcoming, container,false);

	    UpcomminglistView = (ListView) view.findViewById(R.id.upcomingResList);
	    txt=(TextView) view.findViewById(R.id.nobookingtxt);

        adapter = new SimpleAdapter(getActivity(), records, R.layout.mytextview, new String[] {"HotelandTravelertitle","dates"},
				new int[] {R.id.tv,R.id.sub});

        UpcomminglistView.setAdapter(adapter);// Assign adapter to ListView

        ((MainActivity) getActivity()).loadOwnerReservations(dboFKey);
        Fragment_Upcoming.adapter.notifyDataSetChanged(); //Notify the adapter for the update
	    
	    if(UpcomminglistView.getCount()==0){
    		txt.setVisibility(View.VISIBLE);
    	}else{
    		txt.setVisibility(View.GONE);
    	}
	    
	    return view;
	}
}