package com.dipeca.buildstoryactivity;

import java.util.ArrayList;

import android.app.Fragment;

import com.dipeca.item.IListItem;

public interface IBuildActivity {

	void loadFragment(Fragment page);
	
	void setStoryId(long id);
	long getStoryId();
	
	void setJourneyItem(IListItem ji);
	IListItem getJourneyItem();
	
	void setStoryName(String name);
	String getStoryName();
	
	void setJourneyItemList(ArrayList<IListItem> arr);
	
	void setPageToDisplay(Fragment page);
	
	void setSelectedPageIndex(int index);
	int getSelectedPageIndex();
	
	
	void loadStoryPage(boolean isPrev);
	
	/**
	 * @return boolean indicating whatever the current loaded page is the last one.
	 * If the list of pages is null then it will return false
	 */
	boolean isLastPage();
	
	/**
	 * @return boolean indicating whatever the current loaded page is the first one.
	 * If the list of pages is null then it will return false
	 */
	boolean isFirstPage();
}
