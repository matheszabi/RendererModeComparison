package com.mathesoft.renderermodecomparison.game;

import com.mathesoft.renderermodecomparison.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public abstract class AbstractGame2DActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		initContentView();
	}
	
	// it should be overridden:
	protected abstract void initContentView();// setContentView(new AbstractGameView(this));
	

	public abstract void saveResultAndQuit(long millisecsElapsed);
	
	protected void saveResultAndQuitBasic(long millisecsElapsed, String prefKeyMillisec, String prefKeyFps)
	{
		float secondsElapsed = (int) (millisecsElapsed / 1000);
		int fps = (int) (Settings.framesToRender / secondsElapsed);
		
		Context appContext = getApplicationContext();
		SharedPreferences sharedPreferences = appContext.getSharedPreferences(Settings.SHARED_REFERENCES_NAME, MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putLong(prefKeyMillisec, millisecsElapsed);
		editor.putInt(prefKeyFps, fps);
		editor.commit();


		final AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Result: " + fps + " fps")
				.setMessage("Frames rendered: " + Settings.framesToRender + "\nRenderer time: " + (int) secondsElapsed + " seconds")
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// continue with delete
						dialog.dismiss();
						// destroy the activity:
						finish();
					}
				})

				.setIcon(android.R.drawable.ic_dialog_alert);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				builder.show();
			}
		});

	}
	
}
