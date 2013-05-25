package com.example.cargaterminal;

import com.example.cargaterminal.fragments.EditIncidenceFragment;
import com.example.cargaterminal.fragments.EditIncidenceFragment.EditIncidenceInterface;
import com.example.cargaterminal.fragments.ImageGalleryFragment;
import com.example.cargaterminal.fragments.UnitInformationVerticalFragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class UnitIncidenceActivity extends FragmentActivity implements EditIncidenceInterface{
	
	private final String TAG = getClass().getSimpleName();
	private FragmentManager fm = null;
	private Cursor unit;
	private DatabaseHelper db = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.unit_incidence_tablet);
        
        Bundle b = getIntent().getExtras();
        String vin = b.getString("vin");
        
        db = DatabaseHelper.getInstance(this);
        unit = getUnitQuery(vin);
        
        fm = getSupportFragmentManager();
        
        UnitInformationVerticalFragment fragUnitInformationVertical = (UnitInformationVerticalFragment) fm.findFragmentByTag("unit_information");
        if (fragUnitInformationVertical == null) {        	
        	FragmentTransaction ftUnitInformationVertical = fm.beginTransaction();
        	UnitInformationVerticalFragment mUnitInformationVerticalFragment = UnitInformationVerticalFragment.newInstance(unit);
        	ftUnitInformationVertical.add(R.id.llRight, mUnitInformationVerticalFragment, "unit_information");
        	ftUnitInformationVertical.commit();
        }        
        
        EditIncidenceFragment fragEditIncidence = (EditIncidenceFragment) fm.findFragmentByTag("edit_incidence");
        if (fragEditIncidence == null) {
        	String txtIncidence = unit.getString(unit.getColumnIndexOrThrow("incidence"));
        	
        	FragmentTransaction ftEditIncidence = fm.beginTransaction();
        	EditIncidenceFragment mEditIncidenceFragment = EditIncidenceFragment.newInstance(txtIncidence);
        	ftEditIncidence.add(R.id.llHeader, mEditIncidenceFragment, "edit_incidence");
        	ftEditIncidence.commit();
        }
        
        ImageGalleryFragment fragImageGallery = (ImageGalleryFragment) fm.findFragmentByTag("image_gallery");
        if (fragImageGallery == null) {
        	FragmentTransaction ftImageGallery = fm.beginTransaction();
        	ImageGalleryFragment mImageGalleryFragment = ImageGalleryFragment.newInstance(unit, "photography");
        	ftImageGallery.add(R.id.llFootLeftUp, mImageGalleryFragment, "image_gallery");
        	ftImageGallery.commit();
        }
        
        ImageGalleryFragment fragImageGalleryIncidence = (ImageGalleryFragment) fm.findFragmentByTag("image_gallery_incidence");
        if (fragImageGalleryIncidence == null) {
        	FragmentTransaction ftImageGalleryIncidence = fm.beginTransaction();
        	ImageGalleryFragment mImageGalleryIncidenceFragment = ImageGalleryFragment.newInstance(unit, "photography_incidence");
        	ftImageGalleryIncidence.add(R.id.llFootLeftBottom, mImageGalleryIncidenceFragment, "image_gallery_incidence");
        	ftImageGalleryIncidence.commit();
        }
	}
	
	private Cursor getUnitQuery(String vin) {
		Log.d(TAG,"getUnitQuery: ENTER");
		
		Cursor unitCursor = null;

		if(vin!=""){
			Log.d(TAG,"getUnitQuery: execute SELECT");
			
			String[] args= { vin };
	        
			unitCursor = db.getReadableDatabase().rawQuery("SELECT * FROM units_to_load WHERE vin=?", args);
			unitCursor.moveToFirst();
			if(unitCursor.isAfterLast()){
				Log.d(TAG,"getUnitQuery: I cant find unit with VIN: " + vin);
				unitCursor=null;
			}
			else{
				Log.d(TAG,"getUnitQuery: Unit found with VIN: " + vin);
			}
				
		}
		Log.d(TAG,"getUnitQuery: EXIT");
		
		return(unitCursor);
	}
	
	private void saveUnitIncidenceQuery(String incidence) {
		Log.d(TAG,"saveUnitIncidenceQuery: ENTER");
		
		if(unit!=null){
			Log.d(TAG,"saveUnitIncidenceQuery: execute UPDATE");
			
			ContentValues args = new ContentValues();
	        args.put("incidence", incidence);
	        
	        String[] whereArgs= { unit.getString(unit.getColumnIndexOrThrow("vin")) };
			db.getWritableDatabase().update("units_to_load", args, "vin=?", whereArgs);
		}
		else{
			Toast.makeText(this, "No VIN selected", Toast.LENGTH_LONG).show();
		}
		Log.d(TAG,"saveUnitIncidenceQuery: EXIT");
	}
	
	
	private class SaveIncidenceTask extends AsyncTask<String, Void, Void> {
		
		@Override
		protected Void doInBackground(String... params) {
			
			saveUnitIncidenceQuery(params[0]);
			
			return (null);
		}
		
		@Override
		protected void onPostExecute(Void arg0) {
			UnitIncidenceActivity.this.finish();
		}
	}
	

	/*
	 * 
	 * Interface para EditIncidenceInterface
	 * 
	 * 
	 */
	
	@Override
	public void pushIncidenceSave(String incidence) {
		new SaveIncidenceTask().execute(incidence);		
	}

	@Override
	public void pushIncidenceCancel() {
		this.finish();		
	}
}