package com.dipeca.prototype;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PagScareCrow extends Fragment {
	View view = null;
	private IMainActivity onChoice;
	public static String NAME = "KingDom";
	private TextView tv1 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;

	private static Bitmap bitmap1;
	private static Bitmap bitmap2;

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

	private void loadImages() {
		Log.d("KingDom ", "loadImages()");
		image1 = (ImageView) view.findViewById(R.id.pag1ImageView);
		image2 = (ImageView) view.findViewById(R.id.pag1ImageViewBirds);

		bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.nuvem, 600, 150);
		image1.setImageBitmap(bitmap1);
		
		bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.chao, 600, 300);
		image2.setImageBitmap(bitmap2);
		
		int density = (int) getResources().getDisplayMetrics().density;
		Animation animation = new TranslateAnimation(0 * density, 2500
				* density, density * 0, density * 0);
		animation.setDuration(25000);
		animation.setRepeatCount(Animation.INFINITE);


		image1.setAnimation(animation);


		image1.getAnimation().start();


	}

	ImageButton button = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.scarecrow, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time kingdom", "onCreateView after inflate time ="
				+ totalTime);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pag2_1);

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

				PagVillageFrg fb = new PagVillageFrg();

				onChoice.onChoiceMade(fb, PagVillageFrg.NAME);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		BookActivity.playMusic(R.raw.village);
		return view;
	}

	@Override
	public void onDetach() {
		Log.d("Kingdom ", "Kingdom  onDetach()");
		super.onDetach();

		bitmap1.recycle();
		bitmap1 = null;

	}

	AnimationDrawable backGroundChangeAnim;

}
