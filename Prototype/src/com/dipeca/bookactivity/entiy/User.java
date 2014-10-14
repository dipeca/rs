package com.dipeca.bookactivity.entiy;

import android.net.Uri;

import com.dipeca.bookactivity.PrototypeProvider;

public class User {
	// EACH STUDENT HAS UNIQUE ID
	public static final String ID = "_id";
	// NAME OF THE USER
	public static final String NAME = "name";
	// PATH TO PHOTO
	public static final String PHOTO_PATH = "photo_path";
	// NAME OF THE TABLE
	public static final String AGE = "age";
	// NAME OF THE TABLE
	public static final String TABLE_NAME = "users";
	
	
	
	// THE CONTENT URI TO OUR PROVIDER
	public static final Uri CONTENT_URI = Uri.parse("content://" +
	PrototypeProvider.AUTHORITY + "/user");
	// MIME TYPE FOR GROUP OF CITIZENS
	public static final String CONTENT_TYPE =
	"vnd.android.cursor.dir/user";
	// MIME TYPE FOR SINGLE CITIZEN
	public static final String CONTENT_ITEM_TYPE =
	"vnd.android.cursor.item/user";
	// RELATIVE POSITION OF CITIZEN SSID IN URI
	public static final int SSID_PATH_POSITION = 1;
}
