package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
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
import android.widget.TextView;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagSomethingMoving extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.somethingMoving;
	public static int icon = R.drawable.icon_algo_a_mexer;

	private TextView tv1 = null;
	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;
	private ImageView ivMoving;
	private int density;

	private AnimationDrawable backGroundWalkingGuiAnim;

	private ImageView ivGuiWalking;

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
	}

	private int mAnimationRotateCount = 0;

	private void loadImages() {

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Log.d("size:", "w: " + width + " h: " + height + " density: " + density);

		Log.d(getString(NAME), "loadImages()");
		iv1 = (ImageView) view.findViewById(R.id.bg);
		iv2 = (ImageView) view.findViewById(R.id.mg);
		iv3 = (ImageView) view.findViewById(R.id.fg);

		ivGuiWalking = (ImageView) view.findViewById(R.id.ivWalk);
		ivMoving = new ImageView(getActivity());

		Bitmap bg = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.bg_something_moving, 540 * density, 280 * density);
		Bitmap mg = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.midground, 540 * density, 280 * density);
		Bitmap fg = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.foreground, 540 * density, 280 * density);
		Bitmap bmVibrate = Utils.decodeSampledBitmapFromResource(
				getResources(), R.drawable.grass, 180 * density, 120 * density);

		iv1.setImageBitmap(bg);
		iv2.setImageBitmap(mg);
		iv3.setImageBitmap(fg);

		int mGrassWidth = 200;
		int mGrassHeight = 160;

		if (height >= 800) {
			mGrassWidth = 260;
			mGrassHeight = 240;
		}

		// grass vibrating
		RelativeLayout.LayoutParams paramsLayout = new RelativeLayout.LayoutParams(
				mGrassWidth * density, mGrassHeight * density);
		paramsLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		paramsLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		ivMoving.setImageBitmap(bmVibrate);
 
		((RelativeLayout) view.getRootView())
				.addView(ivMoving, 4, paramsLayout);

		// Build moving animation
		final Animation animationRotate = new RotateAnimation(-1, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				1.0f);
		animationRotate.setRepeatCount(5);
		animationRotate.setDuration(100);

		ivMoving.setAnimation(animationRotate);
		animationRotate.start();

		// Set animation
		ivGuiWalking.setBackgroundResource(R.anim.gui_walk);

		// get animation to variable
		backGroundWalkingGuiAnim = (AnimationDrawable) ivGuiWalking
				.getBackground();
		// Build moving animation
		Animation animationGui = new TranslateAnimation(0 - 100 * density,
				((getResources().getDisplayMetrics().widthPixels)) * density,
				-8 * density, -8 * density);
		animationGui.setDuration(10000);

		// SEt moving animation
		ivGuiWalking.setAnimation(animationGui);
		// Start walking animation
		backGroundWalkingGuiAnim.start();
		// Start moving animation
		ivGuiWalking.getAnimation().start();

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
					}
				});

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

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pagSomethingMoving), bg));
	}

	ImageButton button = null;

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

		//Sound to be played
		BookActivity.playMusic(R.raw.wind);
		
		// BookActivity.playMusic(R.raw.robot_in_front);
		final ImageButton buttonNext = (ImageButton) view
				.findViewById(R.id.goToNextPage);

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagFindFriend fb = new PagFindFriend();

				onChoice.onChoiceMade(fb, getString(PagFindFriend.NAME),
						getResources().getResourceName(PagFindFriend.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagVillageAfterEnigmaFrg fb = new PagVillageAfterEnigmaFrg();

				onChoice.onChoiceMade(
						fb,
						getString(PagVillageAfterEnigmaFrg.NAME),
						getResources().getResourceName(
								PagVillageAfterEnigmaFrg.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		return view;
	}

	@Override
	public void onDetach() {
		Log.d(getString(NAME), " onDetach()");
		super.onDetach();

		if (backGroundWalkingGuiAnim != null) {
			backGroundWalkingGuiAnim.stop();
			ivGuiWalking.setVisibility(View.GONE);
			ivGuiWalking.setBackgroundResource(0);
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
