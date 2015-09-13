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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagDungeonInside extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.pagDungeonInsideName;
	public static int icon = R.drawable.icon_inside_dungeon;

	private TextView tv1 = null;

	private ImageView ivBg;
	private ImageView ivSpider;
	private int density;

	private Bitmap bg;
	private Bitmap bmLightDust;


	private ImageView ivCatWalking;

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
		tv1.setText(R.string.pagSomethingMoving);
		tv1.setVisibility(View.GONE);
	}

	private ImageView ivLightDust;
	private ImageView ivWaterFlow1;

	// private ImageView ivWaterFlow4;

	private void loadImages() {

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Log.d("size:", "w: " + width + " h: " + height + " density: " + density);

		Log.d("dalvikvm-heap dungeon:", "load images inside dungeon");
		ivBg = (ImageView) view.findViewById(R.id.bg);
		ivSpider = (ImageView) view.findViewById(R.id.fg);

		ivCatWalking = (ImageView) view.findViewById(R.id.ivCatWalk);
		ivCatWalking.getLayoutParams().height = 220 * density;

		ivLightDust = new ImageView(getActivity());
		ivWaterFlow1 = new ImageView(getActivity());
		bg = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.dungeon_inside_bg, 600 * density, 300 * density);
		ivBg.setImageBitmap(bg);
		Log.d("dalvikvm-heap dungeon:", "dungeon_inside_bg");
		
		bmLightDust = onChoice.decodeSampledBitmapFromResourceFG(getResources(),
				R.drawable.dungeon_inside_mg, 280 * density, 140 * density);
		ivLightDust.setImageBitmap(bmLightDust);
		Log.d("dalvikvm-heap dungeon:", "dungeon_inside_mg");
		
		ivSpider.setBackgroundResource(R.anim.spider_anim);
		Log.d("dalvikvm-heap dungeon:", "spider_anim");
		ivSpider.getLayoutParams().height = 48 * density;
		ivSpider.getLayoutParams().width = 48 * density;
		RelativeLayout.LayoutParams paramsLayoutSpider = (LayoutParams) ivSpider.getLayoutParams();
		paramsLayoutSpider.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsLayoutSpider.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		paramsLayoutSpider.setMargins(98*density, 98*density, 0, 0);
		
		// Mist
		RelativeLayout.LayoutParams paramsLayoutLightDust = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

		paramsLayoutLightDust.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		paramsLayoutLightDust.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		ivLightDust.setScaleType(ScaleType.FIT_XY);
		
		((RelativeLayout) view.getRootView()).addView(ivLightDust, 4,
				paramsLayoutLightDust);


		RelativeLayout.LayoutParams paramsLayoutWFt = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		ivWaterFlow1.setScaleType(ScaleType.FIT_XY);
		((RelativeLayout) view.getRootView()).addView(ivWaterFlow1, 3,
				paramsLayoutWFt); 

	} 
 
	private void loadAnimations() {
		// Light Dust
		Log.d("dalvikvm-heap dungeon:", "loadAnimations");
		Animation animationRotateLightParticules = new RotateAnimation(-8f,8f);
	    animationRotateLightParticules.setRepeatMode(Animation.REVERSE);
	    animationRotateLightParticules.setRepeatCount(20);
		animationRotateLightParticules.setDuration(12000);
		ivLightDust.setAnimation(animationRotateLightParticules);
		ivLightDust.getAnimation().start();
		animationRotateLightParticules.start(); 
		
		ivWaterFlow1.setImageResource(R.anim.water_flow_inside_anim);

		((AnimationDrawable) ivWaterFlow1.getDrawable()).start();
 
		// Cat animation
		ivCatWalking.setBackgroundResource(R.anim.dungeon_cat_walking);
		// get animation to variable
		((AnimationDrawable) ivCatWalking.getBackground()).start();
		// Build moving animation
		Animation translationCat = new TranslateAnimation(0 - 100 * density,
				((getResources().getDisplayMetrics().widthPixels)) * density,
				48 * density, 48 * density);
		translationCat.setDuration(16000);

		// SEt moving animation
		ivCatWalking.setAnimation(translationCat);
		// Start moving animation 
		ivCatWalking.getAnimation().start();

		translationCat.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			} 
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}  
			
			@Override
			public void onAnimationEnd(Animation animation) {
				((AnimationDrawable) ivCatWalking.getBackground()).stop();
				ivCatWalking.setVisibility(View.GONE);
				ivCatWalking.setBackgroundResource(0);
			}
		}); 
		// Foreground Spider
		final Animation animationRotate = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0f);
		animationRotate.setRepeatMode(Animation.REVERSE);
		// animationRotate.setInterpolator(new AccelerateInterpolator());
		animationRotate.setRepeatCount(8);
		animationRotate.setDuration(10000);
		
		final Animation animationSpiderTrans = new TranslateAnimation(-40, 40,
				-10,30);
		animationSpiderTrans.setRepeatMode(Animation.REVERSE);
		
		ivSpider.setAnimation(animationRotate);
		ivSpider.getAnimation().start();
		// ivSpider animation
		// get animation to variable
		((AnimationDrawable) ivSpider.getBackground()).start();
		
		animationRotate.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				((AnimationDrawable) ivWaterFlow1.getDrawable()).stop();
				((AnimationDrawable) ivSpider.getBackground()).stop();
				ivSpider.setVisibility(View.GONE);
				ivSpider.setBackgroundResource(0);
			}
		});
		
		
		Log.d("dalvikvm-heap dungeon:", "end loadAnimations");
	}

	ImageButton btnNext = null; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_3_images, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d(getString(NAME), "onCreateView after inflate time =" + totalTime);

		// loadImages()
		loadImages();
		loadText();
		loadAnimations();

		// BookActivity.playMusic(R.raw.robot_in_front);
		final ImageButton buttonNext = (ImageButton) view
				.findViewById(R.id.goToNextPage);

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagDungeonPrevChallenge fb = new PagDungeonPrevChallenge();

				onChoice.onChoiceMade(fb, getString(PagDungeonPrevChallenge.NAME),
						getResources().getResourceName(PagDungeonPrevChallenge.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);
			}
		});

		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagDungeon fb = new PagDungeon();

				onChoice.onChoiceMade(
						fb,
						getString(PagDungeon.NAME),
						getResources().getResourceName(
								PagVillageAfterEnigmaFrg.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pagSomethingMoving), bg));

		BookActivity.playMusic(R.raw.dungeon_water_drop);
		
		return view;
	}

	@Override
	public void onDetach() {
		Log.d(getString(NAME), " onDetach()");
		super.onDetach();
	}

	@Override
	public String getPrevPage() {
		return PagDungeon.class.getName();
	}

	@Override
	public String getNextPage() {
		return PagEnigmaMoveRocksFrg.class.getName();
	}

}
