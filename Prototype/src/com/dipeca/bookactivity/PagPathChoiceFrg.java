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

import com.dipeca.item.IMainActivity;
import com.dipeca.item.ObjectItem;
import com.dipeca.item.Utils;
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
		
		ObjectItem oiSpell = new ObjectItem();
		oiSpell.setObjectImageType(ObjectItem.TYPE_SPELL);

		// Add buttonNext to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);

		return view;
	}

	private void loadNextPageFragment(boolean isShadowPath) {
		// record the choice on main activity
		if (isShadowPath) {
			PageScareCrowField frg = new PageScareCrowField();
			onChoice.onChoiceMade(frg, PageScareCrowField.NAME,
					PageScareCrowField.icon);
		} else {
			PagSwamp frg = new PagSwamp();
			onChoice.onChoiceMade(frg, PagSwamp.NAME, PagSwamp.icon);

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

		ObjectItem oiRope = new ObjectItem();
		ObjectItem oiBottle = new ObjectItem();

		oiRope.setObjectImageType(ObjectItem.TYPE_ROPE);
		oiBottle.setObjectImageType(ObjectItem.TYPE_BOTTLE);

		isLakeDone = onChoice.isInObjects(oiRope);
		isScareCrowDone = onChoice.isInObjects(oiBottle);

		int resId = 0;
		if (isScareCrowDone && isLakeDone) {
			resId = R.drawable.choice_both_path;

		} else if (isScareCrowDone) {
			resId = R.drawable.choice_after_scarecrow;
		} else if (isLakeDone) {
			resId = R.drawable.choice_lake;
		} else {
			resId = R.drawable.choice_none;
		}

		int density = (int) Math.ceil(getResources().getDisplayMetrics().density);
		bitmap1 = onChoice.decodeSampledBitmapFromResourceBG(getResources(), resId,
				400 * density, 200 * density);

		iv1.setImageBitmap(bitmap1);

		bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.choice_cli, 50, 25);
		iv2.setImageBitmap(bitmap2);

		// set current mapImage
		onChoice.setCurrentMapPosition(R.drawable.mapa_escolha);
		// Add Map btn to screen
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
				loadNextPageFragment(true);
			} else if (Utils.closeMatch(Color.WHITE, touchColor, tolerance)) {
				// Do the action associated with the white region
				loadNextPageFragment(false);
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
