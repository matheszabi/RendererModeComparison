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
import com.mathesoft.renderermodecomparison.opengles10.OpenGLES10Activity;
import com.mathesoft.renderermodecomparison.opengles10empty.OpenGLES10EmptyActivity;

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

	public void btOpenGL10_clicked(View view) {
		Log.d(getClass().getSimpleName(), "btOpenGL10_clicked");
		Intent intent = new Intent(this, OpenGLES10Activity.class);
		startActivity(intent);
	}

	public void btOpenGL10Empty_clicked(View view) {
		Log.d(getClass().getSimpleName(), "btOpenGL10Empty_clicked");
		Intent intent = new Intent(this, OpenGLES10EmptyActivity.class);
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

		String[] millisecKeys = new String[] { Settings.PREF_KEY_CANVAS2D_NO_INHERITANCE_MILLISEC, Settings.PREF_KEY_CANVAS2DSDK_MILLISEC,
				Settings.PREF_KEY_CANVAS2DSDK_EMPTY_MILLISEC, Settings.PREF_KEY_OPENGL_10_MILLISEC, Settings.PREF_KEY_OPENGL_10_EMPTY_MILLISEC };
		
		String[] fpsKeys = new String[] { Settings.PREF_KEY_CANVAS2D_NO_INHERITANCE_FPS, Settings.PREF_KEY_CANVAS2DSDK_FPS,
				Settings.PREF_KEY_CANVAS2DSDK_EMPTY_FPS, Settings.PREF_KEY_OPENGL_10_FPS, Settings.PREF_KEY_OPENGL_10_EMPTY_FPS };
		
		int[] textfieldsId = new int[] { R.id.tvCanvas2DNoInheritanceResult, R.id.tvCanvas2DSdkResult, R.id.tvCanvas2DEmptyResult, R.id.tvOpenGL10Result,
				R.id.tvOpenGL10EmptyResult };

		for (int i = 0; i < millisecKeys.length; i++) {
			String millisecKey = millisecKeys[i];
			String fpsKey = fpsKeys[i];
			int textFieldId = textfieldsId[i];

			long millisecValue = sharedPreferences.getLong(millisecKey, -1L);
			int fpsValue = sharedPreferences.getInt(fpsKey, -1);
			TextView textView = (TextView) findViewById(textFieldId);

			displayResultFor(millisecValue, fpsValue, textView);
		}

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
