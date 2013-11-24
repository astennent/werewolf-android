package com.example.werewolf;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateAccountActivity extends WerewolfActivity {

	@SuppressLint("HandlerLeak") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_account, menu);
		return true;
	}
	
	private void launchCreateAccount() {
		// Parse and encode the user's input.
		//TODO: First Name Last Name Email
		EditText userText = (EditText) findViewById(R.id.lblUsername);
		String username = userText.getText().toString();
		EditText passText = (EditText) findViewById(R.id.txtPassword);
		String password = passText.getText().toString();
		EditText passText2 = (EditText) findViewById(R.id.txtConfirmPassword);
		String password2 = passText2.getText().toString();
		
		if (!password.equals(password2)){
			setErrorMessage("Passords do not match.");
			return;
		} else if (password.equals("") || password == null){
			setErrorMessage("Enter a password");
			return;
		}		
		
		SaveAccountInfo(username, password);
		
		new AsyncJSONParser(this).execute(WerewolfUrls.CREATE_ACCOUNT);
		setProgressBarEnabled(true);
		setErrorMessage("");
	}

}
