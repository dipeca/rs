package com.dipeca.prototype;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ObjectItemImageAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ObjectItem> list;

	public ObjectItemImageAdapter(Context context,
			ArrayList<ObjectItem> mobileValues) {
		this.context = context;
		this.list = mobileValues;
	}

	private View gridView;

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {

			gridView = new View(context);

			// get layout from menu.xml
			gridView = inflater.inflate(R.layout.grid, null);
		} else {
			gridView = (View) convertView;
		}
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			textView.setText(list.get(position).getName());

			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);

			String name = list.get(position).getName();

			list.get(position).setBitmap(
					loadCardImage(list.get(position).getObjectImageType()));

			imageView.setImageBitmap(list.get(position).getBitmap());
		

		return gridView;
	}

	int density = 1;

	private Bitmap loadCardImage(int type) {
		density = (int) gridView.getResources().getDisplayMetrics().density;
		Bitmap bitmap1 = null;
		switch (type) {
		case ObjectItem.TYPE_BOOK_OF_SPELS:

			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.book_spells,
					256 * density, 256 * density);

			return bitmap1;

		case ObjectItem.TYPE_ROPE:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.rope, 256 * density,
					256 * density);

			return bitmap1;

		case ObjectItem.TYPE_PLANK:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.plank, 256 * density,
					256 * density);

			return bitmap1;

		case ObjectItem.TYPE_AMULET:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.talisma, 256 * density,
					256 * density);

			return bitmap1;

		case ObjectItem.TYPE_BOTTLE:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.frasco, 256 * density,
					256 * density);

			return bitmap1;

		case ObjectItem.TYPE_CORN:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.milho, 256 * density,
					256 * density);

			return bitmap1;
		}
		return null;
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}

		return 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).getId();
	}

}