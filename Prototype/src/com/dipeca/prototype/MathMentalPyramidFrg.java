package com.dipeca.prototype;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author dipeca
 * 
 */
public class MathMentalPyramidFrg extends Fragment {

	private int top;
	private int medium1;
	private int medium2;
	private int base2Int;
	private int base1Int;
	private int base3Int;

	private TextView topET;
	private TextView medium1ET;
	private EditText medium2ET;
	private EditText base1ET;
	private TextView base2ET;
	private EditText base3ET;

	private TextView tvSolution1;
	private TextView tvSolution2;
	private TextView tvSolution3;
	private TextView tvSolution4;
	private TextView tvSolution5;
	private TextView tvSolution6;
	private TextView tvSolution7;

	private LinearLayout solutionLayout;
	private LinearLayout layout = null;

	private float density = 1;

	private View view = null;

	private IMainActivity onChoice;

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
		view = inflater.inflate(R.layout.math_mental_pyramid, container, false);

		density = (float) getResources().getDisplayMetrics().density;

		solutionLayout = (LinearLayout) view.findViewById(R.id.solutionLL);
		solutionLayout.setVisibility(View.INVISIBLE);

		layout = (LinearLayout) view.findViewById(R.id.helpBtnLayout);

		randomizeSolutionBoxes();

		generatePyramid();

		tvSolution1.setOnTouchListener(new MyTouchListener());
		tvSolution2.setOnTouchListener(new MyTouchListener());
		tvSolution3.setOnTouchListener(new MyTouchListener());
		tvSolution4.setOnTouchListener(new MyTouchListener());
		tvSolution5.setOnTouchListener(new MyTouchListener());
		tvSolution6.setOnTouchListener(new MyTouchListener());
		tvSolution7.setOnTouchListener(new MyTouchListener());

		medium2ET.setOnDragListener(new MyDragListener());
		base1ET.setOnDragListener(new MyDragListener());
		base3ET.setOnDragListener(new MyDragListener());

