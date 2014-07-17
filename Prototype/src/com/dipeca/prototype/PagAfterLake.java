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

public class PagAfterLake extends Fragment {
	private IMainActivity onChoice;
	public static int NAME = R.string.afterLakeTitle;
	public static int icon = R.drawable.caminho_dia_fim_icon;

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

		final ImageButton buttonNext = (ImageButton) view
				.findViewById(R.id.goToNextPage);
		iv1 = (ImageView) view.findViewById(R.id.page2Image);
		iv2 = (ImageView) view.findViewById(R.id.page2Image2);
		iv3 = (ImageView) view.findViewById(R.id.page2Image3);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.afterLake);

		tv2 = (TextView) view.findViewById(R.id.textPag2);
		tv2.setVisibility(View.GONE);
		
		dialogBox = (DialogBox) view.findViewById(R.id.dialog);
		dialogBox.setVisibility(View.GONE);
		
		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagRobot fb = new PagRobot();

				onChoice.onChoiceMade(fb, PagRobot.NAME, PagRobot.icon);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});
		
		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);
		
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagScareCrow fb = new PagScareCrow();

				onChoice.onChoiceMade(fb, PagScareCrow.NAME, PagScareCrow.icon);
				onChoice.onChoiceMadeCommit(NAME, false);
			}
		});
   
		loadImages();
		
		//BookActivity.playMusic(R.raw.midnight_walk); 
		return view;
	}

	private void loadImages() { 
		Log.d(getString(NAME), "loadImages()");

		iv3.setVisibility(View.INVISIBLE);

		iv1.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		Bitmap caminhoSomebodyBm = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.caminho_dia_fim2, 600, 300 );
		iv1.setImageBitmap(caminhoSomebodyBm);
 
		iv2.setVisibility(View.GONE);

	}
	
	AnimationDrawable backGroundChangeAnim;

}
