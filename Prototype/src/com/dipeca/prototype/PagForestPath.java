package com.dipeca.prototype;

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
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PagForestPath extends Fragment {
	private IMainActivity onChoice;
	public static String NAME = "Caminho da floresta";

	AnimationDrawable backGroundChangeAnimJake;
	AnimationDrawable backGroundChangeAnimGui;

	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;

	private TextView tv1 = null;
	private TextView tv2 = null;
	private DialogBox dialogBox;

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
		view = inflater.inflate(R.layout.pag_two_images, container, false);

		final ImageButton button = (ImageButton) view
				.findViewById(R.id.goToNextPage);

		tv1 = (TextView) view.findViewById(R.id.textPag1);
		tv1.setText(R.string.on_the_roadWithFriend);

		tv2 = (TextView) view.findViewById(R.id.textPag2);
		tv2.setText(R.string.on_the_roadWithFriend2);
		
		dialogBox = (DialogBox) view.findViewById(R.id.dialog);
		dialogBox.setTextDialog(getString(R.string.on_the_roadFriendDialog));
		dialogBox.setImg1Id(getResources().getDrawable(R.drawable.gui2));
		dialogBox.setImg2Id(getResources().getDrawable(R.drawable.companheira));
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				dialogBox.getLayoutParams().width, 
				dialogBox.getLayoutParams().height);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params1.addRule(RelativeLayout.ALIGN_TOP, R.id.textPag1);
		dialogBox.setLayoutParams(params1);

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PagQuizz fb = new PagQuizz();

				onChoice.onChoiceMade(fb, PagPathChoiceFrg.NAME, null);
				onChoice.onChoiceMadeCommit(NAME, true);
			}
		});

		loadImages();

		return view;
	}

	private void loadImages() {
		Log.d(NAME, "loadImages()");

		int density = (int) getResources().getDisplayMetrics().density;

		iv1 = (ImageView) view.findViewById(R.id.page2Image);
		iv2 = (ImageView) view.findViewById(R.id.page2Image2);
		iv3 = (ImageView) view.findViewById(R.id.page2Image3);
		
		iv3.setVisibility(View.INVISIBLE);

		iv1.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 400 * density));

		Bitmap mountain = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.caminho_somebody_companheira, iv1.getLayoutParams().width
						* density, iv1.getLayoutParams().height * density);
		iv1.setImageBitmap(mountain);

		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 250 * density);
		rlp.addRule(RelativeLayout.BELOW, iv1.getId());
		rlp.setMargins(0, 16 * density, 0, 0);
		iv2.setLayoutParams(rlp);
		Bitmap mountainDial = Utils.decodeSampledBitmapFromResource(
				getResources(), R.drawable.theend, iv2.getLayoutParams().width
						/ 2 * density, iv2.getLayoutParams().height / 2
						* density);
		iv2.setImageBitmap(mountainDial);


	}
	AnimationDrawable backGroundChangeAnim;

}
