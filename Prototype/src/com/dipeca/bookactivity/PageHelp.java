package com.dipeca.bookactivity;

import com.dipeca.prototype.R;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PageHelp extends Fragment implements IFragmentBook {
	private IMainActivity onChoice;
	public static int NAME = R.string.adventureRule;
	public static int icon = R.drawable.caminho_somebody_icon;

	private TextView tv1 = null;
	private TextView tv2 = null;
	private TextView tv3 = null;

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

		view = inflater.inflate(R.layout.pag_help, container, false);

		final ImageButton button = (ImageButton) view
				.findViewById(R.id.goToNextPage);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.navHelp);

		tv2 = (TextView) view.findViewById(R.id.textPag2);
		tv2.setText(R.string.helpHelp);

		tv3 = (TextView) view.findViewById(R.id.textPag3);
		tv3.setText(R.string.objectsHelp);

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagEnigmaFrg fb = new PagEnigmaFrg();

				onChoice.onChoiceMade(fb, getString(PagEnigmaFrg.NAME),
						getResources().getResourceName(PagEnigmaFrg.icon));
				onChoice.onChoiceMadeCommit(getString(NAME), true);

			}
		});

		return view;
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
