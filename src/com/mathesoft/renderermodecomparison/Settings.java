package com.mathesoft.renderermodecomparison;

public class Settings {

	public static final String SHARED_REFERENCES_NAME = "results";

	public static final String PREF_KEY_CANVAS2D_NO_INHERITANCE_MILLISEC = "Canvas2DNoInheritanceResultMillisec";
	public static final String PREF_KEY_CANVAS2D_NO_INHERITANCE_FPS = "Canvas2DNoInheritanceResultFps";
	
	public static final String PREF_KEY_CANVAS2DSDK_MILLISEC = "Canvas2DSdkResultMillisec";
	public static final String PREF_KEY_CANVAS2DSDK_FPS = "Canvas2DSdkResultFps";
	
	
	public static final String PREF_KEY_CANVAS2DSDK_EMPTY_MILLISEC = "Canvas2DEmptyResultMillisec";
	public static final String PREF_KEY_CANVAS2DSDK_EMPTY_FPS = "Canvas2DEmptyResultFps";
	
	public static int framesToRender = 500;
	public static int spritesToRender = 3;
}
