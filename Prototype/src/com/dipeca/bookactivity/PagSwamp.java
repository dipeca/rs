package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagSwamp extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.theSwamp;
	public static int icon = R.drawable.swamp_icon;

	private TextView tv1 = null;

	private ImageView iv1;
	private ImageView iv3;
	private ImageView ivMoving;
	private ImageView ivMist;
	
	private int density;

	private Bitmap bmVibrate;

	private AnimationDrawable backGroundWalkingGuiAnim;
	private AnimationDrawable backGroundWalkingCatAnim;

	private ImageView ivGuiWalking;
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
		tv1.setText(R.string.pagLakeToCross);

	}

	private void loadImages() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x; 
		int height = size.y;
		Log.d("size:", "w: " + width + " h: " + height + " density: " + density);

		Log.d("dalvikvm- " + getString(NAME), "loadImages()"); 
		iv1 = (ImageView) view.findViewById(R.id.bg);
		iv3 = (ImageView) view.findViewById(R.id.fg);
   
		ivGuiWalking = (ImageView) view.findViewById(R.id.ivWalk);
		ivCatWalking = (ImageView) view.findViewById(R.id.ivCatWalk);
		ivMoving = new ImageView(getActivity());

		ivMist = new ImageView(getActivity());
  
		Bitmap bg = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.pantano, 480 * density, 280 * density);
		iv1.setImageBitmap(bg); 
		Log.d("dalvikvm- " + getString(NAME), "pantano"); 
		
		Bitmap fg = onChoice.decodeSampledBitmapFromResourceFG(getResources(),
				R.drawable.pant_foreground2, 480 * density, 280 * density);
		iv3.setImageBitmap(fg);
		Log.d("dalvikvm- " + getString(NAME), "pant_foreground2"); 
		bmVibrate = Utils.decodeSampledBitmapFromResource(
				getResources(), R.drawable.pant_objecto, 180 * density,
				120 * density);
		Log.d("dalvikvm- " + getString(NAME), "pant_objecto"); 
		
		Bitmap bmMist = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.pantanomist, 240 * density, 120 * density);
		ivMist.setImageBitmap(bmMist);
		Log.d("dalvikvm- " + getString(NAME), "pantanomist"); 
		

		// Mist
		RelativeLayout.LayoutParams paramsLayoutMist = new RelativeLayout.LayoutParams(
				2000 * density, 1000 * density);

		paramsLayoutMist.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		ivMist.setScaleType(ScaleType.FIT_XY);
		((RelativeLayout) view.getRootView()).addView(ivMist, 6,
				paramsLayoutMist);

		
		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pagLake), bg));
		
	}
	
	private void loadAnimations(){
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int height = size.y;
		
		// Animation set
		Animation translateAnimation = new TranslateAnimation(0 * density,
				16000 * density, 98 * density, 98 * density);
 
		Animation scaleA = new ScaleAnimation(3.0F, 6.0F, 1.5F, 1.5F,
				ScaleAnimation.RELATIVE_TO_PARENT, 1f,
				ScaleAnimation.RELATIVE_TO_SELF, 1f);
		scaleA.setDuration(5000);
		// scaleA.setInterpolator(new AccelerateInterpolator());

		Animation alphaAnimation = new AlphaAnimation(.4F, .8F);
		alphaAnimation.setDuration(50000);

		AnimationSet animationSet = new AnimationSet(true);

		// animationSet.setInterpolator(new BounceInterpolator());
		animationSet.setDuration(100000);
		animationSet.addAnimation(alphaAnimation); 
		animationSet.addAnimation(scaleA);
		animationSet.addAnimation(translateAnimation);

		ivMist.setAnimation(animationSet);
		ivMist.getAnimation().start();

		animationSet.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				if (Debug.isDebuggerConnected()) {
					Log.d("Animation listener", "start");
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				if (Debug.isDebuggerConnected()) {
					Log.d("Animation listener", "start");
				}
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				ivMist.setVisibility(View.GONE);
			}
		});

		int mGrassWidth = 160;
		int mGrassHeight = 200;

		if (height >= 800) {
			mGrassWidth = 260;
			mGrassHeight = 360;
		}

		// grass vibrating
		RelativeLayout.LayoutParams paramsLayout = new RelativeLayout.LayoutParams(
				mGrassWidth * density, mGrassHeight * density);
		paramsLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		ivMoving.setImageBitmap(bmVibrate);
		ivMoving.setY(-48 * density);

		((RelativeLayout) view.getRootView())
				.addView(ivMoving, 4, paramsLayout);

		// Build moving animation
		final Animation animationRotate = new RotateAnimation(-10, 25,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0f);
		animationRotate.setRepeatMode(Animation.REVERSE);
		// animationRotate.setInterpolator(new AccelerateInterpolator());
		animationRotate.setRepeatCount(50);
		animationRotate.setDuration(8000);

		ivMoving.setAnimation(animationRotate);
		animationRotate.start();
 
		Log.d("dalvikvm-", "Set bg animation");
		// Set animation
		ivGuiWalking.setBackgroundResource(R.anim.swamp_gui_walk);
		Log.d("dalvikvm-", "swamp_gui_walk");
		ivCatWalking.setBackgroundResource(R.anim.swamp_cat_walking);
		Log.d("dalvikvm-", "swamp_cat_walking");
		// get animation to variable
		backGroundWalkingGuiAnim = (AnimationDrawable) ivGuiWalking
				.getBackground();
		backGroundWalkingCatAnim = (AnimationDrawable) ivCatWalking
				.getBackground();
		// Build moving animation
		Animation animationGui = new TranslateAnimation(-240 * density,
				((getResources().getDisplayMetrics().widthPixels)) * density,
				-24 * density, -98 * density);
		animationGui.setDuration(20000);

		// SEt moving animation
		ivGuiWalking.setAnimation(animationGui);
		ivCatWalking.setAnimation(animationGui);

		// Start walking animation
		backGroundWalkingGuiAnim.start();
		backGroundWalkingCatAnim.start();
		// Start moving animation
		ivGuiWalking.getAnimation().start();
		ivCatWalking.getAnimation().start();

		ivGuiWalking.getAnimation().setAnimationListener(
				new Animation.AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						Log.d(NAME + "gui animation", "start");
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						Log.d(NAME + "gui animation", "repeat");

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Log.d(NAME + "gui animation", "end");

						backGroundWalkingGuiAnim.stop();
						ivGuiWalking.setVisibility(View.GONE);
						ivGuiWalking.setBackgroundResource(0);

						backGroundWalkingCatAnim.stop();
						ivCatWalking.setVisibility(View.GONE);
						ivCatWalking.setBackgroundResource(0);
					}
				});
	}

	private ImageButton buttonNext;
	private ImageButton buttonPrev;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_3_images, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d(getString(NAME), "onCreateView after inflate time =" + totalTime);

		//Play music
		BookActivity.playMusic(R.raw.swamp);
		
		buttonNext = (ImageButton) view.findViewById(R.id.goToNextPage);

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagLakeToCrossFindObjects fb = new PagLakeToCrossFindObjects();

				onChoice.onChoiceMade(fb, getString(PagLakeToCrossFindObjects.NAME),
						getResources().getResourceName(PagLakeToCrossFindObjects.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagPathChoiceFrg fb = new PagPathChoiceFrg();

				onChoice.onChoiceMade(
						fb,
						getString(PagPathChoiceFrg.NAME),
						getResources().getResourceName(
								PagPathChoiceFrg.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		// loadImages
		loadImages();
		loadText();
		loadAnimations();

		return view;
	}

	@Override
	public void onDetach() {
		Log.d(getString(NAME), " onDetach()");
		super.onDetach();

	}

	@Override
	public String getPrevPage() {
		return PagPathChoiceFrg.class.getName();
	}

	@Override
	public String getNextPage() {
		return PagLakeToCrossFindObjects.class.getName();
	}

}
