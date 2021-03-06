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
import com.dipeca.item.ObjectItem;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagVillageAfterHelpFrg extends Fragment implements IFragmentBook {
	private IMainActivity onChoice;
	public static int icon = R.drawable.desafio_icon;
	public static int NAME = R.string.challengeToOvercome;

	private TextView tv1 = null;
	private TextView tv2 = null;
	private DialogBox dialogB = null;

	private Bitmap villageBitmap;
	private static boolean isMapAcknowledged = false;
	AnimationDrawable backGroundChangeAnimJake;
	AnimationDrawable backGroundChangeAnimGui;

	ObjectItem oiMap = null;
	
	private ImageView iv1;

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
		Log.d("Total time pag2", "onCreateView after inflate time ="
				+ totalTime);

		loadText();

		final ImageButton button = (ImageButton) view
				.findViewById(R.id.goToNextPage);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagEnigmaFrg fb = new PagEnigmaFrg();

				onChoice.onChoiceMade(fb, getString(PagEnigmaFrg.NAME),
						getResources().getResourceName(PagEnigmaFrg.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);
			}
		});
		
		final ImageButton buttonPrev = (ImageButton) view 
				.findViewById(R.id.goToPrevPage);
		
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PageHelp fb = new PageHelp();

				onChoice.onChoiceMade(fb, getString(PageHelp.NAME), getResources().getResourceName(PageHelp.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);
			}
		});

		loadImages();

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pag2_1), villageBitmap));
		
		oiMap = new ObjectItem(null,getString(R.string.mapa), ObjectItem.TYPE_MAP, null);
		isMapAcknowledged = onChoice.isInObjects(oiMap);
		//If we still haven't create the map object we create it now
		if (!isMapAcknowledged) {
			onChoice.objectFoundPersist(oiMap);
		}	
		
		return view;
	}

	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagVillageAfterHelp);

		dialogB = (DialogBox) view.findViewById(R.id.dialog);
		dialogB.setTextDialog(getString(R.string.challengeToProveYouSelf));
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				dialogB.getLayoutParams().width,
				dialogB.getLayoutParams().height);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params1.addRule(RelativeLayout.ALIGN_TOP, R.id.textPag1);
		dialogB.setLayoutParams(params1);
 
		tv2 = (TextView) view.findViewById(R.id.textPag1_2);
		tv2.setVisibility(View.GONE); 
		
		
		dialogB.setImg1Id(getResources().getDrawable(R.anim.anciao_anim));
		dialogB.setImg2Id(getResources().getDrawable(R.anim.gui_anim));
	}

	private void loadImages() {

		long startTime = System.currentTimeMillis();

		iv1 = (ImageView) view.findViewById(R.id.pag1ImageView);

		int density = (int) Math.ceil(getResources().getDisplayMetrics().density);

		Log.d(getString(NAME), "density: " + density);
		villageBitmap = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.village, 600 * density, 300 * density);
		iv1.setImageBitmap(villageBitmap);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(" Village loadImages Total time: " + totalTime);
	}

	@Override
	public String getPrevPage() {
		return PagKingDomfrg.class.getName();
	}

	@Override
	public String getNextPage() {
		// TODO Auto-generated method stub
		return null;
	}

}
