package com.dipeca.bookactivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class PagQuizzAnswer extends Fragment {

	RelativeLayout layout = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		layout = new RelativeLayout(this.getActivity());
		layout.addView(new Button(getActivity()));
		
		return layout;
	}



	
}
