package com.dipeca.prototype;

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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PagEnigmaFish extends Fragment implements OnTouchListener {

	private IMainActivity onChoice;
	ImageView imageView = null;
	RelativeLayout layout = null;
	Paint paintStroke;
	Paint paintFill;
	Canvas canvas = null;
	Bitmap bitmap = null;

	private static ImageButton btn = null;
	private static ImageButton btnHelp = null;

	private boolean isMazeSolved = false;

	public static int NAME = R.string.enigma;
	public static int icon = R.drawable.enigma_icon;

	private static Toast toastObject = null;

	// We'll be creating an image that is 400 pixels wide and 400 pixels
	// tall.
	private int width = 400;
	private int height = 400;

	private int movesAllowed = 0;
	private int totalMoves = 0;
	private float density;
	// We will give 15 px of margin. So everything less then 15px of
	// difference is considered accurate
	private float fingerTouchMargin = 15;

	// Lines segments global
	private final List<LineSegment> lines = new ArrayList<LineSegment>();
	private final List<LineSegment> firstFish = new ArrayList<LineSegment>();
	private final List<LineSegment> secondFishLines = new ArrayList<LineSegment>();

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

		fingerTouchMargin = 15 * density;
		float widthPx = 400 * density;
		float heightPx = 400 * density;

		width = (int) widthPx;
		height = (int) heightPx;

		// Create a bitmap with the dimensions we defined above, and with a
		// 16-bit pixel format. We'll
		// get a little more in depth with pixel formats in a later post.
		bitmap = Bitmap.createBitmap(width + 1, height + 1, Config.RGB_565);

		// Create a paint object for us to draw with, and set our drawing color
		// to blue.
		paintStroke = new Paint();
		paintFill = new Paint();

		paintStroke.setColor(0xffd2c38f);
		paintStroke.setStrokeWidth(2 * density);
		paintStroke.setStyle(Paint.Style.STROKE);
		paintFill.setColor(0xFFb0a16e);

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

		// Set this ImageView's bitmap to the one we have drawn to.
		imageView.setImageBitmap(bitmap);

		// Create a simple layout and add our image view to it.
		layout = new RelativeLayout(this.getActivity());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.addView(imageView, params);
		layout.setBackgroundResource(R.drawable.back_black_fish);

		drawBoardLines();
		drawFirstFish();
		drawSecondFish();

		// clear lines and add the other lines of the board
		// lines.clear();
		lines.addAll(0, firstFish);
		lines.addAll(0, secondFishLines);

		layout.setOnTouchListener(this);

		// Add textview
		handleText();

		// add buttons
		handleButtons();

		return layout;
	}

	private void addListeners() {

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isMazeSolved) {

					PagVillageAfterEnigmaFrg fb = new PagVillageAfterEnigmaFrg();
					stopTimer();

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
					toastObject.show();
				}
			}
		});

		btnHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isMazeSolved) {
					for (LineSegment lg : secondFishLines) {
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
							onChoice.setAddPoints(-2);
							onChoice.setAddPoints(-3);
							
							break;

						}
					}


					imageView.invalidate();
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

	private void handleText() {
		TextView txt = new TextView(this.getActivity());
		txt.setText(R.string.enigma_fish);
		txt.setBackgroundResource(R.drawable.container_dropshadow);
		txt.setTextAppearance(getActivity().getApplicationContext(),
				R.style.StoryBlock);
		int pad = Math.round(16 * density);
		txt.setPadding(pad, pad, pad, pad);
		LayoutParams lpTxt = new LayoutParams(Math.round(240 * density),
				LayoutParams.WRAP_CONTENT);
		lpTxt.setMargins((int) (8 * density), (int) (8 * density), 0, 0);
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
		params2.setMargins(0, 0, (int) Math.ceil(12 * density),
				(int) Math.ceil(12 * density));

		layout.addView(btnHelp, params2);

		// add listeners to buttons
		addListeners();
	}

	private void drawFirstFish() {
		LineSegment lg = new LineSegment();
		int x1, y1, x2, y2;

		paintStroke.setColor(Color.BLACK);
		// Second correct line
		// /
		x1 = (int) (80 * density);
		y1 = (int) (240 * density);
		x2 = (int) (160 * density);
		y2 = (int) (160 * density);
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		lg.setDrawn(true);
		secondFishLines.add(lg);
		firstFish.add(lg);

		// /
		// /
		x1 = (int) (160 * density);
		y1 = (int) (160 * density);
		x2 = (int) (240 * density);
		y2 = (int) (80 * density);
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		lg.setDrawn(true);
		secondFishLines.add(lg);
		firstFish.add(lg);

		// /
		// /\
		x1 = (int) (160 * density);
		y1 = (int) (160 * density);
		x2 = (int) (240 * density);
		y2 = (int) (240 * density);
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		lg.setDrawn(true);
		secondFishLines.add(lg);
		firstFish.add(lg);

		// /
		// /\
		// \
		x1 = (int) (80 * density);
		y1 = (int) (240 * density);
		x2 = (int) (160 * density);
		y2 = (int) (320 * density);
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		lg.setDrawn(true);
		firstFish.add(lg);
		// addLinesAsDrawned(lg);

		// /
		// /\
		// \/
		x1 = (int) (160 * density);
		y1 = (int) (320 * density);
		x2 = (int) (240 * density);
		y2 = (int) (240 * density);
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		lg.setDrawn(true);
		secondFishLines.add(lg);
		firstFish.add(lg);

		// /
		// /\
		// \/
		// \
		x1 = (int) (160 * density);
		y1 = (int) (320 * density);
		x2 = (int) (240 * density);
		y2 = (int) (400 * density);
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		lg.setDrawn(true);
		firstFish.add(lg);
		// addLinesAsDrawned(lg);
		// lines.add(lg);

		// /
		// /\/
		// \/
		// \
		x1 = (int) (240 * density);
		y1 = (int) (240 * density);
		x2 = (int) (320 * density);
		y2 = (int) (160 * density);
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		secondFishLines.add(lg);
		lg.setDrawn(true);
		firstFish.add(lg);

		// /
		// /\/
		// \/\
		// \
		x1 = (int) (240 * density);
		y1 = (int) (240 * density);
		x2 = (int) (320 * density);
		y2 = (int) (320 * density);
		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		firstFish.add(lg);
		lg.setDrawn(true);
		// addLinesAsDrawned(lg);
		lines.add(lg);

		paintStroke.setColor(0xffd2c38f);
	}

	/**
	 * This method add lines to the board lines and if the line already exists
	 * then it will set it as drawn
	 */
	private void addLinesAsDrawned(LineSegment lg) {
		LineSegment lg2 = getBoardLines(new Point(lg.getX3(), lg.getY3()));
		if (lg2 != null) {
			lg2.setDrawn(true);
		} else {
			lines.add(lg);
		}
	}

	private void drawBoardLines() {
		LineSegment lg = new LineSegment();
		int x1 = 0, y1 = 80, x2 = 80, y2 = 80;

		for (int i = 0; i < 5; i++) {
			// diagonal left to right midlle
			y1 = i * 80;
			y2 = (i * 80) + 80;
			x1 = i * 80;
			x2 = (i * 80) + 80;

			y1 *= density;
			y2 *= density;
			x1 *= density;
			x2 *= density;

			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);

		}

		// diagonal left to right midlle
		for (int i = 0; i < 4; i++) {

			// diagonal right to left midlle
			y1 = i * 80;
			y2 = (i * 80) + 80;
			x1 = 400 - (i * 80 + 80);
			x2 = 400 - (i * 80 + 160);

			y1 *= density;
			y2 *= density;
			x1 *= density;
			x2 *= density;

			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}

		// diagonal left to right midlle
		for (int i = 0; i < 3; i++) {
			y1 = i * 80 + 160;
			y2 = (i * 80) + 240;
			x1 = i * 80;
			x2 = (i * 80) + 80;

			y1 *= density;
			y2 *= density;
			x1 *= density;
			x2 *= density;

			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}

		// diagonal left to right midlle
		for (int i = 0; i < 2; i++) {

			// diagonal right to left midlle
			y1 = i * 80;
			y2 = (i * 80) + 80;
			x1 = 400 - (i * 80 + 240);
			x2 = 400 - ((i * 80) + 320);

			y1 *= density;
			y2 *= density;
			x1 *= density;
			x2 *= density;

			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}

		// diagonal left to right midlle X-axis
		for (int i = 0; i < 4; i++) {

			// left to right
			x1 = 400 - (i * 80);
			y1 = i * 80 + 80;
			y2 = i * 80 + 160;
			x2 = 400 - (i * 80 + 80);

			y1 *= density;
			y2 *= density;
			x1 *= density;
			x2 *= density;

			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}

		// diagonal left to right midlle X-axis
		for (int i = 0; i < 3; i++) {
			x1 = i * 80 + 160;
			y1 = i * 80;
			y2 = (i * 80) + 80;
			x2 = (i * 80) + 240;

			y1 *= density;
			y2 *= density;
			x1 *= density;
			x2 *= density;

			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}

		// diagonal left to right midlle X-axis
		for (int i = 0; i < 2; i++) {

			// left to right
			x1 = 400 - (i * 80);
			y1 = i * 80 + 240;
			y2 = i * 80 + 320;
			x2 = 400 - (i * 80 + 80);

			y1 *= density;
			y2 *= density;
			x1 *= density;
			x2 *= density;

			canvas.drawLine(x1, y1, x2, y2, paintStroke);
			lg = new LineSegment();
			lg.setLineEquationFromPoints(x1, y1, x2, y2);
			lines.add(lg);
		}

	}

	private void drawSecondFish() {
		LineSegment lg = new LineSegment();
		int x1 = 0, y1 = 80, x2 = 80, y2 = 80;

		// Solution line
		// /
		x1 = 240;
		y1 = 80;
		x2 = 320;
		y2 = 160;

		y1 *= density;
		y2 *= density;
		x1 *= density;
		x2 *= density;

		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		secondFishLines.add(lg);

		// Second solution line
		// /
		x1 = 160;
		y1 = 0;
		x2 = 240;
		y2 = 80;

		y1 *= density;
		y2 *= density;
		x1 *= density;
		x2 *= density;

		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		secondFishLines.add(lg);

		// Third solution line
		// /
		x1 = 80;
		y1 = 80;
		x2 = 160;
		y2 = 160;

		y1 *= density;
		y2 *= density;
		x1 *= density;
		x2 *= density;

		canvas.drawLine(x1, y1, x2, y2, paintStroke);
		lg = new LineSegment();
		lg.setLineEquationFromPoints(x1, y1, x2, y2);
		secondFishLines.add(lg);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int[] baseCoordinates = { 0, 0 };

		Log.d("OnTouch", "onTouchEvent ");

		// We only want to perform the action once
		if (!isMazeSolved && event.getAction() == MotionEvent.ACTION_DOWN) {
			imageView.getLocationOnScreen(baseCoordinates);

			float x = event.getX() - baseCoordinates[0];
			float y = event.getY() - baseCoordinates[1];

			Log.d("OnTouch", "x: " + x + " y:" + y);

			// Bad, bad move but I have to do this because when we user the
			// drawer layout we have to
			// account for the 240dp = 320 px from the menu hided
			if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) <= Configuration.SCREENLAYOUT_SIZE_LARGE) {
				x = x - (240 * density);

			}

			// Add the space occupied by the menus
			x = x + (240 * density);
			y += (80 * density);

			Log.d("OnTouch after correction", "x: " + x + " y:" + y);

			Point p = new Point((int) x, (int) y);
			LineSegment lg2 = null;

			// Check if we have moves allowed
			if (movesAllowed > 0) {
				Log.d("OnTouch", "MovesAlowed  > 0. =" + movesAllowed);
				paintStroke.setColor(Color.BLACK);
				lg2 = getClosestLine(p);
				if (lg2 != null && !lg2.isDrawn()) {
					int x1 = lg2.getX1();
					int y1 = lg2.getY1();
					int x2 = lg2.getX2();
					int y2 = lg2.getY2();

					Log.d("OnTouch", "lg2 correct line OK.");

					// If the line is not black then we draw it and lose
					Log.d("OnTouch", "before isDrawn() " + x1 + ", " + y1
							+ ", " + x2 + "," + y2);
					int pixel = bitmap.getPixel(lg2.getX3(), lg2.getY3());
					if (!lg2.isDrawn()) {
						canvas.drawLine(x1, y1, x2, y2, paintStroke);
						lg2.setDrawn(true);
						movesAllowed--;
						totalMoves++;
						Log.d("OnTouch",
								"lg2.isDrawn() = false. MovesAlowed  ++. ma ="
										+ movesAllowed);
					}
				} else {
					Log.d("OnTouch", "MovesAlowed  > 0. =" + movesAllowed);
					lg2 = getBoardLines(p);
					if (lg2 != null) {
						Log.d("OnTouch", "lg2 cIncorrect line OK.");
						int x1 = lg2.getX1();
						int y1 = lg2.getY1();
						int x2 = lg2.getX2();
						int y2 = lg2.getY2();

						// If the line is not black then we draw it and lose
						Log.d("OnTouch", "before isDrawn() " + x1 + ", " + y1
								+ ", " + x2 + "," + y2);
						int pixel = bitmap.getPixel(lg2.getX3(), lg2.getY3());
						if (!lg2.isDrawn()) {
							canvas.drawLine(x1, y1, x2, y2, paintStroke);
							lg2.setDrawn(true);
							movesAllowed--;
							totalMoves++;
							Log.d("OnTouch",
									"lg2.isDrawn() = false. MovesAlowed  ++. ma ="
											+ movesAllowed);
						}
					}

				}

			} else if (movesAllowed == 0) {
				Log.d("OnTouch", "MovesAlowed  = 0. =" + movesAllowed);
				// We don't have any moves so we must erase a line segment to
				// earn moves
				paintStroke.setColor(0xffd2c38f);
				lg2 = getBoardLines(p);

				if (lg2 != null) {
					int x1 = lg2.getX1();
					int y1 = lg2.getY1();
					int x2 = lg2.getX2();
					int y2 = lg2.getY2();

					Log.d("OnTouch", "lg2 cIncorrect line OK.");
					Log.d("OnTouch", "lg2.getX3(): " + lg2.getX3()
							+ " lg2.getY3() : " + lg2.getY3());

					// If the line is black then it was drawn so we erase it and
					// earn a move
					int pixel = bitmap.getPixel(lg2.getX3(), lg2.getY3());
					Log.d("OnTouch", "before isDrawn() " + x1 + ", " + y1
							+ ", " + x2 + "," + y2);
					if (lg2.isDrawn()) {

						canvas.drawLine(x1, y1, x2, y2, paintStroke);

						lg2.setDrawn(false);
						movesAllowed++;
						Log.d("OnTouch", "inside isDrawn() movesAllowed "
								+ movesAllowed);
					} else {
						Toast.makeText(this.getActivity(),
								getString(R.string.removeLineFirst),
								Toast.LENGTH_SHORT).show();
					}
				}
			}
			// remove 2 points for each move
			onChoice.setAddPoints(-2);

			imageView.invalidate();
			isMazeSolved = checkMazeDone();
			if (isMazeSolved) {

				Toast.makeText(this.getActivity(),
						getString(R.string.youHaveDoneIt), Toast.LENGTH_LONG)
						.show();
			}
		}

		return true;
	}

	private boolean checkMazeDone() {
		
		//If we have any help line drawn then it's not done 
		if(lockHelp){
			return false;
		}
		
		// Check if all correct lines are Ok
		for (LineSegment lg : secondFishLines) {
			if (!lg.isDrawn()) {
				Log.d("Fish",
						"checkMazeDone x1: " + lg.getX1() + " y1: "
								+ lg.getY1() + ", x2: " + lg.getX2() + " y2: "
								+ lg.getY2());
				return false;
			}
		}

		// For the correct answer we add 30 points,
		onChoice.setAddPoints(30);
		return true;
	}

	private LineSegment getClosestLine(Point p) {
		int b1 = 160;
		int m1 = -1;
		int y1 = 0;

		for (int i = 0; i < secondFishLines.size(); i++) {
			LineSegment lg = secondFishLines.get(i);
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

	private LineSegment getBoardLines(Point p) {
		int b1 = 160;
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

}
