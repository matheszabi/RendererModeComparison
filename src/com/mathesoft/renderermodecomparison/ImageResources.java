package com.mathesoft.renderermodecomparison;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

/**
 * The 2D images are cached here, it could be removed after use, now it isn't, because not needed in this app. Make sure you have added the
 * <code>android:largeHeap="true"</code> to application tag in the AndroidManifest.xml this will give you more memory for SDK and you will not get
 * OutOfMemoryException. Depends on the screen resolution but this class will use 2.5-17 MB, on screen resolutions above 1920 x 1200 even more if you don't
 * change anything.
 * 
 * @author matheszabi
 *
 */
public class ImageResources {
	// 10 x 160 x 182 x 4 = 1,164,800 bytes - on a 800 x 480 device
	// 10 x 384 x 437 x 4 = 6,712,320 bytes - on a 1920 x 1200 device
	public static Bitmap[] digits;

	public static int[] digitsPosX;

	// #### OpenGL 1.0 / 1.1 stuff ###
	// the digits atlas map is:
	// 0 1 2 3 4 5 6 7 8 9
	public static Bitmap bmpTextureAtlas;// digits are located at the top left part of the image
	public static int textureAtlasWidth = -1;// 2048 (4096)
	public static int textureAtlasHeight = -1;// 256 (512)
	public static int digitsHeight = -1;// 182 (437)
	public static int digitsWidth = -1;// 160 (387)

	// all coordinates for the textures:
	public static float[][] textureCoords; // digits.length

	// all vertex coordinates for the sprites.
	public static float[][] vertexCoords; // Settings.spritesToRender
 
	public static void initImages(Context context, int screenWidth, int screenHeight) {
		initDigits(context, screenWidth, screenHeight);
	}

