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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PagAfterChallenge extends Fragment {
	private IMainActivity onChoice;
	public static int NAME = R.string.adventureBegins;
	public static int icon = R.drawable.caminho_somebody_icon;

	AnimationDrawable backGroundChangeAnimJake;
	AnimationDrawable backGroundChangeAnimGui;

	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;
	private ImageView ivWalking;

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

	@Override
	public void onStart() {
		super.onStart();

		ivWalking.setBackgroundResource(R.anim.gui_walk);
		backGroundChangeAnim = (AnimationDrawable) ivWalking.getBackground();

		Animation animation = new TranslateAnimation(0, (getResources()
				.getDisplayMetrics().widthPixels / 2 + 10) * density, -8
				* density, -8 * density);
		animation.setDuration(10000);

		ivWalking.setAnimation(animation);

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

						dialogBox.setVisibility(View.GONE);
						ivWalking.setVisibility(View.GONE);

						tv2.setText(R.string.on_the_road2);
						iv1.setImageBitmap(caminhoBm);
					}
				});

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
		ivWalking = (ImageView) view.findViewById(R.id.ivWalk);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setVisibility(View.GONE);

		tv2 = (TextView) view.findViewById(R.id.textPag2);
		tv2.setText(R.string.on_the_road);

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

				onChoice.onChoiceMade(fb, PagPathChoiceFrg.NAME,
						PagPathChoiceFrg.icon);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagFindFriend fb = new PagFindFriend();

				onChoice.onChoiceMade(fb, PagFindFriend.NAME,
						PagFindFriend.icon);
				onChoice.onChoiceMadeCommit(NAME, false);
			}
		});

		loadImages();

		BookActivity.playMusic(R.raw.midnight_walk);
		return view;
	}

	private static Bitmap caminhoSomebodyBm = null;
	private static Bitmap caminhoBm = null;
	int density = 1;

	private void loadImages() {
		Log.d(getString(NAME), "loadImages()");

		density = (int) getResources().getDisplayMetrics().density;

		iv3.setVisibility(View.INVISIBLE);

		iv1.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		caminhoSomebodyBm = Utils.decodeSampledBitmapFromResource(
				getResources(), R.drawable.caminho_dia, 600, 300);
		iv1.setImageBitmap(caminhoSomebodyBm);

		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rlp.addRule(RelativeLayout.BELOW, iv1.getId());
		rlp.setMargins(0, 16 * density, 0, 0);
		iv2.setLayoutParams(rlp);
		iv2.setVisibility(View.GONE);

		caminhoBm = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.caminho_dia_watching, 800, 400);
		iv2.setImageBitmap(caminhoBm);

	}

	AnimationDrawable backGroundChangeAnim;

	@Override
	public void onDetach() {
		Log.d("PagAfterChallenge ", "PagLateToCross  onDetach()");
		super.onDetach();

		if (caminhoSomebodyBm != null) {
			caminhoSomebodyBm.recycle();
			caminhoSomebodyBm = null;
		}

		if (caminhoBm != null) {
			caminhoBm.recycle();
			caminhoBm = null;
		}
	}
}
