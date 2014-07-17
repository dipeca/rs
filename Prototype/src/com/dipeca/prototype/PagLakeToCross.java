package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PagLakeToCross extends Fragment {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.theLake;
	public static int icon = R.drawable.lago_icon;
	private TextView tv1 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;

	private static Bitmap bitmap1;
	private static Bitmap bitmap2;
	
	ImageButton button = null;
	ImageButton buttonPrev = null;

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

	private ImageView image1;
	private ImageView image2;
	private ImageView ivClickable;

	private void loadImages() {
		Log.d("KingDom ", "loadImages()");
		image1 = (ImageView) view.findViewById(R.id.ivMain);
		image2 = (ImageView) view.findViewById(R.id.ivSmall);
		ivClickable = (ImageView) view.findViewById(R.id.clickable);

		bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.lagoteste2, 600, 300);
		image1.setImageBitmap(bitmap1);
		
		bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.lagoteste3, 400, 200);
		image2.setImageBitmap(bitmap2);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_sub_image, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time kingdom", "onCreateView after inflate time ="
				+ totalTime);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagLakeToCross);

		tv3 = (TextView) view.findViewById(R.id.textPag1_2);
		tv3.setVisibility(View.GONE);

		// loadImages()
		loadImages();

		tv1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int density = (int) getResources().getDisplayMetrics().density;
				int height1 = 0;
				int width3 = 0;
				int multiplier = 7;
				if (isTextHide) {
					height1 = tv1.getHeight() * multiplier - (8 * density);
					width3 = tv3.getWidth() * multiplier - (8 * density);
					isTextHide = false;
				} else {
					height1 = tv1.getHeight() / multiplier + (8 * density);
					width3 = tv3.getWidth() / multiplier + (8 * density);
					isTextHide = true;
				}

				RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
						tv1.getWidth(), height1);
				tv1.setLayoutParams(params1);

				ViewGroup.LayoutParams params3 = tv3.getLayoutParams();
				params3.width = width3;
				params3.height = tv3.getHeight();
				tv3.setLayoutParams(params3);
			}
		});

		button = (ImageButton) view.findViewById(R.id.goToNextPage);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagLakeToCrossFindObjects fb = new PagLakeToCrossFindObjects();
				onChoice.onChoiceMade(fb, PagLakeToCrossFindObjects.NAME, PagLakeToCrossFindObjects.icon);

				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagPathChoiceFrg fb = new PagPathChoiceFrg();

				onChoice.onChoiceMade(fb, PagPathChoiceFrg.NAME);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});
		
		//BookActivity.playMusic(R.raw.village);
		return view;
	}

	@Override
	public void onDetach() {
		Log.d("PagLateToCross ", "PagLateToCross  onDetach()");
		super.onDetach();

		bitmap1.recycle();
		bitmap1 = null;

		bitmap2.recycle();
		bitmap2 = null;
		
	}

}
