package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup; 
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dipeca.item.DialogBox;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.ObjectItem;
import com.dipeca.prototype.R;

public class PagFindFriendZoom extends Fragment implements IFragmentBook {
	private IMainActivity onChoice;
	public static int icon = R.drawable.companheira_presa_icon;
	public static int NAME = R.string.pagFoundFriend;

	private TextView tv1 = null;
	private TextView tv2 = null;
	private DialogBox dialogB = null;

	private Bitmap gataZoomBitmap;
	private static boolean isMapAcknowledged = false;

	ObjectItem oiMap = null;
	
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
		view = inflater
				.inflate(R.layout.pag_one_image_dialog, container, false);

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time pag2", "onCreateView after inflate time ="
				+ totalTime);

		loadText();

		final ImageButton button = (ImageButton) view
				.findViewById(R.id.goToNextPage);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagLockMaths fb = new PagLockMaths();
				fb.isVaultPage(false);
				onChoice.onChoiceMade(fb, getString(PagLockMaths.NAME),
						getResources().getResourceName(PagLockMaths.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);
			}
		});
		
		final ImageButton buttonPrev = (ImageButton) view 
				.findViewById(R.id.goToPrevPage);
		
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagFindFriend fb = new PagFindFriend();

				onChoice.onChoiceMade(fb, getString(PagFindFriend.NAME), getResources().getResourceName(PagFindFriend.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);
			}
		});  

		loadImages(); 
 
		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pag2_1), gataZoomBitmap));
		
		oiMap = new ObjectItem(null,getString(R.string.mapa), ObjectItem.TYPE_MAP, null);
		isMapAcknowledged = onChoice.isInObjects(oiMap);
		//If we still haven't create the map object we create it now
		if (!isMapAcknowledged) {
			onChoice.objectFoundPersist(oiMap);
		}	
		
		return view;
	}

	private void loadText() {
		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.trapLegend);

		dialogB = (DialogBox) view.findViewById(R.id.dialog);
		dialogB.setVisibility(View.GONE);
 
		tv2 = (TextView) view.findViewById(R.id.textPag1_2);
		tv2.setVisibility(View.GONE); 
		
		 
	}

	private void loadImages() {

		long startTime = System.currentTimeMillis();

		iv1 = (ImageView) view.findViewById(R.id.pag1ImageView);

		int density = (int) getResources().getDisplayMetrics().density;

		Log.d(getString(NAME), "density: " + density);
		gataZoomBitmap = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.companheira_presa_zoom, 400 * density, 200 * density);
		iv1.setImageBitmap(gataZoomBitmap);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(" Village loadImages Total time: " + totalTime);
	}

	@Override
	public String getPrevPage() {
		return PagFindFriend.class.getName();
	}

	@Override
	public String getNextPage() {
		// TODO Auto-generated method stub
		return null;
	}

}
