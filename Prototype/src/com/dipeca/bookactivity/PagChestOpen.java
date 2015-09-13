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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.ObjectItem;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagChestOpen extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.openChest;
	public static int icon = R.drawable.cofre_aberto_icon;
	private TextView tv1 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;

	private Bitmap bitmap1;

	private ImageButton button = null;
	private ImageButton buttonPrev = null;

	private ObjectItem oi = null;

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

	private ImageView image1;

	private void loadImages() {
		Log.d("KingDom ", "loadImages()");
		image1 = (ImageView) view.findViewById(R.id.pag1ImageView);
		int density = (int) Math.ceil(getResources().getDisplayMetrics().density);
		bitmap1 = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				R.drawable.cofre_aberto, 400 * density, 200 * density);
		image1.setImageBitmap(bitmap1);

		// Add buttonNext to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);

	}
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Found object sound
		if(!onChoice.isInJourney(getString(PagAfterChestOpen.NAME))){
			//BookActivity.playMusicOnce(R.raw.picked_item);
		}
		
		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.pag_one_image, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time kingdom", "onCreateView after inflate time ="
				+ totalTime);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.pagChestOpen);

		tv3 = (TextView) view.findViewById(R.id.textPag1_2);
		tv3.setVisibility(View.GONE);

		// loadImages()
		loadImages();

		tv1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				float density = (float) getResources().getDisplayMetrics().density;
				int height1 = 0;
				int width3 = 0;
				int multiplier = 7;
				if (isTextHide) {
					height1 = tv1.getHeight() * multiplier
							- ((int) Math.ceil(8 * density));
					width3 = tv3.getWidth() * multiplier
							- ((int) Math.ceil(8 * density));
					isTextHide = false;
				} else {
					height1 = tv1.getHeight() / multiplier
							+ ((int) Math.ceil(8 * density));
					width3 = tv3.getWidth() / multiplier
							+ ((int) Math.ceil(8 * density));
					isTextHide = true;
				}

				RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
						tv1.getWidth(), height1);
				tv1.setLayoutParams(params1);

				ViewGroup.LayoutParams params3 = tv3.getLayoutParams();
				params3.width = width3;
				params3.height = tv3.getHeight();
				tv3.setLayoutParams(params3);
			}
		});

		button = (ImageButton) view.findViewById(R.id.goToNextPage);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Persist object
				oi = new ObjectItem();
				oi.setObjectImageType(ObjectItem.TYPE_KEY);
				oi.setTitle(getString(R.string.key));

				onChoice.objectFoundPersist(oi);

				PagAfterChestOpen fb = new PagAfterChestOpen();
				onChoice.onChoiceMade(fb, PagAfterChestOpen.NAME,
						PagAfterChestOpen.icon);

				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		buttonPrev = (ImageButton) view.findViewById(R.id.goToPrevPage);
		buttonPrev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagChest fb = new PagChest();
				onChoice.onChoiceMade(fb, PagChest.NAME, PagChest.icon);

				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		// Set image and text to share intent
		onChoice.setShareIntent(onChoice.createShareIntent(
				getString(R.string.social_action_desc),
				getString(R.string.pagChestOpen), bitmap1));
		
		return view;
	}

	@Override
	public void onDetach() {
		Log.d("PagLateToCross ", "PagLateToCross  onDetach()");
		super.onDetach();

		image1.setImageBitmap(null);


	}

	@Override
	public String getPrevPage() {
		return PagChest.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}

}
