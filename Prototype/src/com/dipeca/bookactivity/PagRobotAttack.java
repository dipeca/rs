package com.dipeca.bookactivity;

import android.R.array;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.dipeca.prototype.R;

public class PagRobotAttack extends Fragment implements IFragmentBook{
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.robotAttacks;
	public static int icon = R.drawable.robot_attack_icon;

	private TextView tv1 = null;

	private ImageView iv1;
	private ImageView iv2;
	private ImageView ivBattery;
	private ImageView ivTalisman;
	private int density;

	private boolean isTextHide = false;

	private static Bitmap bitmap1;
	private static Bitmap bitmap2;
	private static Bitmap bitmap3;

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
		tv1.setBackgroundResource(R.drawable.container_dropshadow_red);
		tv1.setPadding(density * 16, density * 16, density * 16, density * 16);
		tv1.setText(R.string.pagRobotAttack);
	}
	
	private void loadImages() {
		Log.d(getString(NAME), "loadImages()");
		iv1 = (ImageView) view.findViewById(R.id.topleft);
		iv2 = (ImageView) view.findViewById(R.id.clickable);
		ivBattery = (ImageView) view.findViewById(R.id.porta);
		ivTalisman = (ImageView) view.findViewById(R.id.talisma);

		density = (int) getResources().getDisplayMetrics().density;

		iv1.setImageDrawable(getResources().getDrawable(R.anim.robot_atack));
		((AnimationDrawable) iv1.getDrawable()).start();

		// battery highlighted
		if (BookActivity.bitmap1 == null) {
			BookActivity.bitmap1 = Utils.decodeSampledBitmapFromResource(
					getResources(), R.drawable.robot3, 600, 300);
		}

		ivBattery.setImageBitmap(BookActivity.bitmap1);

		if (BookActivity.bitmap2 == null) {
			BookActivity.bitmap2 = Utils.decodeSampledBitmapFromResource(
					getResources(), R.drawable.robot_click_scal, 50, 25);

		}

		iv2.setImageBitmap(BookActivity.bitmap2);

		if (BookActivity.bitmapTalisma == null) {

			BookActivity.bitmapTalisma = Utils.decodeSampledBitmapFromResource(
					getResources(), R.drawable.talisma, 162, 162);
		}

		ivTalisman.setImageBitmap(BookActivity.bitmapTalisma);

	}

	ImageButton button = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_drag_object, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d(getString(NAME), "onCreateView after inflate time =" + totalTime);

		// loadImages()
		loadImages();
		loadText();

//		BookActivity.playMusic(R.raw.robot_in_front);

		ivTalisman.setOnTouchListener(new MyTouchListener());
		view.setOnDragListener(new MyDragListener());

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

		if (BookActivity.bitmap1 != null) {
			BookActivity.bitmap1.recycle();
			BookActivity.bitmap1 = null;
		}

		if (BookActivity.bitmap2 != null) {
			BookActivity.bitmap2.recycle();
			BookActivity.bitmap2 = null;
		}

		if (BookActivity.bitmapInitial != null) {
			BookActivity.bitmapInitial.recycle();
			BookActivity.bitmapInitial = null;
		}

		if (BookActivity.bitmapTalisma != null) {
			BookActivity.bitmapTalisma.recycle();
			BookActivity.bitmapTalisma = null;
		}
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

	class MyDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent event) {
			Log.d("onDrag", event.getAction() + "");
			
			int touchColor = Utils.getHotspotColor(event, iv2);
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
					ivBattery.setVisibility(ImageView.GONE);
				} else if (Utils.closeMatch(Color.RED, touchColor, tolerance)) {
					Log.d("color", "RED");
					// imgPortaWeek.setVisibility(ImageView.GONE);
					ivBattery.setVisibility(ImageView.VISIBLE);
				} else {
					ivBattery.setVisibility(ImageView.GONE);
					// imgPortaWeek.setVisibility(ImageView.GONE);
					Log.d("color", "Not RED");
				}

				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				if (Utils.closeMatch(Color.RED, touchColor, tolerance)) {

					PagRobotDestroyedfrg fb = new PagRobotDestroyedfrg();

					onChoice.onChoiceMade(fb, getString(PagRobotDestroyedfrg.NAME), getResources().getResourceName(PagRobotDestroyedfrg.icon));
					onChoice.onChoiceMadeCommit(getString(NAME), true);

					view.setOnDragListener(null);
				} else {
					// if we do not have found the portal, we set the object to
					// it's initial position
					ivTalisman.setVisibility(View.VISIBLE);
				}

				break;
			case DragEvent.ACTION_DRAG_ENDED:
				ivTalisman.setVisibility(View.VISIBLE);
			default:
				break;
			}
			return true;
		}
	}

	@Override
	public String getPrevPage() {
		return PagRobotAttack.class.getName();
	}

	@Override
	public String getNextPage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
