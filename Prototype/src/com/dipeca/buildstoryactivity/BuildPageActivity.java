package com.dipeca.buildstoryactivity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dipeca.bookactivity.BookActivity;
import com.dipeca.bookactivity.PrototypeProvider;
import com.dipeca.bookactivity.entiy.Status;
import com.dipeca.buildstoryactivity.entity.Drawable;
import com.dipeca.item.IListItem;
import com.dipeca.item.JourneyItem;
import com.dipeca.item.JourneyListAdapter;
import com.dipeca.prototype.R;

public class BuildPageActivity extends Activity implements
		LoaderManager.LoaderCallbacks<Cursor>, IBuildActivity {

	private Context context;
	public static int playIcon;
	public static ImageButton stopButton;
	private InputMethodManager imm = null;
	private ArrayList<IListItem> journeyItemList;
	private JourneyListAdapter journeyArrayAdapter;

	private IListItem journeyItem;
	private ActionBarDrawerToggle mDrawerToggle;

	private int tableLoaded = 1;

	public static Bitmap bitmapInitial;
	public static Bitmap bitmap1;
	public static Bitmap bitmap2;
	public static Bitmap bitmapTalisma;

	private Bundle myBundle = null;
	
	private TextView tvStoryTitle = null;

	private DrawerLayout mDrawerLayout;

	Display display;
	Point sizeofDisplay = new Point();

	// Menu Points
	public static int points = 100;
	private TextView pointsText = null;
	private Fragment nextPage = null;
	private String nextPageClass = null;

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_build_book);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		display = getWindowManager().getDefaultDisplay();
		display.getSize(sizeofDisplay);

		// Get references to the Fragments
		FragmentManager fm = getFragmentManager();
		StoryPagesListFrg journeySoFar = (StoryPagesListFrg) fm
				.findFragmentById(R.id.journey);

		// Create the array list of to do items
		journeyItemList = new ArrayList<IListItem>();

		tvStoryTitle = (TextView) findViewById(R.id.storyTitle);
		
		// Create the array adapter to bind the array to the listview
		int resID = R.layout.journey_so_far;
		journeyArrayAdapter = new JourneyListAdapter(this, resID,
				journeyItemList, null);
		// Bind the array adapter to the listview.
		journeySoFar.setListAdapter(journeyArrayAdapter);

		pointsText = (TextView) findViewById(R.id.pointsValue);
		// configure the toggle menu
		configureDrawerMenu();

		context = getApplicationContext();

		Fragment fragmentLoaded = new PageBuildStory();

		loadFragment(fragmentLoaded);

		loadDataBase();
	}

	@Override
	public void setPageToDisplay(Fragment page) {
		loadFragment(page);
		
	}

	private void loadDataBase() {

	}

	/**
	 * In case this tablet is not wide enough for the left menu we use a drawer
	 * to show it
	 */
	private void configureDrawerMenu() {

		Log.d("BookActivity", "configureDrawerMenu");
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// If we are in a screen that need to have the toggle menu
		if (mDrawerLayout != null) {
			Log.d("BookActivity", "mDrawerLayout not null, so we configure it");

			mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
			mDrawerLayout, /* DrawerLayout object */
			R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
			R.string.next, /* "open drawer" description */
			R.string.title_activity_pag2 /* "close drawer" description */
			) {

				/**
				 * Called when a drawer has settled in a completely closed
				 * state.
				 */
				public void onDrawerClosed(View view) {
					super.onDrawerClosed(view);
					getActionBar().setTitle(getString(R.string.app_name));
				}

				/** Called when a drawer has settled in a completely open state. */
				public void onDrawerOpened(View drawerView) {
					super.onDrawerOpened(drawerView);
					getActionBar().setTitle(getString(R.string.menu));
				}
			};

			// Set the drawer toggle as the DrawerListener
			mDrawerLayout.setDrawerListener(mDrawerToggle);

			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	/**
	 * Loads the activity to read our own stories
	 */
	private void callActivityReadStory(){
		Intent intent = new Intent(this, ReadStoryActivity.class);
	    startActivity(intent);
	}

	/**
	 * Loads the activity to read our own stories
	 */
	private void callActivityGui(){
		Intent intent = new Intent(this, BookActivity.class);
	    startActivity(intent);
	}
	
	/**
	 * Loads the activity to build our own stories
	 */
	private void callActivityBuildStory(){
		Intent intent = new Intent(this, BuildPageActivity.class);
	    startActivity(intent);
	}
	@Override
	protected void onStop() {
		super.onStop();
		persistStatusGame();
	}

	/**
	 * Persist current game status
	 */
	private void persistStatusGame() {
		// If we are navigating then we must persist data to DataBase
		// Create a new row of values to insert.

		if (nextPageClass != null && nextPageClass != "") {
			ContentResolver cr = getContentResolver();
			ContentValues newValues = new ContentValues();
			// Assign values for each row.
			newValues.put(Status.ID, 1);
			newValues.put(Status.POINTS, points);
			newValues.put(Status.CURRENTCHAPTER, nextPageClass);
			newValues.put(Status.GAME_ID, 1);
			// Insert the row
			Uri myRowUri = cr.insert(PrototypeProvider.CONTENT_URI_STATUS,
					newValues);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.d("bookactivity", "onDestroy");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d("bookactivity", "onRestart");
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// Construct the new query in the form of a Cursor Loader. Use the id
		// parameter to construct and return different loaders.
		String where = args.getString("selection");
		String[] whereArgs = args.getStringArray("selectionArgs");
		String sortOrder = args.getString("sortOrder");
		String[] projection = args.getStringArray("projection");
		// Query URI
		Uri queryUri = Uri.parse(args.getString("uri"));
		// Uri queryUri = PrototypeProvider.CONTENT_URI_USERS;
		// Create the new Cursor loader.
		return new CursorLoader(BuildPageActivity.this, queryUri, projection,
				where, whereArgs, sortOrder);
	}

	@Override
	public void onLowMemory() {
		Log.d("Prototype-ALERTA", "LOW MEMORY - FAz qq coisa p‡h!");
	}

	private void handleDrawableLoader(Loader<Cursor> loader, Cursor data) {
		int id = data.getColumnIndex(Drawable.ID);
		int name = data.getColumnIndex(Drawable.NAME);

		boolean isFirst = true;

		JourneyItem ji = null;
		if (journeyItemList == null) {
			journeyItemList = new ArrayList<IListItem>();
		}

		journeyItemList.clear();

		while (data.moveToNext()) {
			int dateStr = data.getInt(id);
			String nameStr = data.getString(name);

			getResources().getIdentifier(nameStr, "drawable",
					"com.dipeca.buildstoryactivity");
		}

		journeyArrayAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		switch (tableLoaded) {
		case PrototypeProvider.DRAWABLE_ALLROWS:
			handleDrawableLoader(loader, data);
			// Initiate status loading
			// restartLoaderStatus();

			break;
		case PrototypeProvider.CHAPTER_ALLROWS:

			// Initiate quizz Loading
			// restartLoaderQuizz();

			break;

		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadFragment(Fragment page) {

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.detailFragment, page);
		ft.commit();

	}

	private long storyId;
	private String storyName;

	@Override
	public void setStoryId(long id) {
		storyId = id;

	}

	@Override
	public long getStoryId() {
		return storyId;
	}

	@Override
	public void setStoryName(String name) {
		storyName = name;
		tvStoryTitle.setText(name);
	}

	@Override
	public String getStoryName() {
		return storyName;
	}

	@Override
	public void setJourneyItemList(ArrayList<IListItem> arr) {
		if (journeyItemList == null) {
			journeyItemList = new ArrayList<IListItem>();
		}

		journeyItemList.clear();
		journeyItemList.addAll(arr);
		
		journeyArrayAdapter.notifyDataSetChanged();
		
	}
	
	@Override
	public void setJourneyItem(IListItem ji) {
		journeyItem = ji;
		
	}

	@Override
	public IListItem getJourneyItem() {
		return journeyItem;
	}

	@Override
	public void setSelectedPageIndex(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSelectedPageIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void loadStoryPage(boolean isPrev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLastPage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFirstPage() {
		// TODO Auto-generated method stub
		return false;
	}
}
