package com.dipeca.bookactivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dipeca.item.IMainActivity;
import com.dipeca.prototype.R;

public class PagEnigmaFrg extends Fragment implements OnTouchListener,
		IFragmentBook {

	private IMainActivity onChoice;
	public static int NAME = R.string.enigma;
	public static int icon = R.drawable.enigma_icon;
	private Toast toastObject = null;

	private ImageView imageView = null;
	private ImageView ivBG = null;
	private RelativeLayout layout = null;
	private Paint paintStroke;
	private Paint paintFill;
	private Canvas canvas = null;
	private Bitmap bitmap = null;
	private ImageButton btn = null;
	private ImageButton btnHelp = null;
	private ImageButton btnOpenPopup = null;
	private PopupWindow popupWindow = null;
	private ImageButton btnFacebookHelp = null;
	private ImageButton btnGPlusHelp = null;

	Display display = null;
	Point size = null;
	int widthDis = 0;
	int heightDis = 0;

	private static int marginLeftMenu = 0;

	// We'll be creating an image that is 400 pixels wide and 400 pixels
	// tall.
	private int width = 400;
	private int height = 400;

	private int density = 1;

	// We will give 15 px of margin. So everything less then 15px of
	// difference is considered accurate
	private float fingerTouchMargin = 15 * (int) Math.ceil(density);

	private boolean isMazeSolved = false;

	// Lines segments global
	private final List<LineSegment> lines = new ArrayList<LineSegment>();
	private final List<LineSegment> linesCorrect = new ArrayList<LineSegment>();

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

	private Timer timer = new Timer(false);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		density = (int) Math.ceil(getResources().getDisplayMetrics().density);

		//check if we are on a small device
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int heightDisplay = size.y; 
		if (heightDisplay < 800) {
			density = (int) Math.ceil( density * 0.50);
			fingerTouchMargin = 15 ;
		}else{
			fingerTouchMargin = 15 * (int) Math.ceil(density);
		}

		

		// Converts 400 dip into its equivalent px
		Resources r = getResources();
		float widthPx = 400 * (int) Math.ceil(density);
		float heightPx = 400 * (int) Math.ceil(density);

		width = (int) widthPx;
		height = (int) heightPx;

		display = getActivity().getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		widthDis = size.x;
		heightDis = size.y;

		// Create a bitmap with the dimensions we defined above, and with a
		// 16-bit pixel format. We'll
		// get a little more in depth with pixel formats in a later post.
		bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);

		// Create a paint object for us to draw with, and set our drawing color
		// to blue.
		paintStroke = new Paint();
		paintFill = new Paint();

		paintStroke.setColor(0x55d2c38f);
		paintStroke.setStrokeWidth(3 * (int) Math.ceil(density));
		paintStroke.setStyle(Paint.Style.STROKE);
		paintFill.setColor(0xFFb0a16e);

		// Create a new canvas to draw on, and link it to the bitmap that we
		// created above. Any drawing
		// operations performed on the canvas will have an immediate effect on
		// the pixel data of the
		// bitmap.
		canvas = new Canvas(bitmap);

		canvas.drawRect(0, 0, width, height, paintFill);

		// In order to display this image in our activity, we need to create a
		// new ImageView that we
		// can display.
		imageView = new ImageView(this.getActivity());
		ivBG = new ImageView(this.getActivity());

		// Set this ImageView's bitmap to the one we have drawn to.
		imageView.setImageBitmap(bitmap);
 
		// Create a simple layout and add our image view to it.
		layout = new RelativeLayout(this.getActivity());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		RelativeLayout.LayoutParams paramsBG = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    ivBG.setScaleType(ScaleType.FIT_XY);
	    
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.addView(ivBG, paramsBG);
		layout.addView(imageView, params);

		ivBG.setImageBitmap(onChoice.decodeSampledBitmapFromResourceBG(
				getResources(), R.drawable.back_black, 480 * (int) Math.ceil(density),
				240 * (int) Math.ceil(density)));

		drawCorrectLines();
		drawIncorrectLines();

		// we will be drawing the Crosses
		drawCrosses();

		// Add textview
		handleText();

		// add buttons
		handleButtons();

		// set the listeners to buttons
		addListeners();

		imageView.setOnTouchListener(this);

		// BookActivity.playMusic(R.raw.timer);

		startTimer();

		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) <= Configuration.SCREENLAYOUT_SIZE_LARGE) {
			marginLeftMenu = 0;
		} else {
			marginLeftMenu = 160;
		}

		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}

			}
		});

		
