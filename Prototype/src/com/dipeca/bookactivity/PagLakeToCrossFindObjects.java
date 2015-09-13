package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
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
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dipeca.item.DialogBox;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.ObjectItem;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagLakeToCrossFindObjects extends Fragment implements
		OnTouchListener, IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.theLakeToCross;
	public static int icon = R.drawable.lago_icon;
	private TextView tv1 = null;

	private Bitmap bitmap1;
	private Bitmap bitmap2;
	private Bitmap bitmap3;

	private DialogBox dialog = null;
	ImageButton buttonNext = null;
	ImageButton buttonPrev = null;

	private ImageView ivMoving;
	private ImageView iv1;
	private ImageView iv3;

	private ObjectItem oi = null;
	private static boolean isObjectsFound = false;

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

	private ImageView ivObjectFound;
	private ImageView ivClickable;
	private ImageView icon1;
	private ImageView icon2;
	private int density = 1;

	private void loadText() {
		dialog = new DialogBox(getActivity());

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagLake);

		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				280 * density, RelativeLayout.LayoutParams.WRAP_CONTENT);
		// rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		rl.addRule(RelativeLayout.ALIGN_RIGHT, buttonNext.getId());
		rl.addRule(RelativeLayout.ABOVE, buttonNext.getId());

		dialog.setLayoutParams(rl);
		dialog.setBottom(48 * density);

		((RelativeLayout) view.getRootView()).addView(dialog, rl);
		dialog.setTextDialog(getString(R.string.pagLakeGuiDialog));
		dialog.setImg2Id(getResources().getDrawable(R.anim.gui_anim));
		dialog.setImg1Id(getResources().getDrawable(R.anim.friend_dialog));
	}

	private void loadImages() {

		icon1 = (ImageView) view.findViewById(R.id.iconToShow);
		icon2 = (ImageView) view.findViewById(R.id.icon2);

		icon1.setVisibility(View.VISIBLE);
		icon2.setVisibility(View.VISIBLE);

		ivClickable = (ImageView) view.findViewById(R.id.ivClickable);
		ivObjectFound = (ImageView) view.findViewById(R.id.pag1Object);

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Log.d("size:", "w: " + width + " h: " + height + " density: " + density);

		Log.d(getString(NAME), "loadImages()");
		iv1 = (ImageView) view.findViewById(R.id.bg);
		iv3 = (ImageView) view.findViewById(R.id.fg);

		final ImageView ivGuiWalking = (ImageView) view
				.findViewById(R.id.ivWalk);
		ivGuiWalking.setVisibility(View.GONE);

		ivMoving = new ImageView(getActivity());

		Bitmap bg = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.pantano, 600 * density, 300 * density);
		// Bitmap mg = Utils.decodeSampledBitmapFromResource(getResources(),
		// R.drawable.p, 600 * density, 300 * density);
		Bitmap fg = onChoice.decodeSampledBitmapFromResourceFG(getResources(),
				R.drawable.pant_foreground2, 600 * density, 300 * density);
		Bitmap bmVibrate = Utils.decodeSampledBitmapFromResource(
				getResources(), R.drawable.pant_objecto, 180 * density,
				120 * density);
		Bitmap bmClickable = Utils.decodeSampledBitmapFromResource(
				getResources(), R.drawable.lagoteste2todrop,
				(int) Math.ceil(50 * density), (int) Math.ceil(25 * density));

		iv1.setImageBitmap(bg);
		iv3.setImageBitmap(fg);
		ivClickable.setImageBitmap(bmClickable);

		// Mist
		RelativeLayout.LayoutParams paramsLayoutMist = new RelativeLayout.LayoutParams(
				2000 * density, 1000 * density);

		paramsLayoutMist.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

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

		view.setOnTouchListener(this);
		ivMoving.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Do the action associated with the white region
				bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
						R.drawable.pant_objecto, 180, 90);
				ivObjectFound.setImageBitmap(bitmap2);
 
				ivObjectFound.setVisibility(View.VISIBLE);

				// Persist object
				oi = new ObjectItem();
				oi.setObjectImageType(ObjectItem.TYPE_ROPE);
				oi.setTitle(getString(R.string.rope));

				onChoice.objectFoundPersist(oi);

				icon1.setImageBitmap(Utils.decodeSampledBitmapFromResource(
						getResources(), R.drawable.pant_objecto, 48, 48));

				checkObjectsAlreadyFound();
				
			}
		});
		
		//set listener to remove popup with object found
		ivObjectFound.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ivObjectFound.setVisibility(View.GONE);

			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.pag_3_images, container, false);

		//Play music
		BookActivity.playMusic(R.raw.swamp);
		
		density = (int) Math.ceil(getResources().getDisplayMetrics().density);

		buttonNext = (ImageButton) view.findViewById(R.id.goToNextPage);
		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { 

				if (checkObjectsAlreadyFound()) {
					PagChest fb = new PagChest();
					onChoice.onChoiceMade(fb, PagChest.NAME, PagChest.icon);

					onChoice.onChoiceMadeCommit(NAME, true);
				} else {

					Toast t = Toast.makeText(getActivity(),
							getString(R.string.findTwoObjects),
							Toast.LENGTH_SHORT);
					t.setGravity(Gravity.CENTER_HORIZONTAL
							| Gravity.CENTER_VERTICAL,
							80 * Math.round(density), 0);
					t.show();
				}
			}
		});

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagSwamp fb = new PagSwamp();

				onChoice.onChoiceMade(fb, PagSwamp.NAME);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		// loadImages
		loadImages();
		// check if we already found the objects so we can cross the lake
		checkObjectsAlreadyFound();

		loadText();

		return view;
	}

	private boolean checkObjectsAlreadyFound() {
		ObjectItem oiRope = new ObjectItem();
		ObjectItem oiPlank = new ObjectItem();

		oiRope.setObjectImageType(ObjectItem.TYPE_ROPE);
		oiPlank.setObjectImageType(ObjectItem.TYPE_PLANK);

		boolean isRopeOK = onChoice.isInObjects(oiRope);
		boolean isPlankOK = onChoice.isInObjects(oiPlank);

		if (isRopeOK && isPlankOK) {
			isObjectsFound = true;
		}

		if (isRopeOK) {
			icon1.setImageBitmap(Utils.decodeSampledBitmapFromResource(
					getResources(), R.drawable.pant_objecto, 48, 48));
		}

		if (isPlankOK) {
			icon2.setImageBitmap(Utils.decodeSampledBitmapFromResource(
					getResources(), R.drawable.plank, 48, 48));
		}

		return isObjectsFound;
	}

	@Override
	public void onDetach() {
		Log.d("Kingdom ", "Kingdom  onDetach()");
		super.onDetach();

		//remove listener
		view.setOnTouchListener(null);
		
		// image1.setImageBitmap(null);
		ivObjectFound.setImageBitmap(null);
		ivClickable.setImageBitmap(null);

		if (bitmap1 != null) {
			bitmap1.recycle();
			bitmap1 = null;
		}
		if (bitmap2 != null) {
			bitmap2.recycle();
			bitmap2 = null;
		}
		if (bitmap3 != null) {
			bitmap3.recycle();
			bitmap3 = null;
		}

	}

	AnimationDrawable backGroundChangeAnim;

	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		final int action = ev.getAction();
		switch (action) {

		case MotionEvent.ACTION_UP:

			int touchColor = Utils.getHotspotColor(ev, ivClickable);

			int tolerance = 25;
			if (Utils.closeMatch(Color.WHITE, touchColor, tolerance)) {
				// Do the action associated with the white region
				bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
						R.drawable.plank, 180, 90);
				ivObjectFound.setImageBitmap(bitmap2);

				ivObjectFound.setVisibility(View.VISIBLE);

				// Persist object
				oi = new ObjectItem();
				oi.setObjectImageType(ObjectItem.TYPE_PLANK);
				oi.setTitle(getString(R.string.wood));

				onChoice.objectFoundPersist(oi);

				icon2.setImageBitmap(Utils.decodeSampledBitmapFromResource(
				 getResources(), R.drawable.plank, 48, 48));
				checkObjectsAlreadyFound();

			
			} else {
				Log.d("PagSwamp Find obj", "Resto da imagem clicada");
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
