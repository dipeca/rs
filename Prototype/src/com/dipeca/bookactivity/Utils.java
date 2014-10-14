package com.dipeca.bookactivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Utils {

	public static void removeRule(RelativeLayout.LayoutParams params, int rule) {
		// if (android.os.Build.VERSION.SDK_INT >=
		// Build.VERSION_CODES.JELLY_BEAN_MR1) { // API 17
		// params.removeRule(rule);
		// } else {
		params.addRule(rule, 0);
		// }
	}

	public static int getColorFromPoint(Drawable drawable, int x, int y,
			Bitmap bitmap) {

		Rect imageBounds = drawable.getBounds();

		// original height and width of the bitmap
		int intrinsicHeight = drawable.getIntrinsicHeight();
		int intrinsicWidth = drawable.getIntrinsicWidth();

		// height and width of the visible (scaled) image
		int scaledHeight = imageBounds.height();
		int scaledWidth = imageBounds.width();

		// Find the ratio of the original image to the scaled image
		// Should normally be equal unless a disproportionate scaling
		// (e.g. fitXY) is used.
		float heightRatio = (float) intrinsicHeight / scaledHeight;
		float widthRatio = (float) intrinsicWidth / scaledWidth;

		// do whatever magic to get your touch point
		// MotionEvent event;

		// get the distance from the left and top of the image bounds
		float scaledImageOffsetX = x - imageBounds.left;
		float scaledImageOffsetY = y - imageBounds.top;

		// scale these distances according to the ratio of your scaling
		// For example, if the original image is 1.5x the size of the scaled
		// image, and your offset is (10, 20), your original image offset
		// values should be (15, 30).
		float originalImageOffsetX = scaledImageOffsetX * widthRatio;
		float originalImageOffsetY = scaledImageOffsetY * heightRatio;

		int xInt = Math.round(originalImageOffsetX);
		int yInt = Math.round(originalImageOffsetX);

		// To be sure that x < bitmap.getWidth and x >= 0
		if (xInt >= bitmap.getWidth()) {
			xInt = bitmap.getWidth() - 1;
		} else if (xInt < 0) {
			xInt = 0;
		}

		// To be sure that y < bitmap.getHeight and y >= 0
		if (yInt >= bitmap.getHeight()) {
			yInt = bitmap.getHeight() - 1;
		} else if (yInt < 0) {
			yInt = 0;
		}

		// get color from pixel of touch point
		int touchColor = bitmap.getPixel(xInt, yInt);

		return touchColor;
	}
	
	public static int getHotspotColor(DragEvent event, ImageView view) {
		float[] coord = getCoordinatesFromTouch(event.getX(), event.getY(), (ImageView) view);
		
		int x = (int) coord[0];
		int y = (int) coord[1];
		
		return getHotspotColorFromCoordinates(x, y, view);
	}

	public static int getHotspotColor(MotionEvent event, ImageView view) {
		float[] coord = getCoordinatesFromTouch(event.getX(), event.getY(), (ImageView) view);
		
		int x = (int) coord[0];
		int y = (int) coord[1];
		
		return getHotspotColorFromCoordinates(x, y, (ImageView) view);
	}
	
	private static int getHotspotColorFromCoordinates(int x , int y, ImageView view) {
		try {
			
			Bitmap hotspots = ((BitmapDrawable) view.getDrawable()).getBitmap();
			
			if (x >= hotspots.getWidth()) {
				x = hotspots.getWidth() - 1;
			}

			if (y >= hotspots.getHeight()) {
				y = hotspots.getHeight() - 1;
			}

			if (x >= 0 && x < hotspots.getWidth() && y >= 0
					&& y < hotspots.getHeight()) {
				return hotspots.getPixel(x, y);
			}

			return hotspots.getPixel(1, 1);
		} catch (Exception e) {
			Log.d("java null", "null");
			return -1;
		}
	}
	
	
	public static int getHotspotColor(Bitmap hotspots, int x, int y, View view) {
		try {
			Log.d("Point:", "x: " + x + ", y: " + y);
			Log.d("Bitmap Dim:", "w: " + hotspots.getWidth() + ", h: "
					+ hotspots.getHeight());

			// Matrix inverse = new Matrix();
			// ((ImageView) view).getImageMatrix().invert(inverse);
			// float[] touchPoint = new float[] { x, y };
			// inverse.mapPoints(touchPoint);
			// int xCoord = Integer.valueOf((int) touchPoint[0]);
			// int yCoord = Integer.valueOf((int) touchPoint[1]);

			if (x >= hotspots.getWidth()) {
				x = hotspots.getWidth() - 1;
			}

			if (y >= hotspots.getHeight()) {
				y = hotspots.getHeight() - 1;
			}

			if (x >= 0 && x < hotspots.getWidth() && y >= 0
					&& y < hotspots.getHeight()) {
				return hotspots.getPixel(x, y);
			}

			return hotspots.getPixel(1, 1);
		} catch (Exception e) {
			Log.d("java null", "null");
			return -1;
		}
	}
	
	public static int getHotspotColor(int hotspotId, int x, int y, View view) {
//		try {
			ImageView img = (ImageView) view.findViewById(hotspotId);
			img.setDrawingCacheEnabled(true);
			Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());

			img.setDrawingCacheEnabled(false);
			Log.d("Point:", "x: " + x + ", y: " + y);
			Log.d("Bitmap Dim:", "w: " + hotspots.getWidth() + ", h: "
					+ hotspots.getHeight());

			// Matrix inverse = new Matrix();
			// ((ImageView) view).getImageMatrix().invert(inverse);
			// float[] touchPoint = new float[] { x, y };
			// inverse.mapPoints(touchPoint);
			// int xCoord = Integer.valueOf((int) touchPoint[0]);
			// int yCoord = Integer.valueOf((int) touchPoint[1]);

			if (x >= hotspots.getWidth()) {
				x = hotspots.getWidth() - 1;
			}

			if (y >= hotspots.getHeight()) {
				y = hotspots.getHeight() - 1;
			}

			if (x >= 0 && x < hotspots.getWidth() && y >= 0
					&& y < hotspots.getHeight()) {
				return hotspots.getPixel(x, y);
			}

			return hotspots.getPixel(1, 1);
//		} catch (Exception e) {
//			Log.d("java null", "null");
//			return -1;
//		}
	}

	public static boolean closeMatch(int color1, int color2, int tolerance) {
		if ((int) Math.abs(Color.red(color1) - Color.red(color2)) > tolerance)
			return false;
		if ((int) Math.abs(Color.green(color1) - Color.green(color2)) > tolerance)
			return false;
		if ((int) Math.abs(Color.blue(color1) - Color.blue(color2)) > tolerance)
			return false;
		return true;
	} // end match

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		reqWidth = (int) Math.ceil(0.9 * reqWidth);
		reqHeight = (int) Math.ceil(0.9 * reqHeight);
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inScaled = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	
	
	public static float[] getCoordinatesFromTouch(float eventX, float eventY, ImageView imageView){
		Drawable drawable = imageView.getDrawable();
		Rect imageBounds = drawable.getBounds();

		//original height and width of the bitmap
		float intrinsicHeight = drawable.getIntrinsicHeight();
		float intrinsicWidth = drawable.getIntrinsicWidth();

		//height and width of the visible (scaled) image
		float scaledHeight = imageBounds.height();
		float scaledWidth = imageBounds.width();

		//Find the ratio of the original image to the scaled image
		//Should normally be equal unless a disproportionate scaling
		//(e.g. fitXY) is used.
		float heightRatio = intrinsicHeight / scaledHeight;
		float widthRatio = intrinsicWidth / scaledWidth;

		//do whatever magic to get your touch point
		//MotionEvent event;

		//get the distance from the left and top of the image bounds
		float scaledImageOffsetX = eventX - imageBounds.left;
		float scaledImageOffsetY = eventY - imageBounds.top;

		//scale these distances according to the ratio of your scaling
		//For example, if the original image is 1.5x the size of the scaled
		//image, and your offset is (10, 20), your original image offset
		//values should be (15, 30). 
		float originalImageOffsetX = scaledImageOffsetX * widthRatio;
		float originalImageOffsetY = scaledImageOffsetY * heightRatio;
		
		return new float[]{originalImageOffsetX,originalImageOffsetY};
	}
}
