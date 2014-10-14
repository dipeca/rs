package com.dipeca.bookactivity;

import com.dipeca.prototype.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;

public class PagLockMaths extends Fragment implements IFragmentBook {

	private IMainActivity onChoice;
	public static int NAME = R.string.lockFriend;
	public static int icon = R.drawable.cadeado_icon;
	private static ImageButton button;

	MathMentalPyramidFrg math = null;
	View view = null;
	ImageView iv1;
	ImageView ivBack;
	private float density = 1;
	private static ImageButton btnHelp = null;
	private static ImageButton btnFacebookHelp = null;
	private static ImageButton btnGPlusHelp = null;
	private static Bitmap lock = null;
	private static Bitmap background = null;
	private PopupWindow popupWindow = null;

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

	private static boolean isVaultPageToShow = false;

	public void isVaultPage(boolean isVault) {
		isVaultPageToShow = isVault;
	}

	private void loadImages() {
		Log.d(getString(NAME), "loadImages()");

		iv1 = (ImageView) view.findViewById(R.id.imageView1);
		ivBack = (ImageView) view.findViewById(R.id.imageViewBack);

		lock = Utils.decodeSampledBitmapFromResource(getResources(),
				R.drawable.cadeado, 600, 300);
		iv1.setImageBitmap(lock);

		background = null;

		if (isVaultPageToShow) {
			background = Utils.decodeSampledBitmapFromResource(getResources(),
					R.drawable.cofre_fechado_back, 600, 300);
		} else {
			background = Utils.decodeSampledBitmapFromResource(getResources(),
					R.drawable.companheira_presa_lock_bg, 600, 300);
		}

		ivBack.setImageBitmap(background);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_pag4, container, false);

		density = (float) getResources().getDisplayMetrics().density;

		if (math == null) {
			math = new MathMentalPyramidFrg();
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.add(R.id.mathTr, math).commit();
		} else {
			math.generatePyramid();
		}

		loadImages();

		button = (ImageButton) view.findViewById(R.id.goToNextPage);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (math.isLockUnlocked()) {

					PagAfterChallenge frg = new PagAfterChallenge();

					onChoice.onChoiceMade(frg, PagAfterChallenge.NAME,
							PagAfterChallenge.icon);
					onChoice.onChoiceMadeCommit(PagAfterChallenge.NAME, true);
				}

			}
		});

		btnHelp = (ImageButton) view.findViewById(R.id.btnHelp);
		btnHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				math.setHelpVisible();
				// For the help we take 20
				onChoice.addPoints(-20);
			}
		});

		final ImageButton btnOpenPopup = (ImageButton) view
				.findViewById(R.id.btnSocialHelp);
		btnOpenPopup.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (popupWindow == null || !popupWindow.isShowing()) {

					LayoutInflater layoutInflater = (LayoutInflater) getActivity()
							.getBaseContext().getSystemService(
									getActivity().LAYOUT_INFLATER_SERVICE);
					View popupView = layoutInflater.inflate(
							R.layout.social_popup, null);
					popupWindow = new PopupWindow(popupView,
							(int) Math.ceil(164 * density),
							(int) Math.ceil(198 * density));
		               
					popupView.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							popupWindow.dismiss();

						}
					});

					popupWindow.showAsDropDown(btnOpenPopup,
							-(int) Math.ceil(32 * density), (int) Math.ceil(0 * density));

					// FaceBook button
					 btnFacebookHelp = (ImageButton) popupView
							.findViewById(R.id.facebook_button);
					btnFacebookHelp.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onChoice.askForHelpOnFacebook();
							
							if (popupWindow != null && popupWindow.isShowing()) {
								popupWindow.dismiss();
							}
						}
					});

					// Google Plus button
					btnGPlusHelp = (ImageButton) popupView
							.findViewById(R.id.gplus_button);
					btnGPlusHelp.setOnClickListener(new OnClickListener() {
 
						@Override
						public void onClick(View v) {
							onChoice.askForHelpOnGooglePlus();
							
							if (popupWindow != null && popupWindow.isShowing()) {
								popupWindow.dismiss();
							}
						}
					});
				}
			}
		});

		final View activityRootView = view.findViewById(R.id.activityRoot);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						int heightRV = activityRootView.getRootView()
								.getHeight();
						int wRV = activityRootView.getRootView().getWidth();
						int height = activityRootView.getHeight();
						int w = activityRootView.getWidth();
						int heightDiff = heightRV - height;
						if (w / density > 600) { // only adjust for tablets, not
													// for small devices
							if (heightDiff > (100 * density)) { // if more than
																// 100 pixels,
																// its probably
																// a keyboard...
								Log.d("PagLockMaths", "keyboard visible");
								view.findViewById(R.id.mathTr).setPadding(0,
										(int) (160 * density), 0, 0);
							} else {
								view.findViewById(R.id.mathTr).setPadding(0,
										(int) (232 * density), 0, 0);
							}
						}
					} 
				});

		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}

			}
		});

		return view;
	}

	@Override
	public void onDetach() {
		Log.d(getString(NAME), " onDetach()");
		super.onDetach();

		if (lock != null) {
			lock.recycle();
			lock = null;
		}

		if (background != null) {
			background.recycle();
			background = null;
		}
	}

	@Override
	public String getPrevPage() {
		return PagLockMaths.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}
}
