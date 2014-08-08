package com.dipeca.bookactivity;

import java.util.List;

import com.dipeca.prototype.R;

import android.app.LauncherActivity.ListItem;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JourneyListAdapter extends ArrayAdapter<IListItem> {

	int resource;
	
	public JourneyListAdapter(Context context, int resource, List<IListItem> items) {
		super(context, resource, items);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout todoView;

		IListItem item = getItem(position);
		String chapterString = item.getCurrent();
		Drawable iconDrawable = item.getIcon();
//		Date createdDate = item.getCreated();
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
//		String dateString = sdf.format(createdDate);

		if (convertView == null) {
			todoView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(resource, todoView, true);
		} else {
			todoView = (LinearLayout) convertView;
		}

		//TextView dateView = (TextView) todoView.findViewById(R.id.rowDate);
		ImageView icon = (ImageView) todoView.findViewById(R.id.icon);
		TextView chapterName = (TextView) todoView.findViewById(R.id.chapter);
		//dateView.setText(dateString);
		chapterName.setText(chapterString);
		icon.setBackgroundDrawable(iconDrawable);

		return todoView;
	}

}
