package com.example.hotelreseration.NavigationDrawer;

//import android.R;
import java.util.ArrayList;

import com.example.hotelreseration.R;
import com.example.hotelreseration.R.layout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment_Booked extends Fragment {
	ArrayList<String> Target = new ArrayList<String>();
public void a(){
	Target.add("hi");
    Target.add("hello");
    Target.add("wrum");
	}
	TextView txt;

	@Override
	public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState){
	  	    View view = inflater.inflate(R.layout.fragment_traveler_booked, container,false);

	    ListView listView = (ListView) view.findViewById(R.id.list1);
	    txt=(TextView) view.findViewById(R.id.nobookingtxt);
	    
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
	    		R.layout.mytextview,R.id.tv, Target);
	    adapter.setNotifyOnChange(true);
	    
	    // Assign adapter to ListView
	    listView.setAdapter(adapter);
	    
	    if(listView.getCount()==0){
    		txt.setVisibility(View.VISIBLE);
    	}else{
    		txt.setVisibility(View.GONE);
    	}
	    
	    return view;
	}
}