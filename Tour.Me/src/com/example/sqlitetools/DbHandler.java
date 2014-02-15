package com.example.sqlitetools;

import java.util.ArrayList;
import java.util.List;

import com.example.jsonentities.CacheModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHandler extends SQLiteOpenHelper {
	
	private static final int DbVersion = 1;
	private static final String DbName = "Cache_Database";
	private static final String TableName = "cachetable";
		
	private static final String colid = "id";
	private static final String colname = "name";
	private static final String colvalue = "data";

	
	

	public DbHandler(Context context) {
		super(context, DbName, null, DbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sqlQuerry= "CREATE TABLE "+TableName+"("+colid+" INTEGER PRIMARY KEY,"+colname+
				" TEXT,"+colvalue+" TEXT)";
		
		db.execSQL(sqlQuerry);
		init(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISITS"+TableName);
		
		onCreate(db);	
	}
	
	public void addData(int id,String data){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		//id=1 for near me pois data
		//id=2 for navigation data
		//id=3 for blah blah blah data
		cv.put(colvalue, data);
		db.update(TableName, cv, "id="+id, null);
		//db.rawQuery("UPDATE cachetable SET data='"+data+"' WHERE id="+id,null);
		
	}
	
	public void init(SQLiteDatabase db){
		 
		ContentValues cv = new ContentValues();
		//id=1 for near me pois data
		//id=2 for navigation data
		//id=3 for blah blah blah data
		cv.put(colid,1);
		cv.put(colname,"nearme");
		cv.put(colvalue,"");
		
		
		db.insert(TableName, null, cv);
		ContentValues cv2 = new ContentValues();
		cv2.put(colid,2);
		cv2.put(colname,"navigation");
		cv2.put(colvalue,"");
		db.insert(TableName, null, cv2);
	}
	
	public CacheModel getCache(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TableName, new String[] { colid,
	    		colname, colvalue}, colid + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    CacheModel cache = new CacheModel(Integer.parseInt(cursor.getString(0)),
	            cursor.getString(1), cursor.getString(2));
	    return cache;
	}
	
	public String getData(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.rawQuery("SELECT * FROM cachetable WHERE id="+id, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    return cursor.getString(2);
	}
	
	   

}
