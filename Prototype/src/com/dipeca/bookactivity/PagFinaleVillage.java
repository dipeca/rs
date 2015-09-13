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

public class PagFinaleVillage extends Fragment implements IFragmentBook {
	private IMainActivity onChoice;
	public static int icon = R.drawable.desafio_icon;
	public static int NAME = R.string.challengeToOvercome;

	private TextView tv1 = null;
	private TextView tv2 = null;

	private Bitmap villageBitmap;

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

				PagCredits fb = new PagCredits();

				onChoice.onChoiceMade(fb, getString(PagCredits.NAME),
						getResources().getResourceName(PagCredits.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), false);
			}
		});

		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagBossCapture fb = new PagBossCapture();

				onChoice.onChoiceMade(fb, getString(PagBossCapture.NAME),
						getResources().getResourceName(PagBossCapture.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);
			}
		});

		loadImages();

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pag2_1), villageBitmap));


		return view;
	}

	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagVillageFinale);

		tv2 = (TextView) view.findViewById(R.id.textPag1_2);
		tv2.setVisibility(View.GONE);
		
		DialogBox dialog = (DialogBox) view.findViewById(R.id.dialog);
		dialog.setVisibility(View.GONE);
	}

	private void loadImages() {

		iv1 = (ImageView) view.findViewById(R.id.pag1ImageView);

		int density = (int) Math
				.ceil(getResources().getDisplayMetrics().density);

		Log.d(getString(NAME), "density: " + density);
		villageBitmap = onChoice.decodeSampledBitmapFromResourceBG(
				getResources(), R.drawable.village_finale, 400 * density,
				200 * density);
		iv1.setImageBitmap(villageBitmap);
	}

	@Override
	public String getPrevPage() {
		return PagBossCapture.class.getName();
	}

	@Override
	public String getNextPage() {
		// TODO Auto-generated method stub
		return null;
	}

}
