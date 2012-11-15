package com.diplomadoUNAL.geosalesman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TwoLineWithCheckboxAdapter extends
				ArrayAdapter<HashMap<String, String>> {

	// boolean array for storing
	// the state of each CheckBox
	boolean[] checkBoxState;

	ViewHolder viewHolder;
	List<HashMap<String, String>> objects;

	LinearLayout floatingBar;

	/**
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param data
	 * @param floatingBar
	 * @param checkedItemsId
	 *            the items that should be checked, according with their database id. <code>null</code> if none.
	 */
	public TwoLineWithCheckboxAdapter(Context context, int textViewResourceId,
					ArrayList<HashMap<String, String>> data,
					LinearLayout floatingBar, ArrayList<Integer> checkedItemsId) {
		super(context, textViewResourceId, data);
		this.floatingBar = floatingBar;
		checkBoxState = new boolean[data.size()];
		this.objects = data;

		
		if (checkedItemsId != null)
			for (int i = 0; i < data.size(); i++) {
				int dbId = Integer.parseInt(data.get(i).get("dbId"));
				for (int j = 0; j < checkedItemsId.size(); j++) {
					if (dbId == checkedItemsId.get(j))
						checkBoxState[i] = true;
				}
			}
	}

	// class for caching the views in a row
	private class ViewHolder {
		TextView text1, text2;
		CheckBox checkBox;
	}

	public ArrayList<HashMap<String, String>> getChecked() {
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < checkBoxState.length; i++) {
			if (checkBoxState[i] == true) {
				result.add(objects.get(i));
			}
		}
		return result;
	}

	/**
	 * 
	 * @param text1Value
	 * @param text2Value
	 * @return <code>null</code> if not found
	 */
	public String getDbId(String text1Value, String text2Value) {
		for (int i = 0; i < objects.size(); i++) {
			HashMap<String, String> element = objects.get(i);
			if (element.get("text1").equals(text1Value)
							&& element.get("text2").equals(text2Value)) {
				return element.get("dbId");
			}
		}
		return null;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
							R.layout.simple_list_item_2_with_checkbox, null);
			viewHolder = new ViewHolder();

			// cache the views
			viewHolder.text1 = (TextView) convertView
							.findViewById(R.id.simple_list_item_2_with_checkbox_text1);
			viewHolder.text2 = (TextView) convertView
							.findViewById(R.id.simple_list_item_2_with_checkbox_text2);
			viewHolder.checkBox = (CheckBox) convertView
							.findViewById(R.id.simple_list_item_2_with_checkbox_checkBox);

			// link the cached views to the convertview
			convertView.setTag(viewHolder);

		} else
			viewHolder = (ViewHolder) convertView.getTag();

		// set the data to be displayed

		viewHolder.text1.setText(objects.get(position).get("text1").toString());
		viewHolder.text2.setText(objects.get(position).get("text2").toString());
		viewHolder.checkBox.setChecked(checkBoxState[position]);

		// for managing the state of the boolean
		// array according to the state of the
		// CheckBox

		viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					checkBoxState[position] = true;
					floatingBar.setVisibility(View.VISIBLE);

				} else {
					checkBoxState[position] = false;
					floatingBar.setVisibility(View.GONE);
					for (int i = 0; i < checkBoxState.length; i++) {
						if (checkBoxState[i]) {
							floatingBar.setVisibility(View.VISIBLE);
							break;
						}

					}
				}

			}
		});

		// return the view to be displayed
		return convertView;
	}

}
