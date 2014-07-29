package com.dipeca.bookactivity.entiy;

import com.dipeca.bookactivity.PrototypeProvider;

import android.net.Uri;

public class QuestionAnswer {
	// EACH STUDENT HAS UNIQUE ID
	public static final String ID = "_id";
	// Question Value
	public static final String DRAW = "DRAW";
	// Question Value
	public static final String TEXT = "TEXT";
	public static final String ENIGMA_LIKE = "ENIGMA_LIKE";
	public static final String ENIGMA_DISLIKE = "ENIGMA_DISLIKE";
	public static final String POINTS = "POINTS";
	public static final String PATH = "PATH";
	// Number of answer
	public static final String READS = "reads";
	// Number of answer
	public static final String TYPE = "type";
	// Number of answer
	public static final String OWN_TABLET = "OWN_TABLET";
	// Number of answer
	public static final String LIKEIT = "like_it";
	// NAME OF THE TABLE
	public static final String TABLE_NAME = "quizz";

	public static final String TYPE_MISTERY = "MYSTERY";
	public static final String TYPE_ADVENTURE = "AVENTURA";
	public static final String TYPE_RIR = "RIR";
	public static final String TYPE_FANTASIA = "FANTASIA";
	public static final String TYPE_ALL = "ALL";

	public static final String READ_STRONG_READER = "READ_STRONG_READER";
	public static final String READ_OCASION = "READ_OCASION";
	public static final String READ_NOT = "READ_NOT";

	public static final String LIKE_IT_VERY = "VERY";
	public static final String LIKE_IT_SOSO = "SOSO";
	public static final String LIKE_IT_NO = "NO";

	// THE CONTENT URI TO OUR PROVIDER
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PrototypeProvider.AUTHORITY + "/" + TABLE_NAME);
	// MIME TYPE FOR GROUP OF CITIZENS
	public static final String CONTENT_TYPE = "com.dipeca.prototypeprovider/"
			+ TABLE_NAME;
	// MIME TYPE FOR SINGLE CITIZEN
	public static final String CONTENT_ITEM_TYPE = "com.dipeca.prototypeprovider/"
			+ TABLE_NAME;
	// RELATIVE POSITION OF CITIZEN SSID IN URI
	public static final int SSID_PATH_POSITION = 1;
}
