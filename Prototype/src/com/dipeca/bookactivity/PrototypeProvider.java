package com.dipeca.bookactivity;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.dipeca.bookactivity.entiy.Chapter;
import com.dipeca.bookactivity.entiy.Game;
import com.dipeca.bookactivity.entiy.Objects;
import com.dipeca.bookactivity.entiy.QuestionAnswer;
import com.dipeca.bookactivity.entiy.Status;
import com.dipeca.bookactivity.entiy.User;
import com.dipeca.buildstoryactivity.entity.Drawable;
import com.dipeca.buildstoryactivity.entity.Legend;
import com.dipeca.buildstoryactivity.entity.Page;
import com.dipeca.buildstoryactivity.entity.PageDrawable;
import com.dipeca.buildstoryactivity.entity.PageStory;
import com.dipeca.buildstoryactivity.entity.PageType;
import com.dipeca.buildstoryactivity.entity.Story;
import com.dipeca.prototype.R;

public class PrototypeProvider extends ContentProvider {

	public static final Uri CONTENT_URI_USERS = Uri
			.parse("content://com.dipeca.adventurestoriesprovider/users");

	public static final Uri CONTENT_URI_CHAPTERS = Uri
			.parse("content://com.dipeca.adventurestoriesprovider/"
					+ Chapter.TABLE_NAME);

	public static final Uri CONTENT_URI_STATUS = Uri
			.parse("content://com.dipeca.adventurestoriesprovider/"
					+ Status.TABLE_NAME);

	public static final Uri CONTENT_URI_OBJECTS = Uri
			.parse("content://com.dipeca.adventurestoriesprovider/"
					+ Objects.TABLE_NAME);

	public static final Uri CONTENT_URI_DRAWABLES = Uri
			.parse("content://com.dipeca.adventurestoriesprovider/"
					+ Drawable.TABLE_NAME);

	public static final Uri CONTENT_URI_LEGENDS = Uri
			.parse("content://com.dipeca.adventurestoriesprovider/"
					+ Legend.TABLE_NAME);

	public static final Uri CONTENT_URI_PAGE = Uri
			.parse("content://com.dipeca.adventurestoriesprovider/"
					+ Page.TABLE_NAME);

	public static final Uri CONTENT_URI_PAGEDRAWABLE = Uri
			.parse("content://com.dipeca.adventurestoriesprovider/"
					+ PageDrawable.TABLE_NAME);

	public static final Uri CONTENT_URI_PAGETYPE = Uri
			.parse("content://com.dipeca.adventurestoriesprovider/"
					+ PageType.TABLE_NAME);
	
	public static final Uri CONTENT_URI_STORY = Uri
			.parse("content://com.dipeca.adventurestoriesprovider/"
					+ Story.TABLE_NAME);

	public static final Uri CONTENT_URI_PAGESTORY = Uri
			.parse("content://com.dipeca.adventurestoriesprovider/"
					+ PageStory.TABLE_NAME);

	
	public static final String AUTHORITY = "com.dipeca.adventurestoriesprovider";

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
	public static final int QUIZZ_ALLROWS = 9;

	// build story activity
	public static final int PAGE_ALLROWS = 10;
	public static final int PAGE_SINGLE_ROW = 11;
	public static final int DRAWABLE_ALLROWS = 12;
	public static final int DRAWABLE_SINGLE_ROW = 13;
	public static final int LEGEND_ALLROWS = 14;
	public static final int LEGEND_SINGLE_ROW = 15;
	public static final int PAGETYPE_ALLROWS = 16;
	public static final int PAGETYPE_SINGLE_ROW = 17;
	public static final int DRAWABLEPAGE_ALLROWS = 18;
	public static final int DRAWABLEPAGE_SINGLE_ROWS = 19;
	public static final int STORY_ALLROWS = 20;
	public static final int STORY_SINGLE_ROWS = 21;
	public static final int PAGESTORY_ALLROWS = 22;
	public static final int PAGESTORY_SINGLE_ROWS = 23;
	
	private static final UriMatcher uriMatcher;

	private static final String PROVIDER_NAME = "com.dipeca.adventurestoriesprovider";
	// Populate the UriMatcher object, where a URI ending in
	// ‘elements’ will correspond to a request for all items,
	// and ‘elements/[rowID]’ represents a single row.
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		uriMatcher.addURI(PROVIDER_NAME, User.TABLE_NAME, USER_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, User.TABLE_NAME + "/#",
				USER_SINGLE_ROW);

