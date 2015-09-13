package com.dipeca.bookactivity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageButton;

import com.dipeca.buildstoryactivity.BuildPageActivity;
import com.dipeca.buildstoryactivity.ReadStoryActivity;
import com.dipeca.readingprojectlibrary.R;

/**
 * @author dipeca
 * 
 */
public class StartActivity extends Activity{

	// mediaPlayer-object will not we cleaned away since someone holds a
	// reference to it!
	private static MediaPlayer mediaPlayer;
	private static Context context;
	public static int playIcon;
	public static ImageButton stopButton;

	private ActionBarDrawerToggle mDrawerToggle;

	GridView objectsGridView;

	public static Bitmap bitmapInitial;
	public static Bitmap bitmap1;
	public static Bitmap bitmap2;
	public static Bitmap bitmapTalisma;

	private Bundle myBundle = null;

	Display display;
	Point sizeofDisplay = new Point();

	private static boolean isMusicMuted = false;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();

		releaseMusic();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);

		//Load next page
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.detailFragment, new StartPage());
		ft.commitAllowingStateLoss();
	}

	
	public static void playMusic(int id) {

		createMP(id);

		if (mediaPlayer != null) {
			mediaPlayer.setLooping(true);
			play();
		}
	}

	private static void createMP(int id) {
		if (id > -1) {

			releaseMusic();

			mediaPlayer = MediaPlayer.create(context, id);
		}
	} 
  
	private static void play() {

		if (!isMusicMuted && mediaPlayer != null) {
			mediaPlayer.start();
		}
	}

	private static void pauseMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
		}
	}

	public static void stopMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}

	private static void releaseMusic() {
		if (mediaPlayer != null) {
			stopMusic();

			mediaPlayer.release();
			mediaPlayer = null;
		}

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Log.d("BookActivity", "onPostCreate");
		// Sync the toggle state after onRestoreInstanceState has occurred.
		if (mDrawerToggle != null) {
			Log.d("BookActivity", "onPostCreate, != null ");
			mDrawerToggle.syncState();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d("BookActivity", "onConfigurationChanged");
		if (mDrawerToggle != null) {
			Log.d("BookActivity", "onConfigurationChanged, != null ");
			mDrawerToggle.onConfigurationChanged(newConfig);

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_build_story:
			callActivityBuildStory();
			return true;
		case R.id.action_read_story:
			callActivityReadStory();
			return true;
		case R.id.action_gui_story:
			callActivityGui();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Loads the activity to read our own stories
	 */
	private void callActivityReadStory() {
		Intent intent = new Intent(this, ReadStoryActivity.class);
		startActivity(intent);
	}

	/**
	 * Loads the activity to read our own stories
	 */
	private void callActivityGui() {
		Intent intent = new Intent(this, StartActivity.class);
		startActivity(intent);
	}

	/**
	 * Loads the activity to build our own stories
	 */
	private void callActivityBuildStory() {
		Intent intent = new Intent(this, BuildPageActivity.class);
		startActivity(intent);
	}

	public void makeProviderBundle(String[] projection, String selection,
			String[] selectionArgs, String sortOrder, String uri) {
		/*
		 * this is a convenience method to pass it arguments to pass into
		 * myBundle which in turn is passed into the Cursor loader to query the
		 */
		myBundle = new Bundle();
		myBundle.putStringArray("projection", projection);
		myBundle.putString("selection", selection);
		myBundle.putStringArray("selectionArgs", selectionArgs);
		if (sortOrder != null) {
			myBundle.putString("sortOrder", sortOrder);
		}
		myBundle.putString("uri", uri);
	}

	@Override
	protected void onStop() {
		super.onStop();

		releaseMusic();

		Log.d("bookactivity", "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		releaseMusic();

		Log.d("bookactivity", "onDestroy");
	}

	@Override
	protected void onResume() {
		super.onResume();
 
	} 

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		play(); 
	}

}