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
import android.os.Handler;
import android.os.Message;
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
import com.dipeca.buildstoryactivity.entity.Legend;
import com.dipeca.buildstoryactivity.entity.PageDrawable;
import com.dipeca.item.IListItem;
import com.dipeca.item.JourneyListAdapter;
import com.dipeca.prototype.R;

public class ReadStoryActivity extends Activity implements
		LoaderManager.LoaderCallbacks<Cursor>, IBuildActivity {

	private static Context context;
	public static int playIcon;
	public static ImageButton stopButton;
	private static InputMethodManager imm = null;
	private ArrayList<IListItem> pageObjectList;
	private JourneyListAdapter journeyArrayAdapter;
	private IListItem pageObject;

	private ActionBarDrawerToggle mDrawerToggle;

	private int tableLoaded = 1;

	public static Bitmap bitmapInitial;
	public static Bitmap bitmap1;
	public static Bitmap bitmap2;
	public static Bitmap bitmapTalisma;

	private int selectedPageIndex;
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
		pageObjectList = new ArrayList<IListItem>();

		tvStoryTitle = (TextView) findViewById(R.id.storyTitle);

		// Create the array adapter to bind the array to the listview
		int resID = R.layout.journey_so_far;
		journeyArrayAdapter = new JourneyListAdapter(this, resID,
				pageObjectList, null);
		// Bind the array adapter to the listview.
		journeySoFar.setListAdapter(journeyArrayAdapter);

		pointsText = (TextView) findViewById(R.id.pointsValue);
		// configure the toggle menu
		configureDrawerMenu();

		context = getApplicationContext();

		Fragment fragmentLoaded = new PageLoadStory();

		loadFragment(fragmentLoaded);
	}

	@Override
	public void setJourneyItem(IListItem ji) {
		pageObject = ji;

		restartLoaderDrawablesForPage(pageObject.getId());
	}

	/**
	 * @param idPage
	 */
	public void restartLoaderText(long idPage) {
		String[] projection = null;
		projection = new String[] { Legend.ID, Legend.ORDER, Legend.VALUE };
		makeProviderBundle(projection, Legend.PAGE_ID + "=?",
				new String[] { String.valueOf(idPage) }, Legend.ORDER + " ASC",
				PrototypeProvider.CONTENT_URI_LEGENDS.toString());

		tableLoaded = PrototypeProvider.LEGEND_ALLROWS;

		getLoaderManager().restartLoader(0, myBundle, this);
	}

	/**
	 * Set the Drawables table as main cursor again
	 */
	public void restartLoaderDrawablesForPage(long idPage) {
		String[] projection = null;
		projection = new String[] { Drawable.ID, Drawable.NAME,
				PageDrawable.ORDER };

		String queryPage = "SELECT * FROM " + Drawable.TABLE_NAME
				+ " d INNER JOIN " + PageDrawable.TABLE_NAME + " pd ON pd."
				+ PageDrawable.DRAWABLE_ID + "= d." + PageDrawable.ID
				+ " AND pd." + PageDrawable.PAGE_ID + " = ? ORDER BY "
				+ PageDrawable.ORDER + " ASC";

		tableLoaded = PrototypeProvider.DRAWABLEPAGE_ALLROWS;

		myBundle = new Bundle();
		myBundle.putStringArray("projection", projection);
		myBundle.putString("selection", queryPage);
		myBundle.putStringArray("selectionArgs",
				new String[] { String.valueOf(idPage) });
		myBundle.putString("uri",
				PrototypeProvider.CONTENT_URI_PAGEDRAWABLE.toString());

		getLoaderManager().restartLoader(0, myBundle, this);
	}

	@Override
	public IListItem getJourneyItem() {
		return pageObject;
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
		return new CursorLoader(ReadStoryActivity.this, queryUri, projection,
				where, whereArgs, sortOrder);
	}

	@Override
	public void onLowMemory() {
		Log.d("Prototype-ALERTA", "LOW MEMORY - FAz qq coisa p‡h!");
	}

	private void handleDrawableLoader(Loader<Cursor> loader, Cursor data) {
		int id = data.getColumnIndex(Drawable.ID);
		int name = data.getColumnIndex(Drawable.NAME);
		int order = data.getColumnIndex(PageDrawable.ORDER);

		while (data.moveToNext()) {
			String nameStr = data.getString(name);

			// (PageObject)pageObject
			if (data.getInt(order) == 1) {
				pageObject.setDrawable1Id(getResources().getIdentifier(nameStr,
						"drawable", getPackageName()));
			} else if (data.getInt(order) == 2) {
				pageObject.setDrawable2Id(getResources().getIdentifier(nameStr,
						"drawable", getPackageName()));
			}
		}

	}

	private void handleLegendLoader(Loader<Cursor> loader, Cursor data) {
		int id = data.getColumnIndex(Legend.ID);
		int order = data.getColumnIndex(Legend.ORDER);
		int val = data.getColumnIndex(Legend.VALUE);

		while (data.moveToNext()) {
			if (data.getInt(order) == 1) {
				pageObject.setLegend1Id(data.getString(val));
			} else if (data.getInt(order) == 2) {
				pageObject.setLegend2Id(data.getString(val));
			}
		}

		handlerLoadFrg.sendEmptyMessage(-1);
	}

	private Handler handlerLoadFrg = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			loadFragment(new PageLoadPage());
		}
	};

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		switch (tableLoaded) {
		case PrototypeProvider.DRAWABLEPAGE_ALLROWS:
			handleDrawableLoader(loader, data);
			// Initiate status loading
			restartLoaderText(pageObject.getId());

			break;
		case PrototypeProvider.LEGEND_ALLROWS:

			// Initiate quizz Loading
			handleLegendLoader(loader, data);

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
		if (pageObjectList == null) {
			pageObjectList = new ArrayList<IListItem>();
		}

		pageObjectList.clear();
		pageObjectList.addAll(arr);

		journeyArrayAdapter.notifyDataSetChanged();

	}

	@Override
	public void setPageToDisplay(Fragment page) {
		loadFragment(page);

	}

	@Override
	public void setSelectedPageIndex(int index) {
		selectedPageIndex = index;

	}

	@Override
	public int getSelectedPageIndex() {
		return selectedPageIndex;
	}

	@Override
	public void loadStoryPage(boolean isPrev) {
		IListItem ji = null;
		if (isPrev && selectedPageIndex > 0) {
			
			setSelectedPageIndex(selectedPageIndex - 1);
			ji = pageObjectList.get(getSelectedPageIndex());

		} else if (!isPrev && selectedPageIndex < pageObjectList.size() - 1) {
			
			setSelectedPageIndex(selectedPageIndex + 1);
			ji = pageObjectList.get(getSelectedPageIndex());
		}

		if (ji != null) {
			setJourneyItem(ji);
		}
	}

	/* (non-Javadoc)
	 * @see com.dipeca.buildstoryactivity.IBuildActivity#isLastPage()
	 */
	@Override
	public boolean isLastPage() {
		if(pageObjectList == null){
			return false;
		}
		return getSelectedPageIndex() == pageObjectList.size()  - 1;
	}
	
	/* (non-Javadoc)
	 * @see com.dipeca.buildstoryactivity.IBuildActivity#isFirstPage()
	 */
	@Override
	public boolean isFirstPage() {
		if(pageObjectList == null){
			return false;
		}
		return getSelectedPageIndex() == 0;
	}

}
