package com.mathesoft.renderermodecomparison.opengles10empty;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.mathesoft.renderermodecomparison.Settings;


public class MyGLRenderer implements GLSurfaceView.Renderer {


	private Context context;
	private int i;
	private long start;

	public MyGLRenderer(Context context) {
		this.context = context;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background frame color
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		if(i==0 ){
			start = System.currentTimeMillis();
		}
		
		i++;
		
		if (i >= Settings.framesToRender) {
			long end = System.currentTimeMillis();
			if (context instanceof OpenGLES10EmptyActivity) {
				OpenGLES10EmptyActivity openGLES10EmptyActivity = (OpenGLES10EmptyActivity) context;
				openGLES10EmptyActivity.saveResultAndQuit(end-start);
			}
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Adjust the viewport based on geometry changes
		// such as screen rotations
		gl.glViewport(0, 0, width, height);

		// make adjustments for screen ratio
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION); // set matrix to projection mode
		gl.glLoadIdentity(); // reset the matrix to its default state
		gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7); // apply the projection matrix
	}

}