package com.mathesoft.renderermodecomparison.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.SurfaceView;

public abstract class AbstractGameView extends SurfaceView {

	protected GameLoopThread gameLoopThread;
	protected SurfaceHolderCallbackImpl surfaceHolder;

	public AbstractGameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);
		surfaceHolder = new SurfaceHolderCallbackImpl(gameLoopThread);
		getHolder().addCallback(surfaceHolder);
	}

	public void saveResultAndQuit(long millisecsElapsed) {
		Context context = getContext();
		if (context instanceof AbstractGame2DActivity) {
			AbstractGame2DActivity abstractGame2DActivity = (AbstractGame2DActivity) context;
			abstractGame2DActivity.saveResultAndQuit(millisecsElapsed);
		}
	}

	// used at draw();
	protected RectF dst = new RectF();

	public abstract void draw(Canvas canvas, int frameIndex);

	

}
