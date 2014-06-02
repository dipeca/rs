package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Pag4Frg extends Fragment {

	private IMainActivity onChoice;
	public static String NAME = "Pag 4";
	private static ImageButton button;

	MathMentalPyramidFrg math = null;
	View view = null;
	ImageView iv1;

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

	private void loadImages() {
		Log.d(NAME, "loadImages()");

		iv1 = (ImageView) view.findViewById(R.id.imageView1);

		Bitmap mountain = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.cadeado, 600,
				300);
		iv1.setImageBitmap(mountain);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_pag4, container, false);

		if (math == null) {
			math = new MathMentalPyramidFrg();
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.add(R.id.mathTr, math).commit();
		} else {
			math.generatePyramid();
		}

		loadImages();

		button = (ImageButton) view.findViewById(R.id.goToNextPage);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) { 
				if (math.isLockUnlocked()) {
					onChoice.onChoiceMadeCommit("Cadeado", true);
				}

			}
		});

		return view;
	}
}
