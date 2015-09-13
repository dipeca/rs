package com.dipeca.bookactivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
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
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagEnterGateThiefsLand extends Fragment implements IFragmentBook {
	View view = null; 
	private IMainActivity onChoice;
	public static int NAME = R.string.theGate;
	public static int icon = R.drawable.gate_icon;
	private TextView tv1 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;

	private Bitmap bitmap1;
	private Bitmap bitmap2;
	private Bitmap bitmap3;

	private ImageButton buttonNext = null; 
	private ImageButton buttonPrev = null;
	private ImageView ivKey;

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
	private ImageView ivClickable;

	private void loadImages() {

		int density = (int) Math.ceil(getResources().getDisplayMetrics().density);
		iv1 = (ImageView) view.findViewById(R.id.ivMain);
		iv2 = (ImageView) view.findViewById(R.id.ivSmall);
		ivClickable = (ImageView) view.findViewById(R.id.ivSmallHidden);
		RelativeLayout.LayoutParams rl = (LayoutParams) iv2.getLayoutParams();

		Utils.removeRule(rl, RelativeLayout.ALIGN_PARENT_RIGHT);
		rl.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rl.setMargins(0, 48 * Math.round(density), 0, 0);
		rl.width = 740 * Math.round(density);
		rl.height = 580 * Math.round(density);

		ivClickable.setLayoutParams(rl);
		// ivClickable.setVisibility(View.VISIBLE);

		bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.robot_destroyed2, 400 * density, 200 * density);

		bitmap1 = Utils.changeBitmapContrastBrightness(bitmap1, 1, -48);

		iv1.setImageBitmap(bitmap1);

		bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.gate, 480, 240);
		iv2.setImageBitmap(bitmap2);

		bitmap3 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.gate_cli, 40, 24);
		ivClickable.setImageBitmap(bitmap3);

		ivKey = new ImageView(this.getActivity());
		ivKey.setImageBitmap(Utils.decodeSampledBitmapFromResource(
				getResources(), R.drawable.chave, 128, 128));
		((RelativeLayout) view.getRootView()).addView(ivKey);

		ivKey.setOnTouchListener(new MyTouchListener());
		
		ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(
				ivKey, "translationY", 360 * density);
		objectAnimatorY.setDuration(2000);
		objectAnimatorY.start();
		
		ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(
				ivKey, "translationX", 400 * density);
		objectAnimatorX.setDuration(2000);
		objectAnimatorX.start();
		
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_sub_image, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time kingdom", "onCreateView after inflate time ="
				+ totalTime);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagGateOpen);

		tv3 = (TextView) view.findViewById(R.id.textPag1_2);
		tv3.setVisibility(View.GONE);

		// loadImages()
		loadImages();

		tv1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				float density = (float) getResources().getDisplayMetrics().density;
				int height1 = 0;
				int width3 = 0;
				int multiplier = 7;
				if (isTextHide) {
					height1 = tv1.getHeight() * multiplier - ((int)Math.ceil(8 * density));
					width3 = tv3.getWidth() * multiplier - ((int)Math.ceil(8 * density));
					isTextHide = false;
				} else {
					height1 = tv1.getHeight() / multiplier + ((int)Math.ceil(8 * density));
					width3 = tv3.getWidth() / multiplier + ((int)Math.ceil(8 * density));
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

				PagEnigmaFish fb = new PagEnigmaFish();

				onChoice.onChoiceMade(fb, PagEnigmaFish.NAME);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		iv2.setOnDragListener(new MyDragListener());
		
		// Add buttonNext to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);
				
		return view;
	}

	@Override
	public void onDetach() {
		Log.d("PagLateToCross ", "PagLateToCross  onDetach()");
		super.onDetach();

		bitmap1.recycle();
		bitmap1 = null;

		bitmap2.recycle();
		bitmap2 = null;

	}

	@Override
	public String getPrevPage() {
		return PagEnigmaFish.class.getName();
	}

	@Override
	public String getNextPage() { 
		return null;
	}

	class MyDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent event) {
			Log.d("onDrag", event.getAction() + "");
			int touchColor = Utils.getHotspotColor(event, ivClickable);
			int tolerance = 25;
			switch (event.getAction()) {

			case DragEvent.ACTION_DROP:
				Log.d("onDrag", event.getAction() + " ACTION_DROP");
				if (Utils.closeMatch(Color.WHITE, touchColor, tolerance)) {
					long startTime = System.currentTimeMillis();

					PagBoss fb = new PagBoss();

					onChoice.onChoiceMade(fb, getString(PagBoss.NAME),
							getResources().getResourceName(PagBoss.icon));
					onChoice.onChoiceMadeCommit(getString(NAME), true);
					long endTime = System.currentTimeMillis();
					long totalTime = endTime - startTime;

					Log.d("Total time", "time =" + totalTime);
					view.setOnDragListener(null);
				} else {
					Log.d("onDrag", "else..");
					// if we do not have found the portal, we set the object to
					// it's initial position
					ivKey.setVisibility(View.VISIBLE);
				}

				break;
			case DragEvent.ACTION_DRAG_ENDED:
				Log.d("onDrag", event.getAction() + " ACTION_DRAG_ENDED");
				ivKey.setVisibility(View.VISIBLE);
			default:
				Log.d("onDrag", event.getAction() + "");
				break;
			}
			return true;
		}
	}

}
