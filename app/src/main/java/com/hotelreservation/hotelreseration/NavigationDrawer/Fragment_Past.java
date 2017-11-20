package com.hotelreservation.hotelreseration.NavigationDrawer;

import com.hotelreservation.hotelreseration.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hotelreservation.hotelreseration.NavigationDrawer.MainActivity.dboFKey;

public class Fragment_Past extends Fragment {
    static ArrayList<HashMap<String,String>> records = new ArrayList<>();
    static ListView PastlistView;
    static SimpleAdapter adapter;
    static TextView txt;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_owner_past, container,false);

        PastlistView = (ListView) view.findViewById(R.id.pastResList);
        txt=(TextView) view.findViewById(R.id.nopasttxt);

        adapter = new SimpleAdapter(getActivity(), records, R.layout.mytextview, new String[] {"HotelandTravelertitle","dates"},
                new int[] {R.id.tv,R.id.sub});

        PastlistView.setAdapter(adapter);// Assign adapter to ListView

        ((MainActivity) getActivity()).loadOwnerReservations(dboFKey);
        Fragment_Past.adapter.notifyDataSetChanged();

        if(PastlistView.getCount()==0){
            txt.setVisibility(View.VISIBLE);
        }else{
            txt.setVisibility(View.GONE);
        }

        return view;
    }
}