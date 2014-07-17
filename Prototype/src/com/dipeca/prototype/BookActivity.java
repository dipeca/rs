package com.dipeca.prototype;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dipeca.prototype.entiy.Chapter;
import com.dipeca.prototype.entiy.Objects;
import com.dipeca.prototype.entiy.QuestionAnswer;
import com.dipeca.prototype.entiy.Status;
import com.dipeca.prototype.entiy.User;

public class BookActivity extends Activity implements IMainActivity,
		LoaderManager.LoaderCallbacks<Cursor> {

	@Override
	public void restartApp() {

		ContentResolver cr = getContentResolver();

		// Delete all rows
		points = 80;
		cr.delete(PrototypeProvider.CONTENT_URI_CHAPTERS, null, null);

		if (journeyItemList == null) {
			journeyItemList = new ArrayList<JourneyItem>();
		} else {
			journeyItemList.clear();
		}
		journeyArrayAdapter.notifyDataSetChanged();

		cr.delete(PrototypeProvider.CONTENT_URI_OBJECTS, null, null);
		restartLoaderObjects();
	}

	// mediaPlayer-object will not we cleaned away since someone holds a
	// reference to it!
	private static MediaPlayer mediaPlayer;
	private static Context context;
	private static int idToPlay = -1;
	public static int playIcon;
	private static boolean isStopped = false;
	public static ImageButton stopButton;
	private static InputMethodManager imm = null;
	private ArrayList<JourneyItem> journeyItemList;
	private JourneyListAdapter journeyArrayAdapter;

	private ArrayList<ObjectItem> objectItemList;
	private ObjectItemImageAdapter objectsAdapter;

	private ActionBarDrawerToggle mDrawerToggle;

	private int tableLoaded = 1;

	GridView objectsGridView;

	public static Bitmap bitmapInitial;
	public static Bitmap bitmap1;
	public static Bitmap bitmap2;
	public static Bitmap bitmapTalisma;

	private Bundle myBundle = null;

	private DrawerLayout mDrawerLayout;

	Display display;
	Point sizeofDisplay = new Point();

	// Menu Points
	public static int points = 100;
	private TextView pointsText = null;

	@Override
	protected void onPause() {
		super.onPause();

		stopMusic();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		display = getWindowManager().getDefaultDisplay();
		display.getSize(sizeofDisplay);
		Log.d("SIZE: ", " " + sizeofDisplay.x + "x" + sizeofDisplay.y);

		// However, if we're being restored from a previous state,
		// then we don't need to do anything and should return or else
		// we could end up with overlapping fragments.
		// if (savedInstanceState != null) {
		// Log.d("savedInstanceState",
		// "savedInstanceState is not null so we don't perform action");
		// // configure the toggle menu
		// configureDrawerMenu();
		// return;
		// }

		// Get references to the Fragments
		FragmentManager fm = getFragmentManager();
		JourneySoFar journeySoFar = (JourneySoFar) fm
				.findFragmentById(R.id.journey);

		// Create the array list of to do items
		journeyItemList = new ArrayList<JourneyItem>();

		// Create the array adapter to bind the array to the listview
		int resID = R.layout.journey_so_far;
		journeyArrayAdapter = new JourneyListAdapter(this, resID,
				journeyItemList);
		// Bind the array adapter to the listview.
		journeySoFar.setListAdapter(journeyArrayAdapter);

		// Objects
		objectItemList = new ArrayList<ObjectItem>();

		objectsGridView = (GridView) findViewById(R.id.gridView1);
		objectsAdapter = new ObjectItemImageAdapter(this, objectItemList);
		objectsGridView.setAdapter(objectsAdapter);

		objectsGridView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						Log.d("bookactivity", "OnItemClick");
						if (ivObjects != null) {
							((ViewGroup) ivObjects.getParent())
									.removeView(ivObjects);
						}
						ivObjects = new ImageView(arg1.getContext());
						ivObjects.setBackgroundResource(R.drawable.back);

						RelativeLayout layout = (RelativeLayout) nextPage
								.getView();
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
								256, 256);
						params.addRule(RelativeLayout.CENTER_IN_PARENT);

						layout.addView(ivObjects, params);

						ObjectItem oi = (ObjectItem) objectsAdapter
								.getItem(arg2);
						ivObjects.setImageBitmap(oi.getBitmap());

						ivObjects
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View arg0) {
										arg0.setVisibility(View.GONE);

									}
								});
					}
				});

		pointsText = (TextView) findViewById(R.id.pointsValue);
		// configure the toggle menu
		configureDrawerMenu();

		context = getApplicationContext();

		loadDataBase();
	}

	private static ImageView ivObjects = null;

	private void loadDataBase() {
		String[] projection = new String[] { User.ID, User.NAME, User.AGE };
		makeProviderBundle(projection, null, null, null,
				PrototypeProvider.CONTENT_URI_USERS.toString());
		tableLoaded = PrototypeProvider.USER_ALLROWS;
		getLoaderManager().initLoader(0, myBundle, BookActivity.this);

	}

	public static void playMusic(int id) {

		createMP(id);

		if (mediaPlayer != null) {
			mediaPlayer.setLooping(true);
			play();
		}
	}

	public static void playMusicOnce(int id) {
		createMP(id);
		mediaPlayer.setLooping(false);
		play();
	}

	private static void createMP(int id) {
		if (id > -1) {
			stopMusic();
			idToPlay = id;
			mediaPlayer = MediaPlayer.create(context, id);
		}
	}

	private static void play() {

		mediaPlayer.start();
		// Set the icon as stop because the music is now playing
		playIcon = R.drawable.ic_action_stop;
		setStopIcon();
	}

	private static void setStopIcon() {
		if (stopButton != null) {
			stopButton.setImageResource(playIcon);
		}
	}

	public static void stopMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
			// Set the icon as play because the music is now stopped
			playIcon = R.drawable.ic_action_play;
			setStopIcon();
		}
	}

	public static void stopOrPlayMusic() {
		if (mediaPlayer != null) {
			stopMusic();
			isStopped = true;
		} else if (idToPlay > -1) {
			playMusic(idToPlay);
			isStopped = false;
		}
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Fragment nextPage = null;
	private String nextPageName = null;
	private String nextPageClass = null;
	private String iconPath = "quarto_olhar_talisma_icon";

	@Override
	public void onChoiceMade(Fragment frg, String currentPage, String iconPath) {
		nextPage = frg;
		nextPageName = currentPage;
		nextPageClass = frg.getClass().getName();
		if (iconPath != null) {
			this.iconPath = iconPath;
		} else {
			this.iconPath = "quarto_olhar_talisma_icon";
		}
	}

	@Override
	public void onChoiceMade(Fragment frg, String currentPage) {
		nextPage = frg;
		nextPageName = currentPage;
		nextPageClass = frg.getClass().getName();
		this.iconPath = "quarto_olhar_talisma_icon";
	}

	@Override
	protected void onStop() {
		super.onStop();

		stopMusic();

		persistStatusGame();
		Log.d("bookactivity", "onStop");

		//
		// if (nextPageName != null && nextPageName.length() > 0) { //if the
		// next page is not null
		// if (!isInJourney(nextPageName)) { // if the nextpage to be loaded is
		// not already on the list
		// persistJourney(nextPageName);
		// }
		// }
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

		stopMusic();

		Log.d("bookactivity", "onDestroy");
	}

	@Override
	public void onChoiceMadeCommitFirstPage(String namePreviousPage,
			Boolean isToPersist) {
		if (nextPage != null) {

			imm.hideSoftInputFromWindow(pointsText.getWindowToken(), 0);

			if (isToPersist) {
				addItemToJourney(nextPageName);
			}
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d("bookactivity", "onRestart");
	}

	@Override
	public void onChoiceMadeCommit(String namePreviousPage, Boolean isToPersist) {
		if (nextPage != null) {

			stopMusic();

			imm.hideSoftInputFromWindow(pointsText.getWindowToken(), 0);

			// Load the next Page of the story
			loadNextPage();
			if (isToPersist) {
				addItemToJourney(nextPageName);
			}
		}
	}

	@Override
	public void objectFoundPersist(ObjectItem oi) {
		// Only if object have not already been found
		if (!isInObjects(oi)) {
			ContentResolver cr = getContentResolver();
			ContentValues newValues = new ContentValues();

			// Assign values for each row.
			newValues.put(Objects.TYPE, oi.getObjectImageType());
			newValues.put(Objects.IS_SHOW, true);
			newValues.put(Objects.NAME, oi.getName());

			// Insert the row
			Uri myRowUri = cr.insert(PrototypeProvider.CONTENT_URI_OBJECTS,
					newValues);

			// restart objects adapter
			restartLoaderObjects();
		}
	}

	/**
	 * This function will persist the chapter we are about to read
	 * 
	 * @param namePreviousPage
	 */
	private void persistJourney(String namePreviousPage) {
		// If we are navigating then we must persist data to DataBase
		// Create a new row of values to insert.
		ContentResolver cr = getContentResolver();
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		newValues.put(Chapter.CURRENT, nextPageName);
		newValues.put(Chapter.DATE, dateFormat.format(date));
		newValues.put(Chapter.PREVIOUS_CHAPTER_ID, namePreviousPage);
		newValues.put(Chapter.CURRENT_CLASS, nextPageClass);
		newValues.put(Chapter.ICON_PATH, this.iconPath);
		// Insert the row
		Uri myRowUri = cr.insert(PrototypeProvider.CONTENT_URI_CHAPTERS,
				newValues);
	}

	/**
	 * Load next page to be shown
	 */
	private void loadNextPage() {
		if (nextPage != null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			// ft.setCustomAnimations(R.anim.slide_in_left,
			// R.anim.right_to_left);
			// ft.setBreadCrumbShortTitle(nextPageName);
			ft.replace(R.id.detailFragment, nextPage);

			// I'm wondering if i should remove the backstack option to force
			// the fragment destroy method to be called
			// ft.addToBackStack("previousPag");
			ft.commitAllowingStateLoss();
		}
	}

	/**
	 * Add the new chapter/page of the book to the journeylist
	 * 
	 * @param chapterName
	 */
	private void addItemToJourney(String chapterName) {
		JourneyItem ji = new JourneyItem();
		ji.setId(null);
		ji.setCurrent(chapterName);
		ji.setCurrentClass(nextPageClass);
		int iconId = getResources().getIdentifier(iconPath, "drawable",
				getPackageName());
		ji.setIcon(getResources().getDrawable(iconId));

		if (journeyItemList == null) {
			journeyItemList = new ArrayList<JourneyItem>();
		}

		// if not already on journey path
		if (!isInJourney(chapterName)) {
			journeyItemList.add(0, ji);
			persistJourney(chapterName);

			// handle Points; We only add points if we didn't passe here already
			setAddPoints(20);
		}
		journeyArrayAdapter.notifyDataSetChanged();
	}

	private boolean isInJourney(String chapterName) {
		if (journeyItemList == null || journeyItemList.size() == 0) {
			return false;
		}

		for (JourneyItem ji : journeyItemList) {
			if (ji.getCurrent().equals(chapterName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isInObjects(ObjectItem oi) {
		if (objectItemList == null || objectItemList.size() == 0) {
			return false;
		}

		for (ObjectItem oiInList : objectItemList) {
			if (oiInList.getObjectImageType() == oi.getObjectImageType()) {
				return true;
			}
		}
		return false;
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
		return new CursorLoader(BookActivity.this, queryUri, projection, where,
				whereArgs, sortOrder);
	}

	private void handleObjectCursor(Loader<Cursor> loader, Cursor data) {
		int id = data.getColumnIndex(Objects.ID);
		int name = data.getColumnIndex(Objects.NAME);
		int path = data.getColumnIndex(Objects.IMAGE_PATH);
		int isShow = data.getColumnIndex(Objects.IS_SHOW);
		int gameId = data.getColumnIndex(Objects.GAME_ID);
		int type = data.getColumnIndex(Objects.TYPE);

		ObjectItem oi = null;
		objectItemList.clear();

		while (data.moveToNext()) {
			oi = new ObjectItem(data.getLong(id), data.getString(name),
					data.getInt(type), null);
			objectItemList.add(oi);
		}

		objectsAdapter.notifyDataSetChanged();
		objectsGridView.invalidateViews();

		objectsGridView.setAdapter(objectsAdapter);
	}

	private void handleQuizzCursor(Loader<Cursor> loader, Cursor data) {
		int id = data.getColumnIndex(QuestionAnswer.ID);
		int text = data.getColumnIndex(QuestionAnswer.TEXT);
		int enigmaLike = data.getColumnIndex(QuestionAnswer.ENIGMA_LIKE);
		int enigmaDislike = data.getColumnIndex(QuestionAnswer.ENIGMA_DISLIKE);
		int points = data.getColumnIndex(QuestionAnswer.POINTS);
		int path = data.getColumnIndex(QuestionAnswer.PATH);
		int likeIt = data.getColumnIndex(QuestionAnswer.LIKEIT);
		int reads = data.getColumnIndex(QuestionAnswer.READS);
		int type = data.getColumnIndex(QuestionAnswer.TYPE);

		String textStr = null;
		String enigmaLikeStr = null;
		String enigmaDislikeStr = null;
		String pointsStr = null;
		String typeStr = null;
		String pathStr = null;
		String likeItStr = null;
		String readsStr = null;

		while (data.moveToNext()) {
			textStr = data.getString(text);
			enigmaLikeStr = data.getString(enigmaLike);
			enigmaDislikeStr = data.getString(enigmaDislike);
			pointsStr = data.getString(points);
			typeStr = data.getString(type);
			pathStr = data.getString(path);
			likeItStr = data.getString(likeIt);
			readsStr = data.getString(reads);

			Log.d("BookActivity -> ", "Text: " + textStr + "; Enigma Fav: "
					+ enigmaLikeStr + "; Enigma n‹o gosta:  "
					+ enigmaDislikeStr + "; Pontos: " + pointsStr
					+ "; Escolher caminhos: " + pathStr + "; Gosta da app: "
					+ likeItStr + "; Gosta de ler: " + readsStr
					+ "; Tipo de livro que gosta: " + typeStr);

		}

	}

	@Override
	public void onLowMemory() {
		Log.d("Prototype-ALERTA", "LOW MEMORY - FAz qq coisa p‡h!");
	}

	private void handleStatusCursor(Loader<Cursor> loader, Cursor data) {
		int id = data.getColumnIndex(Status.ID);
		int gameId = data.getColumnIndex(Status.GAME_ID);
		int points = data.getColumnIndex(Status.POINTS);
		int currentChapter = data.getColumnIndex(Status.CURRENTCHAPTER);

		String name = null;
		while (data.moveToNext()) {
			Fragment current = getFragmentFromClassname(data
					.getString(currentChapter));
			if (current != null) {
				name = getNameFromFragment(current);
				// Go to next page
				onChoiceMade(current, name);
				// onChoiceMadeCommit(name, false);
				// Load the next Page of the story
				loadNextPage();

				int currentPoints = data.getInt(points);
				setPoints(currentPoints);
			}
		}

		if (name == null || "".equals(name)) {
			handler.sendEmptyMessage(0);
		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Check that the activity is using the layout version with
			// the detailFragment FrameLayout
			if (findViewById(R.id.detailFragment) != null) {

				// Create a new Fragment to be placed in the activity layout
				// PagBedRoomfrg firstFragment = new PagBedRoomfrg();
				PagBedRoomDarkfrg firstFragment = new PagBedRoomDarkfrg();

				// In case this activity was started with special instructions
				// from
				// an Intent, pass the Intent's extras to the fragment as
				// arguments
				firstFragment.setArguments(getIntent().getExtras());

				// Add the fragment to the 'detailFragment' FrameLayout
				getFragmentManager().beginTransaction()
						.add(R.id.detailFragment, firstFragment).commit();
			}
		}
	};

	private void handleChapterCursor(Loader<Cursor> loader, Cursor data) {
		int id = data.getColumnIndex(Chapter.ID);
		int cur = data.getColumnIndex(Chapter.CURRENT);
		int currClass = data.getColumnIndex(Chapter.CURRENT_CLASS);
		int date = data.getColumnIndex(Chapter.DATE);
		int pre = data.getColumnIndex(Chapter.PREVIOUS_CHAPTER_ID);
		int icon = data.getColumnIndex(Chapter.ICON_PATH);
		boolean isFirst = true;

		JourneyItem ji = null;
		if (journeyItemList == null) {
			journeyItemList = new ArrayList<JourneyItem>();
		}

		journeyItemList.clear();

		while (data.moveToNext()) {
			String dateStr = data.getString(date);
			Date dateDt;
			try {
				dateDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(dateStr);
			} catch (ParseException e) {
				dateDt = null;
			}

			String iconName = data.getString(icon);
			int iconID = getResources().getIdentifier(iconName, "drawable",
					getPackageName());

			Drawable iconDr = getResources().getDrawable(iconID);

			ji = new JourneyItem(data.getLong(id), data.getString(cur),
					data.getString(currClass), dateDt, data.getString(pre),
					iconDr, iconName);

			journeyItemList.add(ji);

			isFirst = false;
		}

		journeyArrayAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		String[] projection = null;
		switch (tableLoaded) {
		case PrototypeProvider.USER_ALLROWS:

			int name = data.getColumnIndex(User.NAME);
			int age = data.getColumnIndex(User.AGE);
			int id = data.getColumnIndex(User.ID);

			// Initiate status loading
			restartLoaderStatus();

			break;
		case PrototypeProvider.STATUS_ALLROWS:
			handleStatusCursor(loader, data);

			// Initiate quizz Loading
			restartLoaderChapter();

			break;
		case PrototypeProvider.CHAPTER_ALLROWS:
			handleChapterCursor(loader, data);

			// Initiate quizz Loading
			restartLoaderQuizz();

			break;
		case PrototypeProvider.OBJECT_ALLROWS:
			handleObjectCursor(loader, data);
			// aa.notifyDataSetChanged();
			break;

		case PrototypeProvider.QUIZZ_ALLROWS:
			handleQuizzCursor(loader, data);

			// Initiate Objects Loading
			restartLoaderObjects();

		}

	}

	/**
	 * Set the Objects table as main cursor again
	 */
	@Override
	public void restartLoaderObjects() {
		String[] projection = null;
		projection = new String[] { Objects.ID, Objects.NAME,
				Objects.IMAGE_PATH, Objects.IS_SHOW, Objects.GAME_ID,
				Objects.TYPE };
		makeProviderBundle(projection, null, null, null,
				PrototypeProvider.CONTENT_URI_OBJECTS.toString());
		tableLoaded = PrototypeProvider.OBJECT_ALLROWS;
		getLoaderManager().restartLoader(0, myBundle, BookActivity.this);
	}

	/**
	 * Set the Quizz table as main cursor again
	 */
	public void restartLoaderQuizz() {
		String[] projection = null;
		projection = new String[] { QuestionAnswer.ID, QuestionAnswer.DRAW,
				QuestionAnswer.ENIGMA_DISLIKE, QuestionAnswer.ENIGMA_LIKE,
				QuestionAnswer.LIKEIT, QuestionAnswer.PATH,
				QuestionAnswer.POINTS, QuestionAnswer.READS,
				QuestionAnswer.TEXT, QuestionAnswer.TYPE, };

		makeProviderBundle(projection, null, null, null,
				QuestionAnswer.CONTENT_URI.toString());
		tableLoaded = PrototypeProvider.QUIZZ_ALLROWS;
		getLoaderManager().restartLoader(0, myBundle, BookActivity.this);
	}

	/**
	 * Set the Chapter table as main cursor again
	 */
	private void restartLoaderChapter() {
		String[] projection = null;

		projection = new String[] { Chapter.ID, Chapter.CURRENT, Chapter.DATE,
				Chapter.PREVIOUS_CHAPTER_ID, Chapter.CURRENT_CLASS,
				Chapter.ICON_PATH };
		makeProviderBundle(projection, null, null, Chapter.DATE + " DESC",
				PrototypeProvider.CONTENT_URI_CHAPTERS.toString());
		tableLoaded = PrototypeProvider.CHAPTER_ALLROWS;
		getLoaderManager().restartLoader(0, myBundle, BookActivity.this);

	}

	/**
	 * Set the Status table as main cursor again
	 */
	private void restartLoaderStatus() {
		String[] projection = null;

		projection = new String[] { Status.ID, Status.GAME_ID, Status.POINTS,
				Status.CURRENTCHAPTER };
		makeProviderBundle(projection, null, null, Status.ID + " DESC",
				PrototypeProvider.CONTENT_URI_STATUS.toString());
		tableLoaded = PrototypeProvider.STATUS_ALLROWS;
		getLoaderManager().restartLoader(0, myBundle, BookActivity.this);

	}

	@Override
	public Fragment getFragmentFromJourneyItem(JourneyItem ji) {

		return getFragmentFromClassname(ji.getCurrentClass());
	}

	private Fragment getFragmentFromClassname(String className) {
		Fragment frg = null;

		Class c = null;
		try {
			c = Class.forName(className);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			frg = (Fragment) c.newInstance();
		} catch (java.lang.InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			Log.d("Class instantiation of", "currentClass: " + className);
		}

		return frg;

	}

	@Override
	public String getNameFromFragment(Fragment frg) {

		Field f = null;
		try {
			f = frg.getClass().getField("NAME");

		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String name = null;
		try {
			name = getString((Integer) f.get(f));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return name;
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub

	}

	private void updateStatus(Status status) {

	}

	@Override
	public void setAddPoints(int points) {

		if (BookActivity.points >= 0 || points > 0) {
			BookActivity.points += points;
		}
		
		pointsText.setText(BookActivity.points + "");

	}

	@Override
	public void setPoints(int points) {
		BookActivity.points = points;

		pointsText.setText(BookActivity.points + "");

	}

	// --------------------------
	// ------ Quizz handler
	// --------------------------
	ContentValues newValuesQuizz = new ContentValues();

	/**
	 * Handle the drawing radio button question
	 * 
	 * @param v
	 */
	public void onRadioButtonDrawClicked(View v) {
		// Is the button now checked?
		boolean checked = ((RadioButton) v).isChecked();
		// Check which radio button was clicked
		switch (v.getId()) {
		case R.id.rb_desenhos_gosto_muito:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.DRAW, "Gosto muito");

			}
			break;
		case R.id.rb_desenhos_gosto:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.DRAW, "Gosto");
			}
			break;
		case R.id.rb_desenhos_adultos:
			if (checked)
				if (checked) {

					// Assign values for each row.
					newValuesQuizz.put(QuestionAnswer.DRAW, "Mto sŽrios");
				}
			break;
		case R.id.rb_desenhos_crianca:
			if (checked)
				if (checked) {

					// Assign values for each row.
					newValuesQuizz.put(QuestionAnswer.DRAW, "Mto infantis");
				}
			break;

		}
	}

	/**
	 * Handle the text radio button question
	 * 
	 * @param v
	 */
	public void onRadioButtonTextClicked(View v) {
		// Is the button now checked?
		boolean checked = ((RadioButton) v).isChecked();

		// Check which radio button was clicked
		switch (v.getId()) {
		case R.id.rb_text_ok:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.TEXT, "Texto OK");

			}
			break;
		case R.id.rb_text_not_enough:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.TEXT, "Tem pouco");

			}
			break;
		case R.id.rb_text_to_much:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.TEXT, "Tem mto");

			}
			break;

		}
	}

	/**
	 * Handle the text radio button question
	 * 
	 * @param v
	 */
	public void onRadioButtonLikeItClicked(View v) {
		// Is the button now checked?
		boolean checked = ((RadioButton) v).isChecked();

		// Check which radio button was clicked
		switch (v.getId()) {
		case R.id.rb_gosto_mais_ou_menos:
			if (checked) {

				newValuesQuizz.put(QuestionAnswer.LIKEIT,
						QuestionAnswer.LIKE_IT_SOSO);
			}
			break;
		case R.id.rb_gosto_no:
			if (checked) {

				newValuesQuizz.put(QuestionAnswer.LIKEIT,
						QuestionAnswer.LIKE_IT_NO);

			}
			break;
		case R.id.rb_gosto_sim_mto:
			if (checked) {
				newValuesQuizz.put(QuestionAnswer.LIKEIT,
						QuestionAnswer.LIKE_IT_VERY);

			}
			break;

		}
	}

	/**
	 * Handle the riddle radio button question
	 * 
	 * @param v
	 */
	public void onRadioButtonRiddleLikeClicked(View v) {
		// Is the button now checked?
		boolean checked = ((RadioButton) v).isChecked();

		// Check which radio button was clicked
		switch (v.getId()) {
		case R.id.rb_enigma_linhas:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.ENIGMA_LIKE,
						"Enigma: gosto linhas");

			}
			break;
		case R.id.rb_enigma_maths:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.ENIGMA_LIKE,
						"Enigma: gosto maths");

			}
			break;
		case R.id.rb_enigma_screen:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.ENIGMA_LIKE,
						"Enigma: gosto ecran");

			}
			break;
		case R.id.rb_enigma_todos:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.ENIGMA_LIKE,
						"Enigma: gosto todos");

			}
			break;
		}
	}

	/**
	 * Handle the riddle dislike radio button question
	 * 
	 * @param v
	 */
	public void onRadioButtonRiddleDislikeClicked(View v) {
		// Is the button now checked?
		boolean checked = ((RadioButton) v).isChecked();

		// Check which radio button was clicked
		switch (v.getId()) {
		case R.id.rb_not_enigma_linhas:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.ENIGMA_DISLIKE,
						"N‹o gosto linhas");

			}
			break;
		case R.id.rb_not_enigma_maths:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.ENIGMA_DISLIKE,
						"N‹o gosto maths");

			}
			break;
		case R.id.rb_not_enigma_screen:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.ENIGMA_DISLIKE,
						"N‹o gosto ecran");

			}
			break;
		}
	}

	/**
	 * Handle the points radio button question
	 * 
	 * @param v
	 */
	public void onRadioButtonPointsClicked(View v) {
		// Is the button now checked?
		boolean checked = ((RadioButton) v).isChecked();

		// Check which radio button was clicked
		switch (v.getId()) {
		case R.id.rb_points_bad:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.POINTS, "Pontos: n‹o gosto");

			}
			break;
		case R.id.rb_points_compare:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.POINTS, "Pontos: motiva");

			}
			break;
		case R.id.rb_points_not_important:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.POINTS,
						"Pontos: nem aquece nem arrefece");

			}
			break;
		}
	}

	/**
	 * Handle the type of books question
	 * 
	 * @param v
	 */
	public void onRadioButtonTypeClicked(View v) {
		// Is the button now checked?
		boolean checked = ((RadioButton) v).isChecked();

		// Check which radio button was clicked
		switch (v.getId()) {
		case R.id.rb_mistery:
			if (checked) {
				newValuesQuizz.put(QuestionAnswer.TYPE,
						QuestionAnswer.TYPE_MISTERY);

			}
			break;
		case R.id.rb_aventura:
			if (checked) {
				newValuesQuizz.put(QuestionAnswer.TYPE,
						QuestionAnswer.TYPE_ADVENTURE);

			}
			break;
		case R.id.rb_fantasia:
			if (checked) {
				newValuesQuizz.put(QuestionAnswer.TYPE,
						QuestionAnswer.TYPE_FANTASIA);

			}
			break;
		case R.id.rb_rir:
			if (checked) {
				newValuesQuizz
						.put(QuestionAnswer.TYPE, QuestionAnswer.TYPE_RIR);

			}
			break;
		case R.id.rb_all:
			if (checked) {
				newValuesQuizz
						.put(QuestionAnswer.TYPE, QuestionAnswer.TYPE_ALL);

			}
			break;
		}
	}

	/**
	 * Handle the path radio button question
	 * 
	 * @param v
	 */
	public void onRadioButtonPathClicked(View v) {
		// Is the button now checked?
		boolean checked = ((RadioButton) v).isChecked();

		// Check which radio button was clicked
		switch (v.getId()) {
		case R.id.rb_path_bah:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.PATH,
						"Escolha caminhos: n‹o gosto");

			}
			break;
		case R.id.rb_path_not_important:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.PATH,
						"Escolha caminhos: nem aquece nem arrefece");

			}
			break;
		case R.id.rb_path_ok:
			if (checked) {

				// Assign values for each row.
				newValuesQuizz.put(QuestionAnswer.PATH,
						"Escolha caminhos: Ž porreiro");

			}
			break;
		}
	}

	/**
	 * Handle the quantity of lecture radio button question
	 * 
	 * @param v
	 */
	public void onRadioButtonReadClicked(View v) {
		// Is the button now checked?
		boolean checked = ((RadioButton) v).isChecked();

		// Check which radio button was clicked
		switch (v.getId()) {
		case R.id.rb_leio_mto:
			if (checked) {
				newValuesQuizz.put(QuestionAnswer.READS,
						QuestionAnswer.READ_STRONG_READER);
			}
			break;
		case R.id.rb_leio_as_vezes:
			if (checked) {
				newValuesQuizz.put(QuestionAnswer.READS,
						QuestionAnswer.READ_OCASION);
			}
			break;
		case R.id.rb_leio_pouco:
			if (checked) {
				newValuesQuizz.put(QuestionAnswer.READS,
						QuestionAnswer.READ_NOT);
			}
			break;
		}
	}

	/**
	 * Handle the quantity of lecture radio button question
	 * 
	 * @param v
	 */
	public void onRadioButtonOwnTabletClicked(View v) {
		// Is the button now checked?
		boolean checked = ((RadioButton) v).isChecked();

		// Check which radio button was clicked
		switch (v.getId()) {
		case R.id.rb_sim:
			if (checked) {
				newValuesQuizz.put(QuestionAnswer.OWN_TABLET, "yes");
			}
			break;
		case R.id.rb_nao:
			if (checked) {
				newValuesQuizz.put(QuestionAnswer.OWN_TABLET, "no");
			}
			break;
		}
	}

	public void handleQuizzSubmit() {
		ContentResolver cr = getContentResolver();

		// Insert the row
		cr.insert(QuestionAnswer.CONTENT_URI, newValuesQuizz);

	}

	@Override
	public void onChoiceMade(Fragment nextPag, int currentPage, int iconPath) {

		String iconPathStr = null;

		if (iconPath > -1) {
			iconPathStr = getResources().getResourceName(iconPath);
		}
		onChoiceMade(nextPag, getString(currentPage), iconPathStr);

	}

	@Override
	public void onChoiceMadeCommit(String namePreviousPage,
			Boolean isToPersist, Boolean isToStopMusic) {

		if (isToStopMusic) {
			stopMusic();
		}

		onChoiceMadeCommit(namePreviousPage, isToPersist);

	}

	@Override
	public void onChoiceMade(Fragment nextPag, int currentPage) {
		onChoiceMade(nextPag, getString(currentPage));

	}

	@Override
	public void onChoiceMadeCommit(int namePreviousPage, Boolean isToPersist) {
		onChoiceMadeCommit(getString(namePreviousPage), isToPersist);

	}

}
