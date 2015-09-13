package com.dipeca.buildstoryactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dipeca.bookactivity.IFragmentBook;
import com.dipeca.bookactivity.PagVillageFrg;
import com.dipeca.bookactivity.PrototypeProvider;
import com.dipeca.buildstoryactivity.entity.Drawable;
import com.dipeca.buildstoryactivity.entity.Legend;
import com.dipeca.buildstoryactivity.entity.Page;
import com.dipeca.buildstoryactivity.entity.PageDrawable;
import com.dipeca.buildstoryactivity.entity.PageStory;
import com.dipeca.item.IListItem;
import com.dipeca.item.ObjectItem;
import com.dipeca.item.ObjectItemImageAdapter;
import com.dipeca.item.Utils;
import com.dipeca.prototype.R;

/**
 * @author dipeca
 * 
 */
public class PageBuildPage extends Fragment implements IFragmentBook,
		LoaderManager.LoaderCallbacks<Cursor> {
	public static int NAME = R.string.adventureRule;
	public static int icon = R.drawable.caminho_somebody_icon;

	private GridView objectsGridView;
	private ObjectItemImageAdapter objectsAdapter;
	private ArrayList<ObjectItem> objectItemList;
	private ImageView ivObjects = null;

	private TextView tv1 = null;
	private TextView tv2 = null;
	private TextView tv3 = null;

	private EditText et1 = null;
	private EditText et2 = null;
	private EditText etTitle = null;

	private Bundle myBundle = null;

	private RelativeLayout rvLayout;
	private ImageView ivBig;
	private ImageView ivSmall;
	private Bitmap bitmapBig;
	private Bitmap bitmapSmall;
	private View view = null;
	private float density;
	private Spinner spinnerPageType;
	private TextView txt;
	private TextView txt2;
	private boolean isSetBigImage = true;
	private boolean isOneImage = true;
	private boolean isOneText = true;

	private long idDrawable1;
	private long idDrawable2;

	private ImageButton btnAddLegend1 = null;
	private ImageButton btnAddLegend2 = null;
	private ImageButton btnSavePage = null;

	private int tableLoaded = 1;

	private IBuildActivity buildActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		buildActivity = (IBuildActivity) activity;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		density = (int) getResources().getDisplayMetrics().density;

		view = inflater.inflate(R.layout.page_build_page, container, false);

		addItemsOnSpinner(view);

		// Objects
		objectItemList = new ArrayList<ObjectItem>();

		objectsGridView = (GridView) view.findViewById(R.id.gridView);
		objectsAdapter = new ObjectItemImageAdapter(this.getActivity(),
				objectItemList);
		objectsGridView.setAdapter(objectsAdapter);

		objectsGridView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {

						ObjectItem oi;
						if (isSetBigImage || isOneImage) {
							oi = objectItemList.get(pos);
							if (objectItemList.get(pos).getIdDrawable() > 0) {
								bitmapBig = Utils
										.decodeSampledBitmapFromResource(
												getResources(),
												(int) objectItemList.get(pos)
														.getIdDrawable(), 400,
												200);
							} else {
								bitmapBig = oi.getBitmap();
							}
							if (ivBig != null) {
								ivBig.setImageBitmap(bitmapBig);
							}
							idDrawable1 = arg3;
							isSetBigImage = false;

						} else {
							oi = objectItemList.get(pos);
							if (objectItemList.get(pos).getIdDrawable() > 0) {
								bitmapSmall = Utils
										.decodeSampledBitmapFromResource(
												getResources(), objectItemList
														.get(pos)
														.getIdDrawable(), 400,
												200);
							} else {
								bitmapSmall = oi.getBitmap();
							}
							if (ivSmall != null) {

								ivSmall.setImageBitmap(bitmapSmall);
							}

							idDrawable2 = arg3;
							isSetBigImage = true;
						}

					}

				});

		restartLoaderDrawables();

		handleText();

		addListeners();

		return view;
	}

	private void handleText() {
		tv1 = (TextView) view.findViewById(R.id.textViewTitle);
		tv1.setText(getString(R.string.legend1));

		// request focus to textview so that we don't see the keyboard
		tv1.setFocusableInTouchMode(true);
		tv1.requestFocus();

		txt = new TextView(this.getActivity());
		txt2 = new TextView(this.getActivity());

		et1 = (EditText) view.findViewById(R.id.etLEgend1);
		et2 = (EditText) view.findViewById(R.id.etLEgend2);
		etTitle = (EditText) view.findViewById(R.id.etPageTitle);
	}

	// add items into spinner dynamically
	public void addItemsOnSpinner(View view) {

		spinnerPageType = (Spinner) view.findViewById(R.id.page_type_spinner);
		List<String> list = new ArrayList<String>();
		list.add("Simples");
		list.add("Duas imagens");
		list.add("Imagem com zoom");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item, list);

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPageType.setAdapter(dataAdapter);
	}

	private RelativeLayout.LayoutParams imageParamsBig;
	private RelativeLayout.LayoutParams imageParamsHalfScreen;
	private RelativeLayout.LayoutParams imageParamsSmall;

	public void addListeners() {
		final RelativeLayout layout = (RelativeLayout) view
				.findViewById(R.id.page_type_preview);

		final RelativeLayout.LayoutParams baseParams = new RelativeLayout.LayoutParams(
				380 * (int) density, 260 * (int) density);
		baseParams.addRule(RelativeLayout.ALIGN_LEFT);

		btnAddLegend1 = (ImageButton) view.findViewById(R.id.submitText);
		btnAddLegend2 = (ImageButton) view.findViewById(R.id.submitText2);

		btnSavePage = (ImageButton) view.findViewById(R.id.savePage);

		spinnerPageType
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {

						// imageParams
						if (imageParamsBig == null) {
							imageParamsBig = new RelativeLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT);
						}

						// Half screen image
						if (imageParamsHalfScreen == null) {
							imageParamsHalfScreen = new RelativeLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									130 * (int) density);
						}
						// Small Image in the front
						if (imageParamsSmall == null) {
							imageParamsSmall = new RelativeLayout.LayoutParams(
									196 * (int) density, 128 * (int) density);
							imageParamsSmall.addRule(
									RelativeLayout.CENTER_IN_PARENT,
									RelativeLayout.TRUE);
						}
						if (rvLayout == null) {
							rvLayout = new RelativeLayout(getActivity());
							rvLayout.setBackgroundResource(R.drawable.imagebg);

						}

						// Reset layout
						if (txt.getParent() != null) {
							((ViewGroup) txt.getParent()).removeView(txt);
						}
						if (txt2.getParent() != null) {
							((ViewGroup) txt2.getParent()).removeView(txt2);
						}
						// remove view from parent
						if (rvLayout.getParent() != null) {
							((ViewGroup) rvLayout.getParent())
									.removeView(rvLayout);
						}
						layout.addView(rvLayout, baseParams);

						if (ivBig == null) {
							ivBig = new ImageView(getActivity());
							ivBig.setImageBitmap(bitmapBig);
						}

						if (ivSmall == null) {
							ivSmall = new ImageView(getActivity());
							ivSmall.setImageBitmap(bitmapSmall);
						}
						// Different layout according to selection
						if (pos == 0) {
							isOneImage = true;
							if (ivBig.getParent() != null) {
								((ViewGroup) ivBig.getParent())
										.removeView(ivBig);
							}
							rvLayout.addView(ivBig, imageParamsBig);
							isOneText = true;
						} else if (pos == 1) {
							isOneImage = false;
							LinearLayout ll = new LinearLayout(getActivity());
							ll.setOrientation(LinearLayout.VERTICAL);
							LinearLayout.LayoutParams llLp = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.MATCH_PARENT);

							ivBig.setImageBitmap(bitmapBig);

							if (ivBig.getParent() != null) {
								((ViewGroup) ivBig.getParent())
										.removeView(ivBig);
							}
							if (ivSmall.getParent() != null) {
								((ViewGroup) ivSmall.getParent())
										.removeView(ivSmall);
							}

							// remove border
							ivSmall.setBackgroundResource(R.drawable.back_no_border);
							ivBig.setBackgroundResource(R.drawable.back_no_border);

							ll.addView(ivBig, imageParamsHalfScreen);
							ll.addView(ivSmall, imageParamsHalfScreen);

							rvLayout.addView(ll, llLp);
						} else if (pos == 2) {
							isOneImage = false;
							if (ivBig.getParent() != null) {
								((ViewGroup) ivBig.getParent())
										.removeView(ivBig);
							}
							rvLayout.addView(ivBig, imageParamsBig);

							ivSmall.setBackgroundResource(R.drawable.imagebg);
							if (ivSmall.getParent() != null) {
								((ViewGroup) ivSmall.getParent())
										.removeView(ivSmall);
							}

							rvLayout.addView(ivSmall, imageParamsSmall);

						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		btnAddLegend1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				txt.setText(et1.getText());
				txt.setBackgroundResource(R.drawable.container_dropshadow);
				txt.setTextAppearance(getActivity().getApplicationContext(),
						R.style.StoryBlockBuild);
				int pad = Math.round(16 * density);
				txt.setPadding(pad, pad, pad, pad);
				LayoutParams lpTxt = new LayoutParams(
						Math.round(240 * density), LayoutParams.WRAP_CONTENT);
				lpTxt.setMargins((int) (8 * density), (int) (8 * density), 0, 0);

				if (txt.getParent() != null) {
					((ViewGroup) txt.getParent()).removeView(txt);
				}
				rvLayout.addView(txt, lpTxt);

			}
		});

		btnAddLegend2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				txt2.setText(et2.getText());
				txt2.setBackgroundResource(R.drawable.container_dropshadow);
				txt2.setTextAppearance(getActivity().getApplicationContext(),
						R.style.StoryBlockBuild);
				int pad = Math.round(16 * density);
				txt2.setPadding(pad, pad, pad, pad);
				RelativeLayout.LayoutParams lpTxt = new RelativeLayout.LayoutParams(
						Math.round(240 * density), LayoutParams.WRAP_CONTENT);
				lpTxt.setMargins((int) (8 * density), (int) (8 * density), 0, 0);

				lpTxt.addRule(RelativeLayout.CENTER_IN_PARENT,
						RelativeLayout.TRUE);
				lpTxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
						RelativeLayout.TRUE);

				if (txt2.getParent() != null) {
					((ViewGroup) txt2.getParent()).removeView(txt2);
				}
				isOneText = false;
				rvLayout.addView(txt2, lpTxt);

			}
		});

		btnSavePage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				persistPageData();
			}
		});
	}
	
	/**
	 * Restore the default page setup
	 * 1 image on screen 
	 */
	private void restoreImageLayout(){
		
		if (ivBig.getParent() != null) {
			((ViewGroup) ivBig.getParent())
					.removeView(ivBig);
		}
		
		if (ivSmall.getParent() != null) {
			((ViewGroup) ivSmall.getParent())
					.removeView(ivSmall);
		}

		rvLayout.addView(ivBig, imageParamsBig);
	}
	
	/**
	 * This function saves all data related to the newly created page
	 */
	private void persistPageData(){
		ContentResolver cr = getActivity().getContentResolver();
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		// newValues.put(Page.ID, maxId);
		newValues.put(Page.TYPE_ID,
				spinnerPageType.getSelectedItemPosition() + 1);
		newValues.put(Page.TITLE, etTitle.getText().toString());
		// Insert the row
		Uri myRowUri = cr.insert(PrototypeProvider.CONTENT_URI_PAGE,
				newValues);

		long idPage = ContentUris.parseId(myRowUri);
		newValues = new ContentValues();
		// Assign values for each row.
		// newValues.put(Page.ID, -1);
		newValues.put(PageDrawable.PAGE_ID, idPage);
		newValues.put(PageDrawable.ORDER, 1);
		newValues.put(PageDrawable.DRAWABLE_ID, idDrawable1);
		// Insert the row
		myRowUri = cr.insert(
				PrototypeProvider.CONTENT_URI_PAGEDRAWABLE, newValues);

		// if we have a 2 images page
		if (!isOneImage) {
			newValues = new ContentValues();
			// Assign values for each row.
			// newValues.put(Page.ID, -1);
			newValues.put(PageDrawable.PAGE_ID, idPage);
			newValues.put(PageDrawable.ORDER, 2);
			newValues.put(PageDrawable.DRAWABLE_ID, idDrawable2);
			// Insert the row
			myRowUri = cr.insert(
					PrototypeProvider.CONTENT_URI_PAGEDRAWABLE,
					newValues);
		}

		// Text Legend 1
		newValues = new ContentValues();
		// Assign values for each row.
		// newValues.put(Page.ID, -1);
		newValues.put(Legend.ORDER, 1);
		newValues.put(Legend.PAGE_ID, idPage);
		newValues.put(Legend.VALUE, et1.getText().toString());
		// Insert the row
		myRowUri = cr.insert(PrototypeProvider.CONTENT_URI_LEGENDS,
				newValues);

		if (!isOneImage) {
			// Text Legend 2
			newValues = new ContentValues();
			// Assign values for each row.
			// newValues.put(Page.ID, -1);
			newValues.put(Legend.ORDER, 2);
			newValues.put(Legend.PAGE_ID, idPage);
			newValues.put(Legend.VALUE, et2.getText().toString());
			// Insert the row
			myRowUri = cr.insert(PrototypeProvider.CONTENT_URI_LEGENDS,
					newValues);
		}

		// PageStory
		newValues = new ContentValues();
		// Assign values for each row.
		// newValues.put(Page.ID, -1);
		newValues.put(PageStory.PAGE_ID, idPage);
		newValues.put(PageStory.ORDER, journeyItemList.size() + 1);
		newValues.put(PageStory.STORY_ID, buildActivity.getStoryId());
		// Insert the row
		myRowUri = cr.insert(PrototypeProvider.CONTENT_URI_PAGESTORY,
				newValues);

		// restart dashboard
		restartLoaderPages();
		// load recently created page
		resetPageDrawingBoard();
	}

	/**
	 * Remove all views from the dashboard drawing page Restart all editable
	 * fields
	 */
	private void resetPageDrawingBoard() {
		if (txt.getParent() != null) {
			((ViewGroup) txt.getParent()).removeView(txt);
		}

		if (txt2.getParent() != null) {
			((ViewGroup) txt2.getParent()).removeView(txt2);
		}

		if (ivBig.getParent() != null) {
			((ViewGroup) ivBig.getParent()).removeView(ivBig);
		}

		if (ivSmall.getParent() != null) {
			((ViewGroup) ivSmall.getParent()).removeView(ivSmall);
		}
		
		bitmapBig = null;
		bitmapSmall = null;

		et1.setText("");
		et2.setText("");

		etTitle.setText("");

		spinnerPageType.setSelection(0);
		
		restoreImageLayout();
	}

	/**
	 * Set the Drawables table as main cursor again
	 */
	public void restartLoaderDrawables() {
		String[] projection = null;
		projection = new String[] { Drawable.ID, Drawable.NAME };

		makeProviderBundle(projection, null, null, null,
				PrototypeProvider.CONTENT_URI_DRAWABLES.toString());
		tableLoaded = PrototypeProvider.DRAWABLE_ALLROWS;
		getLoaderManager().restartLoader(0, myBundle, this);
	}

	/**
	 * Set the LoaderPages table as main cursor again
	 */
	public void restartLoaderPages() {
		tableLoaded = PrototypeProvider.DRAWABLEPAGE_ALLROWS;
		myBundle = SqlUtils.restartLoaderPages(buildActivity.getStoryId());
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

	private void handleDrawablesLoaded(Loader<Cursor> loader, Cursor data) {
		ObjectItem oi = null;
		objectItemList.clear();

		if (data != null) {
			int id = data.getColumnIndex(Drawable.ID);
			int name = data.getColumnIndex(Drawable.NAME);

			while (data.moveToNext()) {
				int idResource = getResources().getIdentifier(
						data.getString(name), "drawable",
						getActivity().getPackageName());

				oi = new ObjectItem(data.getLong(id), " ",
						Utils.decodeSampledBitmapFromResource(getResources(),
								idResource, (int) (32 * density),
								(int) (32 * density)), null, idResource, data.getString(name));

				objectItemList.add(oi);
			}
			
			objectsAdapter.notifyDataSetChanged();
		}

	}

	ArrayList<IListItem> journeyItemList = new ArrayList<IListItem>();

	private long idLastCreated = -1;

	private void handlePagesLoaded(Loader<Cursor> loader, Cursor data) {

		PageObject ji = null;

		// reset JourneyItemList
		resetJourneyListItem();

		if (data != null) {
			int id = data.getColumnIndex(Page.ID);
			int order = data.getColumnIndex(PageStory.ORDER);
			int title = data.getColumnIndex(Page.TITLE);
			int type = data.getColumnIndex(Page.TYPE_ID);
			int nameDrawable = data.getColumnIndex(Drawable.NAME);
			int iconID;
			while (data.moveToNext()) {
				idLastCreated = data.getLong(0);
				iconID = getResources().getIdentifier(
						data.getString(nameDrawable), "drawable",
						getActivity().getPackageName());
				ji = new PageObject(idLastCreated, data.getLong(order),
						new BitmapDrawable(getActivity().getResources(), Utils
								.decodeSampledBitmapFromResource(
										getResources(), iconID, 48, 48)),
						data.getString(title), data.getInt(type),
						buildActivity.getStoryId(), iconID);

				journeyItemList.add(ji);
			}

			buildActivity.setJourneyItemList(journeyItemList);

		}

	}

	/**
	 * Reset the page list on the menu
	 */
	private void resetJourneyListItem() {

		if (journeyItemList != null) {
			journeyItemList = new ArrayList<IListItem>();
		}
		journeyItemList.clear();
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
		case PrototypeProvider.DRAWABLE_ALLROWS:
			handleDrawablesLoaded(loader, data);
			// Initiate status loading
			restartLoaderPages();
			break;
		case PrototypeProvider.DRAWABLEPAGE_ALLROWS:
			handlePagesLoaded(loader, data);
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}
