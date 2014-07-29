package com.dipeca.bookactivity;

import com.dipeca.prototype.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PagScareCrow extends Fragment implements OnTouchListener, IFragmentBook {
	View view = null; 
	private IMainActivity onChoice;
	public static int NAME = R.string.scarecrow;
	public static int icon = R.drawable.espantalho_mexer_icon;
	private TextView tv1 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;

	private static Bitmap bitmap1;
	private static Bitmap bitmap2;

	private static ImageButton buttonNext = null;
	private static ImageButton buttonPrev = null;
	private static ImageView ivBottle = null;
	private static ImageView ivCorn = null;
	private static Bitmap bottleBitmap = null;
	private static Bitmap cornBitmap = null;
	private static Bitmap warningBitmap = null;
	private static Bitmap wakedBitmap = null;
	private static Bitmap saveBottleBitmap = null;
	private static boolean isObjectsFound = false;
	private static boolean isCornInObjects = false;
	private static boolean isBottleInObjects = false;
	private static boolean lockPersistBottle = false;
	private ImageView image1;
	private ImageView image2;
	
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

	private boolean checkObjectsAlreadyFound(){
		ObjectItem oiCorn  = new ObjectItem();
		ObjectItem oiBottle = new ObjectItem();
		
		oiCorn.setObjectImageType(ObjectItem.TYPE_CORN);
		oiBottle.setObjectImageType(ObjectItem.TYPE_BOTTLE);
		
		isCornInObjects = onChoice.isInObjects(oiCorn);
		isBottleInObjects = onChoice.isInObjects(oiBottle);
		
		if (isCornInObjects && isBottleInObjects){
			isObjectsFound = isBottleInObjects;
		}
		
		return isObjectsFound;
	}
	
	private void loadImages() {

		if (isAdded()) { 

			Log.d("PagScareCrow ", "loadImages()");
			image2 = (ImageView) view.findViewById(R.id.clickable);

			if (bitmap1 == null) {
				bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
						R.drawable.espantalho1_1, 600, 300);
			}
			image1.setImageBitmap(bitmap1);
			
			if (bitmap2 == null) {
				bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
						R.drawable.espantalho_click, 50, 25);
			}

			image2.setImageBitmap(bitmap2);

			if (cornBitmap == null) {
				cornBitmap = Utils.decodeSampledBitmapFromResource(
						getResources(), R.drawable.milho, 360, 180);

				ivCorn = new ImageView(getActivity());
				ivCorn.setImageBitmap(cornBitmap);
				// Create a simple layout and add our image view to it.
				RelativeLayout layout = (RelativeLayout) view
						.findViewById(R.id.scareCrowRL);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				params.setMargins(0, 0, 64, 0);
				layout.addView(ivCorn, params);
			}

			if (bottleBitmap == null) {
				bottleBitmap = Utils.decodeSampledBitmapFromResource(
						getResources(), R.drawable.frasco, 160, 80);

				ivBottle = new ImageView(getActivity());
				ivBottle.setImageBitmap(bottleBitmap);
				// Create a simple layout and add our image view to it.
				RelativeLayout layout = (RelativeLayout) view
						.findViewById(R.id.scareCrowRL);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				layout.addView(ivBottle, params);
			}

			if (warningBitmap == null) {
				warningBitmap = Utils.decodeSampledBitmapFromResource(
						getResources(), R.drawable.espantalho1_2, 600, 300);
			}

			if (wakedBitmap == null) {
				wakedBitmap = Utils.decodeSampledBitmapFromResource(
						getResources(), R.drawable.espantalho1_3, 600, 300);
			}

			if (saveBottleBitmap == null) {
				saveBottleBitmap = Utils.decodeSampledBitmapFromResource(
						getResources(), R.drawable.espantalho1_4, 600, 300);
			}

			ivCorn.setVisibility(View.GONE);
			ivBottle.setVisibility(View.GONE);

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.scarecrow, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time kingdom", "onCreateView after inflate time ="
				+ totalTime);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagScareCrowTitle);
		//tv1.setWidth(tv1.getWidth() - 20);
		tv3 = (TextView) view.findViewById(R.id.textPag1_2);
		tv3.setVisibility(View.GONE);

		// Postpone image loading
		view.postDelayed(mUpdateTimeTask, 500);
		
		//We must still load the first image
		image1 = (ImageView) view.findViewById(R.id.pag1ImageView);
		if (bitmap1 == null) {
			bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
					R.drawable.espantalho1_1, 600, 300);
		}
		image1.setImageBitmap(bitmap1);
		
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

		buttonNext = (ImageButton) view.findViewById(R.id.goToNextPage);
		buttonNext.setVisibility(View.GONE);

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagPathChoiceFrg fb = new PagPathChoiceFrg();

				onChoice.onChoiceMade(fb, getString( PagPathChoiceFrg.NAME));
				onChoice.onChoiceMadeCommit(getString( NAME), true);
			}
		});

		view.setOnTouchListener(this);
		view.setOnDragListener(new MyDragListener());
		// BookActivity.playMusic(R.raw.village);
		return view;
	}

	@Override
	public void onDetach() {
		Log.d("Kingdom ", "Kingdom  onDetach()");
		super.onDetach();
		if (bitmap1 != null) {
			bitmap1.recycle();
			bitmap1 = null;
		}

		// if (bitmap2 != null) {
		// bitmap2.recycle();
		// bitmap2 = null;
		// }
		if (cornBitmap != null) {
			cornBitmap.recycle();
			cornBitmap = null;
		}

		if (bottleBitmap != null) {
			bottleBitmap.recycle();
			bottleBitmap = null;
		}
	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			Log.d("PagScareCrow ", "mUpdateTimeTask");
			
			tv1.setText(R.string.pagScareCrowTitle);

			loadImages();
		}
	};

	private Runnable persistBottle = new Runnable() {
		public void run() {
			Log.d("PagScareCrow ", "persistBottle");
			
			persistBottle();
		}
	};
	
	private boolean isBottleDragging = false;
	
	@Override
	public boolean onTouch(View arg0, MotionEvent ev) {
		final int action = ev.getAction();
		// (1)
		final int evX = (int) ev.getX();
		final int evY = (int) ev.getY();
		switch (action) {

		case MotionEvent.ACTION_DOWN:

			int touchColor = Utils.getHotspotColor(R.id.clickable, evX, evY,
					view);

			int tolerance = 25;
			if (Utils.closeMatch(Color.RED, touchColor, tolerance)) {
				tv1.setText(R.string.pagScareCrowWarning);
				// Do the action associated with the white region
				image1.setImageBitmap(warningBitmap);

				// Each time we touch the scarecrow we loose 5points
				onChoice.setAddPoints(-5);

				// run the start() method later on the UI thread
				view.postDelayed(mUpdateTimeTask, 1000);

			} else if (Utils.closeMatch(Color.BLUE, touchColor, tolerance)) {

				tv1.setText(R.string.pagScareCrowDontTouchThere);
				// Do the action associated with the white region
				image1.setImageBitmap(wakedBitmap);

				// Each time we touch the scarecrow we loose 5points
				onChoice.setAddPoints(-10);

				// run the start() method later on the UI thread
				view.postDelayed(mUpdateTimeTask, 1000);
			} else if (Utils.closeMatch(Color.GREEN, touchColor, tolerance)) {

				isBottleDragging = false;
				tv1.setText(R.string.pagScareCrowThisIsNotABottle);
				// ivCorn.setVisibility(View.VISIBLE);

				ClipData data = ClipData.newPlainText("", "");
				View.DragShadowBuilder shadowBuilder = ImageDragShadowBuilder
						.fromBitmap(getActivity(), cornBitmap);
				ivCorn.startDrag(data, shadowBuilder, view, 0);

				// 10 points for the curiosity ;)
				checkObjectsAlreadyFound();
				if(!isCornInObjects){
					onChoice.setAddPoints(10);
					persistCorn();
				}
				
			} else if (Utils.closeMatch(Color.WHITE, touchColor, tolerance)) {
				isBottleDragging = true;
				tv1.setText(R.string.pagScareCrowSaveBottle);

				// Start dragging
				ClipData data = ClipData.newPlainText("", "");
				View.DragShadowBuilder shadowBuilder = ImageDragShadowBuilder
						.fromBitmap(getActivity(), bottleBitmap);
				try {
					ivBottle.startDrag(data, shadowBuilder, ivBottle, 0);
					// ivBottle.setVisibility(View.GONE);
				} catch (Exception e) {
					Log.d("Drag error", " error");
				}

				// Do the action associated with the white region
				image1.setImageBitmap(saveBottleBitmap);

				return true;

			} else {
				Log.d("PagFindFriend", "Resto da imagem clicada");
			}
			break;
		}

		return true;
	}

	private static ObjectItem oi = null;

	private void persistBottle() {
		// Persist object
		oi = new ObjectItem();
		oi.setObjectImageType(ObjectItem.TYPE_BOTTLE);
		oi.setName("Frasco");

		onChoice.objectFoundPersist(oi);

	}

	private void persistCorn() {
		// Persist object
		oi = new ObjectItem();
		oi.setObjectImageType(ObjectItem.TYPE_CORN);
		oi.setName("Milho");

		onChoice.objectFoundPersist(oi);

	}
	
	class MyDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			Log.d("onDrag", event.getAction() + "");
			int touchColor = Utils.getHotspotColor(R.id.clickable,
					(int) event.getX(), (int) event.getY(), view);
			int tolerance = 25;

			ivCorn.setVisibility(View.GONE);
			ivBottle.setVisibility(View.GONE);

			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				Log.d("onDrag", "ACTION_DRAG_STARTED");
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				Log.d("onDrag", "ACTION_DRAG_ENTERED");
				break;
			case DragEvent.ACTION_DRAG_LOCATION:
				Log.d("onDrag", "x: " + event.getX() + "y: " + event.getY());

				if (Utils.closeMatch(Color.YELLOW, touchColor, tolerance)) {
					Log.d("color", "YELLOW");

				}
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				Log.d("onDrag", event.getAction() + " ACTION_DRAG_EXITED");
				break;
			case DragEvent.ACTION_DROP:
				Log.d("onDrag", event.getAction() + " ACTION_DROP");
				if (Utils.closeMatch(Color.YELLOW, touchColor, tolerance) && isBottleDragging) {
					Log.d("color", "YELLOW");

					checkObjectsAlreadyFound();
					if(!isBottleInObjects && !lockPersistBottle){
						
						lockPersistBottle = true;
						onChoice.setAddPoints(40);
						view.postDelayed(persistBottle, 1);
					}

					PagAfterChallengeScareCrow fb = new PagAfterChallengeScareCrow();

					onChoice.onChoiceMade(fb, getString( PagAfterChallengeScareCrow.NAME));
					onChoice.onChoiceMadeCommit(getString( NAME), true);
				}

				break;
			case DragEvent.ACTION_DRAG_ENDED:
				Log.d("onDrag", event.getAction() + " ACTION_DRAG_ENDED");
				// run the start() method later on the UI thread
				view.postDelayed(mUpdateTimeTask, 200);
			default:
				Log.d("onDrag", event.getAction() + "");
				break;
			}
			return true;
		}
	}

	@Override
	public String getPrevPage() {
		return PagPathChoiceFrg.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}

}
