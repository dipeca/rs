package com.dipeca.prototype;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Utils {

	public static int getHotspotColor(int hotspotId, int x, int y, View view) {
		ImageView img = (ImageView) view.findViewById(hotspotId);
		img.setDrawingCacheEnabled(true);
		Bitmap hotspots =  Bitmap.createBitmap(img.getDrawingCache());
		img.setDrawingCacheEnabled(false);
		Log.d("Point:", "x: " + x + ", y: " + y);
		Log.d("Bitmap Dim:", "w: " + hotspots.getWidth() + ", h: " + hotspots.getHeight());
		
//		Matrix inverse = new Matrix();
//		((ImageView) view).getImageMatrix().invert(inverse);
//		float[] touchPoint = new float[] { x, y };
//		inverse.mapPoints(touchPoint);
//		int xCoord = Integer.valueOf((int) touchPoint[0]);
//		int yCoord = Integer.valueOf((int) touchPoint[1]);
		
		if (x >= hotspots.getWidth()) {
			x = hotspots.getWidth() - 1;
		}
		
		if (y >= hotspots.getHeight()) {
			y = hotspots.getHeight() -1;
		}
		
		if(x >= 0 && x < hotspots.getWidth() && y >= 0 && y < hotspots.getHeight()){
			return hotspots.getPixel(x, y);
		}

		return hotspots.getPixel(1, 1);
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

		reqWidth = (int)Math.ceil(0.9*reqWidth);
		reqHeight = (int)Math.ceil(0.9*reqHeight);
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
}
