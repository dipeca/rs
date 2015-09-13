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

import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagAfterChestOpen extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.returnFromLake; 
	public static int icon = R.drawable.lake_after_icon;
	private TextView tv1 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;

	private Bitmap bitmap1;

	private ImageButton buttonNext = null;
	private ImageButton buttonPrev = null;

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

	private ImageView mImageView;

	private void loadImages() {
		Log.d("KingDom ", "loadImages()");
		mImageView = (ImageView) view.findViewById(R.id.pag1ImageView);
		
		int density = (int) Math.ceil(getResources().getDisplayMetrics().density);
		bitmap1 = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.lake_after, 600 * density, 300 * density);
		mImageView.setImageBitmap(bitmap1);

		// Add buttonNext to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.pag_one_image, container, false);

		loadText();
		loadImages();

		buttonNext = (ImageButton) view.findViewById(R.id.goToNextPage);
		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (onChoice.isInJourney(getString(PagDungeonPostChallenge.NAME))) {
					PagRobot fb = new PagRobot();

					onChoice.onChoiceMade(fb, getString(PagRobot.NAME),
							getResources().getResourceName(PagRobot.icon));
					onChoice.onChoiceMadeCommit(getString(NAME), true);
				}else{
					PagPathChoiceFrg fb = new PagPathChoiceFrg();
 
					onChoice.onChoiceMade(fb, getString(PagPathChoiceFrg.NAME),
							getResources().getResourceName(PagPathChoiceFrg.icon));
					onChoice.onChoiceMadeCommit(getString(NAME), false);					
				}
			}
		});

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagChestOpen fb = new PagChestOpen();
				onChoice.onChoiceMade(fb, PagChestOpen.NAME, PagChestOpen.icon);

				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		return view;
	}

	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagAfterChestOpen);

		tv3 = (TextView) view.findViewById(R.id.textPag1_2);
		tv3.setVisibility(View.GONE);
	}

	@Override
	public void onDetach() {
		Log.d("PagLateToCross ", "PagLateToCross  onDetach()");
		super.onDetach();
		
		mImageView.setImageBitmap(null);

	}

	@Override
	public String getPrevPage() {
		return PagChestOpen.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}

}
