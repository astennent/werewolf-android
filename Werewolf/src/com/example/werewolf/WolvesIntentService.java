package com.example.werewolf;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WolvesIntentService extends IntentService {

	static int WOLF_PING_INTERVAL = 5; // seconds
	LocationManager mlocManager = null;
	LocationListener mlocListener;
	public Handler mHandler;

	@SuppressLint("HandlerLeak")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				JSONObject json = (JSONObject) msg.obj;
				try {
					String m = json.getString("message");
					if (m.equals("success")) {
						Log.v("SERVICE", "Success");
					} else {
						Log.v("SERVICE", "NOSuccess");
					}
				} catch (JSONException e) {
					Log.e("INTENTSERVICE", e.getMessage());
				}

			}
		};

		return super.onStartCommand(intent, flags, startId);
	}

	public WolvesIntentService() {
		super("WolvesIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		while (true) {
			synchronized (this) {
				try {
					wait(WolvesIntentService.WOLF_PING_INTERVAL * 1000); //
					mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					if (mlocManager
							.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

						Location lastKnownLoc = mlocManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);

						double latitude = lastKnownLoc.getLatitude();
						double longitude = lastKnownLoc.getLongitude();
						Log.v("LAT/LONG", "" + lastKnownLoc);
						postCoords(latitude, longitude);
					}

				} catch (Exception e) {
				}
			}
		}

	}

	private void postCoords(double latitude, double longitude) {
		AsyncJSONParser pewpew = new AsyncJSONParser(this);
		pewpew.addParameter("latitude", "" + latitude);
		pewpew.addParameter("longitude", "" + longitude);
		pewpew.execute(WerewolfUrls.POST_POSITION);
	}

}