//		final ImageView mSuccessIV =  new ImageView(getActivity());
//		Bitmap bmMist = Utils.decodeSampledBitmapFromResource(getResources(),
//				R.drawable.mistfixed, 800 * density, 400 * density);
//		mSuccessIV.setImageBitmap(bmMist);
//		
//		RelativeLayout.LayoutParams paramsLayoutMist = new RelativeLayout.LayoutParams(
//				180 * density, 180 * density);
//		paramsLayoutMist.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		paramsLayoutMist.addRule(RelativeLayout.CENTER_VERTICAL);
//		((RelativeLayout) layout.getRootView()).addView(mSuccessIV,
//				paramsLayoutMist);
		
		return layout;
	}

	private void handleText() {
		TextView txt = new TextView(this.getActivity());
		txt.setText(R.string.enigma_missao);
		txt.setBackgroundResource(R.drawable.container_dropshadow);
		txt.setTextAppearance(getActivity().getApplicationContext(),
				R.style.StoryBlock);
		int pad = (int) Math
				.ceil(Double.valueOf(12 * (int) Math.ceil(density)));

		RelativeLayout.LayoutParams lpTxt = new RelativeLayout.LayoutParams(
				Math.round(240 * (int) Math.ceil(density)),
				LayoutParams.WRAP_CONTENT);

		lpTxt.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lpTxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		lpTxt.setMargins(pad, pad, 0, 0);
		txt.setPadding(pad, pad, pad, pad);

		layout.addView(txt, lpTxt);
	}

	private void handleButtons() {
		btn = new ImageButton(this.getActivity());
		btn.setBackgroundResource(R.drawable.next_bt);

		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				98 * density, 98 * density);
		params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params1.setMargins(0, 0,
				(int) Math.ceil(12 * (int) Math.ceil(density)),
				(int) Math.ceil(12 * (int) Math.ceil(density)));

		layout.addView(btn, params1);

		// Buttons Helps and Social Media help
		btnHelp = new ImageButton(getActivity());
		btnHelp.setBackgroundResource(R.drawable.ic_action_help);

		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
				98 * density, 98 * density);
		params2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params2.setMargins(0, (int) Math.ceil(12 * (int) Math.ceil(density)),
				(int) Math.ceil(12 * (int) Math.ceil(density)), 0);

		layout.addView(btnHelp, params2);

		btnOpenPopup = new ImageButton(getActivity());
		btnOpenPopup.setBackgroundResource(R.drawable.ic_action_social_help);

		RelativeLayout.LayoutParams paramsSocialBtn = new RelativeLayout.LayoutParams(
				98 * density, 98 * density);
		paramsSocialBtn.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsSocialBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// paramsSocialBtn.addRule(RelativeLayout.LEFT_OF, btnHelp.getId());
		paramsSocialBtn.setMargins(0,
				(int) Math.ceil(12 * (int) Math.ceil(density)),
				(int) Math.ceil(98 * (int) Math.ceil(density)), 0);

		layout.addView(btnOpenPopup, paramsSocialBtn);

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
							.ceil(164 * (int) Math.ceil(density)), (int) Math
							.ceil(198 * (int) Math.ceil(density)));

					popupView.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							popupWindow.dismiss();

						}
					});

					popupWindow.showAsDropDown(btnOpenPopup,
							-(int) Math.ceil(32 * (int) Math.ceil(density)),
							(int) Math.ceil(0 * (int) Math.ceil(density)));

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

		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}

			}
		});

	}

	private void addListeners() {

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isMazeSolved) {

					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
					}

					PagVillageAfterEnigmaFrg fb = new PagVillageAfterEnigmaFrg();

					onChoice.onChoiceMade(
							fb,
							getString(PagVillageAfterEnigmaFrg.NAME),
							getResources().getResourceName(
									PagVillageAfterEnigmaFrg.icon));
					onChoice.onChoiceMadeCommit(getString(NAME), true);

				} else {
					cancelToast();
					toastObject = Toast.makeText(getActivity(),
							getString(R.string.solveEnigmaFirst),
							Toast.LENGTH_SHORT);
					toastObject.setGravity(Gravity.CENTER_HORIZONTAL
							| Gravity.CENTER_VERTICAL,
							marginLeftMenu * Math.round(density), 0);
					toastObject.show();
				}
			}
		});

		btnHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				paintStroke.setColor(Color.BLACK);
				if (!isMazeSolved) {
					for (LineSegment lg : linesCorrect) {
						int pixelColor = -1;
						try {
							pixelColor = bitmap.getPixel(lg.getX3(), lg.getY3());
						} catch (IllegalArgumentException e) {
							Log.d(getString(NAME), "Error on: " + e);
						}
						// If the line is not draw already then we draw it, once
						if (!(Color.rgb(Color.red(Color.BLACK),
								Color.green(Color.BLACK),
								Color.blue(Color.BLACK)) == Color.rgb(
								Color.red(pixelColor), Color.green(pixelColor),
								Color.blue(pixelColor)))) {
							canvas.drawLine(lg.getX1(), lg.getY1(), lg.getX2(),
									lg.getY2(), paintStroke);
							lg.setDrawn(true);

							// For each line we take 2 points
							// and we take 10 more because we are in a help
							// state
							if(!onChoice.isInJourney(getString(PagVillageAfterEnigmaFrg.NAME))){
								onChoice.addPoints(-2);
								onChoice.addPoints(-10);
							}

							break;
						}
					}

					imageView.invalidate();
				}
				// Check if the puzzle has been completed
				checkMazeDone();
			}
		});
	}

	/**
	 * Each 10 seconds we will remove 1 point, i.e, 10 minutes removes 60 points
	 */
	private void startTimer() {
		final Handler handler = new Handler();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						if(!onChoice.isInJourney(getString(PagVillageAfterEnigmaFrg.NAME))){
							onChoice.addPoints(-1);
						}

					}
				});
			} 
		};
		timer.scheduleAtFixedRate(timerTask, 10000, 10000); // 1000 = 1 second.
	}

	private void stopTimer() {
		// Reset timer
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
	}

	private void drawCrosses() {
		int x1 = 0;
		int x2 = width / 3;
		int y1 = 0;
		int y2 = height / 3;

		for (int i = 0; i <= 9; i++) {
			paintFill.setColor(0xFF333333);
			paintFill.setTextSize(24 * (int) Math.ceil(density));
			canvas.drawText("X",
					((x1 + x2) / 2) - 8 * (int) Math.ceil(density),
					((y1 + y2) / 2) + 10 * (int) Math.ceil(density), paintFill);
			x1 += width / 3;
			x2 += width / 3;
			if (i % 3 == 0 && i > 0) {
				y1 += height / 3;
				y2 += height / 3;
				x1 = 0;
				x2 = width / 3;
			}
		}
	}

	private void drawCorrectLines() {
		LineSegment lg = new LineSegment();
		int x1, y1, x2, y2;

		// Second correct line
		// /
		x1 = 0;
		y1 = height / 2;
		x2 = width / 2;
		y2 = 0;
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		linesCorrect.add(lg);

		// /\
		x1 = width / 2;
		y1 = 0;
		x2 = width;
		y2 = height / 2;
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		linesCorrect.add(lg);
		// /\
		// \
		x1 = 0;
		y1 = height / 2;
		x2 = width / 2;
		y2 = height;
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		linesCorrect.add(lg);
		// /\
		// \/
		x1 = width / 2;
		y1 = height;
		x2 = width;
		y2 = height / 2;
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		linesCorrect.add(lg);

		// paintStroke.setColor(Color.RED);
		x1 = (0 + (width / 2)) / 2;
		y1 = (0 + (height / 2)) / 2;
		x2 = x1;
		y2 = ((height / 2) + height) / 2;
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		linesCorrect.add(lg);

		x1 = (0 + (width / 2)) / 2;
		y1 = (0 + (height / 2)) / 2;
		x2 = ((width / 2) + width) / 2;
		y2 = y1;
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		linesCorrect.add(lg);

		x1 = ((width / 2) + width) / 2;
		y1 = (0 + (height / 2)) / 2;
		x2 = x1;
		y2 = ((height / 2) + height) / 2;
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		linesCorrect.add(lg);

		x1 = (0 + (width / 2)) / 2;
		y1 = ((height / 2) + height) / 2;
		x2 = ((width / 2) + width) / 2;
		y2 = y1;
		canvas.drawLine(x1, y2, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		linesCorrect.add(lg);
	}

	private void drawIncorrectLines() {
		LineSegment lg = new LineSegment();
		int x1, y1, x2, y2;

		// First wrong line
		x1 = 0;
		x2 = width;
		y1 = 0;
		y2 = 0;
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		lines.add(lg);

		x1 = (0 + (width / 2)) / 2;
		y1 = 0;
		x2 = x1;
		y2 = height;
		// finish line
		canvas.drawLine(x1, 0, x2, height, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, 0, x2, height);
		lines.add(lg);

		x1 = (0 + (width / 2)) / 2;
		y1 = (0 + (height / 2)) / 2;
		x2 = ((width / 2) + width) / 2;
		y2 = y1;
		// finish line
		canvas.drawLine(0, y1, width, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(0, y1, width, y2);
		lines.add(lg);

		x1 = ((width / 2) + width) / 2;
		y1 = (0 + (height / 2)) / 2;
		x2 = x1;
		y2 = ((height / 2) + height) / 2;
		// finish line
		canvas.drawLine(x1, 0, x2, height, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, 0, x2, height);
		lines.add(lg);

		x1 = (0 + (width / 2)) / 2;
		y1 = ((height / 2) + height) / 2;
		x2 = ((width / 2) + width) / 2;
		y2 = y1;
		canvas.drawLine(x1, y2, x2, y2, paintStroke);
		// finish line
		canvas.drawLine(0, y1, width, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(0, y1, width, y2);
		lines.add(lg);
		canvas.drawLine(0, height / 2, width, height / 2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(0, height / 2, width, height / 2);
		lines.add(lg);

		canvas.drawLine(width / 2, 0, width / 2, height, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(width / 2, 0, width / 2, height);
		lines.add(lg);

		canvas.drawLine(0, 0, width, height, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(0, 0, width, height);
		lines.add(lg);

		canvas.drawLine(0, height, width, 0, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(0, height, width, 0);
		lines.add(lg);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int[] baseCoordinates = { 0, 0 };

		// We only want to perform the action once
		if (event.getAction() == MotionEvent.ACTION_DOWN && !checkMazeDone()) {
			imageView.getLocationOnScreen(baseCoordinates);

			float x = event.getX();// - baseCoordinates[0];
			float y = event.getY();// - baseCoordinates[1];

			Log.d("Point", "( " + x + ", " + y + " )\n" + widthDis + "x"
					+ heightDis);

			paintStroke.setColor(Color.BLACK);
			// y+=100;
			Point p = new Point((int) x, (int) y);
			LineSegment lg2 = getClosestLine(p);
			if (lg2 != null) {
				int x1 = lg2.getX1();
				int y1 = lg2.getY1();
				int x2 = lg2.getX2();
				int y2 = lg2.getY2();

				canvas.drawLine(x1, y1, x2, y2, paintStroke);
				lg2.setDrawn(true);

				// For each line we take 2 points
				if(!onChoice.isInJourney(getString(PagVillageAfterEnigmaFrg.NAME))){
					onChoice.addPoints(-2);
				}
			} else {
				paintStroke.setColor(Color.RED);
				lg2 = getClosestLineIncorrect(p);
				if (lg2 != null) {

					int x1 = lg2.getX1();
					int y1 = lg2.getY1();
					int x2 = lg2.getX2();
					int y2 = lg2.getY2();

					// Check if one of the correct lines pass trough here
					LineSegment lgCorrect = getClosestCorrectLineFromIncorrect(p);
					// If the incorrect line pass through the correct one then
					// we set the correct as not drawn anymore
					if (lgCorrect != null) {
						lgCorrect.setDrawn(false);

					}

					int pixel = bitmap.getPixel(lg2.getX3(), lg2.getY3());
					if ((int) Math.abs(Color.red(pixel)) == 255) {
						paintStroke.setColor(0xffd2c38f);
						canvas.drawLine(x1, y1, x2, y2, paintStroke);
						lg2.setDrawn(false);
						drawCrosses();
					} else {
						canvas.drawLine(x1, y1, x2, y2, paintStroke);
						lg2.setDrawn(true);
						drawCrosses();
					}

					// For each line we take 2 points
					if(!onChoice.isInJourney(getString(PagVillageAfterEnigmaFrg.NAME))){
						onChoice.addPoints(-2);
					}
				}
			}

			imageView.invalidate();
			checkMazeDone();
		}

		return true;
	}

	private boolean checkMazeDone() {
		// Check if all correct lines are Ok
		if (!isMazeSolved) {

			for (LineSegment lg : linesCorrect) {
				if (!lg.isDrawn()) {
					isMazeSolved = false;
					return false;
				}
			}

			for (LineSegment lgW : lines) {
				if (lgW.isDrawn()) {
					cancelToast();
					toastObject = Toast
							.makeText(this.getActivity(),
									getString(R.string.removeLines),
									Toast.LENGTH_SHORT);

					toastObject.setGravity(Gravity.CENTER_HORIZONTAL
							| Gravity.CENTER_VERTICAL,
							marginLeftMenu * Math.round(density), 0);
					toastObject.show();
					isMazeSolved = false;
					return false;
				}
			}

			// For the correct answer we add 30 points,
			if(!onChoice.isInJourney(getString(PagVillageAfterEnigmaFrg.NAME))){
				onChoice.addPoints(30);
			}

			isMazeSolved = true;
			BookActivity.stopMusic();
			// stop timer
			stopTimer();
		}

		cancelToast();

		//Success sound
		BookActivity.playMusicOnce(R.raw.success);
		toastObject = Toast.makeText(this.getActivity(),
				getString(R.string.youHaveDoneIt), Toast.LENGTH_SHORT);

		toastObject.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL,
				marginLeftMenu * Math.round(density), 0);
		toastObject.show();

		return true;
	}

	private void cancelToast() {
		if (toastObject != null) {
			toastObject.cancel();

		}
	}

	/*
	 * This function is equivalent to the other function to get the lines from
	 * the points but it doesn't check if the point is inside the line, i.e, it
	 * only checks if line equation ca represent the point, regardless of the
	 * point being inside or not of the line
	 */
	private LineSegment getClosestCorrectLineFromIncorrect(Point p) {
		int b1 = 200;
		int m1 = -1;
		int y1 = 0;

		for (int i = 0; i < linesCorrect.size(); i++) {
			LineSegment lg = linesCorrect.get(i);
			b1 = lg.getB(); // get the b from the line segment we are comparing
							// with the point
			m1 = lg.getM(); // get the slope from the line segment
			y1 = p.x * m1 + b1; // now we apply the line formula to the x of the
								// point and we get an y value
			// now we compute the difference between what would
			// be the y from the line segment and what we had with our point
			int value = Math.abs(y1 - p.y);

			// if horizontal
			if (lg.getY1() == lg.getY2()) {
				value = Math.abs(lg.getY1() - p.y);
			} // if vertical
			else if (lg.getX1() == lg.getX2()) {
				value = Math.abs(lg.getX1() - p.x); // space between the point x
													// where we clicked and the
													// true position of the line
			}

			// We will give 15 px of margin. So everything less then 15px of
			// difference is considered accurate
			// As opposite to the others functions, we don't care if this point
			// is inside the line or not
			// !! WE ONLY COMPARE THE EQUATION TO THE POINT
			if (value < fingerTouchMargin) {
				return lg;
			}
		}

		return null;
	}

	private LineSegment getClosestLine(Point p) {
		int b1 = 200;
		int m1 = -1;
		int y1 = 0;

		for (int i = 0; i < linesCorrect.size(); i++) {
			LineSegment lg = linesCorrect.get(i);
			b1 = lg.getB(); // get the b from the line segment we are comparing
							// with the point
			m1 = lg.getM(); // get the slope from the line segment
			y1 = p.x * m1 + b1; // now we apply the line formula to the x of the
								// point and we get an y value
			// now we compute the difference between what would
			// be the y from the line segment and what we had with our point
			int value = Math.abs(y1 - p.y);

			// if horizontal
			if (lg.getY1() == lg.getY2()) {
				value = Math.abs(lg.getY1() - p.y);
			} // if vertical
			else if (lg.getX1() == lg.getX2()) {
				value = Math.abs(lg.getX1() - p.x); // space between the point x
													// where we clicked and the
													// true position of the line
			}

			// We will give 15 px of margin. So everything less then 15px of
			// difference is considered accurate
			if (value < fingerTouchMargin
					&& (p.x >= Math.min(lg.getX1(), lg.getX2() - 10) && p.x <= Math
							.max(lg.getX1(), lg.getX2()) + 10) /*
																 * The x point
																 * is inside the
																 * line segment
																 */
					&& (p.y >= Math.min(lg.getY1(), lg.getY2()) - 10 && p.y <= Math
							.max(lg.getY1(), lg.getY2()) + 10) /*
																 * The y point
																 * is inside the
																 * line segment
																 */) {
				return lg;
			}
		}

		return null;
	}

	private LineSegment getClosestLineIncorrect(Point p) {
		int b1 = 200;
		int m1 = -1;
		int y1 = 0;

		for (int i = 0; i < lines.size(); i++) {
			LineSegment lg = lines.get(i);
			b1 = lg.getB();
			m1 = lg.getM();
			y1 = p.x * m1 + b1;
			int value = Math.abs(y1 - p.y);

			// if horizontal
			if (lg.getY1() == lg.getY2()) {
				value = Math.abs(lg.getY1() - p.y);
			} // if vertical
			else if (lg.getX1() == lg.getX2()) {
				value = Math.abs(lg.getX1() - p.x);
			}

			if (value < fingerTouchMargin
					&& (p.x >= Math.min(lg.getX1(), lg.getX2() - 10) && p.x <= Math
							.max(lg.getX1(), lg.getX2()) + 10)
					&& (p.y >= Math.min(lg.getY1(), lg.getY2()) - 10 && p.y <= Math
							.max(lg.getY1(), lg.getY2()) + 10)) {
				return lg;
			}
		}

		return null;
	}

	@Override
	public String getPrevPage() {
		return PagVillageFrg.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Stops timer
		stopTimer();
	}

}
