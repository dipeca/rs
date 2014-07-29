package com.dipeca.bookactivity;

import java.util.ArrayList;

import com.dipeca.prototype.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ObjectItemImageAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ObjectItem> list;

	public ObjectItemImageAdapter(Context context,
			ArrayList<ObjectItem> mobileValues) {
		this.context = context;
		this.list = mobileValues;
	}

	private View gridView;

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {

			gridView = new View(context);

			// get layout from menu.xml
			gridView = inflater.inflate(R.layout.grid, null);
		} else {
			gridView = (View) convertView;
		}
		// set value into textview
		TextView textView = (TextView) gridView
				.findViewById(R.id.grid_item_label);
		textView.setText(list.get(position).getName());

		// set image based on selected text
		ImageView imageView = (ImageView) gridView
				.findViewById(R.id.grid_item_image);

		String name = list.get(position).getName();

		list.get(position).setBitmap(
				loadCardImage(list.get(position).getObjectImageType()));

		imageView.setImageBitmap(list.get(position).getBitmap());

		return gridView;
	}

	int density = 1;

	private Bitmap loadCardImage(int type) {
		density = (int) gridView.getResources().getDisplayMetrics().density;
		Bitmap bitmap1 = null;
		switch (type) {
		case ObjectItem.TYPE_BOOK_OF_SPELS:

			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.book_spells,
					256 * density, 256 * density);

			return bitmap1;

		case ObjectItem.TYPE_ROPE:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.rope, 256 * density,
					256 * density);

			return bitmap1;

		case ObjectItem.TYPE_PLANK:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.plank, 256 * density,
					256 * density);

			return bitmap1;

		case ObjectItem.TYPE_AMULET:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.talisma, 256 * density,
					256 * density);

			return bitmap1;

		case ObjectItem.TYPE_BOTTLE:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.frasco, 256 * density,
					256 * density);

			return bitmap1;

		case ObjectItem.TYPE_CORN:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.milho, 256 * density,
					256 * density);

			return bitmap1;

		case ObjectItem.TYPE_PAGE_VILLAGE:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.village, 128 * density,
					128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_BEDROOM:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.quarto, 128 * density,
					128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_KINGDOM:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.kingdom, 128 * density,
					128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_LAKE:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.lagoteste2,
					128 * density, 128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_WALK:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.caminho_dia,
					128 * density, 128 * density);

			return bitmap1;

		case ObjectItem.TYPE_PAGE_SOMETHING_MOVING:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.algo_a_mexer_1,
					128 * density, 128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_PATH_WATCHING:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.caminho_dia_watching,
					128 * density, 128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_PATH:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.caminho_dia_scarecrow,
					128 * density, 128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_CHOICE:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.choice, 128 * density,
					128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_VAULT_OPEN:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.cofre_aberto,
					128 * density, 128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_VAULT_CLOSED:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.cofre_fechado,
					128 * density, 128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_FIND_FRIEND:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.companheira_presa,
					128 * density, 128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_GATE:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.gate, 128 * density,
					128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_SCARECROW:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.espantalho1_1,
					128 * density, 128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_BEDROOM_DARK:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.quarto_vazio_escuro,
					128 * density, 128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_BEDROOM_EMPTY:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.quarto_vazio,
					128 * density, 128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_ROBOT:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.robot, 128 * density,
					128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_ROBOT_ATTACK:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.robot_attack1_scal,
					128 * density, 128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_LOPO:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.robot_destroyed1234,
					128 * density, 128 * density);

			return bitmap1;
		case ObjectItem.TYPE_PAGE_BEDROOM_AMULET:
			bitmap1 = Utils.decodeSampledBitmapFromResource(
					gridView.getResources(), R.drawable.quarto_olhar_talisma,
					128 * density, 128 * density);

			return bitmap1;
		}

		return null;
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}

		return 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).getId();
	}

}