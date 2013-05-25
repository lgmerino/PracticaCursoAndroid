package com.example.cargaterminal.JSONUtil;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;


public class JSONUtilities{
	
	private Context mCtx;
	
	public JSONUtilities(Context context){
		mCtx = context;	
	}

	public String getJSONFromAssets(String resource){
			
		// Obtenemos el JSON, de un archivo en assets 
		
		String json = null;
	    try {
	        InputStream is = mCtx.getAssets().open(resource);
	
	        int size = is.available();
	        byte[] buffer = new byte[size];
	        
	        is.read(buffer);
	        is.close();
	
	        json = new String(buffer, "UTF-8");
	        
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return null;
	    }
	    
	    return json;
	}

}