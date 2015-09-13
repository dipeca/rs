package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
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

public class PageScareCrowField extends Fragment implements IFragmentBook {
	private IMainActivity onChoice;
	public static int icon = R.drawable.scarecrow_field_icon;
	public static int NAME = R.string.scarecrowField;

	private TextView tv1 = null;
	private TextView tv2 = null;
	private DialogBox dialogB = null;

	private Bitmap scareCrowFieldBm;

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

				PagScareCrow fb = new PagScareCrow();
				onChoice.onChoiceMade(fb, getString(PagScareCrow.NAME),
						getResources().getResourceName(PagScareCrow.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);
			}
		});

		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagPathChoiceFrg fb = new PagPathChoiceFrg();
				onChoice.onChoiceMade(fb, getString(PagPathChoiceFrg.NAME),
						getResources().getResourceName(PagPathChoiceFrg.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);
			}
		});

		loadImages();

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pag2_1), scareCrowFieldBm));

		// Check if we already have traveled to the lake
		ObjectItem oiSC = new ObjectItem();
		oiSC.setObjectImageType(ObjectItem.TYPE_BOTTLE);

		ObjectItem oiLake = new ObjectItem();
		oiLake.setObjectImageType(ObjectItem.TYPE_KEY);

		if (onChoice.isInObjects(oiSC) && onChoice.isInObjects(oiLake)) {
			// set current mapImage
			onChoice.setCurrentMapPosition(R.drawable.mapa_ambos);
		} else if (onChoice.isInObjects(oiSC)) {
			// set current mapImage
			onChoice.setCurrentMapPosition(R.drawable.mapa_espantalho);
		} else if (onChoice.isInObjects(oiLake)) {
			// set current mapImage
			onChoice.setCurrentMapPosition(R.drawable.mapa_lago);
		} else {
			// set current mapImage
			onChoice.setCurrentMapPosition(R.drawable.mapa_escolha);
		}
		// Add buttonNext to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);

		return view;

	}

	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(getString(R.string.ScareCrowField1) +" "
				+ getString(R.string.ScareCrowField2));

		dialogB = (DialogBox) view.findViewById(R.id.dialog);
		dialogB.setVisibility(View.GONE);

		tv2 = (TextView) view.findViewById(R.id.textPag1_2);
		tv2.setVisibility(View.GONE);

	}

	private void loadImages() {

		long startTime = System.currentTimeMillis();

		iv1 = (ImageView) view.findViewById(R.id.pag1ImageView);

		int density = (int) Math.ceil(getResources().getDisplayMetrics().density);
 
		Log.d(getString(NAME), "density: " + density);
		scareCrowFieldBm = onChoice.decodeSampledBitmapFromResourceBG(
				getResources(), R.drawable.scarecrow_field, 540 * density, 300 * density);
		Log.d("dalvikvm- " +getString(NAME), "scarecrow_field ");
		
		iv1.setImageBitmap(scareCrowFieldBm);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(" Village loadImages Total time: " + totalTime);
	}

	@Override
	public String getPrevPage() {
		return PagPathChoiceFrg.class.getName();
	}

	@Override
	public String getNextPage() {
		// TODO Auto-generated method stub
		return null;
	}

}
