package com.example.cargaterminal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Dictionary;

import com.example.cargaterminal.fragments.ListUnitsLoadPendingFragment;
import com.example.cargaterminal.fragments.ListUnitsLoadPendingFragment.ListUnitsLoadPendingInterface;
import com.example.cargaterminal.fragments.ListUnitsLoadedFragment;
import com.example.cargaterminal.fragments.ListUnitsLoadedFragment.ListUnitsLoadedInterface;
import com.example.cargaterminal.fragments.ImageGalleryFragment;
import com.example.cargaterminal.fragments.LoadingProcessResumeFragment;
import com.example.cargaterminal.fragments.UnitInformationFragment;
import com.example.cargaterminal.fragments.UnitSearcherFragment;
import com.example.cargaterminal.fragments.UnitSearcherFragment.UnitSearcherInterface;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
//import es.curso.android.fragments.R;
//import es.curso.android.fragments.list.MyListFragment.IListFragment;
import android.widget.Toast;

public class LoadingProcessTabletActivity extends FragmentActivity 
	implements UnitSearcherInterface, ListUnitsLoadPendingInterface, ListUnitsLoadedInterface{
	
	private final String TAG = getClass().getSimpleName();
	private FragmentManager fm = null;
	private UnitSearcherFragment mUnitSearcherFragment = null;
	private UnitInformationFragment mUnitInformationFragment = null;
	private ListUnitsLoadedFragment mListUnitsLoadedFragment = null;
	private ListUnitsLoadPendingFragment mListUnitsLoadPendingFragment = null;
	private ImageGalleryFragment mImageGalleryFragment = null;
	private LoadingProcessResumeFragment mLoadingProcessResumeFragment = null;
	private Integer mTotalUnitsLoaded = 0;
	private Integer mTotalUnitsPending = 0;
	private final String VOYAGE = "1331";
	private final String PORT = "PORT EVERGLADES";
	private DatabaseHelper db = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        setContentView(R.layout.loading_process_tablet);

        fm = getSupportFragmentManager();
        
        UnitSearcherFragment fragUnitSearcher = (UnitSearcherFragment) fm.findFragmentByTag("unit_searcher");
        if (fragUnitSearcher == null) {
        	FragmentTransaction ftUnitSearcher = fm.beginTransaction();
        	mUnitSearcherFragment = new UnitSearcherFragment();
        	ftUnitSearcher.add(R.id.llHeaderLeft, mUnitSearcherFragment, "unit_searcher");
        	ftUnitSearcher.commit();
        }
        
        Fragment fragImageGallery = fm.findFragmentByTag("image_gallery");
        if (fragImageGallery == null) {
           FragmentTransaction ftImageGallery = fm.beginTransaction();
           mImageGalleryFragment = new ImageGalleryFragment();
           ftImageGallery.add (R.id.llHeaderRight, mImageGalleryFragment, "image_gallery");
           ftImageGallery.commit();
        }
        
        Fragment fragUnitInformation = fm.findFragmentByTag("unit_information");
        if (fragUnitInformation == null) {
           FragmentTransaction ftUnitInformation = fm.beginTransaction();
           mUnitInformationFragment = new UnitInformationFragment();
           ftUnitInformation.add (R.id.llHeaderCenter, mUnitInformationFragment, "unit_information");
           ftUnitInformation.commit();
        }
        
        Fragment fragListUnitLoaded = fm.findFragmentByTag("list_unit_loaded");
        if (fragListUnitLoaded == null) {
           FragmentTransaction ftListUnitLoaded = fm.beginTransaction();
           mListUnitsLoadedFragment = new ListUnitsLoadedFragment();
           ftListUnitLoaded.add (R.id.llFootLeft, mListUnitsLoadedFragment, "list_unit_loaded");
           ftListUnitLoaded.commit();
        }
        
        Fragment fragListUnitLoadPending = fm.findFragmentByTag("list_unit_load_pending");
        if (fragListUnitLoadPending == null) {
           FragmentTransaction ftListUnitLoadPending = fm.beginTransaction();
           mListUnitsLoadPendingFragment = new ListUnitsLoadPendingFragment();
           ftListUnitLoadPending.add (R.id.llFootCenter, mListUnitsLoadPendingFragment, "list_unit_load_pending");
           ftListUnitLoadPending.commit();
        }      
        
        LoadingProcessResumeFragment fragLoadingProcessResume = (LoadingProcessResumeFragment) fm.findFragmentByTag("loading_process_resume");
        if (fragLoadingProcessResume == null) {
        	FragmentTransaction ftLoadingProcessResume = fm.beginTransaction();
        	mLoadingProcessResumeFragment = LoadingProcessResumeFragment.newInstance(VOYAGE, PORT);
        	ftLoadingProcessResume.add(R.id.llFootRight, mLoadingProcessResumeFragment, "loading_process_resume");
        	ftLoadingProcessResume.commit();
        }
		
        init();
	}
	
	private UnitSearcherFragment getmUnitSearcherFragment(){
		if (mUnitSearcherFragment==null){
			mUnitSearcherFragment = (UnitSearcherFragment) fm.findFragmentByTag("unit_searcher");
		}
		
		return mUnitSearcherFragment;		
	}
	
	private ImageGalleryFragment getmImageGalleryFragment(){
		if (mImageGalleryFragment==null){
			mImageGalleryFragment = (ImageGalleryFragment) fm.findFragmentByTag("image_gallery");
		}
		
		return mImageGalleryFragment;		
	}
	
	private UnitInformationFragment getmUnitInformationFragment(){
		if (mUnitInformationFragment==null){
			mUnitInformationFragment = (UnitInformationFragment) fm.findFragmentByTag("unit_information");
		}
		
		return mUnitInformationFragment;		
	}
	
	private LoadingProcessResumeFragment getmLoadingProcessResumeFragment(){
		if (mLoadingProcessResumeFragment==null){
			mLoadingProcessResumeFragment = (LoadingProcessResumeFragment) fm.findFragmentByTag("loading_process_resume");
		}
		
		return mLoadingProcessResumeFragment;		
	}
	
	public void init(){
		
		if(fm==null){
			fm = getSupportFragmentManager();
		}
		
		getmImageGalleryFragment().resetPhotos();		
		new LoadDataResumeTask().execute();
		//String[] a = {};
	}

	
	
	private Cursor getListUnitsToLoadQuery(String loaded) {
		String[] args= { loaded };
		
		db = DatabaseHelper.getInstance(this);

		return(db.getReadableDatabase().rawQuery("SELECT * FROM units_to_load WHERE loaded=? ORDER BY date_loaded",args));
	}
	
	
	private class LoadDataResumeTask extends AsyncTask<Void, Void, Void> {
		private String strInitDate = "";
		private String strLastDate = "";
		private Integer TotalUnitsPending = 0;
		private Integer TotalUnitsLoaded = 0;
		
		@Override
		protected Void doInBackground(Void... params) {
			
			Cursor UnitsPending = getListUnitsToLoadQuery("0");
			TotalUnitsPending = UnitsPending.getCount();
			UnitsPending.close();
			
			Cursor UnitsLoaded = getListUnitsToLoadQuery("1");
			TotalUnitsLoaded = UnitsLoaded.getCount();
			if(TotalUnitsLoaded>0){
				UnitsLoaded.moveToFirst();
				strInitDate = UnitsLoaded.getString(UnitsLoaded.getColumnIndexOrThrow("date_loaded"));
				if(TotalUnitsLoaded<=1)
					strLastDate = strInitDate;
				else{
					UnitsLoaded.moveToLast();
					strLastDate = UnitsLoaded.getString(UnitsLoaded.getColumnIndexOrThrow("date_loaded"));
				}
			}
			
			UnitsLoaded.close();
			
			return (null);
		}
		@Override
		protected void onPostExecute(Void result) {
			
			if(TotalUnitsLoaded>0){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				try {
					Log.d(TAG,"updateWidgetInformation. date loaded 1: " + strInitDate);
					strInitDate = dateFormat.parse(strInitDate).toString();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				try {
					Log.d(TAG,"updateWidgetInformation. date loaded 2: " + strLastDate);
					strLastDate = dateFormat.parse(strLastDate).toString();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			getmLoadingProcessResumeFragment().updateWidgetInformation(strInitDate, strLastDate, TotalUnitsLoaded, TotalUnitsPending);
		}
	}
	
	
	
	/*
	 * 
	 * Metodos para interface UnitSearcherFragment
	 * 
	 */
	
	@Override
	public void newPhoto(Cursor unit) {
		Log.d(TAG, "newPhoto: ENTER");
		getmImageGalleryFragment().setPhotos(unit, "photography_incidence");
		Log.d(TAG, "newPhoto: EXIT");
	}
	
	
	@Override
	public void pushLoad(String VIN) {
		//Toast.makeText(this, "Push load: " + VIN, Toast.LENGTH_SHORT).show();
		mListUnitsLoadedFragment.reload();
		getmUnitInformationFragment().resetUnitInformation();
		getmImageGalleryFragment().resetPhotos();
		mListUnitsLoadPendingFragment.reload();
		new LoadDataResumeTask().execute();
	}
	
	@Override
	public void getTotalUnitsLoaded(Integer total) {
		mTotalUnitsLoaded = total;
		getmLoadingProcessResumeFragment().setUnitsLoaded(mTotalUnitsLoaded);
	}

	@Override
	public void selectItem(Cursor unit) {
		getmUnitInformationFragment().updateUnitInformation(unit);
		getmImageGalleryFragment().setPhotos(unit, "photography_incidence");
	}
	
	@Override
	public void pushIncidence(String vin) {
		int screenSize = getResources().getConfiguration().screenLayout &
    	        Configuration.SCREENLAYOUT_SIZE_MASK;

    	switch(screenSize) {
    		case Configuration.SCREENLAYOUT_SIZE_XLARGE:
    	    case Configuration.SCREENLAYOUT_SIZE_LARGE:
    	    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
    	        // Actividad para pantalla grande (tablet)
    	    	Intent intent = new Intent( LoadingProcessTabletActivity.this, UnitIncidenceActivity.class);
    	    	intent.putExtra("vin",vin);
    	    	startActivity(intent);
    	        break;
    	    case Configuration.SCREENLAYOUT_SIZE_SMALL:
    	    default:
    	    	// Actividad para pantalla pequena (movil)
    	        Toast.makeText(this, "Screen size is neither large or normal" , Toast.LENGTH_LONG).show();
    	}
	}
	

	/*
	 * Metodos para interface ListUnitsLoadPendingInterface
	 * 
	 */
	@Override
	public void selectItemPending(Cursor unit) {
		getmUnitSearcherFragment().updateVINFromUnitPending(unit.getString(unit.getColumnIndexOrThrow("vin")));
		getmUnitInformationFragment().updateUnitInformation(unit);
		getmImageGalleryFragment().setPhotos(unit, "photography_incidence");
	}

	@Override
	public void getTotalUnitsPending(Integer total) {
		mTotalUnitsPending = total;
		getmLoadingProcessResumeFragment().setUnitsPending(mTotalUnitsPending);
	}
	
	/*
	 *  
	 * Metodos para interface ListUnitsLoadedInterface
	 * 
	 */
	@Override
	public void selectItemLoaded(Cursor unit) {
		getmUnitSearcherFragment().updateVINFromUnitLoaded(unit.getString(unit.getColumnIndexOrThrow("vin")));
		getmUnitInformationFragment().updateUnitInformation(unit);
		getmImageGalleryFragment().setPhotos(unit, "photography_incidence");
	}

}