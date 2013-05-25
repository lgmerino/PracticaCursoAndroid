package com.example.cargaterminal.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cargaterminal.DatabaseHelper;
import com.example.cargaterminal.R;

public class LoadingProcessResumeFragment extends Fragment 
{
	private final String TAG = getClass().getSimpleName();
	private View myView;
	private TextView mtxtTotalUnitsLoaded = null;
	private TextView mtxtTotalUnitsPending = null;

	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);	
	}
	
	public static LoadingProcessResumeFragment newInstance(String _voyage, String _port) {
		Log.d("LoadingProcessResumeFragment", "newInstance: ENTER");
		
		LoadingProcessResumeFragment f = new LoadingProcessResumeFragment();
		Bundle args = new Bundle();
		args.putString("voyage", _voyage);
		f.setArguments(args);
		args.putString("port", _port);
		f.setArguments(args);
		Log.d("LoadingProcessResumeFragment", "newInstance: EXIT");
		
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//db = DatabaseHelper.getInstance(getActivity());
		
		myView = inflater.inflate(R.layout.fragment_loading_process_resume, container, false);
		
		TextView txtVoyage = (TextView) myView.findViewById(R.id.voyage);
		txtVoyage.setText(getArguments().getString("voyage"));
		
		TextView txtPort = (TextView) myView.findViewById(R.id.port);
		txtPort.setText(getArguments().getString("port"));
		
		return myView;
	}

	public void setUnitsLoaded(Integer unitsLoaded){
		 
		if(mtxtTotalUnitsLoaded==null){
			mtxtTotalUnitsLoaded = (TextView) myView.findViewById(R.id.units_loaded);
		}
		mtxtTotalUnitsLoaded.setText(unitsLoaded.toString());
	}
	
	public void setUnitsPending(Integer unitsPending){
		 
		if(mtxtTotalUnitsPending==null){
			mtxtTotalUnitsPending = (TextView) myView.findViewById(R.id.total_drs_to_load);
		}
		mtxtTotalUnitsPending.setText(unitsPending.toString());
		
	}
	
	public void updateWidgetInformation(String strInitDate, String strLastDate, Integer TotalUnitsLoaded, Integer TotalUnitsPending){
		TextView txtTotalDRs = (TextView) myView.findViewById(R.id.total_dr);
		Integer total = TotalUnitsPending+TotalUnitsLoaded;
		txtTotalDRs.setText(total.toString());
		
		setUnitsLoaded(TotalUnitsLoaded);
		setUnitsPending(TotalUnitsPending);

		TextView txtFirst = (TextView) myView.findViewById(R.id.first);
		txtFirst.setText(strInitDate);
		TextView txtLast = (TextView) myView.findViewById(R.id.last);
		txtLast.setText(strLastDate);
	}
}
