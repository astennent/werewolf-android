package com.example.werewolf;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends WerewolfActivity {
		
	@SuppressLint("HandlerLeak") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		final Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});

		final Button btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
		btnCreateAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchCreateAccount();
			}
		});
		
		setProgressBarEnabled(false);
		setErrorMessage("");
		
		mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	        	JSONObject json = (JSONObject) msg.obj;
	    		try {
					if (json.getString("message").equals("success")){
						launchUserActivity();
					} else {
						setProgressBarEnabled(false);
			    		setErrorMessage("Incorrect Username or Password");
					}
				} catch (JSONException e) {
					Log.e("LoginActivity", e.getMessage());
				}

	        }
		};
		
	}

	public void login() {
		
		// Parse and encode the user's input.
		EditText userText = (EditText) findViewById(R.id.lblUsername);
		String username = userText.getText().toString();
		EditText passText = (EditText) findViewById(R.id.txtPassword);
		String password = passText.getText().toString();
		String text = username + ":" + password;
		byte[] data = null;
		try {
			data = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String base64 = Base64.encodeToString(data, Base64.DEFAULT);
		
		
		//Save the input in preferences, where it will be used by the AsyncJSONParser
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		Editor editor = pref.edit();
		editor.putString("HTTP_AUTHORIZATION", base64);
		editor.putString("username", username);
		editor.commit();
		
		
		Log.v("Login", WerewolfUrls.PING);
		new AsyncJSONParser(this).execute(WerewolfUrls.PING);
		setProgressBarEnabled(true);
		setErrorMessage("");
	}	
	
	
	public void launchUserActivity() {
		Intent intent = new Intent(this, UserActivity.class);
		startActivity(intent);
		finish();
	}
	

	public void launchCreateAccount() {
		Intent intent = new Intent(this, CreateAccountActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	

}
