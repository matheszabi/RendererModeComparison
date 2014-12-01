package com.mathesoft.renderermodecomparison;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class GameImageResources {
	public static Bitmap[] digits;
	public static int[] digitsPosX;

	// this should be at native side
	public static void initImages(Context context, int screenWidth, int screenHeight) {
		initDigits(context, screenWidth, screenHeight);
	}

	private static void initDigits(Context context, int screenWidth, int screenHeight) {

		if (digits != null) {
			return;
		}

		int[] resourceIds = new int[] { R.drawable.digit_0, R.drawable.digit_1, R.drawable.digit_2, R.drawable.digit_3, R.drawable.digit_4, R.drawable.digit_5,
				R.drawable.digit_6, R.drawable.digit_7, R.drawable.digit_8, R.drawable.digit_9 };

		digits = new Bitmap[resourceIds.length];


		// space0, sprite0, space1, sprite1, ...spriteN, spaceN+1
		// sprite0.width = 2*space0.with
		
		
		// scale ...
		int spaceCountBetweenSprites = Settings.spritesToRender - 1;
		int spacesCount = 2 + spaceCountBetweenSprites;// left and right margin;
		
		// 800 / (2* 10 + 11)
		int spaceLogicalWidth = (int) (screenWidth / (2*Settings.spritesToRender + spacesCount));//25
		int spriteLogicalWidth = 2 * spaceLogicalWidth;//50

		Bitmap bmpRaw0 = BitmapFactory.decodeResource(context.getResources(), resourceIds[0]);
		float bmpRatio0 = ((float) bmpRaw0.getWidth() / (float) bmpRaw0.getHeight());// 725 / 825 =  0.8787879
		// check at what dimension need to be scaled keeping this ratio to fit the max space

		int spriteLocicalHeight =   (int) (1f / bmpRatio0 * spriteLogicalWidth);//  56
		if(spriteLocicalHeight > screenHeight){
			// then need to be rescaled
			float heightRatio = (float)screenHeight / spriteLocicalHeight;
			spriteLogicalWidth = (int) (spriteLogicalWidth * heightRatio);
			spaceLogicalWidth = spriteLogicalWidth / 2;
		}
		

		int digitsHeight = spriteLocicalHeight;// 56
		int digitsWidth = spriteLogicalWidth;// 

		Log.e("GameImageResources", "DEBUG_WITH_RED: digitsWidth : " + digitsWidth + ", digitsHeight : " + digitsHeight);

		for (int i = 0; i < resourceIds.length; i++) {
			Bitmap bmpRaw = BitmapFactory.decodeResource(context.getResources(), resourceIds[i]);

			// scale it to the calculated size:
			digits[i] = Bitmap.createScaledBitmap(bmpRaw, digitsWidth, digitsHeight, true);

			// clean the memory:

			bmpRaw.recycle();
			bmpRaw = digits[i];
		}
		digitsPosX = new int[Settings.spritesToRender];
		Bitmap bitmap0 = digits[0];
		int spriteWidth = bitmap0.getWidth();// 87
		// int spriteHeight = bitmap0.getHeight();
		
		float witdthForSpaces = (float) screenWidth - ((Settings.spritesToRender) * digitsWidth);
		int spriteSpaceWidth = (int) (witdthForSpaces / spacesCount);
		Log.e("GameImageResources", "DEBUG_WITH_RED: spiteSpaceWidth : " + spriteSpaceWidth);

		for (int i = 0; i < digitsPosX.length; i++) {
			digitsPosX[i] = spriteSpaceWidth + i * (spriteSpaceWidth + spriteWidth);// 134,355,576
			Log.e("GameImageResources", "DEBUG_WITH_RED: digitsPosX[" + i + "] : " + digitsPosX[i]);
		}
	}
}
