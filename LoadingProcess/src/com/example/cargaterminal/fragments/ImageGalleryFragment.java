package com.example.cargaterminal.fragments;

import java.util.ArrayList;

import com.example.cargaterminal.DatabaseHelper;
import com.example.cargaterminal.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageGalleryFragment extends Fragment{
	private final String TAG = getClass().getSimpleName();
	//private DatabaseHelper db = null;
	private View mView = null;
	private Gallery mGallery = null;
	private static ArrayList<String> mImageArray = null;
	ImageGalleryFragment f = null;
	private ImageUnitAdapter myAdapter = null;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	
	public ImageGalleryFragment() {}
	
	public static ImageGalleryFragment newInstance(Cursor _unit, String _fieldPhotoName) {
		ArrayList<String> imageArray = new ArrayList<String>();
		Log.d("ImageGalleryFragment", "newInstance: ENTER");
		
		imageArray = fillImageArray(_unit, _fieldPhotoName);
		
		ImageGalleryFragment f = new ImageGalleryFragment();
		Bundle args = new Bundle();
		args.putStringArrayList("imageArray", imageArray);
		f.setArguments(args);
		Log.d("ImageGalleryFragment", "newInstance: EXIT");
		
		return f;
	}
	
	private static ArrayList<String> fillImageArray(Cursor unit, String fieldPhotoName){
		Log.d("ImageGalleryFragment","fillImageArray: ENTER");
		ArrayList<String> imageArray = new ArrayList<String>();
	
		String strListPhoto = unit.getString(unit.getColumnIndexOrThrow(fieldPhotoName));
		Log.d("ImageGalleryFragment","fillImageArray. strListPhoto: " + strListPhoto);
		if((strListPhoto!=null)&&(strListPhoto!="")){
			String[] listPhotoName = strListPhoto.split(",");
			 for(int i=0; i < listPhotoName.length ; i++)
				 imageArray.add(listPhotoName[i]);
		}
		Log.d("ImageGalleryFragment","fillImageArray: ENTER");
		return(imageArray);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//db = DatabaseHelper.getInstance(getActivity());
		
		if(getArguments() != null)
			mImageArray = getArguments().getStringArrayList("imageArray");
		
		mView = inflater.inflate(R.layout.fragment_gallery, container, false);
		mGallery = (Gallery) mView.findViewById(R.id.my_gallery);
		
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		myAdapter = new ImageUnitAdapter(getActivity().getBaseContext());
		if(mImageArray!=null)
			mGallery.setAdapter(myAdapter);
		
	}
	
	@Override
	public void onDestroy() {

		super.onDestroy();
		//((CursorAdapter)getListAdapter()).getCursor().close();
		//db.close();
	}
	
	public void setPhotos(Cursor unit, String fieldPhotoName){
		mImageArray = fillImageArray(unit, fieldPhotoName);
		mGallery.setAdapter(myAdapter);
	}
	
	
	public void resetPhotos(){
		if(mImageArray!=null)
			mImageArray.clear();
	}
	
	public static class ImageUnitAdapter extends BaseAdapter 
	{
		private Context mContext;
		
		public ImageUnitAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return mImageArray.size();
		}

		@Override
		public Object getItem(int position) {
			return mImageArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			//Log.d("TAG", "position " + String.valueOf(position));
			View view;

			if (convertView == null) {
				// Make up a new view
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.item_image_gallery, null);
			} else {
				// Use convertView if it is available
				view = convertView;
			}
			
			ImageView image_view = (ImageView)view;
			image_view.setLayoutParams(new Gallery.LayoutParams(250,170));
			image_view.setScaleType(ImageView.ScaleType.FIT_XY);
			image_view.setImageURI(Uri.parse(mImageArray.get(position)));
			return view;
	
		}
	}
	
}