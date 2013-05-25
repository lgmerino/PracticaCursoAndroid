package com.example.cargaterminal.fragments;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cargaterminal.DatabaseHelper;
import com.example.cargaterminal.ObjectUnit;
import com.example.cargaterminal.R;
import com.example.cargaterminal.JSONUtil.JSONUtilities;
import com.example.cargaterminal.JSONUtil.UnitJSONParser;
import com.example.cargaterminal.fragments.ListUnitsLoadPendingFragment.ListUnitsLoadPendingInterface;
import com.example.cargaterminal.fragments.UnitSearcherFragment.UnitSearcherInterface;

public class ListUnitsLoadedFragment extends Fragment{
	private final String TAG = getClass().getSimpleName();
	private ListView mListaUnidades;
	//private UnitsLoadedAdapter mAdapter = null;
	private SimpleCursorAdapter mAdapter = null;
	private View myView;
	//private static ArrayList<ObjectUnit> mArray = new ArrayList<ObjectUnit>();
	private ListUnitsLoadedInterface listener;
	private DatabaseHelper db = null;
	private Integer total_units_loaded = 0;
	
	// Interface
	public interface ListUnitsLoadedInterface{
		//public void selectItemLoaded(ObjectUnit unit);
		public void selectItemLoaded(Cursor unit);
		public void getTotalUnitsLoaded(Integer total);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		if (activity instanceof ListUnitsLoadedInterface) {
			listener = (ListUnitsLoadedInterface) activity;
		}
	}
	
	//public ListUnitsLoadedFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		db = DatabaseHelper.getInstance(getActivity());
		
		myView = inflater.inflate(R.layout.fragment_list_units_loaded, container, false);
		mListaUnidades = (ListView) myView.findViewById(R.id.listUnitsLoaded);
		
		mListaUnidades.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				if(listener==null){
					return;
				}
				
				//listener.selectItemLoaded(mArray.get(position));
				
				Cursor cursor = ((SimpleCursorAdapter) mAdapter).getCursor();
				cursor.moveToPosition(position);
				listener.selectItemLoaded(cursor);
			}
        });
		
		setData();
		
		return myView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		//mAdapter = new UnitsLoadedAdapter(getActivity());
		//mListaUnidades.setAdapter(mAdapter);
		
		/*mListaUnidades.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
        		Log.i("MyListFragment", "Item clicked: " + arg2);
			}
        });*/
	}
	
	@Override
	public void onDestroy() {

		super.onDestroy();
		//((CursorAdapter)getListAdapter()).getCursor().close();
		db.close();
	}
	
	private void setData(){
		Log.d(TAG,"Starts setData");
		
		/*
        mArray.clear();

        // leemos el archivo de assets
        JSONUtilities json_utilites = new JSONUtilities(getActivity());
        String json = json_utilites.getJSONFromAssets("units_loaded.json");
        
        UnitJSONParser unit_parser = new UnitJSONParser(json);
        mArray =  unit_parser.parser();
        */
		
		reload();
		Log.d(TAG,"Finished setData");
    }
	
	
	public void reload(){
		new LoadUnitsLoadedTask().execute();
	}
	
	private Cursor getListUnitsLoadedQuery() {
		String[] args= { "1" };
		return(db.getReadableDatabase().rawQuery("SELECT * FROM units_to_load WHERE loaded=?",args));
	}

	
	private class LoadUnitsLoadedTask extends AsyncTask<Void, Void, Void> {
		private Cursor listUnitsLoadedCursor=null;

		@Override
		protected Void doInBackground(Void... params) {
			listUnitsLoadedCursor=getListUnitsLoadedQuery();
			total_units_loaded = listUnitsLoadedCursor.getCount();
			Log.d(TAG,"LoadUnitsLoadedTask, total: " + total_units_loaded);
			return (null);
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public void onPostExecute(Void arg0) {

			if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
				if (mAdapter==null){
					mAdapter=new SimpleCursorAdapter(getActivity(), R.layout.list_loaded_units_item,
												listUnitsLoadedCursor, 
												new String[] {"vin","dr_number","make","model"},
												new int[] { R.id.vin, R.id.dr_number, R.id.make, R.id.model },
												0);
					mListaUnidades.setAdapter(mAdapter);
				}
				else{
					mAdapter.changeCursor(listUnitsLoadedCursor);
				}
			}
			else {
				if (mAdapter==null){
					mAdapter=new SimpleCursorAdapter(getActivity(), R.layout.list_loaded_units_item,
					listUnitsLoadedCursor, 
					new String[] {"vin","dr_number","make","model"},
					new int[] { R.id.vin, R.id.dr_number, R.id.make, R.id.model });
					mListaUnidades.setAdapter(mAdapter);
				}
				else{
					mAdapter.changeCursor(listUnitsLoadedCursor);
				}
			}
			listener.getTotalUnitsLoaded(total_units_loaded);
		}
	}
	/*
	public static class UnitsLoadedAdapter extends BaseAdapter 
	{
		private Context mContext;
		
		public UnitsLoadedAdapter(Context c) {
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
		
			if (convertView == null)
			{
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				view = inflater.inflate(R.layout.list_loaded_units_item, null);
			}
			else
				view = convertView;
						
					
			TextView tVin = (TextView) view.findViewById(R.id.vin);
			tVin.setText(mArray.get(position).getmVIN());
			
			TextView tMake = (TextView) view.findViewById(R.id.make);
			tMake.setText(mArray.get(position).getmMake());
			
			TextView tModel = (TextView) view.findViewById(R.id.model);
			tModel.setText(mArray.get(position).getmModel());
			
			TextView tDRNumber = (TextView) view.findViewById(R.id.dr_number);
			tDRNumber.setText(mArray.get(position).getmDRNumber().toString());
			
			return view;
	
		}
	}*/
	
}