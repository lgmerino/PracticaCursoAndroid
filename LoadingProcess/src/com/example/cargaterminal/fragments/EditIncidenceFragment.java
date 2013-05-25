package com.example.cargaterminal.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.cargaterminal.R;

public class EditIncidenceFragment extends Fragment 
{
	private final String TAG = getClass().getSimpleName();
	private View myView;
	private EditText txtIncidence = null;
	private Button btnSave = null;
	private Button btnCancel = null;
	private EditIncidenceInterface listener;
	
	// Interface
	public interface EditIncidenceInterface{
		public void pushIncidenceSave(String incidence);
		public void pushIncidenceCancel();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		if (activity instanceof EditIncidenceInterface) {
			listener = (EditIncidenceInterface) activity;
		}
	}
	
	public EditIncidenceFragment() {}
	
	public static EditIncidenceFragment newInstance(String _incidence) {
		
		Log.d("EditIncidenceFragment", "newInstance: ENTER");
		
		EditIncidenceFragment f = new EditIncidenceFragment();
		Bundle args = new Bundle();
		args.putString("incidence", _incidence);
		f.setArguments(args);
		
		Log.d("EditIncidenceFragment", "newInstance: EXIT");
		
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		myView = inflater.inflate(R.layout.fragment_edit_incidence, container, false);
		
		txtIncidence = (EditText) myView.findViewById(R.id.incidence);
		txtIncidence.setText(getArguments().getString("incidence"));

		btnSave = (Button) myView.findViewById(R.id.btnSave);
		btnCancel = (Button) myView.findViewById(R.id.btnCancel);

		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.pushIncidenceSave(txtIncidence.getText().toString());
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.pushIncidenceCancel();
			}
		});
		
		return myView;
	}	
}
