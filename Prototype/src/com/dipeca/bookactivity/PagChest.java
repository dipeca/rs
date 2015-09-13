package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.ObjectItem;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagChest extends Fragment implements OnTouchListener, IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.chest;
	public static int icon = R.drawable.cofre_icon;
	private TextView tv1 = null;
	private boolean isTextHide = false;

	private Bitmap bitmap1;
	private Bitmap bitmap2;

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

	private ImageView iv1;
	private ImageView iv2;

	private int density;

	private void loadImages() {
		Log.d(getString(NAME), "loadImages()");
		iv1 = (ImageView) view.findViewById(R.id.page3Image);
		iv2 = (ImageView) view.findViewById(R.id.page3ImageClick);

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);
		bitmap1 = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.cofre_fechado, 400 * density, 200 * density);
		iv1.setImageBitmap(bitmap1);
  

		bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.cofre_fechado_cli, 50, 25);
		iv2.setImageBitmap(bitmap2);
 
		//Check if we already have traveled to the lake
		ObjectItem oi = new ObjectItem();
		oi.setObjectImageType(ObjectItem.TYPE_BOTTLE);

		// Add buttonNext to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);
		
		
		//mist animation
		Bitmap bmMist = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.pantanomist, 280 * density, 180 * density);
		RelativeLayout.LayoutParams paramsLayoutMist = new RelativeLayout.LayoutParams(
				2000 * density, 1000 * density);
		paramsLayoutMist.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		final ImageView ivMist = new ImageView(getActivity());
		ivMist.setImageBitmap(bmMist);

		ivMist.setScaleType(ScaleType.FIT_XY);
		((RelativeLayout) view.getRootView()).addView(ivMist,
				paramsLayoutMist);
		Animation translateAnimation = new TranslateAnimation(0 * density,
				20000 * density, 98 * density, 98 * density);
 
		Animation scaleA = new ScaleAnimation(3.0F, 6.0F, 1.5F,1.5F,
				ScaleAnimation.RELATIVE_TO_PARENT, 1f,
				ScaleAnimation.RELATIVE_TO_SELF, 1f);
		
		Animation alphaAnim = new AlphaAnimation(0.5f, 0.2f);
		alphaAnim.setDuration(5000);
		
		scaleA.setDuration(5000);
 
		Animation alphaAnimation = new AlphaAnimation(0.4F, 1F);
		alphaAnimation.setDuration(50000);

		AnimationSet animationSet = new AnimationSet(true);

		animationSet.setDuration(100000);
		animationSet.addAnimation(alphaAnim);
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
	}

	ImageButton button = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_one_image_clickable, container,
				false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d(getString(NAME), "onCreateView after inflate time =" + totalTime);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.chestClosed);

		// loadImages()
		loadImages();

		view.setOnTouchListener(this);

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.chest), bitmap1));
		
		return view;
	}

	@Override
	public void onDetach() {
		Log.d(getString(NAME), " onDetach()");
		super.onDetach();
		iv1.setImageBitmap(null);
		iv2.setImageBitmap(null);
		
		if (bitmap2 != null) {
			bitmap2.recycle();
			bitmap2 = null;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		final int action = ev.getAction();

		switch (action) {

		case MotionEvent.ACTION_UP:

			int touchColor = Utils.getHotspotColor(ev, iv2);

			int tolerance = 25;
			if (Utils.closeMatch(Color.WHITE, touchColor, tolerance)) {
				// Do the action associated with the White region
				PagChestOpen frg = new PagChestOpen();

				onChoice.onChoiceMade(frg, PagChestOpen.NAME, PagChestOpen.icon);

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setTransition(R.animator.right_to_left);

				PagLockMathsVault fb = new PagLockMathsVault();
				fb.isVaultPage(true);
				
				ft.replace(R.id.detailFragment, fb);
				//ft.addToBackStack(NAME);
				ft.commit();
			} else {
				Log.d("PagFindFriend", "Resto da imagem clicada");
			}
			break;
		}

		return true;
	}

	@Override
	public String getPrevPage() {
		return PagLakeToCross.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}
}
