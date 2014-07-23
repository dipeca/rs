package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PagLakeToCrossFindObjects extends Fragment implements
		OnTouchListener, IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.theLakeToCross;
	public static int icon = R.drawable.lago_icon;
	private TextView tv1 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;

	private static Bitmap bitmap1;
	private static Bitmap bitmap2;
	private static Bitmap bitmap3;

	private static DialogBox dialog = null;
	ImageButton button = null;
	ImageButton buttonPrev = null;

	private static ObjectItem oi = null;
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

	private ImageView image1;
	private ImageView image2;
	private ImageView ivClickable;
	private ImageView icon1;
	private ImageView icon2;

	private void loadImages() {
		Log.d("KingDom ", "loadImages()");
		image1 = (ImageView) view.findViewById(R.id.pag1ImageView);
		ivClickable = (ImageView) view.findViewById(R.id.clickable);

		bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.lagoteste2, 600, 300);
		image1.setImageBitmap(bitmap1);

		bitmap3 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.lagoteste2todrop, 300, 150);
		ivClickable = (ImageView) view.findViewById(R.id.clickable);
		ivClickable.setImageBitmap(bitmap3);

		image2 = (ImageView) view.findViewById(R.id.pag1Amuleto);

		icon1 = (ImageView) view.findViewById(R.id.icon1);
		icon2 = (ImageView) view.findViewById(R.id.icon2);
		
		icon1.setVisibility(View.VISIBLE);
		icon2.setVisibility(View.VISIBLE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater
				.inflate(R.layout.pag_one_image_dialog, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time kingdom", "onCreateView after inflate time ="
				+ totalTime);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagLake);

		tv3 = (TextView) view.findViewById(R.id.textPag1_2); 
		tv3.setVisibility(View.GONE);

		dialog = (DialogBox) view.findViewById(R.id.dialog);
		dialog.setTextDialog(getString(R.string.pagLakeGuiDialog));
		dialog.setImg2Id(getResources().getDrawable(R.anim.gui_anim));
		dialog.setImg1Id(getResources().getDrawable(R.anim.friend_dialog));
		 
		// loadImages
		loadImages();

		//check if we already found the objects so we can cross the lake
		checkObjectsAlreadyFound();
		
		image2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				image2.setVisibility(View.INVISIBLE);

			}
		});

		tv1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int density = (int) getResources().getDisplayMetrics().density;
				int height1 = 0;
				int width3 = 0;
				int multiplier = 7;
				if (isTextHide) {
					height1 = tv1.getHeight() * multiplier - (8 * density);
					width3 = tv3.getWidth() * multiplier - (8 * density);
					isTextHide = false;
				} else {
					height1 = tv1.getHeight() / multiplier + (8 * density);
					width3 = tv3.getWidth() / multiplier + (8 * density);
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

				if(checkObjectsAlreadyFound()){
					PagChest fb = new PagChest();
					onChoice.onChoiceMade(fb, PagChest.NAME, PagChest.icon);
	
					onChoice.onChoiceMadeCommit(NAME, true);
				}else{
					Toast.makeText(getActivity(), getString(R.string.findTwoObjects), Toast.LENGTH_SHORT).show();
				}
			}
		});

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagLakeToCross fb = new PagLakeToCross();

				onChoice.onChoiceMade(fb, PagLakeToCross.NAME);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		view.setOnTouchListener(this);

		// BookActivity.playMusic(R.raw.village);
		return view;
	}
	
	private boolean checkObjectsAlreadyFound(){
		ObjectItem oiRope  = new ObjectItem();
		ObjectItem oiPlank = new ObjectItem();
		
		oiRope.setObjectImageType(ObjectItem.TYPE_ROPE);
		oiPlank.setObjectImageType(ObjectItem.TYPE_PLANK);
		
		boolean isRopeOK = onChoice.isInObjects(oiRope);
		boolean isPlankOK = onChoice.isInObjects(oiPlank);
		
		if (isRopeOK && isPlankOK){
			isObjectsFound = true;
		}
		
		if(isRopeOK){
			icon1.setImageBitmap(Utils.decodeSampledBitmapFromResource(getResources(),
					R.drawable.rope, 48, 48));
		}

		if(isPlankOK){
			icon2.setImageBitmap(Utils.decodeSampledBitmapFromResource(getResources(),
					R.drawable.plank, 48, 48));
		}
		
		return isObjectsFound;
	}

	@Override
	public void onDetach() {
		Log.d("Kingdom ", "Kingdom  onDetach()");
		super.onDetach();

		bitmap1.recycle();
		bitmap1 = null;

	}

	AnimationDrawable backGroundChangeAnim;

	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		final int action = ev.getAction();
		// (1)
		final int evX = (int) ev.getX();
		final int evY = (int) ev.getY();
		switch (action) {

		case MotionEvent.ACTION_UP:

			int touchColor = Utils.getHotspotColor(R.id.clickable, evX, evY,
					view);

			int tolerance = 25;
			if (Utils.closeMatch(Color.WHITE, touchColor, tolerance)) {
				// Do the action associated with the white region
				bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
						R.drawable.plank, 180, 90);
				image2.setImageBitmap(bitmap2);
				
				image2.setVisibility(View.VISIBLE);
				
				//Persist object
				oi = new ObjectItem();
				oi.setObjectImageType(ObjectItem.TYPE_PLANK);
				oi.setName(getString(R.string.wood));
				
				onChoice.objectFoundPersist(oi);
				
				icon2.setImageBitmap(Utils.decodeSampledBitmapFromResource(getResources(),
						R.drawable.plank, 48, 48));
				checkObjectsAlreadyFound();

				
			}if (Utils.closeMatch(Color.RED, touchColor, tolerance)) {
				// Do the action associated with the white region
				bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
						R.drawable.rope, 180, 90);
				image2.setImageBitmap(bitmap2);
				
				image2.setVisibility(View.VISIBLE);
				
				//Persist object
				oi = new ObjectItem();
				oi.setObjectImageType(ObjectItem.TYPE_ROPE);
				oi.setName(getString(R.string.rope));
				
				onChoice.objectFoundPersist(oi);
				
				icon1.setImageBitmap(Utils.decodeSampledBitmapFromResource(getResources(),
						R.drawable.rope, 48, 48));
				
				checkObjectsAlreadyFound();
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
