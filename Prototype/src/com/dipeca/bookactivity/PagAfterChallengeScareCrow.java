package com.dipeca.bookactivity;

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
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dipeca.item.DialogBox;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.ObjectItem;
import com.dipeca.prototype.R;

public class PagAfterChallengeScareCrow extends Fragment implements
		IFragmentBook {
	private IMainActivity onChoice;
	public static int NAME = R.string.adventureGoesOn;
	public static int icon = R.drawable.caminho_dia_fim_icon;

	private ImageView ivWalking;

	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;

	private int density = 1;

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

	private View view = null;

	@Override
	public void onStart() {
		super.onStart();

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);
		ivWalking.setBackgroundResource(R.anim.gui_run);
		ivWalking.getLayoutParams().width = (int) Math.ceil(206 * density);
		ivWalking.getLayoutParams().height = (int) Math.ceil(380 * density);
		backGroundChangeAnim = (AnimationDrawable) ivWalking.getBackground();

		Animation rotateAnim = new RotateAnimation(0f, 15f);
		Animation animation = new TranslateAnimation(-240 * density,
				(getResources().getDisplayMetrics().widthPixels + 200)
						* density, -8 * density, -8 * density);
		animation.setDuration(4500);

		AnimationSet setAnim = new AnimationSet(true);

		setAnim.addAnimation(rotateAnim);
		setAnim.addAnimation(animation);

		setAnim.setInterpolator(new OvershootInterpolator());

		ivWalking.setAnimation(setAnim);

		backGroundChangeAnim.start();
		ivWalking.getAnimation().start();

		ivWalking.getAnimation().setAnimationListener(

		new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				Log.d(NAME + " animation", "start");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				Log.d(NAME + " animation", "repeat");

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Log.d(NAME + " animation", "end");
				backGroundChangeAnim.stop();

				ivWalking.setVisibility(View.GONE);

				backGroundChangeAnim.stop();
				ivWalking.setBackgroundResource(0);
			}
		});

		// refresh objects
		// if(!isBootleOnObjectList()){
		// onChoice.restartLoaderObjects();
		// }

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.pag_two_images, container, false);

		// loadImages
		loadImages();
		loadText();
		
		final ImageButton btnNext = (ImageButton) view
				.findViewById(R.id.goToNextPage);

		btnNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagDungeon fb = new PagDungeon();

				onChoice.onChoiceMade(fb, PagDungeon.NAME, PagDungeon.icon);
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

		return view;
	}

	private void loadText() {

		dialogBox = (DialogBox) view.findViewById(R.id.dialog);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.on_the_road_Again);

		tv2 = (TextView) view.findViewById(R.id.textPag2);
		tv2.setVisibility(View.GONE);

		// The dialogBox about having the corn depends on whatever the player
		// found it or not
		ObjectItem oiCorn = new ObjectItem();
		oiCorn.setObjectImageType(ObjectItem.TYPE_CORN);
		if (onChoice.isInObjects(oiCorn)) {

			dialogBox.setTextDialog(getString(R.string.onTheRoadAgainDialog));
			dialogBox.setImg1Id(getResources().getDrawable(R.anim.gui_anim));
			RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
					dialogBox.getLayoutParams().width,
					dialogBox.getLayoutParams().height);
			params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params1.addRule(RelativeLayout.ALIGN_TOP, R.id.textPag1);
			dialogBox.setLayoutParams(params1);
		} else {
			dialogBox.setVisibility(View.GONE);
		}

	}

	private void loadImages() {
		Log.d(getString(NAME), "loadImages()");

		iv1 = (ImageView) view.findViewById(R.id.page2Image);
		iv2 = (ImageView) view.findViewById(R.id.page2Image2);
		iv3 = (ImageView) view.findViewById(R.id.page2Image3);
		ivWalking = (ImageView) view.findViewById(R.id.ivWalk); 

		iv3.setVisibility(View.INVISIBLE);

		iv1.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		Bitmap caminhoSomebodyBm = onChoice.decodeSampledBitmapFromResourceBG(
				getResources(), R.drawable.caminho_dia_scarecrow, 400 * density,
				200 * density);
		iv1.setImageBitmap(caminhoSomebodyBm);

		iv2.setVisibility(View.GONE);

		onChoice.setCurrentMapPosition(R.drawable.mapa_espantalho);
		// Add buttonNext to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);
	}

	AnimationDrawable backGroundChangeAnim;

	@Override
	public String getPrevPage() {
		return PagScareCrow.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (backGroundChangeAnim != null) {
			backGroundChangeAnim.stop();
			ivWalking.setBackgroundResource(0);
		}
	}

}
