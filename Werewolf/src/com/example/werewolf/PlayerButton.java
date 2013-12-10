package com.example.werewolf;

import android.content.Context;
import android.widget.Button;

public class PlayerButton extends Button {
	public PlayerButton(Context context, String text, final int wolfIdentifier,
			boolean isDead) {
		super(context);
		this.setText(text);
		this.setTextColor(0xffeeeeee);
		this.setEnabled(!isDead);

		switch (wolfIdentifier) {
		case (-1):
			this.setBackgroundColor(0xaa888888);
			break;
		case (0):
			this.setBackgroundColor(0xaa666699);
			break;
		case (1):
			this.setBackgroundColor(0xaa996666);
			break;
		}

	}
}
