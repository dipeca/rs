package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class PagPathChoiceFrg extends Fragment implements OnTouchListener {

	private IMainActivity onChoice;
	public static String NAME = "Escolha de caminho";
	View view = null;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.pag_one_image_clickable, container, false);

		BookActivity.stopMusic();
		loadImages();

		view.setOnTouchListener(this);

		return view;
	}

	private void transitionFragment(boolean isShadowPath) {
		// record the choice on main activity
		String iconNextPage;
		if (isShadowPath) {
			iconNextPage = "companheira_presa_icon";
			PagFindFriend frg = new PagFindFriend();
			onChoice.onChoiceMade(frg, PagFindFriend.NAME, iconNextPage);
		} else {
			iconNextPage = "robot_icon";
			PagRobot frg = new PagRobot();
			onChoice.onChoiceMade(frg, PagRobot.NAME, iconNextPage);
		}

		onChoice.onChoiceMadeCommit(NAME, true);

	}

	private ImageView iv1;
	private ImageView iv2;
	Bitmap bitmap1;
	Bitmap bitmap2;
	private int density;
	private void loadImages() {

		iv1 = (ImageView) view.findViewById(R.id.page3Image);
		iv2 = (ImageView) view.findViewById(R.id.page3ImageClick);

		density = (int) getResources().getDisplayMetrics().density;

		bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.choice, 600, 300 );
		iv1.setImageBitmap(bitmap1);

		bitmap2 = Utils.decodeSampledBitmapFromResource(
				getResources(), R.drawable.choice_cli, 50,
				25);
		iv2.setImageBitmap(bitmap2);

	}

	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		final int action = ev.getAction();
		// (1)
		final int evX = (int) ev.getX();
		final int evY = (int) ev.getY();
		switch (action) {

		case MotionEvent.ACTION_UP:

			int touchColor = Utils.getHotspotColor(R.id.page3ImageClick, evX,
					evY, view);

			int tolerance = 25;
			if (Utils.closeMatch(Color.RED, touchColor, tolerance)) {
				// Do the action associated with the RED region 
				Toast.makeText(getActivity(), getString(R.string.ravenPath),
						Toast.LENGTH_SHORT).show();

				transitionFragment(true);
			} else if (Utils.closeMatch(Color.WHITE, touchColor, tolerance)) {
				// Do the action associated with the white region
				Toast.makeText(getActivity(), getString(R.string.forestPath),
						Toast.LENGTH_SHORT).show();
 
				transitionFragment(false);
			} 
			break;
		}

		return true;
	}

}
