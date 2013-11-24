package com.example.werewolf;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends WerewolfActivity {
		
	@SuppressLint("HandlerLeak") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
  		final Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});

		final Button btnCreateAccount = (Button) findViewById(R.id.btnLaunchCreateAccount);
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
				setProgressBarEnabled(false);
	    		try {
	    			String m = json.getString("message");
					if (m.equals("success")){
						launchUserActivity();
					} else {
			    		setErrorMessage(m);
					}
				} catch (JSONException e) {
					Log.e("LoginActivity", e.getMessage());
					setErrorMessage("Server Error");
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

		SaveAccountInfo(username, password);
				
		new AsyncJSONParser(this).execute(WerewolfUrls.PING);
		setProgressBarEnabled(true);
		setErrorMessage("");
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
