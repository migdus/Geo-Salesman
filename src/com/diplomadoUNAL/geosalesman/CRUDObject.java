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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diplomadoUNAL.geosalesman.database.ClientTable;
import com.diplomadoUNAL.geosalesman.database.QuestionTable;
import com.diplomadoUNAL.geosalesman.database.ReportTemplateTable;
import com.diplomadoUNAL.geosalesman.database.SchemaHelper;

public class CRUDObject extends Activity {
	Button selectButton;
	Button deleteButton;
	Button cancelButton;
	TwoLineWithCheckboxAdapter twoLineWithCheckboxAdapter;

	public final static String QUERY_SOURCE = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_QUERY_TYPE";
	public final static String QUESTIONS = "QUESTIONS"; // get question titles and questions
	public final static String CLIENTS = "CLIENTS";
	public final static String REPORT_TEMPLATES = "REPORT_TEMPLATES";

	// Menus and buttons enabled
	public final static String ADD_MENU_ITEM_ENABLED = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_ADD_MENU_ITEM_ENABLED";
	public final static String DELETE_BUTTON_ENABLED = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_DELETE_BUTTON_ENABLED";
	public final static String SELECT_BUTTON_ENABLED = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_SELECT_BUTTON_ENABLED";
	public final static String CANCEL_BUTTON_ENABLED = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_CANCEL_BUTTON_ENABLED";
	public final static String REPORT_TEMPLATE_QUESTIONS = "com.diplomadoUNAL.geosalesman.REPORT_TEMPLATE_QUESTIONS";

	public final static String ACTIVITY_MODE_DB_ITEM_ID = "com.diplomadoUNAL.geosalesman.itemId";

	// Set the items checked when shown
	public final static String SET_DISPLAYED_ITEMS_CHECKED = "com.diplomadoUNAL.geosalesman.SET_DISPLAYED_ITEMS_CHECKED";

	public final static String ACTIVITY_TITLE = "com.diplomadoUNAL.geosalesman.CRUD_OBJ_ACTIVITY_TITLE";

	Intent receivedIntent;
	SchemaHelper schemaHelper;
	ListView listViewItems;
	LinearLayout floatingBar;

