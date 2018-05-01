package com.hotelreservation.hotelreseration;

//import android.R;
//import android.R;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ForgotPassActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_pass);

		//disable the ActionBar
		android.support.v7.app.ActionBar AB=getSupportActionBar();
        if (AB == null) throw new AssertionError();
        AB.hide();

		//me aytes tis entoles allazw thn grammatoseira enos Text View
        Typeface fptface = Typeface.createFromAsset(getAssets(),"fonts/KaushanScript-Regular.ttf");

        TextView fpt = (TextView) findViewById(R.id.forgotpasstext);
        TextView dw = (TextView) findViewById(R.id.dontworry);
        TextView luk = (TextView) findViewById(R.id.LetUsKnow);
        fpt.setTypeface(fptface);
        dw.setTypeface(fptface);
        luk.setTypeface(fptface);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_pass, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
    {
		return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
	}

	//Edw orizw ta mail sta opoia tha steilei email o xrhsths se periptwsh pou ksexasei to password tou
	String subject="Forgot Password!";
	String message="I forgot my account password at HotelReservation app, please give me a new one. My account name is: ";
	public void sentMail(View v)
    {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","kzaf@it.teithe.gr,ziskatas@it.teithe.gr", null));
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
	    intent.putExtra(Intent.EXTRA_TEXT, message);
		startActivity(Intent.createChooser(intent, "Choose an Email client :"));
	}
}
