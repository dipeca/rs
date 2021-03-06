package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.Fragment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagFindFriend extends Fragment implements OnTouchListener,
		IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.foundFriend;
	public static int icon = R.drawable.companheira_presa_icon;
	private TextView tv1 = null;
	private boolean isTextHide = false;

	private Bitmap bitmap1;
	private Bitmap bitmap2;

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
		Log.d("tag:dalvikvm- " + getString(NAME), "loadImages()");
		iv1 = (ImageView) view.findViewById(R.id.page3Image);
		iv2 = (ImageView) view.findViewById(R.id.page3ImageClick);

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);

		iv1.setImageDrawable(getResources().getDrawable(R.anim.friend_cry));
		((AnimationDrawable) iv1.getDrawable()).start();
		Log.d("tag:dalvikvm- " + getString(NAME), "friend_cry");
		// iv2.setVisibility(View.VISIBLE);

		bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.encontrar_companheira_cli, 50, 25);
		iv2.setImageBitmap(bitmap2);

		Log.d("tag:dalvikvm- " + getString(NAME), "encontrar_companheira_cli");
		// set current mapImage
		onChoice.setCurrentMapPosition(R.drawable.mapa_friend);
		// Add buttonNext to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);
	}

	ImageButton button = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_one_image_clickable, container,
				false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d(getString(NAME), "onCreateView after inflate time =" + totalTime);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagFoundFriend);

		loadImages();

		view.setOnTouchListener(this);

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent( 
				getString(R.string.social_action_desc),
				getString(R.string.pagSomethingMoving), onChoice
						.decodeSampledBitmapFromResourceBG(getResources(),
								R.drawable.companheira_presa_lock_bg, 400 * density, 200 * density)));
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

			int touchColor = Utils.getHotspotColor(ev, iv2);

			int tolerance = 25;
			if (Utils.closeMatch(Color.RED, touchColor, tolerance)) {
				// Do the action associated with the RED region
				PagFindFriendZoom fb = new PagFindFriendZoom();
				onChoice.onChoiceMade(fb, getString(PagFindFriendZoom.NAME),
						getResources().getResourceName(PagFindFriendZoom.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);
			} else {
				Log.d("PagFindFriend", "Resto da imagem clicada");
			}
			break;
		}

		return true;
	}

	@Override
	public String getPrevPage() {
		return PagSomethingMoving.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}
}
