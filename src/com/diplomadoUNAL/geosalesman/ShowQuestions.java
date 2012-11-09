package com.diplomadoUNAL.geosalesman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TwoLineListItem;

import com.diplomadoUNAL.geosalesman.database.QuestionTable;
import com.diplomadoUNAL.geosalesman.database.SchemaHelper;

public class ShowQuestions extends Activity {

	@SuppressLint("UseSparseArrays")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);

		ListView listViewShowQuestions = (ListView) this
						.findViewById(R.id.activity_show_questions_listView_show_question_menu);

		SchemaHelper schemaHelper = new SchemaHelper(this);
		ArrayList<HashMap<String, String>> questionTitlesAndDescriptions = schemaHelper
						.getQuestionsTitlesAndDescriptions();

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		final HashMap<Integer, String> dbIdListViewRelationship = new HashMap<Integer, String>();
		int listViewIndex = 0;
		if (questionTitlesAndDescriptions.size() > 0) {
			Iterator<HashMap<String, String>> iterQTAD = questionTitlesAndDescriptions
							.iterator();
			while (iterQTAD.hasNext()) {
				HashMap<String, String> row = iterQTAD.next();
				Map<String, String> datum = new HashMap<String, String>(2);
				datum.put("Title", row.get(QuestionTable.QUESTION_TITLE));
				datum.put("Description",
								row.get(QuestionTable.QUESTION_DESCRIPTION));
				dbIdListViewRelationship.put(listViewIndex,
								row.get(QuestionTable.ID));
				data.add(datum);
				listViewIndex++;
			}
		} else {
			Map<String, String> datum = new HashMap<String, String>(2);
			datum.put("Title",
							getResources().getString(
											R.string.no_questions_found));
			datum.put("Description",
							getResources().getString(
											R.string.tap_question_menu_to_add));
			data.add(datum);
			this.openOptionsMenu();
		}

		SimpleAdapter adapter = new SimpleAdapter(this, data,
						android.R.layout.simple_list_item_2, new String[] {
								"Title", "Description" }, new int[] {
								android.R.id.text1, android.R.id.text2 });
		listViewShowQuestions.setAdapter(adapter);

		listViewShowQuestions.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
				String selectedOptionFirstLine = (String) ((TwoLineListItem) view)
								.getText1().getText();
				String selectedOptionSecondLine = (String) ((TwoLineListItem) view)
								.getText1().getText();

				Intent launchAddNewQuestion = new Intent(ShowQuestions.this,
								AddNewQuestion.class);
				launchAddNewQuestion.putExtra(
								AddNewQuestion.ADD_NEW_QUESTION_ACTIVITY_MODE,
								AddNewQuestion.ACTIVITY_MODE_UPDATE).putExtra(
								AddNewQuestion.ACTIVITY_MODE_DB_ITEM_ID,
								dbIdListViewRelationship.get((int) id));
				startActivity(launchAddNewQuestion);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_show_questions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.activity_show_questions_add_new_question:
			Intent launchAddNewQuestion = new Intent(this, AddNewQuestion.class);
			launchAddNewQuestion.putExtra(
							AddNewQuestion.ADD_NEW_QUESTION_ACTIVITY_MODE,
							AddNewQuestion.ACTIVITY_MODE_ADD_NEW);
			startActivity(launchAddNewQuestion);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