	private static void initDigits(Context context, int screenWidth, int screenHeight) {// 800,480 (1920, 1080)

		// init only once:
		if (digits != null) {
			return;
		}

		int[] resourceIds = new int[] { R.drawable.digit_0, R.drawable.digit_1, R.drawable.digit_2, R.drawable.digit_3, R.drawable.digit_4, R.drawable.digit_5,
				R.drawable.digit_6, R.drawable.digit_7, R.drawable.digit_8, R.drawable.digit_9 };

		digits = new Bitmap[resourceIds.length];


		// scale ...
		int spaceCountBetweenSprites = Settings.spritesToRender - 1;// 2
		int spacesCount = 2 + spaceCountBetweenSprites;// left and right margin; //4

		// 800 / (2* 10 + 11)
		int spaceLogicalWidth = (int) (screenWidth / (2 * Settings.spritesToRender + spacesCount));// 80 (192)
		int spriteLogicalWidth = 2 * spaceLogicalWidth;// 160 (384)

		Bitmap bmpRaw0 = BitmapFactory.decodeResource(context.getResources(), resourceIds[0]);
		float bmpRatio0 = ((float) bmpRaw0.getWidth() / (float) bmpRaw0.getHeight());// 725 / 825 = 0.8787879 (1449/1650 = 0.8781818)
		// check at what dimension need to be scaled keeping this ratio to fit the max space

		int spriteLocicalHeight = (int) (1f / bmpRatio0 * spriteLogicalWidth);// 182 (437)
		if (spriteLocicalHeight > screenHeight) {
			// then need to be rescaled
			float heightRatio = (float) screenHeight / spriteLocicalHeight;
			spriteLogicalWidth = (int) (spriteLogicalWidth * heightRatio);
			spaceLogicalWidth = spriteLogicalWidth / 2;
		}

		digitsHeight = spriteLocicalHeight;// 182 (437)
		digitsWidth = spriteLogicalWidth;// 160 (387)

		//Log.e("ImageResources", "DEBUG_WITH_RED: digitsWidth : " + digitsWidth + ", digitsHeight : " + digitsHeight);// 160, 182 (384,437)

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
		int spriteWidth = bitmap0.getWidth();// 160 (384)

		float witdthForSpaces = (float) screenWidth - ((Settings.spritesToRender) * digitsWidth);// 320 (768)
		int spriteSpaceWidth = (int) (witdthForSpaces / spacesCount);// 80 (192)
		//Log.e("ImageResources", "DEBUG_WITH_RED: spiteSpaceWidth : " + spriteSpaceWidth);

		for (int i = 0; i < digitsPosX.length; i++) {
			digitsPosX[i] = spriteSpaceWidth + i * (spriteSpaceWidth + spriteWidth);// 134,355,576
			//Log.e("ImageResources", "DEBUG_WITH_RED: digitsPosX[" + i + "] : " + digitsPosX[i]);
		}

		// generate resources for OpenGL 1.0 :
		// the texture switching it is an expensive method, so it needs to be an atlas
		// because it is OpenGL 1.0 and not 2.0 the atlas sides must be power of 2.
		// the digits atlas map is:
		// 0 1 2 3 4 5 6 7 8 9

		textureAtlasWidth = (int) Math.pow(2, Math.ceil(Math.log(10 * digitsWidth) / Math.log(2))); // 2048 (4096)
		textureAtlasHeight = (int) Math.pow(2, Math.ceil(Math.log(digitsHeight) / Math.log(2)));// 256 (512)
		//Log.e("ImageResources", "DEBUG_WITH_RED: textureAtlasWidth : " + textureAtlasWidth + ", textureAtlasHeight : " + textureAtlasHeight);//

		bmpTextureAtlas = Bitmap.createBitmap(textureAtlasWidth, textureAtlasHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmpTextureAtlas);
		Rect src = null;
		Rect dst = new Rect(0, 0, 0, 0);

		textureCoords = new float[digits.length][];
		// draw the digits to the atlas and calculate / store the texture coordinates
		for (int i = 0; i < digits.length; i++) {
			dst.bottom = digitsHeight;
			dst.left = i * digitsWidth;
			dst.top = 0;
			dst.right = dst.left + digitsWidth;
			// draw it:
			canvas.drawBitmap(digits[i], src, dst, null);

			float left = (float) dst.left / textureAtlasWidth;
			float bottom = 0f;
			float right = (float) dst.right / textureAtlasWidth;
			float top = 1.0f - ((float) textureAtlasHeight - digitsHeight) / textureAtlasHeight;

			float[] curTextureCoords = new float[] {//
			left, top,// 				top left 		(V2)
					left, bottom,// 	bottom left 	(V1)
					right, top,// 		top right 		(V4)
					right, bottom // 	bottom right 	(V3)
			};
			textureCoords[i] = curTextureCoords;
			//Log.e("ImageResources", "DEBUG_WITH_RED: textureBuffer[" + i + "] left: " + left + ", bottom : " + bottom + ", right: " + right + ", top:" + top);//
		}

		// calculate the vertex coordinates and store it: 
		
		vertexCoords = new float[Settings.spritesToRender][];
		for (int i = 0; i < Settings.spritesToRender; i++) {
			// left bottom
			float left = digitsPosX[i];
			float centerY = screenHeight / 2.0f;
			float bottom = (centerY) - digitsHeight / 2.0f;
			float right = left + digitsWidth;
			float top = bottom + digitsHeight;
			float z0 = 0.0f;

			float[] curVertices = new float[] {//
			left, bottom, z0,// 			V1 - bottom left
					left, top, z0, // 		V2 - top left
					right, bottom, z0,// 	V3 - bottom right
					right, top, z0 // 		V4 - top right
			};

			vertexCoords[i] = curVertices;
		}

		// turn it on it you want to see it:
		// then add the <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> to the Manifest!
		boolean saveToSdCard = false;

		if (saveToSdCard) {
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard, "myOpenGLTest");
			if (!dir.isDirectory()) {
				dir.mkdirs();
			}

			File file = new File(dir, "bmpTextureAtlas.png");
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Log.e("ImageResources", "DEBUG_WITH_RED: bmpTextureAtlas path: " + file.getAbsolutePath());
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(file);
				boolean compressed = bmpTextureAtlas.compress(Bitmap.CompressFormat.PNG, 100, fOut);
				Log.e("ImageResources", "DEBUG_WITH_RED: compressed: " + compressed);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fOut != null) {
					try {
						fOut.flush();
						fOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			for (int i = 0; i < digits.length; i++) {
				try {
					File fileDigit = new File(dir, "Digit_" + i + ".png");
					fileDigit.createNewFile();
					fOut = new FileOutputStream(fileDigit);
					digits[i].compress(Bitmap.CompressFormat.PNG, 100, fOut);
					fOut.flush();
					fOut.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
