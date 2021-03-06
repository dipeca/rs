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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dipeca.item.DialogBox;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.ObjectItem;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagAfterLake extends Fragment implements IFragmentBook {
	private IMainActivity onChoice;
	public static int NAME = R.string.afterLakeTitle;
	public static int icon = R.drawable.caminho_dia_fim_icon;

	private Bitmap caminhoSomebodyBm;
	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;

	private TextView tv1 = null;
	private TextView tv2 = null;
	private DialogBox dialogBox;

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

		view = inflater.inflate(R.layout.pag_two_images, container, false);

		final ImageButton buttonNext = (ImageButton) view
				.findViewById(R.id.goToNextPage);
		iv1 = (ImageView) view.findViewById(R.id.page2Image);
		iv2 = (ImageView) view.findViewById(R.id.page2Image2);
		iv3 = (ImageView) view.findViewById(R.id.page2Image3);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.afterLake);

		tv2 = (TextView) view.findViewById(R.id.textPag2);
		tv2.setVisibility(View.GONE);

		dialogBox = (DialogBox) view.findViewById(R.id.dialog);
		dialogBox.setVisibility(View.GONE);

		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Check if we already have traveled to the scarecrow
				ObjectItem oi = new ObjectItem();
				oi.setObjectImageType(ObjectItem.TYPE_BOTTLE);

				if (onChoice.isInObjects(oi)) {
					PagRobot fb = new PagRobot();

					onChoice.onChoiceMade(fb, PagRobot.NAME, PagRobot.icon);
				} else {
					PagPathChoiceFrg fb = new PagPathChoiceFrg();

					onChoice.onChoiceMade(fb, PagPathChoiceFrg.NAME,
							PagPathChoiceFrg.icon);
				}
				onChoice.onChoiceMadeCommit(NAME, true);

			}
		});

		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);

		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagAfterChestOpen fb = new PagAfterChestOpen();

				onChoice.onChoiceMade(fb, PagAfterChestOpen.NAME,
						PagAfterChestOpen.icon);
				onChoice.onChoiceMadeCommit(NAME, false);
			}
		});

		loadImages();

		// BookActivity.playMusic(R.raw.midnight_walk);
		return view;
	}

	private void loadImages() {
		Log.d(getString(NAME), "loadImages()");

		iv3.setVisibility(View.INVISIBLE);

		iv1.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		
		int density = (int) Math.ceil(getResources().getDisplayMetrics().density);
		caminhoSomebodyBm = onChoice.decodeSampledBitmapFromResourceBG(
				getResources(), R.drawable.caminho_dia, 400 * density, 200 * density);
		iv1.setImageBitmap(caminhoSomebodyBm);

		iv2.setVisibility(View.GONE);

		// Add buttonNext to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);
	}

	AnimationDrawable backGroundChangeAnim;

	@Override
	public String getPrevPage() {
		return PagAfterChestOpen.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}

	@Override
	public void onDetach() {
		Log.d(getString(NAME), " onDetach()");
		super.onDetach();

//		iv1.setImageBitmap(null);
//
//		if (caminhoSomebodyBm != null) {
//			caminhoSomebodyBm.recycle();
//			caminhoSomebodyBm = null;
//		}
	}

}
