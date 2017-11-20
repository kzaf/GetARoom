package com.hotelreservation.hotelreseration.NavigationDrawer;

import com.hotelreservation.hotelreseration.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hotelreservation.hotelreseration.NavigationDrawer.MainActivity.dboFKey;

public class Fragment_Favorite extends Fragment {

    static ListView listView;
    static SimpleAdapter adapter;
    static ArrayList<HashMap<String,String>> records = new ArrayList<>(); //Dhmiourgw HashMap gia na mporw na valw polla String na fainontai se kathe stoixeio ths listas

    static TextView nofavoritesLabel;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_traveler_favorite, container, false);

        listView = (ListView) view.findViewById(R.id.favoriteList);
        nofavoritesLabel=(TextView) view.findViewById(R.id.nofavoriteslabel);
        if(listView.getCount()==0){
            nofavoritesLabel.setVisibility(View.VISIBLE); //show/hide the "no favorites yet" label
        }else{
            nofavoritesLabel.setVisibility(View.GONE);
        }

        adapter = new SimpleAdapter(getActivity(), records, R.layout.mytextview,
                new String[] {"hotelname","hotelcity"}, new int[] {R.id.tv,R.id.sub});
        // Assign adapter to ListView
        listView.setAdapter(adapter);

        ((MainActivity) getActivity()).loadFavorites(dboFKey);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                HashMap<String, String> hashMap = (HashMap<String, String>) listView.getItemAtPosition(position);
                String hotelname = hashMap.get("hotelname");
                Toast.makeText(getActivity(), "Hotel " + hotelname, Toast.LENGTH_LONG).show();

            }
        });

        return view;
    }
}