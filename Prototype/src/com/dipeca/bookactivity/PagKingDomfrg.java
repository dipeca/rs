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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagKingDomfrg extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.semgiKingDom;
	public static int icon = R.drawable.kingdom_icon;
	private TextView tv1 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;

	private Bitmap bitmap1;

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

	private ImageView image1;
	private ImageView image2;

	private void loadImages() {
		Log.d("KingDom ", "loadImages()");
		image1 = (ImageView) view.findViewById(R.id.pag1ImageView);
		image2 = (ImageView) view.findViewById(R.id.pag1ImageViewBirds);

		int density = (int) Math.ceil(getResources().getDisplayMetrics().density);
		bitmap1 = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.kingdom, 400 * density, 200 * density);
		image1.setImageBitmap(bitmap1);

		image2.setImageResource(R.anim.crow);

		Animation animation = new TranslateAnimation(1000 * density, -400
				* density, density * 200, density * 64);
		animation.setDuration(6000);
		animation.setRepeatCount(Animation.INFINITE);

		Animation scaleAnimation = new ScaleAnimation(0.3f, 0.0f, 0.3f, 0.0f,
				0.5f, 0.5f);
		scaleAnimation.setDuration(6000);
		// scaleAnimation.setStartOffset(1000);
		animation.setRepeatCount(Animation.INFINITE);

		Animation alphaAnim = new AlphaAnimation(1f, 0f);
		alphaAnim.setDuration(200);
		alphaAnim.setStartOffset(5000);
		animation.setRepeatCount(10);

		AnimationSet set = new AnimationSet(false);
		set.addAnimation(scaleAnimation);
		set.addAnimation(animation);
		set.addAnimation(alphaAnim);

		set.setInterpolator(new BounceInterpolator());

		image2.setAnimation(set);

		image2.getAnimation().setAnimationListener(
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
						animation.setRepeatCount(0);
						image2.setAnimation(null);
						image2.setImageResource(0);
					}
				});
 
		image2.getAnimation().start();
		AnimationDrawable backGroundChangeAnimJake = (AnimationDrawable) image2
				.getDrawable();
		backGroundChangeAnimJake.start();
	}

	ImageButton button = null;
	ImageButton buttonPrev = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		BookActivity.playMusic(R.raw.wind);

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_one_image, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time kingdom", "onCreateView after inflate time ="
				+ totalTime);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pag2_1);

		tv3 = (TextView) view.findViewById(R.id.textPag1_2);
		tv3.setVisibility(View.GONE);

		// loadImages
		loadImages();

		tv1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				float density = (float) getResources().getDisplayMetrics().density;
				int height1 = 0;
				int width3 = 0;
				int multiplier = 7;
				if (isTextHide) {
					height1 = tv1.getHeight() * multiplier
							- ((int) Math.ceil(8 * density));
					width3 = tv3.getWidth() * multiplier
							- ((int) Math.ceil(8 * density));
					isTextHide = false;
				} else {
					height1 = tv1.getHeight() / multiplier
							+ ((int) Math.ceil(8 * density));
					width3 = tv3.getWidth() / multiplier
							+ ((int) Math.ceil(8 * density));
					isTextHide = true;
				}

				RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
						tv1.getWidth(), height1);
				tv1.setLayoutParams(params1);

				ViewGroup.LayoutParams params3 = tv3.getLayoutParams();
				params3.width = width3;
				params3.height = tv3.getHeight();
				tv3.setLayoutParams(params3);
			}
		});

		button = (ImageButton) view.findViewById(R.id.goToNextPage);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagVillageFrg fb = new PagVillageFrg();

				onChoice.onChoiceMade(fb, PagVillageFrg.NAME,
						PagVillageFrg.icon);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagFindPortalFrg fb = new PagFindPortalFrg();

				onChoice.onChoiceMade(fb, PagFindPortalFrg.NAME,
						PagFindPortalFrg.icon);
				onChoice.onChoiceMadeCommit(NAME, false);
			}
		});

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pag2_1), bitmap1));

		return view;
	}

	@Override
	public void onDetach() {
		Log.d("Kingdom ", "Kingdom  onDetach()");
		super.onDetach();
//
//		if (bitmap1 != null) {
//			bitmap1.recycle();
//			bitmap1 = null;
//		}
	}

	AnimationDrawable backGroundChangeAnim;

	@Override
	public String getPrevPage() {
		return PagFindPortalFrg.class.getName();
	}

	@Override
	public String getNextPage() {
		// TODO Auto-generated method stub
		return null;
	}

}
