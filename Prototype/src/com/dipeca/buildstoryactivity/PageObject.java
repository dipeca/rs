package com.dipeca.buildstoryactivity;

import android.graphics.drawable.Drawable;

import com.dipeca.item.IListItem;

public class PageObject implements IListItem{
	private Long id;
	private Long order;
	private Long idPrevPage;
	private Long idNextPage;
	private Drawable icon;
	private String title;
	private int type;
	private Long storyId;
	private int drawable1Id;
	private int drawable2Id;
	private String legend1Id;
	private String legend2Id;
	private int iconID;
	private String drawable1Path;
	private String drawable2Path;
	private String drawable1Name;
	private String drawable2Name;

	public PageObject(Long id, Long order, Drawable icon, String title, int type, Long storyId, int iconID) {
		super();
		this.id = id;
		this.order = order;
		this.icon = icon;
		this.title = title;
		this.type = type;
		this.storyId = storyId;
		this.iconID = iconID;
	}

	public Long getStoryId() {
		return storyId;
	}

	public void setStoryId(Long storyId) {
		this.storyId = storyId;
	}

	public PageObject() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public Long getIdPrevPage() {
		return idPrevPage;
	}

	public void setIdPrevPage(Long idPrevPage) {
		this.idPrevPage = idPrevPage;
	}

	public Long getIdNextPage() {
		return idNextPage;
	}

	public void setIdNextPage(Long idNextPage) {
		this.idNextPage = idNextPage;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String getCurrent() {
		return title;
	}

	public int getDrawable1Id() {
		return drawable1Id;
	}

	public void setDrawable1Id(int drawable1Id) {
		this.drawable1Id = drawable1Id;
	}

	public int getDrawable2Id() {
		return drawable2Id;
	}

	public void setDrawable2Id(int drawable2Id) {
		this.drawable2Id = drawable2Id;
	}

	@Override
	public String getLegend1Id() {
		return legend1Id;
	}

	@Override
	public String getLegend2Id() {
		return legend2Id;
	}

	@Override
	public void setLegend1Id(String var) {
		legend1Id = var;
		
	}

	@Override
	public void setLegend2Id(String var) {
		legend2Id = var;
		
	}

	public int getIconID() {
		return iconID;
	}

	public void setIconID(int val) {
		this.iconID = val;
	}
	
	public void setDrawable1Path(String var) {

	}

	public void setDrawable2Path(String var) {

	}

	public String getDrawable1Path() {
		return null;
	}

	public String getDrawable2Path() {
		return null;
	}
	
	
	public void setDrawable1Name(String var) {
		this.drawable1Name = var;
	}

	public String getDrawable1Name() {
		return drawable1Name;
	}
	
	public void setDrawable2Name(String var) {
		this.drawable2Name = var;
	}

	public String getDrawable2Name() {
		return drawable2Name;
	}
	
}
