package com.diplomadoUNAL.geosalesman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.diplomadoUNAL.geosalesman.database.QuestionTable;
import com.diplomadoUNAL.geosalesman.database.SchemaHelper;

public class CRUDObject extends Activity {
	Button positiveButton;
	Button negativeButton;
	TwoLineWithCheckboxAdapter twoLineWithCheckboxAdapter;

	public final static String CRUD_OBJ_QUERY_SOURCE = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_QUERY_TYPE";
	public final static String QUESTIONS = "QUESTIONS"; // get question titles and questions

	public final static String CRUD_OBJ_ENABLED_ADD_MENU_ITEM = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_ENABLED_ADD_MENU_ITEM";
	public final static String CRUD_OBJ_ENABLED_DELETE_MENU_ITEM = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_ENABLED_DELETE_MENU_ITEM";
	public final static String CRUD_OBJ_ENABLED_SELECT_MENU_ITEM = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_ENABLED_SELECT_MENU_ITEM";

	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crudobject);

		positiveButton = ((Button) this.findViewById(R.id.crud_button_positive));
		negativeButton = ((Button) this.findViewById(R.id.crud_button_cancel));

		ListView listViewShowQuestions = (ListView) this
						.findViewById(R.id.listView_show_question_menu);

		SchemaHelper schemaHelper = new SchemaHelper(this);
		schemaHelper.onUpgrade(schemaHelper.getWritableDatabase(), 1, 1);
		intent = getIntent();

		ArrayList<HashMap<String, String>> queryResults = null;
		String row1 = null;
		String row2 = null;
		String rowId = null;

		if (intent.hasExtra(CRUD_OBJ_QUERY_SOURCE)
						&& intent.getStringExtra(CRUD_OBJ_QUERY_SOURCE).equals(
										QUESTIONS)) {
			queryResults = schemaHelper.getQuestionsTitlesAndDescriptions();
			row1 = QuestionTable.QUESTION_TITLE;
			row2 = QuestionTable.QUESTION;
			rowId = QuestionTable.ID;
		} else {
			// TODO Implement for other data sources: clients, etc...
		}

		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

		final HashMap<Integer, String> dbIdListViewRelationship = new HashMap<Integer, String>();
		int listViewIndex = 0;
		if (queryResults.size() > 0) {
			Iterator<HashMap<String, String>> iterQueryResults = queryResults
							.iterator();
			while (iterQueryResults.hasNext()) {
				HashMap<String, String> row = iterQueryResults.next();
				HashMap<String, String> datum = new HashMap<String, String>(2);
				datum.put("text1", row.get(row1));
				datum.put("text2", row.get(row2));
				dbIdListViewRelationship.put(listViewIndex, row.get(rowId));
				data.add(datum);
				listViewIndex++;
			}
		} else {
			HashMap<String, String> datum = new HashMap<String, String>(2);
			datum.put("text1",
							getResources().getString(
											R.string.no_questions_found));
			datum.put("text2",
							getResources().getString(
											R.string.tap_question_menu_to_add));
			data.add(datum);
		}

		twoLineWithCheckboxAdapter = new TwoLineWithCheckboxAdapter(this,
						R.layout.simple_list_item_2_with_checkbox, data,
						positiveButton);
		listViewShowQuestions.setAdapter(twoLineWithCheckboxAdapter);
		twoLineWithCheckboxAdapter.changeCheckboxesState(View.GONE);

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_crudobject, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();

		if (intent.getBooleanExtra(CRUD_OBJ_ENABLED_ADD_MENU_ITEM, false)) {
			menu.add(R.string.add_item).setEnabled(true);
		}
		if (intent.getBooleanExtra(CRUD_OBJ_ENABLED_DELETE_MENU_ITEM, false)) {
			menu.add(R.string.delete_items).setEnabled(true);
		}
		if (intent.getBooleanExtra(CRUD_OBJ_ENABLED_SELECT_MENU_ITEM, false))
			menu.add(R.string.select_items).setEnabled(true);
			return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.activity_crudobject_delete:

			positiveButton.setText(R.string.delete_items);
			twoLineWithCheckboxAdapter.changeCheckboxesState(View.VISIBLE);
			return true;

		case R.id.activity_crudobject_select:
			positiveButton.setText(R.string.add_item);
			twoLineWithCheckboxAdapter.changeCheckboxesState(View.VISIBLE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
