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
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dipeca.item.DialogBox;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.ObjectItem;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagCredits extends Fragment implements IFragmentBook {
	private IMainActivity onChoice;
	public static int icon = R.drawable.desafio_icon;
	public static int NAME = R.string.challengeToOvercome;

	private TextView tv1 = null;
	private TextView tv2 = null;
	private int density;
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
		density = (int) Math.ceil(getResources().getDisplayMetrics().density);
		view = inflater
				.inflate(R.layout.pag_one_image_dialog, container, false);

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time pag2", "onCreateView after inflate time ="
				+ totalTime);

		loadText();

		final ImageButton btnNext = (ImageButton) view
				.findViewById(R.id.goToNextPage);
		btnNext.setVisibility(View.GONE);

		final ImageButton buttonPrev = (ImageButton) view
				.findViewById(R.id.goToPrevPage);

		buttonPrev.setVisibility(View.GONE);

		loadImages();

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pag2_1), villageBitmap));

		return view;
	}

	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setVisibility(View.GONE);
		tv2 = (TextView) view.findViewById(R.id.textPag1_2);
		tv2.setVisibility(View.GONE);

		TextView tv3 = new TextView(getActivity());
		tv3.setTextAppearance(getActivity(), R.style.credits);
		tv3.setText(R.string.creditsSong);
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				640 * density, RelativeLayout.LayoutParams.WRAP_CONTENT);

		rl.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		((ViewGroup) view.getRootView()).addView(tv3, rl);

		DialogBox dialog = (DialogBox) view.findViewById(R.id.dialog);
		dialog.setVisibility(View.GONE);
	}

	private void loadImages() {

		iv1 = (ImageView) view.findViewById(R.id.pag1ImageView);

		int density = (int) Math
				.ceil(getResources().getDisplayMetrics().density);

		iv1.setBackgroundResource(R.drawable.bg_black_with_border);
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
