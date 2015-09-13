package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagDungeonPrevChallenge extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.pagPrevEnigmaDungeonTitle;
	public static int icon = R.drawable.icon_inside_dungeon;

	private TextView tv1 = null;

	private ImageView iv1;
	private int density;

	private Bitmap bitmap1;
	private Bitmap bitmap2;
	private Bitmap bg;
	private Bitmap bmCat;
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
		tv1.setText(R.string.pagPrevEnigmaDungeon);
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

		bg = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.page_move_rocks_prev, 600 * density, 300 * density);

		bmCat = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.gata_costas, 480 * density, 380 * density);
		
		iv1.setImageBitmap(bg);
		ivCat = (ImageView) view.findViewById(R.id.ivWalk);
		
		ivCat.getLayoutParams().width = 480 * density;
		ivCat.getLayoutParams().height = 380 * density;
		
		// Set animation
		ivCat.setImageBitmap(bmCat);
 
		// Build moving animation
		Animation animationBreathe = new ScaleAnimation(1f, 1.005f, 1f, 1.005f, .5f,
				.5f);
		animationBreathe.setDuration(800); 
		animationBreathe.setRepeatCount(Animation.INFINITE);
		animationBreathe.setRepeatMode(Animation.REVERSE);
		// SEt moving animation
		ivCat.setAnimation(animationBreathe);
		// Start moving animation
		ivCat.getAnimation().start();

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

		// BookActivity.playMusic(R.raw.robot_in_front);
		buttonNext = (ImageButton) view.findViewById(R.id.goToNextPage);

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagEnigmaMoveRocksFrg fb = new PagEnigmaMoveRocksFrg();

				onChoice.onChoiceMade(fb, getString(PagEnigmaMoveRocksFrg.NAME),
						getResources().getResourceName(PagEnigmaMoveRocksFrg.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagDungeonInside fb = new PagDungeonInside();

				onChoice.onChoiceMade(
						fb,
						getString(PagDungeonInside.NAME),
						getResources().getResourceName(
								PagDungeonInside.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		// loadImages
		loadImages();
		loadText();

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
