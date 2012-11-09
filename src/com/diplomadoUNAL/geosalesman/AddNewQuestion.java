package com.diplomadoUNAL.geosalesman;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.diplomadoUNAL.geosalesman.database.QuestionTable;
import com.diplomadoUNAL.geosalesman.database.SchemaHelper;
import com.diplomadoUNAL.geosalesman.util.EditTextValidation;

public class AddNewQuestion extends Activity {
	public static final String PREFS_NAME = "AddNewQuestionSharedPrefs";
	private SharedPreferences sharedPreferences;
	// SharedPreferences items for this activity
	private static final String PREF_SELECTED_QUESTION_TYPE = "selectedQuestionType";
	// Range question type
	private static final String QUESTION_TYPE_NONE_SELECTED = "questionTypeNoneSelected";
	private static final String QUESTION_TYPE_RANGE = "questionTypeRange";
	private static final String QUESTION_TYPE_YES_NO = "questionTypeYesNo";
	private static final String QUESTION_TYPE_OPEN = "questionTypeOpen";
	private static final String PREF_MINIMUM_RANGE_VALUE = "addNewQuestionSaveMinimumRangeValue";
	private static final String PREF_MAXIMUM_RANGE_VALUE = "addNewQuestionSaveMaximumRangeValue";
	// validation flags
	private boolean flagEditTextQuestionTitleValidation = false;
	private boolean flagEditTextQuestionDescriptionValidation = false;
	private boolean flagEditTextQuestionValidation = false;
	private boolean flagEditTextMinimumValidation = false;
	private boolean flagEditTextMaximumValidation = false;
	private boolean flagQuestionTypeValidation = false;

	private EditText editTextMinimumValue;
	private EditText editTextMaximumValue;
	private EditText editTextQuestionTitle;
	private EditText editTextQuestionDescription;
	private EditText editTextQuestion;

	private String editTextMinimumValueTemp;
	private String editTextMaximumValueTemp;

	private HashMap<Integer, String> validationTestNumbers = null;
	private HashMap<Integer, String> validationTestAlphaWithSpace = null;

	// Intent messages
	// Mode of this activity
	public final static String ADD_NEW_QUESTION_ACTIVITY_MODE = "com.diplomadoUNAL.geosalesman.ADD_NEW_QUESTION_ACTIVITY_MODE";
	public final static String ACTIVITY_MODE_READ_ONLY = "readOnly";
	public final static String ACTIVITY_MODE_ADD_NEW = "addNew";
	public final static String ACTIVITY_MODE_UPDATE = "update";
	public final static String ACTIVITY_MODE_DB_ITEM_ID = "itemId";

	//Checks if the spinner selected item was changed for the code or the user
	boolean firstSelected = true;
	
	private void resetValidationFlags() {
		flagEditTextQuestionValidation = Boolean.valueOf(false);
		flagEditTextMinimumValidation = Boolean.valueOf(false);
		flagEditTextMaximumValidation = Boolean.valueOf(false);
		flagQuestionTypeValidation = Boolean.valueOf(false);
	}

