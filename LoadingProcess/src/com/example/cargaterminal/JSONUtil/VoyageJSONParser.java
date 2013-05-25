package com.example.cargaterminal.JSONUtil;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.cargaterminal.ObjectVoyage;

import android.util.Log;


public class VoyageJSONParser{
	
	private String mJsonString;
	
	public VoyageJSONParser(String str){
		super();
		
		if(str==null){
			throw new NullPointerException();
		}
		
		mJsonString = str;		
	}
	
	public ArrayList<ObjectVoyage> parser(){
		
		ArrayList<ObjectVoyage> resArray = new ArrayList<ObjectVoyage>();
		
		try {
			JSONObject root = new JSONObject(mJsonString);
			
			if(root.has("results")){
				JSONArray array = root.getJSONArray("results");

				for(int i=0; i<array.length();i++){
					JSONObject node = array.getJSONObject(i);
					ObjectVoyage item = parseNode(node);
					if(item!=null){
						resArray.add(item);
					}
				}
			}
			else{
				return null;
			}
			
			return resArray;
			
		} catch(JSONException e){
			e.printStackTrace();
			return null;
		}
	}
	
	private ObjectVoyage parseNode(JSONObject node){
		ObjectVoyage item = new ObjectVoyage();
		
		try{
			if(node.has("voyage"))
				item.setmVoyage(node.getString("voyage"));
				
			if(node.has("date")){
				item.setmDate(node.getString("date"));
				//2013-03-08 18:23:32.534878
				//DateFormat df = new SimpleDateFormat("yyyy-MM-dd SS:SS:SS.SSSSSS", Locale.ENGLISH);
				//try {
					//item.setmDate(df.parse(node.getString("date")));
				//} catch (ParseException e){
				//	e.printStackTrace();
				//}
			}
			
			if(node.has("units_to_load")){
				item.setmUnitsToLoad(node.getInt("units_to_load"));
			}
			
			if(node.has("loading_leg")){
				item.setmLoadingLeg(node.getInt("loading_leg"));
			}
			
			return item;
			
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
	}	
}