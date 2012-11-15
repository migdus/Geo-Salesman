package com.diplomadoUNAL.geosalesman;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diplomadoUNAL.geosalesman.database.ReportTemplateTable;
import com.diplomadoUNAL.geosalesman.database.SchemaHelper;
import com.diplomadoUNAL.geosalesman.util.EditTextValidation;

public class AddNewReportTemplate extends Activity {
	public static final String ADD_NEW_REPORT_TEMPLATE_ACTIVITY_MODE = "com.diplomadoUNAL.geosalesman.com.diplomadoUNAL.geosalesman.";
	public final static String ACTIVITY_MODE_READ_ONLY = "readOnly";
	public final static String ACTIVITY_MODE_ADD_NEW = "addNew";
	public final static String ACTIVITY_MODE_UPDATE = "update";
	public final static String ACTIVITY_MODE_DB_ITEM_ID = "itemId";

	private final int REQUEST_GET_SELECTED_QUESTIONS = 3;

	private HashMap<Integer, String> validationTestAlphaWithSpace = null;
	private EditText reportTemplateName = null;
	private EditText reportTemplateDescription = null;
	private TextView reportTemplateQuestionsAdded = null;
	private boolean reportTemplateNameValidationFlag;
	private boolean reportTemplateDescriptionValidationFlag;
	private Intent receivedIntent;

	ArrayList<Integer> checkedQuestionsID = null;

