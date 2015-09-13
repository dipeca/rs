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
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dipeca.item.DialogBox;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagDungeon extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.pagDungeonName;
	public static int icon = R.drawable.icon_dungeon;

	private TextView tv1 = null;

	private ImageView iv1;
	private ImageView iv3;
	private DialogBox dialog = null;
	private int density;

	private Bitmap bitmap1;
	private Bitmap bitmap2;
	private Bitmap bmWaterFlow4;
	private Bitmap bg;
	private Bitmap fg;
	private Bitmap bmLeaves;
	private ImageButton buttonNext = null;

	private AnimationSet animationSetWF1;
	private AnimationSet animationSetWF2;
	private AnimationSet animationSetWF3;

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
		tv1.setText(R.string.pagDungeon); 
	 
		dialog = new DialogBox(getActivity());
		dialog.setTextDialog(getString(R.string.youMustGoAlone));
		dialog.setImg1Id(getResources().getDrawable(R.anim.gui_talking_left));
		dialog.setImg2Id(getResources().getDrawable(R.drawable.gata_zangada)); 

		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				240 * density, RelativeLayout.LayoutParams.WRAP_CONTENT);
		rl.addRule(RelativeLayout.ALIGN_RIGHT, buttonNext.getId());
		rl.addRule(RelativeLayout.ALIGN_TOP, tv1.getId());

		dialog.setLayoutParams(rl);
		dialog.setBottom(48 * density);

		((RelativeLayout) view.getRootView()).addView(dialog, rl);

	}

	private ImageView ivLeaves;
	private ImageView ivWaterFlow1;
	private ImageView ivWaterFlow4;

	private void loadImages() {

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Log.d("size:", "w: " + width + " h: " + height + " density: " + density);
		Log.d("dalvikvm-heap dungeon:", "loadImages");
		Log.d(getString(NAME), "loadImages()");
		iv1 = (ImageView) view.findViewById(R.id.bg);
		iv3 = (ImageView) view.findViewById(R.id.fg);


		ivLeaves = new ImageView(getActivity());
		ivWaterFlow1 = new ImageView(getActivity());
		ivWaterFlow4 = new ImageView(getActivity());
		
		bg = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.dungeon_bg, 400 * density, 200 * density);
		iv1.setImageBitmap(bg);
		// Bitmap mg = Utils.decodeSampledBitmapFromResource(getResources(),
		// R.drawable.dungeon_fg1, 600 * density, 300 * density);
		Log.d("dalvikvm-heap dungeon:", "dungeon_bg");
		
		fg = onChoice.decodeSampledBitmapFromResourceFG(getResources(),
				R.drawable.dungeon_fg2, 400 * density, 200 * density);
		iv3.setImageBitmap(fg);
		Log.d("dalvikvm-heap dungeon:", "dungeon_fg2");
		// Bitmap bmVibrate = Utils.decodeSampledBitmapFromResource(
		// getResources(), R.drawable.grass, 180 * density, 120 * density);
		bmLeaves = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.dungeon_leafs, 340 * density, 172 * density); 
		ivLeaves.setImageBitmap(bmLeaves);
		Log.d("dalvikvm-heap dungeon:", "dungeon_leafs");
		bmWaterFlow4 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.bg_splash, 140, 80);
		ivWaterFlow4.setImageBitmap(bmWaterFlow4);
		Log.d("dalvikvm-heap dungeon:", "bg_splash");
		
		// Mist
		RelativeLayout.LayoutParams paramsLayoutMist = new RelativeLayout.LayoutParams(
				600 * density, 300 * density);

		paramsLayoutMist.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		

		ivLeaves.setScaleType(ScaleType.FIT_XY);
		((RelativeLayout) view.getRootView()).addView(ivLeaves, 1,
				paramsLayoutMist);

		ivLeaves.setScaleType(ScaleType.CENTER);

		RelativeLayout.LayoutParams paramsLayoutWF = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		paramsLayoutWF.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		paramsLayoutWF.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		ivWaterFlow4.setScaleType(ScaleType.FIT_XY);
		ivWaterFlow1.setScaleType(ScaleType.FIT_XY);

		((RelativeLayout) view.getRootView()).addView(ivWaterFlow1, 3,
				paramsLayoutWF);
		((RelativeLayout) view.getRootView()).addView(ivWaterFlow4, 3,
				paramsLayoutWF);
		// ivWaterFlow1.setVisibility(View.GONE);

		final Animation animationRotateWaterFlowSplash = new RotateAnimation(
				0f, -0.5f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 1f);
		animationRotateWaterFlowSplash.setRepeatMode(Animation.REVERSE);
		animationRotateWaterFlowSplash.setRepeatCount(400);
		animationRotateWaterFlowSplash.setDuration(50);

		final Animation alphaAnim = new AlphaAnimation(0f, 0.5f);
		alphaAnim.setDuration(500);
		alphaAnim.setRepeatMode(Animation.REVERSE);

		final Animation translateAnimationWater1 = new TranslateAnimation(
				0 * density, 0 * density, -48 * density, 420 * density);
		Animation translateAnimationWater2 = new TranslateAnimation(
				0 * density, 64 * density, 0 * density, 420 * density);
		Animation translateAnimationWater3 = new TranslateAnimation(
				0 * density, -64 * density, 0 * density, 420 * density);

		translateAnimationWater1.setDuration(2000);
		translateAnimationWater2.setDuration(2000);
		translateAnimationWater3.setDuration(2000);

		translateAnimationWater2.setInterpolator(new LinearInterpolator());
		translateAnimationWater3.setInterpolator(new LinearInterpolator());

		final Animation animationRotateWaterFlow = new RotateAnimation(0f,
				-0.5f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 1f);

		animationSetWF1 = new AnimationSet(true);
		animationSetWF1.addAnimation(translateAnimationWater1);
		animationSetWF1.addAnimation(animationRotateWaterFlow);

		animationSetWF2 = new AnimationSet(true);
		animationSetWF2.addAnimation(translateAnimationWater2);
		animationSetWF2.addAnimation(alphaAnim);

		animationSetWF3 = new AnimationSet(true);
		animationSetWF3.addAnimation(translateAnimationWater3);
		animationSetWF3.addAnimation(alphaAnim);

		ivWaterFlow4.setAnimation(animationRotateWaterFlowSplash);
		ivWaterFlow4.getAnimation().start();

		// Animation set
		final Animation animationRotateLeafs = new RotateAnimation(40, -40,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				.8f);
		animationRotateLeafs.setRepeatMode(Animation.REVERSE);
		animationRotateLeafs.setRepeatCount(6);
		animationRotateLeafs.setStartOffset(50);
		animationRotateLeafs.setDuration(1500);

		final Animation scaleLeafs = new ScaleAnimation(1.5f, 0.0f, 1.5f, 0.0f);
		scaleLeafs.setRepeatCount(1);
		scaleLeafs.setDuration(10000);

		Animation translateAnimationLeafs = new TranslateAnimation(-800
				* density, 600 * density, -220 * density, -620 * density);
		translateAnimationLeafs.setDuration(8000);
		translateAnimationLeafs.setInterpolator(new LinearInterpolator());

		AnimationSet animationSetLeafs = new AnimationSet(true);
		animationSetLeafs.addAnimation(animationRotateLeafs);
		animationSetLeafs.addAnimation(translateAnimationLeafs);
		animationSetLeafs.addAnimation(scaleLeafs);

		ivLeaves.setAnimation(animationSetLeafs);
		ivLeaves.getAnimation().start();

		scaleLeafs.setAnimationListener(new Animation.AnimationListener() {

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
				ivLeaves.setVisibility(View.GONE);

			}
		});

		ivWaterFlow1.setImageResource(R.anim.water_flow_anim);

		
		AnimationDrawable backGroundChangeAnim = (AnimationDrawable) ivWaterFlow1
				.getDrawable();
		backGroundChangeAnim.start();
		Log.d("dalvikvm-heap dungeon:", " after water_flow_anim");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);

		//play sound of dungeon
		BookActivity.playMusic(R.raw.wind_dungeon);
		
		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_3_images, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d(getString(NAME), "onCreateView after inflate time =" + totalTime);

		buttonNext = (ImageButton) view
				.findViewById(R.id.goToNextPage);

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagDungeonInside fb = new PagDungeonInside();

				onChoice.onChoiceMade(fb, getString(PagDungeonInside.NAME),
						getResources().getResourceName(PagDungeonInside.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);

			}
		});

		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagScareCrow fb = new PagScareCrow();

				onChoice.onChoiceMade(
						fb,
						getString(PagScareCrow.NAME),
						getResources().getResourceName(
								PagScareCrow.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		loadImages();
		loadText();
		
		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pagSomethingMoving), bg));

		return view;
	}

	@Override
	public void onDetach() {
		Log.d(getString(NAME), " onDetach()");
		super.onDetach();

		iv1.setImageBitmap(null);
		iv3.setImageBitmap(null);
		ivLeaves.setImageBitmap(null);
		ivWaterFlow1.setImageBitmap(null);
		ivWaterFlow4.setImageBitmap(null);
		
		if (bitmap1 != null) {
			bitmap1.recycle();
			bitmap1 = null;
		}

		if (bitmap2 != null) {
			bitmap2.recycle();
			bitmap2 = null;
		}

		if (bmLeaves != null) {
			bmLeaves.recycle();
			bmLeaves = null;
		}
	}
 
	@Override
	public String getPrevPage() {
		return PagScareCrow.class.getName();
	}

	@Override
	public String getNextPage() {
		return PagDungeonInside.class.getName();
	}

}
