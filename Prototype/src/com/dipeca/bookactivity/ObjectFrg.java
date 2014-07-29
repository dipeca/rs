package com.dipeca.bookactivity;

import com.dipeca.prototype.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ObjectFrg extends Fragment {
	View view = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.pag_one_image, container, false);

		return view;
	}
}
