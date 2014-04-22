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
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PagAfterChallenge extends Fragment {
	private IMainActivity onChoice;
	public static String NAME = "Pag Desafio anciao completo";

	AnimationDrawable backGroundChangeAnimJake;
	AnimationDrawable backGroundChangeAnimGui;

	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;

	private TextView tv1 = null;
	private TextView tv2 = null;
	private DialogBox dialogBox;

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
		
		view = inflater.inflate(R.layout.pag_two_images, container, false);

		final ImageButton button = (ImageButton) view
				.findViewById(R.id.goToNextPage);
		iv1 = (ImageView) view.findViewById(R.id.page2Image);
		iv2 = (ImageView) view.findViewById(R.id.page2Image2);
		iv3 = (ImageView) view.findViewById(R.id.page2Image3);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.on_the_road);

		tv2 = (TextView) view.findViewById(R.id.textPag2);
		tv2.setText(R.string.on_the_road2);
		
		dialogBox = (DialogBox) view.findViewById(R.id.dialog);
		dialogBox.setTextDialog(getString(R.string.on_the_roadThought));
		dialogBox.setImg1Id(null);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				dialogBox.getLayoutParams().width, 
				dialogBox.getLayoutParams().height);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params1.addRule(RelativeLayout.ALIGN_TOP, R.id.textPag1);
		dialogBox.setLayoutParams(params1);
		dialogBox.setImg2Id(getResources().getDrawable(R.anim.gui_anim));
		
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagPathChoiceFrg fb = new PagPathChoiceFrg();

				onChoice.onChoiceMade(fb, PagPathChoiceFrg.NAME, null);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		loadImages();
		
		BookActivity.playMusic(R.raw.midnight_walk); 
		return view;
	}

	private void loadImages() {
		Log.d(NAME, "loadImages()");

		int density = (int) getResources().getDisplayMetrics().density;
		
		iv3.setVisibility(View.INVISIBLE);

		iv1.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 400 * density));

		Bitmap mountain = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.caminho_somebody, iv1.getLayoutParams().width, iv1.getLayoutParams().height );
		iv1.setImageBitmap(mountain);

		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 250 * density);
		rlp.addRule(RelativeLayout.BELOW, iv1.getId()); 
		rlp.setMargins(0, 16 * density, 0, 0);
		iv2.setLayoutParams(rlp);
		Bitmap mountainDial = Utils.decodeSampledBitmapFromResource(
				getResources(), R.drawable.caminho, iv2.getLayoutParams().width, iv2.getLayoutParams().height);
		iv2.setImageBitmap(mountainDial);

		Animation animation1 = new TranslateAnimation(0.0f, 1000.0f, 0.0f,
				20.0f);
		animation1.setDuration(100000);
		animation1.setRepeatMode(Animation.RESTART);
		animation1.setRepeatCount(Animation.INFINITE);
		animation1.setInterpolator(new AnticipateOvershootInterpolator());

		//ImageView cloudImage = new ImageView(getActivity());
		//cloudImage.setBackgroundResource(R.anim.fog);
		// setting image position
		//RelativeLayout.LayoutParams lp =  new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		//		300*density);
		
		//cloudImage.setLayoutParams(lp);

		// Create a simple layout and add our image view to it.
		//RelativeLayout layout = (RelativeLayout) view
		//		.findViewById(R.id.baseLayout);
		// adding view to layout
		//layout.addView(cloudImage);

		// make visible to program
		//backGroundChangeAnim = (AnimationDrawable) cloudImage.getBackground();
		//backGroundChangeAnim.start();

		//cloudImage.startAnimation(animation1);
	}
	AnimationDrawable backGroundChangeAnim;

}
