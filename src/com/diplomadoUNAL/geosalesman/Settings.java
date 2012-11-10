package com.diplomadoUNAL.geosalesman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class Settings extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		ListView listViewMainMenu = (ListView) this.findViewById(R.id.listView);

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		Map<String, String> datum = new HashMap<String, String>(2);
		datum.put("Title", getResources().getString(R.string.questions));
		datum.put("Description",
						getResources().getString(
										R.string.questions_settings_short_description));
		data.add(datum);

		Map<String, String> datum2 = new HashMap<String, String>(2);
		datum2.put("Title", getResources().getString(R.string.clients));
		datum2.put("Description",
						getResources().getString(
										R.string.clients_settings_short_description));
		data.add(datum2);

		datum2 = new HashMap<String, String>(2);
		datum2.put("Title", getResources().getString(R.string.report_templates));
		datum2.put("Description",
						getResources().getString(
										R.string.report_templates_settings_short_description));
		data.add(datum2);

		SimpleAdapter adapter = new SimpleAdapter(this, data,
						android.R.layout.simple_list_item_2, new String[] {
								"Title", "Description" }, new int[] {
								android.R.id.text1, android.R.id.text2 });
		listViewMainMenu.setAdapter(adapter);
		listViewMainMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {

				String selectedOption = (String) ((TwoLineListItem) arg1).getText1()
								.getText();

				Intent intent = new Intent(Settings.this, CRUDObject.class);
				//Setup menu items
				intent= intent.putExtra(CRUDObject.CRUD_OBJ_ENABLED_ADD_MENU_ITEM, true)
								.putExtra(CRUDObject.CRUD_OBJ_ENABLED_DELETE_MENU_ITEM,
												true);
				
				
				if (selectedOption.equals(getResources().getString(R.string.questions))) {
					//Setup database elements
					intent.putExtra(CRUDObject.CRUD_OBJ_QUERY_SOURCE, CRUDObject.QUESTIONS);
					//start intent activity
					startActivityForResult(intent, 1);
				} else if (selectedOption.equals(getResources().getString(
								R.string.clients))) {
					// TODO
					Toast.makeText(Settings.this, selectedOption, Toast.LENGTH_LONG)
									.show();
				} else if (selectedOption.equals(getResources().getString(
								R.string.report_templates))) {
					// TODO
					Toast.makeText(Settings.this, selectedOption, Toast.LENGTH_LONG)
									.show();
				}
			
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}

}
