package com.example.cargaterminal.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cargaterminal.ObjectUnit;
import com.example.cargaterminal.R;
import com.example.cargaterminal.R.id;
import com.example.cargaterminal.R.layout;

public class UnitInformationVerticalFragment extends Fragment 
{
	private View myView;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);	
	}
	
	public UnitInformationVerticalFragment() {}
	
	public static UnitInformationVerticalFragment newInstance(Cursor _unit) {

		Log.d("UnitInformationVerticalFragment", "newInstance: ENTER");

		UnitInformationVerticalFragment f = new UnitInformationVerticalFragment();
		
		Bundle args = new Bundle();
	
		args.putString("booking", _unit.getString(_unit.getColumnIndexOrThrow("booking")));
		f.setArguments(args);
		args.putString("customer", _unit.getString(_unit.getColumnIndexOrThrow("customer")));
		f.setArguments(args);
		args.putString("vin", _unit.getString(_unit.getColumnIndexOrThrow("vin")));
		f.setArguments(args);
		args.putString("make", _unit.getString(_unit.getColumnIndexOrThrow("make")));
		f.setArguments(args);
		args.putString("model", _unit.getString(_unit.getColumnIndexOrThrow("model")));
		f.setArguments(args);
		args.putString("dr_number", _unit.getString(_unit.getColumnIndexOrThrow("dr_number")));
		f.setArguments(args);
		args.putString("longitude", _unit.getString(_unit.getColumnIndexOrThrow("longitude")));
		f.setArguments(args);
		args.putString("width", _unit.getString(_unit.getColumnIndexOrThrow("width")));
		f.setArguments(args);
		args.putString("height", _unit.getString(_unit.getColumnIndexOrThrow("height")));
		f.setArguments(args);
		args.putString("weight", _unit.getString(_unit.getColumnIndexOrThrow("weight")));
		f.setArguments(args);
		args.putString("port_of_discharge", _unit.getString(_unit.getColumnIndexOrThrow("port_of_discharge")));
		f.setArguments(args);
		
		Log.d("UnitInformationVerticalFragment", "newInstance: EXIT");
		
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		myView = inflater.inflate(R.layout.fragment_unit_information_vertical, container, false);
		
		//TextView txtDRNumber = (TextView) myView.findViewById(R.id.dr_number);
		//txtDRNumber.setText(getArguments().getString("dr_number"));
		
		TextView txtVIN = (TextView) myView.findViewById(R.id.vin_dr);
		txtVIN.setText(getArguments().getString("vin") + " (" + getArguments().getString("dr_number") + ")");
		
		TextView txtBooking = (TextView) myView.findViewById(R.id.booking);
		txtBooking.setText(getArguments().getString("booking"));
		
		TextView txtCustomer = (TextView) myView.findViewById(R.id.customer);
		txtCustomer.setText(getArguments().getString("customer"));
		
		TextView txtMake = (TextView) myView.findViewById(R.id.make);
		txtMake.setText(getArguments().getString("make"));
		
		TextView txtModel = (TextView) myView.findViewById(R.id.model);
		txtModel.setText(getArguments().getString("model"));
		
		TextView txtLongitude = (TextView) myView.findViewById(R.id.longitude);
		txtLongitude.setText(getArguments().getString("longitude"));
		
		TextView txtWidth = (TextView) myView.findViewById(R.id.width);
		txtWidth.setText(getArguments().getString("width"));
		
		TextView txtWeight = (TextView) myView.findViewById(R.id.weight);
		txtWeight.setText(getArguments().getString("weight"));
		
		TextView txtHeight = (TextView) myView.findViewById(R.id.height);
		txtHeight.setText(getArguments().getString("height"));
		
		TextView txtPortOfDischarge = (TextView) myView.findViewById(R.id.port_of_discharge);
		txtPortOfDischarge.setText(getArguments().getString("port_of_discharge"));

		return myView;
	}

}
