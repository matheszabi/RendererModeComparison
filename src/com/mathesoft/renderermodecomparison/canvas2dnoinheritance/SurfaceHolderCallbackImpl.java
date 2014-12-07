package com.mathesoft.renderermodecomparison.canvas2dnoinheritance;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

import com.mathesoft.renderermodecomparison.ImageResources;

public class SurfaceHolderCallbackImpl implements Callback {

	private GameLoopThread gameLoopThread;

	public SurfaceHolderCallbackImpl(GameLoopThread gameLoopThread) {
		this.gameLoopThread = gameLoopThread;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		//Log.d("SurfaceHolderCallbackImpl", "surfaceChanged");

		Context context = gameLoopThread.getGameView().getContext();
		ImageResources.initImages(context, width, height);

		// start the game:
		gameLoopThread.setRunning(true);
		gameLoopThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		gameLoopThread.setRunning(false);
		while (retry) {
			try {
				gameLoopThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

}
