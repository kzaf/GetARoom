package com.example.hotelreseration;

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

public class CreateAccountActivity extends ActionBarActivity {
	//--------------------------------------------
	//EditText USER_NAME, USER_PASS, CON_PASS;
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
		final EditText mail= (EditText)findViewById(R.id.ChangeMail);
		final EditText tel= (EditText)findViewById(R.id.ChangeTelephone);
		final EditText pass= (EditText)findViewById(R.id.InputPass);
		final EditText rpass= (EditText)findViewById(R.id.ReEnterPass);
		final Button suButton= (Button)findViewById(R.id.SignUp);

		 suButton.setOnClickListener(
		     new View.OnClickListener()
		     {
		         public void onClick(View arg0)
		         {
		        	 //elegxw an ola ta pedia exoun symplhrwthei
		        	 if (name.getText().toString().equals("") || surname.getText().toString().equals("")||
		        			 mail.getText().toString().equals("")||tel.getText().toString().equals("")||
		        			 pass.getText().toString().equals("")){
		        		    //Toast is the pop up message
		            	 	Toast.makeText(getApplicationContext(), "Please fill in all of the fields",
		            	 	Toast.LENGTH_LONG).show();
		        	 } 	
		             //elegxw an ta pedia tou password einai idia
		        	 else if(pass.getText().toString().equals(rpass.getText().toString())){
				     			//Toast is the pop up message
				            	 	Toast.makeText(getApplicationContext(), "Registration done succesfully",
				            	 	Toast.LENGTH_LONG).show();
				     		}
				     		else{
				     			//Toast is the pop up message
				            	 	Toast.makeText(getApplicationContext(), "Password does not match!",
				            	 	Toast.LENGTH_LONG).show();
				     		}		        	 
		         }
		     });
		 //me aytes tis entoles allazw thn grammatoseira enos Text View
		 TextView reg = (TextView) findViewById(R.id.LoginAs);
		 Typeface regface = Typeface.createFromAsset(getAssets(),"fonts/KaushanScript-Regular.ttf");
		 reg.setTypeface(regface);
		 
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

}
