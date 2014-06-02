package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PagBedRoomDarkfrg extends Fragment {
	View view = null;
	private IMainActivity onChoice;
	public static String NAME = "Um barulho no quarto";
	public static String iconCurrentPage = "quarto_vazio_escuro_icon";
	public static String iconNextPage = "quarto_vazio_icon";
	private TextView tv1 = null;
	private DialogBox tv2 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;
	private static Bitmap bitmap1;

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
	private int density = 1;

	private int tv1OriginalSize;
	private int tv2OriginalSize;
	private int tv3OriginalSize;
	
	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);

		tv2 = (DialogBox) view.findViewById(R.id.dialog);
		tv2.setVisibility(View.GONE);
		tv2.setTextDialog(getString(R.string.pag1_2));

		tv3 = (TextView) view.findViewById(R.id.textPag1_2);
		tv3.setVisibility(View.GONE);

		tv1.setText(getString(R.string.soundIntheDark));
		tv1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int height1 = 0;
				int height2 = 0;
				int width3 = 0;
				int multiplier = 6;
				if (isTextHide) {
					height1 = tv1OriginalSize;//tv1.getHeight() * multiplier + (4 * density);
					height2 = tv2OriginalSize;//tv2.getHeight() * multiplier + (4 * density);
					width3  = tv3OriginalSize;//tv3.getWidth() * multiplier + (4 * density);
					isTextHide = false;
				} else {
					tv1OriginalSize = tv1.getHeight();
					tv2OriginalSize = tv2.getHeight();
					tv3OriginalSize = tv3.getHeight();
					
					height1 = Math.max(tv1.getHeight() / multiplier,
							(10 * density));
					height2 = Math.max(tv2.getHeight() / multiplier,
							(10 * density));
					width3 = Math.max(tv3.getWidth() / multiplier,
							(10 * density));
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

		density = (int) getResources().getDisplayMetrics().density;
		
		bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.quarto_vazio_escuro, 600, 300);
		iv1.setImageBitmap(bitmap1);

	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long startTime = System.currentTimeMillis();

			if (isAdded()) {
				// if we the bitmap was not loaded; If we came from the back
				// button
				// we do not load again
				if (BookActivity.bitmapInitial == null) {
					BookActivity.bitmapInitial = Utils
							.decodeSampledBitmapFromResource(getResources(),
									R.drawable.quarto_vazio, 600, 300);

				}
			}

			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			Log.d("Total time mUpdateTimeTask", "mUpdateTimeTask time =" + totalTime);
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
		Log.d("Total time onCreateView", "onCreateView after inflate time ="
				+ totalTime);

		
		loadImages();
		loadText();

		button = (ImageButton) view.findViewById(R.id.goToNextPage);
		// button.setVisibility(View.INVISIBLE);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Class c = null;
				Fragment fb = null;

				try {
					c = Class.forName("com.dipeca.prototype.PagBedRoomfrg");
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

				onChoice.onChoiceMade(PagBedRoomDarkfrg.this, PagBedRoomDarkfrg.NAME,
						PagBedRoomDarkfrg.iconCurrentPage);
				// Commit the page to database
				onChoice.onChoiceMadeCommitFirstPage(PagBedRoomDarkfrg.NAME, true);
				
				onChoice.onChoiceMade(fb, PagBedRoomfrg.NAME, iconNextPage);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});
		
		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);
		buttonPrev.setVisibility(View.INVISIBLE);

		// run the start() method later on the UI thread
		view.postDelayed(mUpdateTimeTask, 1000);

		BookActivity.playMusic(R.raw.boxes_moving);

		return view;
	}

	@Override
	public void onDetach() {
		Log.d("BedRoom ", "BedRoom onDetach()");
		super.onDetach();

		bitmap1.recycle();
		bitmap1 = null;

	}


}
