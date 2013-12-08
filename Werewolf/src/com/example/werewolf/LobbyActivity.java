package com.example.werewolf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class LobbyActivity extends WerewolfActivity {
	
	LinearLayout buttonList;
	
	public void onRestart(){
		super.onRestart();
		refresh();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);
		
		refresh();
		
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				setProgressBarEnabled(false);
				JSONObject json = (JSONObject) msg.obj;
				try {
					String m = json.getString("message");
					if (m.equals("success")) {
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
		
		buttonList = (LinearLayout) findViewById(R.id.gameButtons);

		
		Button btnCreateGame = (Button) findViewById(R.id.btnCreateGame);
		btnCreateGame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchCreateGame();
			}
		});
		
				
		
		
		
	}
	
	public void refresh(){
		new AsyncJSONParser(this).execute(WerewolfUrls.GET_GAMES_INFO);
		setProgressBarEnabled(true);
		setErrorMessage("");
	}
	
	private void updatePage(JSONObject json) {
		clearGameList();
		try {
			JSONArray a = json.getJSONArray("players");
			for (int i = 0; i < a.length(); i++) {
				JSONObject jsonPlayer = (JSONObject) a.get(i);
				int gameId = jsonPlayer.getInt("game__id");
			    String gameName = jsonPlayer.getString("game__name");
			    boolean inProgress = jsonPlayer.getBoolean("game__in_progress");
			    boolean isWolf = jsonPlayer.getBoolean("is_wolf");
			    boolean isDead = jsonPlayer.getBoolean("is_dead");

				addGameToList(gameName, isWolf, inProgress, isDead);
			}


		} catch (JSONException e) {
			// TODO: handle invalid badges
		}

	}
	
	private void clearGameList(){
		buttonList.removeAllViews();
	}
	
	private void addGameToList(String gameName, boolean isWolf, boolean inProgress, boolean isDead){
        Button myButton = new Button(this);
        myButton.setText(gameName);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        buttonList.addView(myButton, lp);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lobby, menu);
		return true;
	}
	
	//Starts the new game activity.
	public void launchCreateGame() {
		Intent intent = new Intent(this, CreateGameActivity.class);
		startActivity(intent);
	}

}
