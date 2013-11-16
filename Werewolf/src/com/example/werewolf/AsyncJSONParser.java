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

import android.os.AsyncTask;
import android.util.Log;

public class AsyncJSONParser extends AsyncTask<String, Void, JSONObject> {

	static InputStream is = null;
	public JSONObject jObj = null;
	public String json = "";
	private List<NameValuePair> params = new ArrayList<NameValuePair>();

	// default constructor
	public AsyncJSONParser() {
	}

	@Override
	protected JSONObject doInBackground(String... urls) {

		String url = urls[0];

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

		// return JSON String
		return jObj;
	}

	// convenience constructor which prepopulates params with a single value
	public AsyncJSONParser(String name, String value) {
		addParameter(name, value);
	}

	public void addParameter(String name, String value) {
		params.add(new WerewolfNVP(name, value));
	}

}