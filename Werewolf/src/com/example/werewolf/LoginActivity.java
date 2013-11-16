package com.example.werewolf;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	@Override
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

	}

	public void login() {
		EditText userText = (EditText) findViewById(R.id.txtUsername);
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
		AsyncJSONParser jp = new AsyncJSONParser("HTTP_AUTHORIZATION", base64);
		Log.v("Login", WerewolfUrls.PING);
		new AsyncJSONParser().execute(WerewolfUrls.PING);
		Log.v("Login", jp.json);
		/*
		 * Intent intent = new Intent(this, DisplayMessageActivity.class);
		 * EditText editText = (EditText) findViewById(R.id.edit_message);
		 * String message = editText.getText().toString();
		 * intent.putExtra(EXTRA_MESSAGE, message); startActivity(intent);
		 * finish();
		 */
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
