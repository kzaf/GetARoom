package com.example.hotelreseration.NavigationDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hotelreseration.R;

public class Fragment_hotels extends Fragment {

	static ListView listView;
	SimpleAdapter adapter;
	ArrayList<HashMap<String,String>> records = new ArrayList<>(); //Dhmiourgw HashMap gia na mporw na valw polla String na fainontai se kathe stoixeio ths listas

    static TextView txt;
	
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState){

		View view = inflater.inflate(R.layout.fragment_hotels, container,false);
		listView = (ListView) view.findViewById(R.id.list);
		txt=(TextView) view.findViewById(R.id.nobookingtxt);
		MainActivity.flagpinhotel = false;

		adapter = new SimpleAdapter(getActivity(), records, R.layout.mytextview,
                new String[] {"name","city"}, new int[] {R.id.tv,R.id.sub});

	    // Assign adapter to ListView
		listView.setAdapter(adapter);

		if(listView.getCount()==0){
    		txt.setVisibility(View.VISIBLE); //show/hide the "add a new hotel" message
    	}else{
    		txt.setVisibility(View.GONE);
    	}
		
		// BUTTON
	    Button btnAddHotel = (Button) view.findViewById(R.id.addbutton);
	    btnAddHotel.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
	    	txt.setVisibility(View.GONE);
            switch (v.getId()) {
            case R.id.addbutton:
            	if (listView.getCount()<=2){
            		((MainActivity) getActivity()).alerthotel();
            	}
            	else{
            		Toast.makeText(getActivity(),"You can add up to 3 hotels! Please read the info",Toast.LENGTH_LONG).show();
            	}
            	
            break;
            }
        }  
	    });
	    //energopoiw to click event otan epilegw ena item apo to listview
	    listView.setOnItemClickListener(new OnItemClickListener() {
	    	  public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	    	       switch(position) {
	    	        case 0:
	    	        	((MainActivity) getActivity()).EditHotels();
	    	        	break;
	    	        case 1: 
	    	        	((MainActivity) getActivity()).EditHotels();
	    	        	break;
	    	        case 2:
	    	        	((MainActivity) getActivity()).EditHotels();
	    	        	break;
	    	       }
	    	  }
	    	});
	    
	    return view;  
	}	
}