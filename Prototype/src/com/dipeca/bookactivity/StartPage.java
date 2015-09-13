package com.dipeca.bookactivity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.dipeca.buildstoryactivity.BuildPageActivity;
import com.dipeca.buildstoryactivity.ReadStoryActivity;
import com.dipeca.item.IFragmentBook;
import com.dipeca.readingprojectlibrary.R;

public class StartPage extends Fragment implements IFragmentBook {
	View view = null;
	public static int NAME = R.string.bedroom;
	public static int icon = R.drawable.quarto_vazio_icon;
	ImageView amuleto = null;
	private Bitmap bitmap1;

	public String nextPage = PagBedRoomDarkfrg.class.getName();
	public String prevPage = PagBedRoomDarkfrg.class.getName();

	private ImageView iv1;
	private float density = 1;

	private void loadImages() {
		iv1 = (ImageView) view.findViewById(R.id.pag1ImageView);

		density = (int) getResources().getDisplayMetrics().density;
		Log.d(getString(NAME), "Density: " + density);

		iv1.setImageResource(R.anim.startpageanim);
		AnimationDrawable backGroundChangeAnim = (AnimationDrawable) iv1
				.getDrawable();
		backGroundChangeAnim.start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		long startTime = System.currentTimeMillis();
		view = inflater.inflate(R.layout.start_page, container, false);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("Total time " + NAME, "onCreateView after inflate time ="
				+ totalTime);

		density = (int) getResources().getDisplayMetrics().density;

		loadImages();

		Button btnStartGame = (Button) view.findViewById(R.id.startGame);

		btnStartGame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), BookActivity.class);
				startActivity(intent);

			}
		});

		Button btnCreateStory = (Button) view.findViewById(R.id.buildStory);

		btnCreateStory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),
						BuildPageActivity.class);
				startActivity(intent);

			}
		});

		Button btnReadStory = (Button) view.findViewById(R.id.readMyStory);

		btnReadStory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),
						ReadStoryActivity.class);
				startActivity(intent);

			}
		});
		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (bitmap1 != null) {
			bitmap1.recycle();
			bitmap1 = null;
		}
	}

	@Override
	public String getPrevPage() {
		return prevPage;
	}

	@Override
	public String getNextPage() {
		return nextPage;
	}

}
