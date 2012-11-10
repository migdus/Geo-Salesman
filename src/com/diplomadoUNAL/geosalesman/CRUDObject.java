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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.diplomadoUNAL.geosalesman.database.QuestionTable;
import com.diplomadoUNAL.geosalesman.database.SchemaHelper;

public class CRUDObject extends Activity {
	Button positiveButton;
	Button negativeButton;
	TwoLineWithCheckboxAdapter twoLineWithCheckboxAdapter;

	public final static String QUERY_SOURCE = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_QUERY_TYPE";
	public final static String QUESTIONS = "QUESTIONS"; // get question titles and questions

	public final static String ADD_MENU_ITEM_ENABLED = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_ENABLED_ADD_MENU_ITEM";
	public final static String DELETE_MENU_ITEM_ENABLED = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_ENABLED_DELETE_MENU_ITEM";
	public final static String SELECT_MENU_ITEM_ENABLED = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_ENABLED_SELECT_MENU_ITEM";

	public final static String ACTIVITY_TITLE = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_ACTIVITY_TITLE";

	Intent receivedIntent;
	SchemaHelper schemaHelper;
	ListView listViewItems;
	
	ArrayList<HashMap<String, String>> data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crudobject);

		positiveButton = ((Button) this.findViewById(R.id.crud_button_positive));
		negativeButton = ((Button) this.findViewById(R.id.crud_button_cancel));

		listViewItems = (ListView) this
						.findViewById(R.id.listView_show_question_menu);

		schemaHelper = new SchemaHelper(this);

		receivedIntent = getIntent();
	};

	@Override
	protected void onStart() {
		super.onStart();

		setTitle(receivedIntent.getStringExtra(ACTIVITY_TITLE));
		refreshListView();

	}

	private void refreshListView() {
		ArrayList<HashMap<String, String>> queryResults = null;
		String row1 = null;
		String row2 = null;
		String rowId = null;

		if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(QUESTIONS)) {
			queryResults = schemaHelper.getQuestionsTitlesAndDescriptions();
			row1 = QuestionTable.QUESTION_TITLE;
			row2 = QuestionTable.QUESTION_DESCRIPTION;
			rowId = QuestionTable.ID;
		} else {
			// TODO Implement for other data sources: clients, etc...
		}

		data = new ArrayList<HashMap<String, String>>();

		int listViewIndex = 0;
		if (queryResults.size() > 0) {
			Iterator<HashMap<String, String>> iterQueryResults = queryResults
							.iterator();
			while (iterQueryResults.hasNext()) {
				HashMap<String, String> row = iterQueryResults.next();
				HashMap<String, String> datum = new HashMap<String, String>(2);
				datum.put("text1", row.get(row1));
				datum.put("text2", row.get(row2));
				datum.put("dbId", row.get(rowId));
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
		listViewItems.setAdapter(twoLineWithCheckboxAdapter);
		twoLineWithCheckboxAdapter.changeCheckboxesState(View.GONE);
		listViewItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

				//Search for database Id of the selected Item
				((ListView)view).getItemAtPosition(position).toString();
				
				for (@SuppressWarnings("rawtypes")
				Iterator dataIterator = data.iterator(); dataIterator
								.hasNext();) {
					@SuppressWarnings("unchecked")
					HashMap<String, String> hashMap = (HashMap<String, String>) dataIterator
									.next();
					
					
				}
				if (receivedIntent.hasExtra(QUERY_SOURCE)
								&& receivedIntent.getStringExtra(QUERY_SOURCE)
												.equals(QUESTIONS)) {
					Intent launchAddNewQuestion = new Intent(CRUDObject.this,
									AddNewQuestion.class);
					launchAddNewQuestion
									.putExtra(AddNewQuestion.ADD_NEW_QUESTION_ACTIVITY_MODE,
													AddNewQuestion.ACTIVITY_MODE_UPDATE)
									.putExtra(AddNewQuestion.ACTIVITY_MODE_DB_ITEM_ID,
													dbIdListViewRelationship
																	.get((int) id));
					startActivityForResult(launchAddNewQuestion, 1);
				} else {
					// TODO Implement for other data sources: clients, etc...
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_crudobject, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();

		if (receivedIntent.getBooleanExtra(ADD_MENU_ITEM_ENABLED, false)) {
			menu.add(0, R.id.activity_crudobject_create, 0, R.string.add_item)
							.setEnabled(true);
		}
		if (receivedIntent.getBooleanExtra(SELECT_MENU_ITEM_ENABLED, false))
			menu.add(0, R.id.activity_crudobject_select, 3,
							R.string.select_items).setEnabled(true);
		if (receivedIntent.getBooleanExtra(DELETE_MENU_ITEM_ENABLED, false)) {
			menu.add(0, R.id.activity_crudobject_delete, 3,
							R.string.delete_items).setEnabled(true);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.activity_crudobject_create:
			// Prepare an intent in order to call the add question activity
			Intent intent = new Intent(CRUDObject.this, AddNewQuestion.class);
			intent.putExtra(AddNewQuestion.ADD_NEW_QUESTION_ACTIVITY_MODE,
							AddNewQuestion.ACTIVITY_MODE_ADD_NEW);
			startActivityForResult(intent, 1);
			return true;
		case R.id.activity_crudobject_delete:
			// set button text
			positiveButton.setText(R.string.delete_items);
			// set button action when clicked
			positiveButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Delete the checked items
					ArrayList<HashMap<String, String>> checked = twoLineWithCheckboxAdapter
									.getChecked();

					for (int i = 0; i < checked.size(); i++) {
						String dbId = checked.get(i).get("dbId");
						int result = 0;
						if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(
										QUESTIONS)) {
							result = schemaHelper.deleteQuestion(dbId);
						}

						if(result>0){
							
						}
						
					}
					// Refresh the listview
					// refreshListView();
				}
			});
			// show checkboxes
			twoLineWithCheckboxAdapter.changeCheckboxesState(View.VISIBLE);
			return true;

		case R.id.activity_crudobject_select:
			// set button text
			positiveButton.setText(R.string.select_items);
			// TODO set action to perform when the button is clicked
			// show checkboxes
			twoLineWithCheckboxAdapter.changeCheckboxesState(View.VISIBLE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