	ArrayList<HashMap<String, String>> data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crudobject);

		receivedIntent = getIntent();

		floatingBar = ((LinearLayout) this
						.findViewById(R.id.floating_buttons_bar));

		selectButton = ((Button) this.findViewById(R.id.select_button));
		selectButton.setText(R.string.select);
		if (receivedIntent.getBooleanExtra(SELECT_BUTTON_ENABLED, true))
			selectButton.setVisibility(View.VISIBLE);
		selectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent();
				sendIntent.putExtra("checkedItems",
								twoLineWithCheckboxAdapter.getChecked());
				setResult(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, sendIntent);
				finish();
			}
		});
		deleteButton = ((Button) this.findViewById(R.id.delete_button));
		deleteButton.setText(R.string.delete);
		if (receivedIntent.getBooleanExtra(DELETE_BUTTON_ENABLED, true))
			deleteButton.setVisibility(View.VISIBLE);
		// Set actions for the delete button
		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Delete the checked items
				ArrayList<HashMap<String, String>> checked = twoLineWithCheckboxAdapter
								.getChecked();

				for (int i = 0; i < checked.size(); i++) {
					String dbId = checked.get(i).get("dbId");
					int result = 0;
					// Select table to delete from
					// Question table
					if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(
									QUESTIONS)) {
						result = schemaHelper.deleteQuestion(dbId);

					} else
					// Client table
					if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(
									CLIENTS)) {
						result = schemaHelper.deleteClient(dbId);
					}
					//Report Template table
					if(receivedIntent.getStringExtra(QUERY_SOURCE).equals(REPORT_TEMPLATES)){
						result=schemaHelper.deleteReportTemplate(dbId);
					}

					if (result > 0) {
						Toast.makeText(CRUDObject.this,
										R.string.database_success_deleting_data,
										Toast.LENGTH_LONG).show();
					}

				}
				// Refresh the listview
				floatingBar.setVisibility(View.GONE);
				refreshListView();
			}
		});

		cancelButton = ((Button) this.findViewById(R.id.cancel_button));
		cancelButton.setText(R.string.cancel);
		if (receivedIntent.getBooleanExtra(CANCEL_BUTTON_ENABLED, true))
			cancelButton.setVisibility(View.VISIBLE);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		listViewItems = (ListView) this.findViewById(R.id.listView_show_items);
		listViewItems.setItemsCanFocus(false);
		listViewItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

				String text1 = ((TextView) view
								.findViewById(R.id.simple_list_item_2_with_checkbox_text1))
								.getText().toString();
				String text2 = ((TextView) view
								.findViewById(R.id.simple_list_item_2_with_checkbox_text2))
								.getText().toString();

				if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(
								QUESTIONS)) {
					Intent launchAddNewQuestion = new Intent(CRUDObject.this,
									AddNewQuestion.class);
					launchAddNewQuestion
									.putExtra(AddNewQuestion.ADD_NEW_QUESTION_ACTIVITY_MODE,
													AddNewQuestion.ACTIVITY_MODE_UPDATE)
									.putExtra(ACTIVITY_TITLE,
													R.string.activity_add_new_question_update_title)
									.putExtra(AddNewQuestion.ACTIVITY_MODE_DB_ITEM_ID,
													twoLineWithCheckboxAdapter
																	.getDbId(text1,
																					text2));
					startActivityForResult(launchAddNewQuestion, 1);
				} else if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(
								CLIENTS)) {
					Intent launchAddNewClient = new Intent(CRUDObject.this,
									AddNewClient.class);
					launchAddNewClient
									.putExtra(AddNewClient.ADD_NEW_CLIENT_ACTIVITY_MODE,
													AddNewClient.ACTIVITY_MODE_UPDATE)
									.putExtra(ACTIVITY_TITLE,
													R.string.activity_add_new_client_update_title)
									.putExtra(AddNewClient.ACTIVITY_MODE_DB_ITEM_ID,
													twoLineWithCheckboxAdapter
																	.getDbId(text1,
																					text2));
					startActivityForResult(launchAddNewClient, 1);
				} else if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(
								REPORT_TEMPLATES)) {
					Intent launchAddNewReportTemplate = new Intent(
									CRUDObject.this, AddNewReportTemplate.class);
					launchAddNewReportTemplate
									.putExtra(AddNewReportTemplate.ADD_NEW_REPORT_TEMPLATE_ACTIVITY_MODE,
													AddNewReportTemplate.ACTIVITY_MODE_UPDATE)
									.putExtra(ACTIVITY_TITLE,
													R.string.activity_add_new_report_template_update_title)
									.putExtra(AddNewReportTemplate.ACTIVITY_MODE_DB_ITEM_ID,
													twoLineWithCheckboxAdapter
																	.getDbId(text1,
																					text2));
					startActivityForResult(launchAddNewReportTemplate, 1);

				} else {

					Toast.makeText(CRUDObject.this, "Some error with this",
									Toast.LENGTH_LONG).show();
				}

			}
		});
		schemaHelper = new SchemaHelper(this);

		receivedIntent = getIntent();
	};

	@Override
	protected void onStart() {
		super.onStart();

		setTitle(receivedIntent.getStringExtra(ACTIVITY_TITLE));
		floatingBar.setVisibility(View.GONE);
		refreshListView();

	}

	private void refreshListView() {
		RelativeLayout noItemsFound = (RelativeLayout) this
						.findViewById(R.id.no_items_found_message);
		noItemsFound.setVisibility(View.GONE);

		listViewItems.setVisibility(View.VISIBLE);

		ArrayList<HashMap<String, String>> queryResults = null;
		String row1 = null;
		String row2 = null;
		String rowId = null;

		// Refresh view for Question table data source
		if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(QUESTIONS)
						|| receivedIntent.getStringExtra(QUERY_SOURCE).equals(
										REPORT_TEMPLATE_QUESTIONS)) {
			queryResults = schemaHelper.getQuestionsTitlesAndDescriptions();
			row1 = QuestionTable.QUESTION_TITLE;
			row2 = QuestionTable.QUESTION_DESCRIPTION;
			rowId = QuestionTable.ID;
		}
		// Refresh view for Client table data source
		else if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(CLIENTS)) {
			queryResults = schemaHelper.getClientNameAndContactName();
			row1 = ClientTable.NAME;
			row2 = ClientTable.CONTACT_NAME;
			rowId = ClientTable.ID;
		}

		// Refresh view for Client table data source
		else if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(
						REPORT_TEMPLATES)) {
			queryResults = schemaHelper.getReportTemplateNameAndDescription();
			row1 = ReportTemplateTable.NAME;
			row2 = ReportTemplateTable.DESCRIPTION;
			rowId = ReportTemplateTable.ID;
		} else {
			// TODO Implement for other data sources: clients, etc...
		}

		data = new ArrayList<HashMap<String, String>>();

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
			}
		} else {
			// No items found
			listViewItems.setVisibility(View.GONE);

			noItemsFound.setVisibility(View.VISIBLE);

			String text1ErrorMessage = null;
			String text2ErrorMessage = null;

			if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(QUESTIONS)) {
				text1ErrorMessage = getResources().getString(
								R.string.no_questions_found);
				text2ErrorMessage = getResources().getString(
								R.string.tap_question_menu_to_add);
			} else if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(
							CLIENTS)) {
				text1ErrorMessage = getResources().getString(
								R.string.no_clients_found);
				text2ErrorMessage = getResources().getString(
								R.string.tap_client_menu_to_add);
			} else if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(
							REPORT_TEMPLATES)) {
				text1ErrorMessage = getResources().getString(
								R.string.no_report_templates_found);
				text2ErrorMessage = getResources().getString(
								R.string.tap_report_templates_menu_to_add);
			}

			TextView largeTextView = (TextView) this
							.findViewById(R.id.textView_large_size);
			TextView mediumTextView = (TextView) this
							.findViewById(R.id.textView_medium_size);
			largeTextView.setText(text1ErrorMessage);
			mediumTextView.setText(text2ErrorMessage);

		}

		// twoLineWithCheckboxAdapter.changeCheckboxesVisibility(View.GONE);
		// Check the questions that are selected for this particular report template
		ArrayList<Integer> dbIds = null;
		if (receivedIntent.hasExtra(ACTIVITY_MODE_DB_ITEM_ID)
						&& receivedIntent.getStringExtra(QUERY_SOURCE).equals(
										REPORT_TEMPLATE_QUESTIONS)) {

			ArrayList<String> result = schemaHelper
							.getReportTemplateQuestionByReportTemplateID(Integer.parseInt(receivedIntent
											.getStringExtra(ACTIVITY_MODE_DB_ITEM_ID)));
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for (int i = 0; i < result.size(); i++) {
				String qId = result.get(i);
				temp.add(Integer.parseInt(qId));
			}
			dbIds = temp;
			// twoLineWithCheckboxAdapter.notifyDataSetChanged();
			// receivedIntent.putExtra(QUERY_SOURCE, QUESTIONS);

		}

		twoLineWithCheckboxAdapter = new TwoLineWithCheckboxAdapter(this,
						R.layout.simple_list_item_2_with_checkbox, data,
						floatingBar, dbIds);

		listViewItems.setAdapter(twoLineWithCheckboxAdapter);
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.activity_crudobject_create:
			// Prepare an intent in order to call the add question activity
			Intent intent = null;
			if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(QUESTIONS)) {
				intent = new Intent(CRUDObject.this, AddNewQuestion.class);
				intent.putExtra(AddNewQuestion.ADD_NEW_QUESTION_ACTIVITY_MODE,
								AddNewQuestion.ACTIVITY_MODE_ADD_NEW)
								.putExtra(ACTIVITY_TITLE,
												R.string.activity_add_new_question_add_new_title_activity);
			} else if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(
							CLIENTS)) {
				intent = new Intent(CRUDObject.this, AddNewClient.class);
				intent.putExtra(AddNewClient.ADD_NEW_CLIENT_ACTIVITY_MODE,
								AddNewClient.ACTIVITY_MODE_ADD_NEW)
								.putExtra(ACTIVITY_TITLE,
												R.string.activity_add_new_client_add_new_title_activity);

			} else if (receivedIntent.getStringExtra(QUERY_SOURCE).equals(
							REPORT_TEMPLATES)) {
				intent = new Intent(CRUDObject.this, AddNewReportTemplate.class);
				intent.putExtra(AddNewReportTemplate.ADD_NEW_REPORT_TEMPLATE_ACTIVITY_MODE,
								AddNewReportTemplate.ACTIVITY_MODE_ADD_NEW)
								.putExtra(ACTIVITY_TITLE,
												R.string.activity_add_new_client_add_new_title_activity);

			}
			startActivityForResult(intent, 1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
