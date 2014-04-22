package com.dipeca.prototype;

import java.util.Date;

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

	public static final int TYPE_BOOK = 1;
	public static final int TYPE_PAPER = 2;
	public static final int TYPE_BOTTLE = 3;
	public static final int TYPE_MASK = 4;
	public static final int TYPE_AMULET = 5;

	private Long id;
	private String name;
	private int objectImageType;

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

}
