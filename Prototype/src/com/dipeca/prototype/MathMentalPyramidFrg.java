package com.dipeca.prototype;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
	private int base1;
	private int base2;
	private int base3;
	
	private TextView topET;
	private TextView medium1ET;
	private EditText medium2ET;
	private EditText base1ET;
	private TextView base2ET;
	private EditText base3ET;
	
	private View view = null;
	
	private IMainActivity onChoice;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        try {
        	onChoice = (IMainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnChoiceMade");
        }
	}
	
	public Button button;
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    view = inflater.inflate(R.layout.math_mental_pyramid,
	        container, false);
	    
	    
	    generatePyramid();
        button = (Button) view.findViewById(R.id.submitSolution);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(checkAllFieldsFilled()){
            		top 	= Integer.parseInt(topET.getText().toString());
            		medium1 = Integer.parseInt(medium1ET.getText().toString());
            		medium2 = Integer.parseInt(medium2ET.getText().toString()) ;
            		base1 	= Integer.parseInt(base1ET.getText().toString());
            		base2 	= Integer.parseInt(base2ET.getText().toString());
            		base3 	= Integer.parseInt(base3ET.getText().toString());
            		if((top == medium1 + medium2) && (medium1 + medium2 == base1+ base2 +base3)){
            			
            			Toast.makeText(getActivity(), "Destrancaste o cadeado!", Toast.LENGTH_SHORT).show();
            			
            			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
            				      Context.INPUT_METHOD_SERVICE);
            				imm.hideSoftInputFromWindow(base3ET.getWindowToken(), 0);
            			
            				onChoice.onChoiceMadeCommit("Cadeado", true);
            		}else{
            			Toast.makeText(getActivity(), "O cadeado n‹o abriu, ser‡ que te enganaste?", Toast.LENGTH_SHORT).show();
            		}
            	}else{
            		Toast.makeText(getActivity(), "Resolve o enigma antes de submeter", Toast.LENGTH_SHORT).show();
            	}
            }
        });
	    return view;
	  }

	/**
	 * Generates the pyramid so that the enigma can be solved by the user
	 * 
	 * @param view
	 */
	public void generatePyramid() {
		// Random value to see if we show the top of pyramid
		int isTopShown = (int) Math.ceil(Math.random() * 10);

		top = (int) Math.ceil(Math.random() * 100);
		// number of top can never be smaller than 20
		if (top < 5) {
			top = (top * 4) + (int) Math.ceil(Math.random() * 10);
		}

		medium1 = (int) ((int) Math.ceil(top / 2) + Math.ceil(top / 4) - Math
				.ceil(Math.random() * 10));
		medium2 = top - medium1;
		base1 = (int) ((int) Math.ceil(medium1 / 3) + Math.ceil(medium1 / 4));

		// if the number is pair then we show the top of the pyramid
		// if(isTopShown % 2 == 0){
		topET = (TextView) view.findViewById(R.id.top);
		medium1ET = (TextView) view.findViewById(R.id.middle1); 
		medium2ET = (EditText) view.findViewById(R.id.middle2);
		base2ET = (TextView) view.findViewById(R.id.base2);
		base3ET = (EditText) view.findViewById(R.id.base3);
		base1ET = (EditText) view.findViewById(R.id.base1);
		
		topET.setText(top + "");
		medium1ET.setText(medium2 + "");
		base2ET.setText(base1 + "");

	}
	
	
	
	private boolean checkAllFieldsFilled(){
		
		if(topET.getText() != null && topET.getText().length() > 0 &&
				medium1ET.getText() != null && medium1ET.getText().length() > 0 && medium2ET.getText() != null && medium2ET.getText().length() > 0 &&
						base1ET.getText() != null && base1ET.getText().length() > 0 && 
							base2ET.getText() != null && base2ET.getText().length() > 0 &&
									base3ET.getText() != null && base3ET.getText().length() > 0){
			return true;
		}
		
		return false;
	}
}
