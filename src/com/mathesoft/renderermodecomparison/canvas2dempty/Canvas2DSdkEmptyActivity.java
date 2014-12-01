package com.mathesoft.renderermodecomparison.canvas2dempty;

import com.mathesoft.renderermodecomparison.Settings;
import com.mathesoft.renderermodecomparison.game.AbstractGame2DActivity;

public class Canvas2DSdkEmptyActivity extends AbstractGame2DActivity {

	protected  void initContentView(){
		 setContentView(new GameView(this));
	}
	
	public void saveResultAndQuit(long millisecsElapsed) {
		saveResultAndQuitBasic(millisecsElapsed, Settings.PREF_KEY_CANVAS2DSDK_EMPTY_MILLISEC, Settings.PREF_KEY_CANVAS2DSDK_EMPTY_FPS);
	}
}
