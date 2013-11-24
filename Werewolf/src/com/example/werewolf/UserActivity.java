package com.example.werewolf;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class UserActivity extends WerewolfActivity {

	@SuppressLint("HandlerLeak") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		refresh();
		
		mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
				setProgressBarEnabled(false);
	        	JSONObject json = (JSONObject) msg.obj;
	    		try {
	    			String m = json.getString("message");
					if (m.equals("success")){
						updatePage(json);
					} else {
			    		setErrorMessage(m);
					}
				} catch (JSONException e) {
					Log.e("UserActivity", e.getMessage());
					setErrorMessage("Server Error");
				}

	        }
		};

	}
	
	private void refresh(){
		new AsyncJSONParser(this).execute(WerewolfUrls.GET_ACCOUNT_INFO);		
		setProgressBarEnabled(true);	
		setErrorMessage("");
	}
	
	private void updatePage(JSONObject json) {

		TextView userText = (TextView) findViewById(R.id.lblUsername);
		
		//Get the user's information from preferences
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		String username = pref.getString("username", "User");
		
		userText.setText(username);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}

}
