package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.Fragment;
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

public class PagRobotDestroyedfrg extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.robotDestroyed;
	public static int icon = R.drawable.robot_destroyed_icon;
	
	private TextView tv1 = null;
	private TextView tv3 = null;
	private DialogBox dialog = null;
	private boolean isTextHide = false;

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

	private ImageView ivBg;
  
	private void loadImages() {
		Log.d(getString(NAME), "loadImages()");
		ivBg = (ImageView) view.findViewById(R.id.pag1ImageView);

		ivBg.setImageResource(R.anim.robot_destroyed); 
		AnimationDrawable animation = (AnimationDrawable) ivBg.getDrawable();
		animation.setOneShot(true);
		animation.start();
		
		Log.d("dalvikvm- PagRobotDestr", "R.anim.robot_destroyed");
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
		//rl.removeRule(RelativeLayout.ALIGN_LEFT);
		//rl.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		Utils.removeRule(rl, RelativeLayout.ALIGN_LEFT);
		Utils.removeRule(rl, RelativeLayout.ALIGN_PARENT_BOTTOM);
	}
	
	
	ImageButton btnNext = null;

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
		BookActivity.playMusicOnce(R.raw.explosion);
		 
		// loadImages()   
		loadImages();  
		loadText();
		
		tv1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
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

		btnNext = (ImageButton) view.findViewById(R.id.goToNextPage);
		btnNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Do the action associated with the RED region
				PagRobotDestroyedEnigmafrg frg = new PagRobotDestroyedEnigmafrg();

				onChoice.onChoiceMade(frg,PagRobotDestroyedEnigmafrg.NAME, PagRobotDestroyedEnigmafrg.icon);
				onChoice.onChoiceMadeCommit(getString( NAME), true);
			}
		});
		
		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);
		
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagRobot fb = new PagRobot();

				onChoice.onChoiceMade(fb, getString(PagRobot.NAME), null);
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		return view;
	}

	@Override
	public void onDetach() {
		Log.d("Kingdom ", "Kingdom  onDetach()");
		super.onDetach();

		ivBg.setImageBitmap(null);
	}

	AnimationDrawable backGroundChangeAnim;

	@Override
	public String getPrevPage() {
		return PagRobotAttack.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}

}
