package com.dipeca.bookactivity;

import java.net.URL;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dipeca.item.DialogBox;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagBedRoomfrg extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.bedroom;
	public static int icon = R.drawable.quarto_vazio_icon;
	private TextView tv1 = null;
	private DialogBox tv2 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;
	ImageView amuleto = null;
	private Bitmap bitmap1;
	private Bitmap bitmap2;

	public static String nextPage = PagBedRoomfAmuletfrg.class.getName();
	public static String prevPage = PagBedRoomDarkfrg.class.getName();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			onChoice = (IMainActivity) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnChoiceMade");
		}
	}

	private ImageView iv1;
	private ImageView iv2;
	private int density = 1;

	private int tv1OriginalSize;
	private int tv2OriginalSize;
	private int tv3OriginalSize;

	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv2 = (DialogBox) view.findViewById(R.id.dialog);
		tv2.setTextDialog(getString(R.string.pag1_2));

		tv3 = (TextView) view.findViewById(R.id.textPag1_2);
		tv3.setVisibility(View.GONE);

		tv1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int height1 = 0;
				int height2 = 0;
				int width3 = 0;
				int multiplier = 6;
				if (isTextHide) {

					height1 = tv1OriginalSize;// tv1.getHeight() * multiplier +
												// (4 * density);
					height2 = tv2OriginalSize;// tv2.getHeight() * multiplier +
												// (4 * density);
					width3 = tv3OriginalSize;// tv3.getWidth() * multiplier + (4
												// * density);

					isTextHide = false;
				} else {
					tv1OriginalSize = tv1.getHeight();
					tv2OriginalSize = tv2.getHeight();
					tv3OriginalSize = tv3.getWidth();

					height1 = Math.max(tv1.getHeight() / multiplier,
							((int) Math.ceil(10 * density)));
					height2 = Math.max(tv2.getHeight() / multiplier,
							((int) Math.ceil(10 * density)));
					width3 = Math.max(tv3.getWidth() / multiplier,
							((int) Math.ceil(10 * density)));
					isTextHide = true;
				}

				ViewGroup.LayoutParams params1 = tv1.getLayoutParams();
				params1.width = tv1.getWidth();
				params1.height = height1;
				tv1.setLayoutParams(params1);

				ViewGroup.LayoutParams params2 = tv2.getLayoutParams();
				params2.width = tv2.getWidth();
				params2.height = height2;
				tv2.setLayoutParams(params2);

				ViewGroup.LayoutParams params3 = tv3.getLayoutParams();
				params3.width = width3;
				params3.height = tv3.getHeight();
				tv3.setLayoutParams(params3);
			}
		});

	}

	private void loadImages() {
		Log.d("Frg Bed Room ", "loadImages()");
		iv1 = (ImageView) view.findViewById(R.id.pag1ImageView);
		iv2 = (ImageView) view.findViewById(R.id.pag1ImageViewAmuleto);
          
		density = (int) Math.ceil(getResources().getDisplayMetrics().density);
		Log.d(getString(NAME), "Density: " + density);
		bitmap1 = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.quarto, 400 * density, 200 * density);
		iv1.setImageBitmap(bitmap1);

		tv2.setImg1Id(getResources().getDrawable(R.anim.rocket));
		tv2.setImg2Id(getResources().getDrawable(R.anim.gui_quarto_anim));
		Log.d("Frg Bed Room ", "after loadImages()");
	} 

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long startTime = System.currentTimeMillis();
			if (isAdded()) {
				// if we the bitmap was not loaded; If we came from the back
				// buttonNext
				// we do not load again
				if (BookActivity.bitmap1 == null) {

					BookActivity.bitmap1 = Utils
							.decodeSampledBitmapFromResource(getResources(),
									R.drawable.quarto_portal, 400 * density, 200 * density);
				}
			}
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			Log.d("Total time", "mUpdateTimeTask time =" + totalTime);
			// buttonNext.setVisibility(View.VISIBLE);
		}
	};

	ImageButton button = null;
	ImageButton buttonPrev = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater
				.inflate(R.layout.pag_one_image_dialog, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time " + NAME, "onCreateView after inflate time ="
				+ totalTime);

		density = (int) getResources().getDisplayMetrics().density;

		amuleto = (ImageView) view.findViewById(R.id.pag1Amuleto);

		amuleto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				amuleto.setVisibility(View.INVISIBLE);

			}
		});

		loadText();
		// loadImages()
		loadImages();

		// BookActivity.playMusic(R.raw.bedroom);

		button = (ImageButton) view.findViewById(R.id.goToNextPage);
		// buttonNext.setVisibility(View.INVISIBLE);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Class c = null;
				Fragment fb = null;

				try {
					c = Class.forName(nextPage);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					fb = (Fragment) c.newInstance();
				} catch (java.lang.InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				onChoice.onChoiceMade(fb, PagBedRoomfAmuletfrg.NAME,
						PagBedRoomfAmuletfrg.icon);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagBedRoomDarkfrg fb = new PagBedRoomDarkfrg();

				onChoice.onChoiceMade(fb, PagBedRoomDarkfrg.NAME,
						PagBedRoomfAmuletfrg.icon);
				onChoice.onChoiceMadeCommit(NAME, false);
			}
		});

		// run the start() method later on the UI thread
		view.postDelayed(mUpdateTimeTask, 1000);

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pag1), bitmap1));

		return view;
	}

	@Override
	public void onDetach() {
		Log.d("BedRoom ", "BedRoom onDetach()");
		super.onDetach();
		
		iv1.setImageBitmap(null);
		
//		if (bitmap1 != null) {
//			bitmap1.recycle();
//			bitmap1 = null;
//		}
	}

	ImageView jakeImage;
	ImageView cloudImage;

	private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
		// Do the long-running work in here
		protected Long doInBackground(URL... urls) {
			int count = urls.length;

			return null;
		}

		// This is called each time you call publishProgress()
		protected void onProgressUpdate(Integer... progress) {
			// setProgressPercent(progress[0]);
		}

		// This is called when doInBackground() is finished
		protected void onPostExecute(Long result) {
			// showNotification("Downloaded " + result + " bytes");
		}
	}

	AnimationDrawable backGroundChangeAnim;

	@Override
	public String getPrevPage() {
		return prevPage;
	}

	@Override
	public String getNextPage() {
		return nextPage;
	}

}
