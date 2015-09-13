package com.dipeca.bookactivity;

import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.ObjectItem;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagDungeonPostChallenge extends Fragment implements
		OnTouchListener, IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.pagAfterEnigmaDungeon;
	public static int icon = R.drawable.icon_page_move_rocks_after;

	private TextView tv1 = null;

	private ImageView iv1;
	private int density;

	private Bitmap bitmap1;
	private Bitmap bitmap2;
	private Bitmap bg;

	private ImageView ivClickable;
	private ImageView ivCat;
	ObjectItem oi = null;
	private boolean isSpellPapelAknowledged = false;

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
		tv1.setText(R.string.pagAfterEnigmaDungeon);
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
		ivClickable = (ImageView) view.findViewById(R.id.mg);

		bg = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.page_move_rocks_after, 600 * density, 300 * density);

		Bitmap bmCat = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.gata_costas, 480 * density, 380 * density);

		iv1.setImageBitmap(bg);
		ivClickable.setImageBitmap(Utils.decodeSampledBitmapFromResource(
				getResources(), R.drawable.page_move_rocks_after_cli,
				50 * density, 25 * density));
		ivClickable.setVisibility(View.INVISIBLE);

		// Set animation
		ivCat = (ImageView) view.findViewById(R.id.ivWalk);
		ivCat.setImageBitmap(bmCat);

		ivCat.getLayoutParams().width = 480 * density;
		ivCat.getLayoutParams().height = 380 * density;

		// Build moving animation
		Animation animationBreathe = new ScaleAnimation(1f, 1.005f, 1f, 1.005f,
				.5f, .5f);
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

		oi = new ObjectItem(null, getString(R.string.amulet),
				ObjectItem.TYPE_SPELL, null);
		isSpellPapelAknowledged = onChoice.isInObjects(oi);

		// BookActivity.playMusic(R.raw.robot_in_front);
		buttonNext = (ImageButton) view.findViewById(R.id.goToNextPage);

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (isSpellPapelAknowledged) {

					if (onChoice.isInJourney(getString(PagChest.NAME))) {
						PagRobot fb = new PagRobot();

						onChoice.onChoiceMade(fb, getString(PagRobot.NAME),
								getResources().getResourceName(PagRobot.icon));
						onChoice.onChoiceMadeCommit(getString(NAME), false);
					} else {
						PagPathChoiceFrg fb = new PagPathChoiceFrg();

						onChoice.onChoiceMade(
								fb,
								getString(PagPathChoiceFrg.NAME),
								getResources().getResourceName(
										PagPathChoiceFrg.icon));
						onChoice.onChoiceMadeCommit(getString(NAME), false);
					}
				} else {
					Toast toast = Toast.makeText(getActivity(),
							getString(R.string.theAmuletWarning),
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER_HORIZONTAL
							| Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
				}
			}
		});

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagDungeonPrevChallenge fb = new PagDungeonPrevChallenge();

				onChoice.onChoiceMade(fb,
						getString(PagDungeonPrevChallenge.NAME), getResources()
								.getResourceName(PagDungeonPrevChallenge.icon));
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

		view.setOnTouchListener(this);

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
		return PagDungeonPrevChallenge.class.getName();
	}

	@Override
	public String getNextPage() {
		return PagRobot.class.getName();
	}

	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		long start = new Date().getTime();
		final int action = ev.getAction();
		// (1)
		final int evX = (int) ev.getX();
		final int evY = (int) ev.getY();

		switch (action) {

		case MotionEvent.ACTION_UP:

			// int touchColor = Utils.getHotspotColor(R.id.pag1ImageViewAmuleto,
			// evX, evY, ivClickable);
			int touchColor = Utils.getHotspotColor(ev, ivClickable);

			int tolerance = 25;
			if (Utils.closeMatch(Color.WHITE, touchColor, tolerance)) {

				if (!isSpellPapelAknowledged) {
					// Found object
					//BookActivity.playMusicOnce(R.raw.picked_item);
					isSpellPapelAknowledged = true;
					// Persist object
					oi = new ObjectItem();
					oi.setObjectImageType(ObjectItem.TYPE_SPELL);
					oi.setTitle(getString(R.string.spell));

					onChoice.objectFoundPersist(oi);

				}
				onChoice.addViewToScreen(Utils.decodeSampledBitmapFromResource(
						getResources(), R.drawable.spell, 128 * density,
						128 * density), this);

			}
			break;
		}

		return true;
	}

}
