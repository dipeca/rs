package com.dipeca.bookactivity;

import android.app.Fragment;
import android.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class JourneySoFar extends ListFragment{

	private IMainActivity onChoice;

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		onChoice = (IMainActivity) getActivity();
		
		JourneyItem ji = (JourneyItem) l.getAdapter().getItem(position);

		Fragment fb = onChoice.getFragmentFromJourneyItem(ji);
		String name = onChoice.getAttrValueFromFragment(fb, "NAME");
		
		//Go to next page
		onChoice.onChoiceMade(fb, name, ji.getIconName());
		onChoice.onChoiceMadeCommit(name, false);
		
		super.onListItemClick(l, v, position, id);
	}
	
	

}
