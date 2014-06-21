package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class PagLockMaths extends Fragment {

	private IMainActivity onChoice;
	public static String NAME = "Lock";
	private static ImageButton button;

	MathMentalPyramidFrg math = null;
	View view = null;
	ImageView iv1;
	private float density = 1;

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
		
		density = (float) getResources().getDisplayMetrics().density;
		
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

		final View activityRootView = view.findViewById(R.id.activityRoot);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		    @Override
		    public void onGlobalLayout() {
		    	int heightRV = activityRootView.getRootView().getHeight();
		    	int wRV = activityRootView.getRootView().getWidth();
		    	int height = activityRootView.getHeight();
		    	int w = activityRootView.getWidth();
		        int heightDiff = heightRV - height;
		        if(w/density > 600 ){ //only adjust for tablets, not for small devices
			        if (heightDiff > (100 * density)) { // if more than 100 pixels, its probably a keyboard...
				        Log.d("PagLockMaths", "keyboard visible");
				        view.findViewById(R.id.mathTr).setPadding(0, (int) (160 * density), 0, 0);
			        }
			        else{
			            view.findViewById(R.id.mathTr).setPadding(0, (int) (264 * density), 0, 0);
			        }
		        }
		     }
		});
		return view;
	}
}
