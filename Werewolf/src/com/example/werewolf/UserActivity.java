package com.example.werewolf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.werewolf.WerewolfBadges.Badge;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserActivity extends WerewolfActivity {

	private int previousBadge = 0;
	private ImageButton[] badges = new ImageButton[9];
	private boolean[] obtainedBadges = new boolean[9];
	private TextView badgeDescription;
	private TextView badgeName;
	private TextView badgePoints;
	private TextView experiencePoints;

	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user);

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
		
		experiencePoints = (TextView) findViewById(R.id.lblExperience);

		initBadges();
		
		Button btnGames = (Button) findViewById(R.id.btnGames);
		btnGames.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchGamesLobby();
			}
		});	


	}
	
	

	private void clickBadge(int badgeNumber) {
		if (badgeNumber == previousBadge) {
			setButtonImage(previousBadge, false);
			previousBadge = 0;
		} else {
			if (previousBadge != 0) {
				setButtonImage(previousBadge, false);
			}
			setButtonImage(badgeNumber, true);
			previousBadge = badgeNumber;
		}
	}

	private void setButtonImage(int badgeNumber, boolean selected) {
		ImageButton badgeButton = badges[badgeNumber - 1];
		boolean obtained = obtainedBadges[badgeNumber - 1];
		if (selected) {
			if (obtained) {
				badgeButton.setImageResource(R.drawable.coin_selected);
			} else {
				badgeButton.setImageResource(R.drawable.uncoin_selected);
			}
			Badge badge = WerewolfBadges.getBadge(badgeNumber);
			badgeName.setText(badge.name + " (" + badge.points + ")");
			badgeDescription.setText(badge.description);
		} else {
			if (obtained) {
				badgeButton.setImageResource(R.drawable.coin);
			} else {
				badgeButton.setImageResource(R.drawable.uncoin);
			}
			badgeName.setText("");
			badgeDescription.setText("");
		}
	}

	private void refresh() {
		new AsyncJSONParser(this).execute(WerewolfUrls.GET_ACCOUNT_INFO);
		setProgressBarEnabled(true);
		setErrorMessage("");
	}

	private void updatePage(JSONObject json) {

		TextView userText = (TextView) findViewById(R.id.lblUsername);

		// Get the user's information from preferences
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"MyPref", 0); // 0 - for private mode
		String username = pref.getString("username", "User");
		userText.setText(username);

		// Parse the badges value in the query to determine which badgees to
		// light up.
		try {
			JSONArray a = json.getJSONArray("badges");
			for (int i = 0; i < a.length(); i++) {
				JSONObject jsonBadge = (JSONObject) a.get(i);
				int badgeID = jsonBadge.getInt("tag");
				obtainedBadges[badgeID] = true;
			}

			int points = 0;
			// Calculate badge points and reset selection.
			for (int i = 0; i < 9; i++) {
				setButtonImage(i + 1, false); // clear selection
				if (obtainedBadges[i]) {
					points += WerewolfBadges.getBadge(i + 1).points;
				}
			}

			badgePoints.setText("Total Badge Points: " + points);
			experiencePoints
					.setText("Experience: " + json.getInt("experience"));

		} catch (JSONException e) {
			// TODO: handle invalid badges
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}

	private void initBadges() {
		badgeName = (TextView) findViewById(R.id.lblBadgeName);
		badgeDescription = (TextView) findViewById(R.id.lblBadgeDescription);
		badgePoints = (TextView) findViewById(R.id.lblBadgePoints);
		

		badges[0] = (ImageButton) findViewById(R.id.badge1);
		badges[0].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickBadge(1);
			}
		});

		badges[1] = (ImageButton) findViewById(R.id.badge2);
		badges[1].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickBadge(2);
			}
		});

		badges[2] = (ImageButton) findViewById(R.id.badge3);
		badges[2].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickBadge(3);
			}
		});

		badges[3] = (ImageButton) findViewById(R.id.badge4);
		badges[3].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickBadge(4);
			}
		});

		badges[4] = (ImageButton) findViewById(R.id.badge5);
		badges[4].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickBadge(5);
			}
		});

		badges[5] = (ImageButton) findViewById(R.id.badge6);
		badges[5].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickBadge(6);
			}
		});

		badges[6] = (ImageButton) findViewById(R.id.badge7);
		badges[6].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickBadge(7);
			}
		});

		badges[7] = (ImageButton) findViewById(R.id.badge8);
		badges[7].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickBadge(8);
			}
		});

		badges[8] = (ImageButton) findViewById(R.id.badge9);
		badges[8].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickBadge(9);
			}
		});

	}
	
	public void launchGamesLobby() {
		Intent intent = new Intent(this, LobbyActivity.class);
		startActivity(intent);
	}
}
