package com.example.werewolf;

import android.app.Activity;
import android.os.Handler;
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
		int visibility;
		if (enabled) {
			visibility = View.VISIBLE;
		} else {
			visibility = View.INVISIBLE;
		}
		progressBar.setVisibility(visibility);
	}

}
