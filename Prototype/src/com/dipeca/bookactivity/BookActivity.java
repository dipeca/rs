package com.dipeca.bookactivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
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
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.dipeca.bookactivity.entiy.Chapter;
import com.dipeca.bookactivity.entiy.Objects;
import com.dipeca.bookactivity.entiy.QuestionAnswer;
import com.dipeca.bookactivity.entiy.Status;
import com.dipeca.bookactivity.entiy.User;
import com.dipeca.buildstoryactivity.BuildPageActivity;
import com.dipeca.buildstoryactivity.ReadStoryActivity;
import com.dipeca.item.IListItem;
import com.dipeca.item.IMainActivity;
import com.dipeca.item.JourneyItem;
import com.dipeca.item.JourneyListAdapter;
import com.dipeca.item.JourneySoFar;
import com.dipeca.item.ObjectItem;
import com.dipeca.item.ObjectItemImageAdapter;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.plus.PlusShare;

/**
 * @author dipeca
 * 
 */
public class BookActivity extends Activity implements IMainActivity,
		LoaderManager.LoaderCallbacks<Cursor> {

	// mediaPlayer-object will not we cleaned away since someone holds a
	// reference to it!
	private static MediaPlayer mediaPlayer;
	private static Context context;
	private static int idToPlay = -1;
	public static int playIcon;
	public static ImageButton stopButton;
	private float density = 1;
	private static InputMethodManager imm = null;
	private ArrayList<IListItem> journeyItemList;
	private JourneyListAdapter journeyArrayAdapter;

	private ArrayList<ObjectItem> objectItemList;
	private ObjectItemImageAdapter objectsAdapter;
	private ShareActionProvider mShareActionProvider;

	private ActionBarDrawerToggle mDrawerToggle;

	private int tableLoaded = 1;

	GridView objectsGridView;

	public static Bitmap bitmapInitial;
	public static Bitmap bitmap1;
	public static Bitmap bitmap2;
	public static Bitmap bitmapTalisma;

	private static ImageButton iBtnMap = null;
	private static ImageView ivObjects = null;

	private Bundle myBundle = null;

	private DrawerLayout mDrawerLayout;

	Display display;
	Point sizeofDisplay = new Point();

	// Menu Points
	public static int points = 80;
	private TextView pointsText = null;
	private static boolean isMusicMuted = false;

	// facebook helper
	private UiLifecycleHelper uiFacebookHelper;

	private String currentMapPosition = null;
	private Drawable map;

	private BitmapFactory.Options mBitmapOptions;
	private Bitmap mCurrentBitmap = null;

	@Override
	public Bitmap decodeSampledBitmapFromResourceBG(Resources res, int resId,
			int reqWidth, int reqHeight) {

		//resId = R.drawable.ic_launcher;
		reqWidth = (int) Math.ceil(0.9 * reqWidth);
		reqHeight = (int) Math.ceil(0.9 * reqHeight);

		// / 

		if (mCurrentBitmap == null) {
			// Create bitmap to be re-used, based on the size of one of the
			// bitmaps
			mBitmapOptions = new BitmapFactory.Options();
			mBitmapOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(getResources(), resId, mBitmapOptions);
			mCurrentBitmap = Bitmap.createBitmap(mBitmapOptions.outWidth,
					mBitmapOptions.outHeight, Bitmap.Config.ARGB_8888);
			mBitmapOptions.inJustDecodeBounds = false;
			mBitmapOptions.inBitmap = mCurrentBitmap;
			mBitmapOptions.inSampleSize = 1;
		}  

		mBitmapOptions.inBitmap = mCurrentBitmap;
		BitmapFactory.decodeResource(getResources(), resId, mBitmapOptions);

		return mCurrentBitmap;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiFacebookHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback() {
					@Override
					public void onError(FacebookDialog.PendingCall pendingCall,
							Exception error, Bundle data) {
						Log.e("Activity",
								String.format("Error: %s", error.toString()));
					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data) {
						Log.i("Activity", "Success!");
					}
				});
	}

	@Override
	public void restartApp() {

		ContentResolver cr = getContentResolver();

		// Delete all rows
		points = 80;
		cr.delete(PrototypeProvider.CONTENT_URI_CHAPTERS, null, null);

		restartItemJourneyList();
		journeyArrayAdapter.notifyDataSetChanged();

		cr.delete(PrototypeProvider.CONTENT_URI_OBJECTS, null, null);
		restartLoaderObjects();
	}

	@Override
	protected void onPause() {
		super.onPause();

		releaseMusic();

		uiFacebookHelper.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		density = (float) getResources().getDisplayMetrics().density;

		display = getWindowManager().getDefaultDisplay();
		display.getSize(sizeofDisplay);
		Log.d("SIZE: ", " " + sizeofDisplay.x + "x" + sizeofDisplay.y);

		// Get references to the Fragments
		FragmentManager fm = getFragmentManager();
		JourneySoFar journeySoFar = (JourneySoFar) fm
				.findFragmentById(R.id.journey);

		// Create the array list of to do items
		restartItemJourneyList();

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

						addViewToScreen();
						ObjectItem oi = (ObjectItem) objectsAdapter
								.getItem(arg2);
						ivObjects.setImageBitmap(oi.getBitmap());

					}
				});

		// setPoints text
		pointsText = (TextView) findViewById(R.id.pointsValue);
		addPoints(0);

		// configure the toggle menu
		configureDrawerMenu();

		context = getApplicationContext();

		try {
			Log.d("package name", this.getPackageName());
			PackageInfo info = getPackageManager().getPackageInfo(
					this.getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
			Log.d("NameNotFoundException:", e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			Log.d("NoSuchAlgorithmException:", e.getMessage());
		}

		// Facebook helper instance
		uiFacebookHelper = new UiLifecycleHelper(this, null);
		uiFacebookHelper.onCreate(savedInstanceState);

	}

	private void addViewToScreen(boolean isFullScreen) {

		if (ivObjects != null) {
			((ViewGroup) ivObjects.getParent()).removeView(ivObjects);
		}

		if (isFullScreen) {

			ivObjects = new ImageView(this);
			ivObjects.setBackgroundResource(R.drawable.back);

			RelativeLayout layout = (RelativeLayout) nextPage.getView();
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					nextPage.getView().getWidth(), nextPage.getView()
							.getHeight());
			params.addRule(RelativeLayout.CENTER_IN_PARENT);

			layout.addView(ivObjects, params);

			ivObjects.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					arg0.setVisibility(View.GONE);

				}
			});
		}

	}

	private void addViewToScreen() {
		if (ivObjects != null) {
			((ViewGroup) ivObjects.getParent()).removeView(ivObjects);
		}
		ivObjects = new ImageView(this);
		ivObjects.setBackgroundResource(R.drawable.back);
 
		
		ViewGroup layout = (ViewGroup) nextPage.getView();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);

		layout.addView(ivObjects, params);

		ivObjects.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				arg0.setVisibility(View.GONE);

			}
		});

	}

	private boolean isShowed = false;

	@SuppressLint("NewApi")
	@Override
	public void addMapButtonToScreen(RelativeLayout layout) {

		if (nextPage != null) {

			// remove previously added buttonNext
			if (iBtnMap != null && iBtnMap.getParent() != null) {
				((ViewGroup) iBtnMap.getParent()).removeView(iBtnMap);
			}

			iBtnMap = new ImageButton(this);
			iBtnMap.setBackgroundResource(R.drawable.button_map);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					(int) Math.ceil(96 * density),
					(int) Math.ceil(96 * density));
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

			params.setMargins((int) Math.ceil(-62 * density),
					(int) Math.ceil(12 * density),
					(int) Math.ceil(12 * density),
					(int) Math.ceil(12 * density));

			layout.addView(iBtnMap, params);

			isShowed = false;
			iBtnMap.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (!isShowed) {
						ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
								iBtnMap, "translationX", 54 * density);
						objectAnimator.setDuration(500);
						objectAnimator.start();

						isShowed = true;
					} else {
						addViewToScreen(true);
						int currentapiVersion = android.os.Build.VERSION.SDK_INT;
						if (currentapiVersion > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
							ivObjects.setBackground(map);
						} else {
							ivObjects.setImageDrawable(map);
						}

						ivObjects
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View arg0) {
										arg0.setVisibility(View.GONE);

									}
								});

						iBtnMap.setX(-62 * density);
						isShowed = false;
					}

				}
			});
		}

	}

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
		if (mediaPlayer != null) {
			mediaPlayer.setLooping(false);
			play();
		}
	}

	private static void createMP(int id) {
		if (id > -1) {

			releaseMusic();

			idToPlay = id;

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

	public static void setMuted() {
		isMusicMuted = !isMusicMuted;

		if (isMusicMuted) {
			// Set the icon as muted because the music is now stopped
			playIcon = R.drawable.ic_action_volume_muted;
			pauseMusic();
		} else {
			// Set the icon as volume on because the music is now playing
			playIcon = R.drawable.ic_action_volume_on;
			play();
		}

		stopButton.setImageResource(playIcon);

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
	private void callActivityReadStory() {
		persistStatusGame();
		Intent intent = new Intent(this, ReadStoryActivity.class);
		startActivity(intent);
	}

	/**
	 * Loads the activity to read our own stories
	 */
	private void callActivityGui() {
		Intent intent = new Intent(this, BookActivity.class);
		startActivity(intent);
	}

	/**
	 * Loads the activity to build our own stories
	 */
	private void callActivityBuildStory() {
		persistStatusGame();
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.menu_item_share);

		// Fetch and store ShareActionProvider
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();

		// Return true to display menu
		return true;
	}

	@Override
	public Intent createShareIntent(String title, String desc, Bitmap bitmap) {
		Intent shareIntent = null;
		Uri imageUri = null;

		// check if special messages have been sent
		if (title == null || title == "") {
			title = getString(R.string.social_brand_title);
		}
		if (desc == null || desc == "") {
			desc = getString(R.string.social_action_desc);
		}

		try {
			imageUri = Uri.parse(MediaStore.Images.Media.insertImage(
					this.getContentResolver(), bitmap, null, null));
			shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("image/*");
			shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
			shareIntent.putExtra(Intent.EXTRA_TITLE, title);
			shareIntent.putExtra(Intent.EXTRA_TEXT, desc);
		} catch (NullPointerException e) {
			Log.e("Create intent to share image", "Image not found.");
		}

		return shareIntent;
	}

	@Override
	public void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}

	private Fragment nextPage = null;
	private String nextPageName = null;
	private String nextPageClass = null;
	private String prevPageClass = null;
	private String iconPath = "quarto_olhar_talisma_icon";

	@Override
	public void onChoiceMade(Fragment frg, String currentPage, String iconPath) {
		nextPage = frg;
		nextPageName = currentPage;
		// get previous page
		prevPageClass = nextPageClass;
		nextPageClass = frg.getClass().getName();
		if (iconPath != null) {
			this.iconPath = iconPath;
		} else {
			this.iconPath = "quarto_olhar_talisma_icon";
		}
	}

	@Override
	public void onChoiceMade(Fragment frg, String currentPage) {
		Log.d("Bookac", "onChoiceMade");
		nextPage = frg;
		nextPageName = currentPage;
		nextPageClass = frg.getClass().getName();
		this.iconPath = "quarto_olhar_talisma_icon";

	}

	@Override
	protected void onStop() {
		super.onStop();

		releaseMusic();

		persistStatusGame();
		Log.d("bookactivity", "onStop");
	}

	/**
	 * Persist current game status
	 */
	@Override
	public void persistStatusGame() {
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
			newValues.put(Status.CURRENTMAPTOSHOW, currentMapPosition);
			// Insert the row
			Uri myRowUri = cr.insert(PrototypeProvider.CONTENT_URI_STATUS,
					newValues);
		}
	}

	private String SCREENSHOTS_LOCATIONS = Environment
			.getExternalStorageDirectory().getPath() + "/";
	private Bitmap bitmap;

	private Bitmap getPrintScreenFromScreen() {
		// Get device dimensions
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		// Get root view
		View view = getWindow().getDecorView().findViewById(
				android.R.id.content);

		// Create the bitmap to use to draw the screenshot
		bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_4444);
		final Canvas canvas = new Canvas(bitmap);

		// Get current theme to know which background to use
		final Activity activity = this;
		final Theme theme = activity.getTheme();
		final TypedArray ta = theme
				.obtainStyledAttributes(new int[] { android.R.attr.windowBackground });
		final int res = ta.getResourceId(0, 0);
		final Drawable background = activity.getResources().getDrawable(res);

		// Draw background
		background.draw(canvas);

		// Draw views
		view.draw(canvas);

		// Save the screenshot to the file system
		FileOutputStream fos = null;
		String imageName = "";
		try {
			final File sddir = new File(SCREENSHOTS_LOCATIONS);
			if (!sddir.exists()) {
				sddir.mkdirs();
			}
			imageName = SCREENSHOTS_LOCATIONS + System.currentTimeMillis()
					+ ".jpg";

			fos = new FileOutputStream(imageName);
			if (fos != null) {
				if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)) {
					Log.d("booakactivity", "Compress/Write failed");
				}
				fos.flush();
				fos.close();
			}

		} catch (FileNotFoundException e) {
			Toast.makeText(context, getString(R.string.errorSocialPicture),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(context, getString(R.string.errorSocialPicture),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		return bitmap;
	}

	@Override
	public void askForHelpOnGooglePlus() {

		if (isGooglePlusInstalled()) {

			// persist game status
			persistStatusGame();

			Bitmap bitmap = getPrintScreenFromScreen();

			List<Bitmap> photos = new ArrayList<Bitmap>();
			photos.add(bitmap);

			// Abra a caixa de diálogo de compartilhamento do Google+ com
			// atribuição para seu aplicativo.
			Intent shareIntent = new PlusShare.Builder(BookActivity.this)
					.setType("text/plain")
					.setText(
							getString(R.string.social_help) + " "
									+ getString(R.string.social_brand_title))
					.setContentUrl(
							Uri.parse("https://play.google.com/store/apps/details?id=com.endomondo.android"))
					.addStream(getImageUri(this, bitmap)).getIntent();

			startActivityForResult(shareIntent, 0);
		} else {
			Toast.makeText(context, getString(R.string.social_app_not_found),
					Toast.LENGTH_LONG).show();
		}

	}

	public boolean isGooglePlusInstalled() {
		try {
			getPackageManager().getApplicationInfo(
					"com.google.android.apps.plus", 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	@Override
	public void askForHelpOnFacebook() {

		// persist game status
		persistStatusGame();

		if (Session.getActiveSession().isOpened()) {
			// If the user has a supported version of Facebook and if the
			// session is
			// open
			if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
					FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {

				Bitmap bitmap = getPrintScreenFromScreen();

				List<Bitmap> photos = new ArrayList<Bitmap>();
				photos.add(bitmap);

				// Publish the post using the Share Dialog
				FacebookDialog shareDialog = new FacebookDialog.PhotoShareDialogBuilder(
						this).addPhotos(photos).build();
				uiFacebookHelper.trackPendingDialogCall(shareDialog.present());
			} else {
				publishFeedDialog();
			}

		} else {
			// Fallback. Try to login
			facebookLogin();
		}

	}

	/**
	 * Publish on Facebook when the user doesn't have the facebook app installed
	 */
	private void publishFeedDialog() {
		Bitmap bitmap = getPrintScreenFromScreen();
		Request.Callback callback = new Request.Callback() {

			@Override
			public void onCompleted(Response response) {
				if (response.getError() != null) {
					Toast.makeText(
							context,
							"COMPLETE: "
									+ response.getError().getErrorMessage(),
							Toast.LENGTH_LONG).show();
				}

			}
		};
		// We need this permission so that we can publish to the user feed
		NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
				this, Arrays.asList("publish_actions", "publish_stream"));
		Session.getActiveSession().requestNewPublishPermissions(
				newPermissionsRequest);

		Request request = Request.newUploadPhotoRequest(
				Session.getActiveSession(), bitmap, callback);
		Bundle parameters = request.getParameters();
		parameters.putString("message", getString(R.string.social_help));
		request.executeAsync();
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	public void facebookLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this)
					.setCallback(statusCallback));
		} else {
			Session.openActiveSession(this, true, statusCallback);
		}
	}

	SessionStatusCallback statusCallback = new SessionStatusCallback();

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			Log.d("SessionStatusCallback", " SessionStatusCallback");
			if (exception != null) {
				Log.d("FaceBOOM", exception.getMessage());
				Toast.makeText(context, getString(R.string.socialMediaError),
						Toast.LENGTH_SHORT).show();
			}
			if (state.isOpened()) {
				Log.i("isOpened", "after Login");
				askForHelpOnFacebook();
			} else if (state.isClosed()) {
				Toast.makeText(context,
						getString(R.string.socialMediaSessionClosed),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		releaseMusic();

		Log.d("bookactivity", "onDestroy");
		uiFacebookHelper.onDestroy();
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
	protected void onResume() {
		super.onResume();
		uiFacebookHelper.onResume();

		loadDataBase();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiFacebookHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		play();
		Log.d("bookactivity", "onRestart");
	}

	@Override
	public void onChoiceMadeCommit(String namePreviousPage, Boolean isToPersist) {
		Log.d("Bookactivity", "onChoiceMadeCommit");
		getActiveFragments();
		if (nextPage != null) {

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
			Log.d("Bookactivity", "loadNextPage");
			releaseMusic();

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

		// if not already on journey path
		if (!isInJourney(chapterName)) {
			journeyItemList.add(0, ji);
			persistJourney(chapterName);

			// handle Points; We only add points if we didn't passe here already
			addPoints(20);
		}
		journeyArrayAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean isInJourney(String chapterName) {
		if (journeyItemList == null || journeyItemList.size() == 0) {
			return false;
		}

		for (IListItem ji : journeyItemList) {
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
					+ enigmaLikeStr + "; Enigma não gosta:  "
					+ enigmaDislikeStr + "; Pontos: " + pointsStr
					+ "; Escolher caminhos: " + pathStr + "; Gosta da app: "
					+ likeItStr + "; Gosta de ler: " + readsStr
					+ "; Tipo de livro que gosta: " + typeStr);

		}

	}

	List<WeakReference<Fragment>> fragList = new ArrayList<WeakReference<Fragment>>();

	@Override
	public void onAttachFragment(Fragment fragment) {
		fragList.add(new WeakReference(fragment));
	}

	public List<Fragment> getActiveFragments() {
		ArrayList<Fragment> ret = new ArrayList<Fragment>();
		for (WeakReference<Fragment> ref : fragList) {
			Fragment f = ref.get();
			if (f != null) {
				if (f.isVisible()) {
					ret.add(f);
				}
			}
		}
		return ret;
	}

	@Override
	public void onLowMemory() {
		Log.d("Prototype-ALERTA", "LOW MEMORY - FAz qq coisa páh!");
	}

	private void handleStatusCursor(Loader<Cursor> loader, Cursor data) {
		int id = data.getColumnIndex(Status.ID);
		int gameId = data.getColumnIndex(Status.GAME_ID);
		int points = data.getColumnIndex(Status.POINTS);
		int currentChapter = data.getColumnIndex(Status.CURRENTCHAPTER);
		int currentMapIdx = data.getColumnIndex(Status.CURRENTMAPTOSHOW);

		String name = null;
		while (data.moveToNext()) {
			Fragment current = getFragmentFromClassname(data
					.getString(currentChapter));

			if (current != null) {
				name = getAttrValueFromFragment(current, "NAME");
				// Go to next page
				onChoiceMade(current, name);
				// onChoiceMadeCommit(name, false);
				// Load the next Page of the story
				loadNextPage();

				int currentPoints = data.getInt(points);
				setPoints(currentPoints);
			}

			// Add action for map
			if (data.getString(currentMapIdx) != null) {
				Resources resources = context.getResources();
				final int resourceId = resources.getIdentifier(
						data.getString(currentMapIdx), "drawable",
						context.getPackageName());

				setCurrentMapPosition(resourceId);
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
				PagBedRoomDarkfrg firstFragment = new PagBedRoomDarkfrg();
				nextPage = firstFragment;
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
		restartItemJourneyList();
		int iconID;
		String iconName;
		while (data.moveToNext()) {
			String dateStr = data.getString(date);
			Date dateDt;
			try {
				dateDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(dateStr);
			} catch (ParseException e) {
				dateDt = null;
			}

			iconName = data.getString(icon);
			iconID = getResources().getIdentifier(iconName, "drawable",
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

		switch (tableLoaded) {
		case PrototypeProvider.USER_ALLROWS:

			// Initiate chapter loading
			restartLoaderChapter();

			break;
		case PrototypeProvider.STATUS_ALLROWS:
			handleStatusCursor(loader, data);

			// Initiate Objects Loading
			restartLoaderObjects();
			break;
		case PrototypeProvider.CHAPTER_ALLROWS:
			handleChapterCursor(loader, data);
			// Initiate status Loading
			restartLoaderStatus();
			break;
		case PrototypeProvider.OBJECT_ALLROWS:
			handleObjectCursor(loader, data);

			break;

		case PrototypeProvider.QUIZZ_ALLROWS:
			// handleQuizzCursor(loader, data);

			// Initiate Objects Loading

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
	 * Set the Page table as main cursor again
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
				Status.CURRENTCHAPTER, Status.CURRENTMAPTOSHOW };
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
		if (className != null && className != "") {
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
		}
		return frg;

	}

	@Override
	public String getAttrValueFromFragment(Fragment frg, String attrName) {

		Field f = null;
		try {
			f = frg.getClass().getField(attrName);

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

	@Override
	public void addPoints(int points) {

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
	
	private void restartItemJourneyList() {
		if (journeyItemList == null) {
			journeyItemList = new ArrayList<IListItem>();
		} else {
			journeyItemList.clear();
		}
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
	public void onChoiceMade(Fragment nextPag, int currentPage) {
		onChoiceMade(nextPag, getString(currentPage));

	}

	@Override
	public void onChoiceMadeCommit(int namePreviousPage, Boolean isToPersist) {
		onChoiceMadeCommit(getString(namePreviousPage), isToPersist);

	}

	@Override
	public void onBackPressed() {

		if (prevPageClass != null && prevPageClass != "") {

			try {
				Fragment prev = getFragmentFromClassname(prevPageClass);

				if (prev != null) {
					String name = getAttrValueFromFragment(prev, "NAME");
					if (name != null) {
						onChoiceMade(prev, name);

						// Load the next Page of the story
						loadNextPage();
					}
				}
			} catch (ClassCastException e) {
				Log.d("BookActivity", "CAst exception");

				throw new ClassCastException();
			}

		}
	}

	@Override
	public void setCurrentMapPosition(int idResource) {
		currentMapPosition = getResources().getResourceEntryName(idResource);
		map = getResources().getDrawable(idResource);

	}

	@Override
	public String getCurrentMapPosition() {

		return currentMapPosition;
	}

}
