package com.dipeca.bookactivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagBossCapture extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.terribleSob;
	public static int icon = R.drawable.final_boss_icon;

	private TextView tv1 = null;

	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;
	private ImageView ivBossFlying;
	private ImageView ivAmulet;
	private int density;

	private Bitmap bmFlare;
	private Animation bossTranslation;
	private Bitmap bg;
	private Bitmap mg;
	private Bitmap fg;

	private Bitmap bottle;

	private AnimationSet bossAnimationSet;
	private ImageButton buttonNext;
	private ImageButton buttonPrev;

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
		tv1.setText(R.string.pagBossSobCapture);
	}

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

		ivBossFlying = new ImageView(getActivity());

		bg = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.bg_capture_boss, 540 * density, 280 * density);

		bmFlare = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.flare, 260 * density, 260 * density);

		iv1.setImageBitmap(bg);
		Log.d("tag:dalvikvm- " + getString(NAME), "bg_capture_boss");

		mg = onChoice.decodeSampledBitmapFromResourceFG(getResources(),
				R.drawable.mg_capture_boss, 540 * density, 280 * density);
		iv2.setImageBitmap(mg);
		Log.d("tag:dalvikvm- " + getString(NAME), "mg_capture_boss");
		fg = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.fg_capture_boss, 400 * density, 200 * density);
		iv3.setImageBitmap(fg);

		ivBossFlying.setBackgroundResource(R.anim.boss_flying_anim);

		Log.d("tag:dalvikvm- " + getString(NAME), "boss_flying_anim");

		RelativeLayout.LayoutParams paramsLayoutBossFlying = new RelativeLayout.LayoutParams(
				260 * density, 340 * density);
		paramsLayoutBossFlying.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsLayoutBossFlying.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);

		((RelativeLayout) view.getRootView()).addView(ivBossFlying, 3,
				paramsLayoutBossFlying);

		bossTranslation = new TranslateAnimation(-180 * density, 800 * density,
				360 * density, -720 * density);
		bossTranslation.setRepeatCount(20);
		bossTranslation.setRepeatMode(Animation.REVERSE);
		bossTranslation.setDuration(8000);

		Animation bossInclination = new RotateAnimation(12f, 25f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 1f);
		bossInclination.setRepeatCount(40);
		bossInclination.setRepeatMode(Animation.REVERSE);
		bossInclination.setDuration(8000);

		bossAnimationSet = new AnimationSet(true);
		bossAnimationSet.addAnimation(bossTranslation);
		bossAnimationSet.addAnimation(bossInclination);

		((AnimationDrawable) ivBossFlying.getBackground()).start();
		ivBossFlying.setAnimation(bossAnimationSet);
		ivBossFlying.getAnimation().start();

		bottle = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.frasco, 98 * density, 98 * density);

		ivAmulet = new ImageView(this.getActivity());
		ivAmulet.setImageBitmap(bottle);
		((RelativeLayout) view.getRootView()).addView(ivAmulet, 128 * density,
				128 * density);

		ivAmulet.setOnTouchListener(new MyTouchListener());

		ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(ivAmulet,
				"translationY", 280 * density);
		objectAnimatorY.setDuration(2000);
		objectAnimatorY.start();

		ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(ivAmulet,
				"translationX", 80 * density);
		objectAnimatorX.setDuration(2000);
		objectAnimatorX.start();

		ivBossFlying.setOnDragListener(new MyDragListener());

	}

	private DragShadowBuilder shadowBuilder;

	private final class MyTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {

			Log.d("boss fly touch", "boss flying");

			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN
					&& view == ivAmulet) {
				ClipData data = ClipData.newPlainText("", "");
				shadowBuilder = new View.DragShadowBuilder(view);
				view.startDrag(data, shadowBuilder, view, 0);
				view.setVisibility(View.INVISIBLE);
				return true;
			} else {
				return false;
			}
		}
	}

	private boolean isOnBoss = false;

	class MyDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();

			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				Log.d("onDrag", "ACTION_DRAG_STARTED");
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				// Set blue shadow
				isOnBoss = true;
				//ivBossFlyingImageView.setVisibility(View.VISIBLE);
				Log.d("onDrag", "ACTION_DRAG_ENTERED");
				break;
			case DragEvent.ACTION_DRAG_LOCATION:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				// remove shadow
				isOnBoss = false;
				Log.d("onDrag", "ACTION_DRAG_EXITED");
				break;
			case DragEvent.ACTION_DROP:
				// remove shadow
				if (isOnBoss) {
					buttonNext.setVisibility(View.VISIBLE);
					
					bossAnimationSet.cancel();
					Log.d("onDrag", "ACTION_DROP");

					//plays the explosion sound
					BookActivity.stopMusic();
					BookActivity.playMusicOneAfterAnother(R.raw.boss_captured, R.raw.finale_music);
					
					// Stop boss flying
					Animation anim = ivBossFlying.getAnimation();
					if(anim != null){
						anim.cancel();
					}

					// Change image of boss
					ivBossFlying.setImageBitmap(bmFlare);
					((AnimationDrawable) ivBossFlying.getBackground()).stop();
					ivBossFlying.setBackgroundResource(0);

					// Set scale animation to light disappearance
					Animation scaleAnimationBoss = new ScaleAnimation(1, 0, 1,
							0, ScaleAnimation.RELATIVE_TO_SELF, .5f,
							ScaleAnimation.RELATIVE_TO_SELF, .5f);
					scaleAnimationBoss.setDuration(5000);
					ivBossFlying.setAnimation(scaleAnimationBoss);
					scaleAnimationBoss.start();
					scaleAnimationBoss
							.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation arg0) {
								}

								@Override
								public void onAnimationRepeat(Animation arg0) {
								}

								@Override
								public void onAnimationEnd(Animation arg0) {
									ivBossFlying.setVisibility(View.GONE);
									((ViewGroup) ivBossFlying.getParent())
											.removeView(ivBossFlying);
								}
							});

					// Vibration of pillars
					final Animation vibrateFg = new TranslateAnimation(-1, 1,
							0, 0);
					vibrateFg.setRepeatMode(Animation.REVERSE);
					vibrateFg.setRepeatCount(200);
					vibrateFg.setDuration(25);
					iv3.setAnimation(vibrateFg);
					vibrateFg.start();

					// set background as black
					bg = Utils.changeBitmapContrastBrightness(bg, 1, -255);
					iv1.setImageBitmap(bg);
					iv2.setImageBitmap(bg);

					scaleAnimationBoss
							.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation arg0) {
								}

								@Override
								public void onAnimationRepeat(Animation arg0) {
								}

								@Override
								public void onAnimationEnd(Animation arg0) {
									// Change ivAmulet image to boss trapped
									ivAmulet.setImageBitmap(null);
									ivAmulet.setBackgroundResource(R.anim.bottle_boss_anim);
									ivAmulet.getLayoutParams().height = 320 * density;
									ivAmulet.getLayoutParams().width = 256 * density;
									((AnimationDrawable) ivAmulet
											.getBackground()).start();
									// Change shadow image of dragging image
									ClipData data = ClipData.newPlainText("",
											"");
									shadowBuilder = new View.DragShadowBuilder(
											ivAmulet);
									ivAmulet.startDrag(data, shadowBuilder,
											view, 0);

									// stop vibration
									vibrateFg.cancel();
									
									tv1.setText(R.string.boss_captured);
									
									iv3.setImageBitmap(bg);
								}
							});
				}
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				Log.d("onDrag", event.getAction() + " ACTION_DRAG_ENDED");
			default:
				Log.d("onDrag", event.getAction() + "");
				view.post(new Runnable() {
					public void run() {
						// remove shadow
						ivAmulet.setVisibility(View.VISIBLE);
					}
				});

				break;
			}
			return true;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);

		//plays the sound
		BookActivity.playMusic(R.raw.midnight_walk);
		
		view = inflater.inflate(R.layout.pag_3_images, container, false);

		buttonNext = (ImageButton) view.findViewById(R.id.goToNextPage);

		buttonNext.setVisibility(View.GONE);
		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagFinaleVillage fb = new PagFinaleVillage();

				onChoice.onChoiceMade(fb, getString(PagFinaleVillage.NAME),
						getResources().getResourceName(PagFinaleVillage.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagBoss fb = new PagBoss();

				onChoice.onChoiceMade(fb, getString(PagBoss.NAME),
						getResources().getResourceName(PagBoss.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		// loadImages
		loadImages();
		loadText();

		// Set image and text to share intent
		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pagBossSobCapture), bg));

		return view;
	}

	@Override
	public void onDetach() {
		Log.d(getString(NAME), " onDetach()");
		super.onDetach();

	}

	@Override
	public String getPrevPage() {
		return PagBoss.class.getName();
	}

	@Override
	public String getNextPage() {
		return PagFindFriend.class.getName();
	}

}
