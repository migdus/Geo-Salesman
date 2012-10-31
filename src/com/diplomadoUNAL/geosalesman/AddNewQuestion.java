package com.diplomadoUNAL.geosalesman;

import java.util.HashMap;

import com.diplomadoUNAL.geosalesman.database.SchemaHelper;
import com.diplomadoUNAL.geosalesman.util.EditTextValidation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNewQuestion extends Activity {
	private static final String PREFS_NAME = "AddNewQuestionSharedPrefs";
	private SharedPreferences sharedPreferences;
	// SharedPreferences items for this activity
	private static final String PREF_SELECTED_QUESTION_TYPE = "selectedQuestionType";
	// Range question type
	private static final String QUESTION_TYPE_RANGE = "questionTypeRange";
	private static final String PREF_MINIMUM_RANGE_VALUE = "addNewQuestionSaveMinimumRangeValue";
	private static final String PREF_MAXIMUM_RANGE_VALUE = "addNewQuestionSaveMaximumRangeValue";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_question);

		// Clear sharedPreferences for this activity
		sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
		Editor sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.putString("TEST", "MIGUEL");
		sharedPreferencesEditor.commit();

		Spinner spinnerQuestion = (Spinner) findViewById(R.id.spinner_question_type);
		new SpinnerControl().execute(spinnerQuestion);

		Button buttonOk = (Button) this
						.findViewById(R.id.button_add_new_question_ok);
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SchemaHelper schemaHelper = new SchemaHelper(
								AddNewQuestion.this);

				SparseArray<String> validationTestAlphaWithSpace = new SparseArray<String>();
				validationTestAlphaWithSpace
								.put(EditTextValidation.ALPHABETHIC_VALIDATION,
												getResources().getString(
																R.string.editText_validation_error_alpha_only));

				EditText editTextQuestionTitle = (EditText) AddNewQuestion.this
								.findViewById(R.id.editText_question_title);
				boolean flagEditTextQuestionTitleValidation = EditTextValidation
								.editTextValidation(editTextQuestionTitle,
												validationTestAlphaWithSpace);

				EditText editTextQuestionDescription = (EditText) AddNewQuestion.this
								.findViewById(R.id.editText_question_description);
				boolean flagEditTextQuestionDescriptionValidation = EditTextValidation
								.editTextValidation(
												editTextQuestionDescription,
												validationTestAlphaWithSpace);

				EditText editTextQuestion = (EditText) AddNewQuestion.this
								.findViewById(R.id.editText_question);
				boolean flagEditTextQuestionValidation = EditTextValidation
								.editTextValidation(editTextQuestion,
												validationTestAlphaWithSpace);

				boolean flagQuestionTypeValidation = false;
				String questionType = sharedPreferences.getString(
								PREF_SELECTED_QUESTION_TYPE, "none");
				String answerOptions = new String();
				if (questionType.equals("none")
								|| questionType.equals(getResources()
												.getString(R.string.spinner_question_default_option)
												.toString())) {

					Toast.makeText(AddNewQuestion.this,
									getResources().getString(
													R.string.spinner_question_error_no_option_chosen)
													.toString(),
									Toast.LENGTH_LONG).show();

					flagQuestionTypeValidation = false;
				} else if (questionType.equals(QUESTION_TYPE_RANGE)) {
					answerOptions = Integer.toString(sharedPreferences.getInt(PREF_MINIMUM_RANGE_VALUE, -1))
									+ "-"
									+ Integer.toString(sharedPreferences.getInt(PREF_MINIMUM_RANGE_VALUE, -1));
					flagQuestionTypeValidation = true;
				} else {
					// TODO Implement other question type
					Toast.makeText(AddNewQuestion.this, "Not implemented yet",
									Toast.LENGTH_LONG).show();
					flagQuestionTypeValidation = false;
				}

				if (flagEditTextQuestionTitleValidation
								&& flagEditTextQuestionDescriptionValidation
								&& flagEditTextQuestionValidation
								&& flagQuestionTypeValidation) {
					// Get data from fields
					String questionTitle = (editTextQuestionTitle).getText().toString();
					String question = (editTextQuestion).getText().toString();
					String questionDescription = (editTextQuestionDescription)
									.getText().toString();
					// Save to database
					long dbResult = schemaHelper.addQuestion(questionTitle,
									question, questionDescription,
									questionType, answerOptions);
					if (dbResult < 0) {
						Toast.makeText(AddNewQuestion.this,
										"Error storing at database",
										Toast.LENGTH_LONG).show();
					} else {

					}
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_new_question, menu);
		return true;
	}

	private class SpinnerControl extends AsyncTask<Spinner, Integer, Long> {

		@Override
		protected Long doInBackground(Spinner... params) {
			Spinner spinnerQuestion = params[0];
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
							.createFromResource(
											AddNewQuestion.this,
											R.array.question_type_options,
											android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerQuestion.setAdapter(adapter);
			spinnerQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
								int pos, long id) {
					long selectedItem = parent.getItemIdAtPosition(pos);

					if (selectedItem == 0) {
						// Do nothing
					} else if (selectedItem == 1) {
						// Yes/No Question
						Toast.makeText(AddNewQuestion.this,
										"Not implemented yet",
										Toast.LENGTH_LONG).show();
					} else if (selectedItem == 2) {
						// Multiple choice answer
						Toast.makeText(AddNewQuestion.this,
										"Not implemented yet",
										Toast.LENGTH_LONG).show();
					} else if (selectedItem == 3) {
						// Open question
						Toast.makeText(AddNewQuestion.this,
										"Not implemented yet",
										Toast.LENGTH_LONG).show();
					} else if (selectedItem == 4) {
						// Range Question
						// The range dialog
						AlertDialog.Builder rangeDialogBuilder = new AlertDialog.Builder(
										AddNewQuestion.this);
						rangeDialogBuilder.setTitle(getResources().getString(
										R.string.range_selection_dialog_title));
						rangeDialogBuilder
										.setMessage(getResources()
														.getString(R.string.range_selection_dialog_message));
						LayoutInflater alertDialogLayoutInflater = getLayoutInflater();
						View rangeSelectionDialogLayout = alertDialogLayoutInflater
										.inflate(R.layout.range_selection_dialog,
														null);
						rangeDialogBuilder.setView(rangeSelectionDialogLayout);
						final EditText minimumValue = (EditText) rangeSelectionDialogLayout
										.findViewById(R.id.editText_minimum_value);
						final EditText maximumValue = (EditText) rangeSelectionDialogLayout
										.findViewById(R.id.editText_maximum_value);

						rangeDialogBuilder.setPositiveButton(getResources()
										.getString(R.string.OK),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
															DialogInterface dialog,
															int which) {
												Editor sharedPreferencesEditor = sharedPreferences
																.edit();
												sharedPreferencesEditor
																.putInt(PREF_MINIMUM_RANGE_VALUE,
																				Integer.parseInt(minimumValue
																								.getText()
																								.toString()));
												sharedPreferencesEditor
																.putInt(PREF_MAXIMUM_RANGE_VALUE,
																				Integer.parseInt(maximumValue
																								.getText()
																								.toString()));

												sharedPreferencesEditor
																.putString(PREF_SELECTED_QUESTION_TYPE,
																				QUESTION_TYPE_RANGE);
												sharedPreferencesEditor
																.commit();
											}
										});
						rangeDialogBuilder.setNegativeButton(R.string.cancel,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
															DialogInterface dialog,
															int which) {
												if (which == Dialog.BUTTON_NEGATIVE) {
													dialog.dismiss();
												}

											}
										});
						rangeDialogBuilder.create();
						rangeDialogBuilder.show();
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
			return null;
		}

	}
}
