package com.mathesoft.renderermodecomparison.opengles10;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

import com.mathesoft.renderermodecomparison.ImageResources;
import com.mathesoft.renderermodecomparison.Settings;

public class MyGLRenderer implements GLSurfaceView.Renderer {

	private Context context;

	private int framesCount;
	private long start;


	private int[] textures = new int[1];



	public MyGLRenderer(Context context) {
		this.context = context;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// SETTINGS
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// DRAWING SETUP
		gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do

		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		// init images, needed for textures.
		ImageResources.initImages(context, width, height);

		int[] maxTextureSize = new int[1];// 2048, 4096
		GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);

		Log.e("opengles10.MyGLRenderer", "DEBUG_WITH_RED: maxTextureSize: " + maxTextureSize[0] );

		// loadGLTexture
		
		// generate one texture pointer
		gl.glGenTextures(1, textures, 0);
		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		
		// Use Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, ImageResources.bmpTextureAtlas, 0);


		// gl.glEnable(GL10.GL_CULL_FACE);
		// // What faces to remove with the face culling.
		// gl.glCullFace(GL10.GL_FRONT);
		
		gl.glViewport(0, 0, width, height);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix //Reset The Projection Matrix
		gl.glLoadIdentity();
		// Orthographic mode for 2d
		gl.glOrthof(0, width, 0, height, -1, 8);// (float left, float right, float bottom, float top, float zNear, float zFar)
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();// Reset The Modelview Matrix
		
	}

	@Override
	public void onDrawFrame(GL10 gl) {

		if (framesCount == 0) {
			start = System.currentTimeMillis();
			// Log.e("MyGLRenderer", "DEBUG_WITH_RED: onDrawFrame");
		}

		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Replace the current matrix with the identity matrix
		gl.glLoadIdentity();
		// Rotate world by 180 around x axis so positive y is down (like canvas)
		// gl.glRotatef(-180, 1, 0, 0);

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Tell OpenGL where our texture is located.
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		// Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		// Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);

		// make a local variable and don't touch the instance variable
		int frameIndex = framesCount; // just for currentDigitValue value
		for (int i = Settings.spritesToRender - 1; i >= 0; i--) {// i = 2, 1, 0
			int currentDigitValue = frameIndex % 10;// 0...9
			frameIndex /= 10;// move it to next digit.
			
			float[] vertices = ImageResources.vertexCoords[i];
			float[] textureCoords = ImageResources.textureCoords[currentDigitValue];
			
			ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoords.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			FloatBuffer textureBuffer = tbb.asFloatBuffer();
			textureBuffer.put(textureCoords);
			textureBuffer.position(0);
			
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			FloatBuffer vertexBuffer = vbb.asFloatBuffer();
			vertexBuffer.put(vertices);
			vertexBuffer.position(0);
					
			
			//Log.e("MyGLRenderer", "currentDigitValue: "+currentDigitValue + ", i: "+i);
			// Point to our vertex buffer
			// Specifies the location and data format of the array of vertex coordinates to use when rendering.
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

			// Telling OpenGL where our textureCoords are.
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

			// Draw the vertexCoords as triangle strip
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);			
		}

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		
		framesCount++;
		if (framesCount >= Settings.framesToRender) {
			long end = System.currentTimeMillis();
			if (context instanceof OpenGLES10Activity) {
				OpenGLES10Activity openGLES10Activity = (OpenGLES10Activity) context;
				openGLES10Activity.saveResultAndQuit(end - start);
			}
		}
	}

}