package com.example.cargaterminal.fragments;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cargaterminal.DatabaseHelper;
import com.example.cargaterminal.R;
import com.example.cargaterminal.R.id;
import com.example.cargaterminal.R.layout;
import com.example.cargaterminal.R.string;

public class UnitSearcherFragment extends Fragment 
{
	private final String TAG = getClass().getSimpleName();
	private View myView;
	private AutoCompleteTextView txtVIN = null;
	private Button btnLoad = null;
	private Button btnPhoto = null;
	private Button btnIncidence = null;
	private UnitSearcherInterface listener;
	private ArrayList<String> listVIN = new ArrayList<String>();
	private ArrayAdapter<String> myAdapter = null;
	private DatabaseHelper db = null;
	private Uri imageUri = null;
	private static final int CAMERA_REQUEST = 1888; 
	
	// Interface
	public interface UnitSearcherInterface{
		public void pushLoad(String VIN);
		public void selectItem(Cursor unit);
		public void pushIncidence(String vin);
		public void newPhoto(Cursor unit);
		//public void dismissedList(String text);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		if (activity instanceof UnitSearcherInterface) {
			listener = (UnitSearcherInterface) activity;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		myView = inflater.inflate(R.layout.fragment_searcher, container, false);

		db = DatabaseHelper.getInstance(getActivity());
		
		txtVIN = (AutoCompleteTextView) myView.findViewById(R.id.txtVIN);
		
