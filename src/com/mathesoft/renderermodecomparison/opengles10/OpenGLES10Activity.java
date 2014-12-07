package com.mathesoft.renderermodecomparison.opengles10;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.mathesoft.renderermodecomparison.Settings;

public class OpenGLES10Activity extends Activity {

	private GLSurfaceView mGLView;
	private MyGLRenderer mRenderer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mGLView = new GLSurfaceView(this);
		mRenderer = new MyGLRenderer(this);
		mGLView.setRenderer(mRenderer);

        // Render the view always
		mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        
		setContentView(mGLView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
	}

	public void saveResultAndQuit(long millisecsElapsed) {
		// stop rendering CONTINUOUSLY
		mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		float secondsElapsed = (int) (millisecsElapsed / 1000);
		int fps = (int) (Settings.framesToRender / secondsElapsed);

		Context appContext = getApplicationContext();
		SharedPreferences sharedPreferences = appContext.getSharedPreferences(Settings.SHARED_REFERENCES_NAME, MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putLong(Settings.PREF_KEY_OPENGL_10_MILLISEC, millisecsElapsed);
		editor.putInt(Settings.PREF_KEY_OPENGL_10_FPS, fps);
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