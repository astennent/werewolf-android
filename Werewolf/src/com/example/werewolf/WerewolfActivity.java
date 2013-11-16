package com.example.werewolf;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

public abstract class WerewolfActivity extends Activity{
	public void onPostComplete (JSONObject json, String method) throws JSONException{
		//TO BE CALLED ON COMPLETION OF AsyncJSONParser
		//IMPLEMENTATION CHANGES PER ACTIVITY
	}
}
