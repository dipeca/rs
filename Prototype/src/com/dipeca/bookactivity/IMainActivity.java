package com.dipeca.bookactivity;

import android.app.Fragment;

public interface IMainActivity {
	
	void onChoiceMade(Fragment nextPag, String currentPage, String iconPath);	
	
	void onChoiceMade(Fragment nextPag, int currentPage, int iconPath);
	
	void onChoiceMade(Fragment nextPag, String currentPage);	
	
	void onChoiceMade(Fragment nextPag, int currentPage);	
	
	void onChoiceMadeCommit(String namePreviousPage, Boolean isToPersist);
	
	void onChoiceMadeCommit(int namePreviousPage, Boolean isToPersist);
	
	void onChoiceMadeCommitFirstPage(String namePreviousPage, Boolean isToPersist);
	
	Fragment getFragmentFromJourneyItem(JourneyItem ji);
	
	String getAttrValueFromFragment(Fragment frg, String AttrName);
	
	void objectFoundPersist(ObjectItem oi);
	
	void restartLoaderObjects();
	
	void addPoints(int points);
	
	void setPoints(int points);
	
	boolean isInObjects(ObjectItem oi);
	
	void restartApp();
}
