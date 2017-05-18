package com.example.hotelreseration.NavigationDrawer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.hotelreseration.LoginActivity;
import com.example.hotelreseration.R;

public class Fragment_user extends Fragment {
	
	private Uri mSelectedImageUri;
	
	 private static final int SELECT_PICTURE = 1;
	 private String selectedImagePath=null;
	 private ImageView img;
	 
	
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState){
    	View rootView = inflater.inflate(R.layout.fragment_user, container, false);
    	
    	EditText ChangeName=(EditText)rootView.findViewById(R.id.ChangeName);
		EditText ChangeSurname=(EditText)rootView.findViewById(R.id.ChangeSurname);
    	EditText ChangeTelephone=(EditText)rootView.findViewById(R.id.ChangeTelephone);
    	EditText ChangeMail=(EditText)rootView.findViewById(R.id.ChangeMail);
    	EditText ChangeCountry=(EditText)rootView.findViewById(R.id.ChangeCountry);

        ChangeName.setText(new StringBuilder().append(MainActivity.dbname));
        ChangeSurname.setText(new StringBuilder().append(MainActivity.dbsurname));
        ChangeTelephone.setText(new StringBuilder().append(MainActivity.dbtelephone));
        ChangeMail.setText(new StringBuilder().append(MainActivity.dbmail));
        ChangeCountry.setText(new StringBuilder().append(MainActivity.dbcountry));
    	
    	if(LoginActivity.flagkzaf){
    		ChangeName.setText(new StringBuilder().append("Konstantinos"));
    		ChangeSurname.setText(new StringBuilder().append("Zafeiropoulos"));
    		ChangeTelephone.setText(new StringBuilder().append("+30 6984794915"));
    		ChangeMail.setText(new StringBuilder().append("kzaf@it.teithe.gr"));
    		ChangeCountry.setText(new StringBuilder().append("Greece"));
    	}

    		//Apo edw kai katw einai gia tin eikona pou tha epilegei o xrhsths na emfanizetai
    		img = (ImageView)rootView.findViewById(R.id.userImage);
    		if (mSelectedImageUri != null) {
    		    img.setImageURI(mSelectedImageUri);
    		}
                img.setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
                    }
                });     
        return rootView;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == MainActivity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
            	mSelectedImageUri = data.getData();
                selectedImagePath = getPath(mSelectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                img.setImageURI(mSelectedImageUri);
            }
        }
    }
 
    public String getPath(Uri uri) 
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    
    @Override
	public void onSaveInstanceState(Bundle outState) {        
        super.onSaveInstanceState(outState);
        // Save the image bitmap into outState
        Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
        outState.putParcelable("bitmap", bitmap);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Read the bitmap from the savedInstanceState and set it to the ImageView
        if (savedInstanceState != null){
	        Bitmap bitmap = (Bitmap) savedInstanceState.getParcelable("bitmap");
	        img.setImageBitmap(bitmap);
         }  
    }

}