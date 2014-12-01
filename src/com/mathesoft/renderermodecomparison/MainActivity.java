package com.mathesoft.renderermodecomparison;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mathesoft.renderermodecomparison.canvas2dempty.Canvas2DSdkEmptyActivity;
import com.mathesoft.renderermodecomparison.canvas2dnoinheritance.Canvas2DNoInheritanceActivity;
import com.mathesoft.renderermodecomparison.canvas2dsdk.Canvas2DSdkActivity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		String title = getString(R.string.title);
		TextView tvTitle = (TextView) findViewById(R.id.tvTitle);

		title += ": " + Settings.framesToRender + " frames " + Settings.spritesToRender + " sprites";
		tvTitle.setText(title);

	}

	public void btCanvas2D_no_inheritance_clicked(View view) {
		Log.d(getClass().getSimpleName(), "btCanvas2D_No_inheritance_clicked");
		Intent intent = new Intent(this, Canvas2DNoInheritanceActivity.class);
		startActivity(intent);
	}

	public void btCanvas2D_sdk_clicked(View view) {
		Log.d(getClass().getSimpleName(), "btCanvas2D_Sdk_clicked");
		Intent intent = new Intent(this, Canvas2DSdkActivity.class);
		startActivity(intent);
	}

	public void btCanvas2D_empty_clicked(View view) {
		Log.d(getClass().getSimpleName(), "btCanvas2D_Empty_clicked");
		Intent intent = new Intent(this, Canvas2DSdkEmptyActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadResults();
	}

	private void loadResults() {
		Context appContext = getApplicationContext();
		SharedPreferences sharedPreferences = appContext.getSharedPreferences(Settings.SHARED_REFERENCES_NAME, MODE_PRIVATE);

		// 2D No inheritance
		long canvas2NoInheritanceResultMillisec = sharedPreferences.getLong(Settings.PREF_KEY_CANVAS2D_NO_INHERITANCE_MILLISEC, -1L);
		int canvas2DNoInheritanceResultFps = sharedPreferences.getInt(Settings.PREF_KEY_CANVAS2D_NO_INHERITANCE_FPS, -1);
		TextView tvCanvas2DNoInheritanceResult = (TextView) findViewById(R.id.tvCanvas2DNoInheritanceResult);
		displayResultFor(canvas2NoInheritanceResultMillisec, canvas2DNoInheritanceResultFps, tvCanvas2DNoInheritanceResult);

		// 2D SDK
		long canvas2DSdkResultMillisec = sharedPreferences.getLong(Settings.PREF_KEY_CANVAS2DSDK_MILLISEC, -1L);
		int canvas2DSdkResultFps = sharedPreferences.getInt(Settings.PREF_KEY_CANVAS2DSDK_FPS, -1);
		TextView tvCanvas2DSdkResult = (TextView) findViewById(R.id.tvCanvas2DSdkResult);
		displayResultFor(canvas2DSdkResultMillisec, canvas2DSdkResultFps, tvCanvas2DSdkResult);

		// 2D Empty
		long canvas2DEmptyResultMillisec = sharedPreferences.getLong(Settings.PREF_KEY_CANVAS2DSDK_EMPTY_MILLISEC, -1L);
		int canvas2DEmptyResultFps = sharedPreferences.getInt(Settings.PREF_KEY_CANVAS2DSDK_EMPTY_FPS, -1);
		TextView tvCanvas2DEmptyResult = (TextView) findViewById(R.id.tvCanvas2DEmptyResult);
		displayResultFor(canvas2DEmptyResultMillisec, canvas2DEmptyResultFps, tvCanvas2DEmptyResult);

	}

	private void displayResultFor(long resultMillisec, int fps, TextView tvResult) {
		String result = getString(R.string.Result);
		tvResult.setText(result);
		if (resultMillisec != -1L) {
			int millisec = (int) (resultMillisec % 1000);
			int seconds = (int) (resultMillisec / 1000);
			int minutes = (int) (resultMillisec / (60 * 1000));

			StringBuilder sb = new StringBuilder();
			sb.append(minutes).append(" min, ");
			sb.append(seconds).append(" sec, ");
			sb.append(millisec).append(" millisec");

			tvResult.setText(result + " " + sb.toString() + ", fps: " + fps);
		} else {
			tvResult.setText(result + " Please run the test");
		}

	}

}
