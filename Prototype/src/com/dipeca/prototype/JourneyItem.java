package com.dipeca.prototype;

import java.util.Date;

import android.graphics.drawable.Drawable;

public class JourneyItem {
	private Long id;
	private String current;
	private String currentClass;
	private Date date;
	private String previous;
	private Drawable icon;
	private String iconName;
	
	public JourneyItem() {
		super();
	}

	public JourneyItem(Long id, String current, String currentClass, Date date,
			String previous, Drawable icon, String iconName) {
		super();
		this.id = id;
		this.current = current;
		this.currentClass = currentClass;
		this.date = date;
		this.previous = previous;
		this.icon = icon;
		this.iconName = iconName;
	}

	public String getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(String currentClass) {
		this.currentClass = currentClass;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

}
