package com.mathesoft.renderermodecomparison.canvas2dnoinheritance;

import android.graphics.Canvas;

import com.mathesoft.renderermodecomparison.Settings;

public class GameLoopThread extends Thread {


	private GameView view;
	private boolean running = false;

	public GameLoopThread(GameView view) {
		this.view = view;
		
	}

	public void setRunning(boolean run) {
		running = run;
	}

	@Override
	public void run() {

		int i = 0;
		long start = System.currentTimeMillis();
		while (running) {
			Canvas c = null;			

			try {
				c = view.getHolder().lockCanvas();
				synchronized (view.getHolder()) {
					view.draw(c, i);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (c != null) {
					view.getHolder().unlockCanvasAndPost(c);
				}
			}	
			// increment
			i++;
			// check and jump out from loop if needed
			if(i == Settings.framesToRender){
				// running = false;
				break;
			}
		}
		
		long end = System.currentTimeMillis();
		
		long millisecsElapsed = end-start;
		
		view.saveResultAndQuit(millisecsElapsed);
		
	}

	public GameView getGameView() {
		return view;
	}

}
