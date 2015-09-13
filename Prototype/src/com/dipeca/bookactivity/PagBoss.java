package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dipeca.item.DialogBox;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagBoss extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.terribleSob;
	public static int icon = R.drawable.final_boss_icon;

	private TextView tv1 = null;

	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;
	private ImageView ivMoving;
	private DialogBox dialog = null;
	private int density;

	private Bitmap bitmap1;
	private Bitmap bitmap2;
	private Bitmap bg;
	private Bitmap mg;
	
	private ImageView ivGui;
	private ImageView ivCat;

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
		tv1.setText(R.string.pagBossSob);

		dialog = new DialogBox(getActivity());
		dialog.setTextDialog(getString(R.string.pagBossDialog));
		dialog.setImg1Id(getResources().getDrawable(R.anim.gui_talking_left)); 
		dialog.setImg2Id(getResources().getDrawable(R.anim.boss_talking_anim));

		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				280 * density, RelativeLayout.LayoutParams.WRAP_CONTENT);

		rl.addRule(RelativeLayout.ALIGN_RIGHT, buttonNext.getId());
		rl.addRule(RelativeLayout.ABOVE, buttonNext.getId());

		dialog.setLayoutParams(rl);
		dialog.setBottom(48 * density);

		((RelativeLayout) view.getRootView()).addView(dialog, rl);

	}

	private int mAnimationRotateCount = 0;

	private void loadImages() {

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Log.d("size:", "w: " + width + " h: " + height + " density: " + density);

		Log.d("tag:dalvikvm- " + getString(NAME), "loadImages()");
		iv1 = (ImageView) view.findViewById(R.id.bg);
		iv2 = (ImageView) view.findViewById(R.id.mg);
		iv3 = (ImageView) view.findViewById(R.id.fg);

		ivGui = (ImageView) view.findViewById(R.id.ivWalk);
		ivCat  = (ImageView) view.findViewById(R.id.ivCat);
		
		ivMoving = new ImageView(getActivity());

		final ImageView ivMist = new ImageView(getActivity());

		bg = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.bossbackground, 600 * density, 300 * density);
		iv1.setImageBitmap(bg);
		Log.d("tag:dalvikvm- " + getString(NAME), "bossbackground()");
		
		mg = onChoice.decodeSampledBitmapFromResourceFG(getResources(),
				R.drawable.bossmiddleground, 600 * density, 300 * density);
		iv2.setImageBitmap(mg);
		Log.d("tag:dalvikvm- " + getString(NAME), "bossmiddleground()");
		
		Bitmap bmGui = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.gui_costas, 280 * density, 280 * density);
		Bitmap bmCat = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.gata_costas, 480 * density, 380 * density);
		
		ivGui.setImageBitmap(bmGui);
		ivCat.setImageBitmap(bmCat);
		
		ivCat.setVisibility(View.VISIBLE);
		
		Log.d("tag:dalvikvm- " + getString(NAME), "gui_costas()");
		
		Bitmap bmMist = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.magicmist, 480 * density, 180 * density);
		ivMist.setImageBitmap(bmMist);
		Log.d("tag:dalvikvm- " + getString(NAME), "magicmist()");
		
		
		iv3.setBackgroundResource(R.anim.fireanim);
		Log.d("tag:dalvikvm- " + getString(NAME), "fireanim");
		
		AnimationDrawable animSeq = (AnimationDrawable) iv3.getBackground();

		animSeq.start();
		// Mist
		RelativeLayout.LayoutParams paramsLayoutMist = new RelativeLayout.LayoutParams(
				2000 * density, 1000 * density);

		paramsLayoutMist.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		

		ivMist.setScaleType(ScaleType.FIT_XY);
		((RelativeLayout) view.getRootView()).addView(ivMist, 1,
				paramsLayoutMist);

		ivMist.setScaleType(ScaleType.CENTER);

		// Animation set
		Animation scaleA = new ScaleAnimation(2F, 3F, 1.5F, 3F,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		scaleA.setInterpolator(new CycleInterpolator(100f));
		scaleA.setDuration(800000);

		Animation alphaAnimation = new AlphaAnimation(0.1f, 0.6f);
		alphaAnimation.setDuration(5000);
		// alphaAnimation.setStartOffset(1000);
		//
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.setInterpolator(new CycleInterpolator(100f));
		animationSet.setRepeatCount(Animation.INFINITE);
		animationSet.setDuration(1000000);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(scaleA);
		// animationSet.addAnimation(translateAnimation);

		ivMist.setAnimation(animationSet);
		ivMist.getAnimation().start();

		animationSet.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				ivMist.setVisibility(View.GONE);

			}
		});

		int mGrassWidth = 180;
		int mGrassHeight = 140;

		if (height >= 800) {
			mGrassWidth = 260;
			mGrassHeight = 198;
		}

		// grass vibrating
		RelativeLayout.LayoutParams paramsLayout = new RelativeLayout.LayoutParams(
				mGrassWidth * density, mGrassHeight * density);
		paramsLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		paramsLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		// Build moving animation
		final Animation animationRotate = new RotateAnimation(-1, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				1.0f);
		animationRotate.setRepeatCount(5);
		animationRotate.setDuration(100);

		ivMoving.setAnimation(animationRotate);
		animationRotate.start();
		Log.d("tag:dalvikvm- " + getString(NAME), "ivMoving animation");
		
		
		// Build moving animation
		Animation animationGui = new ScaleAnimation(1f, 1.005f, 1f, 1.005f, .5f,
				.5f);
		animationGui.setDuration(800); 
		animationGui.setRepeatCount(Animation.INFINITE);
		animationGui.setRepeatMode(Animation.REVERSE);
		// SEt moving animation
		ivGui.setAnimation(animationGui);
		ivCat.setAnimation(animationGui);
		// Start moving animation
		ivGui.getAnimation().start();
		ivCat.getAnimation().start();
		Log.d("tag:dalvikvm- " + getString(NAME), "ivMoving animation");
		animationRotate.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				Log.d("dalvikvm-heap",
						"animation starts  " + animationRotate.hashCode());
			}
 
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
 
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Log.d("dalvikvm-heap", "animation rotate ends "
						+ animationRotate.hashCode());

				mAnimationRotateCount++;
				if (mAnimationRotateCount < 20) {
					animationRotate.reset();
					animationRotate.setRepeatCount(5);
					animationRotate.setDuration(200);
					animationRotate.start();
				}

			}
		});
	}

	private ImageButton buttonNext;
	private ImageButton buttonPrev;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);

		//plays the sound
		BookActivity.playMusic(R.raw.boss);
		
		view = inflater.inflate(R.layout.pag_3_images, container, false);

		// BookActivity.playMusic(R.raw.robot_in_front);
		buttonNext = (ImageButton) view.findViewById(R.id.goToNextPage);

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagBossCapture fb = new PagBossCapture();

				onChoice.onChoiceMade(fb, getString(PagBossCapture.NAME),
						getResources().getResourceName(PagBossCapture.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);
			}
		});

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagEnterGateThiefsLand fb = new PagEnterGateThiefsLand();

				onChoice.onChoiceMade(
						fb,
						getString(PagEnterGateThiefsLand.NAME),
						getResources().getResourceName(
								PagEnterGateThiefsLand.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		// loadImages
		loadImages();
		loadText();

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pagBossSob), bg));

		
		// Add buttonNext to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);
		
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
		return PagFindFriend.class.getName();
	}

}
