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
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dipeca.item.DialogBox;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagAfterChallenge extends Fragment implements IFragmentBook {
	private IMainActivity onChoice;
	public static int NAME = R.string.adventureBegins;
	public static int icon = R.drawable.caminho_somebody_icon;

	AnimationDrawable backGroundChangeAnimJake;
	AnimationDrawable backGroundChangeAnimGui;

	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;
	private ImageView ivGuiWalking;
	private ImageView ivCatWalking;

	private AnimationDrawable backGroundWalkingGuiAnim;
	private AnimationDrawable backGroundWalkingCatAnim;

	private TextView tv1 = null;
	private TextView tv2 = null;
	private DialogBox dialogBox;
	
	private Bitmap caminhoSomebodyBm = null;
	private Bitmap caminhoBm = null;
	private int density = 1;
	private ImageButton btnNext;

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

	@Override
	public void onStart() {
		super.onStart();

		// Set animation
		ivGuiWalking.setBackgroundResource(R.anim.gui_walk);
		ivCatWalking.setBackgroundResource(R.anim.gata_walk);

		// get animation to variable
		backGroundWalkingGuiAnim = (AnimationDrawable) ivGuiWalking
				.getBackground();
		backGroundWalkingCatAnim = (AnimationDrawable) ivCatWalking
				.getBackground();

		// Build moving animation
		Animation animationGui = new TranslateAnimation(0 - 100 * density,
				((getResources().getDisplayMetrics().widthPixels)) * density,
				-8 * density, -8 * density);
		animationGui.setDuration(10000);

		Animation animationCat = new TranslateAnimation(0 - 20 * density,
				((getResources().getDisplayMetrics().widthPixels)) * density,
				0, 0);
		animationCat.setDuration(9000);

		// SEt moving animation
		ivGuiWalking.setAnimation(animationGui);
		ivCatWalking.setAnimation(animationCat);

		// Start walking animation
		backGroundWalkingGuiAnim.start();
		backGroundWalkingCatAnim.start();

		// Start moving animation
		ivGuiWalking.getAnimation().start();
		ivCatWalking.getAnimation().start();

		ivCatWalking.getAnimation().setAnimationListener(
				new Animation.AnimationListener() {

					@Override
					public void onAnimationStart(Animation arg0) {
						Log.d(NAME + "cat animation", "start");

					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						Log.d(NAME + "cat animation", "start");

					}

					@Override
					public void onAnimationEnd(Animation arg0) {
						Log.d(NAME + "cat animation", "end");
						backGroundWalkingCatAnim.stop();
						
						ivCatWalking.setVisibility(View.GONE);
						ivCatWalking.setBackgroundResource(0);
					}
				});

		ivGuiWalking.getAnimation().setAnimationListener(
				new Animation.AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						Log.d(NAME + "gui animation", "start");
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						Log.d(NAME + "gui animation", "repeat");

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Log.d(NAME + "gui animation", "end");
						backGroundWalkingGuiAnim.stop();
						ivGuiWalking.setVisibility(View.GONE);
						ivGuiWalking.setBackgroundResource(0);
					}
				});

	}

	View view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.pag_two_images, container, false);

		btnNext = (ImageButton) view
				.findViewById(R.id.goToNextPage);
		iv1 = (ImageView) view.findViewById(R.id.page2Image);
		iv2 = (ImageView) view.findViewById(R.id.page2Image2);
		iv3 = (ImageView) view.findViewById(R.id.page2Image3);
		ivGuiWalking = (ImageView) view.findViewById(R.id.ivWalk);
		ivCatWalking = (ImageView) view.findViewById(R.id.ivCatWalk);



		btnNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagPathChoiceFrg fb = new PagPathChoiceFrg();

				onChoice.onChoiceMade(fb, PagPathChoiceFrg.NAME,
						PagPathChoiceFrg.icon);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagFindFriendZoom fb = new PagFindFriendZoom();

				onChoice.onChoiceMade(fb, PagFindFriendZoom.NAME,
						PagFindFriendZoom.icon);
				onChoice.onChoiceMadeCommit(NAME, false);
			}
		});

		loadText();
		loadImages();

		return view;
	}

	
	private void loadText(){
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setVisibility(View.GONE);

		tv2 = (TextView) view.findViewById(R.id.textPag2);
		tv2.setText(R.string.on_the_road);

		dialogBox = (DialogBox) view.findViewById(R.id.dialog);
		dialogBox.setTextDialog(getString(R.string.on_the_roadThought));
		dialogBox.setImg1Id(null);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				dialogBox.getLayoutParams().width,
				dialogBox.getLayoutParams().height);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params1.setMargins(0, 0,16 * density,0);
		params1.addRule(RelativeLayout.ALIGN_TOP, R.id.textPag1);
		dialogBox.setLayoutParams(params1);
		dialogBox.setImg2Id(getResources().getDrawable(R.anim.gui_anim));
		
	} 
	
	private void loadImages() {
		Log.d(getString(NAME), "loadImages()");

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);

		iv3.setVisibility(View.INVISIBLE);
   
		iv1.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		caminhoSomebodyBm = onChoice.decodeSampledBitmapFromResourceBG(
				getResources(), R.drawable.caminho_dia, 400 * density, 200 * density);
		iv1.setImageBitmap(caminhoSomebodyBm);

		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rlp.addRule(RelativeLayout.BELOW, iv1.getId());
		rlp.setMargins(0, (int) Math.ceil(16 * density), 0, 0);
		iv2.setLayoutParams(rlp);
		iv2.setVisibility(View.GONE);

		// set current mapImage
		onChoice.setCurrentMapPosition(R.drawable.mapa_friend);
		// Add buttonNext to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);

	}

	@Override
	public void onDetach() {
		Log.d("PagAfterChallenge ", "  onDetach()");
		super.onDetach();

//		iv1.setImageBitmap(null);
//		iv2.setImageBitmap(null);
//
//		if (caminhoSomebodyBm != null) {
//			caminhoSomebodyBm.recycle();
//			caminhoSomebodyBm = null;
//		}

		ivGuiWalking = null;
		ivCatWalking = null;
		if (caminhoBm != null) {
			caminhoBm.recycle();
			caminhoBm = null;
		}

	}

	@Override
	public String getPrevPage() {
		return PagFindFriend.class.getName();
	}

	@Override
	public String getNextPage() {
		// TODO Auto-generated method stub
		return null;
	}
}
