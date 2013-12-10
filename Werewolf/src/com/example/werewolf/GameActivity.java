package com.example.werewolf;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class GameActivity extends WerewolfActivity {

	TextView villagerCount;
	TextView wolfCount;
	TextView remainingTime;
	TextView dayOrNight;
	TextView gameName;
	SeekBar seekBar;
	LinearLayout actionList;
	LayoutParams lp;

	final int VOTE = 0;
	final int KILL = 1;
	final int SMELL = 2;
	final int LABEL = 3;

	int gameId; // passed in from lobby

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		Intent mIntent = getIntent();
		gameId = mIntent.getIntExtra("gameId", 0);
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

		villagerCount = (TextView) findViewById(R.id.lblVillagerCount);
		wolfCount = (TextView) findViewById(R.id.lblWolfCount);
		remainingTime = (TextView) findViewById(R.id.lblRemainingTime);
		dayOrNight = (TextView) findViewById(R.id.lblDayOrNight);
		gameName = (TextView) findViewById(R.id.lblGameName);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		seekBar.setEnabled(false);
		actionList = (LinearLayout) findViewById(R.id.actions);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

	}

	private void refresh() {
		AsyncJSONParser pewpew = new AsyncJSONParser(this);
		pewpew.addParameter("game_id", "" + gameId);
		pewpew.execute(WerewolfUrls.GET_GAME_INFO);
		setProgressBarEnabled(true);
		setErrorMessage("");
	}

	private void updatePage(JSONObject json) {

		try {
			// Update basic information.
			final String method = json.getString("method");
			if (method.equals("get_game_data")) {
				int numVillagers = json.getInt("num_villagers");
				int numWolves = json.getInt("num_wolves");
				double fraction = (double) numVillagers
						/ (numVillagers + numWolves);
				seekBar.setProgress((int) (fraction * seekBar.getMax()));
				villagerCount.setText("Villagers: " + numVillagers);
				wolfCount.setText("Wolves: " + numWolves);
				gameName.setText(json.getString("game_name"));
				boolean playerIsWolf = json.getBoolean("player_is_wolf");
				
				String wolfText;
				if (playerIsWolf)
					wolfText = " You are a wolf.";
				else{
					wolfText = " You are a villager.";
				}
				
				String nextCycle;
				boolean isDay = json.getBoolean("is_day");
				if (isDay) {
					dayOrNight.setText("It is day." + wolfText);
					nextCycle = "nightfall";
				} else {
					dayOrNight.setText("It is night." + wolfText);
					nextCycle = "dawn";
				}
				
				int minutesRemaining = json.getInt("minutes_remaining");
				if (minutesRemaining > 1) {
					remainingTime.setText("There are " + minutesRemaining
							+ " minutes left until " + nextCycle);
				} else if (minutesRemaining == 1) {
					remainingTime.setText("There is one minute left until "
							+ nextCycle);
				} else {
					remainingTime.setText("There is less than a minute until "
							+ nextCycle);
				}

				actionList.removeAllViews();
				if (isDay) {
					TextView votedView = new TextView(this);
					votedView.setTextColor(0xffeeeeee);
					JSONObject actions = json.getJSONObject("actions");
					if (actions.getBoolean("voted")) {
						votedView.setText("You voted for "
								+ actions.getString("your_vote")
								+ " but you can change your vote.");
					} else {
						votedView.setText("You have not yet voted");
					}
					actionList.addView(votedView, lp);
					JSONArray all_players = actions.getJSONArray("all_players");
					for (int i = 0; i < all_players.length(); i++) {
						JSONObject jsonPlayer = (JSONObject) all_players.get(i);
						int playerId = jsonPlayer.getInt("id");
						String playerName = jsonPlayer.getString("name");
						boolean isDead = jsonPlayer.getBoolean("is_dead");

						if (isDead) {
							playerName += " (Dead)";
						}

						int wolfIdentifier = jsonPlayer
								.getInt("wolf_identifier");

						addPlayerButtonToActions(VOTE, playerId, "Vote for "
								+ playerName, wolfIdentifier, isDead);
					}

				} else if (playerIsWolf) { // && isNight

					JSONObject actions = json.getJSONObject("actions");
					JSONArray all_players = actions.getJSONArray("all_players");
					JSONArray killable_players = actions
							.getJSONArray("killable_players");
					JSONArray smellable_players = actions
							.getJSONArray("all_players");

					ArrayList<JSONArray> lists = new ArrayList<JSONArray>();
					lists.add(killable_players);
					lists.add(smellable_players);
					lists.add(all_players);

					for (int listId = 0; listId < lists.size(); listId++) {
						JSONArray list = lists.get(listId);
						final int listNumber = listId;

						switch (listNumber) {
						case (0):
							TextView tvk = new TextView(this);
							tvk.setTextColor(0xffeeeeee);
							if (killable_players.length() > 0) {
								tvk.setText("Killable Villagers");
							} else {
								tvk.setText("No villagers in kill range.");
							}
							actionList.addView(tvk, lp);
							break;
						case (1):
							TextView tvs = new TextView(this);
							tvs.setTextColor(0xffeeeeee);
							if (killable_players.length() > 0) {
								tvs.setText("Smellable Villagers");
							} else {
								tvs.setText("No villagers in scent range.");
							}
							actionList.addView(tvs, lp);
							break;
						case (2):
							TextView tvl = new TextView(this);
							tvl.setTextColor(0xffeeeeee);
							tvl.setText("All Players");
							actionList.addView(tvl, lp);
						}

						for (int i = 0; i < list.length(); i++) {
							JSONObject jsonPlayer = (JSONObject) list.get(i);
							int playerId = jsonPlayer.getInt("id");
							boolean isDead = jsonPlayer.getBoolean("is_dead");
							String playerName = jsonPlayer.getString("name");
							int wolfIdentifier = jsonPlayer
									.getInt("wolf_identifier");
							if (isDead) {
								playerName += " (Dead)";
							}

							switch (listNumber) {
							case (0):
								addPlayerButtonToActions(KILL, playerId,
										"Kill " + playerName, wolfIdentifier,
										isDead);
								break;
							case (1):
								addPlayerButtonToActions(SMELL, playerId,
										"Hunt " + playerName, wolfIdentifier,
										isDead);
								break;
							case (2):
								addPlayerButtonToActions(LABEL, playerId,
										playerName, wolfIdentifier, isDead);
								break;
							}

						}
					}
				} else {
					TextView tv = new TextView(this);
					tv.setTextColor(0xffeeeeee);
					tv.setText("Don't Die!");
					actionList.addView(tv, lp);
				}
			} else if (method.equals("place_vote")) {
				makeToast("Vote cast");
				refresh(); // There's probably a less expensive way to do this.
			} else if (method.equals("smell")) {
				makeToast(json.getDouble("smell_distance")+" miles");
				refresh();
			} else if (method.equals("kill")) {
				makeToast("Killed " + json.getString("victim_name"));
				refresh();
			}

		} catch (JSONException e) {
			Log.e("GAMEACTIVTY", e.getMessage());
		}
	}

	private void addPlayerButtonToActions(int action, final int playerId,
			String text, int wolfIdentifier, boolean isDead) {
		Button b = new PlayerButton(this, text, wolfIdentifier, isDead);
		actionList.addView(b, lp);
		switch (action) {
		case VOTE:
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					voteForPlayer(playerId);
				}
			});
			break;
		case KILL:
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					killPlayer(playerId);
				}
			});
			break;
		case SMELL:
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					smellPlayer(playerId);
				}
			});
			break;

		} // end switch
	}

	private void voteForPlayer(int playerId) {
		AsyncJSONParser pewpew = new AsyncJSONParser(this);
		pewpew.addParameter("votee_id", "" + playerId);
		pewpew.addParameter("game_id", "" + gameId);
		pewpew.execute(WerewolfUrls.PLACE_VOTE);
		setProgressBarEnabled(true);
		setErrorMessage("");
	}

	private void killPlayer(int playerId) {
		AsyncJSONParser pewpew = new AsyncJSONParser(this);
		pewpew.addParameter("victim_id", "" + playerId);
		pewpew.addParameter("game_id", "" + gameId);
		pewpew.execute(WerewolfUrls.KILL);
		setProgressBarEnabled(true);
		setErrorMessage("");
	}

	private void smellPlayer(int playerId) {
		AsyncJSONParser pewpew = new AsyncJSONParser(this);
		pewpew.addParameter("victim_id", "" + playerId);
		pewpew.addParameter("game_id", "" + gameId);
		pewpew.execute(WerewolfUrls.SMELL);
		setProgressBarEnabled(true);
		setErrorMessage("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
