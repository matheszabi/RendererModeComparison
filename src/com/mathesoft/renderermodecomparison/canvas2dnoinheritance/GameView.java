package com.mathesoft.renderermodecomparison.canvas2dnoinheritance;

import com.mathesoft.renderermodecomparison.ImageResources;
import com.mathesoft.renderermodecomparison.Settings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.SurfaceView;

public class GameView extends SurfaceView {

	private GameLoopThread gameLoopThread;
	private SurfaceHolderCallbackImpl surfaceHolder;

	public GameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);
		surfaceHolder = new SurfaceHolderCallbackImpl(gameLoopThread);
		getHolder().addCallback(surfaceHolder);
	}

	public void saveResultAndQuit(long millisecsElapsed) {
		Context context = getContext();
		if (context instanceof Canvas2DNoInheritanceActivity) {
			Canvas2DNoInheritanceActivity canvas2DNoInheritanceActivity = (Canvas2DNoInheritanceActivity) context;
			canvas2DNoInheritanceActivity.saveResultAndQuit(millisecsElapsed);
		}
	}

	private RectF dst = new RectF();

	// Called from GameLoopThread
	public void draw(Canvas canvas, int frameIndex) {
		 
	
		canvas.drawColor(Color.BLACK);
		for (int i = Settings.spritesToRender - 1; i >= 0; i--) {// i = 2, 1, 0
			
			int currentDigitValue = frameIndex % 10;// 0...9
			frameIndex /= 10;// move it to next digit.

			int posX = ImageResources.digitsPosX[i];
			Bitmap curBitmap = ImageResources.digits[currentDigitValue];

			int posY = (int) ((canvas.getHeight() + curBitmap.getHeight()) / 2) - curBitmap.getHeight();

			// store values for destination:
			dst.left = posX;
			dst.top = posY;
			dst.right = posX + curBitmap.getWidth();
			dst.bottom = posY + curBitmap.getHeight();

			canvas.drawBitmap(curBitmap, null, dst, null);
		}
	}

}
