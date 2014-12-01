package com.mathesoft.renderermodecomparison.canvas2dnoinheritance;

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

public class Canvas2DNoInheritanceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(new GameView(this));
	}

	public void saveResultAndQuit(long millisecsElapsed) {

		float secondsElapsed = (int) (millisecsElapsed / 1000);
		int fps = (int) (Settings.framesToRender / secondsElapsed);

		Context appContext = getApplicationContext();
		SharedPreferences sharedPreferences = appContext.getSharedPreferences(Settings.SHARED_REFERENCES_NAME, MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putLong(Settings.PREF_KEY_CANVAS2D_NO_INHERITANCE_MILLISEC, millisecsElapsed);
		editor.putInt(Settings.PREF_KEY_CANVAS2D_NO_INHERITANCE_FPS, fps);
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
