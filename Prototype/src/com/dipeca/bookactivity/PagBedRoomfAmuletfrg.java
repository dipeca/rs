package com.dipeca.bookactivity;

import java.lang.ref.WeakReference;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.dipeca.item.DialogBox;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.ObjectItem;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagBedRoomfAmuletfrg extends Fragment implements OnTouchListener,
		IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.TheAmulet;
	public static int icon = R.drawable.quarto_olhar_talisma_icon;
	private TextView tv1 = null;
	private DialogBox tv2 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;
	private Bitmap bitmap1;
	private Bitmap bitmap2;
	private Bitmap bitmap3;
	private boolean isAmuletAcknoledged = false;
	ImageButton buttonNext = null;
	ImageButton buttonPrev = null;
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
	private ImageView ivClickable;
	private ImageView ivTalisman;
	private int density = 1;

	private void loadImages() {
		Log.d("Frg Bed Room amulet", "loadImages()");
		iv1 = (ImageView) view.findViewById(R.id.pag1ImageView);
		ivClickable = (ImageView) view.findViewById(R.id.pag1ImageViewAmuleto);

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);
		bitmap1 = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.quarto_olhar_talisma, 400 * density, 200 * density);
		iv1.setImageBitmap(bitmap1);
 
		bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.amuleto_cli, (int) Math.ceil(50 * density),
				(int) Math.ceil(25 * density));

		ivClickable.setImageBitmap(bitmap2);

		bitmap3 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.talisma, (int) Math.ceil(198* density),
				(int) Math.ceil(198 * density));

		ivTalisman.setImageBitmap(bitmap3);

		Log.d("Frg Bed Room amulet", "after loadImages()");
	}

	private Handler mHandler = new Handler();

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long startTime = System.currentTimeMillis();

			if (isAdded()) {
				float density = (float) getResources().getDisplayMetrics().density;

				// if we the bitmap was not loaded; If we came from the back
				// buttonNext
				// we do not load again
				if (BookActivity.bitmap2 == null) {
					BookActivity.bitmap2 = Utils
							.decodeSampledBitmapFromResource(getResources(),
									R.drawable.quarto_cli,
									(int) Math.ceil(50 * density),
									(int) Math.ceil(25 * density));

				}
				if (BookActivity.bitmapTalisma == null) {
					BookActivity.bitmapTalisma = Utils
							.decodeSampledBitmapFromResource(getResources(),
									R.drawable.talisma,
									128 * (int) Math.ceil(density),
									128 * (int) Math.ceil(density));
				}
				// amuleto.setImageBitmap(BookActivity.bitmapTalisma);
			}
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			Log.d("Total time", "mUpdateTimeTask time =" + totalTime);
			// buttonNext.setVisibility(View.VISIBLE);
		}

	};

	private int tv1OriginalSize;
	private int tv2OriginalSize;
	private int tv3OriginalSize;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater
				.inflate(R.layout.pag_one_image_dialog, container, false);

		oi = new ObjectItem(null, getString(R.string.amulet),
				ObjectItem.TYPE_AMULET, null);
		isAmuletAcknoledged = onChoice.isInObjects(oi);

		ivTalisman = (ImageView) view.findViewById(R.id.pag1Amuleto);

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d(NAME + " Total time", "onCreateView after inflate time ="
				+ totalTime);

		ivTalisman.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ivTalisman.setVisibility(View.INVISIBLE);

			}
		});

		// loadImages()
		loadImages();

		loadText();

		view.setOnTouchListener(this);

		buttonNext = (ImageButton) view.findViewById(R.id.goToNextPage);
		// buttonNext.setVisibility(View.INVISIBLE);
		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				PagFindPortalFrg fb = new PagFindPortalFrg();
				WeakReference<Fragment> weakFgr = new WeakReference<Fragment>(fb);
				
				if (isAmuletAcknoledged) {
					onChoice.onChoiceMade(weakFgr, PagFindPortalFrg.NAME,
							PagFindPortalFrg.icon);
					onChoice.onChoiceMadeCommit(NAME, true);
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

				PagBedRoomfrg fb = new PagBedRoomfrg();

				WeakReference<Fragment> weakFgr = new WeakReference<Fragment>(fb);
				
				onChoice.onChoiceMade(weakFgr, PagBedRoomfrg.NAME,
						PagBedRoomfrg.icon);
				onChoice.onChoiceMadeCommit(NAME, false);
			}
		});

		mHandler.postDelayed(mUpdateTimeTask, 1000);

		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
		Log.d(NAME + " Total time", "onCreateView before return =" + totalTime);

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.theAmulet), bitmap1));

		return view;
	}

	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv2 = (DialogBox) view.findViewById(R.id.dialog);
		tv3 = (TextView) view.findViewById(R.id.textPag1_2);
		tv3.setVisibility(View.GONE);

		RelativeLayout.LayoutParams rl = (LayoutParams) tv1.getLayoutParams();
		rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		rl.setMargins((int) Math.ceil(16 * density),
				(int) Math.ceil(16 * density), (int) Math.ceil(16 * density),
				(int) Math.ceil(16 * density));
		// rl.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
		Utils.removeRule(rl, RelativeLayout.ALIGN_PARENT_LEFT);

		RelativeLayout.LayoutParams rl2 = (LayoutParams) tv2.getLayoutParams();
		rl2.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		rl2.setMargins((int) Math.ceil(60 * density),
				(int) Math.ceil(16 * density), (int) Math.ceil(16),
				(int) Math.ceil(16 * density));
		// rl2.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		Utils.removeRule(rl2, RelativeLayout.ALIGN_PARENT_RIGHT);

		tv1.setText(R.string.theAmulet);
		tv2.setTextDialog(getString(R.string.useTheAmulet));

		tv2.setImg1Id(getResources().getDrawable(R.anim.rocket));
		tv2.setImg2Id(getResources().getDrawable(R.anim.gui_quarto_anim));

		tv1.setOnClickListener(new View.OnClickListener() {
			synchronized public void onClick(View v) {
				int height1 = 0;
				int height2 = 0;
				int width3 = 0;
				int multiplier = 6;
				int density = (int) getResources().getDisplayMetrics().density;

				if (isTextHide) {
					height1 = tv1OriginalSize;// tv1.getHeight() * multiplier +
												// (4 * density);
					height2 = tv2OriginalSize;// tv2.getHeight() * multiplier +
												// (4 * density);
					width3 = tv3OriginalSize;// tv3.getWidth() * multiplier
					isTextHide = false;
				} else {

					tv1OriginalSize = tv1.getHeight();
					tv2OriginalSize = tv2.getHeight();
					tv3OriginalSize = tv3.getWidth();

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
	}

	@Override
	public void onDetach() {
		Log.d("BedRoom ", "BedRoom onDetach()");
		super.onDetach();

		// if (bitmap1 != null) {
		// bitmap1.recycle();
		// bitmap1 = null;
		// }
		//
		// if (bitmap2 != null) {
		// bitmap2.recycle();
		// bitmap2 = null;
		// }
		//
		// if (bitmap3 != null) {
		// bitmap3.recycle();
		// bitmap3 = null;
		// }
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
			if (Utils.closeMatch(Color.RED, touchColor, tolerance)) {

				if(!isAmuletAcknoledged){
					//Found object
					//BookActivity.playMusicOnce(R.raw.picked_item);
				}
				
				ivTalisman.setVisibility(View.VISIBLE);

				onChoice.objectFoundPersist(oi);

				isAmuletAcknoledged = true;

				long end = new Date().getTime();
				long timeTotal = end - start;
				Log.d("PagBedRoomAm", "Time = " + timeTotal);
			}
			break;
		}

		return true;
	}

	@Override
	public String getPrevPage() {
		// TODO Auto-generated method stub
		return PagBedRoomfrg.class.getName();
	}

	@Override
	public String getNextPage() {
		return PagFindPortalFrg.class.getName();
	}

}
