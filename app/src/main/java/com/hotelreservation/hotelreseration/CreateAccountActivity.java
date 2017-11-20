package com.hotelreservation.hotelreseration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hotelreservation.hotelreseration.DataBase.AppConfig;
import com.hotelreservation.hotelreseration.DataBase.AppController;
import com.hotelreservation.hotelreseration.DataBase.SQLiteHandler;
import com.hotelreservation.hotelreseration.DataBase.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends ActionBarActivity {

	private static final String TAG = CreateAccountActivity.class.getSimpleName();

    public String getname;
    public String getsurname;
    public String getcountry;
    public String getemail;
    public String gettel;
    public String getpassword;
	public Button suButton;

	private ProgressDialog pDialog;
	private SessionManager session;
	private SQLiteHandler db;
    public String URL_REGISTER;

	//--------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		//disable the ActionBar
		android.support.v7.app.ActionBar AB=getSupportActionBar();
		AB.hide();
		
		final EditText name= (EditText)findViewById(R.id.Country);
        final EditText surname= (EditText)findViewById(R.id.ChangeSurname);
        final EditText country= (EditText)findViewById(R.id.regCountry);
        final EditText mail= (EditText)findViewById(R.id.ChangeMail);
        final EditText tel= (EditText)findViewById(R.id.ChangeTelephone);
        final EditText pass= (EditText)findViewById(R.id.InputPass);
        final EditText rpass= (EditText)findViewById(R.id.ReEnterPass);
        suButton= (Button)findViewById(R.id.SignUp);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        suButton.setOnClickListener(new View.OnClickListener()
        {
             public void onClick(View arg0)
             {
                 //elegxw an ola ta pedia exoun symplhrwthei
                 if (name.getText().toString().equals("")||
                         surname.getText().toString().equals("")||
                         country.getText().toString().equals("")||
                         mail.getText().toString().equals("")||
                         tel.getText().toString().equals("")||
                         pass.getText().toString().equals(""))
                 {
                        //Toast is the pop up message
                        Toast.makeText(getApplicationContext(), "Please fill in all of the fields",
                        Toast.LENGTH_LONG).show();
                 }
                 //elegxw an ta pedia tou password einai idia
                 else if(pass.getText().toString().equals(rpass.getText().toString()))
                 {
                     getname = name.getText().toString().trim();
                     getsurname = surname.getText().toString().trim();
                     getcountry = country.getText().toString().trim();
                     getemail = mail.getText().toString().trim();
                     getpassword = pass.getText().toString().trim();
                     gettel = tel.getText().toString().trim();

                     if (SelectUserActivity.flagOwner){
                         URL_REGISTER = AppConfig.URL_REGISTER_OWNER;
                     }else{
                         URL_REGISTER = AppConfig.URL_REGISTER_TRAVELER;
                     }
                     //Make the registration
                     registerUser(getname, getsurname, getcountry, getemail, getpassword, gettel);
                 }
                 else{
                    //Toast is the pop up message
                        Toast.makeText(getApplicationContext(), "Password does not match!",
                        Toast.LENGTH_LONG).show();
                 }
             }
        });

        //Change the Text View font
        TextView reg = (TextView) findViewById(R.id.LoginAs);
        Typeface regface = Typeface.createFromAsset(getAssets(),"fonts/KaushanScript-Regular.ttf");
        reg.setTypeface(regface);
        //Set the text on the login form
        final TextView Register = (TextView)findViewById(R.id.LoginAs);
        if (SelectUserActivity.flagOwner){
            Register.setText("Register as " +" "+"Owner");
        }
        else{
            Register.setText("Register as " +" "+"Traveler");
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_account, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}

	//-----------------------------------------------------------DB--------------------------------------------------------//

	private void registerUser(final String name, final String surname, final String country, final String email, final String password, final String telephone)
    {
        // Tag used to cancel the request
		String tag_string_req = "req_register";

		pDialog.setMessage("Registering ...");
		showDialog();

		StringRequest strReq = new StringRequest(Request.Method.POST, URL_REGISTER, new Response.Listener<String>()
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
					if (!error)
					{
						// User successfully stored in MySQL
						// Now store the user in sqlite
						JSONObject user = jObj.getJSONObject("user");
                        String id = user.getString("id");
						String name = user.getString("name");
                        String surname = user.getString("surname");
                        String country = user.getString("country");
						String email = user.getString("email");
                        String password = user.getString("password");
                        String telephone = user.getString("telephone");
                        //int telephone = 123456;//Integer.parseInt(tel);

						// Inserting row in users table
						db.addUser(id, name, surname, country, email, password, telephone);

						Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

						// Launch login activity
						Intent intent = new Intent(
								CreateAccountActivity.this,
								LoginActivity.class);
						startActivity(intent);
						finish();
					}
					else
                    {

						// Error occurred in registration. Get the error
						// message
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
				params.put("name", name);
                params.put("surname", surname);
                params.put("country", country);
				params.put("email", email);
				params.put("password", password);
                params.put("telephone", String.valueOf(telephone));

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

}
