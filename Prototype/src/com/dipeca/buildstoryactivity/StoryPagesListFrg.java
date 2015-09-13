package com.dipeca.buildstoryactivity;

import android.app.Activity;
import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.dipeca.item.IListItem;

public class StoryPagesListFrg extends ListFragment{

	private IBuildActivity buildActivity;

	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		buildActivity = (IBuildActivity) activity;
	}


	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		buildActivity = (IBuildActivity) getActivity();
		
		IListItem ji = (IListItem) l.getAdapter().getItem(position);

		buildActivity.setJourneyItem(ji);
		buildActivity.setSelectedPageIndex(position);
		
		super.onListItemClick(l, v, position, id);
	}
	
	

}
