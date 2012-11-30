package com.diplomadoUNAL.geosalesman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.diplomadoUNAL.geosalesman.database.PopulateWithExamples;
import com.diplomadoUNAL.geosalesman.database.SchemaHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.TwoLineListItem;

public class GeoSalesman extends Activity implements OnItemClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ListView listViewMainMenu = (ListView) this
						.findViewById(R.id.listView_main_menu);

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		Map<String, String> datum = new HashMap<String, String>(2);
		datum.put("Title", getResources().getString(R.string.new_report));
		datum.put("Description",
						getResources().getString(
										R.string.new_report_short_description));
		data.add(datum);

		Map<String, String> datum4 = new HashMap<String, String>(2);
		datum4.put("Title", getResources().getString(R.string.settings));
		datum4.put("Description",
						getResources().getString(
										R.string.settings_short_description));
		data.add(datum4);

		SimpleAdapter adapter = new SimpleAdapter(this, data,
						android.R.layout.simple_list_item_2, new String[] {
								"Title", "Description" }, new int[] {
								android.R.id.text1, android.R.id.text2 });
		listViewMainMenu.setAdapter(adapter);

		listViewMainMenu.setOnItemClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String selectedOption = (String) ((TwoLineListItem) arg1).getText1()
						.getText();

		if (selectedOption
						.equals(getResources().getString(R.string.new_report))) {
			Intent intent = new Intent(GeoSalesman.this, AddNewReport.class);
			startActivity(intent);
		} else if (selectedOption.equals(getResources().getString(
						R.string.stored_report)))
			Toast.makeText(GeoSalesman.this, selectedOption, Toast.LENGTH_LONG)
							.show();
		else if (selectedOption.equals(getResources().getString(
						R.string.send_reports)))
			Toast.makeText(GeoSalesman.this, selectedOption, Toast.LENGTH_LONG)
							.show();
		else if (selectedOption.equals(getResources().getString(
						R.string.settings))) {
			Intent intent = new Intent(GeoSalesman.this, Settings.class);
			startActivity(intent);
		}
	}
}
