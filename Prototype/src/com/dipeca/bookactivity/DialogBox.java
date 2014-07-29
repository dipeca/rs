package com.dipeca.bookactivity;

import com.dipeca.prototype.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogBox extends FrameLayout {

	private TextView text;
	private ImageView iv1;
	private ImageView iv2;

	private String textDialog;
	private Drawable img1Id;
	private Drawable img2Id;

	public Drawable getImg1Id() {
		return img1Id;
	}

	public void setImg1Id(Drawable img1Id) {
		this.img1Id = img1Id;
		
		setImageViews();
	}

	public Drawable getImg2Id() {
		return img2Id;
	}

	public void setImg2Id(Drawable img2Id) {
		this.img2Id = img2Id;
		
		setImageViews();
	}

	public String getTextDialog() {
		return textDialog;
	}

	public void setTextDialog(String textDialog) {
		this.textDialog = textDialog;
		
		if(text != null){
			text.setText(textDialog);
		}
	}

	public DialogBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// get attrs if setted
		TypedArray arr = context.obtainStyledAttributes(attrs,
				R.styleable.DialogBox);
		String textToShow = arr.getString(R.styleable.DialogBox_textDialog);
		if (textToShow != null) {
			setTextDialog(textToShow);
		}
		setImg1Id(arr.getDrawable(R.styleable.DialogBox_img1Dialog));
		setImg2Id(arr.getDrawable(R.styleable.DialogBox_img2Dialog));
		arr.recycle(); // Do this when done.

		initDialog();
	}

	public DialogBox(Context context, AttributeSet attrs) {
		super(context, attrs);

		// get attrs if setted
		TypedArray arr = context.obtainStyledAttributes(attrs,
				R.styleable.DialogBox);
		String textToShow = arr.getString(R.styleable.DialogBox_textDialog);
		if (textToShow != null) {
			setTextDialog(textToShow);
		}
 
		setImg1Id(arr.getDrawable(R.styleable.DialogBox_img1Dialog));
		setImg2Id(arr.getDrawable(R.styleable.DialogBox_img2Dialog)); 
		arr.recycle(); // Do this when done.

		initDialog();
	}

	public DialogBox(Context context) {
		super(context);
		initDialog();
	}

	AnimationDrawable backGroundChangeAnimJake;
	AnimationDrawable backGroundChangeAnimGui;

	private void initDialog() {

		// Inflate the view from the layout resource.
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;

		li = (LayoutInflater) getContext().getSystemService(infService);
		li.inflate(R.layout.dialog_text, this, true);

		int density = (int) getResources().getDisplayMetrics().density;
		
		// Get references to the child controls.
		text = (TextView) findViewById(R.id.text);

		text.setText(textDialog);
		text.setPadding(56*density, 4*density, 56*density,4*density);

		setImageViews();

	}
	
	private void setImageViews(){
		
		iv1 = (ImageView) findViewById(R.id.img1);
		iv2 = (ImageView) findViewById(R.id.img2);
		
		if (iv1 != null && getImg1Id() != null) {
			iv1.setImageDrawable(getImg1Id());
			if (iv1.getDrawable() instanceof AnimationDrawable) {
				backGroundChangeAnimJake = (AnimationDrawable) iv1
						.getDrawable();
			}
			if (backGroundChangeAnimJake != null) {
				backGroundChangeAnimJake.start();
			}
			iv1.setVisibility(View.VISIBLE);
		}else if (iv1 != null){
			iv1.setVisibility(View.GONE);
		}

		if (iv2 != null &&  getImg2Id() != null) {
			iv2.setImageDrawable(getImg2Id());

			if (iv2.getDrawable() instanceof AnimationDrawable) {
				backGroundChangeAnimGui = (AnimationDrawable) iv2
						.getDrawable();
			}

			if (backGroundChangeAnimGui != null) {
				backGroundChangeAnimGui.start();
			}
			iv2.setVisibility(View.VISIBLE);
		}else if (iv2 != null){
			iv2.setVisibility(View.GONE);
		}
	}

}
