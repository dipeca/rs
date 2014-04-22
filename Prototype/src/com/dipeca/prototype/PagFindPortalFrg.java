package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

public class PagFindPortalFrg extends Fragment {

	@Override
	public void onDetach() {
		super.onDetach();

		Log.d("FindPortal", "onDetach");

		BookActivity.bitmap1.recycle();
		BookActivity.bitmap1 = null;

		BookActivity.bitmap2.recycle();
		BookActivity.bitmap2 = null;

		BookActivity.bitmapInitial.recycle();
		BookActivity.bitmapInitial = null;

		BookActivity.bitmapTalisma.recycle();
		BookActivity.bitmapTalisma = null;
	}

	public static String NAME = "Find portal";

	// Create a string for the ImageView label
	View view;
	ImageView ivRoom;
	ImageView ivPortal;
	ImageView ivTalisman;
	ImageView ivClickable;

	private IMainActivity onChoice; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {    

		long startTime = System.currentTimeMillis();

		view = inflater.inflate(R.layout.pag_drag_object, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time find portal", "onCreateView after inflate time ="
				+ totalTime);

		ivTalisman = (ImageView) view.findViewById(R.id.talisma);
		ivTalisman.setOnTouchListener(new MyTouchListener());

		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
		Log.d("Total time", "onCreateView before dragListener time ="
				+ totalTime);

		view.setOnDragListener(new MyDragListener());

		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
		Log.d("Total time", "onCreateView before load images time ="
				+ totalTime);

		loadImages();

		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
		Log.d("Total time", "onCreateView time =" + totalTime);

		return view;

	}

	private final class MyTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				view.startDrag(data, shadowBuilder, view, 0);
				view.setVisibility(View.INVISIBLE);
				return true;
			} else {
				return false;
			}
		}
	}

	private void loadImages() {
		Log.d("PagFindPortal ", "loadImages()");
		long startTime = System.currentTimeMillis();

		ivRoom = (ImageView) view.findViewById(R.id.topleft);
		ivPortal = (ImageView) view.findViewById(R.id.porta);
		ivClickable = (ImageView) view.findViewById(R.id.clickable);

		int density = (int) getResources().getDisplayMetrics().density;

		if (BookActivity.bitmapInitial == null) {
			BookActivity.bitmapInitial = Utils.decodeSampledBitmapFromResource(
					getResources(), R.drawable.quarto_vazio, 600,
					300);

		}

		if (BookActivity.bitmap1 == null) {
			BookActivity.bitmap1 = Utils.decodeSampledBitmapFromResource(
					getResources(), R.drawable.quarto_portal, 600,
					300);

		}

		if (BookActivity.bitmap2 == null) {

			BookActivity.bitmap2 = Utils.decodeSampledBitmapFromResource(
					getResources(), R.drawable.quarto_cli, 50 * density,
					25 * density);

		}

		if (BookActivity.bitmapTalisma == null) {

			BookActivity.bitmapTalisma = Utils.decodeSampledBitmapFromResource(
					getResources(), R.drawable.talisma, 162,
					162);
		}

		ivRoom.setImageBitmap(BookActivity.bitmapInitial);
		ivPortal.setImageBitmap(BookActivity.bitmap1);
		ivClickable.setImageBitmap(BookActivity.bitmap2);
		ivTalisman.setImageBitmap(BookActivity.bitmapTalisma);

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time", " loadImages time =" + totalTime);
		
		Log.d(NAME, "width: " + ivTalisman.getWidth() + " layout Params width: " + ivTalisman.getLayoutParams().width + " x: " + ivTalisman.getX());
		
		int[] location = new int[2];
		Rect rectf = new Rect();
		ivTalisman.getLocalVisibleRect(rectf);

		
		
		float toXPos = 0;
		float toYPos = 0;
		
		TranslateAnimation ta = new TranslateAnimation(-800*density, toXPos, 200*density, toYPos);
		ta.setDuration(2000);
		ivTalisman.setAnimation(ta);
		ivTalisman.getAnimation().start();
	}

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

	private int density;
	class MyDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent event) {
			Log.d("onDrag", event.getAction() + "");
			int touchColor = Utils.getHotspotColor(R.id.clickable,
					(int) event.getX(), (int) event.getY(), view);
			int tolerance = 25;
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				Log.d("onDrag", "ACTION_DRAG_STARTED");
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				Log.d("onDrag", "ACTION_DRAG_ENTERED");
				break;
			case DragEvent.ACTION_DRAG_LOCATION:
				Log.d("onDrag", "x: " + event.getX() + "y: " + event.getY());

				if (Utils.closeMatch(Color.WHITE, touchColor, tolerance)) {
					Log.d("color", "WHITE");
					// imgPortaWeek.setVisibility(ImageView.VISIBLE);
					// imgPorta.setVisibility(ImageView.GONE);
				} else if (Utils.closeMatch(Color.RED, touchColor, tolerance)) {
					Log.d("color", "RED");
					// imgPortaWeek.setVisibility(ImageView.GONE);
					ivPortal.setVisibility(ImageView.VISIBLE);
				} else {
					ivPortal.setVisibility(ImageView.GONE);
					// imgPortaWeek.setVisibility(ImageView.GONE);
					Log.d("color", "Not RED");
				}

				break;
			case DragEvent.ACTION_DRAG_EXITED:
				Log.d("onDrag", event.getAction() + " ACTION_DRAG_EXITED");
				break;
			case DragEvent.ACTION_DROP:
				Log.d("onDrag", event.getAction() + " ACTION_DROP");
				if (Utils.closeMatch(Color.RED, touchColor, tolerance)) {
					long startTime = System.currentTimeMillis();

					Toast.makeText(getActivity(), R.string.success,
							Toast.LENGTH_LONG).show();
					PagKingDomfrg fb = new PagKingDomfrg();

					onChoice.onChoiceMade(fb, PagVillageFrg.NAME);
					onChoice.onChoiceMadeCommit(NAME, true);

					long endTime = System.currentTimeMillis();
					long totalTime = endTime - startTime;
					
					Log.d("Total time", "time =" + totalTime);
					view.setOnDragListener(null);
				} else {
					//if we do not have found the portal, we set the object to it's initial position
					ivTalisman.setVisibility(View.VISIBLE);
				}

				break;
			case DragEvent.ACTION_DRAG_ENDED:
				Log.d("onDrag", event.getAction() + " ACTION_DRAG_ENDED");
			default:
				Log.d("onDrag", event.getAction() + "");
				break;
			}
			return true;
		}
	}
}
