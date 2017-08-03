package com.example.hotelreseration.NavigationDrawer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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


//    public boolean flag=false;
    public boolean checkflag = false;

	@Override
    public View onCreateView(
            LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_traveler_search, container, false);

        listView = (ListView) rootView.findViewById(R.id.searchList);
        adapter = new SimpleAdapter(getActivity(), records, R.layout.traveler_hotel_search_list,
                new String[] {"name","city"}, new int[] {R.id.tv,R.id.sub});
        // Assign adapter to ListView
        listView.setAdapter(adapter);
//
//        if (!flag){
//       		final TextView Ascending = (TextView) rootView.findViewById(R.id.ascending);
//       		final TextView Descending = (TextView) rootView.findViewById(R.id.descending);
//
//            Ascending.setVisibility(View.GONE);
//            Descending.setVisibility(View.GONE);
//        }

        final Button Search = (Button)rootView.findViewById(R.id.search);
        Search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //the next two rows of code hides the keyboard on button click
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

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
                        final TextView Ascending = (TextView) rootView.findViewById(R.id.ascending);
                        final TextView Descending = (TextView) rootView.findViewById(R.id.descending);

                        Ascending.setVisibility(View.VISIBLE);
                        Descending.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        final Button CheckIn = (Button)rootView.findViewById(R.id.checkin);
        CheckIn.setOnClickListener(new View.OnClickListener() {
    	    public void onClick(View v) {
                checkflag = true;
                ((MainActivity) getActivity()).SetDate(checkflag);
//                flag=true;
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
//                    flag=true;
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                final Button CheckIn = (Button)rootView.findViewById(R.id.checkin);
                String checkin = CheckIn.getText().toString();
                final Button CheckOut = (Button)rootView.findViewById(R.id.checkout);
                String checkout = CheckOut.getText().toString();

                HashMap<String, String> hashMap = (HashMap<String, String>) listView.getItemAtPosition(position);
                String hotelName = hashMap.get("name");
                String hotelCity = hashMap.get("name");
                Toast.makeText(getActivity(), "Select option for hotel " + hotelName, Toast.LENGTH_SHORT).show();

                //((MainActivity) getActivity()).hotelcoords(hotelName); //load the coordinates for the selected hotel
                ((MainActivity) getActivity()).hotelBooking(hotelName, hotelCity, checkin, checkout);

            }
        });
        return rootView;
    }


}