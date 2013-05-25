package com.example.cargaterminal.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cargaterminal.ObjectUnit;
import com.example.cargaterminal.R;
import com.example.cargaterminal.JSONUtil.JSONUtilities;
import com.example.cargaterminal.JSONUtil.UnitJSONParser;
import com.example.cargaterminal.fragments.UnitSearcherFragment.UnitSearcherInterface;
import com.example.cargaterminal.DatabaseHelper;

public class ListUnitsLoadPendingFragment extends Fragment {
	private final String TAG = getClass().getSimpleName();
	private ListView mListaUnidades;
	//private UnitsLoadPendingAdapter mAdapter = null;
	private SimpleCursorAdapter mAdapter = null;
	private View myView;
	//private static ArrayList<ObjectUnit> mArray = new ArrayList<ObjectUnit>();
	private ListUnitsLoadPendingInterface listener;
	private DatabaseHelper db = null;
	private Integer total_units_pending = 0;
	
	// Interface
	public interface ListUnitsLoadPendingInterface{
		//public void selectItemPending(ObjectUnit unit);
		public void selectItemPending(Cursor unit);
		public void getTotalUnitsPending(Integer total);
		//public void listUnitsPendingLoaded(String[] listUnitsPending);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		if (activity instanceof ListUnitsLoadPendingInterface) {
			listener = (ListUnitsLoadPendingInterface) activity;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		myView = inflater.inflate(R.layout.fragment_list_units_load_pending, container, false);
		mListaUnidades = (ListView) myView.findViewById(R.id.listUnitsLoadPending);
		
		mListaUnidades.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				if(listener==null){
					return;
				}
				
				//listener.selectItemPending(mArray.get(position));
				Cursor cursor = ((SimpleCursorAdapter) mAdapter).getCursor();
				cursor.moveToPosition(position);
				listener.selectItemPending(cursor);
			}
        });
		
		setData();
		
		return myView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		//mAdapter = new UnitsLoadPendingAdapter(getActivity());
		//mListaUnidades.setAdapter(mAdapter);
	}
	
	@Override
	public void onDestroy() {

		super.onDestroy();
		//((CursorAdapter)getListAdapter()).getCursor().close();
		db.close();
	}

	private void setData(){
		Log.d(TAG,"Starts setData");
        //mArray.clear();

        // leemos el archivo de assets
        //JSONUtilities json_utilites = new JSONUtilities(getActivity());
        //String json = json_utilites.getJSONFromAssets("units_load_pending.json");
        
        //UnitJSONParser unit_parser = new UnitJSONParser(json);
        //mArray =  unit_parser.parser();
		
		//DatabaseHelper.getInstance(getActivity()).getListUnitToLoad(0, this);
		reload();
		Log.d(TAG,"Finished setData");
    }
	
	public void reload(){
		new LoadUnitsLoadPendingTask().execute();
	}
	
	private Cursor getListUnitsLoadPendingQuery() {
		String[] args= { "0" };
		db = DatabaseHelper.getInstance(getActivity());
		return(db.getReadableDatabase().rawQuery("SELECT * FROM units_to_load WHERE loaded=?",args));
	}

	private class LoadUnitsLoadPendingTask extends AsyncTask<Void, Void, Void> {
		private Cursor listUnitsLoadPendingCursor=null;

		@Override
		protected Void doInBackground(Void... params) {
			listUnitsLoadPendingCursor=getListUnitsLoadPendingQuery();
			total_units_pending = listUnitsLoadPendingCursor.getCount();
			Log.d(TAG,"LoadUnitsLoadPendingTask, total: " + total_units_pending);
			return (null);
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public void onPostExecute(Void arg0) {

			if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
				if (mAdapter==null){
					mAdapter=new SimpleCursorAdapter(getActivity(), R.layout.list_loaded_units_item,
												listUnitsLoadPendingCursor, 
												new String[] {"vin","dr_number","make","model"},
												new int[] { R.id.vin, R.id.dr_number, R.id.make, R.id.model },
												0);
					mListaUnidades.setAdapter(mAdapter);
				}
				else{
					mAdapter.changeCursor(listUnitsLoadPendingCursor);
				}
			}
			else {
				if (mAdapter==null){
					mAdapter=new SimpleCursorAdapter(getActivity(), R.layout.list_loaded_units_item,
					listUnitsLoadPendingCursor, 
					new String[] {"ooking","dr_number","make","model"},
					new int[] { R.id.booking, R.id.dr_number, R.id.make, R.id.model });
					mListaUnidades.setAdapter(mAdapter);
				}
				else{
					mAdapter.changeCursor(listUnitsLoadPendingCursor);
				}
			}
			
			//listener.listUnitsPendingLoaded(getListVIN());
			listener.getTotalUnitsPending(total_units_pending);
		}
	}
	
	
	/*
	public static class UnitsLoadPendingAdapter extends BaseAdapter 
	{
		private Context mContext;
		
		public UnitsLoadPendingAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return mArray.size();
		}

		@Override
		public Object getItem(int position) {
			return mArray.get(position);
		}

		@Override
		public long getItemId(int position) {
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
	}
	*/
	
	/*
	 * (non-Javadoc)
	 * @see com.example.cargaterminal.DatabaseHelper.ListUnitsToLoadInterface#getListUnits(android.database.Cursor)
	 * 
	 * Interface para recuperar una lista de unidades de DatabaseHelper
	 *
	 
	@Override
	public void getListUnits(Cursor list_unit) {
		
		
	}
	*/
	
}