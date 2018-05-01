package com.hotelreservation.hotelreseration.NavigationDrawer;

import com.hotelreservation.hotelreseration.R;
import com.hotelreservation.hotelreseration.SelectUserActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_info extends Fragment
{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        
        final TextView OwnerGuide = (TextView)rootView.findViewById(R.id.guide);
        OwnerGuide.setText(SelectUserActivity.flagOwner ? "Owner Guide" : "Traveler Guide");
        OwnerGuide.setOnClickListener(new View.OnClickListener()
        {
    	    public void onClick(View v)
            {
                ((MainActivity) getActivity()).OwnerGuide();            
            }  
        });

        final TextView AboutUs = (TextView)rootView.findViewById(R.id.changeinfo);
        AboutUs.setOnClickListener(new View.OnClickListener()
        {
    	    public void onClick(View v)
            {
                ((MainActivity) getActivity()).AboutUs();            
            }  
        });

        final TextView ContactUs = (TextView)rootView.findViewById(R.id.contactus);
        ContactUs.setOnClickListener(new View.OnClickListener()
        {
    	    public void onClick(View v)
            {
                ((MainActivity) getActivity()).ContactUs();            
            }  
        });

        final TextView ReportProblem = (TextView)rootView.findViewById(R.id.reportaproblem);
        ReportProblem.setOnClickListener(new View.OnClickListener()
        {
    	    public void onClick(View v)
            {
                ((MainActivity) getActivity()).ReportProblem();            
            }  
        });

        return rootView;
    }   
}