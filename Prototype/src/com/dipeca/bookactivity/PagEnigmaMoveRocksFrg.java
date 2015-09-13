package com.dipeca.bookactivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.dipeca.item.IMainActivity;
import com.dipeca.item.ObjectItem;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PagEnigmaMoveRocksFrg extends Fragment implements OnTouchListener,
		IFragmentBook {

	private IMainActivity onChoice;
	ImageView imageView = null;
	ImageView ivBG = null;
	RelativeLayout layout = null;
	Paint paintStroke;
	Paint paintFill;
	Canvas canvas = null;
	Bitmap bitmap = null;

	private ImageButton btn = null;
	private ImageButton btnHelp = null;
	private static int marginLeftMenu = 0;

	private ImageButton btnOpenPopup = null;
	private PopupWindow popupWindow = null;
	private ImageButton btnFacebookHelp = null;
	private ImageButton btnGPlusHelp = null;

	private boolean isMazeSolved = false;

	public static int NAME = R.string.enigma;
	public static int icon = R.drawable.enigma_icon;

	private Toast toastObject = null;

	// We'll be creating an image that is 400 pixels wide and 400 pixels
	// tall.
	private int width = 400;
	private int height = 400;

	private int movesAllowed = 0;
	private int totalMoves = -100;
	private float density;
	// We will give 15 px of margin. So everything less then 15px of
	// difference is considered accurate
	private float fingerTouchMargin = 15;

	// Lines segments global
	private final List<LineSegment> lines = new ArrayList<LineSegment>();
	private final List<LineSegment> firstShovel = new ArrayList<LineSegment>();
	private final List<LineSegment> secondShovel = new ArrayList<LineSegment>();
	private final List<LineSegment> thirdShovel = new ArrayList<LineSegment>();
	private final List<LineSegment> drewLines = new ArrayList<LineSegment>();

	private final List<LineSegment> forbiddenDrawLines1 = new ArrayList<LineSegment>();
	private final List<LineSegment> forbiddenDrawLines2 = new ArrayList<LineSegment>();
	private final List<LineSegment> forbiddenDrawLines3 = new ArrayList<LineSegment>();
	private final List<LineSegment> forbiddenDrawLines4 = new ArrayList<LineSegment>();

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		density = (float) getResources().getDisplayMetrics().density;

		fingerTouchMargin = 15 * (int) Math.ceil(density);
		float widthPx = 400 * (int) Math.ceil(density);
		float heightPx = 400 * (int) Math.ceil(density);

		width = (int) widthPx;
		height = (int) heightPx;

		// Create a bitmap with the dimensions we defined above, and with a
		// 16-bit pixel format. We'll
		// get a little more in depth with pixel formats in a later post.
		bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);

		// Create a paint object for us to draw with, and set our drawing color
		// to blue.
		paintStroke = new Paint();
		paintFill = new Paint();

		paintStroke.setColor(0xffcccccc);
		paintStroke.setStrokeWidth(2 * (int) Math.ceil(density));
		paintStroke.setStyle(Paint.Style.STROKE);
		paintFill.setColor(0xFF666668);

		// Create a new canvas to draw on, and link it to the bitmap that we
		// created above. Any drawing
		// operations performed on the canvas will have an immediate effect on
		// the pixel data of the
		// bitmap.
		canvas = new Canvas(bitmap);

		// Draw a rectangle inside our image using the paint object we defined
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
		RelativeLayout.LayoutParams paramsBG = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.addView(ivBG, paramsBG);
		layout.addView(imageView, params);
		ivBG.setScaleType(ScaleType.FIT_XY);
		ivBG.setImageBitmap(onChoice.decodeSampledBitmapFromResourceBG(
				getResources(), R.drawable.dungeon_enigma_back, 480 * (int) Math.ceil(density),
				240 * (int) Math.ceil(density)));
 
		drawBoard();
		drawFirstShovel();
		drawSecondShovel();
		drawThirdShovel();

		drawFirstBadDrawing();
		drawFirstBadDrawing2();
		drawFirstBadDrawing3();
		drawFirstBadDrawing4();
		// clear lines and add the other lines of the board
		// lines.clear();
		// lines.addAll(0, firstShovel);
		// lines.addAll(0, secondShovel);

		imageView.setOnTouchListener(this);

		// Add textview
		loadText();

		// add buttons
		handleButtons();

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

		return layout;
	}

	private void addListeners() {

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isMazeSolved) {

					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
					}

					// ///
					stopTimer();

					Fragment fb = null;

					fb = new PagDungeonPostChallenge();

					onChoice.onChoiceMade(fb, PagDungeonPostChallenge.NAME,
							PagDungeonPostChallenge.icon);

					onChoice.onChoiceMadeCommit(NAME, true);

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
				if (!isMazeSolved) {
					for (LineSegment lg : secondShovel) {
						if (!lg.isDrawn() && !lockHelp) {
							// get the previous color of the line (black or
							// grey)
							previousColor = bitmap.getPixel(lg.getX3(),
									lg.getY3());

							paintStroke.setColor(Color.BLUE);

							int x1 = lg.getX1();
							int y1 = lg.getY1();
							int x2 = lg.getX2();
							int y2 = lg.getY2();

							canvas.drawLine(x1, y1, x2, y2, paintStroke);
							lgToBeErased = lg;
							lgToBeErased.setDrawn(true);

							lockHelp = true;
							startTimer();

							// For each line we take 2 points
							// and we take 3 more because we are in a help state
							onChoice.addPoints(-2);
							onChoice.addPoints(-3);

							break;

						}
					}

					imageView.invalidate();
				}
			}
		});

		btnOpenPopup.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (popupWindow == null || !popupWindow.isShowing()) {

					LayoutInflater layoutInflater = (LayoutInflater) getActivity()
							.getBaseContext().getSystemService(
									BookActivity.LAYOUT_INFLATER_SERVICE);
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

	}

	Timer timer = new Timer(false);
	LineSegment lgToBeErased = null;
	int previousColor = -1;
	private static boolean lockHelp = false;

	private void startTimer() {
		final Handler handler = new Handler();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						paintStroke.setColor(previousColor);

						int x1 = lgToBeErased.getX1();
						int y1 = lgToBeErased.getY1();
						int x2 = lgToBeErased.getX2();
						int y2 = lgToBeErased.getY2();

						canvas.drawLine(x1, y1, x2, y2, paintStroke);
						lgToBeErased.setDrawn(false);

						imageView.invalidate();

						lockHelp = false;
					}
				});
			}
		};

		timer.schedule(timerTask, 1500); // 1000 = 1 second.
	}

	private void stopTimer() {
		// Reset timer
		timer.cancel();
		timer.purge();
	}

	private void loadText() {
		TextView txt = new TextView(this.getActivity());
		txt.setText(R.string.enigmaShovel);
		txt.setBackgroundResource(R.drawable.container_dropshadow);
		txt.setTextAppearance(getActivity().getApplicationContext(),
				R.style.StoryBlock);
		int pad = Math.round(12 * density);
		txt.setPadding(pad, pad, pad, pad);
		RelativeLayout.LayoutParams lpTxt = new RelativeLayout.LayoutParams(
				Math.round(240 * density), LayoutParams.WRAP_CONTENT);
		lpTxt.setMargins(pad, pad, 0, 0);

		layout.addView(txt, lpTxt);
	}

	private void cancelToast() {
		if (toastObject != null) {
			toastObject.cancel();

		}
	}

	private void handleButtons() {
		btn = new ImageButton(this.getActivity());
		btn.setImageResource(R.drawable.ic_action_play);
		btn.setBackgroundResource(R.drawable.button_back);

		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params1.setMargins(0, 0, (int) Math.ceil(12 * density),
				(int) Math.ceil(12 * density));

		layout.addView(btn, params1);

		btnHelp = new ImageButton(getActivity());
		btnHelp.setImageResource(R.drawable.ic_action_help);
		btnHelp.setBackgroundResource(R.drawable.button_back);

		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params2.setMargins(0, (int) Math.ceil(12 * density),
				(int) Math.ceil(12 * density), 0);

		layout.addView(btnHelp, params2);

		btnOpenPopup = new ImageButton(getActivity());
		btnOpenPopup.setImageResource(R.drawable.ic_action_social_help);
		btnOpenPopup.setBackgroundResource(R.drawable.button_back);

		RelativeLayout.LayoutParams paramsSocialBtn = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsSocialBtn.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsSocialBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
				btnHelp.getId());
		// paramsSocialBtn.addRule(RelativeLayout.LEFT_OF, btnHelp.getId());
		paramsSocialBtn.setMargins(0, (int) Math.ceil(12 * density),
				(int) Math.ceil(84 * density), 0);

		layout.addView(btnOpenPopup, paramsSocialBtn);

		// add listeners to buttons
		addListeners();
	}

	private void drawFirstBadDrawing() {
		LineSegment lg = new LineSegment();
		int x1, y1, x2, y2;

		paintStroke.setColor(Color.RED);
		// Second correct line
		// /
		x1 = 160 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 80 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines1.add(lg);
		// /
		// /
		x1 = 320 * (int) Math.ceil(density);
		y1 = 320 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 400 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines1.add(lg);

		// /
		// /\
		x1 = 160 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines1.add(lg);

		// /
		// /\
		// \
		x1 = 80 * (int) Math.ceil(density);
		y1 = 240 * (int) Math.ceil(density);
		x2 = 160 * (int) Math.ceil(density);
		y2 = 320 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines1.add(lg);

		// /
		// /\
		// \/
		x1 = 160 * (int) Math.ceil(density);
		y1 = 320 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines1.add(lg);

		// /
		// /\
		// \/
		// \
		x1 = 320 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 400 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines1.add(lg);

		// /
		// /\/
		// \/
		// \
		x1 = 240 * (int) Math.ceil(density);
		y1 = 240 * (int) Math.ceil(density);
		x2 = 320 * (int) Math.ceil(density);
		y2 = 160 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines1.add(lg);

		// /
		// /\/
		// \/\
		// \
		x1 = 240 * (int) Math.ceil(density);
		y1 = 240 * (int) Math.ceil(density);
		x2 = 320 * (int) Math.ceil(density);
		y2 = 320 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines1.add(lg);

		paintStroke.setColor(0xffcccccc);
	}

	private void drawFirstBadDrawing2() {
		LineSegment lg = new LineSegment();
		int x1, y1, x2, y2;

		paintStroke.setColor(Color.BLUE);
		// Second correct line
		// /
		x1 = 0 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 80 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines2.add(lg);
		// /
		// /
		x1 = 80 * (int) Math.ceil(density);
		y1 = 80 * (int) Math.ceil(density);
		x2 = 160 * (int) Math.ceil(density);
		y2 = 160 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines2.add(lg);

		// /
		// /\
		x1 = 160 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines2.add(lg);

		// /
		// /\
		// \
		x1 = 80 * (int) Math.ceil(density);
		y1 = 80 * (int) Math.ceil(density);
		x2 = 160 * (int) Math.ceil(density);
		y2 = 0 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines2.add(lg);

		// /
		// /\
		// \/
		x1 = 160 * (int) Math.ceil(density);
		y1 = 320 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines2.add(lg);

		// /
		// /\
		// \/
		// \
		x1 = 160 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 80 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines2.add(lg);

		// /
		// /\/
		// \/
		// \
		x1 = 160 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 80 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines2.add(lg);

		// /
		// /\/
		// \/\
		// \
		x1 = 320 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 80 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines2.add(lg);

		paintStroke.setColor(0xffcccccc);
	}

	private void drawFirstBadDrawing3() {
		LineSegment lg = new LineSegment();
		int x1, y1, x2, y2;

		paintStroke.setColor(Color.RED);
		// Second correct line
		// /
		x1 = 160 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines3.add(lg);
		// /
		// /
		x1 = 320 * (int) Math.ceil(density);
		y1 = 320 * (int) Math.ceil(density);
		x2 = 400 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines3.add(lg);

		// /
		// /\
		x1 = 160 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 80 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines3.add(lg);

		// /
		// /\
		// \
		x1 = 240 * (int) Math.ceil(density);
		y1 = 240 * (int) Math.ceil(density);
		x2 = 160 * (int) Math.ceil(density);
		y2 = 320 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines3.add(lg);

		// /
		// /\
		// \/
		x1 = 320 * (int) Math.ceil(density);
		y1 = 320 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines3.add(lg);

		// /
		// /\
		// \/
		// \
		x1 = 160 * (int) Math.ceil(density);
		y1 = 320 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 400 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines3.add(lg);

		// /
		// /\/
		// \/
		// \
		x1 = 240 * (int) Math.ceil(density);
		y1 = 80 * (int) Math.ceil(density);
		x2 = 320 * (int) Math.ceil(density);
		y2 = 160 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines3.add(lg);

		// /
		// /\/
		// \/\
		// \
		x1 = 240 * (int) Math.ceil(density);
		y1 = 240 * (int) Math.ceil(density);
		x2 = 320 * (int) Math.ceil(density);
		y2 = 160 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines3.add(lg);

		paintStroke.setColor(0xffcccccc);
	}

	private void drawFirstBadDrawing4() {
		LineSegment lg = new LineSegment();
		int x1, y1, x2, y2;

		paintStroke.setColor(Color.YELLOW);
		// Second correct line
		// /
		x1 = 160 * (int) Math.ceil(density);
		y1 = 0 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 80 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines4.add(lg);
		// /
		// /
		x1 = 240 * (int) Math.ceil(density);
		y1 = 80 * (int) Math.ceil(density);
		x2 = 160 * (int) Math.ceil(density);
		y2 = 160 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines4.add(lg);

		// /
		// /\
		x1 = 160 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 80 * (int) Math.ceil(density);
		y2 = 80 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines4.add(lg);

		// /
		// /\
		// \
		x1 = 160 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 80 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines4.add(lg);

		// /
		// /\
		// \/
		x1 = 160 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines4.add(lg);

		// /
		// /\
		// \/
		// \
		x1 = 0 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 80 * (int) Math.ceil(density);
		y2 = 80 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines4.add(lg);

		// /
		// /\/
		// \/
		// \
		x1 = 80 * (int) Math.ceil(density);
		y1 = 240 * (int) Math.ceil(density);
		x2 = 160 * (int) Math.ceil(density);
		y2 = 320 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines4.add(lg);

		// /
		// /\/
		// \/\
		// \
		x1 = 240 * (int) Math.ceil(density);
		y1 = 240 * (int) Math.ceil(density);
		x2 = 320 * (int) Math.ceil(density);
		y2 = 160 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		forbiddenDrawLines4.add(lg);

		paintStroke.setColor(0xffcccccc);
	}

	private void drawFirstShovel() {
		LineSegment lg = new LineSegment();
		int x1, y1, x2, y2;

		paintStroke.setColor(Color.BLACK);
		// Second correct line
		// /
		x1 = width / 3;
		y1 = height / 3;
		x2 = width / 3;
		y2 = height * 2 / 3;
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		lg.setDrawn(true);
		firstShovel.add(lg);
		drewLines.add(lg);
		// /
		// /
		x1 = width / 3;
		y1 = height * 2 / 3;
		x2 = width * 2 / 3;
		y2 = height * 2 / 3;
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		lg.setDrawn(true);
		firstShovel.add(lg);
		drewLines.add(lg);

		// /
		// /\
		x1 = 160 * (int) Math.ceil(density);
		y1 = 160 * (int) Math.ceil(density);
		x2 = 240 * (int) Math.ceil(density);
		y2 = 240 * (int) Math.ceil(density);
		// canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		lg.setDrawn(true);
		// firstShovel.add(lg);
		// drewLines.add(lg);

		// /
		// /\
		// \
		x1 = width / 2;
		y1 = height * 2 / 3;
		x2 = width / 2;
		y2 = height;
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		lg.setDrawn(true);
		firstShovel.add(lg);
		drewLines.add(lg);

		// /
		// /\
		// \/
		x1 = width * 2 / 3;
		y1 = height * 2 / 3;
		x2 = width * 2 / 3;
		y2 = height / 3;
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		lg.setDrawn(true);
		firstShovel.add(lg);
		drewLines.add(lg);

		paintStroke.setColor(0xff72a654);
		// Draw object in middle of screen
		canvas.drawText("XXXXXX", (width / 2) - 20, (height / 2), paintStroke);
		canvas.drawText("XXXXXX", (width / 2) - 20, (height / 2) + 10,
				paintStroke);
		canvas.drawText("XXXXXX", (width / 2) - 20, (height / 2) + 20,
				paintStroke);
		canvas.drawText("XXXXXX", (width / 2) - 20, (height / 2) + 30,
				paintStroke);

		paintStroke.setColor(0xffcccccc);
	}

	private void drawBoard() {
		LineSegment lg = new LineSegment();
		int x1, y1, x2, y2;

		for (int i = 0; i < 6; i++) {
			x1 = (width * i) / 6;
			y1 = 0;
			x2 = (width * (i + 1)) / 6;
			y2 = 0;
			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}

		for (int i = 0; i <= 6; i++) {

			x1 = width * i / 6;
			y1 = 0;
			x2 = width * i / 6;
			y2 = height / 3;
			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}

		for (int i = 0; i < 6; i++) {

			x1 = width * i / 6;
			y1 = height / 3;
			x2 = width * (i + 1) / 6;
			y2 = height / 3;
			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}
		for (int i = 0; i <= 6; i++) {
			x1 = width * i / 6;
			y1 = height / 3;
			x2 = width * i / 6;
			y2 = height * 2 / 3;
			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}

		for (int i = 0; i < 6; i++) {

			x1 = width * i / 6;
			y1 = height * 2 / 3;
			x2 = width * (i + 1) / 6;
			y2 = height * 2 / 3;
			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}

		for (int i = 0; i <= 6; i++) {

			x1 = width * i / 6;
			y1 = height * 2 / 3;
			x2 = width * i / 6;
			y2 = height;
			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}

		for (int i = 0; i < 6; i++) {

			x1 = width * i / 6;
			y1 = height;
			x2 = width * (i + 1) / 6;
			y2 = height;
			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}

	}

	private void drawSecondShovel() {
		LineSegment lg = new LineSegment();
		int x1 = 0, y1 = 80, x2 = 80, y2 = 80;

		// Solution line
		// |
		x1 = width * 2 / 3;
		y1 = height / 3;
		x2 = width * 2 / 3;
		y2 = height * 2 / 3;

		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		secondShovel.add(lg);

		// Solution line
		// _|_
		x1 = width / 2;
		y1 = height * 2 / 3;
		x2 = width * 5 / 6;
		y2 = height * 2 / 3;

		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		secondShovel.add(lg);

		// Solution line
		// _|_
		// |
		x1 = width / 2;
		y1 = height * 2 / 3;
		x2 = width / 2;
		y2 = height;

		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		secondShovel.add(lg);

		// Solution line
		// _|_
		// | |
		x1 = width * 5 / 6;
		y1 = height * 2 / 3;
		x2 = width * 5 / 6;
		y2 = height;

		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		secondShovel.add(lg);

	}

	private void drawThirdShovel() {
		LineSegment lg = new LineSegment();
		int x1 = 0, y1 = 80, x2 = 80, y2 = 80;

		// Solution line
		// |
		x1 = width / 3;
		y1 = height / 3;
		x2 = width / 3;
		y2 = height * 2 / 3;

		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		thirdShovel.add(lg);

		// Solution line
		// _|_
		x1 = width / 6;
		y1 = height * 2 / 3;
		x2 = width / 2;
		y2 = height * 2 / 3;

		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		thirdShovel.add(lg);

		// Solution line
		// _|_
		// |
		x1 = width / 6;
		y1 = height * 2 / 3;
		x2 = width / 6;
		y2 = height;

		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		thirdShovel.add(lg);

		// Solution line
		// _|_
		// | |
		x1 = width / 2;
		y1 = height * 2 / 3;
		x2 = width / 2;
		y2 = height;

		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		thirdShovel.add(lg);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int[] baseCoordinates = { 0, 0 };

		Log.d("OnTouch", "onTouchEvent ");
		if (totalMoves < 3) {
			// We only want to perform the action once
			if (!isMazeSolved && event.getAction() == MotionEvent.ACTION_DOWN) {
				imageView.getLocationOnScreen(baseCoordinates);

				float x = event.getX();// - baseCoordinates[0];
				float y = event.getY();// - baseCoordinates[1];

				Log.d("OnTouch", "x: " + x + " y:" + y);

				// Bad, bad move but I have to do this because when we user the
				// drawer layout we have to
				// account for the 240dp = 320 px from the menu hided
				if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) <= Configuration.SCREENLAYOUT_SIZE_LARGE) {
					// x = x - (240 * density);

				}

				// Add the space occupied by the menus
				// x = x + (240 * density);

				// y += (80 * density);

				Log.d("OnTouch after correction", "x: " + x + " y:" + y);

				Point p = new Point((int) x, (int) y);
				LineSegment lg2 = null;

				// Check if we have moves allowed
				if (movesAllowed > 0) {
					Log.d("OnTouch", "MovesAlowed  > 0. =" + movesAllowed);
					paintStroke.setColor(Color.BLACK);
					lg2 = getBoardLines(p);

					if (lg2 != null) {
						int x1 = lg2.getX1();
						int y1 = lg2.getY1();
						int x2 = lgHorizontalCopy.getX2();
						int y2 = lg2.getY2();

						Log.d("OnTouch", "lg2 correct line OK.");

						// If the line is not black then we draw it and lose
						Log.d("OnTouch", "before isDrawn() " + x1 + ", " + y1
								+ ", " + x2 + "," + y2);

						// if horizontal line
						if (y1 == y2) {
							int x3 = Math.max(x1, x2);
							x3 -= 30;

							if (getDrewLines(p) == null
									&& getDrewLines(new Point(x3, y1)) == null) {
								canvas.drawLine(x1, y1, x2, y2, paintStroke);
								drewLines.add(lgHorizontalCopy);
								movesAllowed--;
								totalMoves++;
								Log.d("OnTouch",
										"lg2.isDrawn() = false. MovesAlowed  ++. ma ="
												+ movesAllowed);

							} else {
								toastObject = Utils.setToastObject(
										getActivity(), toastObject,
										"Line already drew at this point",
										marginLeftMenu * Math.round(density));
								toastObject.show();
							}

						} else if (getDrewLines(p) == null) {
							canvas.drawLine(x1, y1, x2, y2, paintStroke);
							drewLines.add(lgHorizontalCopy);
							movesAllowed--;
							totalMoves++;
							Log.d("OnTouch",
									"lg2.isDrawn() = false. MovesAlowed  ++. ma ="
											+ movesAllowed);
						}
					}

				} else if (movesAllowed == 0) {
					Log.d("OnTouch", "MovesAlowed  = 0.");
					// We don't have any moves so we must erase a line segment
					// to
					// earn moves
					paintStroke.setColor(0xffcccccc);
					lg2 = getDrewLines(p);

					if (lg2 != null) {
						int x1 = lg2.getX1();
						int y1 = lg2.getY1();
						int x2 = lg2.getX2();
						int y2 = lg2.getY2();

						Log.d("OnTouch", "lg2 cIncorrect line OK.");
						Log.d("OnTouch", "lg2.getX3(): " + lg2.getX3()
								+ " lg2.getY3() : " + lg2.getY3());

						canvas.drawLine(x1, y1, x2, y2, paintStroke);
						drewLines.remove(lg2);
						movesAllowed++;
						Log.d("OnTouch", "inside isDrawn() movesAllowed "
								+ movesAllowed);
					} else {
						cancelToast();
						toastObject = Toast.makeText(this.getActivity(),
								getString(R.string.removeLineFirst),
								Toast.LENGTH_SHORT);
						toastObject.setGravity(Gravity.CENTER_HORIZONTAL
								| Gravity.CENTER_VERTICAL, marginLeftMenu
								* Math.round(density), 0);
						toastObject.show();
					}
				}

				imageView.invalidate();
				isMazeSolved = checkMazeDone();

				// remove 2 points for each move
				if (!onChoice.isInJourney(getString(PagBoss.NAME))) {
					onChoice.addPoints(-2);
				}

				if (isMazeSolved) {
					cancelToast();
					
					//Success sound
					BookActivity.playMusicOnce(R.raw.success);
					toastObject = Toast.makeText(this.getActivity(),
							getString(R.string.youHaveDoneIt),
							Toast.LENGTH_LONG);
					toastObject.setGravity(Gravity.CENTER_HORIZONTAL
							| Gravity.CENTER_VERTICAL,
							marginLeftMenu * Math.round(density), 0);
					toastObject.show();

				}
			}
		} else {
			if (!isMazeSolved) {
				if (toastObject == null) {
					toastObject = Toast.makeText(getActivity(),
							getString(R.string.thirdlineplayed),
							Toast.LENGTH_LONG);
				} else {
					toastObject.setText(getString(R.string.thirdlineplayed));
				}

				toastObject.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL,
						marginLeftMenu * Math.round(density), 0);
				toastObject.show();
			}
		}
		return true;
	}

	private boolean checkMazeDone() {

		// If we have any help line drawn then it's not done
		if (lockHelp) {
			return false;
		}

		if (drewLines.size() != secondShovel.size()) {
			return false;
		}

		boolean isLgDrew = false;
		LineSegment lg = null;
		for (LineSegment drewLg : drewLines) {

			// Check if all correct lines are Ok
			for (int i = 0; i < thirdShovel.size(); i++) {
				lg = thirdShovel.get(i);
				if (Math.min(drewLg.getX1(), drewLg.getX2()) == Math.min(
						lg.getX1(), lg.getX2())
						&& Math.max(drewLg.getX2(), drewLg.getX1()) == Math
								.max(lg.getX2(), lg.getX1())
						&& Math.min(drewLg.getY1(), drewLg.getY2()) == Math
								.min(lg.getY1(), lg.getY2())
						&& Math.max(drewLg.getY2(), drewLg.getY1()) == Math
								.max(lg.getY2(), lg.getY1())) {

					isLgDrew = true;
					i = secondShovel.size();
				} else {
					isLgDrew = false;
				}
			}
			if (!isLgDrew) {
				break; // we gonna check the third shovel now
			}
		}

		if (!isLgDrew) {
			for (LineSegment drewLg : drewLines) {

				// Check if all correct lines are Ok
				for (int i = 0; i < secondShovel.size(); i++) {
					lg = secondShovel.get(i);
					if (Math.min(drewLg.getX1(), drewLg.getX2()) == Math.min(
							lg.getX1(), lg.getX2())
							&& Math.max(drewLg.getX2(), drewLg.getX1()) == Math
									.max(lg.getX2(), lg.getX1())
							&& Math.min(drewLg.getY1(), drewLg.getY2()) == Math
									.min(lg.getY1(), lg.getY2())
							&& Math.max(drewLg.getY2(), drewLg.getY1()) == Math
									.max(lg.getY2(), lg.getY1())) {

						isLgDrew = true;
						i = secondShovel.size();
					} else {
						isLgDrew = false;
					}
				}
				if (!isLgDrew) {
					return false; // there is no more shovel so the no shovel is
									// drew
				}
			}
		}

		// For the correct answer we add 30 points,
		// remove 2 points for each move
		if (!onChoice.isInJourney(getString(PagBoss.NAME))) {
			onChoice.addPoints(30);
		}
		// return true;
		return true;
	}

	private LineSegment getClosestLine(Point p) {
		int b1 = 160;
		int m1 = -1;
		int y1 = 0;

		for (int i = 0; i < secondShovel.size(); i++) {
			LineSegment lg = secondShovel.get(i);
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

			// We will give 15 px * density of margin. So everything less then
			// 15px of
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

	/**
	 * Check if this point belongs to an already drew line
	 * 
	 * @param p
	 *            point
	 * @return the line where this points belongs if this point is already drew
	 */
	private LineSegment getDrewLines(Point p) {
		int b1 = 160;
		int m1 = -1;
		int y1 = 0;

		for (int i = 0; i < drewLines.size(); i++) {

			LineSegment lg = drewLines.get(i);

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

	LineSegment lgHorizontalCopy = new LineSegment();

	private LineSegment getBoardLines(Point p) {
		int b1 = 160;
		int m1 = -1;
		int y1 = 0;

		for (int i = 0; i < lines.size(); i++) {

			LineSegment lg = lines.get(i);
			lgHorizontalCopy = new LineSegment();

			lgHorizontalCopy.setB(lg.getB());
			lgHorizontalCopy.setDrawn(lg.isDrawn());
			lgHorizontalCopy.setM(lg.getM());
			lgHorizontalCopy.setX1(lg.getX1());
			lgHorizontalCopy.setX2(lg.getX2());
			lgHorizontalCopy.setX3(lg.getX3());
			lgHorizontalCopy.setY1(lg.getY1());
			lgHorizontalCopy.setY2(lg.getY2());
			lgHorizontalCopy.setY3(lg.getY3());

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

				// this is a horizontal line
				if (lg.getY1() == lg.getY2()
						&& (Math.abs(lg.getX1() - lg.getX2()) < (width / 3) - 1)) {

					// Copy lineSegment
					if (i < lines.size() - 1
							&& lines.get(i + 1).getY1() == lines.get(i + 1)
									.getY2()) {
						lgHorizontalCopy.setB(lines.get(i + 1).getB());
						lgHorizontalCopy.setDrawn(lines.get(i + 1).isDrawn());
						lgHorizontalCopy.setM(lines.get(i + 1).getM());
						lgHorizontalCopy.setX1(lines.get(i + 1).getX1());
						lgHorizontalCopy.setX2(lines.get(i + 1).getX2());
						lgHorizontalCopy.setX3(lines.get(i + 1).getX3());
						lgHorizontalCopy.setY1(lines.get(i + 1).getY1());
						lgHorizontalCopy.setY2(lines.get(i + 1).getY2());
						lgHorizontalCopy.setY3(lines.get(i + 1).getY3());

						lgHorizontalCopy.setX1(lg.getX1());
					} else if (i > 0
							&& lines.get(i - 1).getY1() == lines.get(i - 1)
									.getY2()) {
						lg = lines.get(i - 1);
						lgHorizontalCopy.setB(lines.get(i).getB());
						lgHorizontalCopy.setDrawn(lines.get(i).isDrawn());
						lgHorizontalCopy.setM(lines.get(i).getM());
						lgHorizontalCopy.setX1(lines.get(i - 1).getX1());
						lgHorizontalCopy.setX2(lines.get(i).getX2());
						lgHorizontalCopy.setX3(lines.get(i).getX3());
						lgHorizontalCopy.setY1(lines.get(i).getY1());
						lgHorizontalCopy.setY2(lines.get(i).getY2());
						lgHorizontalCopy.setY3(lines.get(i).getY3());
					}

					// if (lg.getX1() < lg.getX2()) {
					// int diff = lg.getX2() - lg.getX1();
					// lgHorizontalCopy.setX2(lg.getX2() + diff);
					// } else {
					// int diff = lg.getX1() - lg.getX2();
					// lgHorizontalCopy.setX1(lg.getX1() + diff);
					// }
				}

				return lg;
			}
		}

		lgHorizontalCopy = null;
		return null;
	}

	@Override
	public String getPrevPage() {

		return PagDungeonPrevChallenge.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}

}
