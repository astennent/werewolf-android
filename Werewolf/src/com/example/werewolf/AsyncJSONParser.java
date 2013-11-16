/*
 * Modified code.
 * Author: Raj Amal
 * Source: https://www.learn2crack.com/2013/10/android-json-parsing-url-example.html
 */

package com.example.werewolf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncJSONParser extends AsyncTask<String, Void, JSONObject> {

	static InputStream is = null;
	public JSONObject jObj = null;
	public String json = "";
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private WerewolfActivity caller;

	// default constructor, includes caller for preferences access
	public AsyncJSONParser(WerewolfActivity caller) {
		this.caller = caller;
	}
	
	// convenience constructor which pre-populates params with a single value
	public AsyncJSONParser(WerewolfActivity caller, String name, String value) {
		this.caller = caller;
		addParameter(name, value);
	}

	@Override
	protected JSONObject doInBackground(String... urls) {

		String url = urls[0];
		String function = null; //extra information for the callback
		
		if (urls.length > 1) {
			function = urls[1];
		}
		
		//Get the user's information from preferences
		SharedPreferences pref = caller.getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		String auth = pref.getString("HTTP_AUTHORIZATION", null);
		addParameter("HTTP_AUTHORIZATION", auth);	
		

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();

			// create post request and populate it
			HttpPost httpPost = new HttpPost(url);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
					"UTF-8");
			httpPost.setEntity(entity);

			// make the query to the server
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		Log.v("JSON Parser", jObj.toString());

		// return JSON String with a function call to the activity
		try {
			caller.onPostComplete(jObj, function);
		} catch (JSONException e) {
			Log.e("JSON Parser", jObj.toString());
			Log.e("JSON Parser", e.getMessage());
		}
		
		//Dummy return isn't used.
		return null;
	}
	
	


	// Methods for setting the parameters of the post request.
	public void addParameter(String name, String value) {
		params.add(new WerewolfNVP(name, value));
	}	
	public void setParams(List<NameValuePair> params){
		this.params = params;
	}

}