package com.dipeca.bookactivity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RelativeLayout;

public interface IMainActivity {

	void onChoiceMade(Fragment nextPag, String currentPage, String iconPath);

	void onChoiceMade(Fragment nextPag, int currentPage, int iconPath);

	void onChoiceMade(Fragment nextPag, String currentPage);

	void onChoiceMade(Fragment nextPag, int currentPage);

	void onChoiceMadeCommit(String namePreviousPage, Boolean isToPersist);

	void onChoiceMadeCommit(int namePreviousPage, Boolean isToPersist);

	void onChoiceMadeCommitFirstPage(String namePreviousPage,
			Boolean isToPersist);

	Fragment getFragmentFromJourneyItem(JourneyItem ji);

	String getAttrValueFromFragment(Fragment frg, String AttrName);

	void objectFoundPersist(ObjectItem oi);

	void restartLoaderObjects();

	void addPoints(int points);

	void setPoints(int points);

	boolean isInObjects(ObjectItem oi);

	void restartApp();

	void askForHelpOnFacebook();

	void askForHelpOnGooglePlus();

	Intent createShareIntent(String title, String desc, Bitmap bitmap);

	void setShareIntent(Intent shareIntent);

	/**
	 * Setter to current map definition
	 * 
	 * @param idResource
	 */
	void setCurrentMapPosition(int idResource);

	/**
	 * Getter Accessor to current map definition
	 * 
	 * @param idResource
	 */
	String getCurrentMapPosition();

	/**
	 * Persist to Database the game status
	 */
	void persistStatusGame();

	/**
	 * Add Map button to screen
	 * 
	 * @param layout
	 */
	void addMapButtonToScreen(RelativeLayout layout);
}
