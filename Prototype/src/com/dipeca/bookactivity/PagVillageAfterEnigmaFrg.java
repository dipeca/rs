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
import android.widget.TextView;

import com.dipeca.item.DialogBox;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagVillageAfterEnigmaFrg extends Fragment implements IFragmentBook {
	private IMainActivity onChoice;
	public static int NAME = R.string.enigmaSolved;
	public static int icon = R.drawable.desafio_superado_icon;

	private TextView tv1 = null;
	private TextView tv2 = null;
	private DialogBox dialogB = null;

	AnimationDrawable backGroundChangeAnimJake;
	AnimationDrawable backGroundChangeAnimGui;

	private ImageView iv1;
	private Bitmap bitmapVillage;
	
	@Override
	public void onDetach() {
		Log.d("Kingdom ", "Kingdom  onDetach()");
		super.onDetach();

		if(bitmapVillage != null){
			bitmapVillage.recycle();
			bitmapVillage = null;
		}

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

	View view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater
				.inflate(R.layout.pag_one_image_dialog, container, false);

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d(getString(NAME), "onCreateView after inflate time ="
				+ totalTime);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagVillageChallengeDone);

		dialogB = (DialogBox) view.findViewById(R.id.dialog);
		dialogB.setTextDialog(getString(R.string.pagVillageChallengeDoneDialog));
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				dialogB.getLayoutParams().width, 
				dialogB.getLayoutParams().height);
		params1.addRule(RelativeLayout.ALIGN_RIGHT,R.id.goToNextPage );
		params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params1.addRule(RelativeLayout.ALIGN_TOP, R.id.textPag1);

		dialogB.setImg1Id(getResources().getDrawable(R.anim.anciao_anim));
		dialogB.setImg2Id(getResources().getDrawable(R.anim.gui_anim));
		dialogB.setLayoutParams(params1);

		tv2 = (TextView) view.findViewById(R.id.textPag1_2);
		tv2.setVisibility(View.GONE);

		final ImageButton button = (ImageButton) view
				.findViewById(R.id.goToNextPage);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagSomethingMoving fb = new PagSomethingMoving();

				onChoice.onChoiceMade(fb, getString(PagSomethingMoving.NAME), getResources().getResourceName(PagSomethingMoving.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);
			}
		});

		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);
		
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagEnigmaFrg fb = new PagEnigmaFrg();

				onChoice.onChoiceMade(fb, getString(PagEnigmaFrg.NAME), getResources().getResourceName(PagEnigmaFrg.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});
		
		loadImages();
		
		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pagVillageChallengeDone), bitmapVillage));
		
		return view;
	} 
  
	private void loadImages() {

		iv1 = (ImageView) view.findViewById(R.id.pag1ImageView);

		int density = (int) getResources().getDisplayMetrics().density;

		bitmapVillage = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.villageafterchall, 540 * density, 270 * density);
		iv1.setImageBitmap(bitmapVillage);
	
		// set current mapImage
		onChoice.setCurrentMapPosition(R.drawable.mapa_primeiro);
		// Add buttonNext to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);
		
	}

	@Override
	public String getPrevPage() {
		return PagEnigmaFrg.class.getName();
	}

	@Override
	public String getNextPage() {
		return PagSomethingMoving.class.getName();
	} 

}
