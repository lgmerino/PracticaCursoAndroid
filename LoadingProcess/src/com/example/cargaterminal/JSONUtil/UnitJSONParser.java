package com.example.cargaterminal.JSONUtil;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.cargaterminal.ObjectUnit;

import android.util.Log;


public class UnitJSONParser{
	
	private String mJsonString;
	
	public UnitJSONParser(String str){
		super();
		
		if(str==null){
			throw new NullPointerException();
		}
		
		mJsonString = str;		
	}
	
	public ArrayList<ObjectUnit> parser(){
		
		ArrayList<ObjectUnit> resArray = new ArrayList<ObjectUnit>();
		
		try {
			JSONObject root = new JSONObject(mJsonString);
			
			if(root.has("results")){
				JSONArray array = root.getJSONArray("results");

				for(int i=0; i<array.length();i++){
					JSONObject node = array.getJSONObject(i);
					ObjectUnit item = parseNode(node);
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
	
	private ObjectUnit parseNode(JSONObject node){
		ObjectUnit item = new ObjectUnit();
		
		try{
			if(node.has("booking"))
				item.setmBooking(node.getString("booking"));
				
			if(node.has("customer")){
				item.setmCustomer(node.getString("customer"));
			}
			
			if(node.has("vin")){
				item.setmVIN(node.getString("vin"));
			}
			
			if(node.has("make")){
				item.setmMake(node.getString("make"));
			}
			
			if(node.has("model")){
				item.setmModel(node.getString("model"));
			}
			
			if(node.has("dr_number")){
				item.setmDRNumber(node.getInt("dr_number"));
			}
			
			return item;
			
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
	}	
}