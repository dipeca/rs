package com.dipeca.buildstoryactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dipeca.bookactivity.IFragmentBook;
import com.dipeca.bookactivity.PagVillageFrg;
import com.dipeca.buildstoryactivity.entity.PageType;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PageLoadPage extends Fragment implements IFragmentBook {
	public static int NAME = R.string.story;
	public static int icon = R.drawable.caminho_somebody_icon;

	private IBuildActivity buildActivity;

	private TextView tv1 = null;
	private TextView tv2 = null;

	private ImageButton iBtnNext = null;
	private ImageButton iBtnPrev = null;

	private RelativeLayout view;
	private int density;
	private LayoutParams ivLegend1LayoutParams;
	private LayoutParams ivLegend2LayoutParams;

	private LayoutParams iBtnPrevLayoutParams;
	private LayoutParams iBtnNextLayoutParams;

	private ImageView ivFirstImage;
	private ImageView ivSecondImage;

	private PageObject page;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		buildActivity = (IBuildActivity) activity;
		page = (PageObject) buildActivity.getJourneyItem();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);

		view = new RelativeLayout(getActivity());

		view.post(new Runnable() {
			@Override
			public void run() {
				loadImages();
				loadText();
				loadButtons();
			}
		});

		return view;
	}

	/**
	 * Create buttons and display them on the screen
	 */
	private void loadButtons() {
		iBtnNext = new ImageButton(getActivity());
		iBtnPrev = new ImageButton(getActivity());

		int padding = (int) Math.ceil(Double.valueOf(12 * density));

		if (!buildActivity.isLastPage()) {
			iBtnNext.setImageResource(R.drawable.ic_action_play);
			iBtnNext.setBackgroundResource(R.drawable.button_back);

			iBtnNextLayoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			iBtnNextLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			iBtnNextLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			iBtnNextLayoutParams.setMargins(0, 0, padding, padding);

			view.addView(iBtnNext, iBtnNextLayoutParams);
		}

		if (!buildActivity.isFirstPage()) {
			iBtnPrev.setImageResource(R.drawable.ic_action_prev);
			iBtnPrev.setBackgroundResource(R.drawable.button_back);

			iBtnPrevLayoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			iBtnPrevLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			iBtnPrevLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

			iBtnPrevLayoutParams.setMargins(padding, 0, 0, padding);

			view.addView(iBtnPrev, iBtnPrevLayoutParams);
		}
		addListeners();
	}

	private void addListeners() {
		iBtnPrev.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				buildActivity.loadStoryPage(true);

			}
		});

		iBtnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				buildActivity.loadStoryPage(false);

			}
		});
	}

	/**
	 * Create text views and display them on the screen
	 */
	private void loadText() {
		tv1 = new TextView(getActivity());
		tv2 = new TextView(getActivity());

		int padding = (int) Math.ceil(Double.valueOf(12 * density));

		if (buildActivity.getJourneyItem().getType() == PageType.ONE_IMAGE) {
			// Set layout
			ivLegend1LayoutParams = new RelativeLayout.LayoutParams(
					view.getHeight() / 3,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			ivLegend1LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			ivLegend1LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			tv1.setBackgroundResource(R.drawable.container_dropshadow);

			// Set text
			tv1.setText(page.getLegend1Id());

			// Set margins
			ivLegend1LayoutParams
					.setMargins(padding, padding, padding, padding);
			tv1.setPadding(padding, padding, padding, padding);

			view.addView(tv1, ivLegend1LayoutParams);
		} else if (buildActivity.getJourneyItem().getType() == PageType.TWO_IMAGES
				|| buildActivity.getJourneyItem().getType() == PageType.INTENSE) {
			// Set layout
			ivLegend1LayoutParams = new RelativeLayout.LayoutParams(
					view.getHeight() / 3,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			ivLegend1LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			ivLegend1LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			tv1.setBackgroundResource(R.drawable.container_dropshadow);

			ivLegend2LayoutParams = new RelativeLayout.LayoutParams(
					view.getHeight() / 3,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			// Set left margin according to type of page
			if (buildActivity.getJourneyItem().getType() == PageType.INTENSE) {
				ivLegend2LayoutParams.addRule(RelativeLayout.ALIGN_LEFT,
						ivSecondImage.getId());
			} else {
				ivLegend2LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			}

			ivLegend2LayoutParams.addRule(RelativeLayout.ALIGN_TOP,
					ivSecondImage.getId());
			tv2.setBackgroundResource(R.drawable.container_dropshadow);

			// Set text
			tv1.setText(page.getLegend1Id());
			tv2.setText(page.getLegend2Id());

			// Set margins
			ivLegend1LayoutParams
					.setMargins(padding, padding, padding, padding);
			tv1.setPadding(padding, padding, padding, padding);
			tv2.setPadding(padding, padding, padding, padding);
			ivLegend2LayoutParams
					.setMargins(padding, padding, padding, padding);

			view.addView(tv1, ivLegend1LayoutParams);
			view.addView(tv2, ivLegend2LayoutParams);
		}
	}

	/**
	 * Create image views and display them on the screen
	 */
	@SuppressLint("NewApi")
	private void loadImages() {
		Bitmap bitmap2;

		LayoutParams ivBackParams;
		LayoutParams ivSecondParams;

		ivFirstImage = new ImageView(getActivity());
		ivSecondImage = new ImageView(getActivity());
		ivSecondImage.setId(1);

		Bitmap bitmap1 = Utils.decodeSampledBitmapFromResource(getResources(),
				buildActivity.getJourneyItem().getDrawable1Id(), 400 * density, 200 * density);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
			ivFirstImage.setBackground(new BitmapDrawable(getResources(),
					bitmap1));
		} else {
			ivFirstImage.setImageBitmap(bitmap1);
		}

		if (buildActivity.getJourneyItem().getType() == PageType.TWO_IMAGES) {
			ivSecondParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					view.getHeight() / 2);
			ivSecondParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
					RelativeLayout.TRUE);

			ivBackParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					view.getHeight() / 2);
			ivBackParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

			bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
					buildActivity.getJourneyItem().getDrawable2Id(), 600, 200);

			if (currentapiVersion > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
				ivSecondImage.setBackground(new BitmapDrawable(getResources(),
						bitmap2));
			} else {
				ivSecondImage.setImageBitmap(bitmap2);
			}

			view.addView(ivFirstImage, ivBackParams);
			view.addView(ivSecondImage, ivSecondParams);

		} else if (buildActivity.getJourneyItem().getType() == PageType.INTENSE) {
			ivSecondParams = new RelativeLayout.LayoutParams(
					view.getWidth() / 2,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			ivSecondParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			ivSecondParams.addRule(RelativeLayout.CENTER_VERTICAL);

			ivBackParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);

			bitmap2 = Utils.decodeSampledBitmapFromResource(getResources(),
					buildActivity.getJourneyItem().getDrawable2Id(), 400 * density, 200 * density);

			if (currentapiVersion > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
				ivSecondImage.setBackground(new BitmapDrawable(getResources(),
						bitmap2));
			} else {
				ivSecondImage.setImageBitmap(bitmap2);
				ivSecondImage.setBackgroundResource(R.drawable.imagebg);
			}

			view.addView(ivFirstImage, ivBackParams);
			view.addView(ivSecondImage, ivSecondParams);
		} else {
			ivBackParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);

			view.addView(ivFirstImage, ivBackParams);
		}

		view.setBackgroundResource(R.drawable.back_black);
	}

	@Override
	public String getPrevPage() {
		return PagVillageFrg.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}

}
