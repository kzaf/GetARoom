package com.example.hotelreseration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreenActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private CharSequence test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container,PlaceholderFragment.newInstance(position + 1)).commit();
	}
	//final TextView User = (TextView)findViewById(R.id.Register);
	//String a=User.getText().toString();
	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			if (SelectUserActivity.flagOwner){
				mTitle = "Owner";
			}else{
				mTitle= "Traveler";
			}
			
			test= mTitle;
			break;
		case 2:
			mTitle = getString(R.string.Info);
			test= getString(R.string.Info);
			break;
		case 3:
			mTitle = getString(R.string.Settings);
			test= getString(R.string.Settings);
			break;
		case 4:
			if (SelectUserActivity.flagOwner){
				mTitle =  getString(R.string.ManageReservations);
			}else{
				mTitle=  getString(R.string.Reservations);
			}
			
			test= mTitle;
			break;
		case 5:
			if (SelectUserActivity.flagOwner){
				mTitle =  getString(R.string.ManageHotels);
			}else{
				mTitle=  "";
			}
			break;
		}
	}
	
	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
		//krataw ta stoixeia pou epilegw apo to drawer kai ta vazw se ena TextField Test1
		TextView Test1= (TextView)findViewById(R.id.section_label);
		Test1.setText("Exeis epileksei " +" "+test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main_screen, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.Logout) {
			//Vazw ena alert menu gia erwthsh prin apo to logout
			new AlertDialog.Builder(this)
		    .setTitle("Logout")
		    .setMessage("Are you sure you want to Logout?")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	//vazw sthn default ayth methodo pou xeirizetai ta items tou action bar 
					//to parakatw gia na kanei logout ton xrhsth kai na epistrefei sthn othonh epiloghs rolou
					startActivity(new Intent(MainScreenActivity.this, SelectUserActivity.class));
					//petaw kai ena toast
					Toast.makeText(getApplicationContext(), "You Have Successfully Logged Out",Toast.LENGTH_LONG).show();
		        }
			})
		    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		     .show();		
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_screen,container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainScreenActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
