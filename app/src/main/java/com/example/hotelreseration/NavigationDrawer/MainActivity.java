package com.example.hotelreseration.NavigationDrawer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;

import java.util.ArrayList;
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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hotelreseration.CreateAccountActivity;
import com.example.hotelreseration.DataBase.AppController;
import com.example.hotelreseration.DataBase.SQLiteHandler;
import com.example.hotelreseration.LoginActivity;
import com.example.hotelreseration.R;
import com.example.hotelreseration.SelectUserActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static com.example.hotelreseration.DataBase.AppConfig.URL_LOAD_HOTELS;
import static com.example.hotelreseration.DataBase.AppConfig.URL_REGISTER_COORDINATES;
import static com.example.hotelreseration.DataBase.AppConfig.URL_REGISTER_HOTEL;
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

    public static String hid, hn, hc, ha,
            htk, ht, hs, hoidFK, hw, hsp;

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
                db.deleteUsers();
                Toast.makeText(getApplicationContext(), "Your account has been deleted!",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SelectUserActivity.class));

            }

        });
        // Showing Alert Message
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
            hotel1.setVisibility(promptView.GONE);
            hotel2.setVisibility(promptView.GONE);
            hotel3.setVisibility(promptView.GONE);
        }else if (listView.getCount()==1){
            hotel1.setEnabled(true);
            hotel1.setChecked(true);
            hotels.adapter.notifyDataSetChanged();
            HashMap<String, String> hashMap = (HashMap<String, String>) listView.getItemAtPosition(0);
            String aLongValue = hashMap.get("name");
            hotel1.setText(aLongValue);
            hotel2.setVisibility(promptView.GONE);
            hotel3.setVisibility(promptView.GONE);
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
            hotel3.setVisibility(promptView.GONE);
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
                    alertDialog.setTitle("Add new hotel");
                    alertDialog.setIcon(R.drawable.addhotel);
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    View promptView = layoutInflater.inflate(R.layout.fragment_add_hotel, null);


                    hotelname= (EditText)promptView.findViewById(R.id.hotelname);
                    hotelcity= (EditText)promptView.findViewById(R.id.hotelcity);
                    hoteladdress= (EditText)promptView.findViewById(R.id.hoteladdress);
                    hotelpostalcode= (EditText)promptView.findViewById(R.id.hotelpostalcode);
                    hoteltelephone= (EditText)promptView.findViewById(R.id.hoteltelephone);
                    hotelwebsite= (EditText)promptView.findViewById(R.id.hotelwebsite);

                    alertDialog.setButton("OK", new DialogInterface.OnClickListener()
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
                                //Orizw tis times pou thelw sto HashMap kai tis kanw add sth lista me ta ksenodoxeia tou Owner
                                //Sto arxeio Fragment_hotels.java vrisketai h lista
                                HashMap<String,String> hotel = new HashMap<String,String>();
                                hotel.put("name",hotelname.getText().toString());
                                hotel.put("city", hotelcity.getText().toString());
                                hotels.records.add(hotel);
                                onomaxarth=hotelname.getText().toString();//krataei to onoma gia na to kanei pin sto xarth
                                hotels.adapter.notifyDataSetChanged();//kanei update to listview me ta hotels
                                Toast.makeText(getApplicationContext(), "Hotel info changed!", Toast.LENGTH_SHORT).show();
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
                                hotels.records.remove(0);
                                hotels.adapter.notifyDataSetChanged();
                                //Afou kanei delete to ksenodoxeio phgainei sto fragment me th lista twn ksenodoxeiwn
                                fragTran = getSupportFragmentManager().beginTransaction();
                                fragTran.replace(R.id.content_frame, hotels);
                                fragTran.commit();
                            }else if (hotel2.isChecked()){
                                Toast.makeText(getApplicationContext(), "Hotel "+hotel2.getText()+" removed!",Toast.LENGTH_LONG).show();
                                hotels.records.remove(1);
                                hotels.adapter.notifyDataSetChanged();
                                //Afou kanei delete to ksenodoxeio phgainei sto fragment me th lista twn ksenodoxeiwn
                                fragTran = getSupportFragmentManager().beginTransaction();
                                fragTran.replace(R.id.content_frame, hotels);
                                fragTran.commit();
                            }else if (hotel3.isChecked()){
                                Toast.makeText(getApplicationContext(), "Hotel "+hotel3.getText()+" removed!",Toast.LENGTH_LONG).show();
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
        alertDialog.setButton("Save Changes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(getApplicationContext(), "Your account info has been changed!",
                        Toast.LENGTH_SHORT).show();
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
        final TextView Ascending = (TextView) findViewById(R.id.ascending);
        final TextView Descending = (TextView) findViewById(R.id.descending);
        final boolean[] flg = {false};

        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                day=datePicker.getDayOfMonth();
                month=datePicker.getMonth() + 1;
                year=datePicker.getYear();

                if (flag){
                    CheckIn.setText(new StringBuilder().append(day).append(" ").append("-").append(month).append("-").append(year));
                }
                else{

                    String a = CheckIn.getText().toString();

                    String[] partsa = a.split("-");
                    String part1a = partsa[0];
                    String[] p = part1a.split(" ");
                    part1a = p[0];
                    String part2a = partsa[1];
                    String part3a = partsa[2];
                    int p1a = Integer.parseInt(part1a);
                    int p2a = Integer.parseInt(part2a);
                    int p3a = Integer.parseInt(part3a);

                    if(p3a < year){
                        CheckOut.setText(new StringBuilder().append(day).append(" ").
                                append("-").append(month).append("-").append(year));
                        flg[0] = true;
                    }else if(p3a == year)
                    {
                        if(p2a < month){
                            CheckOut.setText(new StringBuilder().append(day).append(" ").
                                    append("-").append(month).append("-").append(year));
                            flg[0] = true;
                        }else if(p2a == month)
                        {
                            if(p1a < day){
                                CheckOut.setText(new StringBuilder().append(day).append(" ").
                                        append("-").append(month).append("-").append(year));
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
                if (flg[0]){
                    Ascending.setVisibility(View.VISIBLE);
                    Descending.setVisibility(View.VISIBLE);
                }

            }

        });
        // Showing Alert Message
        alertDialog.show();
    }

    // Classes ----------------------------------------------------------------
    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // When clicked, select open the appropriate fragment
            selectItem(position);

        }
    }


    //-----------------------------------------------------------DB-Functions-------------------------------------------------------//

    private void registerHotel(final String hotelname, final String city, final String address,
                               final String postalcode, final String hoteltelephone,
                               final String stars, final String dboFKey, final String website, final String swimmingpool)
    {

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

    public void registerCoordinates(final String onomaxarth, final String oidFK, final String latitude, final String longitude)
    {

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
                        //loadHotels(dboFKey); //reload the hotel list each time the user add a new one
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
                                hotels.records.add(hotelhm);

                                flagaddnew = false;
                            }
                            hotels.adapter.notifyDataSetChanged(); //Notify the adapter for the update
                        }else{
                            Toast.makeText(getApplicationContext(), "To edit a hotel, click on it", Toast.LENGTH_SHORT).show();
                        }
                        Fragment_hotels.txt.setVisibility(View.GONE);

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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //-----------------------------------------------------------DB-Functions-------------------------------------------------------//

}