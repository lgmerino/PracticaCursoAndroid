package com.example.cargaterminal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private final String TAG = getClass().getSimpleName();
	private static final String DATABASE_NAME="scline_load_discharge.db";
	private static final int SCHEMA_VERSION=1;
	private static DatabaseHelper singleton=null;
	private Context ctxt=null;

	public synchronized static DatabaseHelper getInstance(Context ctxt) {
	    if (singleton == null) {
	      singleton=new DatabaseHelper(ctxt.getApplicationContext());
	    }
	
	    return(singleton);
	}

	private DatabaseHelper(Context ctxt) {
	    super(ctxt, DATABASE_NAME, null, SCHEMA_VERSION);
	    this.ctxt=ctxt;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "Starts onCreate");
	    try {
	      db.beginTransaction();
	      
	      /*db.execSQL("CREATE TABLE loading_process (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
	      		"date_init DATETIME, date_finish DATETIME, tramo_carga_id INTEGER);");*/
	      
	      db.execSQL("CREATE TABLE units_to_load (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
	      		"booking VARCHAR(30), customer VARCHAR(50), vin VARCHAR(17), make VARCHAR(30)," +
	      		"model VARCHAR(50), dr_number VARCHAR(8)," +
	      		"dr_id INTEGER, longitude VARCHAR(30), width VARCHAR(30), height VARCHAR(30), weight VARCHAR(30)," +
	      		"port_of_discharge VARCHAR(50), port_of_discharge_id INTEGER, tramo_carga_id INTEGER," +
	      		"incidence TEXT, photography TEXT, photography_incidence TEXT, " +
	      		"loaded TINYINT, date_loaded DATETIME, sent_to_server TINYINT, ack_from_server TINYINT);");
	      
	      db.execSQL("CREATE UNIQUE INDEX idx_units_to_load_vin ON units_to_load (vin, tramo_carga_id);");
	      
	      ContentValues cv=new ContentValues();

	      /*
	       * 
	       * 
	       * DATOS DE PRUEBA
	       * 
	       * 
	       */
	      
		  cv.put("booking", "SLSW033ALTCTG/01-13");
		  cv.put("vin", "1234567890A");
		  cv.put("customer", "SOFASA");
		  cv.put("make", "RENAULT");
		  cv.put("model", "DUSTER 4X4");
		  cv.put("longitude", "3.50");
		  cv.put("width", "1.80");
		  cv.put("height", "1.65");
		  cv.put("weight", "1500");
		  cv.put("port_of_discharge", "CARTAGENA");
		  cv.put("port_of_discharge_id", 2);
		  cv.put("dr_number", 1001);
		  cv.put("dr_id", 1001);
		  cv.put("tramo_carga_id", 100);
		  cv.put("loaded", 0);
		  cv.put("sent_to_server", 0);
		  cv.put("ack_from_server", 0);
	      db.insert("units_to_load", null, cv);
	      
		  cv.put("vin", "1234567890B");
		  cv.put("dr_number", 1002);
		  cv.put("dr_id", 1002);
	      db.insert("units_to_load", null, cv);
	      
		  cv.put("vin", "1234567890C");
		  cv.put("dr_number", 1003);
		  cv.put("dr_id", 1003);
		  db.insert("units_to_load", null, cv);
	      
		  cv.put("vin", "1234567890E");
		  cv.put("dr_number", 1004);
		  cv.put("dr_id", 1004);
		  db.insert("units_to_load", null, cv);

		  cv.put("vin", "1234567890F");
		  cv.put("dr_number", 1005);
		  cv.put("dr_id", 1005);
		  db.insert("units_to_load", null, cv);
		  
		  cv.put("vin", "1234567890G");
		  cv.put("dr_number", 1006);
		  cv.put("dr_id", 1006);
		  db.insert("units_to_load", null, cv);
		  
		  cv.put("vin", "1234567890H");
		  cv.put("dr_number", 1007);
		  cv.put("dr_id", 1007);
		  cv.put("model", "CLIO");
		  cv.put("longitude", "2.90");
		  cv.put("width", "1.65");
		  cv.put("height", "1.60");
		  cv.put("weight", "1100");
		  cv.put("port_of_discharge", "ALTAMIRA");
		  cv.put("port_of_discharge_id", 1);
		  db.insert("units_to_load", null, cv);
		  
		  cv.put("vin", "1234567890I");
		  cv.put("dr_number", 1008);
		  cv.put("dr_id", 1008);
		  db.insert("units_to_load", null, cv);
		  
		  cv.put("vin", "1234567890J");
		  cv.put("dr_number", 1009);
		  cv.put("dr_id", 1009);
		  db.insert("units_to_load", null, cv);
		  
		  cv.put("vin", "1234567890K");
		  cv.put("dr_number", 1010);
		  cv.put("dr_id", 1010);
		  db.insert("units_to_load", null, cv);
		  
		  cv.put("vin", "1234567890L");
		  cv.put("dr_number", 1011);
		  cv.put("dr_id", 1011);
		  db.insert("units_to_load", null, cv);
		  
		  cv.put("vin", "1234567890M");
		  cv.put("dr_number", 1012);
		  cv.put("dr_id", 1012);
		  db.insert("units_to_load", null, cv);
		  
		  cv.put("vin", "1234567890N");
		  cv.put("dr_number", 1013);
		  cv.put("dr_id", 1013);
		  db.insert("units_to_load", null, cv);
		  
		  cv.put("vin", "1234567890O");
		  cv.put("dr_number", 1014);
		  cv.put("dr_id", 1014);
		  db.insert("units_to_load", null, cv);
		  
		  cv.put("vin", "1234567890P");
		  cv.put("dr_number", 1015);
		  cv.put("dr_id", 1015);
		  db.insert("units_to_load", null, cv);
		  
		  cv.put("vin", "1234567890Q");
		  cv.put("dr_number", 1016);
		  cv.put("dr_id", 1016);
		  db.insert("units_to_load", null, cv);
		  
		  cv.put("vin", "1234567890R");
		  cv.put("dr_number", 1017);
		  cv.put("dr_id", 1017);
		  db.insert("units_to_load", null, cv);
	      db.setTransactionSuccessful();
	    }
	    finally {
	      db.endTransaction();
	    }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) {
		throw new RuntimeException(ctxt.getString(R.string.on_upgrade_error));
	}
}