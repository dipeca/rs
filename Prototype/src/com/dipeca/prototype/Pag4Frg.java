package com.dipeca.prototype;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Pag4Frg extends Fragment {

	private IMainActivity onChoice;
	public static String NAME = "Pag 4";

	MathMentalPyramidFrg math = null;
	View view = null;
	ImageView iv1;
 
	private void loadImages() { 
		Log.d(NAME, "loadImages()");  

		iv1 = (ImageView) view.findViewById(R.id.imageView1);

		Bitmap mountain = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.cadeado, iv1.getLayoutParams().width,
				iv1.getLayoutParams().height);
		iv1.setImageBitmap(mountain);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_pag4, container, false);

		if (math == null) {
			math = new MathMentalPyramidFrg();
			FragmentTransaction transaction = getChildFragmentManager()
					.beginTransaction();
			transaction.add(R.id.mathTr, math).commit();
		} else {
			math.generatePyramid();
		}

		loadImages();

		return view;
	}
}