		uriMatcher.addURI(PROVIDER_NAME, Chapter.TABLE_NAME, CHAPTER_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, Chapter.TABLE_NAME + "/#",
				CHAPTER_SINGLE_ROW);

		uriMatcher.addURI(PROVIDER_NAME, Status.TABLE_NAME, STATUS_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, Status.TABLE_NAME + "/#",
				STATUS_SINGLE_ROW);

		uriMatcher.addURI(PROVIDER_NAME, Objects.TABLE_NAME, OBJECT_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, Objects.TABLE_NAME + "/#",
				OBJECT_SINGLE_ROW);

		uriMatcher.addURI(PROVIDER_NAME, QuestionAnswer.TABLE_NAME,
				QUIZZ_ALLROWS);

		uriMatcher.addURI(PROVIDER_NAME, Drawable.TABLE_NAME, DRAWABLE_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, Drawable.TABLE_NAME + "/#",
				DRAWABLE_SINGLE_ROW);

		uriMatcher.addURI(PROVIDER_NAME, Legend.TABLE_NAME, LEGEND_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, Legend.TABLE_NAME + "/#",
				LEGEND_SINGLE_ROW);

		uriMatcher.addURI(PROVIDER_NAME, Page.TABLE_NAME, PAGE_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, Page.TABLE_NAME + "/#",
				PAGE_SINGLE_ROW);

		uriMatcher.addURI(PROVIDER_NAME, PageDrawable.TABLE_NAME,
				DRAWABLEPAGE_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, PageDrawable.TABLE_NAME + "/#",
				DRAWABLEPAGE_SINGLE_ROWS);

		uriMatcher.addURI(PROVIDER_NAME, Story.TABLE_NAME,
				STORY_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, Story.TABLE_NAME + "/#",
				STORY_SINGLE_ROWS);
		
		uriMatcher.addURI(PROVIDER_NAME, PageStory.TABLE_NAME,
				PAGESTORY_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, PageStory.TABLE_NAME + "/#",
				PAGESTORY_SINGLE_ROWS);
		
