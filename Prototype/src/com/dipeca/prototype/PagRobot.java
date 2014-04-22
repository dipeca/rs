package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class PagRobot extends Fragment {
	View view = null;
	private IMainActivity onChoice;
	public static String NAME = "Robot";
	private TextView tv1 = null;
	private TextView tv3 = null;
	private DialogBox dialogBox = null;
	private boolean isTextHide = false;

	private static Bitmap bitmap1;
	private static Bitmap bitmap2;

	private int density = 1;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			Log.d(NAME, "On attach started");
			onChoice = (IMainActivity) activity;   
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() 
					+ " must implement OnChoiceMade");
		}
	}

	private ImageView image1;
	private ImageView image2;
 
	private void loadImages() {
		Log.d(NAME, "loadImages()");
		image1 = (ImageView) view.findViewById(R.id.pag1ImageView);
		image2 = (ImageView) view.findViewById(R.id.pag1ImageViewBirds);

		bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.robot, 600, 300);
		image1.setImageBitmap(bitmap1);

		image2.setVisibility(View.GONE);

	}

	private void loadText() {
		Log.d(NAME, "loadText()");
		
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagRobotInFront);

		tv3 = (TextView) view.findViewById(R.id.textPag1_2);
		tv3.setVisibility(View.GONE);

		dialogBox = (DialogBox) view.findViewById(R.id.dialog);
		dialogBox.setImg1Id(getResources().getDrawable(R.anim.gui_anim));
		dialogBox.setImg2Id(null);
		
		dialogBox.setTextDialog(getString(R.string.pagRobotImGui));
		
		RelativeLayout.LayoutParams rl = (LayoutParams) dialogBox.getLayoutParams();
		rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		rl.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		rl.setMargins(16*density, 16*density, 16*density, 16*density);
		rl.removeRule(RelativeLayout.ALIGN_LEFT);
		rl.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	}
	
	
	
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long startTime = System.currentTimeMillis();

			// This is to load the image of the next screen, the attack from the robot
			BookActivity.bitmap1 = Utils.decodeSampledBitmapFromResource(
					getResources(), R.drawable.robot3, 600, 300);
			BookActivity.bitmap2 = Utils.decodeSampledBitmapFromResource(
					getResources(), R.drawable.robot_click_scal, 50, 25);
			//if the talisman was cleaned after portal page
			if (BookActivity.bitmapTalisma == null) {

				BookActivity.bitmapTalisma = Utils.decodeSampledBitmapFromResource(
						getResources(), R.drawable.talisma, 162, 162);
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
		view = inflater.inflate(R.layout.pag_one_image_dialog, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time kingdom", "onCreateView after inflate time ="
				+ totalTime);

		BookActivity.playMusic(R.raw.robot_page);
		
		density = (int) getResources().getDisplayMetrics().density;
		
		// loadImages()
		loadImages();
		loadText();

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

				PagRobotAttack fb = new PagRobotAttack();

				onChoice.onChoiceMade(fb, PagRobotAttack.NAME);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		// run the start() method later on the UI thread
        view.postDelayed(mUpdateTimeTask, 1000);
        
		return view;
	}

	@Override
	public void onDetach() {
		Log.d("Kingdom ", "Kingdom  onDetach()");
		super.onDetach();

		bitmap1.recycle();
		bitmap1 = null;

	}

	AnimationDrawable backGroundChangeAnim;

}
