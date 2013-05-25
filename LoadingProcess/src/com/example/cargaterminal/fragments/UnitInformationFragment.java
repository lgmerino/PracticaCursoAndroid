package com.example.cargaterminal.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cargaterminal.ObjectUnit;
import com.example.cargaterminal.R;
import com.example.cargaterminal.R.id;
import com.example.cargaterminal.R.layout;

public class UnitInformationFragment extends Fragment 
{
	private View myView;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		myView = inflater.inflate(R.layout.fragment_unit_information, container, false);
		
		return myView;
	}

	//public void updateUnitInformation (ObjectUnit unit)
	public void updateUnitInformation (Cursor unit)
	{

		TextView txtBooking = (TextView) myView.findViewById(R.id.booking);
		txtBooking.setText(unit.getString(unit.getColumnIndexOrThrow("booking")));
		
		TextView txtCustomer = (TextView) myView.findViewById(R.id.customer);
		txtCustomer.setText(unit.getString(unit.getColumnIndexOrThrow("customer")));
		
		TextView txtMake = (TextView) myView.findViewById(R.id.make);
		txtMake.setText(unit.getString(unit.getColumnIndexOrThrow("make")));
		
		TextView txtModel = (TextView) myView.findViewById(R.id.model);
		txtModel.setText(unit.getString(unit.getColumnIndexOrThrow("model")));
		
		/*
		TextView txtLongitude = (TextView) myView.findViewById(R.id.logitude);
		txtLongitude.setText(unit.getString(unit.getColumnIndexOrThrow("longitude")));
		
		TextView txtHeight = (TextView) myView.findViewById(R.id.height);
		txtHeight.setText(unit.getString(unit.getColumnIndexOrThrow("height")));
		*/
		
		TextView txtPortOfDischarge = (TextView) myView.findViewById(R.id.port_of_discharge);
		txtPortOfDischarge.setText(unit.getString(unit.getColumnIndexOrThrow("port_of_discharge")));
	}
	
	public void resetUnitInformation ()
	{
		TextView txtBooking = (TextView) myView.findViewById(R.id.booking);
		txtBooking.setText("");
		
		TextView txtCustomer = (TextView) myView.findViewById(R.id.customer);
		txtCustomer.setText("");
		
		TextView txtMake = (TextView) myView.findViewById(R.id.make);
		txtMake.setText("");
		
		TextView txtModel = (TextView) myView.findViewById(R.id.model);
		txtModel.setText("");
		
		/*
		TextView txtLongitude = (TextView) myView.findViewById(R.id.logitude);
		txtLongitude.setText("");
		
		TextView txtHeight = (TextView) myView.findViewById(R.id.height);
		txtHeight.setText("");
		*/
		
		TextView txtPortOfDischarge = (TextView) myView.findViewById(R.id.port_of_discharge);
		txtPortOfDischarge.setText("");	
	}
}
