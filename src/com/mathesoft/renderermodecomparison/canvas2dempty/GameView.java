package com.mathesoft.renderermodecomparison.canvas2dempty;

import android.content.Context;
import android.graphics.Canvas;

import com.mathesoft.renderermodecomparison.game.AbstractGameView;

public class GameView extends AbstractGameView {

	public GameView(Context context) {
		super(context);
	}

	// Called from GameLoopThread
	public void draw(Canvas canvas, int frameIndex) {
		// it is an empty draw body
	}

}
