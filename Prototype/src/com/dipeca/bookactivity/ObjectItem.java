package com.dipeca.bookactivity;

import java.util.Date;

import android.graphics.Bitmap;

public class ObjectItem {

	public ObjectItem() {
		super();
	}

	public ObjectItem(Long id, String name, int objectImageType, Date created) {
		super();
		this.id = id;
		this.name = name;
		this.objectImageType = objectImageType;
		this.created = created;
	}

	public ObjectItem(Long id, String name, Bitmap image, Date created) {
		super();
		this.id = id;
		this.name = name;
		this.bitmap = image;
		this.created = created;
	}

	public ObjectItem(Long id, String name, Bitmap image, Date created, int idDrawable) {
		super();
		this.id = id;
		this.name = name;
		this.bitmap = image;
		this.created = created;
		this.idDrawable = idDrawable;
	}
	
	public static final int TYPE_BOOK_OF_SPELS = 1;
	public static final int TYPE_PAPER = 2;
	public static final int TYPE_BOTTLE = 3;
	public static final int TYPE_ROPE = 4;
	public static final int TYPE_AMULET = 5;
	public static final int TYPE_PLANK = 6;
	public static final int TYPE_BOOK_OF_SCIENCES = 7;
	public static final int TYPE_CORN = 8;
	
	public static final int TYPE_PAGE_VILLAGE = 9;
	public static final int TYPE_PAGE_BEDROOM = 10;
	public static final int TYPE_PAGE_KINGDOM = 11;
	public static final int TYPE_PAGE_LAKE = 12;
	public static final int TYPE_PAGE_WALK = 13;
	public static final int TYPE_PAGE_SOMETHING_MOVING = 14;
	public static final int TYPE_PAGE_PATH_WATCHING = 15;
	public static final int TYPE_PAGE_PATH = 16;
	public static final int TYPE_PAGE_CHOICE = 17;
	public static final int TYPE_PAGE_VAULT_OPEN = 18;
	public static final int TYPE_PAGE_VAULT_CLOSED = 19;
	public static final int TYPE_PAGE_FIND_FRIEND = 20;
	public static final int TYPE_PAGE_GATE = 21;
	public static final int TYPE_PAGE_SCARECROW = 22;
	public static final int TYPE_PAGE_BEDROOM_DARK = 23;
	public static final int TYPE_PAGE_BEDROOM_EMPTY = 24;
	public static final int TYPE_PAGE_ROBOT = 25;
	public static final int TYPE_PAGE_ROBOT_ATTACK = 26;
	public static final int TYPE_PAGE_LOPO = 27;
	public static final int TYPE_PAGE_BEDROOM_AMULET = 28;
	
	private Long id;
	private String name;
	private int objectImageType;
	private int idDrawable;

	public int getObjectImageType() {
		return objectImageType;
	}

	public void setObjectImageType(int objectImageType) {
		this.objectImageType = objectImageType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	Date created;
	
	Bitmap bitmap = null;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public int getIdDrawable() {
		return idDrawable;
	}

	public void setIdDrawable(int idDrawable) {
		this.idDrawable = idDrawable;
	}

}