		return view;
	}

	public void setHelpVisible() {
		solutionLayout.setVisibility(View.VISIBLE);
	}

	private void randomizeSolutionBoxes() {
		double randomNum = 0;
		randomNum = Math.random() * (10 * Math.random());

		if (randomNum > 0 && randomNum <= 2) {
			if (randomNum > 1) {
				tvSolution1 = (TextView) view.findViewById(R.id.solution1);
				tvSolution2 = (TextView) view.findViewById(R.id.solution2);
				tvSolution3 = (TextView) view.findViewById(R.id.solution3);
				tvSolution4 = (TextView) view.findViewById(R.id.solution4);
				tvSolution5 = (TextView) view.findViewById(R.id.solution5);
				tvSolution7 = (TextView) view.findViewById(R.id.solution6);
				tvSolution6 = (TextView) view.findViewById(R.id.solution7);
			} else {
				tvSolution2 = (TextView) view.findViewById(R.id.solution1);
				tvSolution1 = (TextView) view.findViewById(R.id.solution2);
				tvSolution4 = (TextView) view.findViewById(R.id.solution3);
				tvSolution3 = (TextView) view.findViewById(R.id.solution4);
				tvSolution6 = (TextView) view.findViewById(R.id.solution5);
				tvSolution5 = (TextView) view.findViewById(R.id.solution6);
				tvSolution7 = (TextView) view.findViewById(R.id.solution7);
			}
		} else if (randomNum > 2 && randomNum < 5) {
			if (randomNum >= 4) {
				tvSolution1 = (TextView) view.findViewById(R.id.solution3);
				tvSolution2 = (TextView) view.findViewById(R.id.solution2);
				tvSolution3 = (TextView) view.findViewById(R.id.solution4);
				tvSolution4 = (TextView) view.findViewById(R.id.solution6);
				tvSolution5 = (TextView) view.findViewById(R.id.solution5);
				tvSolution6 = (TextView) view.findViewById(R.id.solution1);
				tvSolution7 = (TextView) view.findViewById(R.id.solution7);
			} else {
				tvSolution7 = (TextView) view.findViewById(R.id.solution3);
				tvSolution6 = (TextView) view.findViewById(R.id.solution2);
				tvSolution5 = (TextView) view.findViewById(R.id.solution4);
				tvSolution4 = (TextView) view.findViewById(R.id.solution6);
				tvSolution3 = (TextView) view.findViewById(R.id.solution5);
				tvSolution2 = (TextView) view.findViewById(R.id.solution1);
				tvSolution1 = (TextView) view.findViewById(R.id.solution7);
			}

		} else if (randomNum >= 5 && randomNum < 8) {
			tvSolution1 = (TextView) view.findViewById(R.id.solution5);
			tvSolution2 = (TextView) view.findViewById(R.id.solution7);
			tvSolution3 = (TextView) view.findViewById(R.id.solution3);
			tvSolution4 = (TextView) view.findViewById(R.id.solution6);
			tvSolution5 = (TextView) view.findViewById(R.id.solution1);
			tvSolution6 = (TextView) view.findViewById(R.id.solution2);
			tvSolution7 = (TextView) view.findViewById(R.id.solution4);
		} else {
			tvSolution1 = (TextView) view.findViewById(R.id.solution7);
			tvSolution2 = (TextView) view.findViewById(R.id.solution2);
			tvSolution3 = (TextView) view.findViewById(R.id.solution1);
			tvSolution4 = (TextView) view.findViewById(R.id.solution4);
			tvSolution5 = (TextView) view.findViewById(R.id.solution6);
			tvSolution6 = (TextView) view.findViewById(R.id.solution3);
			tvSolution7 = (TextView) view.findViewById(R.id.solution5);
		}
	}

	private final class MyTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				view.startDrag(data, shadowBuilder, view, 0);
				// view.setVisibility(View.INVISIBLE);
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean isLockUnlocked() {
		if (checkAllFieldsFilled()) {
			top = Integer.parseInt(topET.getText().toString());
			medium1 = Integer.parseInt(medium1ET.getText().toString());
			medium2 = Integer.parseInt(medium2ET.getText().toString());
			base2Int = Integer.parseInt(base1ET.getText().toString());
			base1Int = Integer.parseInt(base2ET.getText().toString());
			base3Int = Integer.parseInt(base3ET.getText().toString());
			if ((top == medium1 + medium2) && (medium1 == base2Int + base1Int)
					&& (medium2 == base1Int + base3Int)) {

				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(base3ET.getWindowToken(), 0);

				//onChoice.onChoiceMadeCommit(R.string.lock, true);
				return true;
			} else {
				Toast toast = Toast.makeText(getActivity(),
						getString(R.string.didnotopen), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

				return false;
			}
		} else {
			Toast toast = Toast.makeText(getActivity(),
					getString(R.string.solveEnigmaFirst), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

			return false;
		}
	}

	/**
	 * Generates the pyramid so that the enigma can be solved by the user
	 * 
	 * @param view
	 */
	public void generatePyramid() {

		top = (int) Math.ceil(Math.random() * 100);
		// number of top can never be smaller than 20
		if (top < 10) {
			if (top < 4) {
				top += 4;
				top *= 3;
			}
			top = (top * 4) + (int) Math.ceil(Math.random() * 10);

		}

		if (top < 20) {
			top = 25;
		}

		medium1 = (int) ((int) Math.ceil(top / 3) + Math.ceil(top / 4) - Math
				.ceil(Math.random() * 10));

		medium2 = top - medium1;
		base2Int = (int) ((int) Math.ceil(medium2 / 3) + Math.ceil(medium2 / 4) - 2);

		//if we gonna have negativa values on the base of the pyramyd we force to have 1!!
		if (medium1 - base2Int < 0) {
			base2Int = 1;
		}

		topET = (TextView) view.findViewById(R.id.top);
		medium1ET = (TextView) view.findViewById(R.id.middle1);
		medium2ET = (EditText) view.findViewById(R.id.middle2);
		base2ET = (TextView) view.findViewById(R.id.base2);
		base3ET = (EditText) view.findViewById(R.id.base3);
		base1ET = (EditText) view.findViewById(R.id.base1);

		topET.setText(top + "");
		medium1ET.setText(medium2 + "");
		base2ET.setText(base2Int + "");

		int base1 = medium1 - base2Int;
		int base3 = medium2 - base2Int;

		Log.d("Values pyramid ", "top: " + top + ",medium1: " + medium1
				+ ",medium2: " + medium2 + ",base1: " + base1 + ",base2Int: "
				+ base2Int + ",base3: " + base3);

		// Set text to solution
		int solution1 = medium1;
		int solution2 = base1;
		long solution3 = base1 + Math.round(Math.random() * 10);
		int solution4 = base3;
		long solution5 = base3 + Math.round(Math.random() * 12);
		long solution6 = base1 + Math.round(Math.random() * 14);
		long solution7 = medium1 + Math.round(Math.random() * 20);

		while (solution3 == solution1 || solution3 == solution2
				|| solution3 == solution4 || solution3 == solution5
				|| solution3 == solution6 || solution3 == solution7) {
			solution3 += Math.round(Math.random() * 10);
		}

		while (solution5 == solution1 || solution5 == solution2
				|| solution5 == solution4 || solution3 == solution5
				|| solution5 == solution6 || solution5 == solution7) {
			solution5 += Math.round(Math.random() * 10);
		}

		while (solution6 == solution1 || solution6 == solution2
				|| solution6 == solution4 || solution6 == solution5
				|| solution6 == solution3 || solution6 == solution7) {
			solution6 += Math.round(Math.random() * 10);
		}
		while (solution7 == solution1 || solution7 == solution2
				|| solution7 == solution4 || solution7 == solution5
				|| solution7 == solution3 || solution3 == solution7) {
			solution7 += Math.round(Math.random() * 10);
		}

		tvSolution1.setText(solution1 + "");
		tvSolution2.setText(solution2 + "");
		tvSolution3.setText(solution3 + "");
		tvSolution4.setText(solution4 + "");
		tvSolution5.setText(solution5 + "");
		tvSolution6.setText(solution6 + "");
		tvSolution7.setText(solution7 + "");
	}

	private boolean checkAllFieldsFilled() {

		if (topET.getText() != null && topET.getText().length() > 0
				&& medium1ET.getText() != null
				&& medium1ET.getText().length() > 0
				&& medium2ET.getText() != null
				&& medium2ET.getText().length() > 0
				&& base1ET.getText() != null && base1ET.getText().length() > 0
				&& base2ET.getText() != null && base2ET.getText().length() > 0
				&& base3ET.getText() != null && base3ET.getText().length() > 0) {
			return true;
		}

		return false;
	}

	class MyDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent event) {
			Log.d("onDrag", event.getAction() + "");
			float xPos = event.getX();
			float yPos = event.getY();

			int[] xyPos = new int[2];
			medium1ET.getLocationInWindow(xyPos);
			float yPosM = medium1ET.getY();

			Log.d("position", xPos + " " + yPos + " " + xyPos[0] + " "
					+ xyPos[1]);

			View view = (View) event.getLocalState();
			view.getLocationInWindow(xyPos);
			Log.d("locaState", xPos + " " + yPos + " " + xyPos[0] + " "
					+ xyPos[1]);

			if (event.getAction() == DragEvent.ACTION_DROP
					|| event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
				view = (View) event.getLocalState();
				tvSolution1.setVisibility(View.VISIBLE);
			}

			if (event.getAction() == DragEvent.ACTION_DRAG_LOCATION) {
				Log.d("position ACTION_DRAG_LOCATION", event.getX() + " "
						+ event.getY());
			}

			if (event.getAction() == DragEvent.ACTION_DRAG_ENTERED) {
				Log.d("ACTION_DRAG_ENTERED", "ACTION_DRAG_ENTERED !!!");
				TextView tv = (TextView) v;

				tv.setBackgroundResource(R.drawable.border_on_drag_entered);
			} else if (event.getAction() == DragEvent.ACTION_DROP) {
				Log.d("ACTION_DROP", "ACTION_DROP !!!");
				TextView tv = (TextView) v;

				tv.setBackgroundResource(R.drawable.textbox);
				tv.setText(((TextView) view).getText());

				view.setVisibility(View.VISIBLE);
			} else if (event.getAction() == DragEvent.ACTION_DRAG_EXITED) {
				Log.d("ACTION_DROP", "ACTION_DROP !!!");
				TextView tv = (TextView) v;

				tv.setBackgroundResource(R.drawable.textbox);
				tv.setText(((TextView) v).getText());

				view.setVisibility(View.VISIBLE);
			}

			return true;
		}

	}
}