	@SuppressLint("UseSparseArrays")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_report_template);

		receivedIntent = getIntent();

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
		OnFocusChangeListener onFocusChangeListenerAlphaWithSpace = new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validateEditText((EditText) v, validationTestAlphaWithSpace);
				}
			}
		};
		reportTemplateName = (EditText) AddNewReportTemplate.this
						.findViewById(R.id.report_template_name);
		reportTemplateName
						.setOnFocusChangeListener(onFocusChangeListenerAlphaWithSpace);
		reportTemplateDescription = (EditText) AddNewReportTemplate.this
						.findViewById(R.id.report_template_description);
		reportTemplateDescription
						.setOnFocusChangeListener(onFocusChangeListenerAlphaWithSpace);
		reportTemplateQuestionsAdded = (TextView) AddNewReportTemplate.this
						.findViewById(R.id.report_template_number_of_questions_added);
	}

	private void validateEditText(EditText editText,
					HashMap<Integer, String> validationTestNumbers) {

		String textToValidate = editText.getText().toString();
		String errorMessage = EditTextValidation.editTextValidation(
						textToValidate, validationTestNumbers);

		if (editText.equals(reportTemplateName))
			reportTemplateNameValidationFlag = errorMessage == null ? true
							: false;
		else if (editText.equals(reportTemplateDescription))
			reportTemplateDescriptionValidationFlag = errorMessage == null ? true
							: false;
		if (errorMessage != null) {
			editText.setError(errorMessage);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		int dbId = -1;
		if (receivedIntent
						.getStringExtra(ADD_NEW_REPORT_TEMPLATE_ACTIVITY_MODE)
						.equals(ACTIVITY_MODE_READ_ONLY)
						|| receivedIntent.getStringExtra(
										ADD_NEW_REPORT_TEMPLATE_ACTIVITY_MODE)
										.equals(ACTIVITY_MODE_UPDATE)) {
			dbId = Integer.parseInt(receivedIntent
							.getStringExtra(ACTIVITY_MODE_DB_ITEM_ID));
			// Load the previously stored data from the DB
			SchemaHelper schemaHelper = new SchemaHelper(
							getApplicationContext());
			HashMap<String, String> storedValues = schemaHelper
							.getReportTemplate(dbId);
			reportTemplateName.setText(storedValues
							.get(ReportTemplateTable.NAME));
			reportTemplateDescription.setText(storedValues
							.get(ReportTemplateTable.DESCRIPTION));
			reportTemplateQuestionsAdded
							.setText(R.string.report_template_number_of_questions_currently_added
											+ Integer.toString(schemaHelper
															.getReportTemplateQuestionByReportTemplateID(
																			dbId)
															.size()));
		}
		Button addQuestionButton = (Button) this
						.findViewById(R.id.report_template_questions_button);
		addQuestionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Setup database elements
				Intent callQuestionsIntent = new Intent(
								AddNewReportTemplate.this, CRUDObject.class);
				callQuestionsIntent
								.putExtra(CRUDObject.QUERY_SOURCE,
												CRUDObject.REPORT_TEMPLATE_QUESTIONS)
								.putExtra(CRUDObject.ADD_MENU_ITEM_ENABLED,
												false)
								.putExtra(CRUDObject.SELECT_BUTTON_ENABLED,
												true)
								.putExtra(CRUDObject.DELETE_BUTTON_ENABLED,
												false)
								.putExtra(CRUDObject.CANCEL_BUTTON_ENABLED,
												true)
								.putExtra(CRUDObject.SET_DISPLAYED_ITEMS_CHECKED,
												true)

								.putExtra(CRUDObject.ACTIVITY_TITLE,
												getResources().getString(
																R.string.activity_crud_questions_title));
				if (receivedIntent.getStringExtra(
								ADD_NEW_REPORT_TEMPLATE_ACTIVITY_MODE).equals(
								ACTIVITY_MODE_READ_ONLY)
								|| receivedIntent
												.getStringExtra(ADD_NEW_REPORT_TEMPLATE_ACTIVITY_MODE)
												.equals(ACTIVITY_MODE_UPDATE))
					callQuestionsIntent
									.putExtra(CRUDObject.ACTIVITY_MODE_DB_ITEM_ID,
													receivedIntent.getStringExtra(ACTIVITY_MODE_DB_ITEM_ID));
				// start intent activity
				startActivityForResult(callQuestionsIntent,
								REQUEST_GET_SELECTED_QUESTIONS);
			}
		});
		Button cancelButton = (Button) this.findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Button okButton = (Button) this.findViewById(R.id.ok_button);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SchemaHelper schemaHelper = new SchemaHelper(
								AddNewReportTemplate.this);

				// This will store in a string data according to the selected answer option
				String answerOptions = new String();
				validateEditText(reportTemplateName,
								validationTestAlphaWithSpace);
				validateEditText(reportTemplateDescription,
								validationTestAlphaWithSpace);

				if (reportTemplateDescriptionValidationFlag
								&& reportTemplateNameValidationFlag) {
					// Get data from fields
					String reportTemplateNameData = reportTemplateName
									.getText().toString();
					String reportTemplateDescriptionData = reportTemplateDescription
									.getText().toString();

					// Add New Question Activity Mode: add new
					if (receivedIntent
									.getStringExtra(AddNewReportTemplate.ADD_NEW_REPORT_TEMPLATE_ACTIVITY_MODE)
									.equals(AddNewQuestion.ACTIVITY_MODE_ADD_NEW)) {

						// Save to database
						long dbResult = schemaHelper.addReportTemplate(
										checkedQuestionsID,
										reportTemplateNameData,
										reportTemplateDescriptionData);

						if (dbResult < 0) {
							Toast.makeText(AddNewReportTemplate.this,
											getResources().getString(
															R.string.database_error_storing_data),
											Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(AddNewReportTemplate.this,
											getResources().getString(
															R.string.database_success_storing_data),
											Toast.LENGTH_LONG).show();
						}
						// Go to previous activity
						finish();
					} else if (receivedIntent
									.getStringExtra(AddNewReportTemplate.ADD_NEW_REPORT_TEMPLATE_ACTIVITY_MODE)
									.equals(AddNewQuestion.ACTIVITY_MODE_UPDATE)) {
						long result = schemaHelper.updateReportTemplate(
										receivedIntent.getStringExtra(AddNewReportTemplate.ACTIVITY_MODE_DB_ITEM_ID),
										reportTemplateNameData,
										reportTemplateDescriptionData,
										checkedQuestionsID);
						if (result > 0)
							Toast.makeText(AddNewReportTemplate.this,
											getResources().getString(
															R.string.database_success_updating_data),
											Toast.LENGTH_LONG).show();
						else
							Toast.makeText(AddNewReportTemplate.this,
											getResources().getString(
															R.string.database_error_storing_data),
											Toast.LENGTH_LONG).show();
						// Go to previous activity
						finish();
					}
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
					Intent receivedData) {

		super.onActivityResult(requestCode, resultCode, receivedData);
		switch (requestCode) {

		case REQUEST_GET_SELECTED_QUESTIONS:
			checkedQuestionsID = new ArrayList<Integer>();
			ArrayList<HashMap<String, String>> temp = (ArrayList<HashMap<String, String>>) receivedData
							.getSerializableExtra("checkedItems");
			for (int i = 0; i < temp.size(); i++) {
				HashMap<String, String> hashMap = temp.get(i);
				checkedQuestionsID.add(Integer.parseInt(hashMap.get("dbId")));
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater()
						.inflate(R.menu.activity_add_new_report_template, menu);
		return true;
	}
}
