package com.example.hotelreseration.NavigationDrawer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hotelreseration.DataBase.AppConfig;
import com.example.hotelreseration.DataBase.AppController;
import com.example.hotelreseration.DataBase.SQLiteHandler;
import com.example.hotelreseration.LoginActivity;
import com.example.hotelreseration.R;
import com.example.hotelreseration.SelectUserActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static android.view.View.GONE;
import static com.example.hotelreseration.DataBase.AppConfig.URL_DELETE_HOTEL;
import static com.example.hotelreseration.DataBase.AppConfig.URL_LOAD_BOOKING_COORDS;
import static com.example.hotelreseration.DataBase.AppConfig.URL_LOAD_COORDINATES;
import static com.example.hotelreseration.DataBase.AppConfig.URL_LOAD_FAVORITES;
import static com.example.hotelreseration.DataBase.AppConfig.URL_LOAD_HOTELS;
import static com.example.hotelreseration.DataBase.AppConfig.URL_LOAD_OWNER_RESERVATIONS;
import static com.example.hotelreseration.DataBase.AppConfig.URL_LOAD_RESULT_HOTELS;
import static com.example.hotelreseration.DataBase.AppConfig.URL_LOAD_TRAVELER;
import static com.example.hotelreseration.DataBase.AppConfig.URL_LOAD_TRAVELER_MYBOOKINGS;
import static com.example.hotelreseration.DataBase.AppConfig.URL_MAKE_BOOKING;
import static com.example.hotelreseration.DataBase.AppConfig.URL_REGISTER_COORDINATES;
import static com.example.hotelreseration.DataBase.AppConfig.URL_REGISTER_FAVORITES;
import static com.example.hotelreseration.DataBase.AppConfig.URL_REGISTER_HOTEL;
import static com.example.hotelreseration.DataBase.AppConfig.URL_UPDATE_HOTEL;
import static com.example.hotelreseration.NavigationDrawer.Fragment_hotels.listView;

public class MainActivity extends ActionBarActivity {

    // Fields -----------------------------------------------------------------

    android.support.v4.app.FragmentTransaction fragTran;
    final Context context=this;
    public static String onomaxarth="none"; //orizw mia metavlhth na krataei to onoma tou ksenodoxeiou gia na kanei pin sto xarth
    public int backButtonCount=0;

    public int day;
    public int month;
    public int year;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private MenuListAdapter menuAdapter;

    private int[] iconsOwner,iconsTraveler;

    private Fragment_home home;
    private Fragment_user user;
    private Fragment_info info;
    private Fragment_settings settings;
    private Fragment_reservations reservations;
    private Fragment_maps maps;
    private Fragment_hotels hotels;

    private SQLiteHandler db;

    //for the user
    public static String dbname, dbsurname, dbcountry,
            dbmail, dbtelephone, dboFKey;

    private ProgressDialog pDialog;
    private static final String TAG = MainActivity.class.getSimpleName();

    //for the hotel
    EditText hotelname, hotelcity, hoteladdress, hotelpostalcode, hoteltelephone, hotelwebsite;
    Spinner hotelstars;
    CheckBox hotelswimmingpool;

    public String gethotelname, gethotelcity, gethoteladdress, gethotelpostalcode,
            gethoteltelephone, gethotelwebsite, gethotelstars;
    public boolean gethotelswimmingpool;
    public String sp = "false";
    public boolean flagaddnew = false;
    public static boolean flagpinhotel = false;
    public String URL_DELETE, URL_UPDATE;
    public String hoteln;

    public String travelerName;

    public static String hid, hn, hc, ha,
            htk, ht, hs, hoidFK, hw, hsp;

    public double booking_lat;
    public double booking_lon;

    public String drawerSelection = "Home";

    private final String[] titlesowner = new String[]{
            "Home",
            "Profile",
            "Manage Reservations",
            "Manage Hotels",
            "Info",
            "Settings",
            "Logout"
    };
    private final String[] titlestraveler = new String[]{
            "Home",
            "Profile",
            "Manage Booking",
            "Map",
            "Info",
            "Settings",
            "Logout"
    };

    // Lifecycle Callbacks ----------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Base implemenation
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Instantiate the fragments
        home = new Fragment_home();
        user = new Fragment_user();
        info = new Fragment_info();
        settings = new Fragment_settings();
        reservations = new Fragment_reservations();
        maps = new Fragment_maps();
        hotels=new Fragment_hotels();

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        HashMap<String, String> user = db.getUserDetails();
        dbname = user.get("name");
        dbsurname = user.get("surname");
        dbcountry = user.get("country");
        dbmail = user.get("email");
        dbtelephone = user.get("telephone");
        dboFKey = user.get("id");

        // Get the icons from the drawables folder
        if (SelectUserActivity.flagOwner){
            iconsOwner = new int[]{
                    R.drawable.action_home,
                    R.drawable.action_user,
                    R.drawable.action_reservations,
                    R.drawable.action_hotels,
                    R.drawable.action_about,
                    R.drawable.action_settings,
                    R.drawable.action_logout
            };

        }else{
            iconsTraveler = new int[]{
                    R.drawable.action_home,
                    R.drawable.action_user,
                    R.drawable.action_reservations,
                    R.drawable.action_maps,
                    R.drawable.action_about,
                    R.drawable.action_settings,
                    R.drawable.action_logout
            };
        }

