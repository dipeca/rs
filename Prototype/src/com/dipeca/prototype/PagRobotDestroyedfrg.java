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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class PagRobotDestroyedfrg extends Fragment {
	View view = null;
	private IMainActivity onChoice;
	public static String NAME = "Robot destroyed";
	private TextView tv1 = null;
	private TextView tv3 = null;
	private DialogBox dialog = null;
	private boolean isTextHide = false;

	private static Bitmap bitmap1;

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

	private void loadImages() {
		Log.d(NAME, "loadImages()");
		image1 = (ImageView) view.findViewById(R.id.pag1ImageView);
		image2 = (ImageView) view.findViewById(R.id.pag1ImageViewBirds);

		image1.setImageResource(R.anim.robot_destroyed); 
		AnimationDrawable animation = (AnimationDrawable) image1.getDrawable();
		animation.setOneShot(true);
		animation.start();
	}
 
	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagRobotDestroyed);
		
		tv3 = (TextView) view.findViewById(R.id.textPag1_2);
		tv3.setVisibility(View.INVISIBLE);
		

		dialog = (DialogBox) view.findViewById(R.id.dialog);
		dialog.setTextDialog(getString(R.string.pagRobotDestroyedDialog));
		dialog.setImg1Id(getResources().getDrawable(R.anim.lopo_anim));
		dialog.setImg2Id(getResources().getDrawable(R.anim.gui_anim));
		
		RelativeLayout.LayoutParams rl = (LayoutParams) dialog.getLayoutParams();
		rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		rl.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		rl.removeRule(RelativeLayout.ALIGN_LEFT);
		rl.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	}
	
	
	ImageButton button = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
  
		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_one_image_dialog, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time " + NAME, "onCreateView after inflate time ="
				+ totalTime);

		//plays the explosion sound
		BookActivity.playMusicOnce(R.raw.robot_destroyed);
		
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
					height1 = tv1.getHeight() * multiplier ;
					width3 = dialog.getWidth() * multiplier ;
					isTextHide = false;
				} else {
					height1 = tv1.getHeight() / multiplier;
					width3 = dialog.getWidth() / multiplier ;
					isTextHide = true;
				}

				RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
						tv1.getWidth(), height1);
				tv1.setLayoutParams(params1);

				ViewGroup.LayoutParams params3 = dialog.getLayoutParams();
				params3.width = width3;
				params3.height = dialog.getHeight();
				dialog.setLayoutParams(params3);
			}
		});

		button = (ImageButton) view.findViewById(R.id.goToNextPage);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagVillageFrg fb = new PagVillageFrg();

				onChoice.onChoiceMade(fb, PagVillageFrg.NAME);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		return view;
	}

	@Override
	public void onDetach() {
		Log.d("Kingdom ", "Kingdom  onDetach()");
		super.onDetach();

		if(bitmap1 != null){
			bitmap1.recycle();
			bitmap1 = null;
		}

	}

	AnimationDrawable backGroundChangeAnim;

}
