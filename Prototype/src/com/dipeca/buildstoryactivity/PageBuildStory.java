package com.dipeca.buildstoryactivity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dipeca.bookactivity.IFragmentBook;
import com.dipeca.bookactivity.PagVillageFrg;
import com.dipeca.bookactivity.PrototypeProvider;
import com.dipeca.buildstoryactivity.entity.Story;
import com.dipeca.item.ObjectItem;
import com.dipeca.item.ObjectItemImageAdapter;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

public class PageBuildStory extends Fragment implements IFragmentBook,
		LoaderManager.LoaderCallbacks<Cursor> {
	public static int NAME = R.string.story;
	public static int icon = R.drawable.caminho_somebody_icon;

	
	private IBuildActivity buildActivity;
	private GridView objectsGridView;
	private ObjectItemImageAdapter objectsAdapter;
	private ArrayList<ObjectItem> objectItemList;

	private TextView tv1 = null;

	private EditText et1 = null;

	private Bundle myBundle = null;
	
	private View view = null;
	private float density;

	private int tableLoaded = 1;
	
	
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		buildActivity = (IBuildActivity) activity;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		density = (int) getResources().getDisplayMetrics().density;

		view = inflater.inflate(R.layout.page_build_story, container, false);

		// Objects
		objectItemList = new ArrayList<ObjectItem>();

		objectsGridView = (GridView) view.findViewById(R.id.gridViewStories);
		objectsAdapter = new ObjectItemImageAdapter(this.getActivity(),
				objectItemList);
		objectsGridView.setAdapter(objectsAdapter);

		objectsGridView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						
						buildActivity.setStoryId(objectItemList.get(pos).getId());
						buildActivity.setStoryName(objectItemList.get(pos).getTitle());
						
						buildActivity.loadFragment(new PageBuildPage());
					}
				});

		restartLoaderStories();

		handleText();

		addListeners();

		return view;
	}

	private ImageButton btnSubmit = null;

	private void handleText() {
		tv1 = (TextView) view.findViewById(R.id.textViewTitle);
		tv1.setText(getString(R.string.story));

		// request focus to textview so that we dont see the keyboard
		tv1.setFocusableInTouchMode(true);
		tv1.requestFocus();

		et1 = (EditText) view.findViewById(R.id.etLEgend1);

		btnSubmit = (ImageButton) view.findViewById(R.id.saveStory);
	}

	public void addListeners() {

		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ContentResolver cr = getActivity().getContentResolver();
				ContentValues newValues = new ContentValues();
				// Assign values for each row.
				newValues.put(Story.TITLE, et1.getText().toString());
				newValues.put(Story.DRAWABLE, "book");
				// Insert the row
				Uri myRowUri = cr.insert(PrototypeProvider.CONTENT_URI_STORY,
						newValues);
				long idStory = ContentUris.parseId(myRowUri);
				
				if (objectItemList == null) {
					objectItemList = new ArrayList<ObjectItem>();
				}
				
				objectItemList.add(new ObjectItem(Long.valueOf(idStory), et1.getText().toString(), Utils.decodeSampledBitmapFromResource(
						getResources(),  R.drawable.book, 32, 32), null));
				
				objectsAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * Set the drawawble table as main cursor again
	 */
	public void restartLoaderStories() {
		String[] projection = null;
		projection = new String[] { Story.ID, Story.TITLE, Story.DRAWABLE };

		makeProviderBundle(projection, null, null, null,
				PrototypeProvider.CONTENT_URI_STORY.toString());
		tableLoaded = PrototypeProvider.STORY_ALLROWS;
		getLoaderManager().restartLoader(0, myBundle, this);
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

	private void handleStoryLoadedCursor(Loader<Cursor> loader, Cursor data) {
		ObjectItem oi = null;
		objectItemList.clear();

		if (data != null) {
			int id = data.getColumnIndex(Story.ID);
			int name = data.getColumnIndex(Story.TITLE);
			int drawbleIdx = data.getColumnIndex(Story.DRAWABLE);

			while (data.moveToNext()) {
				int idResource = getResources().getIdentifier(data.getString(drawbleIdx), "drawable",
						getActivity().getPackageName());
				
				oi = new ObjectItem(Long.valueOf(data.getInt(id)), data.getString(name),
						Utils.decodeSampledBitmapFromResource(
								getResources(), idResource, (int) (32 * density),
								(int)(32 * density)), null);
				
				objectItemList.add(oi);
				
			}
		}

	}

	@Override
	public String getPrevPage() {
		return PagVillageFrg.class.getName();
	}

	@Override
	public String getNextPage() {
		return null;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle args) {
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
		return new CursorLoader(getActivity(), queryUri, projection, where,
				whereArgs, sortOrder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		switch (tableLoaded) {
		case PrototypeProvider.STORY_ALLROWS:
			handleStoryLoadedCursor(loader, data);
			// Initiate status loading
			// restartLoaderStatus();
			break;

		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}