        // Get the drawer layout from the XML file and the ListView inside it
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.listview_drawer);

        // Set a custom shadow over that overlays the main content
        // when the drawer opens
        drawerLayout.setDrawerShadow(
                R.drawable.drawer_shadow, GravityCompat.START);

        // Pass the string arrays to the MenuListAdapter, set the drawer
        // list adapter to it and set up its click listener
        if (SelectUserActivity.flagOwner){
            menuAdapter = new MenuListAdapter(MainActivity.this, titlesowner, iconsOwner);
            drawerList.setAdapter(menuAdapter);
            drawerList.setOnItemClickListener(new DrawerItemClickListener());
        }else{
            menuAdapter = new MenuListAdapter(MainActivity.this, titlestraveler, iconsTraveler);
            drawerList.setAdapter(menuAdapter);
            drawerList.setOnItemClickListener(new DrawerItemClickListener());
        }
        //Set the name of the ActionBar on open
        getSupportActionBar().setTitle(drawerSelection);// Set the drawer selection name on the ActionBar

        // Enable the action bar to have up navigation
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow the the action bar to toggle the drawer
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){

            public void onDrawerClosed(View view){
                getSupportActionBar().setTitle(drawerSelection);// Set the drawer selection name on the ActionBar
                super.onDrawerClosed(view);
            }
            @SuppressWarnings("deprecation")
            public void onDrawerOpened(View view){
                backButtonCount=0;
                getSupportActionBar().setTitle(drawerSelection);// Set the drawer selection name on the ActionBar

                super.onDrawerOpened(view);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        // If this is the first time opening this activity,
        // start with loading fragment #1
        if (savedInstanceState == null){
            selectItem(0);
        }
    }

    // Methods ----------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                // If the drawer is open, close it; vice versa
                if (drawerLayout.isDrawerOpen(drawerList)){
                    drawerLayout.closeDrawer(drawerList);
                } else {
                    drawerLayout.openDrawer(drawerList);
                }
                return true;
            case R.id.Logout:
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Logout");
                alertDialog.setMessage("Are you sure you want to Logout?");
                alertDialog.setIcon(R.drawable.logout);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        startActivity(new Intent(MainActivity.this, SelectUserActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "You Have Successfully Logged Out", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }


        // Finish by letting the super class do the rest
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){

        // Call the super implementation and synchronize the drawer
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){

        // Call the super implementation on this activity
        // and the drawer toggle object
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);

    }

    @SuppressWarnings("deprecation")
    private void selectItem(int position){

        // Create a new fragment transaction and start it
        fragTran = getSupportFragmentManager().beginTransaction();

        // Locate the position selected replace the content view
        // with the fragment of the number selected
        switch (position){
            case 0:{
                fragTran.replace(R.id.content_frame, home);
                drawerSelection = "Home";
                break;
            }
            case 1:{
                fragTran.replace(R.id.content_frame, user);
                drawerSelection = "Profile";
                break;
            }
            case 2:{
                fragTran.replace(R.id.content_frame, reservations);
                drawerSelection = "Reservations";
                Fragment_Search.records.clear(); //clear the list with the hotels in traveler Manage Booking
                break;
            }
            case 3:{
                if (!SelectUserActivity.flagOwner){
                    fragTran.replace(R.id.content_frame, maps);
                    drawerSelection = "Map";
                }else{
                    fragTran.replace(R.id.content_frame, hotels);
                    drawerSelection = "Hotels";

                    loadHotels(dboFKey);

                }
                break;
            }
            case 4:{
                fragTran.replace(R.id.content_frame, info);
                drawerSelection = "Info";
                break;
            }
            case 5:{
                fragTran.replace(R.id.content_frame, settings);
                drawerSelection = "Settings";
                break;
            }
            case 6:{
                //Petaw alert dialog gia to logout tou xrhsth
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Logout");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to Logout?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.logout);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        db.deleteUsers();
                        //to parakatw gia na kanei logout ton xrhsth kai na epistrefei sthn othonh epiloghs rolou
                        startActivity(new Intent(MainActivity.this, SelectUserActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "You Have Successfully Logged Out", Toast.LENGTH_LONG).show();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
                break;
            }
        }

        // Commit the transaction and close the drawer
        fragTran.commit();
        drawerList.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerList);

    }

    public void setTitle(CharSequence title){
        getSupportActionBar().setTitle(title);
    }

    @SuppressWarnings("deprecation")
    public void alerthotel()
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Add new hotel");
        alertDialog.setIcon(R.drawable.addhotel);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.fragment_add_hotel, null);
        alertDialog.setView(promptView);

        hotelname= (EditText)promptView.findViewById(R.id.hotelname);
        hotelcity= (EditText)promptView.findViewById(R.id.hotelcity);
        hoteladdress= (EditText)promptView.findViewById(R.id.hoteladdress);
        hotelpostalcode= (EditText)promptView.findViewById(R.id.hotelpostalcode);
        hoteltelephone= (EditText)promptView.findViewById(R.id.hoteltelephone);
        hotelwebsite= (EditText)promptView.findViewById(R.id.hotelwebsite);
        hotelstars = (Spinner)promptView.findViewById(R.id.hotelstars);
        hotelswimmingpool = (CheckBox)promptView.findViewById(R.id.hotelswimmingpool);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                if (hotelname.getText().toString().equals("")||
                        hotelcity.getText().toString().equals("")||
                        hoteladdress.getText().toString().equals("")||
                        hotelpostalcode.getText().toString().equals("")||
                        hoteltelephone.getText().toString().equals("")||
                        hotelwebsite.getText().toString().equals("")){
                    //Toast is the pop up message
                    Toast.makeText(getApplicationContext(), "Please fill in all of the fields",
                            Toast.LENGTH_LONG).show();

                }
                else{
                    gethotelname = hotelname.getText().toString().trim();
                    gethotelcity = hotelcity.getText().toString().trim();
                    gethoteladdress = hoteladdress.getText().toString().trim();
                    gethotelpostalcode= hotelpostalcode.getText().toString().trim();
                    gethoteltelephone= hoteltelephone.getText().toString().trim();
                    gethotelwebsite= hotelwebsite.getText().toString().trim();
                    gethotelstars= hotelstars.getSelectedItem().toString();
                    gethotelswimmingpool = hotelswimmingpool.isChecked();
                    if(gethotelswimmingpool){
                        sp = "true";
                    }
                    registerHotel(gethotelname, gethotelcity, gethoteladdress, gethotelpostalcode,
                            gethoteltelephone, gethotelstars, dboFKey, gethotelwebsite, sp);
                    flagaddnew = true;
                }
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    @SuppressWarnings("deprecation")
    public void OwnerGuide(){
        if (SelectUserActivity.flagOwner){
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Owner Guide");
            alertDialog.setIcon(R.drawable.guide);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptView = layoutInflater.inflate(R.layout.ownerguide, null);
            alertDialog.setView(promptView);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    Toast.makeText(getApplicationContext(), "For more info contact us via mail",
                            Toast.LENGTH_SHORT).show();
                }

            });
            // Showing Alert Message
            alertDialog.show();
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Traveler Guide");
            alertDialog.setIcon(R.drawable.guide);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptView = layoutInflater.inflate(R.layout.travelerguide, null);
            alertDialog.setView(promptView);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    Toast.makeText(getApplicationContext(), "For more info contact us via mail",
                            Toast.LENGTH_SHORT).show();
                }

            });
            // Showing Alert Message
            alertDialog.show();
        }

    }

    @SuppressWarnings("deprecation")
    public void AboutUs(){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("About Us");
        alertDialog.setIcon(R.drawable.aboutus);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        alertDialog.setView(promptView);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
            }

        });
        // Showing Alert Message
        alertDialog.show();
    }

    @SuppressWarnings("deprecation")
    public void ContactUs(){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Contact Us");
        alertDialog.setIcon(R.drawable.contactus);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.contactus, null);
        alertDialog.setView(promptView);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
            }

        });
        // Showing Alert Message
        alertDialog.show();
    }

    @SuppressWarnings("deprecation")
    public void ReportProblem(){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Report A Problem");
        alertDialog.setIcon(R.drawable.reporthotel);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.reportproblem, null);
        alertDialog.setView(promptView);
        final EditText reportproblem= (EditText)promptView.findViewById(R.id.suite1);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                //Edw orizw ta mail sta opoia tha steilei email o xrhsths se periptwsh pou ksexasei to password tou
                String subject="Report a problem";
                String message=reportproblem.getText().toString();
                {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","kzaf@it.teithe.gr,ziskatas@it.teithe.gr", null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                }
            }

        });
        // Showing Alert Message
        alertDialog.show();
    }

    @SuppressWarnings("deprecation")
    public void DeleteAccount(){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Delete Account");
        alertDialog.setIcon(R.drawable.deletehotel);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.deleteaccount, null);
        alertDialog.setView(promptView);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                if (SelectUserActivity.flagOwner)
                {
                    URL_DELETE = AppConfig.URL_DELETE_OWNER;
                }else{
                    URL_DELETE = AppConfig.URL_DELETE_TRAVELER;
                }

                db.deleteUsers();
                deleteUser(dbmail);
                startActivity(new Intent(MainActivity.this, SelectUserActivity.class));

            }

        });
        // Showing Alert Message
        alertDialog.show();
    }

    public static int getIndexOFValue(String value, ArrayList<HashMap<String, String>> listMap) { //Get the index out of a HashMap ArrayList, by key

        int i = 0;
        for (Map<String, String> map : listMap) {
            if (map.containsValue(value)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public void hotelBooking(final String hotelname, final String hotelCity,
                             final String checkin, final String checkout){
        hotelcoords(hotelname);
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Hotel Info");
        alertDialog.setIcon(R.drawable.action_hotels);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.traveler_hotel_registration, null);
        alertDialog.setView(promptView);

//        ListView FavoritelistView = (ListView) promptView.findViewById(R.id.favoriteList);
//        final SimpleAdapter Favoriteadapter;
//        final ArrayList<HashMap<String,String>> Favoriterecords = new ArrayList<>();

        final int i = getIndexOFValue(hotelname, Fragment_Search.records); //Get the index of the ArryList with HashMap with the hotels

        final TextView website= (TextView) promptView.findViewById(R.id.tvwebsite);
        final TextView address= (TextView) promptView.findViewById(R.id.tvaddress);
        final TextView telephone= (TextView) promptView.findViewById(R.id.tvtelephone);
        final RatingBar rb = (RatingBar) promptView.findViewById(R.id.ratingBar);
        final CheckBox spool = (CheckBox)promptView.findViewById(R.id.swimmingpoolCheckBox);
        website.setText(Fragment_Search.records.get(i).get("website"));
        address.setText(Fragment_Search.records.get(i).get("address"));
        telephone.setText(Fragment_Search.records.get(i).get("telephone"));
        final String starsNo = Fragment_Search.records.get(i).get("stars");
        rb.setRating(Integer.parseInt(starsNo));
        boolean a = Boolean.parseBoolean(Fragment_Search.records.get(i).get("swimmingpool"));
        spool.setChecked(a);

        MapView mMapView = (MapView) promptView.findViewById(R.id.mapView2);
        MapsInitializer.initialize(this);

        mMapView.onCreate(alertDialog.onSaveInstanceState());
        mMapView.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                LatLng posisiabsen = new LatLng(booking_lat, booking_lon); //// lat lng
                googleMap.addMarker(new MarkerOptions().position(posisiabsen).title(hotelname));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        });

        final TextView hoteltitle= (TextView)promptView.findViewById(R.id.HotelInfoTitle);
        hoteltitle.setText("Hotel " + hotelname);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Favorite", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                registerFavorites(hotelname, hotelCity, dboFKey);
            }

        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Make booking", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                bookingAlert(hotelname, checkin, checkout);
            }

        });
        alertDialog.show();
    }

    public void bookingAlert(final String hotelname, final String checkin,
                             final String checkout){

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Make Booking");
        alertDialog.setIcon(R.drawable.action_hotels);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.fragment_make_booking, null);
        alertDialog.setView(promptView);

        final TextView checkIn= (TextView) promptView.findViewById(R.id.tvCheckin);
        final TextView checkOut= (TextView) promptView.findViewById(R.id.tvCheckOut);
        final TextView bookingtitle= (TextView) promptView.findViewById(R.id.bookingtitle);

        final Spinner adultsSP= (Spinner) promptView.findViewById(R.id.spinnerAdults);
        final Spinner childrenSP= (Spinner) promptView.findViewById(R.id.spinnerChildren);

        final RadioGroup roomtype = (RadioGroup) promptView.findViewById(R.id.radioGroupRoomType);
        final RadioGroup bedsNo = (RadioGroup) promptView.findViewById(R.id.radioGroupBedsNo);

        checkIn.setText(checkin);
        checkOut.setText(checkout);
        bookingtitle.setText("Your booking at "+hotelname);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Make booking", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                final int selectedRoomType =roomtype.getCheckedRadioButtonId();
                String roomType = "Regular";
                switch(selectedRoomType){
                    case R.id.radioRegular:
                        roomType = "Regular";
                        break;
                    case R.id.radioLuxury:
                        roomType = "Luxury";
                        break;
                    case R.id.radioSuite:
                        roomType = "Suite";
                        break;
                }

                final int selectedBedType = bedsNo.getCheckedRadioButtonId();
                String bedType = "Single";
                switch(selectedBedType){
                    case R.id.radioOne:
                        bedType = "Single";
                        break;
                    case R.id.radioTwo:
                        bedType = "Double";
                        break;
                    case R.id.radioThree:
                        bedType = "Triple";
                        break;
                }

                final String adults = adultsSP.getSelectedItem().toString();
                final String children = childrenSP.getSelectedItem().toString();
                final String finalRoomType = roomType;
                final String finalBedType = bedType;

                makeBooking(dboFKey, hotelname, checkin, checkout, adults, children, finalBedType, finalRoomType);

                Toast.makeText(getApplicationContext(), "You make your booking at " + hotelname, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Check in:" + checkin +", Check out:" + checkout, Toast.LENGTH_LONG).show();

            }
        });
        alertDialog.show();

    }

    public void EditHotels(){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Edit Hotels");
        alertDialog.setIcon(R.drawable.reporthotel);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.edithotels, null);
        alertDialog.setView(promptView);

        final CheckBox hotel1= (CheckBox)promptView.findViewById(R.id.checkHotel1);
        final CheckBox hotel2= (CheckBox)promptView.findViewById(R.id.checkHotel2);
        final CheckBox hotel3= (CheckBox)promptView.findViewById(R.id.checkHotel3);
        final TextView edithoteltitle= (TextView)promptView.findViewById(R.id.edithoteltitle);

        if (listView==null){
            edithoteltitle.setText("No hotels added yet! Please add one!");
            hotel1.setVisibility(GONE);
            hotel2.setVisibility(GONE);
            hotel3.setVisibility(GONE);
        }else if (listView.getCount()==1){
            hotel1.setEnabled(true);
            hotel1.setChecked(true);
            hotels.adapter.notifyDataSetChanged();
            HashMap<String, String> hashMap = (HashMap<String, String>) listView.getItemAtPosition(0);
            String aLongValue = hashMap.get("name");
            hotel1.setText(aLongValue);
            hotel2.setVisibility(GONE);
            hotel3.setVisibility(GONE);
        }else if (listView.getCount()==2){
            hotel1.setEnabled(true);
            hotels.adapter.notifyDataSetChanged();
            HashMap<String, String> hashMap = (HashMap<String, String>) listView.getItemAtPosition(0);
            String aLongValue = hashMap.get("name");
            hotel1.setText(aLongValue);
            hotel2.setEnabled(true);
            hotels.adapter.notifyDataSetChanged();
            HashMap<String, String> hashMap1 = (HashMap<String, String>) listView.getItemAtPosition(1);
            String aLongValue1 = hashMap1.get("name");
            hotel2.setText(aLongValue1);
            hotel3.setVisibility(GONE);
        }else{
            hotel1.setEnabled(true);
            hotels.adapter.notifyDataSetChanged();
            HashMap<String, String> hashMap = (HashMap<String, String>) listView.getItemAtPosition(0);
            String aLongValue = hashMap.get("name");
            hotel1.setText(aLongValue);
            hotel2.setEnabled(true);
            hotels.adapter.notifyDataSetChanged();
            HashMap<String, String> hashMap1 = (HashMap<String, String>) listView.getItemAtPosition(1);
            String aLongValue1 = hashMap1.get("name");
            hotel2.setText(aLongValue1);
            hotel3.setEnabled(true);
            hotels.adapter.notifyDataSetChanged();
            HashMap<String, String> hashMap2 = (HashMap<String, String>) listView.getItemAtPosition(2);
            String aLongValue2 = hashMap2.get("name");
            hotel3.setText(aLongValue2);

        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Pin on map", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(getApplicationContext(), "Long press to pin!",
                        Toast.LENGTH_LONG).show();
                fragTran = getSupportFragmentManager().beginTransaction();
                fragTran.replace(R.id.content_frame, maps);
                fragTran.commit();
                MainActivity.flagpinhotel = true;
                if(hotel1.isChecked()){
                    onomaxarth=(String) hotel1.getText();
                }else if (hotel2.isChecked()){
                    onomaxarth=(String) hotel2.getText();
                }else{
                    onomaxarth=(String) hotel3.getText();
                }
            }

        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Change info", new DialogInterface.OnClickListener()
        {
            @SuppressWarnings("deprecation")
            public void onClick(DialogInterface dialog, int which)
            {
                if (!hotel1.isChecked() && !hotel2.isChecked() && !hotel3.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please select a hotel!",Toast.LENGTH_LONG).show();
                }else if (hotel1.isChecked() && hotel2.isChecked() && hotel3.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please select ONLY ONE hotel!",Toast.LENGTH_LONG).show();
                }else if (hotel1.isChecked() && hotel2.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please select ONLY ONE hotel!",Toast.LENGTH_LONG).show();
                }else if (hotel1.isChecked() && hotel3.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please select ONLY ONE hotel!",Toast.LENGTH_LONG).show();
                }else if (hotel3.isChecked() && hotel2.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please select ONLY ONE hotel!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "You can change your hotel info",Toast.LENGTH_SHORT).show();

                    final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Edit hotel");
                    alertDialog.setIcon(R.drawable.addhotel);
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    View promptView = layoutInflater.inflate(R.layout.fragment_add_hotel, null);
                    alertDialog.setView(promptView);


                    hotelname= (EditText)promptView.findViewById(R.id.hotelname);
                    hotelcity= (EditText)promptView.findViewById(R.id.hotelcity);
                    hoteladdress= (EditText)promptView.findViewById(R.id.hoteladdress);
                    hotelpostalcode= (EditText)promptView.findViewById(R.id.hotelpostalcode);
                    hoteltelephone= (EditText)promptView.findViewById(R.id.hoteltelephone);
                    hotelwebsite= (EditText)promptView.findViewById(R.id.hotelwebsite);
                    hotelstars = (Spinner)promptView.findViewById(R.id.hotelstars);
                    hotelswimmingpool = (CheckBox)promptView.findViewById(R.id.hotelswimmingpool);

                    if(hotel1.isChecked()){
                        hotelname.setText(hotels.records.get(0).get("name"));
                        hotelcity.setText(hotels.records.get(0).get("city"));
                        hoteladdress.setText(hotels.records.get(0).get("address"));
                        hotelpostalcode.setText(hotels.records.get(0).get("tk"));
                        hoteltelephone.setText(hotels.records.get(0).get("telephone"));
                        hotelwebsite.setText(hotels.records.get(0).get("website"));
                        try {
                            final int myNum = Integer.parseInt(hotels.records.get(0).get("stars"));
                            hotelstars.setSelection(myNum);
                        } catch(NumberFormatException nfe){
                            System.err.println("Exception: " + nfe.getMessage());
                        }
                        boolean a = Boolean.parseBoolean(hotels.records.get(0).get("swimmingpool"));
                        hotelswimmingpool.setChecked(a);
                    }else if(hotel2.isChecked()){
                        hotelname.setText(hotels.records.get(1).get("name"));
                        hotelcity.setText(hotels.records.get(1).get("city"));
                        hoteladdress.setText(hotels.records.get(1).get("address"));
                        hotelpostalcode.setText(hotels.records.get(1).get("tk"));
                        hoteltelephone.setText(hotels.records.get(1).get("telephone"));
                        hotelwebsite.setText(hotels.records.get(1).get("website"));
                        try {
                            final int myNum = Integer.parseInt(hotels.records.get(1).get("stars"));
                            hotelstars.setSelection(myNum);
                        } catch(NumberFormatException nfe){
                            System.err.println("Exception: " + nfe.getMessage());
                        }
                        boolean a = Boolean.parseBoolean(hotels.records.get(1).get("swimmingpool"));
                        hotelswimmingpool.setChecked(a);
                    }else{
                        hotelname.setText(hotels.records.get(2).get("name"));
                        hotelcity.setText(hotels.records.get(2).get("city"));
                        hoteladdress.setText(hotels.records.get(2).get("address"));
                        hotelpostalcode.setText(hotels.records.get(2).get("tk"));
                        hoteltelephone.setText(hotels.records.get(2).get("telephone"));
                        hotelwebsite.setText(hotels.records.get(2).get("website"));
                        try {
                            final int myNum = Integer.parseInt(hotels.records.get(2).get("stars"));
                            hotelstars.setSelection(myNum);
                        } catch(NumberFormatException nfe){
                            System.err.println("Exception: " + nfe.getMessage());
                        }
                        boolean a = Boolean.parseBoolean(hotels.records.get(2).get("swimmingpool"));
                        hotelswimmingpool.setChecked(a);
                    }
                    alertDialog.setButton("Update", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            if (hotelname.getText().toString().equals("")||
                                    hotelcity.getText().toString().equals("")||
                                    hoteladdress.getText().toString().equals("")||
                                    hotelpostalcode.getText().toString().equals("")||
                                    hoteltelephone.getText().toString().equals("")||
                                    hotelwebsite.getText().toString().equals("")) {
                                //Toast is the pop up message
                                Toast.makeText(getApplicationContext(), "Please fill in all of the fields",
                                        Toast.LENGTH_LONG).show();
                            }
                            else{
                                gethotelname = hotelname.getText().toString().trim();
                                gethotelcity = hotelcity.getText().toString().trim();
                                gethoteladdress = hoteladdress.getText().toString().trim();
                                gethotelpostalcode= hotelpostalcode.getText().toString().trim();
                                gethoteltelephone= hoteltelephone.getText().toString().trim();
                                gethotelwebsite= hotelwebsite.getText().toString().trim();
                                gethotelstars= hotelstars.getSelectedItem().toString();
                                gethotelswimmingpool = hotelswimmingpool.isChecked();
                                if(gethotelswimmingpool){
                                    sp = "true";
                                }
                                if(hotel1.isChecked()){
                                    onomaxarth=(String) hotel1.getText();
                                }else if (hotel2.isChecked()){
                                    onomaxarth=(String) hotel2.getText();
                                }else{
                                    onomaxarth=(String) hotel3.getText();
                                }
                                //onomaxarth=hotelname.getText().toString();//krataei to onoma gia na to kanei pin sto xarth
                                hotels.adapter.notifyDataSetChanged();//kanei update to listview me ta hotels
                                updateHotel(onomaxarth, gethotelname, gethotelcity, gethoteladdress, gethotelpostalcode,
                                        gethoteltelephone, gethotelstars, gethotelwebsite, sp);
                                db.deleteUsers();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                                Toast.makeText(getApplicationContext(), "Please sign in again for the changes to take effect", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();

                }
            }

        });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Remove hotel", new DialogInterface.OnClickListener()
        {
            @SuppressWarnings("deprecation")
            public void onClick(DialogInterface dialog, int which)
            {
                if (!hotel1.isChecked() && !hotel2.isChecked() && !hotel3.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please select a hotel!",Toast.LENGTH_LONG).show();
                }else if (hotel1.isChecked() && hotel2.isChecked() && hotel3.isChecked()){
                    Toast.makeText(getApplicationContext(), "You can remove ONE hotel per time!",Toast.LENGTH_LONG).show();
                }else if (hotel1.isChecked() && hotel2.isChecked()){
                    Toast.makeText(getApplicationContext(), "You can remove ONE hotel per time!",Toast.LENGTH_LONG).show();
                }else if (hotel1.isChecked() && hotel3.isChecked()){
                    Toast.makeText(getApplicationContext(), "You can remove ONE hotel per time!",Toast.LENGTH_LONG).show();
                }else if (hotel3.isChecked() && hotel2.isChecked()){
                    Toast.makeText(getApplicationContext(), "You can remove ONE hotel per time!",Toast.LENGTH_LONG).show();
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Delete Hotel");
                    alertDialog.setIcon(R.drawable.deletehotel);
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    View promptView = layoutInflater.inflate(R.layout.deletehotel, null);
                    alertDialog.setView(promptView);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            if (hotel1.isChecked()){
                                Toast.makeText(getApplicationContext(), "Hotel "+hotel1.getText()+" removed!",Toast.LENGTH_LONG).show();
                                hoteln = hotel1.getText().toString();
                                deleteHotel(hoteln);
                                hotels.records.remove(0);
                                hotels.adapter.notifyDataSetChanged();
                                //Afou kanei delete to ksenodoxeio phgainei sto fragment me th lista twn ksenodoxeiwn
                                fragTran = getSupportFragmentManager().beginTransaction();
                                fragTran.replace(R.id.content_frame, hotels);
                                fragTran.commit();
                            }else if (hotel2.isChecked()){
                                Toast.makeText(getApplicationContext(), "Hotel "+hotel2.getText()+" removed!",Toast.LENGTH_LONG).show();
                                hoteln = hotel2.getText().toString();
                                deleteHotel(hoteln);
                                hotels.records.remove(1);
                                hotels.adapter.notifyDataSetChanged();
                                //Afou kanei delete to ksenodoxeio phgainei sto fragment me th lista twn ksenodoxeiwn
                                fragTran = getSupportFragmentManager().beginTransaction();
                                fragTran.replace(R.id.content_frame, hotels);
                                fragTran.commit();
                            }else if (hotel3.isChecked()){
                                Toast.makeText(getApplicationContext(), "Hotel "+hotel3.getText()+" removed!",Toast.LENGTH_LONG).show();
                                hoteln = hotel3.getText().toString();
                                deleteHotel(hoteln);
                                hotels.records.remove(2);
                                hotels.adapter.notifyDataSetChanged();
                                //Afou kanei delete to ksenodoxeio phgainei sto fragment me th lista twn ksenodoxeiwn
                                fragTran = getSupportFragmentManager().beginTransaction();
                                fragTran.replace(R.id.content_frame, hotels);
                                fragTran.commit();
                            }

                        }

                    });
                    // Showing Alert Message
                    alertDialog.show();
                }

            }

        });
        // Showing Alert Message
        alertDialog.show();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBackPressed(){
        if(backButtonCount >= 1)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Logout");
            alertDialog.setMessage("Are you sure you want to Logout?");
            alertDialog.setIcon(R.drawable.logout);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    //to parakatw gia na kanei logout ton xrhsth kai na epistrefei sthn othonh epiloghs rolou
                    startActivity(new Intent(MainActivity.this, SelectUserActivity.class));
                    finish();
                    Toast.makeText(getApplicationContext(), "You Have Successfully Logged Out", Toast.LENGTH_LONG).show();
                }
            });
            // Showing Alert Message
            alertDialog.show();
        }
        else
        {
            //Toast.makeText(getApplicationContext(), "Select logout to leave!",Toast.LENGTH_LONG).show();
            fragTran = getSupportFragmentManager().beginTransaction();
            fragTran.replace(R.id.content_frame, home);
            drawerLayout.closeDrawer(drawerList);
            fragTran.commit();
            Toast.makeText(this, "Press the back button once again to logout.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @SuppressWarnings("deprecation")
    public void ChangeInfo(){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("You can change your info");
        alertDialog.setIcon(R.drawable.action_home);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.changeinfo, null);
        alertDialog.setView(promptView);

        final EditText name= (EditText)promptView.findViewById(R.id.editTextName);
        name.setText(dbname);

        final EditText surname= (EditText)promptView.findViewById(R.id.editTextSurname);
        surname.setText(dbsurname);

        final EditText telephone= (EditText)promptView.findViewById(R.id.editTextTelephone);
        telephone.setText(dbtelephone);

        final EditText email= (EditText)promptView.findViewById(R.id.editTextEmail);
        email.setText(dbmail);

        final EditText pass= (EditText)promptView.findViewById(R.id.editTextPass);
        pass.setText("**********");

        final EditText country= (EditText)promptView.findViewById(R.id.editTextCountry);
        country.setText(dbcountry);

        alertDialog.setButton("Save Changes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                final String nname = name.getText().toString().trim();
                final String ssurname = surname.getText().toString().trim();
                final String eemail = email.getText().toString().trim();
                final String ppass = pass.getText().toString().trim();
                final String ttelephone = telephone.getText().toString().trim();
                final String ccountry = country.getText().toString().trim();

                if (SelectUserActivity.flagOwner)
                {
                    URL_UPDATE = AppConfig.URL_UPDATE_OWNER;
                }else{
                    URL_UPDATE = AppConfig.URL_UPDATE_TRAVELER;
                }
                updateUser(dbmail, nname, ssurname, eemail, ppass, ttelephone, ccountry);
                db.deleteUsers();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                Toast.makeText(getApplicationContext(), "Please login with the new credentials", Toast.LENGTH_LONG).show();
            }

        });
        // Showing Alert Message
        alertDialog.show();
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")

    public void SetDate(final boolean flag){

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Select Date");
        alertDialog.setIcon(R.drawable.action_reservations);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.datepicker, null);
        alertDialog.setView(promptView);
        final DatePicker datePicker = (DatePicker) promptView.findViewById(R.id.datePicker1);
        datePicker.setMinDate(System.currentTimeMillis() - 1000); //prevent selecting older dates
        final Button CheckIn = (Button) findViewById(R.id.checkin);
        final Button CheckOut = (Button) findViewById(R.id.checkout);
        final boolean[] flg = {false};

        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                day=datePicker.getDayOfMonth();
                month=datePicker.getMonth() + 1;
                year=datePicker.getYear();

                if (flag){
                    CheckIn.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
                }
                else{

                    String a = CheckIn.getText().toString();

                    String[] partsa = a.split("/");
                    String part1a = partsa[0];
                    String[] p = part1a.split(" ");
                    part1a = p[0];
                    String part2a = partsa[1];
                    String part3a = partsa[2];
                    int p1a = Integer.parseInt(part1a);
                    int p2a = Integer.parseInt(part2a);
                    int p3a = Integer.parseInt(part3a);

                    if(p3a < year){
                        CheckOut.setText(new StringBuilder().append(day).
                                append("/").append(month).append("/").append(year));
                        flg[0] = true;
                    }else if(p3a == year)
                    {
                        if(p2a < month){
                            CheckOut.setText(new StringBuilder().append(day).
                                    append("/").append(month).append("/").append(year));
                            flg[0] = true;
                        }else if(p2a == month)
                        {
                            if(p1a < day){
                                CheckOut.setText(new StringBuilder().append(day).
                                        append("/").append(month).append("/").append(year));
                                flg[0] = true;
                            }else{
                                Toast.makeText(getApplicationContext(), "CheckIn date cannot be the same or bigger than CheckOut!", Toast.LENGTH_LONG).show();
                                flg[0] = false;
                                CheckIn.setText("CHECK IN");
                                CheckOut.setText("CHECK OUT");

                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "CheckIn date cannot be the same or bigger than CheckOut!", Toast.LENGTH_LONG).show();
                            flg[0] = false;
                            CheckIn.setText("CHECK IN");
                            CheckOut.setText("CHECK OUT");
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "CheckIn date cannot be the same or bigger than CheckOut!", Toast.LENGTH_LONG).show();
                        flg[0] = false;
                        CheckIn.setText("CHECK IN");
                        CheckOut.setText("CHECK OUT");
                    }
                }

            }

        });
        // Showing Alert Message
        alertDialog.show();
    }

    //-----------------------------------------------------------DB-Functions-------------------------------------------------------//


    // Register functions
    private void registerHotel(final String hotelname, final String city, final String address,
                               final String postalcode, final String hoteltelephone, final String stars,
                               final String dboFKey, final String website, final String swimmingpool){

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_REGISTER_HOTEL, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Hotel has been added successfully!", Toast.LENGTH_SHORT).show();
                        loadHotels(dboFKey); //reload the hotel list each time the user add a new one
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("hotelname", hotelname);
                params.put("city", city);
                params.put("address", address);
                params.put("tk", String.valueOf(postalcode));
                params.put("telephone", String.valueOf(hoteltelephone));
                params.put("stars", stars);
                params.put("oidFK", dboFKey);
                params.put("website", website);
                params.put("swimmingpool", swimmingpool);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void registerFavorites(final String hotelname, final String city, final String dboFKey){

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Adding to favorites ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_REGISTER_FAVORITES, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Hotel has been added to favorites!", Toast.LENGTH_SHORT).show();
                        loadFavorites(dboFKey);
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("hotelname", hotelname);
                params.put("city", city);
                params.put("userID", dboFKey);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void registerCoordinates(final String onomaxarth, final String oidFK,
                                    final String latitude, final String longitude){

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering coordinates...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_REGISTER_COORDINATES, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Hotel has been pinned successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("hotelname", onomaxarth);
                params.put("oidFK", String.valueOf(oidFK));
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void makeBooking(final String dboFKey, final String hotelname, final String checkIn,
                            final String checkOut, final String adults, final String children,
                            final String bedType, final String roomType){
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Create reservation ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_MAKE_BOOKING, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Reservation has been created!", Toast.LENGTH_SHORT).show();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("hotelname", hotelname);
                params.put("checkin", checkIn);
                params.put("checkout", checkOut);
                params.put("tidFK", dboFKey);
                params.put("adults", adults);
                params.put("children", children);
                params.put("bedtype", bedType);
                params.put("roomtype", roomType);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    // Load Functions
    void findTravelerById(final String tID){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Loading reservations ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_LOAD_TRAVELER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        JSONArray juser= jObj.getJSONArray("traveler");
                        Log.d("traveler",juser.toString());

                        for(int i=0; i<juser.length(); i++){
//                            HashMap<String,String> userhm = new HashMap<>();
                            JSONObject user = juser.getJSONObject(i);
                            final String name = user.getString("name");
                            final String surname = user.getString("surname");
                            travelerName = name + " " + surname;
                        }
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("tid", tID);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    void loadOwnerReservations(final String dboFKey){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Loading reservations ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_LOAD_OWNER_RESERVATIONS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        Fragment_Running.records.clear(); //clear the old list and load the new one
                        Fragment_Upcoming.records.clear();
                        Fragment_Past.records.clear();

                        JSONArray jhotel= jObj.getJSONArray("hotel");
                        Log.d("hotel",jhotel.toString());

                        for(int i=0; i<jhotel.length(); i++){
                            HashMap<String,String> hotelhm = new HashMap<>();
                            JSONObject hotel = jhotel.getJSONObject(i);
                            final String resID = hotel.getString("reservationID");
                            final String hoteln = hotel.getString("hotelname");
                            final String checkn = hotel.getString("checkin");
                            final String checkt = hotel.getString("checkout");
                            final String dates = checkn + " - " + checkt;
                            final String tid = hotel.getString("tidFK");

                            findTravelerById(tid);
                            final String title = hoteln + " - " + travelerName;

                            String a = checkt;

                            String[] partsa = a.split("/");
                            String part1a = partsa[0];
                            String[] p = part1a.split(" ");
                            part1a = p[0];
                            String part2a = partsa[1];
                            String part3a = partsa[2];
                            int p1a = Integer.parseInt(part1a);
                            int p2a = Integer.parseInt(part2a);
                            int p3a = Integer.parseInt(part3a);

                            Calendar c = Calendar.getInstance();
                            final int y = c.get(Calendar.YEAR);
                            final int m = c.get(Calendar.MONTH) + 1;
                            final int d = c.get(Calendar.DATE);

                            hotelhm.put("resID",resID);
                            hotelhm.put("hotelname",hoteln);
                            hotelhm.put("checkin", checkn);
                            hotelhm.put("checkout", checkt);
                            hotelhm.put("dates", dates);
                            hotelhm.put("HotelandTravelertitle", title);
                            hotelhm.put("tid", tid);

                            if(p3a > y){
                                Fragment_Running.records.add(hotelhm);
                            }else if(p3a == y){
                                if(p2a > m){
                                    Fragment_Running.records.add(hotelhm);
                                }else if(p2a == m){
                                    if(p1a > d){
                                        Fragment_Running.records.add(hotelhm);
                                    }else{
                                        Fragment_Past.records.add(hotelhm);
                                    }
                                }else{
                                    Fragment_Past.records.add(hotelhm);
                                }
                            }else{
                                Fragment_Past.records.add(hotelhm);
                            }
                        }
                        Fragment_Running.adapter.notifyDataSetChanged();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("oidFK", dboFKey);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    void loadFavorites(final String dboFKey){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Loading favorites ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_LOAD_FAVORITES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        //if(Fragment_Favorite.listView.getCount()==0){

                        Fragment_Favorite.records.clear(); //clear the old list and load the new one

                        JSONArray jhotel= jObj.getJSONArray("hotel");
                        Log.d("hotel",jhotel.toString());

                        for(int i=0; i<jhotel.length(); i++){
                            HashMap<String,String> hotelhm = new HashMap<>();
                            JSONObject hotel = jhotel.getJSONObject(i);
                            final String fid = hotel.getString("fid");
                            final String hname = hotel.getString("name");
                            final String hcity = hotel.getString("city");
                            final String userID = hotel.getString("userID");

                            hotelhm.put("fid",fid);
                            hotelhm.put("hotelname",hname);
                            hotelhm.put("hotelcity", hcity);
                            hotelhm.put("userID", userID);
                            hotelhm.put("tk", htk);

                            Fragment_Favorite.records.add(hotelhm);
                        }
                        Fragment_Favorite.adapter.notifyDataSetChanged(); //Notify the adapter for the update
                       // }else{
                       //     Toast.makeText(getApplicationContext(), "Your Favorite hotels", Toast.LENGTH_SHORT).show();
                        //}
                        Fragment_Favorite.nofavoritesLabel.setVisibility(GONE);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("oidFK", dboFKey);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    void loadMyBookings(final String dboFKey){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Loading bookings ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_LOAD_TRAVELER_MYBOOKINGS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        if(Fragment_Booked.myBookingslistView.getCount()==0){

                            Fragment_Booked.records.clear(); //clear the old list and load the new one

                            JSONArray jhotel= jObj.getJSONArray("hotel");
                            Log.d("hotel",jhotel.toString());

                            for(int i=0; i<jhotel.length(); i++){
                                HashMap<String,String> hotelhm = new HashMap<>();
                                JSONObject hotel = jhotel.getJSONObject(i);
                                final String resID = hotel.getString("reservationID");
                                final String hoteln = hotel.getString("hotelname");
                                final String checkn = hotel.getString("checkin");
                                final String checkt = hotel.getString("checkout");
                                final String dates = checkn + " - " + checkt;
                                final String tid = hotel.getString("tidFK");


                                String a = checkt;

                                String[] partsa = a.split("/");
                                String part1a = partsa[0];
                                String[] p = part1a.split(" ");
                                part1a = p[0];
                                String part2a = partsa[1];
                                String part3a = partsa[2];
                                int p1a = Integer.parseInt(part1a);
                                int p2a = Integer.parseInt(part2a);
                                int p3a = Integer.parseInt(part3a);

                                Calendar c = Calendar.getInstance();
                                final int y = c.get(Calendar.YEAR);
                                final int m = c.get(Calendar.MONTH) + 1;
                                final int d = c.get(Calendar.DATE);

                                hotelhm.put("resID",resID);
                                hotelhm.put("hotelname",hoteln);
                                hotelhm.put("checkin", checkn);
                                hotelhm.put("checkout", checkt);
                                hotelhm.put("dates", dates);
                                hotelhm.put("tid", tid);

                                if(p3a > y){
                                    Fragment_Booked.records.add(hotelhm);
                                }else if(p3a == y){
                                    if(p2a > m){
                                        Fragment_Booked.records.add(hotelhm);
                                    }else if(p2a == m){
                                        if(p1a > d){
                                            Fragment_Booked.records.add(hotelhm);
                                        }else{
                                            //Save here a list with all the bookings so
                                            //the user have the option to switch between
                                            // all booking dates and the upcomming
                                        }

                                    }else{
                                        //Save here a list with all the bookings so
                                        //the user have the option to switch between
                                        // all booking dates and the upcomming
                                    }

                                }else{
                                    //Save here a list with all the bookings so
                                    //the user have the option to switch between
                                    // all booking dates and the upcomming
                                }
                            }
                            Fragment_Booked.adapter.notifyDataSetChanged(); //Notify the adapter for the update
                        }else{
                            Toast.makeText(getApplicationContext(), "Your Bookings", Toast.LENGTH_SHORT).show();
                        }
                        Fragment_Booked.nobookings.setVisibility(GONE);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("oidFK", dboFKey);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void loadHotels(final String dboFKey){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Loading hotels ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_LOAD_HOTELS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Login Response: " + response.toString());
                //String resp = response.toString();
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        if(listView.getCount()==0 || flagaddnew){

                            hotels.records.clear(); //clear the old list and load the new one

                            JSONArray jhotel= jObj.getJSONArray("hotel");
                            Log.d("hotel",jhotel.toString());

                            for(int i=0; i<jhotel.length(); i++){
                                HashMap<String,String> hotelhm = new HashMap<>();
                                JSONObject hotel = jhotel.getJSONObject(i);
                                hid = hotel.getString("hid");
                                hn = hotel.getString("name");
                                hc = hotel.getString("city");
                                ha = hotel.getString("address");
                                htk = hotel.getString("tk");
                                ht = hotel.getString("telephone");
                                hs = hotel.getString("stars");
                                hoidFK = hotel.getString("oidFK");
                                hw = hotel.getString("website");
                                hsp = hotel.getString("swimmingpool");

                                hotelhm.put("name",hn);
                                hotelhm.put("city", hc);
                                hotelhm.put("address", ha);
                                hotelhm.put("tk", htk);
                                hotelhm.put("telephone", ht);
                                hotelhm.put("stars", hs);
                                hotelhm.put("website", hw);
                                hotelhm.put("swimmingpool", hsp);
                                hotels.records.add(hotelhm);

                                flagaddnew = false;
                            }
                            hotels.adapter.notifyDataSetChanged(); //Notify the adapter for the update
                        }else{
                            Toast.makeText(getApplicationContext(), "To edit a hotel, click on it", Toast.LENGTH_SHORT).show();
                        }
                        Fragment_hotels.txt.setVisibility(GONE);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("oidFK", dboFKey);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void loadCoordinates(){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Loading hotels ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_LOAD_COORDINATES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {

                        JSONArray jcoord= jObj.getJSONArray("coords");
                        Log.d("hotel",jcoord.toString());

                        for(int i=0; i<jcoord.length(); i++){
                            //HashMap<String,String> coordlhm = new HashMap<>();
                            JSONObject coord = jcoord.getJSONObject(i);
                            String name = coord.getString("hotelname");
                            String lat = coord.getString("latitude");
                            String lon = coord.getString("longitude");
                            Fragment_maps.addMarkers(lat, lon, name);
                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("oidFK", dboFKey);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void hotelcoords(final String hotelname){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Loading hotels ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_LOAD_BOOKING_COORDS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        JSONArray jcoord= jObj.getJSONArray("coords");
                        Log.d("hotel",jcoord.toString());

                        JSONObject coord = jcoord.getJSONObject(0);
                        booking_lat = Double.parseDouble(coord.getString("latitude"));
                        booking_lon = Double.parseDouble(coord.getString("longitude"));
                        //setCoords(booking_lat, booking_lon);


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        booking_lat = 34.43772254507513;
                        booking_lon = -41.0009765625;
                        //setCoords(34.43772254507513,-41.0009765625);
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("hotelname", hotelname);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    // Update Functions
    public void updateUser(final String dbmail, final String name, final String surname,
                           final String email, final String pass, final String telephone,
                           final String country){
        String tag_string_req = "req_login";

        pDialog.setMessage("Update user ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {


                        Toast.makeText(getApplicationContext(), "Your account info has been updated!",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("dbmail", dbmail);
                params.put("nname", name);
                params.put("nsurname", surname);
                params.put("nemail", email);
                params.put("npass", pass);
                params.put("ntelephone", telephone);
                params.put("ncountry", country);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void updateHotel(final String hoteln, final String hotelname, final String city,
                            final String address, final String postalcode, final String hoteltelephone,
                            final String stars, final String website, final String swimmingpool){
        String tag_string_req = "req_login";

        pDialog.setMessage("Update hotel ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE_HOTEL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Hotel "+ hoteln +" has been updated!",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("dbhname", hoteln);
                params.put("nhname", hotelname);
                params.put("nhcity", city);
                params.put("nhaddress", address);
                params.put("nhpostalcode", postalcode);
                params.put("nhtelephone", hoteltelephone);
                params.put("nhwebsite", website);
                params.put("nhstars", stars);
                params.put("nhswimmingpool", swimmingpool);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    // Delete Functions
    public void deleteHotel(final String hoteln){

        String tag_string_req = "req_login";

        pDialog.setMessage("Delete user ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_DELETE_HOTEL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String toast = jObj.getString("error_msg");
                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(), toast,
                                Toast.LENGTH_SHORT).show();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("hotelname", hoteln);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void deleteUser(final String dbmail){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Delete user ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_DELETE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {


                        Toast.makeText(getApplicationContext(), "Your account has been deleted!",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", dbmail);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void travelerLoadHotels(final String city, final String checkin, final String checkout){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Loading hotels ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_LOAD_RESULT_HOTELS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Login Response: " + response.toString());
                //String resp = response.toString();
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {

                        Fragment_Search.records.clear(); //clear the old list and load the new one

                        JSONArray jhotel= jObj.getJSONArray("hotel");
                        Log.d("hotel",jhotel.toString());

                        for(int i=0; i<jhotel.length(); i++){
                            HashMap<String,String> hotelhm = new HashMap<>();
                            JSONObject hotel = jhotel.getJSONObject(i);
                            hid = hotel.getString("hid");
                            hn = hotel.getString("name");
                            hc = hotel.getString("city");
                            ha = hotel.getString("address");
                            htk = hotel.getString("tk");
                            ht = hotel.getString("telephone");
                            hs = hotel.getString("stars");
                            hoidFK = hotel.getString("oidFK");
                            hw = hotel.getString("website");
                            hsp = hotel.getString("swimmingpool");

                            hotelhm.put("name",hn);
                            hotelhm.put("city", hc);
                            hotelhm.put("address", ha);
                            hotelhm.put("tk", htk);
                            hotelhm.put("telephone", ht);
                            hotelhm.put("stars", hs);
                            hotelhm.put("website", hw);
                            hotelhm.put("swimmingpool", hsp);
                            Fragment_Search.records.add(hotelhm);

                            flagaddnew = false;
                        }
                        Fragment_Search.adapter.notifyDataSetChanged(); //Notify the adapter for the update

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("city", city);
                params.put("checkin", checkin);
                params.put("checkout", checkout);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    // Progress Dialog
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //-----------------------------------------------------------Classes-------------------------------------------------------//

    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // When clicked, select open the appropriate fragment
            selectItem(position);

        }
    }
}