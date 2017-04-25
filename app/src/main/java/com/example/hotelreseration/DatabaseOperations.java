package com.example.hotelreseration;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hotelreseration.TableData.TableInfo;

public class DatabaseOperations extends SQLiteOpenHelper {
	public static final int database_version = 1;
	public String CREATE_QUERY = "CREATE_TABLE "+TableInfo.TABLE_NAME+"("+TableInfo.USER_NAME+" TEXT,"+TableInfo.USER_PASS+"TEXT );";

	public DatabaseOperations(Context context) {
		super(context, TableInfo.DATABASE_NAME, null, database_version);
		Log.d("DataBase Operations", "DataBase Created");
	}

	@Override
	public void onCreate(SQLiteDatabase sdb) {
		
		sdb.execSQL(CREATE_QUERY);
		Log.d("DataBase Operations", "Table Created");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	public void putInformation(DatabaseOperations dop, String name, String pass)
	{
		//SQLiteDatabase SQ= dop.getWritableDatabase();
		ContentValues cv= new ContentValues();
		cv.put(TableInfo.USER_NAME, name);
		cv.put(TableInfo.USER_PASS, pass);
		//long k = SQ.insert(TableInfo.TABLE_NAME, null, cv);
		Log.d("DataBase Operations", "One raw inserted");

	}

}
