package com.dipeca.prototype;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.dipeca.prototype.entiy.Chapter;
import com.dipeca.prototype.entiy.Game;
import com.dipeca.prototype.entiy.Objects;
import com.dipeca.prototype.entiy.Status;
import com.dipeca.prototype.entiy.User;

public class PrototypeStatsProvider extends ContentProvider {

	public static final Uri CONTENT_URI_USERS = Uri
			.parse("content://com.dipeca.prototypeprovider/users");

	public static final Uri CONTENT_URI_CHAPTERS = Uri
			.parse("content://com.dipeca.prototypeprovider/"
					+ Chapter.TABLE_NAME);

	public static final Uri CONTENT_URI_STATUS = Uri
			.parse("content://com.dipeca.prototypeprovider/"
					+ Status.TABLE_NAME);

	public static final Uri CONTENT_URI_OBJECTS = Uri
			.parse("content://com.dipeca.prototypeprovider/"
					+ Objects.TABLE_NAME);

	public static final String AUTHORITY = "com.dipeca.prototypeprovider";

	SchemaHelper myOpenHelper;

	// Create the constants used to differentiate between the different URI
	// requests.
	public static final int USER_ALLROWS = 1;
	public static final int USER_SINGLE_ROW = 2;
	public static final int CHAPTER_ALLROWS = 3;
	public static final int CHAPTER_SINGLE_ROW = 4;
	public static final int STATUS_ALLROWS = 5;
	public static final int STATUS_SINGLE_ROW = 6;
	public static final int OBJECT_ALLROWS = 7;
	public static final int OBJECT_SINGLE_ROW = 8;

	private static final UriMatcher uriMatcher;

