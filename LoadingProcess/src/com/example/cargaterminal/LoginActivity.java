package com.example.cargaterminal;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
		
	private SharedPreferences  mSharedPreferences;
	private EditText txtLogin;
	private Button btnLoad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		txtLogin =  (EditText) this.findViewById(R.id.txtLogin);
		btnLoad = (Button) this.findViewById(R.id.btnLogin);
		
		btnLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (txtLogin.getText().toString().length()>0){
					savePreference("user", txtLogin.getText().toString());
				}
				Intent intent = new Intent( LoginActivity.this, ListVoyageActivity.class);
    	    	startActivity(intent);
			}
		});
		
		setData();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setData(){
		String value = loadPreference("user");
		if (value!=null)
			txtLogin.setText(value);
		
        new MyAsyncTask().execute(null,null,null);
    }
	
	private void savePreference (String key, String value)
	{
		Editor editor = mSharedPreferences.edit();
		editor.putString(key,value);
		editor.commit();
	}
	
	private String loadPreference(String key)
	{
		return mSharedPreferences.getString(key, null);		
	}
	
	
	public class MyAsyncTask extends AsyncTask<Void, Void, Void>{

    	ProgressDialog pd = null;
    	String mQuote;
    	
    	protected void onPreExecute()
    	{
    		pd = ProgressDialog.show(LoginActivity.this, "Making request to http://iheartquotes.com", 
    											"Getting quote");
    	}
    	
    	@Override
    	protected Void doInBackground(Void... arg0) {
    		
    		String url_quote = "http://iheartquotes.com/api/v1/random?format=json&max_lines=4&max_characters=320";


			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = null;

			httpGet = new HttpGet(url_quote);
			HttpResponse response = null;
			try {
				response = httpclient.execute(httpGet);
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			HttpEntity entity = response.getEntity();

    		InputStream is=null;
			try {
				is = entity.getContent();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
    		BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8*1024);
			StringBuilder sb = new StringBuilder();
    		
    		String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			String content = sb.toString();
			try {
				JSONObject json = new JSONObject(content);
		        mQuote = json.getString("quote");
			} 
			catch (JSONException e)
	        {
				mQuote = "Error downloading quote...";
				return null;
	        }		

            return null;    		
    	}
    	
    	@Override
        protected void onPostExecute(Void unused)
        {
        	pd.dismiss();
        	Toast.makeText(LoginActivity.this, mQuote, Toast.LENGTH_LONG).show();
        }

    }
	
	
}