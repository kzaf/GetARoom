package com.hotelreservation.hotelreseration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.hotelreservation.hotelreseration.NavigationDrawer.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity
{
    public static String mail, pass;
    public static boolean flagkzaf=false;
    private static final String TAG = CreateAccountActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    public String URL_LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //disable the ActionBar
        android.support.v7.app.ActionBar AB=getSupportActionBar();
        if (AB != null) AB.hide();

        //Get Text Values
        final Button signInButton= (Button)findViewById(R.id.SignInbutton);
        final EditText Email= (EditText)findViewById(R.id.EmaileditText);
        final EditText Pass= (EditText)findViewById(R.id.PasswordeditText);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        signInButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                mail = Email.getText().toString().trim();
                pass = Pass.getText().toString().trim();

                // Check for empty data in the form
                if (Email.length() != 0 && Pass.length() != 0)
                {
                    URL_LOGIN = SelectUserActivity.flagOwner ? AppConfig.URL_LOGIN_OWNER : AppConfig.URL_LOGIN_TRAVELER;
                    // login user
                    checkLogin(mail, pass);
                }
                else
                {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });


        //me aytes tis entoles allazw thn grammatoseira enos Text View
        TextView LoginText = (TextView) findViewById(R.id.LoginAs);
        LoginText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/KaushanScript-Regular.ttf"));

        //Edw orizw to TextView Login as "Owner h Traveler" analoga ti exw epileksei.
        LoginText.setText(SelectUserActivity.flagOwner ? "Login as " + " " + "Owner" : "Login as " + " " + "Traveler");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    //change to: create account activity
    public void changeToCreateAccount(View v)
    {
        startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
    }

    //change to: forgot pass activity
    public void changeForgotPass(View v)
    {
        startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class));
    }

//------------------------------------------------------DB--------------------------------------------------------------//
    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password)
    {
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();

                try
                {
                    JSONObject jObj = new JSONObject(response);

                    // Check for error node in json
                    if (jObj.getBoolean("error"))
                    {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in sqlite
                        JSONObject user = jObj.getJSONObject("user");
                        String id = user.getString("id");
                        String name = user.getString("name");
                        String surname = user.getString("surname");
                        String country = user.getString("country");
                        String email = user.getString("email");
                        String password = user.getString("password");
                        String telephone = user.getString("telephone");

                        // Inserting row in users table (SQLlite)
                        db.addUser(id, name, surname, country, email, password, telephone);

                        // Launch main activity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }
                catch (JSONException e)
                {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener()
            {

                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            })
        {
            @Override
            protected Map<String, String> getParams()
            {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "req_login");
    }

    private void showDialog()
    {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog()
    {
        if (pDialog.isShowing()) pDialog.dismiss();
    }
}