		uriMatcher.addURI(PROVIDER_NAME, PageType.TABLE_NAME, PAGETYPE_ALLROWS);
		uriMatcher.addURI(PROVIDER_NAME, PageType.TABLE_NAME + "/#",
				PAGETYPE_SINGLE_ROW);
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
		case QUIZZ_ALLROWS:
			tableName = QuestionAnswer.TABLE_NAME;
			break;
		case LEGEND_ALLROWS:
			tableName = Legend.TABLE_NAME;
			break;
		case LEGEND_SINGLE_ROW:
			tableName = Legend.TABLE_NAME;
			break;
		case DRAWABLE_ALLROWS:
			tableName = Drawable.TABLE_NAME;
			break;
		case DRAWABLE_SINGLE_ROW:
			tableName = Drawable.TABLE_NAME;
			break;
		case PAGETYPE_ALLROWS:
			tableName = PageType.TABLE_NAME;
			break;
		case PAGETYPE_SINGLE_ROW:
			tableName = PageType.TABLE_NAME;
			break;
		case DRAWABLEPAGE_ALLROWS:
			tableName = PageDrawable.TABLE_NAME;
			break;
		case DRAWABLEPAGE_SINGLE_ROWS:
			tableName = PageDrawable.TABLE_NAME;
			break;
		case PAGE_ALLROWS:
			tableName = Page.TABLE_NAME;
			break;
		case STORY_SINGLE_ROWS:
			tableName = Story.TABLE_NAME;
			break;
		case STORY_ALLROWS:
			tableName = Story.TABLE_NAME;
			break;
		case PAGESTORY_SINGLE_ROWS:
			tableName = PageStory.TABLE_NAME;
			break;
		case PAGESTORY_ALLROWS:
			tableName = PageStory.TABLE_NAME;
			break;
		default:
			break;
		}
		Cursor cursor;
		if(PageStory.TABLE_NAME.equals(tableName) || PageDrawable.TABLE_NAME.equals(tableName)){
			cursor = db.rawQuery(selection, selectionArgs);
		}else{
			// Specify the table on which to perform the query. This can
			// be a specific table or a join as required.
			queryBuilder.setTables(tableName);
			// Execute the query.
			cursor = queryBuilder.query(db, projection, selection,
					selectionArgs, groupBy, having, sortOrder);
	
			// Return the result Cursor.
		}
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
		case DRAWABLE_ALLROWS:
			tableName = Drawable.TABLE_NAME;
			content_uri = CONTENT_URI_DRAWABLES;
			break;
		case LEGEND_ALLROWS:
			tableName = Legend.TABLE_NAME;
			content_uri = CONTENT_URI_LEGENDS;
			break;
		case PAGE_ALLROWS:
			tableName = Page.TABLE_NAME;
			content_uri = CONTENT_URI_PAGE;
			break;
		case DRAWABLEPAGE_ALLROWS:
			tableName = PageDrawable.TABLE_NAME;
			content_uri = CONTENT_URI_PAGEDRAWABLE;
			break;
		case PAGESTORY_ALLROWS:
			tableName = PageStory.TABLE_NAME;
			content_uri = CONTENT_URI_PAGESTORY;
			break;
		case PAGESTORY_SINGLE_ROWS:
			tableName = PageStory.TABLE_NAME;
			content_uri = CONTENT_URI_PAGESTORY;
			break;
		case STORY_ALLROWS:
			tableName = Story.TABLE_NAME;
			content_uri = CONTENT_URI_STORY;
			break;
		case STORY_SINGLE_ROWS:
			tableName = Story.TABLE_NAME;
			content_uri = CONTENT_URI_STORY;
			break;
		default:
			break;
		}

		long id = db.insertWithOnConflict(tableName, nullColumnHack, values,
				SQLiteDatabase.CONFLICT_REPLACE);
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
			// USER WANTS TO DELETE A SPECIFIC ID
			String finalWhere = User.ID + "=" + ssid;
			// IF USER SPECIFIES WHERE FILTER THEN APPEND
			if (where != null) {
				finalWhere = finalWhere + " AND " + where;
			}
			count = db.delete(User.TABLE_NAME, finalWhere, whereArgs);
			break;
		case CHAPTER_ALLROWS:
			// PERFORM REGULAR DELETE
			count = db.delete(Chapter.TABLE_NAME, where, whereArgs);
			break;
		case OBJECT_ALLROWS:
			// PERFORM REGULAR DELETE
			count = db.delete(Objects.TABLE_NAME, where, whereArgs);
			break;
		case PAGE_ALLROWS:
			// PERFORM REGULAR DELETE
			count = db.delete(Page.TABLE_NAME, where, whereArgs);
			break;
		case PAGE_SINGLE_ROW:
			// PERFORM REGULAR DELETE
			count = db.delete(Page.TABLE_NAME, where, whereArgs);
			break;
		case STORY_SINGLE_ROWS:
			// PERFORM REGULAR DELETE
			count = db.delete(Story.TABLE_NAME, where, whereArgs);
			break;
		case STORY_ALLROWS:
			// PERFORM REGULAR DELETE
			count = db.delete(Story.TABLE_NAME, where, whereArgs);
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
		public static final int DATABASE_VERSION = 3;

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
					+ Chapter.ICON_PATH + " TEXT," + Chapter.CURRENT_CLASS
					+ " TEXT," + " FOREIGN KEY (" + Chapter.GAME_ID
					+ ") REFERENCES " + Game.TABLE_NAME + " (" + Game.ID
					+ "));");

			// CREATE STATUS TABLE
			db.execSQL("CREATE TABLE " + Status.TABLE_NAME + " (" + Status.ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Status.ENERGY
					+ " INTEGER," + Status.GAME_ID + " INTEGER,"
					+ Status.CURRENTCHAPTER + " TEXT," + Status.POINTS
					+ " INTEGER ," + " FOREIGN KEY (" + Status.GAME_ID
					+ ") REFERENCES " + Game.TABLE_NAME + " (" + Game.ID
					+ "));");

			// CREATE OBJECTS TABLE
			db.execSQL("CREATE TABLE " + Objects.TABLE_NAME + " (" + Objects.ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Objects.NAME
					+ " TEXT," + Objects.GAME_ID + " INTEGER,"
					+ Objects.IMAGE_PATH + " TEXT," + Objects.TYPE
					+ " INTEGER," + Objects.IS_SHOW + " INTEGER,"
					+ " FOREIGN KEY (" + Objects.GAME_ID + ") REFERENCES "
					+ Game.TABLE_NAME + " (" + Game.ID + "));");

			// CREATE STORY TABLE
			db.execSQL("CREATE TABLE " + Story.TABLE_NAME + " ("
					+ Story.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ Story.TITLE + " INTEGER,"+ Story.DRAWABLE + " INTEGER);");
			
			// CREATE PAGE_TYPE TABLE
			db.execSQL("CREATE TABLE " + PageType.TABLE_NAME + " ("
					+ PageType.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ PageType.NAME + " INTEGER);");

			// CREATE PAGE TABLE
			db.execSQL("CREATE TABLE " + Page.TABLE_NAME + " (" + Page.ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Page.TITLE
					+ " TEXT," + Page.TYPE_ID + " INTEGER, FOREIGN KEY ("
					+ Page.TYPE_ID + ") REFERENCES " + PageType.TABLE_NAME
					+ " (" + PageType.ID + "));");

			// CREATE DRAWABLE DRAWING TABLE
			db.execSQL("CREATE TABLE " + Drawable.TABLE_NAME + " ("
					+ Drawable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ Drawable.NAME + " TEXT);");

			// CREATE LEGEND TABLE
			db.execSQL("CREATE TABLE " + Legend.TABLE_NAME + " (" + Legend.ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Legend.ORDER
					+ " INTEGER," + Legend.VALUE + " TEXT, " + Legend.PAGE_ID
					+ " INTEGER,  FOREIGN KEY (" + Legend.PAGE_ID
					+ ") REFERENCES " + Page.TABLE_NAME + " (" + Page.ID
					+ "));");

			// CREATE PAGE_DRAWABLE TABLE
			db.execSQL("CREATE TABLE " + PageDrawable.TABLE_NAME + " ("
					+ PageDrawable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ PageDrawable.ORDER + " INTEGER," + PageDrawable.PAGE_ID
					+ " INTEGER," + PageDrawable.DRAWABLE_ID + " INTEGER,"
					+ "FOREIGN KEY (" + PageDrawable.PAGE_ID + ") REFERENCES "
					+ Page.TABLE_NAME + " (" + Page.ID + "),"
					+ " FOREIGN KEY (" + PageDrawable.DRAWABLE_ID
					+ ") REFERENCES " + Drawable.TABLE_NAME + " ("
					+ Drawable.ID + "));");

			// CREATE PAGE_story TABLE
			db.execSQL("CREATE TABLE " + PageStory.TABLE_NAME + " ("
					+ PageStory.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ PageStory.ORDER + " INTEGER," + PageStory.PAGE_ID
					+ " INTEGER," + PageStory.STORY_ID + " INTEGER,"
					+ "FOREIGN KEY (" + PageStory.STORY_ID + ") REFERENCES "
					+ Story.TABLE_NAME + " (" + Story.ID + "),"
					+ " FOREIGN KEY (" + PageStory.PAGE_ID
					+ ") REFERENCES " + Page.TABLE_NAME + " ("
					+ Page.ID + "));");
			
			insertDrawables(db);
			insertPageTypes(db);
		}

		/**
		 * @param db
		 * 
		 *            Insert into database all drawables of the application
		 */
		private void insertDrawables(SQLiteDatabase db) {

			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(1,'village');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(2,'quarto');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(3,'kingdom');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(4,'lagoteste2');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(5,'caminho_dia');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(6,'algo_a_mexer_1');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(7,'caminho_dia_watching');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(8,'caminho_dia_scarecrow');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(9,'choice');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(10,'cofre_aberto');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(11,'cofre_fechado');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(12,'companheira_presa');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(13,'gate');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(14,'espantalho1_1');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(15,'quarto_vazio_escuro');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(16,'quarto_vazio');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(17,'robot');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(18,'robot_attack1_scal');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(19,'robot_destroyed1234');");
			db.execSQL("INSERT INTO " + Drawable.TABLE_NAME
					+ " VALUES(20,'quarto_olhar_talisma');");
		}
		
		/**
		 * @param db
		 * 
		 *            Insert into database all drawables of the application
		 */
		private void insertPageTypes(SQLiteDatabase db) {
			db.execSQL("INSERT INTO " + PageType.TABLE_NAME
					+ " VALUES(1,'single');");
			db.execSQL("INSERT INTO " + PageType.TABLE_NAME
					+ " VALUES(2,'double');");
			db.execSQL("INSERT INTO " + PageType.TABLE_NAME
					+ " VALUES(3,'intense');");
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
			db.execSQL("DROP TABLE IF EXISTS " + QuestionAnswer.TABLE_NAME);
			// Table to build the story
			db.execSQL("DROP TABLE IF EXISTS " + Drawable.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Legend.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Page.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + PageDrawable.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + PageType.TABLE_NAME);
			// CREATE NEW INSTANCE OF SCHEMA
			onCreate(db);

		}
	}

}
