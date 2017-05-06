package com.example.hotelreseration;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SelectUserActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_user);
		//disable the ActionBar
		android.support.v7.app.ActionBar AB=getSupportActionBar();
		AB.hide();
		
		//mhdenismos twn flag
		SelectUserActivity.flagOwner=false;
		LoginActivity.flagkzaf=false;
		
		//Orizw to label sto loginactivity na leei Log in as Owner otan epilegw Owner
		//opote to startActivity event energopoieitai apo edw.
		//An den epileksw Owner kai epileksw Traveler, to flagOwner den ginetai true, opote to startActivity
		//energopoieitai kanonika apo th methodo 'changeToLoginScreen' pou vrisketai parakatw
		final Button ownerButton= (Button)findViewById(R.id.Owner);
		ownerButton.setOnClickListener(
			     new View.OnClickListener()
			     {
			         public void onClick(View arg0)
			         {
			             flagOwner=true;
			             startActivity(new Intent(SelectUserActivity.this, LoginActivity.class));
			         }
			     });
		//me aytes tis entoles allazw thn grammatoseira enos Text View
		TextView sr = (TextView) findViewById(R.id.SelectRole);
		Typeface srface = Typeface.createFromAsset(getAssets(),"fonts/black_jack.ttf");
		sr.setTypeface(srface);

		TextView welcome = (TextView) findViewById(R.id.Welcome);
		Typeface welcomeface = Typeface.createFromAsset(getAssets(),"fonts/KaushanScript-Regular.ttf");
		welcome.setTypeface(welcomeface);

		//orizw to flag gia to listview me ta hotel na einai false
	}
	
	
	/**
	 * Back button listener.
	 * Will close the application if the back button pressed twice.
	 * Kanei elegxo gia na kanei eksodo apo to programma
	 */
	private int backButtonCount=0;

	@Override
	public void onBackPressed()
	{
	    if(backButtonCount >= 1)
	    {	    	
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.addCategory(Intent.CATEGORY_HOME);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(intent);
	    }
	    else
	    {
	        Toast.makeText(this, "Press the back button once again to exit.", Toast.LENGTH_SHORT).show();
	        backButtonCount++;
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_user, menu);
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

	//change to: login activity
	//ksanakanw to flagOwner = false se periptwsh pou epistrepsw sthn othonh epiloghs xrhsth ksana
	//na mhn exei meinei to flag = true giati tha deixnei panta Owner

	public void changeToLoginScreen(View v)
	{
		backButtonCount=0;//midenizw ton counter gia na pataw 2 fores to plhktro pisw gia na termatistei h efarmogh. Vrisketai parapanw
		startActivity(new Intent(SelectUserActivity.this, LoginActivity.class));
		flagOwner=false;
	}
	public static boolean flagOwner=false;
}
