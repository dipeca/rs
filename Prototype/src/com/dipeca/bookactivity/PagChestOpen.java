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

import com.dipeca.prototype.R;

public class PagChestOpen extends Fragment implements IFragmentBook {
	View view = null;
	private IMainActivity onChoice;
	public static int NAME = R.string.openChest;
	public static int icon = R.drawable.cofre_aberto_icon;
	private TextView tv1 = null;
	private TextView tv3 = null;
	private boolean isTextHide = false;

	private static Bitmap bitmap1;
	
	private static ImageButton button = null;
	private static ImageButton buttonPrev = null;

	private static ObjectItem oi = null;
	
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

		bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.cofre_aberto, 600, 300);
		image1.setImageBitmap(bitmap1);
		
		// Add button to screen
		onChoice.addMapButtonToScreen((RelativeLayout) view);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

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
					height1 = tv1.getHeight() * multiplier - ((int)Math.ceil(8 * density));
					width3 = tv3.getWidth() * multiplier - ((int)Math.ceil(8 * density));
					isTextHide = false;
				} else {
					height1 = tv1.getHeight() / multiplier + ((int)Math.ceil(8 * density));
					width3 = tv3.getWidth() / multiplier + ((int)Math.ceil(8 * density));
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

				//Persist object
				oi = new ObjectItem();
				oi.setObjectImageType(ObjectItem.TYPE_KEY);
				oi.setName(getString(R.string.key));
				
				onChoice.objectFoundPersist(oi);
				
				PagAfterChestOpen fb = new PagAfterChestOpen();
				onChoice.onChoiceMade(fb, PagAfterChestOpen.NAME, PagAfterChestOpen.icon);

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
		
		//BookActivity.playMusic(R.raw.village);
		return view;
	}

	@Override
	public void onDetach() {
		Log.d("PagLateToCross ", "PagLateToCross  onDetach()");
		super.onDetach();

		bitmap1.recycle();
		bitmap1 = null;

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
