package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PagSomethingMoving extends Fragment implements IFragmentBook{
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.somethingMoving;
	public static int icon = R.drawable.icon_algo_a_mexer;

	private TextView tv1 = null;
	private TextView tv2 = null;

	private ImageView iv1;
	private int density;

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

	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv2 = (TextView) view.findViewById(R.id.textPag1_2);
	//	tv1.setBackgroundResource(R.drawable.container_dropshadow_red);
	//	tv1.setPadding(density * 16, density * 16, density * 16, density * 16);
		
		tv2.setVisibility(View.GONE);
		tv1.setText(R.string.pagSomethingMoving);
	}
	
	private void loadImages() {
		Log.d(getString(NAME), "loadImages()");
		iv1 = (ImageView) view.findViewById(R.id.pag1ImageView);
		//iv2 = (ImageView) view.findViewById(R.id.clickable);

		density = (int) getResources().getDisplayMetrics().density;

		iv1.setImageDrawable(getResources().getDrawable(R.anim.grass));
		((AnimationDrawable) iv1.getDrawable()).start();

	}

	ImageButton button = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_one_image, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d(getString(NAME), "onCreateView after inflate time =" + totalTime);

		// loadImages()
		loadImages();
		loadText();

		//BookActivity.playMusic(R.raw.robot_in_front);
		final ImageButton buttonNext = (ImageButton) view
				.findViewById(R.id.goToNextPage);
		
				buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagFindFriend fb = new PagFindFriend();

				onChoice.onChoiceMade(fb, getString(PagFindFriend.NAME), getResources().getResourceName(PagFindFriend.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});
		
		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);
		
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagVillageAfterEnigmaFrg fb = new PagVillageAfterEnigmaFrg();

				onChoice.onChoiceMade(fb, getString(PagVillageAfterEnigmaFrg.NAME), getResources().getResourceName(PagVillageAfterEnigmaFrg.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});
		
		return view;
	}

	@Override
	public void onDetach() {
		Log.d(getString(NAME), " onDetach()");
		super.onDetach();

		if (bitmap1 != null) {
			bitmap1.recycle();
			bitmap1 = null;
		}

		if (bitmap2 != null) {
			bitmap2.recycle();
			bitmap2 = null;
		}

	}

	@Override
	public String getPrevPage() {
		return PagVillageAfterEnigmaFrg.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}

}
