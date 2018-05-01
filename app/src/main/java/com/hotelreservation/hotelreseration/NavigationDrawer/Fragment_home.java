package com.hotelreservation.hotelreseration.NavigationDrawer;

import java.text.DateFormat;
import java.util.Date;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hotelreservation.hotelreseration.LoginActivity;
import com.hotelreservation.hotelreseration.R;

public class Fragment_home extends Fragment
{
	TextView date;
	TextView UserWelcomeLabel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        UserWelcomeLabel=(TextView) rootView.findViewById(R.id.user_label);
        UserWelcomeLabel.setText("Welcome, "+MainActivity.dbname);
        if(LoginActivity.flagkzaf) UserWelcomeLabel.setText("Welcome, Kzaf");

        date=(TextView) rootView.findViewById(R.id.DateTextView);
        date.setText(DateFormat.getDateInstance().format(new Date()));

        return rootView;
    }
}