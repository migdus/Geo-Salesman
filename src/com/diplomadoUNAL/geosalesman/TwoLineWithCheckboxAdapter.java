package com.diplomadoUNAL.geosalesman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class TwoLineWithCheckboxAdapter extends
				ArrayAdapter<HashMap<String, String>> {

	// boolean array for storing
	// the state of each CheckBox
	boolean[] checkBoxState;

	ViewHolder viewHolder;
	List<HashMap<String, String>> objects;

	Button positiveButton;

	ArrayList<CheckBox> allCheckBoxes;

	public TwoLineWithCheckboxAdapter(Context context, int textViewResourceId,
					ArrayList<HashMap<String, String>> data,
					final Button positiveButton) {
		super(context, textViewResourceId, data);
		this.positiveButton = positiveButton;
		checkBoxState = new boolean[data.size()];
		this.objects = data;
		allCheckBoxes=new ArrayList<CheckBox>();
	}

	// class for caching the views in a row
	private class ViewHolder {
		TextView text1, text2;
		CheckBox checkBox;
	}

	public void changeCheckboxesState(int visibility) {
		for (int i = 0; i < allCheckBoxes.size(); i++) {
			allCheckBoxes.get(i).setVisibility(visibility);
		}
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
		viewHolder.checkBox.setVisibility(View.GONE);
		allCheckBoxes.add(viewHolder.checkBox);

		// for managing the state of the boolean
		// array according to the state of the
		// CheckBox

		viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					checkBoxState[position] = true;
					positiveButton.setEnabled(true);

				} else {
					checkBoxState[position] = false;
					positiveButton.setEnabled(false);
					for (int i = 0; i < checkBoxState.length; i++) {
						if (checkBoxState[i]) {
							positiveButton.setEnabled(true);
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