	@SuppressLint("UseSparseArrays")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_question);

		// Clear sharedPreferences for this activity
		sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
		Editor sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();

		// EditText definition and validation
		validationTestAlphaWithSpace = new HashMap<Integer, String>();
		validationTestAlphaWithSpace
						.put(EditTextValidation.ALPHABETHIC_VALIDATION,
										getResources().getString(
														R.string.editText_validation_error_alpha_only));
		validationTestAlphaWithSpace
						.put(EditTextValidation.CHARACTER_VALIDATION,
										getResources().getString(
														R.string.editText_validation_error_char_not_allowed));
		validationTestAlphaWithSpace
						.put(EditTextValidation.NO_PIPE_CHAR_VALIDATION,
										getResources().getString(
														R.string.editText_validation_error_pipe_char_not_allowed));
		// EditText Validation
		editTextQuestionTitle = (EditText) AddNewQuestion.this
						.findViewById(R.id.activity_add_new_question_editText_question_title);

		editTextQuestionTitle
						.setOnFocusChangeListener(new OnFocusChangeListener() {
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if (!hasFocus) {
									validateEditText((EditText) v,
													validationTestAlphaWithSpace);
								}
							}
						});

		editTextQuestionDescription = (EditText) AddNewQuestion.this
						.findViewById(R.id.activity_add_new_question_editText_question_description);
		editTextQuestionDescription
						.setOnFocusChangeListener(new OnFocusChangeListener() {
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if (!hasFocus) {
									validateEditText((EditText) v,
													validationTestAlphaWithSpace);
								}
							}
						});

		editTextQuestion = (EditText) AddNewQuestion.this
						.findViewById(R.id.activity_add_new_question_editText_question);
		editTextQuestion.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validateEditText((EditText) v, validationTestAlphaWithSpace);
				}
			}
		});
	}

	@SuppressLint("UseSparseArrays")
	@Override
	protected void onStart() {
		super.onStart();

		final Intent intent = getIntent();

		int dbId = -1;
		// Fill the form fields according with database information
		if (intent.hasExtra(ADD_NEW_QUESTION_ACTIVITY_MODE)
						&& (intent.getStringExtra(
										ADD_NEW_QUESTION_ACTIVITY_MODE).equals(
										ACTIVITY_MODE_READ_ONLY) || intent
										.getStringExtra(ADD_NEW_QUESTION_ACTIVITY_MODE)
										.equals(ACTIVITY_MODE_UPDATE))) {

			dbId = Integer.parseInt(intent
							.getStringExtra(ACTIVITY_MODE_DB_ITEM_ID));
			SchemaHelper schemaHelper = new SchemaHelper(
							getApplicationContext());
			HashMap<String, String> storedValues = schemaHelper
							.getQuestion(dbId);
			editTextQuestion.setText(storedValues.get(QuestionTable.QUESTION));
			editTextQuestionDescription.setText(storedValues
							.get(QuestionTable.QUESTION_DESCRIPTION));
			editTextQuestionTitle.setText(storedValues
							.get(QuestionTable.QUESTION_TITLE));

			Editor edit = sharedPreferences.edit();
			String questionType = storedValues.get(QuestionTable.QUESTION_TYPE);
			if (questionType.equals(AddNewQuestion.QUESTION_TYPE_NONE_SELECTED)) {
				edit.putString(PREF_SELECTED_QUESTION_TYPE,
								QUESTION_TYPE_NONE_SELECTED);
			} else if (questionType.equals(AddNewQuestion.QUESTION_TYPE_OPEN)) {
				edit.putString(PREF_SELECTED_QUESTION_TYPE, QUESTION_TYPE_OPEN);
			} else if (questionType.equals(AddNewQuestion.QUESTION_TYPE_YES_NO)) {
				edit.putString(PREF_SELECTED_QUESTION_TYPE,
								QUESTION_TYPE_YES_NO);
			} else if (questionType.equals(AddNewQuestion.QUESTION_TYPE_RANGE)) {
				String[] splitted = storedValues.get(
								QuestionTable.ANSWER_OPTIONS).split("-");
				editTextMinimumValueTemp = splitted[0];
				editTextMaximumValueTemp = splitted[1];
				edit.putString(PREF_SELECTED_QUESTION_TYPE, QUESTION_TYPE_RANGE);
			}
			edit.commit();

		}
		final Spinner spinnerQuestion = (Spinner) findViewById(R.id.activity_add_new_question_spinner_question_type);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
						AddNewQuestion.this, R.array.question_type_options,
						android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		String selectedQuestionType = sharedPreferences.getString(
						PREF_SELECTED_QUESTION_TYPE, "none");
		String selectedQuestionOption = new String();

		if (selectedQuestionType.equals("none")
						|| selectedQuestionType
										.equals(QUESTION_TYPE_NONE_SELECTED)) {
			selectedQuestionOption = getResources()
							.getString(R.string.activity_add_new_question_spinner_option_default_option);
		} else if (selectedQuestionType.equals(QUESTION_TYPE_RANGE)) {
			selectedQuestionOption = getResources()
							.getString(R.string.activity_add_new_question_spinner_option_scale);
		} else if (selectedQuestionType.equals(QUESTION_TYPE_OPEN)) {
			selectedQuestionOption = getResources()
							.getString(R.string.activity_add_new_question_spinner_option_open);
		} else if (selectedQuestionType.equals(QUESTION_TYPE_YES_NO)) {
			selectedQuestionOption = getResources()
							.getString(R.string.activity_add_new_question_spinner_option_yes_no);
		}
		// Get position of the selected option

		int tempPosition = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			String item = adapter.getItem(i).toString();
			if (item.equals(selectedQuestionOption)) {
				tempPosition = i;
				break;
			}
		}

		final int defaultSpinnerOptionPosition = tempPosition;

		spinnerQuestion.setAdapter(adapter);
		spinnerQuestion.setSelection(defaultSpinnerOptionPosition);

		
		spinnerQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
							int pos, long id) {
				if (firstSelected) {
					firstSelected = false;
				}else{
					String selectedItem = (String) parent.getItemAtPosition(pos);
					Editor sharedPreferencesEditor = sharedPreferences.edit();
					// Do nothing, but record that the Question Type is not selected yet
					if (selectedItem.equals(AddNewQuestion.this
									.getResources()
									.getString(R.string.activity_add_new_question_spinner_option_default_option))) {
						sharedPreferencesEditor.putString(
										PREF_SELECTED_QUESTION_TYPE,
										QUESTION_TYPE_NONE_SELECTED);
						sharedPreferencesEditor.commit();
						flagQuestionTypeValidation = false;

					}
					// Yes/No Question
					else if (selectedItem
									.equals(AddNewQuestion.this
													.getResources()
													.getString(R.string.activity_add_new_question_spinner_option_yes_no))) {
						sharedPreferencesEditor.putString(
										PREF_SELECTED_QUESTION_TYPE,
										QUESTION_TYPE_YES_NO);
						sharedPreferencesEditor.commit();
						flagQuestionTypeValidation = true;
					} else if (selectedItem
									.equals(AddNewQuestion.this
													.getResources()
													.getString(R.string.activity_add_new_question_spinner_multiple_choice_answer))) {
						// Multiple choice answer question
						Toast.makeText(AddNewQuestion.this, "Not implemented yet",
										Toast.LENGTH_LONG).show();
						// Open question
					} else if (selectedItem
									.equals(AddNewQuestion.this
													.getResources()
													.getString(R.string.activity_add_new_question_spinner_option_open))) {
						sharedPreferencesEditor.putString(
										PREF_SELECTED_QUESTION_TYPE,
										QUESTION_TYPE_OPEN);
						sharedPreferencesEditor.commit();
						flagQuestionTypeValidation = true;
						// Scale question
					} else if (selectedItem
									.equals(AddNewQuestion.this
													.getResources()
													.getString(R.string.activity_add_new_question_spinner_option_scale))) {
						rangeDiag();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

			private void rangeDiag() {
				// Scale Question
				// The scale dialog
				final Dialog rangeDialog = new Dialog(AddNewQuestion.this);
				rangeDialog.setContentView(R.layout.range_selection_dialog);

				rangeDialog.setTitle(getResources()
								.getString(R.string.activity_add_new_question_scale_selection_dialog_title));

				Button okButton = (Button) rangeDialog
								.findViewById(R.id.activity_add_new_question_range_selection_button_ok);

				Button cancelButton = (Button) rangeDialog
								.findViewById(R.id.activity_add_new_question_range_selection_button_cancel);

				// EditText definition and validation
				validationTestNumbers = new HashMap<Integer, String>();
				validationTestNumbers
								.put(EditTextValidation.NUMBER_VALIDATION,
												getResources().getString(
																R.string.editText_validation_error_numbers_only));
				validationTestNumbers
								.put(EditTextValidation.CHARACTER_VALIDATION,
												getResources().getString(
																R.string.editText_validation_error_char_not_allowed));
				validationTestNumbers
								.put(EditTextValidation.NO_PIPE_CHAR_VALIDATION,
												getResources().getString(
																R.string.editText_validation_error_pipe_char_not_allowed));

				// Edit text objects
				editTextMinimumValue = (EditText) rangeDialog
								.findViewById(R.id.activity_add_new_question_editText_minimum_value);

				editTextMinimumValue
								.setOnFocusChangeListener(new OnFocusChangeListener() {
									@Override
									public void onFocusChange(View v,
													boolean hasFocus) {
										if (!hasFocus) {
											validateEditText((EditText) v,
															validationTestNumbers);
										}
									}
								});

				editTextMaximumValue = (EditText) rangeDialog
								.findViewById(R.id.activity_add_new_question_editText_maximum_value);
				editTextMaximumValue
								.setOnFocusChangeListener(new OnFocusChangeListener() {
									@Override
									public void onFocusChange(View v,
													boolean hasFocus) {
										if (!hasFocus) {
											validateEditText((EditText) v,
															validationTestNumbers);
										}
									}
								});

				if (intent.hasExtra(ADD_NEW_QUESTION_ACTIVITY_MODE)
								&& (intent.getStringExtra(
												ADD_NEW_QUESTION_ACTIVITY_MODE)
												.equals(ACTIVITY_MODE_READ_ONLY) || intent
												.getStringExtra(ADD_NEW_QUESTION_ACTIVITY_MODE)
												.equals(ACTIVITY_MODE_UPDATE))) {
					editTextMinimumValue.setText(editTextMinimumValueTemp);
					editTextMaximumValue.setText(editTextMaximumValueTemp);
				}
				okButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						validateEditText(editTextMinimumValue,
										validationTestNumbers);
						validateEditText(editTextMaximumValue,
										validationTestNumbers);
						if (flagEditTextMinimumValidation
										&& flagEditTextMaximumValidation) {

							Editor sharedPreferencesEditor = sharedPreferences
											.edit();
							sharedPreferencesEditor.putInt(
											PREF_MINIMUM_RANGE_VALUE,
											Integer.parseInt(editTextMinimumValue
															.getText()
															.toString()));
							sharedPreferencesEditor.putInt(
											PREF_MAXIMUM_RANGE_VALUE,
											Integer.parseInt(editTextMaximumValue
															.getText()
															.toString()));

							sharedPreferencesEditor.putString(
											PREF_SELECTED_QUESTION_TYPE,
											QUESTION_TYPE_RANGE);
							sharedPreferencesEditor.commit();
							rangeDialog.dismiss();
						} else {
							Toast.makeText(AddNewQuestion.this,
											getResources().getString(
															R.string.editText_validation_error_in_one_or_more)
															.toString(),
											Toast.LENGTH_LONG).show();
						}
					}
				});

				cancelButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						spinnerQuestion.setSelection(defaultSpinnerOptionPosition);
						rangeDialog.dismiss();

					}
				});
				rangeDialog.show();
			}
		});

		Button buttonOk = (Button) this
						.findViewById(R.id.activity_add_new_question_button_add_new_question_ok);
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SchemaHelper schemaHelper = new SchemaHelper(
								AddNewQuestion.this);

				String questionType = sharedPreferences.getString(
								PREF_SELECTED_QUESTION_TYPE, "none");

				// This will store in a string data according to the selected answer option
				String answerOptions = new String();

				if (questionType.equals(QUESTION_TYPE_NONE_SELECTED)) {
					Toast.makeText(AddNewQuestion.this,
									getResources().getString(
													R.string.spinner_question_error_no_option_chosen)
													.toString(),
									Toast.LENGTH_LONG).show();
					flagQuestionTypeValidation = false;
				} else if (questionType.equals(QUESTION_TYPE_RANGE)) {
					answerOptions = Integer.toString(sharedPreferences.getInt(
									PREF_MINIMUM_RANGE_VALUE, -1))
									+ "-"
									+ Integer.toString(sharedPreferences
													.getInt(PREF_MAXIMUM_RANGE_VALUE,
																	-1));
					flagQuestionTypeValidation = true;
				} else if (questionType.equals(QUESTION_TYPE_YES_NO)) {
					answerOptions = getResources()
									.getString(R.string.activity_add_new_question_yes_answer_option)
									+ "|"
									+ getResources().getString(
													R.string.activity_add_new_question_no_answer_option);
					flagQuestionTypeValidation = true;
				} else if (questionType.equals(QUESTION_TYPE_OPEN)) {
					answerOptions = new String();
					flagQuestionTypeValidation = true;
				}

				else {
					// TODO Implement other question type
					Toast.makeText(AddNewQuestion.this, "Not implemented yet",
									Toast.LENGTH_LONG).show();
					flagQuestionTypeValidation = false;
				}
				validateEditText(editTextQuestionTitle,
								validationTestAlphaWithSpace);
				validateEditText(editTextQuestionDescription,
								validationTestAlphaWithSpace);
				validateEditText(editTextQuestion, validationTestAlphaWithSpace);

				if (flagEditTextQuestionTitleValidation
								&& flagEditTextQuestionDescriptionValidation
								&& flagEditTextQuestionValidation
								&& flagQuestionTypeValidation) {
					// Get data from fields
					String questionTitle = (editTextQuestionTitle).getText()
									.toString();
					String question = (editTextQuestion).getText().toString();
					String questionDescription = (editTextQuestionDescription)
									.getText().toString();

					String intentMsg = intent
									.getStringExtra(AddNewQuestion.ADD_NEW_QUESTION_ACTIVITY_MODE);

					// Add New Question Activity Mode: add new
					if (intentMsg.equals(AddNewQuestion.ACTIVITY_MODE_ADD_NEW)) {
						// Save to database
						long dbResult = schemaHelper.addQuestion(questionTitle,
										question, questionDescription,
										questionType, answerOptions);
						if (dbResult < 0) {
							Toast.makeText(AddNewQuestion.this,
											getResources().getString(
															R.string.database_error_storing_data),
											Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(AddNewQuestion.this,
											getResources().getString(
															R.string.database_success_storing_data),
											Toast.LENGTH_LONG).show();
						}
					} else if (intentMsg
									.equals(AddNewQuestion.ACTIVITY_MODE_UPDATE)) {
						schemaHelper.updateQuestion(
										intent.getStringExtra(AddNewQuestion.ACTIVITY_MODE_DB_ITEM_ID),
										questionTitle, questionDescription,
										question, questionType, answerOptions);

					}
					// Go to ShowQuestions activity
					if (intentMsg.equals(AddNewQuestion.ACTIVITY_MODE_ADD_NEW)
									|| intentMsg.equals(AddNewQuestion.ACTIVITY_MODE_READ_ONLY)
									|| intentMsg.equals(AddNewQuestion.ACTIVITY_MODE_UPDATE)) {
						Intent launchAddNewQuestion = new Intent(
										AddNewQuestion.this,
										ShowQuestions.class);
						startActivity(launchAddNewQuestion);
					}

				}

			}
		});

		Button buttonCancel = (Button) this
						.findViewById(R.id.activity_add_new_question_button_add_new_question_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resetValidationFlags();
				Intent intent = new Intent(AddNewQuestion.this,
								ShowQuestions.class);
				startActivity(intent);

			}
		});
	}

	private void validateEditText(EditText editText,
					HashMap<Integer, String> validationTestNumbers) {

		String textToValidate = editText.getText().toString();
		String errorMessage = EditTextValidation.editTextValidation(
						textToValidate, validationTestNumbers);

		if (editText.equals(editTextMinimumValue))
			flagEditTextMinimumValidation = errorMessage == null ? true : false;
		else if (editText.equals(editTextMaximumValue))
			flagEditTextMaximumValidation = errorMessage == null ? true : false;
		else if (editText.equals(editTextQuestion))
			flagEditTextQuestionValidation = errorMessage == null ? true
							: false;
		else if (editText.equals(editTextQuestionTitle))
			flagEditTextQuestionTitleValidation = errorMessage == null ? true
							: false;
		else if (editText.equals(editTextQuestionDescription))
			flagEditTextQuestionDescriptionValidation = errorMessage == null ? true
							: false;

		if (errorMessage != null) {
			editText.setError(errorMessage);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

}
