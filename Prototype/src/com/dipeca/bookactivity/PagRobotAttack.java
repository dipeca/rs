package com.dipeca.bookactivity;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagRobotAttack extends Fragment implements IFragmentBook {
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

	private Bitmap bitmap1;

	private ImageButton btnOpenPopup = null;
	private PopupWindow popupWindow = null;
	private ImageButton btnFacebookHelp = null;
	private ImageButton btnGPlusHelp = null;

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
		tv1.setPadding((int) Math.ceil(density * 16),
				(int) Math.ceil(density * 16), (int) Math.ceil(density * 16),
				(int) Math.ceil(density * 16));
		tv1.setText(R.string.pagRobotAttack);
	}

	private void loadImages() {
		Log.d(getString(NAME), "loadImages()");
		iv1 = (ImageView) view.findViewById(R.id.topleft);
		iv2 = (ImageView) view.findViewById(R.id.clickable);
		ivBattery = (ImageView) view.findViewById(R.id.porta);
		ivTalisman = (ImageView) view.findViewById(R.id.talisma);
 
		iv1.setImageDrawable(getResources().getDrawable(R.anim.robot_atack));
		((AnimationDrawable) iv1.getDrawable()).start();

		// battery highlighted
		BookActivity.bitmap1 = onChoice
				.decodeSampledBitmapFromResourceBG(getResources(),
						R.drawable.robot3, 400 * density, 200 * density);
		Log.d("dalvikvm-heap robot:", "robot3");

		ivBattery.setImageBitmap(BookActivity.bitmap1);

		BookActivity.bitmap2 = Utils.decodeSampledBitmapFromResource(
				getResources(), R.drawable.robot_click_scal, 50, 25);
		Log.d("dalvikvm-heap robot:", "robot_click_scal");

		iv2.setImageBitmap(BookActivity.bitmap2);

		if (BookActivity.bitmapTalisma == null) {

			BookActivity.bitmapTalisma = Utils
					.decodeSampledBitmapFromResource(getResources(),
							R.drawable.talisma, 128 *(int) Math.ceil(density), 128 * (int)Math.ceil(density));
			Log.d("dalvikvm-heap robot:", "talisma");
		}

		ivTalisman.setImageBitmap(BookActivity.bitmapTalisma);

	}

	ImageButton button = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);
		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_drag_object, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d(getString(NAME), "onCreateView after inflate time =" + totalTime);

		loadImages();
		loadText();

		BookActivity.playMusic(R.raw.robot_walk);
		Log.d("dalvikvm-heap robot music:", "robot_walk");
		ivTalisman.setOnTouchListener(new MyTouchListener());
		view.setOnDragListener(new MyDragListener());

		btnOpenPopup = new ImageButton(getActivity());
		btnOpenPopup.setImageResource(R.drawable.ic_action_social_help);
		btnOpenPopup.setBackgroundResource(R.drawable.button_back);

		RelativeLayout.LayoutParams paramsSocialBtn = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsSocialBtn.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsSocialBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// paramsSocialBtn.addRule(RelativeLayout.LEFT_OF, btnHelp.getId());
		paramsSocialBtn.setMargins(0, (int) Math.ceil(12 * density),
				(int) Math.ceil(12 * density), 0);

		((RelativeLayout) view.getRootView()).addView(btnOpenPopup,
				paramsSocialBtn);

		btnOpenPopup.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (popupWindow == null || !popupWindow.isShowing()) {

					LayoutInflater layoutInflater = (LayoutInflater) getActivity()
							.getBaseContext().getSystemService(
									getActivity().LAYOUT_INFLATER_SERVICE);
					View popupView = layoutInflater.inflate(
							R.layout.social_popup, null);
					popupWindow = new PopupWindow(popupView, (int) Math
							.ceil(164 * density), (int) Math
							.ceil(198 * density));

					popupView.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							popupWindow.dismiss();

						}
					});

					popupWindow.showAsDropDown(btnOpenPopup,
							-(int) Math.ceil(32 * density),
							(int) Math.ceil(0 * density));

					// FaceBook buttonNext
					btnFacebookHelp = (ImageButton) popupView
							.findViewById(R.id.facebook_button);
					btnFacebookHelp.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onChoice.askForHelpOnFacebook();

							if (popupWindow != null && popupWindow.isShowing()) {
								popupWindow.dismiss();
							}
						}
					});

					// Google Plus buttonNext
					btnGPlusHelp = (ImageButton) popupView
							.findViewById(R.id.gplus_button);
					btnGPlusHelp.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onChoice.askForHelpOnGooglePlus();

							if (popupWindow != null && popupWindow.isShowing()) {
								popupWindow.dismiss();
							}
						}
					});
				}
			}
		});

		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}

			}
		});
		
		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pagrobotAttack), bitmap1));
		
		return view;
	}

	@Override
	public void onDetach() {
		Log.d(getString(NAME), " onDetach()");
		super.onDetach();

		ivBattery.setImageBitmap(null);
		ivTalisman.setImageBitmap(null);
		iv2.setImageBitmap(null);
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

					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
					}

					PagRobotDestroyedfrg fb = new PagRobotDestroyedfrg();

					onChoice.onChoiceMade(
							fb,
							getString(PagRobotDestroyedfrg.NAME),
							getResources().getResourceName(
									PagRobotDestroyedfrg.icon));
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
		return PagRobot.class.getName();
	}

	@Override
	public String getNextPage() {
		// TODO Auto-generated method stub
		return null;
	}

}
