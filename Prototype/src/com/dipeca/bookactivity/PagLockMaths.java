package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
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

import com.dipeca.item.IMainActivity;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagLockMaths extends Fragment implements IFragmentBook {

	private IMainActivity onChoice;
	public static int NAME = R.string.lockFriend;
	public static int icon = R.drawable.cadeado_icon;
	private ImageButton button;

	private MathMentalPyramidFrg math = null;
	private View view = null;
	private ImageView iv1;
	private ImageView ivBack;
	private int density = 1;
	private ImageButton btnHelp = null;
	private ImageButton btnFacebookHelp = null;
	private ImageButton btnGPlusHelp = null;
	private Bitmap lock = null;
	private Bitmap background = null;
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
  
		background = null; 
		int resId = R.drawable.cofre_fechado; 
		if (isVaultPageToShow) {
			resId = R.drawable.cofre_fechado;
			lock = Utils.decodeSampledBitmapFromResource(getResources(),
					R.drawable.cadeado, 400, 400);
		} else { 
			resId = R.drawable.companheira_presa_lock_bg;
			lock = Utils.decodeSampledBitmapFromResource(getResources(),
					R.drawable.cadeado_trap, 400, 400);

		}
		
		iv1.setImageBitmap(lock); 
		Log.d("dalvikvm- " + getString(NAME), "lock");
		
		background = onChoice.decodeSampledBitmapFromResourceBG(getResources(),
				resId, 400 * density, 200 * density);
		
		ivBack.setImageBitmap(background);
		Log.d("dalvikvm- " + getString(NAME), "background");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_pag4, container, false);

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);

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
					popupWindow = new PopupWindow(popupView, (int) Math
							.ceil(164 * density), (int) Math
							.ceil(198 * density));

					popupView.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							popupWindow.dismiss();

						}
					});

					popupWindow.showAsDropDown(btnOpenPopup,
							-(int) Math.ceil(32 * density),
							(int) Math.ceil(0 * density));

					// FaceBook buttonNext
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

					// Google Plus buttonNext
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
							if (heightDiff > (100 * density)) { // if more than 100 pixels,its probably a keyboard...
								Log.d("PagLockMaths", "keyboard visible");
								view.findViewById(R.id.mathTr).setPadding(0,
										(int) (180 * density), 0, 0);
							} else {
								view.findViewById(R.id.mathTr).setPadding(0,
										(int) (220 * density), 0, 0);
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

//		if (background != null) {
//			background.recycle();
//			background = null;
//		}
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
