package com.example.hotelreseration;

//import android.R;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelreseration.NavigationDrawer.MainActivity;

public class LoginActivity extends ActionBarActivity {
	
	public static String mail;
	public static String pass;
	public static boolean flagkzaf=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//disable the ActionBar
		android.support.v7.app.ActionBar AB=getSupportActionBar();
		AB.hide();
		
		//Get Text Values
		final Button mButton= (Button)findViewById(R.id.SignInbutton);
		final EditText Email= (EditText)findViewById(R.id.EmaileditText);
		final EditText Pass= (EditText)findViewById(R.id.PasswordeditText);

		 mButton.setOnClickListener(
		     new View.OnClickListener()
		     {
		         public void onClick(View arg0)
		         {
		        	 if (SelectUserActivity.flagOwner==true){//ginetai elegxos an exeis syndethei ws Owner wste to email na einai owner
		        		 if (Email.getText().toString().contains("owner") && Pass.getText().toString().contains("0000")){
		        			 startActivity(new Intent(LoginActivity.this, MainActivity.class));
		        		 }
		        		 else
		        			 //Toast is the pop up message
		        			 Toast.makeText(getApplicationContext(), "Wrong Email or Password!",
		        					 Toast.LENGTH_LONG).show();
		        		 
		        	 }else{//ginetai elegxos an exeis syndethei ws Traveler wste to email na einai traveler
		        		 if (Email.getText().toString().contains("traveler") && Pass.getText().toString().contains("0000")){
		        			 startActivity(new Intent(LoginActivity.this, MainActivity.class));
		        		 }
		        		 else
		        			 //Toast is the pop up message
		        			 Toast.makeText(getApplicationContext(), "Wrong Email or Password!",
		        					 Toast.LENGTH_LONG).show();
		        	 }
						/////////////////////////////////////////////////
						if (Email.getText().toString().contains("kzaf") && Pass.getText().toString().contains("251991")){
							 startActivity(new Intent(LoginActivity.this, MainActivity.class));
							 flagkzaf=true;
						}
						else
							 //Toast is the pop up message
							 Toast.makeText(getApplicationContext(), "Wrong Email or Password!",
									 Toast.LENGTH_LONG).show();
						/////////////////////////////////////////////////
						mail=Email.getText().toString();
						pass=Pass.getText().toString();
		         }
		     });
		 //me aytes tis entoles allazw thn grammatoseira enos Text View
		 TextView reg = (TextView) findViewById(R.id.LoginAs);
		 Typeface regface = Typeface.createFromAsset(getAssets(),"fonts/KaushanScript-Regular.ttf");
		 reg.setTypeface(regface);
		 
		 //Edw orizw to TextView Login as "Owner h Traveler" analoga ti exw epileksei.
		 final TextView LoginText = (TextView)findViewById(R.id.LoginAs);
		 if (SelectUserActivity.flagOwner==true){
			LoginText.setText("Login as " +" "+"Owner");
		 }
		 else{
			LoginText.setText("Login as " +" "+"Traveler");
		 } 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	//change to: create account activity
	public void changeToCreateAccount(View v){
		startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
	}
	//change to: main screen activity
	public void changeToMainScreen(View v){
		startActivity(new Intent(LoginActivity.this, MainActivity.class));
	}
	//change to: forgot pass activity
	public void changeForgotPass(View v){
		startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class));
	}
	
}
