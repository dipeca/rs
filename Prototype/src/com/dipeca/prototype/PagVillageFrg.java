package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PagVillageFrg extends Fragment {
	private IMainActivity onChoice;
	public static String NAME = "Terra de L‡";
	private static String iconNextPage = "enigma_icon";

	private TextView tv1 = null;
	private TextView tv2 = null;
	private DialogBox dialogB = null;

	AnimationDrawable backGroundChangeAnimJake;
	AnimationDrawable backGroundChangeAnimGui;

	private ImageView iv1;

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

	View view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater
				.inflate(R.layout.pag_one_image_dialog, container, false);

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time pag2", "onCreateView after inflate time ="
				+ totalTime);

		loadText();

		final ImageButton button = (ImageButton) view
				.findViewById(R.id.goToNextPage);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagEnigmaFrg fb = new PagEnigmaFrg();

				onChoice.onChoiceMade(fb, PagEnigmaFrg.NAME, iconNextPage);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});
		
		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);
		
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagKingDomfrg fb = new PagKingDomfrg();

				onChoice.onChoiceMade(fb, PagKingDomfrg.NAME, iconNextPage);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		loadImages();

		return view;
	}

	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagVillage);

		dialogB = (DialogBox) view.findViewById(R.id.dialog);
		dialogB.setTextDialog(getString(R.string.welcome));
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				dialogB.getLayoutParams().width,
				dialogB.getLayoutParams().height);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params1.addRule(RelativeLayout.ALIGN_TOP, R.id.textPag1);
		dialogB.setLayoutParams(params1);

		tv2 = (TextView) view.findViewById(R.id.textPag1_2);
		tv2.setVisibility(View.GONE);
		
		
		dialogB.setImg1Id(getResources().getDrawable(R.anim.anciao_anim));
		dialogB.setImg2Id(getResources().getDrawable(R.anim.gui_anim));
	}

	private void loadImages() {

		long startTime = System.currentTimeMillis();

		iv1 = (ImageView) view.findViewById(R.id.pag1ImageView);

		int density = (int) getResources().getDisplayMetrics().density;

		Log.d(NAME, "density: " + density);
		Bitmap village = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.village, 600, 300);
		iv1.setImageBitmap(village);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(" Village loadImages Total time: " + totalTime);
	}

}
