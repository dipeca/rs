package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dipeca.item.DialogBox;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagRobot extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.robot;
	public static int icon = R.drawable.robot_icon;
	private TextView tv1 = null;
	private TextView tv3 = null;
	private DialogBox dialogBox = null;
	private boolean isTextHide = false;

	private Bitmap bitmap1;

	private int density = 1;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			Log.d(getString(NAME), "On attach started");
			onChoice = (IMainActivity) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnChoiceMade");
		}
	}

	private ImageView image1;
	private ImageView image2;
 
	private void loadImages() {
		Log.d(getString(NAME), "loadImages()");
		image1 = (ImageView) view.findViewById(R.id.pag1ImageView);
		image2 = (ImageView) view.findViewById(R.id.pag1ImageViewBirds);
		
		Log.d("dalvikvm-heap robot:", "loadImages");
		
		bitmap1 = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.robot, 600 * density, 300 * density);
		image1.setImageBitmap(bitmap1);

		image2.setVisibility(View.GONE);
		Log.d("dalvikvm-heap robot:", "robot");
	}

	private void loadText() {
		Log.d(getString(NAME), "loadText()");

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagRobotInFront);

		tv3 = (TextView) view.findViewById(R.id.textPag1_2);
		tv3.setVisibility(View.GONE);

		dialogBox = (DialogBox) view.findViewById(R.id.dialog);
		dialogBox.setImg1Id(getResources().getDrawable(R.anim.gui_anim));
		dialogBox.setImg2Id(null);

		dialogBox.setTextDialog(getString(R.string.pagRobotImGui));

		RelativeLayout.LayoutParams rl = (LayoutParams) dialogBox
				.getLayoutParams();
		rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		rl.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		rl.setMargins((int) Math.ceil(16 * density),
				(int) Math.ceil(16 * density), (int) Math.ceil(16 * density),
				(int) Math.ceil(16 * density));
		// rl.removeRule(RelativeLayout.ALIGN_LEFT);
		// rl.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		Utils.removeRule(rl, RelativeLayout.ALIGN_LEFT);
		Utils.removeRule(rl, RelativeLayout.ALIGN_PARENT_BOTTOM);

	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long startTime = System.currentTimeMillis();

			Log.d("dalvikvm-heap robot:", "mUpdateTimeTask");
			if (isAdded()) {
				Log.d("dalvikvm-heap robot:", "robot_click_scal");
				// if the talisman was cleaned after portal page
				if (BookActivity.bitmapTalisma == null) {

					BookActivity.bitmapTalisma = Utils
							.decodeSampledBitmapFromResource(getResources(),
									R.drawable.talisma, 128 *(int) Math.ceil(density), 128 * (int)Math.ceil(density));
				}
			}
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			Log.d(NAME + " Total time", "mUpdateTimeTask time =" + totalTime);
		}
	};

	ImageButton button = null;

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

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);

		BookActivity.playMusic(R.raw.robot_page);
		
		// loadImages()
		loadImages();
		loadText();

		tv1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				float density = (float) getResources().getDisplayMetrics().density;
				int height1 = 0;
				int width3 = 0;
				int multiplier = 7;
				if (isTextHide) {
					height1 = tv1.getHeight() * multiplier
							- ((int) Math.ceil(8 * density));
					width3 = tv3.getWidth() * multiplier
							- ((int) Math.ceil(8 * density));
					isTextHide = false;
				} else {
					height1 = tv1.getHeight() / multiplier
							+ ((int) Math.ceil(8 * density));
					width3 = tv3.getWidth() / multiplier
							+ ((int) Math.ceil(8 * density));
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

				PagRobotAttack fb = new PagRobotAttack();

				onChoice.onChoiceMade(fb, PagRobotAttack.NAME,
						PagRobotAttack.icon);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagPathChoiceFrg fb = new PagPathChoiceFrg();

				onChoice.onChoiceMade(fb, PagPathChoiceFrg.NAME,
						PagPathChoiceFrg.icon);
				onChoice.onChoiceMadeCommit(NAME, false);
			}
		});

		// run the start() method later on the UI thread
		view.postDelayed(mUpdateTimeTask, 1000);

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pagrobotInFront), bitmap1));
		
		return view;
	}

	@Override
	public void onDetach() {
		Log.d(getString(NAME), " onDetach()");
		super.onDetach();

		image1.setImageBitmap(null);
		
	}

	AnimationDrawable backGroundChangeAnim;

	@Override
	public String getPrevPage() {
		return PagAfterLake.class.getName();
	}

	@Override
	public String getNextPage() {
		// TODO Auto-generated method stub
		return null;
	}

}
