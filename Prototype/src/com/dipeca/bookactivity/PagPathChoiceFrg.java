package com.dipeca.bookactivity;

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
import android.widget.RelativeLayout;

import com.dipeca.prototype.R;

public class PagPathChoiceFrg extends Fragment implements OnTouchListener,
		IFragmentBook {

	private IMainActivity onChoice;
	public static int NAME = R.string.choicePath;
	public static int icon = R.drawable.choice_icon;
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

	private static boolean isScareCrowDone = false;
	private static boolean isLakeDone = false;

	@Deprecated
	public void setScareCrowPathDone(boolean isScareCrow) {
		this.isScareCrowDone = isScareCrow;
	}

	@Deprecated
	public void setLakePathDone(boolean isLakeDone) {
		this.isLakeDone = isLakeDone;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.pag_one_image_clickable, container,
				false);

		loadImages();

		view.setOnTouchListener(this);

		// Check if we already have traveled to the lake
		ObjectItem oiSC = new ObjectItem();
		oiSC.setObjectImageType(ObjectItem.TYPE_BOTTLE);

		ObjectItem oiLake = new ObjectItem();
		oiLake.setObjectImageType(ObjectItem.TYPE_KEY);

		if (onChoice.isInObjects(oiSC) && onChoice.isInObjects(oiLake)) {
			// set current mapImage
			onChoice.setCurrentMapPosition(R.drawable.mapa_ambos);
		} else if (onChoice.isInObjects(oiSC)) {
			// set current mapImage
			onChoice.setCurrentMapPosition(R.drawable.mapa_espantalho);
		} else if (onChoice.isInObjects(oiLake)) {
			// set current mapImage
			onChoice.setCurrentMapPosition(R.drawable.mapa_lago);
		} else {
			// set current mapImage
			onChoice.setCurrentMapPosition(R.drawable.mapa_escolha);
		}
		// Add button to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);

		return view;
	}

	private void transitionFragment(boolean isShadowPath) {
		// record the choice on main activity
		String iconNextPage;
		if (isShadowPath) {
			PageScareCrowField frg = new PageScareCrowField();
			onChoice.onChoiceMade(frg, PageScareCrowField.NAME, PageScareCrowField.icon);
		} else {
			PagLakeToCross frg = new PagLakeToCross();
			onChoice.onChoiceMade(frg, PagLakeToCross.NAME, PagLakeToCross.icon);

		}

		onChoice.onChoiceMadeCommit(NAME, true);

	}

	private ImageView iv1;
	private ImageView iv2;
	Bitmap bitmap1;
	Bitmap bitmap2;

	private void loadImages() {

		iv1 = (ImageView) view.findViewById(R.id.page3Image);
		iv2 = (ImageView) view.findViewById(R.id.page3ImageClick);

		ObjectItem oiKey = new ObjectItem();
		ObjectItem oiBottle = new ObjectItem();

		oiKey.setObjectImageType(ObjectItem.TYPE_KEY);
		oiBottle.setObjectImageType(ObjectItem.TYPE_BOTTLE);

		isLakeDone = onChoice.isInObjects(oiKey);
		isScareCrowDone = onChoice.isInObjects(oiBottle);

		if (isScareCrowDone && isLakeDone) {

			bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
					R.drawable.choice_both_path, 600, 300);
		} else if (isScareCrowDone) {

			bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
					R.drawable.choice_after_scarecrow, 600, 300);
		} else if (isLakeDone) {

			bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
					R.drawable.choice_lake, 600, 300);
		} else {

			bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
					R.drawable.choice_none, 600, 300);
		}

		iv1.setImageBitmap(bitmap1);

		bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.choice_cli, 50, 25);
		iv2.setImageBitmap(bitmap2);

		// set current mapImage
		onChoice.setCurrentMapPosition(R.drawable.mapa_escolha);
		// Add button to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);
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
				transitionFragment(true);
			} else if (Utils.closeMatch(Color.WHITE, touchColor, tolerance)) {
				// Do the action associated with the white region
				transitionFragment(false);
			}
			break;
		}

		return true;
	}

	@Override
	public String getPrevPage() {
		return PagPathChoiceFrg.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}

}
