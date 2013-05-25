package com.example.cargaterminal;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.cargaterminal.JSONUtil.JSONUtilities;
import com.example.cargaterminal.JSONUtil.VoyageJSONParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListVoyageActivity extends ListActivity {

	private VoyageListAdapter mAdapter = null;
		
	// ArrayList
	private static ArrayList<ObjectVoyage> mArray = new ArrayList<ObjectVoyage>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_voyage);
		
		setData();
		
		mAdapter = new VoyageListAdapter(this);
        setListAdapter(mAdapter);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
    	// Create a new intent to call other Activity. 
    	// Using the methods "putExtra" we can
    	// send data to the new activity
    	
    	Toast.makeText(this, mArray.get(position).getmVoyage(), Toast.LENGTH_SHORT).show();
    	
    	int screenSize = getResources().getConfiguration().screenLayout &
    	        Configuration.SCREENLAYOUT_SIZE_MASK;

    	switch(screenSize) {
    		case Configuration.SCREENLAYOUT_SIZE_XLARGE:
    	    case Configuration.SCREENLAYOUT_SIZE_LARGE:
    	    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
    	        // Actividad para pantalla grande (tablet)
    	    	Intent intent = new Intent( ListVoyageActivity.this, LoadingProcessTabletActivity.class);
    	    	startActivity(intent);
    	        break;
    	    case Configuration.SCREENLAYOUT_SIZE_SMALL:
    	    default:
    	    	// Actividad para pantalla pequena (movil)
    	        Toast.makeText(this, "Screen size is neither large or normal" , Toast.LENGTH_LONG).show();
    	}
    	
    	//Intent intent = new Intent( ListVoyageActivity.this, Activity1.class);
		//intent.putExtra(VALUE_1, getString(R.string.app_name));
		//intent.putExtra(VALUE_2, 3);
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setData(){

        mArray.clear();

        // leemos el archivo de assets
        JSONUtilities json_utilites = new JSONUtilities(this);
        String json = json_utilites.getJSONFromAssets("list_voyage.json");
        
        VoyageJSONParser voyage_parser = new VoyageJSONParser(json);
        mArray =  voyage_parser.parser();
    }

	public static class VoyageListAdapter extends BaseAdapter 
	{
		private Context mContext;
		
		public VoyageListAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mArray.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			//Log.d("TAG", "position " + String.valueOf(position));
			View view = null;
		
			if (convertView == null){
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				view = inflater.inflate(R.layout.list_voyage_activity, null);
			}
			else
				view = convertView;
						
						
			TextView tVoyage = (TextView) view.findViewById(R.id.voyage);
			tVoyage.setText(mArray.get(position).getmVoyage());
			
			TextView tDate = (TextView) view.findViewById(R.id.date);
			tDate.setText(mArray.get(position).getmDate());
			
			TextView tUnitsToLoad = (TextView) view.findViewById(R.id.units_to_load);
			tUnitsToLoad.setText(mArray.get(position).getmUnitsToLoad().toString());
			
			return view;
	
		}
	}
	
}