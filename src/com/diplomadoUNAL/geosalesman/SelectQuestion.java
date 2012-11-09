package com.diplomadoUNAL.geosalesman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.diplomadoUNAL.geosalesman.database.QuestionTable;
import com.diplomadoUNAL.geosalesman.database.SchemaHelper;

@SuppressLint("UseSparseArrays")
public class SelectQuestion extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		
		
		
		ListView listViewShowQuestions = (ListView) this
						.findViewById(R.id.listView_show_question_menu);

		SchemaHelper schemaHelper = new SchemaHelper(this);
		ArrayList<HashMap<String, String>> questionTitlesAndDescriptions = schemaHelper
						.getQuestionsTitlesAndDescriptions();

		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

		final HashMap<Integer, String> dbIdListViewRelationship = new HashMap<Integer, String>();
		int listViewIndex = 0;
		if (questionTitlesAndDescriptions.size() > 0) {
			Iterator<HashMap<String, String>> iterQTAD = questionTitlesAndDescriptions
							.iterator();
			while (iterQTAD.hasNext()) {
				HashMap<String, String> row = iterQTAD.next();
				HashMap<String, String> datum = new HashMap<String, String>(2);
				datum.put("text1", row.get(QuestionTable.QUESTION_TITLE));
				datum.put("text2",
								row.get(QuestionTable.QUESTION_DESCRIPTION));
				dbIdListViewRelationship.put(listViewIndex,
								row.get(QuestionTable.ID));
				data.add(datum);
				listViewIndex++;
			}
		} else {
			HashMap<String, String> datum = new HashMap<String, String>(2);
			datum.put("Title",
							getResources().getString(
											R.string.no_questions_found));
			datum.put("Description",
							getResources().getString(
											R.string.tap_question_menu_to_add));
			data.add(datum);
			this.openOptionsMenu();
		}

		LinearLayout relativeLayoutFloatingButtonsBar = (LinearLayout) this
						.findViewById(R.id.listview_floating_buttons_bar);
		relativeLayoutFloatingButtonsBar.setVisibility(View.VISIBLE);
		
		//TODO Make buttons do something
		Button buttonOk=(Button)this.findViewById(R.id.listview_button_ok);
		buttonOk.setEnabled(false);
		
		Button buttonCancel=(Button)this.findViewById(R.id.listview_button_cancel);
		
		TwoLineWithCheckboxAdapter adapter = new TwoLineWithCheckboxAdapter(this, R.layout.simple_list_item_2_with_checkbox, data,buttonOk);
		listViewShowQuestions.setAdapter(adapter);
	}
}
