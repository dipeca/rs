package com.dipeca.buildstoryactivity;

import android.os.Bundle;

import com.dipeca.bookactivity.PrototypeProvider;
import com.dipeca.buildstoryactivity.entity.Drawable;
import com.dipeca.buildstoryactivity.entity.Page;
import com.dipeca.buildstoryactivity.entity.PageDrawable;
import com.dipeca.buildstoryactivity.entity.PageStory;

public class SqlUtils {

	private static Bundle myBundle = null;
	/**
	 * Set the LoaderPages table as main cursor again
	 */
	public static Bundle restartLoaderPages(long storyId) {
		String[] projection = null;
		projection = new String[] { Page.ID, Page.TITLE, Page.TYPE_ID,
				PageStory.ORDER };

		String queryPage = "SELECT * FROM " + Page.TABLE_NAME
				+ " p INNER JOIN " + PageStory.TABLE_NAME + " ps ON p."
				+ Page.ID + "= ps." + PageStory.PAGE_ID + " INNER JOIN "
				+ PageDrawable.TABLE_NAME + " pd ON p." + Page.ID + "=pd."
				+ PageDrawable.PAGE_ID + " INNER JOIN " + Drawable.TABLE_NAME
				+ " d ON d." + Drawable.ID + "=pd." + PageDrawable.DRAWABLE_ID
				+ " AND pd." + PageDrawable.ORDER + " = 1" + " WHERE ps."
				+ PageStory.STORY_ID + "=?";

		

		myBundle = new Bundle();
		myBundle.putStringArray("projection", projection);
		myBundle.putString("selection", queryPage);
		myBundle.putStringArray("selectionArgs",
				new String[] { String.valueOf(storyId) });
		myBundle.putString("uri",
				PrototypeProvider.CONTENT_URI_PAGESTORY.toString());

		return myBundle;
		
	}
}
