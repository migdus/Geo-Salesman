package com.diplomadoUNAL.geosalesman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TwoLineListItem;
import android.widget.AdapterView.OnItemClickListener;

import com.diplomadoUNAL.geosalesman.database.ClientTable;
import com.diplomadoUNAL.geosalesman.database.QuestionTable;
import com.diplomadoUNAL.geosalesman.database.SchemaHelper;

public class ShowQuestions extends Activity implements OnItemClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_questions);

		ListView listViewShowQuestions = (ListView) this
				.findViewById(R.id.listView_show_question_menu);

		SchemaHelper schemaHelper = new SchemaHelper(this);
		ArrayList<HashMap<String, String>> questionTitlesAndDescriptions = schemaHelper
				.getQuestionsTitlesAndDescriptions();

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		if (questionTitlesAndDescriptions.size() > 0) {
			Iterator<HashMap<String, String>> iterQTAD = questionTitlesAndDescriptions
					.iterator();
			while (iterQTAD.hasNext()) {
				HashMap<String, String> row = iterQTAD.next();
				Map<String, String> datum = new HashMap<String, String>(2);
				datum.put("Title", row.get(QuestionTable.QUESTION_TITLE));
				datum.put("Description",
						row.get(QuestionTable.QUESTION_DESCRIPTION));
				data.add(datum);
			}
		}
		else{
			Map<String, String> datum = new HashMap<String, String>(2);
			datum.put("Title", getResources().getString(R.string.no_questions_found));
			datum.put("Description",getResources().getString(R.string.tap_question_menu_to_add));
			data.add(datum);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, data,
				android.R.layout.simple_list_item_2, new String[] { "Title",
						"Description" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		listViewShowQuestions.setAdapter(adapter);

		listViewShowQuestions.setOnItemClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_show_questions, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String selectedOption = (String) ((TwoLineListItem) arg1).getText1()
				.getText();

	}

}
