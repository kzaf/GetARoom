package com.hotelreservation.hotelreseration.NavigationDrawer;

import com.hotelreservation.hotelreseration.R;
import com.hotelreservation.hotelreseration.SelectUserActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_settings extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        
        final TextView EditHotels = (TextView)rootView.findViewById(R.id.edithotels);
        final ImageView HotelImage=(ImageView)rootView.findViewById(R.id.hotelimage);
        View View = rootView.findViewById(R.id.View03);
        if (SelectUserActivity.flagOwner)
        {
        	EditHotels.setOnClickListener(new View.OnClickListener()
            {
        	    public void onClick(View v)
                {
        	    	if(Fragment_hotels.listView.getCount()==0)
        	    	{
        	    		Toast.makeText(getActivity(),"No hotels added yet!",Toast.LENGTH_SHORT).show();
        	    	}
        	    	else
                    {
        	    		((MainActivity) getActivity()).EditHotels();
        	    	}
                }
            });
        }
        else
        {
        	EditHotels.setVisibility(rootView.INVISIBLE);
        	HotelImage.setVisibility(rootView.INVISIBLE);
        	View.setVisibility(rootView.INVISIBLE);
        }

        final TextView DeleteAcc = (TextView)rootView.findViewById(R.id.deleteaccount);
        DeleteAcc.setOnClickListener(new View.OnClickListener()
        {
    	    public void onClick(View v)
            {
                ((MainActivity) getActivity()).DeleteAccount();            
            }  
        });
        
        final TextView ChangeInfo = (TextView)rootView.findViewById(R.id.changeinfo);
        ChangeInfo.setOnClickListener(new View.OnClickListener()
        {
    	    public void onClick(View v)
            {
                ((MainActivity) getActivity()).ChangeInfo();            
            }  
        });
        
        return rootView;
    }
}