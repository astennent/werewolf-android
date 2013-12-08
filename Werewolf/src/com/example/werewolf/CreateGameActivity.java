package com.example.werewolf;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGameActivity extends WerewolfActivity {
	EditText nameText, cycleText, killText, scentText;
	String defaultName;
	
	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);
		
		final Button btnCreateAccount = (Button) findViewById(R.id.btnCreateGame);
		btnCreateAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchCreateGame();
			}
		});
		
		setProgressBarEnabled(false);
		setErrorMessage("");
		
		nameText = (EditText) findViewById(R.id.txtGameName);
		cycleText = (EditText) findViewById(R.id.txtCycleLength);
		scentText = (EditText) findViewById(R.id.txtScentRange);
		killText = (EditText) findViewById(R.id.txtKillRange);	
		
		//Set the default name to username + semi-arbitrary number
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		defaultName = pref.getString("username", "User") + 
				(""+System.currentTimeMillis()).substring(8);
		nameText.setHint("Name (" + defaultName + ")");
		
	
		
		mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	        	JSONObject json = (JSONObject) msg.obj;
				setProgressBarEnabled(false);
	    		try {
	    			String m = json.getString("message");
					if (m.equals("success")){
						alertGameMade();
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
	
	private void launchCreateGame() {
		// Parse and encode the user's input.
		String name = nameText.getText().toString();
		String cycle = cycleText.getText().toString();
		String scent = scentText.getText().toString();
		String kill = killText.getText().toString();
		
		if (name.equals("")){
			name = defaultName;
		} 
		if (cycle.equals("")) {
			cycle = "720";
		} 
		if (scent.equals("")) {
			scent = "1.0";
		}
		if (kill.equals("")) {
			kill = ".5";
		}
		
			
		AsyncJSONParser pewpew = new AsyncJSONParser(this);
		pewpew.addParameter("name", name);
		pewpew.addParameter("cycle_length", cycle);
		pewpew.addParameter("scent_range", scent);
		pewpew.addParameter("kill_range", kill);
		pewpew.execute(WerewolfUrls.CREATE_GAME);
		
		setProgressBarEnabled(true);
		setErrorMessage("");
	}
	
	private void alertGameMade(){
		Context context = getApplicationContext();
		CharSequence text = "Created game";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		finish();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_game, menu);
		return true;
	}

}
