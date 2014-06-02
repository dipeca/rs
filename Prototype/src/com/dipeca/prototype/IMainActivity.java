package com.dipeca.prototype;

import android.app.Fragment;

public interface IMainActivity {
	
	void onChoiceMade(Fragment nextPag, String currentPage, String iconPath);	
	
	void onChoiceMade(Fragment nextPag, String currentPage);	
	
	void onChoiceMadeCommit(String namePreviousPage, Boolean isToPersist);
	
	void onChoiceMadeCommitFirstPage(String namePreviousPage, Boolean isToPersist);
	
	void onChoiceMadeCommit(String namePreviousPage, Boolean isToPersist, Boolean isToStopMusic);
	
	Fragment getFragmentFromJourneyItem(JourneyItem ji);
	
	String getNameFromFragment(Fragment frg);
	
	void objectFoundPersist(ObjectItem oi);
	
	void setAddPoints(int points);
	
	boolean isInObjects(ObjectItem oi);
	
	void restartLoaderQuizz();
	
	void handleQuizzSubmit();
	
	void restartApp();
}
