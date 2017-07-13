package com.example.hotelreseration.NavigationDrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelreseration.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Fragment_Search extends Fragment {

    static ListView listView;
    static SimpleAdapter adapter;
    static ArrayList<HashMap<String,String>> records = new ArrayList<>(); //Dhmiourgw HashMap gia na mporw na valw polla String na fainontai se kathe stoixeio ths listas


    public boolean flag=false;
    public boolean checkflag = false;

	@Override
    public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_traveler_search, container, false);

        listView = (ListView) rootView.findViewById(R.id.list1);
        adapter = new SimpleAdapter(getActivity(), records, R.layout.mytextview,
                new String[] {"name","city"}, new int[] {R.id.tv,R.id.sub});
        // Assign adapter to ListView
        listView.setAdapter(adapter);
        
        if (!flag){
       		final TextView Ascending = (TextView) rootView.findViewById(R.id.ascending);
       		final TextView Descending = (TextView) rootView.findViewById(R.id.descending);
       		
            Ascending.setVisibility(rootView.GONE);
            Descending.setVisibility(rootView.GONE);
        }

        final Button Search = (Button)rootView.findViewById(R.id.search);
        Search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final EditText citysearch = (EditText) rootView.findViewById(R.id.citysearch);
                String city = citysearch.getText().toString();

                final Button CheckIn = (Button)rootView.findViewById(R.id.checkin);
                String checkin = CheckIn.getText().toString();
                final Button CheckOut = (Button)rootView.findViewById(R.id.checkout);
                String checkout = CheckOut.getText().toString();

                if(checkout.equals("Check Out")){
                    Toast.makeText(getActivity(), "Please select checkin and check out dates", Toast.LENGTH_SHORT).show();
                }else{
                    if(city.equals("")){
                        Toast.makeText(getActivity(), "Please fill in the destination city", Toast.LENGTH_SHORT).show();
                    }else{
                        ((MainActivity) getActivity()).travelerLoadHotels(city, checkin, checkout);
                    }
                }
            }
        });

        final Button CheckIn = (Button)rootView.findViewById(R.id.checkin);
        CheckIn.setOnClickListener(new View.OnClickListener() {
    	    public void onClick(View v) {
                checkflag = true;
                ((MainActivity) getActivity()).SetDate(checkflag);
                flag=true;
            }  
    	    });

        final Button CheckOut = (Button)rootView.findViewById(R.id.checkout);
        CheckOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!checkflag){
                    Toast.makeText(getActivity(), "Please Check in first", Toast.LENGTH_SHORT).show();
                }else{
                    checkflag = false;
                    ((MainActivity) getActivity()).SetDate(checkflag);
                    flag=true;
                }

            }
        });
        return rootView;
    }
}