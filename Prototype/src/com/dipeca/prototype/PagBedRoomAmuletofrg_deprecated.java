package com.dipeca.prototype;

import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PagBedRoomAmuletofrg_deprecated extends Fragment implements
		OnTouchListener {
	View view = null;
	private IMainActivity onChoice;
	public static String NAME = "Amulet offer";
	private static String icon = "R.drawable.quarto_olhar_talisma_icon";
	private TextView tv1 = null;
	private DialogBox tv2 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;
	ImageView amuleto = null;
	private static Bitmap bitmap1;
	private static Bitmap bitmap2;
	private static Bitmap bitmap3;
	ObjectItem oi = null;

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
	private ImageView ivTalisman;

	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv2 = (DialogBox) view.findViewById(R.id.dialog);
		tv3 = (TextView) view.findViewById(R.id.textPag1_2);

		tv3.setVisibility(View.GONE);
		tv1.setText(R.string.theAmulet);
		tv2.setTextDialog(getString(R.string.useTheAmulet));
	}

	private void loadImages() {
		Log.d("BedRoom Amulet ", "loadImages()");

		int density = (int) getResources().getDisplayMetrics().density;

		iv1 = (ImageView) view.findViewById(R.id.pag1ImageView);
		iv2 = (ImageView) view.findViewById(R.id.pag1ImageViewAmuleto);
		ivTalisman = (ImageView) view.findViewById(R.id.pag1Amuleto);

		density = (int) getResources().getDisplayMetrics().density;

		bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.quarto_olhar_talisma, 600, 500);
		iv1.setImageBitmap(bitmap1);

		bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.quarto_olhar_talisma, 40 * density, 20 * density);

		iv2.setImageBitmap(bitmap2);

		bitmap3 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.quarto_olhar_talisma, 126 * density, 126 * density);

		ivTalisman.setImageBitmap(bitmap3);
	}

	private Handler mHandler = new Handler();

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long startTime = System.currentTimeMillis();

			int density = (int) getResources().getDisplayMetrics().density;
			if (BookActivity.bitmap2 == null) {
				BookActivity.bitmap2 = Utils.decodeSampledBitmapFromResource(
						getResources(), R.drawable.quarto_cli, 40 * density,
						20 * density);

				BookActivity.bitmapTalisma = Utils
						.decodeSampledBitmapFromResource(getResources(),
								R.drawable.talisma, 128 * density,
								128 * density);
			}

			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			Log.d("Total time", "mUpdateTimeTask time =" + totalTime);
			button.setVisibility(View.VISIBLE);
		}
	};

	ImageButton button = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater
				.inflate(R.layout.pag_one_image_dialog, container, false);

		oi = new ObjectItem(null, "Amulet", ObjectItem.TYPE_AMULET, null);
		isAmuletAcknoledged = onChoice.isInObjects(oi);

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time pag1", "onCreateView after inflate time ="
				+ totalTime);

		amuleto = (ImageView) view.findViewById(R.id.pag1Amuleto);
		amuleto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ScaleAnimation sa = new ScaleAnimation(amuleto
						.getLayoutParams().width,
						amuleto.getLayoutParams().width, 0, 0);

				amuleto.setAnimation(sa);
				amuleto.getAnimation().start();

				amuleto.setVisibility(View.INVISIBLE);

			}
		});

		// loadImages()
		loadImages();

		loadText();

		view.setOnTouchListener(this);

		tv1.setOnClickListener(new View.OnClickListener() {
			synchronized public void onClick(View v) {
				int height1 = 0;
				int height2 = 0;
				int width3 = 0;
				int multiplier = 7;
				int density = (int) getResources().getDisplayMetrics().density;

				if (isTextHide) {
					height1 = tv1.getHeight() * multiplier + (12 * density);
					height2 = tv2.getHeight() * multiplier + (12 * density);
					width3 = tv3.getWidth() * multiplier + (12 * density);
					isTextHide = false;
				} else {
					height1 = Math.max(tv1.getHeight() / multiplier,
							(12 * density));
					height2 = Math.max(tv2.getHeight() / multiplier,
							(12 * density));
					width3 = Math.max(tv3.getWidth() / multiplier,
							(12 * density));
					isTextHide = true;
				}

				ViewGroup.LayoutParams params1 = tv1.getLayoutParams();
				params1.width = tv1.getWidth();
				params1.height = height1;
				tv1.setLayoutParams(params1);
				tv1.setLayoutParams(params1);

				ViewGroup.LayoutParams params2 = tv2.getLayoutParams();
				params2.width = tv2.getWidth();
				params2.height = height2;
				tv2.setLayoutParams(params2);

				ViewGroup.LayoutParams params3 = tv3.getLayoutParams();
				params3.width = width3;
				params3.height = tv3.getHeight();
				tv3.setLayoutParams(params3);
			}
		});

		button = (ImageButton) view.findViewById(R.id.goToNextPage);
		button.setVisibility(View.INVISIBLE);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Class c = null;
				Fragment fb = null;

				try {
					c = Class.forName("com.dipeca.prototype.PagFindPortalFrg");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					fb = (Fragment) c.newInstance();
				} catch (java.lang.InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (isAmuletAcknoledged) {
					onChoice.onChoiceMade(fb, PagFindPortalFrg.NAME, icon);
					onChoice.onChoiceMadeCommit(NAME, true);
				} else {
					Toast.makeText(getActivity(), R.string.theAmuletWarning,
							Toast.LENGTH_LONG).show();
				}

			}
		});

		mHandler.postDelayed(mUpdateTimeTask, 1000);

		return view;
	}

	@Override
	public void onDetach() {
		Log.d("BedRoom amulet", "BedRoom amulet onDetach()");
		super.onDetach();

		bitmap1.recycle();
		bitmap1 = null;

		bitmap2.recycle();
		bitmap2 = null;

		bitmap3.recycle();
		bitmap3 = null;
	}

	private boolean isAmuletAcknoledged = false;

	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		long start = new Date().getTime();
		final int action = ev.getAction();
		// (1)
		final int evX = (int) ev.getX();
		final int evY = (int) ev.getY();

		switch (action) {

		case MotionEvent.ACTION_UP:

			int touchColor = Utils.getHotspotColor(R.id.pag1ImageViewAmuleto,
					evX, evY, iv2);

			int tolerance = 25;
			if (Utils.closeMatch(Color.RED, touchColor, tolerance)) {

				amuleto.setVisibility(View.VISIBLE);
				ScaleAnimation sa = new ScaleAnimation(0, 0,
						amuleto.getLayoutParams().width,
						amuleto.getLayoutParams().width);

				amuleto.setAnimation(sa);
				amuleto.getAnimation().start();

				amuleto.setVisibility(View.INVISIBLE);

				onChoice.objectFoundPersist(oi);

				isAmuletAcknoledged = true;

				long end = new Date().getTime();
				long timeTotal = end - start;
				System.out.println("Time = " + timeTotal);
			}
			break;
		}

		return true;
	}
}
