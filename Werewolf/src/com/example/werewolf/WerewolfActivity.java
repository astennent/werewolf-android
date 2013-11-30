package com.example.werewolf;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class WerewolfActivity extends Activity{
	Handler mHandler;

	public void setErrorMessage(String message){
		TextView error = (TextView) findViewById(R.id.lblError);
		int visibility;
		if (message.length() > 0) {
			visibility = View.VISIBLE;
		} else {
			visibility = View.INVISIBLE;
		}
		error.setVisibility(visibility);
		error.setText(message);
	}
	
	
	public void setProgressBarEnabled(boolean enabled) {
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setEnabled(enabled);
	}
	
	/* 
	 * Used by both CreateAccount and Login
	 */
	public void launchUserActivity() {
		Intent intent = new Intent(this, UserActivity.class);
		startActivity(intent);
		finish();
	}

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	//Save the input in preferences, where it will be used by the AsyncJSONParser
	public void SaveAccountInfo(String username, String password){				
		String text = username + ":" + password;

		byte[] data = null;
		try {
			data = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String base64 = Base64.encodeToString(data, Base64.DEFAULT);		
		
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		Editor editor = pref.edit();
		editor.putString("HTTP_AUTHORIZATION", base64);
		editor.putString("username", username);
		editor.commit();
	}
	
	public void SignOut(){
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		Editor editor = pref.edit();
		editor.remove("HTTP_AUTHORIZATION");
		editor.remove("username");
		editor.commit();
		launchLoginActivity();
	}
	
	/* 
	 * Used when Logging out
	 */
	public void launchLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.signout:
	            SignOut();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
