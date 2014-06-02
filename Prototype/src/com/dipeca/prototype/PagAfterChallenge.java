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
	public static String NAME = "Aventura começa";
	private static String iconNextPage = "choice_icon";

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

				onChoice.onChoiceMade(fb, PagPathChoiceFrg.NAME, iconNextPage);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});
		
		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);
		
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagVillageAfterEnigmaFrg fb = new PagVillageAfterEnigmaFrg();

				onChoice.onChoiceMade(fb, PagVillageAfterEnigmaFrg.NAME, iconNextPage);
				onChoice.onChoiceMadeCommit(NAME, false);
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
				LayoutParams.MATCH_PARENT, 350 * density));

		Bitmap caminhoSomebodyBm = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.caminho_somebody, 600, 300 );
		iv1.setImageBitmap(caminhoSomebodyBm);

		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 300 * density);
		rlp.addRule(RelativeLayout.BELOW, iv1.getId()); 
		rlp.setMargins(0, 16 * density, 0, 0); 
		iv2.setLayoutParams(rlp);
		
		Bitmap caminhoBm = Utils.decodeSampledBitmapFromResource(
				getResources(), R.drawable.caminho, 600, 300);
		iv2.setImageBitmap(caminhoBm);


	}
	AnimationDrawable backGroundChangeAnim;

}