	private static final String PROVIDER_NAME = "com.dipeca.prototypeprovider";
	// Populate the UriMatcher object, where a URI ending in
	// ‘elements’ will correspond to a request for all items,
	// and ‘elements/[rowID]’ represents a single row.
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "users", USER_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, "users/#", USER_SINGLE_ROW);
		uriMatcher.addURI(PROVIDER_NAME, Chapter.TABLE_NAME, CHAPTER_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, Chapter.TABLE_NAME + "/#",
				CHAPTER_SINGLE_ROW);
		uriMatcher.addURI(PROVIDER_NAME, Status.TABLE_NAME, STATUS_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, Status.TABLE_NAME + "/#",
				STATUS_SINGLE_ROW);
		uriMatcher.addURI(PROVIDER_NAME, Objects.TABLE_NAME, OBJECT_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, Objects.TABLE_NAME + "/#",
				OBJECT_SINGLE_ROW);
	}

	@Override
	public boolean onCreate() {
		// Construct the underlying database.
		// Defer opening the database until you need to perform // a query or
		// transaction.
		myOpenHelper = new SchemaHelper(getContext(),
				SchemaHelper.DATABASE_NAME, null, SchemaHelper.DATABASE_VERSION);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Open the database.
		SQLiteDatabase db;
		try {
			db = myOpenHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = myOpenHelper.getReadableDatabase();
		}
		// Replace these with valid SQL statements if necessary.
		String groupBy = null;
		String having = null;
		// Use an SQLite Query Builder to simplify constructing the
		// database query.
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// If this is a row query, limit the result set to the passed in row
		// according to tables.
		String tableName = null;
		String rowID = null;
		switch (uriMatcher.match(uri)) {
		case USER_SINGLE_ROW:
			tableName = User.TABLE_NAME;
			rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(User.ID + "=" + rowID);
			break;
		case USER_ALLROWS:
			tableName = User.TABLE_NAME;
			break;
		case CHAPTER_SINGLE_ROW:
			tableName = Chapter.TABLE_NAME;
			rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(Chapter.ID + "=" + rowID);
			break;
		case CHAPTER_ALLROWS:
			tableName = Chapter.TABLE_NAME;
			break;
		case STATUS_SINGLE_ROW:
			tableName = Status.TABLE_NAME;
			rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(Status.ID + "=" + rowID);
			break;
		case STATUS_ALLROWS:
			tableName = Status.TABLE_NAME;
			break;
		case OBJECT_SINGLE_ROW:
			tableName = Objects.TABLE_NAME;
			rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(Objects.ID + "=" + rowID);
			break;
		case OBJECT_ALLROWS:
			tableName = Objects.TABLE_NAME;
			break;
		default:
			break;
		}
		// Specify the table on which to perform the query. This can
		// be a specific table or a join as required.
		queryBuilder.setTables(tableName);
		// Execute the query.
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, groupBy, having, sortOrder);
		// Return the result Cursor.
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// Return a string that identifies the MIME type
		// for a Content Provider URI
		// switch (uriMatcher.match(uri))
		// {
		// case ALLROWS:
		// return "com.dipeca.cursor.dir/com.dipeca.elemental";
		// case SINGLE_ROW:
		// return "com.dipeca.cursor.item/com.dipeca.elemental";
		// default:
		// throw new IllegalArgumentException("Unsupported URI: " + uri)
		// ￼￼￼￼￼￼￼}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// Open a read / write database to support the transaction.
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();

		// To add empty rows to your database by passing in an empty Content
		// Values object, you must use the null column hack parameter to specify
		// the name of the column that can be set to null.
		String nullColumnHack = null;
		String tableName = null;
		Uri content_uri = null;
		// Insert the values into the table
		switch (uriMatcher.match(uri)) {
		case USER_ALLROWS:
			tableName = User.TABLE_NAME;
			content_uri = CONTENT_URI_USERS;
			break;
		case CHAPTER_ALLROWS:
			tableName = Chapter.TABLE_NAME;
			content_uri = CONTENT_URI_CHAPTERS;
			break;
		case STATUS_ALLROWS:
			tableName = Status.TABLE_NAME;
			content_uri = CONTENT_URI_STATUS;
			break;
		case OBJECT_ALLROWS:
			tableName = Objects.TABLE_NAME;
			content_uri = CONTENT_URI_OBJECTS;
			break;
		default:
			break;
		}

		long id = db.insert(tableName, nullColumnHack, values);
		if (id > -1) {
			// Construct and return the URI of the newly inserted row.
			Uri insertedId = ContentUris.withAppendedId(content_uri, id);
			// Notify any observers of the change in the data set.
			getContext().getContentResolver().notifyChange(insertedId, null);
			return insertedId;
		} else
			return null;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		int count;
		switch (uriMatcher.match(uri)) {
		case USER_ALLROWS:
			// PERFORM REGULAR DELETE
			count = db.delete(User.TABLE_NAME, where, whereArgs);
			break;
		case USER_SINGLE_ROW:
			// FROM INCOMING URI GET SSID
			String ssid = uri.getPathSegments().get(User.SSID_PATH_POSITION);
			// USER WANTS TO DELETE A SPECIFIC CITIZEN
			String finalWhere = User.ID + "=" + ssid;
			// IF USER SPECIFIES WHERE FILTER THEN APPEND
			if (where != null) {
				finalWhere = finalWhere + " AND " + where;
			}
			count = db.delete(User.TABLE_NAME, finalWhere, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static class SchemaHelper extends SQLiteOpenHelper {

		public static final String DATABASE_NAME = "reading_project.db";
		public static final int DATABASE_VERSION = 12;

		public SchemaHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		public SchemaHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		} 

		@Override
		public void onCreate(SQLiteDatabase db) {
			// CREATE USER TABLE
			db.execSQL("CREATE TABLE " + User.TABLE_NAME + " (" + User.ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + User.NAME
					+ " TEXT," + User.AGE + " INTEGER," + User.PHOTO_PATH
					+ " TEXT);");

			// CREATE GAME TABLE
			db.execSQL("CREATE TABLE " + Game.TABLE_NAME + " (" + Game.ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Game.USER_ID
					+ " INTEGER ," + " FOREIGN KEY (" + Game.USER_ID
					+ ") REFERENCES " + User.TABLE_NAME + " (" + User.ID
					+ "));");

			// CREATE CHAPTER TABLE
			db.execSQL("CREATE TABLE " + Chapter.TABLE_NAME + " (" + Chapter.ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Chapter.CURRENT
					+ " INTEGER," + Chapter.DATE
					+ " DATETIME DEFAULT CURRENT_TIMESTAMP," + Chapter.GAME_ID
					+ " INTEGER," + Chapter.POINTS + " INTEGER,"
					+ Chapter.PREVIOUS_CHAPTER_ID + " INTEGER,"
					+ Chapter.CURRENT_CLASS + " TEXT," + " FOREIGN KEY ("
					+ Chapter.GAME_ID + ") REFERENCES " + Game.TABLE_NAME
					+ " (" + Game.ID + "));");

			// CREATE STATUS TABLE
			db.execSQL("CREATE TABLE " + Status.TABLE_NAME + " (" + Status.ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Status.ENERGY
					+ " INTEGER," + Status.GAME_ID + " INTEGER,"
					+ Status.POINTS + " INTEGER ," + " FOREIGN KEY ("
					+ Status.GAME_ID + ") REFERENCES " + Game.TABLE_NAME + " ("
					+ Game.ID + "));");

			// CREATE OBJECTS TABLE
			db.execSQL("CREATE TABLE " + Objects.TABLE_NAME + " (" + Objects.ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Objects.NAME
					+ " TEXT," + Objects.GAME_ID + " INTEGER,"
					+ Objects.IMAGE_PATH + " TEXT," + Objects.TYPE
					+ " INTEGER," + Objects.IS_SHOW + " INTEGER,"
					+ " FOREIGN KEY (" + Objects.GAME_ID + ") REFERENCES "
					+ Game.TABLE_NAME + " (" + Game.ID + "));");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("LOG_TAG", "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");
			// KILL PREVIOUS TABLES IF UPGRADED
			db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Chapter.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Game.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Status.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Objects.TABLE_NAME);
			// CREATE NEW INSTANCE OF SCHEMA
			onCreate(db);

		}
	}

}
