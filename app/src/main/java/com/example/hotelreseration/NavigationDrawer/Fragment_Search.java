package com.example.hotelreseration.NavigationDrawer;

import java.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.hotelreseration.R;

public class Fragment_Search extends Fragment {
	
	public boolean flag=false;
	
	@Override
    public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_traveler_search, container, false);
        
        if (!flag){
       		final TextView Ascending = (TextView) rootView.findViewById(R.id.ascending);
       		final TextView Descending = (TextView) rootView.findViewById(R.id.descending);
       		
            Ascending.setVisibility(rootView.GONE);
            Descending.setVisibility(rootView.GONE);
        }
        
        final Button SetDate = (Button)rootView.findViewById(R.id.selectdate);
        SetDate.setOnClickListener(new View.OnClickListener() {
    	    public void onClick(View v) {   	
                ((MainActivity) getActivity()).SetDate(); 
                flag=true;
            }  
    	    });
        
        TextView tvDisplayDate = (TextView) rootView.findViewById(R.id.todaysdate);


        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview        // Month is 0 based, just add 1
        tvDisplayDate.setText(new StringBuilder().append(dd).append(" ").append("-").append(mm + 1).append("-").append(yy));
        
        return rootView;
    }   
}