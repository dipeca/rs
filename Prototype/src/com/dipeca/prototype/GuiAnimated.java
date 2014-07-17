package com.dipeca.prototype;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class GuiAnimated {

	private static final String TAG = GuiAnimated.class.getSimpleName();

	private Bitmap bitmap; // the animation sequence
	private Rect sourceRect; // the rectangle to be drawn from the animation
								// bitmap
	private int frameNr; // number of frames in animation
	private int currentFrame; // the current frame
	private long frameTicker; // the time of the last frame update
	private int framePeriod; // milliseconds between each frame (1000/fps)

	private int spriteWidth; // the width of the sprite to calculate the cut out
								// rectangle
	private int spriteHeight; // the height of the sprite

	private int x; // the X coordinate of the object (top left of the image)
	private int y; // the Y coordinate of the object (top left of the image)
	
	
	public GuiAnimated(Bitmap bitmap, int x, int y, int width, int height, int fps, int frameCount) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		currentFrame = 0;
		frameNr = frameCount;
		spriteWidth = bitmap.getWidth() / frameCount;
		spriteHeight = bitmap.getHeight();
		sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
		framePeriod = 1000 / fps;
		frameTicker = 0l;
	}
	
	public void update(long gameTime) {
		if (gameTime > frameTicker + framePeriod) {
			frameTicker = gameTime;
			// increment the frame
			currentFrame++;
			if (currentFrame >= frameNr) {
				currentFrame = 0;
			}
		}
		// define the rectangle to cut out sprite
		this.sourceRect.left = currentFrame * spriteWidth;
		this.sourceRect.right = this.sourceRect.left + spriteWidth;
	}
	

}
