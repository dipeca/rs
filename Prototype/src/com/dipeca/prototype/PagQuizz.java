package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * @author dipeca
 * 
 */
public class PagQuizz extends Fragment {
	View view = null;
	private IMainActivity onChoice;
	public static String NAME = "Quizz";
	ImageView amuleto = null;

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
	}


	ImageButton button = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_quizz, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time " + NAME, "onCreateView after inflate time ="
				+ totalTime);

		// loadImages()
		loadImages();

		button = (ImageButton) view.findViewById(R.id.goToNextPage);
		// button.setVisibility(View.INVISIBLE);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				onChoice.handleQuizzSubmit();
				onChoice.restartLoaderQuizz();
				
				PagTheEnd fb = new PagTheEnd();

				onChoice.onChoiceMade(fb, PagTheEnd.NAME, null);
				onChoice.onChoiceMadeCommit(NAME, true);
				
			}
		});

		return view;
	}
	
	@Override
	public void onDetach() {
		Log.d(NAME, "onDetach()");
		super.onDetach();

	}

}
