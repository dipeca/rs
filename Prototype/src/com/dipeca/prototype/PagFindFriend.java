package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PagFindFriend extends Fragment implements OnTouchListener {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.foundFriend;
	public static int icon = R.drawable.companheira_presa_icon;
	private TextView tv1 = null;
	private boolean isTextHide = false;

	private static Bitmap bitmap1;
	private static Bitmap bitmap2;

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

	private ImageView iv1;
	private ImageView iv2;

	private int density;

	private void loadImages() {
		Log.d(getString(NAME), "loadImages()");
		iv1 = (ImageView) view.findViewById(R.id.page3Image);
		iv2 = (ImageView) view.findViewById(R.id.page3ImageClick);

		density = (int) getResources().getDisplayMetrics().density;
		// bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
		// R.drawable.companheira_presa, 600, 300);
		// iv1.setImageBitmap(bitmap1);

		iv1.setImageDrawable(getResources().getDrawable(R.anim.friend_cry));
		((AnimationDrawable) iv1.getDrawable()).start();
		// iv2.setVisibility(View.VISIBLE);

		bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.encontrar_companheira_cli, 50, 25);
		iv2.setImageBitmap(bitmap2);

	}

	ImageButton button = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_one_image_clickable, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d(getString(NAME), "onCreateView after inflate time =" + totalTime);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagFoundFriend);

		BookActivity.stopMusic();
		// loadImages()
		loadImages();
		
		view.setOnTouchListener(this);

		return view;
	}

	@Override
	public void onDetach() {
		Log.d(getString(NAME), " onDetach()");
		super.onDetach();

		if (bitmap1 != null) {
			bitmap1.recycle();
			bitmap1 = null;
		}

		if (bitmap2 != null) {
			bitmap2.recycle();
			bitmap2 = null;
		}
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
//				PagForestPath frg = new PagForestPath();
//				onChoice.onChoiceMade(frg, PagForestPath.NAME, null);
				PagAfterChallenge frg = new PagAfterChallenge();

				onChoice.onChoiceMade(frg, PagAfterChallenge.NAME, PagAfterChallenge.icon);
				
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setTransition(R.animator.right_to_left);

				PagLockMaths fb = new PagLockMaths();
				fb.isVaultPage(false);
				
				ft.replace(R.id.detailFragment, fb);
				//ft.addToBackStack(getString(NAME));
				ft.commit();
			} else {
				Log.d("PagFindFriend", "Resto da imagem clicada");
			}
			break;
		}

		return true;
	}
}
