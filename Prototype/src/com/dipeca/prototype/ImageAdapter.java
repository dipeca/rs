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
 
 
public class ImageAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ObjectItem> list;
 
	public ImageAdapter(Context context, ArrayList<ObjectItem> mobileValues) {
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
 
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			textView.setText(list.get(position).getName());
 
			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);
 
			String mobile = list.get(position).getName();
 
			//imageView.setImageResource(R.drawable.old_paper_light);
			imageView.setImageBitmap(loadCardImage(list.get(position).getObjectImageType()));
		} else {
			gridView = (View) convertView;
		}
 
		return gridView;
	}
	
	int density = 1;
	
	private Bitmap loadCardImage(int type) {
		density = (int) gridView.getResources().getDisplayMetrics().density;
		Bitmap bitmap1 = null;
	    switch( type ) {
	    case ObjectItem.TYPE_BOOK:
	    	
	    	bitmap1 = Utils.decodeSampledBitmapFromResource(gridView.getResources(),
	    			R.drawable.book, 64 * density, 64 * density);
	    	
	        return bitmap1;
	        
	    case ObjectItem.TYPE_MASK:
	    	bitmap1 = Utils.decodeSampledBitmapFromResource(gridView.getResources(),
	    			R.drawable.book, 64 * density, 64 * density);
	    	
	        return bitmap1;


	    case ObjectItem.TYPE_AMULET:
	    	bitmap1 = Utils.decodeSampledBitmapFromResource(gridView.getResources(),
	    			R.drawable.talisma, 64 * density, 64 * density);
	    	
	        return bitmap1;
	    	 
	    }
	    return null;
	}
 
	@Override
	public int getCount() {
		if(list != null){
			return list.size();
		}
		
		return 0;
	}
 
	@Override
	public Object getItem(int position) {
		return null;
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}
 
}