		btnIncidence = (Button) myView.findViewById(R.id.btnIncindence);
		btnIncidence.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.pushIncidence(txtVIN.getText().toString());
			}
		});
		btnIncidence.setVisibility(View.INVISIBLE);
		
		btnPhoto = (Button) myView.findViewById(R.id.btnPhoto);
		btnPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
				Date today = Calendar.getInstance().getTime();
				String name = df.format(today);
		        File photo = new File(Environment.getExternalStorageDirectory(), name + ".jpg");
		        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
		        imageUri = Uri.fromFile(photo);
		        startActivityForResult(intent, CAMERA_REQUEST);
			}
		});
		btnPhoto.setVisibility(View.INVISIBLE);
		
		
		btnLoad = (Button) myView.findViewById(R.id.btnLoad);
		btnLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pushLoadButton(v);
			}
		});
		btnLoad.setVisibility(View.INVISIBLE);
		
		myAdapter = new ArrayAdapter<String>(getActivity(), R.layout.autocomplete_item, R.id.item, listVIN);
		//myAdapter.setNotifyOnChange(true);
		
		txtVIN.setAdapter(myAdapter);
		
		txtVIN.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
				selectItemAutocomplete(v);
				//btnLoad.setClickable(true);
				btnLoad.setVisibility(View.VISIBLE);
				btnPhoto.setVisibility(View.VISIBLE);
				btnIncidence.setVisibility(View.VISIBLE);
			}
		});
		
		txtVIN.setOnKeyListener(new OnKeyListener() {	
			@Override
			public boolean onKey(View v, int arg1, KeyEvent arg2) {
				if(!txtVIN.isPerformingCompletion()){
					//btnLoad.setClickable(false);
					btnLoad.setVisibility(View.INVISIBLE);
					btnPhoto.setVisibility(View.INVISIBLE);
					btnIncidence.setVisibility(View.INVISIBLE);
				}
				return false;
			}
		});
		
		setAutoCompleteListVIN();
		
		// valido a partir de la API 17
		//txtVIN.setOnDismissListener(new OnDismissListener(){
		//});
		
		return myView;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		//((CursorAdapter)getListAdapter()).getCursor().close();
		db.close();
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG,"onActivityResult: requestCode: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
	        case CAMERA_REQUEST:
	            if (resultCode == Activity.RESULT_OK) {
	            	Log.d(TAG,"onActivityResult: Returning from CAMERA: ENTER");
	                Uri selectedImage = imageUri;
	                getActivity().getContentResolver().notifyChange(selectedImage, null);
	                //ContentResolver cr = getActivity().getContentResolver();
	                //Bitmap bitmap;
	                try {
	                     /*bitmap = android.provider.MediaStore.Images.Media
	                     .getBitmap(cr, selectedImage);
	
	                    viewHolder.imageView.setImageBitmap(bitmap);*/
	                	new SavePhotoTask().execute(selectedImage.toString());
	                    Toast.makeText(getActivity(), selectedImage.toString(),
	                            Toast.LENGTH_LONG).show();
	                } catch (Exception e) {
	                    Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT)
	                            .show();
	                    Log.e("Camera", e.toString());
	                }
	                Log.d(TAG,"onActivityResult: Returning from CAMERA: EXIT");
	         }
        }
	}
	
	public void setAutoCompleteListVIN(){
		Log.d(TAG,"setAutoCompleteListVIN: ENTER.");
		new LoadUnitsLoadPendingTask().execute();
		Log.d(TAG,"setAutoCompleteListVIN: EXIT");
	}
	
	private void pushLoadButton(View v){
		
		if(listener==null){
			return;
		}

		new LoadUnitTask().execute();
	}
	
	private void selectItemAutocomplete(View v){
		
		if(listener==null){
			return;
		}
		
		//listener.selectItem(txtVIN.getText().toString());
		new GetUnitTask().execute();
	}
	
	public void updateVINFromUnitPending(String vin)
	{
		
		if (vin!=null){
			txtVIN.setText(vin);
			txtVIN.dismissDropDown();
		}
		
		if (vin==""){
			btnLoad.setVisibility(View.INVISIBLE);
			btnPhoto.setVisibility(View.INVISIBLE);
			btnIncidence.setVisibility(View.INVISIBLE);
		}
		else{
			btnLoad.setVisibility(View.VISIBLE);
			btnPhoto.setVisibility(View.VISIBLE);
			btnIncidence.setVisibility(View.VISIBLE);
		}
	}
	
	public void updateVINFromUnitLoaded(String vin){
		if (vin!=null)
			txtVIN.setText(vin);
		
		btnIncidence.setVisibility(View.VISIBLE);
		btnPhoto.setVisibility(View.VISIBLE);
		btnLoad.setVisibility(View.INVISIBLE);
	}
	
	private void loadUnitQuery() {
		Log.d(TAG,"loadUnitQuery: ENTER");
		
		String vin = txtVIN.getText().toString();
		
		if(vin!=""){
			Log.d(TAG,"loadUnitQuery: execute UPDATE");
			
			ContentValues args = new ContentValues();
	        args.put("loaded", 1);
	        
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	        Date date = new Date();
	        args.put("date_loaded", dateFormat.format(date));
	        
	        String[] whereArgs= { vin };
			db.getWritableDatabase().update("units_to_load", args, "vin=?", whereArgs);
		}
		else{
			Toast.makeText(getActivity(), "No VIN selected", Toast.LENGTH_LONG).show();
		}
		Log.d(TAG,"loadUnitQuery: EXIT");
	}

	
	private class LoadUnitTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			loadUnitQuery();
			return (null);
		}
		
		@Override
		protected void onPostExecute(Void arg0) {
			String vin = txtVIN.getText().toString();
			
			listener.pushLoad(vin);
			btnLoad.setVisibility(View.INVISIBLE);
			btnPhoto.setVisibility(View.INVISIBLE);
			btnIncidence.setVisibility(View.INVISIBLE);
			myAdapter.remove(vin);
			txtVIN.setText("");
			listener.pushLoad(vin);
		}
	}
	
	private Cursor getListUnitsLoadPendingQuery() {
		String[] args= { "0" };
		return(db.getReadableDatabase().rawQuery("SELECT vin FROM units_to_load WHERE loaded=?",args));
	}

	private class LoadUnitsLoadPendingTask extends AsyncTask<Void, Void, Void> {
		private Cursor listUnitsLoadPendingCursor=null;

		@Override
		protected Void doInBackground(Void... params) {
			listUnitsLoadPendingCursor=getListUnitsLoadPendingQuery();
			Integer total_units_pending = listUnitsLoadPendingCursor.getCount();
			Log.d(TAG,"LoadUnitsLoadPendingTask, total: " + total_units_pending);
			return (null);
		}
		
		@Override
		public void onPostExecute(Void arg0) {			
			
			listVIN.clear();
			for(listUnitsLoadPendingCursor.moveToFirst();!listUnitsLoadPendingCursor.isAfterLast();listUnitsLoadPendingCursor.moveToNext()){
				listVIN.add(listUnitsLoadPendingCursor.getString(listUnitsLoadPendingCursor.getColumnIndexOrThrow("vin")));

			}
		    listUnitsLoadPendingCursor.close();
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
	
	private void saveUnitPhotoQuery(String strListPhoto, String vin) {
		Log.d(TAG,"saveUnitPhotoQuery: ENTER");
		
		if(vin!=""){
			Log.d(TAG,"saveUnitPhotoQuery: execute UPDATE");
			
			ContentValues args = new ContentValues();
	        args.put("photography_incidence", strListPhoto);
	        
	        String[] whereArgs= { vin };
			db.getWritableDatabase().update("units_to_load", args, "vin=?", whereArgs);
		}
		else{
			Toast.makeText(getActivity(), "No VIN selected", Toast.LENGTH_LONG).show();
		}
		Log.d(TAG,"saveUnitPhotoQuery: EXIT");
	}
	
	
	private class SavePhotoTask extends AsyncTask<String, Void, Void> {
		String vin;
		Cursor unitCursor;
		
		@Override
		protected void onPreExecute(){
			vin = txtVIN.getText().toString();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			
			unitCursor = getUnitQuery(vin);
			
			if(unitCursor!=null){			
				String path_photo = params[0];
				String strListPhoto = unitCursor.getString(unitCursor.getColumnIndexOrThrow("photography_incidence"));
				
				if((strListPhoto=="")||(strListPhoto==null)){
					strListPhoto = path_photo;
				}
				else{
					strListPhoto += "," + path_photo;
				}
				saveUnitPhotoQuery(strListPhoto, vin);
				
				// refrescamos el cursor con la unidad, sino no se actualiza y el listener newPhotos no se entera
				unitCursor = getUnitQuery(vin);
			}
			return (null);
		}
		
		@Override
		protected void onPostExecute(Void arg0) {
			
			if(unitCursor==null){
				Toast.makeText(getActivity(), getResources().getString(R.string.error_unit_not_found), Toast.LENGTH_LONG).show();
			}
			else{
				listener.newPhoto(unitCursor);
			}
		}
	}
	
	private class GetUnitTask extends AsyncTask<Void, Void, Void> {
		String vin;
		Cursor unitCursor;
		
		@Override
		protected void onPreExecute(){
			vin = txtVIN.getText().toString();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			unitCursor = getUnitQuery(vin);
		
			return (null);
		}
		
		@Override
		protected void onPostExecute(Void arg0) {
			listener.selectItem(unitCursor);
		}
	}
	
}
